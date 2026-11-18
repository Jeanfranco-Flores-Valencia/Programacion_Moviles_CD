package com.floresv.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.floresv.campusgps.model.TipoError
import com.floresv.campusgps.model.UbicacionUi

/** Recuadro de error con el boton que corresponde a cada tipo de problema. */
@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Text(error.mensaje, textAlign = TextAlign.Center)
            when (error.tipo) {
                TipoError.SIN_PERMISO -> Button(onClick = onAbrirAjustes) { Text("Abrir ajustes") }
                TipoError.GPS_APAGADO, TipoError.OTRO -> Button(onClick = onReintentar) { Text("Reintentar") }
                TipoError.SIN_PLAY_SERVICES -> Unit // sin boton: reintentar no lo arregla
            }
        }
    }
}
