package com.floresvalencia.lavacampus.ui.components

import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.model.Unidad

/** Textos de presentacion de un pedido (no contienen reglas de negocio). */

val PedidoConDetalle.estado: EstadoPedido get() = EstadoPedido.desde(pedido.estado)

/** "3.5 kg + 5 prendas" */
fun PedidoConDetalle.resumenLineas(): String =
    detalles.joinToString(" + ") { cantidadTexto(it.detalle.cantidad, Unidad.desde(it.servicio.unidad)) }
        .ifEmpty { "Sin detalle" }

/** Icono del servicio principal del pedido. */
fun PedidoConDetalle.iconoPrincipal(): String = detalles.firstOrNull()?.servicio?.icono ?: "wash"
