package com.floresvalencia.lavacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.floresvalencia.lavacampus.data.local.entity.DetallePedido
import com.floresvalencia.lavacampus.data.local.entity.Pedido
import com.floresvalencia.lavacampus.data.local.entity.Usuario

/**
 * Pedido completo: el pedido, su estudiante y todas sus lineas con el servicio.
 * Relacion uno a muchos anidada (Pedido -> DetallePedido -> Servicio).
 */
data class PedidoConDetalle(
    @Embedded val pedido: Pedido,
    @Relation(parentColumn = "usuarioId", entityColumn = "id")
    val usuario: Usuario,
    @Relation(entity = DetallePedido::class, parentColumn = "id", entityColumn = "pedidoId")
    val detalles: List<DetalleConServicio>
)
