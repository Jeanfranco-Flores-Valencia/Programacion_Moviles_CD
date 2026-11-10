package com.tecsup.manhwas.data.repository

import com.tecsup.manhwas.data.local.ItemDao
import com.tecsup.manhwas.data.mapper.aEntity
import com.tecsup.manhwas.data.mapper.aModelo
import com.tecsup.manhwas.data.remote.ApiService
import com.tecsup.manhwas.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/** Retrofit llena Room y la UI siempre lee de Room. */
class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> = dao.todos().map { lista -> lista.map { it.aModelo() } }
    override val favoritos: Flow<List<Item>> = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        // Si se inserta sin cuidado, el Upsert pone esFavorito = false y se pierden. Se conservan aqui:
        val favoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar().data.map { it.aEntity(esFavorito = it.id in favoritos) }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {          // sin internet
        Result.failure(e)
    } catch (e: HttpException) {        // 404, 429 (muchas peticiones), 500...
        Result.failure(e)
    } catch (e: SerializationException) { // la API cambio el formato
        Result.failure(e)
    }

    /**
     * Segundo endpoint. Si responde, se actualiza la copia en Room (conservando el favorito);
     * si no hay internet, se muestra lo que ya estaba guardado.
     */
    override suspend fun obtenerDetalle(id: Int): Result<Item> {
        val guardado = dao.obtener(id)
        return try {
            val entidad = api.detalle(id).data.aEntity(esFavorito = guardado?.esFavorito ?: false)
                .let { if (guardado != null && it.popularidad == 0) it.copy(popularidad = guardado.popularidad) else it }
            dao.guardar(entidad)
            Result.success(entidad.aModelo())
        } catch (e: Exception) {
            if (e !is IOException && e !is HttpException && e !is SerializationException) throw e
            if (guardado != null) Result.success(guardado.aModelo()) else Result.failure(e)
        }
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada(): Int = dao.contar()
    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
