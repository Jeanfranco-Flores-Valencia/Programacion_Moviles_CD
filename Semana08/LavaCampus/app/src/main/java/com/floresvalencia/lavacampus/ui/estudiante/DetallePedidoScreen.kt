package com.floresvalencia.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.model.Unidad
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.EstadoPedidoChip
import com.floresvalencia.lavacampus.ui.components.EstiloBoton
import com.floresvalencia.lavacampus.ui.components.IconoServicio
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.components.StepperEstado
import com.floresvalencia.lavacampus.ui.components.cantidadTexto
import com.floresvalencia.lavacampus.ui.components.estado
import com.floresvalencia.lavacampus.ui.components.fechaCorta
import com.floresvalencia.lavacampus.ui.components.fechaHora
import com.floresvalencia.lavacampus.ui.components.iconoPrincipal
import com.floresvalencia.lavacampus.ui.components.numeroPedido
import com.floresvalencia.lavacampus.ui.components.soles
import com.floresvalencia.lavacampus.viewmodel.DetallePedidoViewModel

@Composable
fun DetallePedidoScreen(vm: DetallePedidoViewModel, onAtras: () -> Unit) {
    val pedido by vm.pedido.collectAsState()
    val puedeCancelar by vm.puedeCancelar.collectAsState()
    var confirmar by remember { mutableStateOf(false) }
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        val p = pedido
        AppTopBar(titulo = if (p != null) "Pedido ${numeroPedido(p.pedido.id)}" else "Pedido", onAtras = onAtras)
        if (p == null) {
            Text("Cargando...", modifier = Modifier.padding(16.dp), color = colores.onSurfaceVariant)
            return@Column
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            CabeceraPedido(p)
            Spacer(Modifier.height(16.dp))
            SeccionTitulo("Seguimiento")
            Tarjeta {
                if (p.estado == EstadoPedido.CANCELADO) {
                    Text(
                        "Este pedido fue cancelado",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EstadoPedido.CANCELADO.color
                    )
                } else {
                    StepperEstado(
                        estado = p.estado,
                        vertical = true,
                        textosLaterales = textosSeguimiento(p)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            SeccionTitulo("Detalle")
            Tarjeta {
                p.detalles.forEach { d ->
                    val unidad = Unidad.desde(d.servicio.unidad)
                    Row(Modifier.padding(vertical = 3.dp)) {
                        Text(
                            "${d.servicio.nombre} · ${cantidadTexto(d.detalle.cantidad, unidad)} × ${soles(d.detalle.precioUnitario)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colores.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            soles(d.detalle.subtotal),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colores.onSurface
                        )
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = colores.outlineVariant)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Total", style = MaterialTheme.typography.titleSmall, color = colores.onSurface, modifier = Modifier.weight(1f))
                    Text(soles(p.pedido.total), style = MaterialTheme.typography.titleLarge, color = colores.primary)
                }
                if (p.pedido.nota.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text("Nota: ${p.pedido.nota}", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                }
            }
            Spacer(Modifier.height(12.dp))

            // Recuadro de pago y casillero
            val listo = p.estado == EstadoPedido.LISTO
            val casilleroTexto = when {
                listo && p.pedido.casillero != null -> "Recógelo en el casillero ${p.pedido.casillero}"
                p.estado == EstadoPedido.ENTREGADO -> "Pedido entregado"
                p.estado == EstadoPedido.CANCELADO -> "Sin casillero"
                else -> "Casillero: se asignará al estar listo"
            }
            if (p.pedido.pagado) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(EstadoPedido.LISTO.color.copy(alpha = 0.12f))
                        .padding(12.dp)
                ) {
                    Text("Pagado", style = MaterialTheme.typography.titleSmall, color = EstadoPedido.LISTO.color)
                    Text(casilleroTexto, style = MaterialTheme.typography.bodySmall, color = colores.onSurface)
                }
            } else {
                Aviso(titulo = "Pago pendiente · se paga al recoger", texto = casilleroTexto)
            }
            Spacer(Modifier.height(18.dp))

            if (p.estado != EstadoPedido.CANCELADO && p.estado != EstadoPedido.ENTREGADO) {
                LavaButton(
                    texto = "Cancelar pedido",
                    onClick = { confirmar = true },
                    enabled = puedeCancelar,
                    estilo = EstiloBoton.PELIGRO
                )
                if (!puedeCancelar) {
                    Text(
                        "Solo se cancela antes de empezar el lavado",
                        style = MaterialTheme.typography.bodySmall,
                        color = colores.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }

    if (confirmar) {
        AlertDialog(
            onDismissRequest = { confirmar = false },
            title = { Text("¿Cancelar pedido?") },
            text = { Text("El pedido pasará a Cancelado y no se podrá recuperar.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar()
                    confirmar = false
                }) { Text("Sí, cancelar", color = colores.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmar = false }) { Text("No") }
            }
        )
    }
}

/** Texto a la derecha de cada etapa: hora de registro, "Hecho" o "Pendiente". */
internal fun textosSeguimiento(p: PedidoConDetalle): List<String> {
    val actual = EstadoPedido.etapas.indexOf(p.estado)
    return EstadoPedido.etapas.mapIndexed { i, _ ->
        when {
            i == 0 -> fechaHora(p.pedido.fechaCreacion)
            i <= actual -> "Hecho"
            else -> "Pendiente"
        }
    }
}

@Composable
private fun CabeceraPedido(p: PedidoConDetalle) {
    val colores = MaterialTheme.colorScheme
    Tarjeta {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconoServicio(icono = p.iconoPrincipal(), tamano = 48.dp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "Registrado el ${fechaCorta(p.pedido.fechaRegistro)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = colores.onSurface
                )
                Text(
                    "Entrega estimada: ${fechaCorta(p.pedido.fechaEntrega)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                EstadoPedidoChip(p.estado)
            }
        }
    }
}

@Composable
internal fun SeccionTitulo(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
internal fun Tarjeta(modifier: Modifier = Modifier, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), content = contenido)
    }
}
