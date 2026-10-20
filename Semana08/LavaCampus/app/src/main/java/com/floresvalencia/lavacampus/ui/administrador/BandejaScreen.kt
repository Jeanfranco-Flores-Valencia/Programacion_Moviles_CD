package com.floresvalencia.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.LavaTextField
import com.floresvalencia.lavacampus.ui.components.PedidoCard
import com.floresvalencia.lavacampus.ui.components.estado
import com.floresvalencia.lavacampus.ui.components.iconoPrincipal
import com.floresvalencia.lavacampus.ui.components.nombreCorto
import com.floresvalencia.lavacampus.ui.components.resumenLineas
import com.floresvalencia.lavacampus.viewmodel.BandejaViewModel
import com.floresvalencia.lavacampus.viewmodel.FiltroBandeja

@Composable
fun BandejaScreen(vm: BandejaViewModel, onGestionar: (Int) -> Unit) {
    val pedidos by vm.pedidos.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        AppTopBar(titulo = "Pedidos")
        LavaTextField(
            valor = busqueda,
            onCambio = vm::buscar,
            etiqueta = "Buscar por estudiante o número",
            icono = Icons.Filled.Search,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(FiltroBandeja.entries) { f ->
                FilterChip(
                    selected = f == filtro,
                    onClick = { vm.elegirFiltro(f) },
                    label = { Text(f.etiqueta) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colores.primary,
                        selectedLabelColor = colores.onPrimary
                    )
                )
            }
        }
        if (mensaje != null) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colores.primaryContainer)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(mensaje ?: "", style = MaterialTheme.typography.bodySmall, color = colores.onPrimaryContainer)
                TextButton(onClick = vm::limpiarMensaje, contentPadding = PaddingValues(0.dp), modifier = Modifier.height(28.dp)) {
                    Text("Entendido", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (pedidos.isEmpty()) {
                item {
                    Text("No hay pedidos con este filtro", style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant)
                }
            }
            items(pedidos, key = { it.pedido.id }) { p ->
                TarjetaBandeja(
                    p = p,
                    onClick = { onGestionar(p.pedido.id) },
                    onIniciar = { vm.iniciar(p) },
                    onMarcarListo = { onGestionar(p.pedido.id) },
                    onEntregar = { vm.entregar(p) }
                )
            }
        }
    }
}

@Composable
private fun TarjetaBandeja(
    p: PedidoConDetalle,
    onClick: () -> Unit,
    onIniciar: () -> Unit,
    onMarcarListo: () -> Unit,
    onEntregar: () -> Unit
) {
    PedidoCard(
        id = p.pedido.id,
        detalle = "${nombreCorto(p.usuario.nombre)} · ${p.resumenLineas()}",
        total = p.pedido.total,
        estado = p.estado,
        icono = p.iconoPrincipal(),
        onClick = onClick,
        pie = {
            // Boton de accion segun el estado
            val accion: Pair<String, () -> Unit>? = when (p.estado) {
                EstadoPedido.RECIBIDO -> "Iniciar" to onIniciar
                EstadoPedido.EN_PROCESO -> "Marcar listo" to onMarcarListo
                EstadoPedido.LISTO -> "Entregar" to onEntregar
                else -> null
            }
            if (accion != null) {
                Button(
                    onClick = accion.second,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(accion.first, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}
