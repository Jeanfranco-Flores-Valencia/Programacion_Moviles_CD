package com.floresvalencia.lavacampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.floresvalencia.lavacampus.data.local.dao.ServicioDao
import com.floresvalencia.lavacampus.data.local.entity.Servicio

@Database(
    entities = [Servicio::class /* TODO: agrega las otras 3 */],
    version = 1,
    exportSchema = false
)
abstract class LavaDatabase : RoomDatabase() {
    abstract fun servicioDao(): ServicioDao
    // TODO: usuarioDao(), pedidoDao() y detalleDao()

    companion object {
        @Volatile
        private var INSTANCE: LavaDatabase? = null

        fun obtener(context: Context): LavaDatabase = INSTANCE ?: synchronized(this) {
            // TODO: Room.databaseBuilder(...) con nombre "lavacampus.db" (singleton)
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                LavaDatabase::class.java,
                "lavacampus.db"
            ).build().also { INSTANCE = it }
        }
    }
}
