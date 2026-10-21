package com.floresvalencia.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.components.PedidoCard
import com.floresvalencia.lavacampus.ui.components.StepperEstado
import com.floresvalencia.lavacampus.ui.components.estado
import com.floresvalencia.lavacampus.ui.components.fechaCorta
import com.floresvalencia.lavacampus.ui.components.iconoPrincipal
import com.floresvalencia.lavacampus.ui.components.numeroPedido
import com.floresvalencia.lavacampus.ui.components.resumenLineas
import com.floresvalencia.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    vm: InicioViewModel,
    onNuevoPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onTarifas: () -> Unit,
    onPedido: (Int) -> Unit
) {
    val activo by vm.pedidoActivo.collectAsState()
    val recientes by vm.recientes.collectAsState()
    val listos by vm.listos.collectAsState()
    val colores = MaterialTheme.colorScheme
    val primerNombre = vm.estudiante.nombre.substringBefore(" ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Hola, $primerNombre", style = MaterialTheme.typography.headlineSmall, color = colores.onBackground)
                Text(
                    "${vm.estudiante.codigo} · Tecsup",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant
                )
            }
            // Mejora IA: la campana muestra cuantos pedidos estan listos y abre Mis pedidos
            CampanaAvisos(cantidad = listos, onClick = onMisPedidos)
        }
        Spacer(Modifier.height(16.dp))

        TarjetaPedidoActivo(activo, onClick = { activo?.let { onPedido(it.pedido.id) } })
        Spacer(Modifier.height(14.dp))

        LavaButton(texto = "Nuevo pedido", onClick = onNuevoPedido)
        Spacer(Modifier.height(14.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AccesoRapido(Icons.AutoMirrored.Filled.List, "Mis pedidos", "Ver estado", onMisPedidos, Modifier.weight(1f))
            AccesoRapido(Icons.Outlined.LocalOffer, "Tarifas", "Precios por kg", onTarifas, Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Últimos pedidos",
                style = MaterialTheme.typography.titleMedium,
                color = colores.onBackground,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Ver todos",
                style = MaterialTheme.typography.labelMedium,
                color = colores.primary,
                modifier = Modifier.clickable(onClick = onMisPedidos)
            )
        }
        Spacer(Modifier.height(10.dp))
        if (recientes.isEmpty()) {
            Text("Aún no tienes pedidos", style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant)
        }
        recientes.forEach { p ->
            PedidoCard(
                id = p.pedido.id,
                detalle = "${p.resumenLineas()} · ${fechaCorta(p.pedido.fechaRegistro)}",
                total = p.pedido.total,
                estado = p.estado,
                icono = p.iconoPrincipal(),
                onClick = { onPedido(p.pedido.id) },
                pie = if (p.estado == EstadoPedido.LISTO && p.pedido.casillero != null) {
                    {
                        Text(
                            "Casillero ${p.pedido.casillero}",
                            style = MaterialTheme.typography.labelMedium,
                            color = colores.primary
                        )
                    }
                } else null
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

/** Campana con insignia (Mejora IA). Se reutiliza en el panel del administrador. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CampanaAvisos(cantidad: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        BadgedBox(badge = {
            if (cantidad > 0) Badge { Text(cantidad.toString()) }
        }) {
            Icon(Icons.Filled.Notifications, contentDescription = "Avisos", tint = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
private fun TarjetaPedidoActivo(activo: PedidoConDetalle?, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(colores.primary)
            .clickable(enabled = activo != null, onClick = onClick)
            .padding(16.dp)
    ) {
        if (activo == null) {
            Text("Tu pedido activo", style = MaterialTheme.typography.labelMedium, color = colores.onPrimary.copy(alpha = 0.85f))
            Spacer(Modifier.height(6.dp))
            Text("No tienes pedidos activos", style = MaterialTheme.typography.titleLarge, color = colores.onPrimary)
            Text(
                "Crea un pedido y sigue aquí el estado de tu ropa",
                style = MaterialTheme.typography.bodySmall,
                color = colores.onPrimary.copy(alpha = 0.85f)
            )
            return@Column
        }
        val estado = activo.estado
        Row {
            Text(
                "Tu pedido activo",
                style = MaterialTheme.typography.labelMedium,
                color = colores.onPrimary.copy(alpha = 0.85f),
                modifier = Modifier.weight(1f)
            )
            Text(
                numeroPedido(activo.pedido.id),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = colores.onPrimary
            )
        }
        Text(estado.etiqueta, style = MaterialTheme.typography.headlineSmall, color = colores.onPrimary)
        Text(
            if (estado == EstadoPedido.LISTO) "Recógelo en el casillero ${activo.pedido.casillero ?: "-"}"
            else "Entrega estimada: ${fechaCorta(activo.pedido.fechaEntrega)}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (estado == EstadoPedido.LISTO) FontWeight.Bold else FontWeight.Normal,
            color = colores.onPrimary.copy(alpha = 0.9f)
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(colores.surface)
                .padding(horizontal = 6.dp, vertical = 10.dp)
        ) {
            StepperEstado(estado = estado)
        }
    }
}

@Composable
private fun AccesoRapido(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.outlineVariant),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colores.primaryContainer)
            ) {
                Icon(icono, contentDescription = null, tint = colores.primary)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(titulo, style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
                Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
            }
        }
    }
}
