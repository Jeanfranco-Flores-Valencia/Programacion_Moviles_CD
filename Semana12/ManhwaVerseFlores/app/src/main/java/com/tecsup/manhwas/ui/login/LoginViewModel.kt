package com.tecsup.manhwas.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.manhwas.data.auth.AuthRepository
import com.tecsup.manhwas.domain.model.Sesion
import com.tecsup.manhwas.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Sesion de la app. Exito(null) = no hay sesion (se muestra el login);
 * Exito(sesion) = ya entro y el rol decide que pantallas ve.
 */
class LoginViewModel(private val auth: AuthRepository) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    /** false mientras se revisa si quedo una sesion abierta (la app muestra Cargando). */
    private val _verificado = MutableStateFlow(false)
    val verificado: StateFlow<Boolean> = _verificado.asStateFlow()

    init {
        viewModelScope.launch {
            _estado.value = UiState.Exito(auth.sesionActual())
            _verificado.value = true
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        val c = correo.trim()
        // Campos vacios: se valida aqui y no se llama a Firebase
        if (c.isEmpty() || clave.isEmpty()) {
            _estado.value = UiState.Error("Completa el correo y la contraseña")
            return
        }
        if (!REGEX_CORREO.matches(c)) {
            _estado.value = UiState.Error("El correo no tiene un formato válido")
            return
        }
        _estado.value = UiState.Cargando
        viewModelScope.launch {
            auth.iniciarSesion(c, clave)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(it.message ?: "No se pudo iniciar sesión") }
        }
    }

    fun limpiarError() {
        if (_estado.value is UiState.Error) _estado.value = UiState.Exito(null)
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _estado.value = UiState.Exito(null)
    }

    private companion object {
        val REGEX_CORREO = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    }
}
