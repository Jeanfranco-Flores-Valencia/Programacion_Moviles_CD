package com.floresv.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
            PrimosUi.Calculando -> Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Text("Calculando en segundo plano...")
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
