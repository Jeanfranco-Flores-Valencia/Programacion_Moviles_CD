package com.floresvalencia.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.ui.components.capitalizar
import com.floresvalencia.lavacampus.ui.components.fechaCorta
import com.floresvalencia.lavacampus.ui.components.graficos.GraficoBarras
import com.floresvalencia.lavacampus.ui.components.graficos.GraficoDona
import com.floresvalencia.lavacampus.ui.components.soles
import com.floresvalencia.lavacampus.ui.estudiante.SeccionTitulo
import com.floresvalencia.lavacampus.ui.estudiante.Tarjeta
import com.floresvalencia.lavacampus.viewmodel.PanelViewModel

@Composable
fun PanelScreen(vm: PanelViewModel, onVerListos: () -> Unit) {
    val pedidosHoy by vm.pedidosHoy.collectAsState()
    val enProceso by vm.enProceso.collectAsState()
    val porRecoger by vm.porRecoger.collectAsState()
    val ingresos by vm.ingresosSemana.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Panel de la lavandería", style = MaterialTheme.typography.headlineSmall, color = colores.onBackground)
                Text(
                    "Hoy · ${capitalizar(fechaCorta(vm.hoy))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant
                )
            }
            Icon(Icons.Filled.Notifications, contentDescription = "Avisos", tint = colores.onBackground)
        }
        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador(pedidosHoy, "Pedidos hoy", colores.primary, Modifier.weight(1f))
            Contador(enProceso, "En proceso", EstadoPedido.EN_PROCESO.color, Modifier.weight(1f))
            Contador(porRecoger, "Por recoger", EstadoPedido.LISTO.color, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colores.primary)
                .padding(16.dp)
        ) {
            Text("Ingresos de la semana", style = MaterialTheme.typography.labelMedium, color = colores.onPrimary.copy(alpha = 0.85f))
            Text(soles(ingresos), style = MaterialTheme.typography.headlineSmall, color = colores.onPrimary)
            Text("Pedidos pagados desde el lunes", style = MaterialTheme.typography.bodySmall, color = colores.onPrimary.copy(alpha = 0.85f))
        }
        Spacer(Modifier.height(18.dp))

        SeccionTitulo("Pedidos por estado")
        Tarjeta {
            GraficoDona(datos = porEstado, subtituloCentro = "pedidos")
        }
        Spacer(Modifier.height(18.dp))

        SeccionTitulo("Ingresos por servicio (S/)")
        Tarjeta {
            GraficoBarras(datos = porServicio)
        }
        Spacer(Modifier.height(14.dp))

        if (porRecoger > 0) {
            val verde = EstadoPedido.LISTO.color
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(verde.copy(alpha = 0.12f))
                    .padding(12.dp)
            ) {
                Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = verde)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        if (porRecoger == 1) "1 pedido espera recojo" else "$porRecoger pedidos esperan recojo",
                        style = MaterialTheme.typography.titleSmall,
                        color = colores.onBackground
                    )
                    Text("Avisa a los estudiantes", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                }
                Button(
                    onClick = onVerListos,
                    colors = ButtonDefaults.buttonColors(containerColor = verde, contentColor = Color.White)
                ) { Text("Ver") }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun Contador(valor: Int, etiqueta: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(vertical = 14.dp)
    ) {
        Text(valor.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
        Text(etiqueta, style = MaterialTheme.typography.labelSmall, color = color)
    }
}
