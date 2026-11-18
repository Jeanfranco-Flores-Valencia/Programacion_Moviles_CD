package com.floresv.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresv.centrotareas.data.TareasRepository
import com.floresv.centrotareas.model.CargaUi
import com.floresv.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {

    // ----- Tarea con progreso (pasos 1 a 4)
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    /** Avance de la tarea de 0f a 1f. La pantalla solo lo lee. */
    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    /** Corrutina de la tarea en curso: se guarda para poder cancelarla. */
    private var trabajo: Job? = null

    /** true mientras la tarea trabaja (termine bien o la cancelen, vuelve a false). */
    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    /** La tarea larga corre en segundo plano: la pantalla sigue respondiendo. */
    fun tareaLarga() {
        if (_corriendo.value) return   // evita lanzar dos a la vez
        _corriendo.value = true
        trabajo = viewModelScope.launch {
            try {
                _progreso.value = 0f
                _mensaje.value = "Trabajando..."
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                // Mejora IA: el avance llega del Repository como Flow; aqui solo se publica
                repo.progresoTarea().collect { _progreso.value = it }
                Log.d("HILO", "Despues: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } finally {
                _corriendo.value = false
            }
        }
    }

    /** Detiene la tarea a mitad de camino. delay revisa la cancelacion, la barra se queda donde estaba. */
    fun cancelar() {
        if (!_corriendo.value) return
        trabajo?.cancel()
        _mensaje.value = "Cancelado"
    }

    // ----- Contar primos (paso 5)
    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private var trabajoPrimos: Job? = null

    /** Cuenta los primos en el Repository (segundo plano) y mide cuanto tarda. */
    fun contar(hasta: Int) {
        if (hasta < 2 || _primos.value is PrimosUi.Calculando) return
        trabajoPrimos = viewModelScope.launch {
            _primos.value = PrimosUi.Calculando
            val inicio = System.currentTimeMillis()
            try {
                val cantidad = repo.contarPrimos(hasta)
                _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e   // se vuelve a lanzar para no romper la cancelacion
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    // ----- Dos cargas (pasos 6 y 7)
    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    /** Interruptor "Simular error": los pedidos fallan con IOException("Sin conexion"). */
    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    /** Perfil y despues pedidos: los tiempos se suman (unos 5 s). */
    fun cargarUnaPorUna() = ejecutarCarga("Una por una") {
        val perfil = repo.cargarPerfil()
        val pedidos = repo.cargarPedidos(_simularError.value)
        Pair(perfil, pedidos)
    }

    /** Las dos cargas al mismo tiempo con async: tarda lo de la mas lenta (unos 3 s). */
    fun cargarALaVez() = ejecutarCarga("A la vez") {
        coroutineScope {
            val a = async { repo.cargarPerfil() }
            val b = async { repo.cargarPedidos(_simularError.value) }
            Pair(a.await(), b.await())
        }
    }

    /** Comun a las dos cargas: estado Cargando, mide el tiempo y atrapa los errores. */
    private fun ejecutarCarga(modo: String, cargar: suspend () -> Pair<String, List<String>>) {
        if (_carga.value is CargaUi.Cargando) return
        viewModelScope.launch {
            _carga.value = CargaUi.Cargando
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = cargar()
                _carga.value = CargaUi.Exito(modo, perfil, pedidos.size, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                throw e   // la cancelacion no es un error: se deja pasar
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: " + (e.message ?: "error desconocido"))
            }
        }
    }
}
