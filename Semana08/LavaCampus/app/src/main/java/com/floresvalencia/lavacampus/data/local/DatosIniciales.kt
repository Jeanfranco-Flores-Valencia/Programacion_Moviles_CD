package com.floresvalencia.lavacampus.data.local

import com.floresvalencia.lavacampus.data.local.entity.Servicio

/** Datos de prueba (ficticios, Ley 29733). Se cargan solo la primera vez. */
object DatosIniciales {

    val servicios = listOf(
        Servicio(nombre = "Lavado y secado", descripcion = "Lavado, secado y doblado por kilo", unidad = "KG", precio = 8.00, horasEntrega = 24, activo = true, icono = "wash"),
        Servicio(nombre = "Planchado", descripcion = "Planchado de camisas, pantalones y polos", unidad = "PRENDA", precio = 2.50, horasEntrega = 48, activo = true, icono = "iron"),
        Servicio(nombre = "Lavado en seco", descripcion = "Sacos, abrigos y prendas delicadas", unidad = "PRENDA", precio = 12.00, horasEntrega = 48, activo = true, icono = "shirt"),
        Servicio(nombre = "Edredón / frazada", descripcion = "Edredones, frazadas y cobertores", unidad = "UNIDAD", precio = 20.00, horasEntrega = 48, activo = false, icono = "wash")
    )

    suspend fun sembrar(db: LavaDatabase) {
        if (db.servicioDao().contar() == 0) {
            db.servicioDao().insertarTodos(servicios)
        }
        // TODO (Paso 3): si usuarioDao().contar() == 0 inserta usuarios, pedidos y sus detalles
    }
}
