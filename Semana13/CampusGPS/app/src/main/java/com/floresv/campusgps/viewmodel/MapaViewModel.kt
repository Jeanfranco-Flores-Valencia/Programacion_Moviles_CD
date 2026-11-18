package com.floresv.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresv.campusgps.data.LugaresRepository
import com.floresv.campusgps.data.PreferenciasRepository
import com.floresv.campusgps.data.UbicacionNoDisponibleException
import com.floresv.campusgps.data.UbicacionRepository
import com.floresv.campusgps.model.Lugar
import com.floresv.campusgps.model.LugarConDistancia
import com.floresv.campusgps.model.Posicion
import com.floresv.campusgps.model.TipoError
import com.floresv.campusgps.model.UbicacionUi
import com.floresv.campusgps.util.distanciaMetros
import com.floresv.campusgps.util.formatearDistancia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository,
    private val preferenciasRepo: PreferenciasRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado: StateFlow<UbicacionUi> = _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map { LugarConDistancia(it, null) }
    )
    val lugares: StateFlow<List<LugarConDistancia>> = _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)
    val seleccionado: StateFlow<Int?> = _seleccionado.asStateFlow()

    init {
        // Mejora IA: al abrir la app se recupera el ultimo lugar visitado (el mapa viaja hasta el)
        viewModelScope.launch {
            val ultimo = preferenciasRepo.ultimoLugar.first()
            if (ultimo != null && _seleccionado.value == null) _seleccionado.value = ultimo
        }
    }

    fun buscar() {
        // Sin Google Play Services no hay ubicacion: no tiene sentido reintentar
        if (!ubicacionRepo.playServicesDisponible()) {
            _estado.value = UbicacionUi.Error("Este dispositivo no tiene Google Play Services", TipoError.SIN_PLAY_SERVICES)
            return
        }
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess {
                    _estado.value = UbicacionUi.Lista(it)
                    actualizarDistancias(it)
                }
                .onFailure { _estado.value = errorDe(it) }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error("Sin permiso de ubicación no puedo mostrar dónde estás", TipoError.SIN_PERMISO)
    }

    /** Calcula la distancia a cada lugar y ordena del mas cercano al mas lejano. */
    private fun actualizarDistancias(pos: Posicion) {
        val ordenados = lugaresRepo.obtenerLugares()
            .map { LugarConDistancia(it, distanciaMetros(pos, it)) }
            .sortedBy { it.metros }
        _lugares.value = ordenados
        ordenados.forEach { Log.d("DISTANCIA", "${it.lugar.nombre}: ${it.metros?.let(::formatearDistancia)}") }
    }

    /** Lugar elegido en la lista (null = ninguno). La pantalla mueve la camara hasta el. */
    fun seleccionar(id: Int?) {
        _seleccionado.value = id
        // Mejora IA: se guarda como ultimo lugar visitado
        if (id != null) viewModelScope.launch { preferenciasRepo.guardarUltimoLugar(id) }
    }
    // ---------- Mejora IA: filtro por categoria ----------
    /** Categorias para los chips (Estudio, Comida, Eventos, Servicios), en el orden del repositorio. */
    val categorias: List<String> = lugaresRepo.obtenerLugares().map { it.categoria }.distinct()

    /** Categoria elegida; null = todas. */
    private val _categoria = MutableStateFlow<String?>(null)
    val categoria: StateFlow<String?> = _categoria.asStateFlow()

    /** Lo que se ve en el mapa y en la lista: los lugares de la categoria elegida, ya ordenados. */
    val visibles: StateFlow<List<LugarConDistancia>> = combine(_lugares, _categoria, ::filtrar)
        .stateIn(viewModelScope, SharingStarted.Eagerly, _lugares.value)

    private fun filtrar(lista: List<LugarConDistancia>, cat: String?) =
        if (cat == null) lista else lista.filter { it.lugar.categoria == cat }

    /** Tocar la categoria ya elegida la quita (vuelve a Todas). */
    fun elegirCategoria(nueva: String?) {
        _categoria.value = if (_categoria.value == nueva) null else nueva
        // Si el lugar seleccionado queda oculto, se deselecciona
        if (filtrar(_lugares.value, _categoria.value).none { it.lugar.id == _seleccionado.value }) _seleccionado.value = null
    }

    // ---------- Mejora IA: radio de cercania ----------
    val opcionesRadio = listOf(100, 200, 500)

    /** Radio elegido en metros; null = sin radio. */
    private val _radio = MutableStateFlow<Int?>(null)
    val radio: StateFlow<Int?> = _radio.asStateFlow()

    /** Ids de los lugares que quedan dentro del radio (se recalcula al buscar o al cambiar el radio). */
    val dentroDelRadio: StateFlow<Set<Int>> = combine(_lugares, _radio) { lista, r ->
        if (r == null) emptySet()
        else lista.filter { it.metros != null && it.metros <= r }.map { it.lugar.id }.toSet()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

    /** Tocar el radio ya elegido lo quita. */
    fun elegirRadio(metros: Int) {
        _radio.value = if (_radio.value == metros) null else metros
    }

    /** El primero visible con distancia calculada (la lista ya esta ordenada). Null antes de buscar. */
    val masCercano: StateFlow<Lugar?> = visibles
        .map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private fun errorDe(e: Throwable): UbicacionUi.Error = when (e) {
        is SecurityException -> UbicacionUi.Error("Falta el permiso de ubicación", TipoError.SIN_PERMISO)
        is UbicacionNoDisponibleException -> UbicacionUi.Error("Activa el GPS e intenta de nuevo", TipoError.GPS_APAGADO)
        else -> UbicacionUi.Error(e.message ?: "No se pudo obtener la ubicación", TipoError.OTRO)
    }
}
