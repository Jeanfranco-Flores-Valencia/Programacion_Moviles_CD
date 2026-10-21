package com.floresvalencia.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.model.ClienteResumen
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.LavaTextField
import com.floresvalencia.lavacampus.ui.components.iniciales
import com.floresvalencia.lavacampus.ui.components.soles
import com.floresvalencia.lavacampus.ui.estudiante.Tarjeta
import com.floresvalencia.lavacampus.viewmodel.ClientesViewModel

@Composable
fun ClientesScreen(vm: ClientesViewModel, onCliente: (String) -> Unit = {}) {
    val clientes by vm.clientes.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val colores = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        AppTopBar(titulo = "Clientes")
        LavaTextField(
            valor = busqueda,
            onCambio = vm::buscar,
            etiqueta = "Buscar estudiante",
            icono = Icons.Filled.Search,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (clientes.isEmpty()) {
                item { Text("Sin resultados", style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant) }
            }
            items(clientes, key = { it.id }) { c -> TarjetaCliente(c, onClick = { onCliente(c.nombre) }) }
        }
    }
}

@Composable
private fun TarjetaCliente(c: ClienteResumen, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    // Mejora IA: la tarjeta abre los pedidos del cliente
    Tarjeta(modifier = Modifier.clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(colores.primaryContainer)
            ) {
                Text(iniciales(c.nombre), style = MaterialTheme.typography.titleSmall, color = colores.primary)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(c.nombre, style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
                Text(c.codigo, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                Text(
                    if (c.pedidos == 1) "1 pedido" else "${c.pedidos} pedidos",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = colores.primary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = colores.onSurfaceVariant)
                Text(soles(c.gastado), style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
            }
        }
    }
}
