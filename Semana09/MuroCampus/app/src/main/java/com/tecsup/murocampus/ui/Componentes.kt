package com.tecsup.murocampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/** Un color por usuario para la etiqueta "Usuario N". */
private val coloresUsuario = listOf(
    Color(0xFF1E88E5), Color(0xFFF9A825), Color(0xFF43A047), Color(0xFF8E24AA), Color(0xFFE53935),
    Color(0xFF00897B), Color(0xFF6D4C41), Color(0xFF3949AB), Color(0xFFD81B60), Color(0xFF546E7A)
)

fun colorDeUsuario(userId: Int): Color = coloresUsuario[(userId - 1).mod(coloresUsuario.size)]

/** Etiqueta redondeada con el numero de usuario. */
@Composable
fun EtiquetaUsuario(userId: Int) {
    Text(
        "Usuario $userId",
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colorDeUsuario(userId))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

/** Tarjeta de una publicacion: etiqueta del usuario, titulo en negrita y las primeras lineas del texto. */
@Composable
fun TarjetaPost(
    userId: Int,
    titulo: String,
    cuerpo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(12.dp)) {
            EtiquetaUsuario(userId)
            Text(titulo, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                cuerpo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Indicador de carga centrado con su texto. */
@Composable
fun Cargando(texto: String = "Cargando publicaciones...", modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp)
            ) {
                CircularProgressIndicator()
                Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** Recuadro de error con el mensaje y el boton Reintentar. */
@Composable
fun ErrorConReintento(mensaje: String, onReintentar: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            mensaje,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.errorContainer)
                .padding(16.dp)
        )
        Button(onClick = onReintentar) { Text("Reintentar") }
    }
}
