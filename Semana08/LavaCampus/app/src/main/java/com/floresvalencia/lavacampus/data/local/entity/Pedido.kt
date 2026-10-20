package com.floresvalencia.lavacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pedidos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("usuarioId"),
        Index("estado")        // acelera las consultas del panel y de la bandeja
    ]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val fechaRegistro: String,     // 'aaaa-MM-dd'
    val fechaEntrega: String,      // 'aaaa-MM-dd'
    val estado: String,            // EstadoPedido.name
    val total: Double,
    val casillero: String? = null, // nulo hasta estar LISTO
    val pagado: Boolean = false,
    val nota: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)
