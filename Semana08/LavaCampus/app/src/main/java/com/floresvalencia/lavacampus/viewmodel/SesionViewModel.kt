package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.entity.Usuario
import com.floresvalencia.lavacampus.data.prefs.PreferenciasStore
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Sesion de la app: usuario actual, perfil activo y preferencias (DataStore).
 * Vive a nivel de la Activity y lo comparten ambos perfiles.
 */
class SesionViewModel(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /** true mientras se lee la sesion guardada (lo usa el Splash). */
    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        viewModelScope.launch { restaurarSesion() }
    }

    /** Si ya hay un perfil guardado, se recupera el usuario y se salta el login. */
    private suspend fun restaurarSesion() {
        val rol = Rol.desde(prefs.perfilActivo.first())
        val id = prefs.usuarioId.first()
        if (rol != null && id > 0) {
            val usuario = repo.obtenerUsuario(id)
            if (usuario != null && usuario.rol == rol.name) _usuarioActual.value = usuario
        }
        _cargando.value = false
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        val correoLimpio = correo.trim()
        if (correoLimpio.isEmpty() || clave.isEmpty()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correoLimpio, clave)
            when {
                usuario == null ->
                    _error.value = "Correo o contraseña incorrectos"

                usuario.rol != rolSeleccionado.name ->
                    _error.value = if (rolSeleccionado == Rol.ADMIN)
                        "Esta cuenta no es de administrador. Usa la pestaña Estudiante"
                    else
                        "Esta cuenta es de administrador. Usa la pestaña Administrador"

                else -> {
                    prefs.guardarSesion(usuario.rol, usuario.id)
                    _error.value = null
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    /** Modo practica: guarda el otro perfil y avisa a la vista para que navegue a su grafo. */
    fun cambiarPerfil(alCambiar: (Rol) -> Unit) {
        val actual = _usuarioActual.value ?: return
        val otroRol = if (actual.rol == Rol.ADMIN.name) Rol.CLIENTE else Rol.ADMIN
        viewModelScope.launch {
            val otro = repo.primerUsuarioDeRol(otroRol)
            if (otro == null) {
                _error.value = "No hay un usuario con el perfil ${otroRol.etiqueta}"
                return@launch
            }
            prefs.guardarSesion(otro.rol, otro.id)
            _usuarioActual.value = otro
            alCambiar(otroRol)
        }
    }

    fun cerrarSesion(alCerrar: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
            alCerrar()
        }
    }

    fun cambiarModoOscuro(b: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(b) }
    }

    fun cambiarNotificaciones(b: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(b) }
    }

    fun cambiarRecibirPedidos(b: Boolean) {
        viewModelScope.launch { prefs.guardarRecibirPedidos(b) }
    }
}
