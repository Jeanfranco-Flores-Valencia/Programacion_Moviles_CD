package com.floresvalencia.lavacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.floresvalencia.lavacampus.data.local.entity.DetallePedido
import com.floresvalencia.lavacampus.data.local.entity.Servicio

/** Una linea del pedido junto con su servicio (N detalles -> 1 servicio). */
data class DetalleConServicio(
    @Embedded val detalle: DetallePedido,
    @Relation(parentColumn = "servicioId", entityColumn = "id")
    val servicio: Servicio
)
