package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: LavaRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Servicio que se esta editando en el formulario (null = nuevo). */
    private val _editando = MutableStateFlow<Servicio?>(null)
    val editando: StateFlow<Servicio?> = _editando.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** true cuando se guardo o elimino: el formulario se cierra. */
    private val _terminado = MutableStateFlow(false)
    val terminado: StateFlow<Boolean> = _terminado.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    /** Carga el servicio a editar (id = 0 es un servicio nuevo). */
    fun cargar(id: Int) {
        if (id == 0) return
        viewModelScope.launch {
            _editando.value = repo.observarServicios().first().firstOrNull { it.id == id }
        }
    }

    /** Guarda un servicio nuevo o editado. Nombre obligatorio y precio mayor que 0. */
    fun guardar(servicio: Servicio) {
        val limpio = servicio.copy(nombre = servicio.nombre.trim(), descripcion = servicio.descripcion.trim())
        when {
            limpio.nombre.isEmpty() -> _mensaje.value = "El nombre es obligatorio"
            limpio.precio <= 0.0 -> _mensaje.value = "El precio debe ser mayor que 0"
            limpio.horasEntrega !in listOf(24, 48, 72) -> _mensaje.value = "Elige 24, 48 o 72 h"
            else -> viewModelScope.launch {
                repo.guardarServicio(limpio)
                _mensaje.value = null
                _terminado.value = true
            }
        }
    }

    /** Un servicio que ya aparece en algun pedido no se elimina: solo se desactiva. */
    fun eliminar(servicio: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneDetalles(servicio.id)) {
                _mensaje.value = "No se puede eliminar: el servicio ya está en pedidos. Desactívalo"
            } else {
                repo.eliminarServicio(servicio)
                _terminado.value = true
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
