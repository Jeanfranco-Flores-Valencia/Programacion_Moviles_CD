package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Comentario
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

/** Solo conoce la interface PostRepository: nada de Retrofit, PostApi, HttpException ni Context. */
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
}
