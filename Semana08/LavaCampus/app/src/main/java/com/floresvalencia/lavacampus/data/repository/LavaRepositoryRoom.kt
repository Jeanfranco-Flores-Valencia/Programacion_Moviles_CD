package com.floresvalencia.lavacampus.data.repository

import com.floresvalencia.lavacampus.data.local.LavaDatabase
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

/** Implementacion del Repository con Room. Solo delega en los DAOs: las reglas viven en los ViewModels. */
class LavaRepositoryRoom(private val db: LavaDatabase) : LavaRepository {

    private val servicioDao = db.servicioDao()

    // ---------- Servicios (ejemplo resuelto) ----------
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) servicioDao.insertar(s) else servicioDao.actualizar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) = servicioDao.eliminar(s)

    override suspend fun cambiarActivo(id: Int, activo: Boolean) = servicioDao.cambiarActivo(id, activo)

    // TODO (Paso 4): implementa el resto de funciones de la interface
}
