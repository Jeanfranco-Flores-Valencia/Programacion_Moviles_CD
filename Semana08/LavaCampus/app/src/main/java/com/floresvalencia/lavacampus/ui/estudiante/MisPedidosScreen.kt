package com.floresvalencia.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.PedidoCard
import com.floresvalencia.lavacampus.ui.components.estado
import com.floresvalencia.lavacampus.ui.components.fechaCorta
import com.floresvalencia.lavacampus.ui.components.iconoPrincipal
import com.floresvalencia.lavacampus.ui.components.nombreCorto
import com.floresvalencia.lavacampus.ui.components.numeroPedido
import com.floresvalencia.lavacampus.ui.components.resumenLineas
import com.floresvalencia.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun MisPedidosScreen(
    vm: MisPedidosViewModel,
    onAtras: () -> Unit,
    onPedido: (Int) -> Unit,
    onNuevoPedido: () -> Unit,
    onRepetir: (Int) -> Unit = {}
) {
    val activos by vm.activos.collectAsState()
    val historial by vm.historial.collectAsState()
    var pestana by rememberSaveable { mutableStateOf(0) }
    var aCancelar by remember { mutableStateOf<PedidoConDetalle?>(null) }
    val colores = MaterialTheme.colorScheme
    val lista = if (pestana == 0) activos else historial
    val listo = activos.firstOrNull { it.estado == EstadoPedido.LISTO }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        Column(Modifier.fillMaxSize()) {
            AppTopBar(titulo = "Mis pedidos", onAtras = onAtras)
            Pestanas(
                opciones = listOf("Activos", "Historial"),
                elegida = pestana,
                onElegir = { pestana = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (lista.isEmpty()) {
                    item {
                        Column {
                            Text(
                                if (pestana == 0) "No tienes pedidos activos" else "Tu historial está vacío",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colores.onSurfaceVariant
                            )
                            // Mejora IA: accion directa desde el estado vacio
                            if (pestana == 0) {
                                TextButton(onClick = onNuevoPedido, contentPadding = PaddingValues(horizontal = 0.dp)) {
                                    Text("Hacer un pedido ahora", style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    }
                }
                items(lista, key = { it.pedido.id }) { p ->
                    TarjetaMiPedido(
                        p = p,
                        puedeCancelar = vm.puedeCancelar(p),
                        onClick = { onPedido(p.pedido.id) },
                        onCancelar = { aCancelar = p },
                        onRepetir = { onRepetir(p.pedido.id) }
                    )
                }
                if (pestana == 0 && listo != null) {
                    item {
                        Aviso(
                            titulo = "Aviso",
                            texto = "Tu pedido ${numeroPedido(listo.pedido.id)} está listo, recógelo en el casillero ${listo.pedido.casillero ?: "indicado"}."
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = onNuevoPedido,
            containerColor = colores.primary,
            contentColor = colores.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Nuevo pedido")
        }
    }

    aCancelar?.let { p ->
        AlertDialog(
            onDismissRequest = { aCancelar = null },
            title = { Text("¿Cancelar pedido ${numeroPedido(p.pedido.id)}?") },
            text = { Text("El pedido pasará a Cancelado.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(p)
                    aCancelar = null
                }) { Text("Sí, cancelar", color = colores.error) }
            },
            dismissButton = { TextButton(onClick = { aCancelar = null }) { Text("No") } }
        )
    }
}

@Composable
private fun TarjetaMiPedido(
    p: PedidoConDetalle,
    puedeCancelar: Boolean,
    onClick: () -> Unit,
    onCancelar: () -> Unit,
    onRepetir: () -> Unit
) {
    val estado = p.estado
    val verde = EstadoPedido.LISTO.color
    PedidoCard(
        id = p.pedido.id,
        detalle = "${nombreCorto(p.usuario.nombre)} · ${p.resumenLineas()}",
        total = p.pedido.total,
        estado = estado,
        icono = p.iconoPrincipal(),
        borde = if (estado == EstadoPedido.LISTO) verde else null,
        onClick = onClick,
        pie = {
            when {
                estado == EstadoPedido.LISTO -> Text(
                    "Casillero ${p.pedido.casillero ?: "-"}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(verde)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                )
                puedeCancelar -> Text(
                    "Cancelar",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable(onClick = onCancelar)
                )
                estado.esActivo -> Text(
                    "Entrega ${fechaCorta(p.pedido.fechaEntrega)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                // Mejora IA: repetir un pedido del historial con los precios actuales
                else -> Text(
                    "Pedir de nuevo",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onRepetir)
                )
            }
        }
    )
}

/** Selector tipo pastilla (Activos / Historial). */
@Composable
internal fun Pestanas(
    opciones: List<String>,
    elegida: Int,
    onElegir: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colores.surfaceVariant)
            .padding(4.dp)
    ) {
        opciones.forEachIndexed { i, texto ->
            val activa = i == elegida
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activa) colores.primary else colores.surfaceVariant)
                    .clickable { onElegir(i) }
                    .padding(vertical = 9.dp)
            ) {
                Text(
                    texto,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (activa) colores.onPrimary else colores.onSurfaceVariant
                )
            }
        }
    }
}
