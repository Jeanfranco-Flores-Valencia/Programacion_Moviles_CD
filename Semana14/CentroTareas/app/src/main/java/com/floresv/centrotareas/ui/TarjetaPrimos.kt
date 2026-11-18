package com.floresv.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.floresv.centrotareas.model.AvancePrimos
import com.floresv.centrotareas.model.PrimosUi
import com.floresv.centrotareas.ui.components.BotonPrincipal
import com.floresv.centrotareas.ui.components.BotonSecundario
import com.floresv.centrotareas.ui.components.EstadoTarea
import com.floresv.centrotareas.ui.components.FilaAcciones
import com.floresv.centrotareas.ui.components.Resultado
import com.floresv.centrotareas.ui.components.TarjetaTarea
import java.text.NumberFormat
import java.util.Locale

/** Tarjeta 2: contar numeros primos (calculo pesado en Dispatchers.Default). */
@Composable
fun TarjetaPrimos(
    primos: PrimosUi,
    avance: AvancePrimos?,
    numero: String,
    onNumero: (String) -> Unit,
    onContar: (Int) -> Unit,
    onCancelar: () -> Unit
) {
    val estado = when (primos) {
        PrimosUi.Inicial -> EstadoTarea.LISTA
        PrimosUi.Calculando -> EstadoTarea.EN_CURSO
        is PrimosUi.Listo -> EstadoTarea.COMPLETADA
        PrimosUi.Cancelado -> EstadoTarea.CANCELADA
    }
    val calculando = primos is PrimosUi.Calculando
    // Solo numeros; textos no numericos o menores que 2 no habilitan Contar
    val hasta = numero.toIntOrNull()?.takeIf { it >= 2 }
    val formato = NumberFormat.getIntegerInstance(Locale.US)

    TarjetaTarea("Contar números primos", "Cálculo pesado que se puede cancelar", Icons.Outlined.Functions, estado) {
        OutlinedTextField(
            value = numero,
            onValueChange = { onNumero(it.filter(Char::isDigit).take(9)) },
            label = { Text("Contar primos hasta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            enabled = !calculando,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        FilaAcciones {
            BotonPrincipal("Contar", Icons.Outlined.Calculate, habilitado = hasta != null && !calculando) { hasta?.let(onContar) }
            BotonSecundario("Cancelar", Icons.Outlined.Close, habilitado = calculando, onClick = onCancelar)
        }
        when (primos) {
            PrimosUi.Inicial -> Unit
            // Mejora IA: barra con el porcentaje y los primos encontrados hasta ahora
            PrimosUi.Calculando -> Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                val p = avance?.porcentaje ?: 0f
                LinearProgressIndicator(progress = { p }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)))
                Row {
                    Text("${(p * 100).toInt()} %", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("${formato.format(avance?.cantidad ?: 0)} primos encontrados", style = MaterialTheme.typography.bodySmall)
                }
            }
            is PrimosUi.Listo -> Resultado(
                "${formato.format(primos.cantidad)} números primos",
                "Calculado en ${formato.format(primos.milisegundos)} ms",
                exito = true
            )
            PrimosUi.Cancelado -> Resultado("Cálculo cancelado", "Puedes volver a contar cuando quieras", exito = false)
        }
    }
}
