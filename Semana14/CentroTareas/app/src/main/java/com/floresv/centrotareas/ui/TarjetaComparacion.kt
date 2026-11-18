package com.floresv.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresv.centrotareas.model.Medicion
import com.floresv.centrotareas.model.ResumenTiempos
import com.floresv.centrotareas.ui.components.BarraTiempo
import com.floresv.centrotareas.ui.components.BotonSecundario
import com.floresv.centrotareas.ui.components.EstadoTarea
import com.floresv.centrotareas.ui.components.FilaAcciones
import com.floresv.centrotareas.ui.components.Resultado
import com.floresv.centrotareas.ui.components.TarjetaTarea

private val ColorUna = Color(0xFFEF6C00)
private val ColorVez = Color(0xFF1565C0)

/** Mejora IA: tarjeta 4, compara los tiempos de las cargas con barras y muestra el ahorro de async. */
@Composable
fun TarjetaComparacion(historial: List<Medicion>, resumen: ResumenTiempos, onLimpiar: () -> Unit) {
    val estado = if (historial.isEmpty()) EstadoTarea.LISTA else EstadoTarea.COMPLETADA
    TarjetaTarea("Comparación de tiempos", "Últimas cargas exitosas", Icons.Outlined.Leaderboard, estado) {
        if (historial.isEmpty()) {
            Text(
                "Haz una carga «Una por una» y otra «A la vez» para compararlas.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            resumen.ahorroPorcentaje?.let {
                Resultado("A la vez fue $it % más rápido", "Promedio: ${resumen.promedioALaVez} ms contra ${resumen.promedioUnaPorUna} ms", exito = true)
            }
            val maximo = historial.maxOf { it.milisegundos }.coerceAtLeast(1)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                historial.forEach { m ->
                    BarraTiempo(m.modo, m.milisegundos, m.milisegundos.toFloat() / maximo, if (m.modo == "A la vez") ColorVez else ColorUna)
                }
            }
            Text("Una por una suma los tiempos; a la vez espera solo a la más lenta.", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            FilaAcciones { BotonSecundario("Limpiar historial", Icons.Outlined.DeleteSweep, onClick = onLimpiar) }
        }
    }
}
