package com.floresvalencia.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.floresvalencia.lavacampus.data.local.entity.Usuario
import com.floresvalencia.lavacampus.model.ClienteResumen
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtener(id: Int): Usuario?

    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1")
    suspend fun primeroDeRol(rol: String): Usuario?

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>): List<Long>

    /** Registro de un estudiante nuevo. */
    @Insert
    suspend fun insertar(u: Usuario): Long

    @Query("SELECT COUNT(*) FROM usuarios WHERE correo = :correo")
    suspend fun contarPorCorreo(correo: String): Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE codigo = :codigo")
    suspend fun contarPorCodigo(codigo: String): Int

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    /**
     * Estudiantes con su cantidad de pedidos (sin cancelados) y lo gastado en pedidos pagados.
     * LEFT JOIN para que aparezcan tambien los estudiantes sin pedidos (0 y 0.0).
     */
    @Query(
        """
        SELECT u.id AS id, u.nombre AS nombre, u.codigo AS codigo, u.correo AS correo,
               COUNT(CASE WHEN p.estado != 'CANCELADO' THEN p.id END) AS pedidos,
               COALESCE(SUM(CASE WHEN p.pagado = 1 THEN p.total ELSE 0 END), 0.0) AS gastado
        FROM usuarios u
        LEFT JOIN pedidos p ON p.usuarioId = u.id
        WHERE u.rol = 'CLIENTE'
        GROUP BY u.id
        ORDER BY u.nombre
        """
    )
    fun observarClientes(): Flow<List<ClienteResumen>>
}
