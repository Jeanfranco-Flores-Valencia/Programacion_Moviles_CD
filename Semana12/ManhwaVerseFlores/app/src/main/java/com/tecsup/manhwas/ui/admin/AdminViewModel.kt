package com.tecsup.manhwas.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.manhwas.data.repository.ItemRepository
import com.tecsup.manhwas.notification.Notificador
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Totales del panel del administrador. */
data class Totales(val guardados: Int, val favoritos: Int)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Totales>>(UiState.Cargando)
    val estado: StateFlow<UiState<Totales>> = _estado.asStateFlow()

    /** Resultado de la ultima accion (sincronizar o limpiar). */
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _ocupado = MutableStateFlow(false)
    val ocupado: StateFlow<Boolean> = _ocupado.asStateFlow()

    init {
        cargarTotales()
    }

    private fun cargarTotales() {
        viewModelScope.launch {
            _estado.value = UiState.Exito(Totales(repo.cantidadGuardada(), repo.cantidadFavoritos()))
        }
    }

    fun sincronizar() {
        if (_ocupado.value) return
        _ocupado.value = true
        viewModelScope.launch {
            repo.refrescar()
                .onSuccess { n ->
                    _mensaje.value = "Se guardaron $n elementos"
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { _mensaje.value = mensajeDe(it) }
            _ocupado.value = false
            cargarTotales()
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repo.limpiarCache()
            _mensaje.value = "Caché eliminada: la lista quedó vacía"
            cargarTotales()
        }
    }
}
