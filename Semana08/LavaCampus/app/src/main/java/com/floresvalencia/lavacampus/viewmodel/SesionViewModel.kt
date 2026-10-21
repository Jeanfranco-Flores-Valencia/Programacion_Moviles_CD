package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floresvalencia.lavacampus.data.local.entity.Usuario
import com.floresvalencia.lavacampus.data.prefs.PreferenciasStore
import com.floresvalencia.lavacampus.data.repository.LavaRepository
import com.floresvalencia.lavacampus.model.Rol
import com.floresvalencia.lavacampus.model.EstadoPedido
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Campos del formulario de registro (para limpiar el error de cada uno). */
enum class CampoRegistro { NOMBRE, CODIGO, CORREO, CLAVE, CONFIRMAR }

/** Error de cada campo del registro (null = correcto). */
data class ErroresRegistro(
    val nombre: String? = null,
    val codigo: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmar: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, codigo, correo, clave, confirmar).any { it != null }
}

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

    /**
     * Mejora IA: numero que se muestra como insignia en "Pedidos" de la barra inferior.
     * Estudiante: sus pedidos activos. Administrador: pedidos RECIBIDOS por atender.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val insigniaPedidos: StateFlow<Int> = _usuarioActual
        .flatMapLatest { u ->
            when {
                u == null -> flowOf(0)
                u.rol == Rol.ADMIN.name -> repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO)
                else -> repo.observarPedidosDeUsuario(u.id)
                    .map { lista -> lista.count { EstadoPedido.desde(it.pedido.estado).esActivo } }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // ---------- Registro ----------
    private val _erroresRegistro = MutableStateFlow(ErroresRegistro())
    val erroresRegistro: StateFlow<ErroresRegistro> = _erroresRegistro.asStateFlow()

    /** true cuando la cuenta se creo: la vista navega al login. */
    private val _registroOk = MutableStateFlow(false)
    val registroOk: StateFlow<Boolean> = _registroOk.asStateFlow()

    /** Correo recien registrado: el login lo muestra ya escrito. */
    private val _correoRegistrado = MutableStateFlow<String?>(null)
    val correoRegistrado: StateFlow<String?> = _correoRegistrado.asStateFlow()

    private val _registrando = MutableStateFlow(false)
    val registrando: StateFlow<Boolean> = _registrando.asStateFlow()

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
        val correoLimpio = correo.trim().lowercase()
        if (correoLimpio.isEmpty() || clave.isEmpty()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        if (!REGEX_CORREO.matches(correoLimpio)) {
            _error.value = "Ingresa un correo válido"
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
                    _correoRegistrado.value = null
                    _usuarioActual.value = usuario
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    /**
     * Registro de un estudiante. Valida cada campo; si todo esta bien, crea la cuenta
     * y la vista lleva al login para que ingrese con sus datos.
     */
    fun registrar(nombre: String, codigo: String, correo: String, clave: String, confirmar: String) {
        if (_registrando.value) return
        val n = nombre.trim().replace(Regex("\\s+"), " ")
        val c = codigo.trim().uppercase()
        val e = correo.trim().lowercase()

        val errores = ErroresRegistro(
            nombre = when {
                n.isEmpty() -> "Ingresa tu nombre completo"
                !n.all { it.isLetter() || it == ' ' } -> "Usa solo letras y espacios"
                n.split(" ").size < 2 -> "Ingresa tu nombre y apellido"
                n.split(" ").any { it.length < 2 } -> "Nombre o apellido demasiado corto"
                else -> null
            },
            codigo = when {
                c.isEmpty() -> "Ingresa tu código de estudiante"
                !REGEX_CODIGO.matches(c) -> "Formato: U + 9 dígitos (ej. U202310234)"
                else -> null
            },
            correo = when {
                e.isEmpty() -> "Ingresa tu correo"
                !REGEX_CORREO.matches(e) -> "Correo no válido (ej. nombre@correo.test)"
                else -> null
            },
            clave = when {
                clave.isEmpty() -> "Crea una contraseña"
                clave.length < 6 -> "Mínimo 6 caracteres"
                !clave.any { it.isLetter() } || !clave.any { it.isDigit() } -> "Usa letras y números"
                clave.contains(' ') -> "No uses espacios"
                else -> null
            },
            confirmar = when {
                confirmar.isEmpty() -> "Repite la contraseña"
                confirmar != clave -> "Las contraseñas no coinciden"
                else -> null
            }
        )
        _erroresRegistro.value = errores
        if (errores.hayErrores) return

        _registrando.value = true
        viewModelScope.launch {
            try {
                val correoUsado = repo.correoRegistrado(e)
                val codigoUsado = repo.codigoRegistrado(c)
                if (correoUsado || codigoUsado) {
                    _erroresRegistro.value = ErroresRegistro(
                        correo = if (correoUsado) "Este correo ya tiene una cuenta" else null,
                        codigo = if (codigoUsado) "Este código ya está registrado" else null
                    )
                    return@launch
                }
                repo.registrarEstudiante(nombre = n, codigo = c, correo = e, clave = clave)
                _correoRegistrado.value = e
                _registroOk.value = true
            } catch (ex: Exception) {
                _erroresRegistro.value = ErroresRegistro(correo = "No se pudo crear la cuenta. Intenta de nuevo")
            } finally {
                _registrando.value = false
            }
        }
    }

    /** Limpia el error del campo que el usuario esta corrigiendo. */
    fun limpiarCampo(campo: CampoRegistro) {
        val actual = _erroresRegistro.value
        _erroresRegistro.value = when (campo) {
            CampoRegistro.NOMBRE -> actual.copy(nombre = null)
            CampoRegistro.CODIGO -> actual.copy(codigo = null)
            CampoRegistro.CORREO -> actual.copy(correo = null)
            CampoRegistro.CLAVE -> actual.copy(clave = null)
            CampoRegistro.CONFIRMAR -> actual.copy(confirmar = null)
        }
    }

    /** La vista ya navego al login despues del registro. */
    fun registroAtendido() {
        _registroOk.value = false
        _erroresRegistro.value = ErroresRegistro()
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

    companion object {
        val REGEX_CORREO = Regex("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
        /** Codigo de estudiante: U seguido de 9 digitos (ej. U202310234). */
        val REGEX_CODIGO = Regex("^U\\d{9}$")
    }
}
