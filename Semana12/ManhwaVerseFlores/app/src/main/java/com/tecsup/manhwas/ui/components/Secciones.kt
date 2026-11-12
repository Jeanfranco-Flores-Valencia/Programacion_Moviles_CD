package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.manhwas.ui.theme.Colores

/** Mejora IA: nombre de la app con "Verse" en morado, como el logo de una web. */
@Composable
fun Logo(tamano: TextUnit = 22.sp) {
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(color = Colores.Texto)) { append("Manhwa") }
            withStyle(SpanStyle(color = Colores.Morado)) { append("Verse") }
        },
        fontSize = tamano,
        fontWeight = FontWeight.ExtraBold
    )
}

/** Titulo de seccion con una barra morada a la izquierda y un detalle opcional a la derecha. */
@Composable
fun EncabezadoSeccion(titulo: String, modifier: Modifier = Modifier, detalle: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.width(4.dp).height(20.dp).clip(RoundedCornerShape(2.dp)).background(Colores.Morado))
        Text(
            titulo,
            style = MaterialTheme.typography.titleMedium,
            color = Colores.Texto,
            modifier = Modifier.padding(start = 10.dp).weight(1f)
        )
        detalle?.let { Text(it, style = MaterialTheme.typography.labelMedium, color = Colores.TextoSuave) }
    }
}

enum class TipoAviso(val color: Color, val icono: ImageVector) {
    Info(Colores.Verde, Icons.Outlined.CheckCircle),
    SinConexion(Colores.Dorado, Icons.Outlined.CloudOff),
    Error(Colores.Rojo, Icons.Outlined.ErrorOutline)
}

/** Aviso con icono y color segun el tipo (info, sin conexion o error). */
@Composable
fun Aviso(texto: String, tipo: TipoAviso, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(tipo.color.copy(alpha = 0.12f))
            .border(1.dp, tipo.color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Icon(tipo.icono, contentDescription = null, tint = tipo.color, modifier = Modifier.size(20.dp))
        Text(texto, style = MaterialTheme.typography.bodySmall, color = Colores.Texto)
    }
}

/** Numero grande con su etiqueta (panel del administrador). */
@Composable
fun TarjetaEstadistica(valor: Int, etiqueta: String, icono: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Colores.Superficie)
            .border(1.dp, Colores.Borde, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Text(valor.toString(), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Colores.Texto)
        Text(etiqueta, style = MaterialTheme.typography.bodySmall, color = Colores.TextoSuave)
    }
}
