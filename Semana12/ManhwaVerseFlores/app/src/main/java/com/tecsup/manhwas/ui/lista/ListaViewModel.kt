package com.tecsup.manhwas.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.manhwas.data.repository.ItemRepository
import com.tecsup.manhwas.domain.model.Item
import com.tecsup.manhwas.notification.Notificador
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.mensajeDe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    /** Aviso sobre la lista, ej. "Sin conexion: mostrando datos guardados". */
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _refrescando = MutableStateFlow(false)
    val refrescando: StateFlow<Boolean> = _refrescando.asStateFlow()

    private var refrescoTerminado = false

    init {
        // La lista siempre sale de Room. Mientras Room este vacio y no termine el primer
        // refresco, se queda en Cargando (asi no aparece "vacio" antes de que responda la API).
        viewModelScope.launch {
            repo.items.collect { lista ->
                if (lista.isNotEmpty() || refrescoTerminado) _estado.value = UiState.Exito(lista)
            }
        }
        refrescar()
    }

    fun refrescar() {
        if (_refrescando.value) return
        viewModelScope.launch {
            // Reintentar desde la pantalla de error vuelve a mostrar Cargando
            if (_estado.value is UiState.Error) _estado.value = UiState.Cargando
            _refrescando.value = true
            val resultado = repo.refrescar()
            refrescoTerminado = true
            _refrescando.value = false
            resultado
                .onSuccess { n ->
                    if (n == 0 && _estado.value is UiState.Cargando) _estado.value = UiState.Exito(emptyList())
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    if (repo.cantidadGuardada() == 0) _estado.value = UiState.Error(mensajeDe(e))
                    else _aviso.value = "Sin conexión: mostrando datos guardados"
                }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }
}
