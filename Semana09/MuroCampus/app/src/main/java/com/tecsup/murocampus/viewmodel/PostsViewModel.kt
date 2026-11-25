package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Lo que muestra la pantalla de detalle: la publicacion y sus comentarios. */
data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

/** Solo conoce la interface PostRepository: no sabe de la capa de red ni recibe un Context. */
class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    // ----- Lista (paso 6) y filtro por usuario (paso 8)
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    /** Usuario elegido en los chips. null = Todos. */
    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private var trabajoLista: Job? = null

    init {
        cargar()
    }

    /** Pide la lista (toda o la del usuario filtrado): Cargando, y luego Exito o Error. */
    fun cargar() {
        trabajoLista?.cancel()   // si habia una carga en curso, se reemplaza
        trabajoLista = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val filtro = _usuarioFiltro.value
            val resultado = if (filtro == null) repo.listar() else repo.porUsuario(filtro)
            resultado
                .onSuccess { _lista.value = UiState.Exito(it) }
                .onFailure { _lista.value = UiState.Error(mensajeDe(it)) }
        }
    }

    // ----- Mejora IA: deslizar para actualizar y avisos
    /** true mientras se actualiza la lista sin quitar la que ya se ve. */
    private val _refrescando = MutableStateFlow(false)
    val refrescando: StateFlow<Boolean> = _refrescando.asStateFlow()

    /** Mensaje corto para el Snackbar (null = nada que mostrar). */
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    fun avisoMostrado() {
        _aviso.value = null
    }

    /** Vuelve a pedir la lista dejando la actual en pantalla; si falla, solo avisa. */
    fun refrescar() {
        if (_refrescando.value) return
        trabajoLista?.cancel()
        trabajoLista = viewModelScope.launch {
            _refrescando.value = true
            try {
                val filtro = _usuarioFiltro.value
                (if (filtro == null) repo.listar() else repo.porUsuario(filtro))
                    .onSuccess { _lista.value = UiState.Exito(it) }
                    .onFailure { _aviso.value = "No se pudo actualizar: " + mensajeDe(it).lineSequence().first() }
            } finally {
                _refrescando.value = false
            }
        }
    }

    /** Guarda el filtro y recarga: null llama a listar() y un numero a porUsuario(userId). */
    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    // ----- Detalle con comentarios (paso 7)
    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private var trabajoDetalle: Job? = null
    private var idDetalle: Int? = null

    /**
     * Pide la publicacion y sus comentarios a la vez (async). Si una de las dos falla, todo es Error.
     * Si ese detalle ya esta cargado o cargando, no lo vuelve a pedir (por ejemplo, al rotar).
     */
    fun abrir(id: Int) {
        if (id == idDetalle && (trabajoDetalle?.isActive == true || _detalle.value is UiState.Exito)) return
        idDetalle = id
        trabajoDetalle?.cancel()
        trabajoDetalle = viewModelScope.launch {
            _detalle.value = UiState.Cargando
            val (post, comentarios) = coroutineScope {
                val p = async { repo.detalle(id) }
                val c = async { repo.comentarios(id) }
                p.await() to c.await()
            }
            val error = post.exceptionOrNull() ?: comentarios.exceptionOrNull()
            _detalle.value =
                if (error != null) UiState.Error(mensajeDe(error))
                else UiState.Exito(DetalleUi(post.getOrThrow(), comentarios.getOrThrow()))
        }
    }

    // ----- Crear publicacion (paso 9)
    /** null = todavia no se envio nada. */
    private val _publicacion = MutableStateFlow<UiState<Post>?>(null)
    val publicacion: StateFlow<UiState<Post>?> = _publicacion.asStateFlow()

    /** Envia la publicacion nueva con POST. JSONPlaceholder responde con id 101 pero no la guarda. */
    fun publicar(titulo: String, texto: String) {
        if (_publicacion.value is UiState.Cargando) return
        viewModelScope.launch {
            _publicacion.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo.trim(), body = texto.trim()))
                .onSuccess { _publicacion.value = UiState.Exito(it) }
                .onFailure { _publicacion.value = UiState.Error(mensajeDe(it)) }
        }
    }

    /** Al salir de la pantalla Crear se borra el mensaje anterior. */
    fun reiniciarPublicacion() {
        _publicacion.value = null
    }
}
