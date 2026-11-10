package com.tecsup.manhwas.ui.lista

import com.tecsup.manhwas.data.repository.ItemRepository
import com.tecsup.manhwas.domain.model.Item
import com.tecsup.manhwas.notification.Notificador
import com.tecsup.manhwas.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

/** Repositorio falso: guarda los items en memoria y se le dice que debe responder refrescar(). */
private class RepositorioFalso(
    inicial: List<Item> = emptyList(),
    private val alRefrescar: suspend RepositorioFalso.() -> Result<Int>
) : ItemRepository {
    val datos = MutableStateFlow(inicial)
    var favoritosAlternados = mutableListOf<Int>()

    override val items: Flow<List<Item>> = datos
    override val favoritos: Flow<List<Item>> = datos.map { lista -> lista.filter { it.esFavorito } }
    override suspend fun refrescar(): Result<Int> = alRefrescar()
    override suspend fun obtenerDetalle(id: Int): Result<Item> = Result.failure(IOException())
    override suspend fun alternarFavorito(id: Int) {
        favoritosAlternados += id
        datos.value = datos.value.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
    }
    override suspend fun cantidadGuardada(): Int = datos.value.size
    override suspend fun cantidadFavoritos(): Int = datos.value.count { it.esFavorito }
    override suspend fun limpiarCache() { datos.value = emptyList() }
}

/** Notificador falso: en lugar de mostrar la notificacion, la anota. */
private class NotificadorFalso : Notificador {
    val mostradas = mutableListOf<Pair<String, String>>()
    override fun mostrar(titulo: String, texto: String) { mostradas += titulo to texto }
}

private fun manhwa(id: Int, titulo: String, generos: String = "Acción · Fantasía") =
    Item(id = id, titulo = titulo, subtitulo = generos, imagenUrl = null, puntaje = 8.0, capitulos = 100, estado = "Finalizado")

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    // viewModelScope usa Dispatchers.Main: en las pruebas se reemplaza por uno que corre al instante
    @Before fun antes() = Dispatchers.setMain(UnconfinedTestDispatcher())
    @After fun despues() = Dispatchers.resetMain()

    @Test
    fun `refresco exitoso muestra la lista y avisa cuantos guardo`() {
        val descargados = listOf(manhwa(1, "Solo Leveling"), manhwa(2, "Dungeon Reset"))
        val repo = RepositorioFalso { datos.value = descargados; Result.success(descargados.size) }
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)

        assertEquals(UiState.Exito(descargados), vm.estado.value)
        assertEquals(listOf("Datos actualizados" to "Se guardaron 2 elementos"), notificador.mostradas)
        assertNull(vm.aviso.value)
    }

    @Test
    fun `sin internet y sin datos guardados muestra error para reintentar`() {
        val repo = RepositorioFalso { Result.failure(IOException("sin red")) }
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)

        assertEquals(UiState.Error("No hay conexión a internet"), vm.estado.value)
        assertTrue(notificador.mostradas.isEmpty())
    }

    @Test
    fun `sin internet con datos guardados muestra lo guardado con aviso`() {
        val guardados = listOf(manhwa(1, "Solo Leveling"))
        val repo = RepositorioFalso(inicial = guardados) { Result.failure(IOException("sin red")) }

        val vm = ListaViewModel(repo, NotificadorFalso())

        assertEquals(UiState.Exito(guardados), vm.estado.value)
        assertEquals("Sin conexión: mostrando datos guardados", vm.aviso.value)
    }

    @Test
    fun `reintentar despues de un error vuelve a cargar la lista`() {
        var hayInternet = false
        val descargados = listOf(manhwa(1, "Solo Leveling"))
        val repo = RepositorioFalso {
            if (hayInternet) { datos.value = descargados; Result.success(1) } else Result.failure(IOException())
        }
        val vm = ListaViewModel(repo, NotificadorFalso())
        assertTrue(vm.estado.value is UiState.Error)

        hayInternet = true
        vm.refrescar()

        assertEquals(UiState.Exito(descargados), vm.estado.value)
    }

    @Test
    fun `busqueda y genero filtran los visibles`() {
        val descargados = listOf(
            manhwa(1, "Solo Leveling", "Acción · Fantasía"),
            manhwa(2, "Omniscient Reader", "Acción · Apocalipsis"),
            manhwa(3, "Dungeon Reset", "Fantasía · Isekai")
        )
        val vm = ListaViewModel(RepositorioFalso { datos.value = descargados; Result.success(3) }, NotificadorFalso())

        vm.buscar("solo")
        assertEquals(listOf(1), vm.visibles.value.map { it.id })

        vm.buscar("")
        vm.elegirGenero("Apocalipsis")
        assertEquals(listOf(2), vm.visibles.value.map { it.id })

        vm.elegirGenero("Apocalipsis") // tocarlo otra vez lo quita
        assertEquals(3, vm.visibles.value.size)
    }

    @Test
    fun `marcar favorito se guarda en el repositorio`() {
        val repo = RepositorioFalso(inicial = listOf(manhwa(1, "Solo Leveling"))) { Result.success(0) }
        val vm = ListaViewModel(repo, NotificadorFalso())

        vm.alternarFavorito(1)

        assertEquals(listOf(1), repo.favoritosAlternados)
        assertTrue((vm.estado.value as UiState.Exito).datos.first().esFavorito)
    }
}
