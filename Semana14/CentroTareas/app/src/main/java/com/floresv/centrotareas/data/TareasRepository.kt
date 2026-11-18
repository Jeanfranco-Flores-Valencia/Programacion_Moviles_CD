package com.floresv.centrotareas.data

import com.floresv.centrotareas.model.AvancePrimos
import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    suspend fun contarPrimos(hasta: Int): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>

    /** Mejora IA: la tarea simulada vive en el Repository y emite su avance de 0f a 1f. */
    fun progresoTarea(): Flow<Float>

    /** Mejora IA: cuenta primos y va emitiendo su avance (cada 1 %). */
    fun contarPrimosConProgreso(hasta: Int): Flow<AvancePrimos>
}
