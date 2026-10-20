package com.floresvalencia.lavacampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.floresvalencia.lavacampus.data.local.dao.DetalleDao
import com.floresvalencia.lavacampus.data.local.dao.PedidoDao
import com.floresvalencia.lavacampus.data.local.dao.ServicioDao
import com.floresvalencia.lavacampus.data.local.dao.UsuarioDao
import com.floresvalencia.lavacampus.data.local.entity.DetallePedido
import com.floresvalencia.lavacampus.data.local.entity.Pedido
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.data.local.entity.Usuario

@Database(
    entities = [Servicio::class, Usuario::class, Pedido::class, DetallePedido::class],
    version = 1,
    exportSchema = false
)
abstract class LavaDatabase : RoomDatabase() {
    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun detalleDao(): DetalleDao

    companion object {
        @Volatile
        private var INSTANCE: LavaDatabase? = null

        /** Una sola instancia de la base para toda la app (singleton). */
        fun obtener(context: Context): LavaDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                LavaDatabase::class.java,
                "lavacampus.db"
            ).build().also { INSTANCE = it }
        }
    }
}
