package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.manhwas.ui.theme.Colores

/**
 * Mejora IA: botones reutilizables. Todas las pantallas usan estos para que tengan
 * el mismo alto, forma y colores, y para no repetir la configuracion en cada una.
 */
private val FormaBoton = RoundedCornerShape(12.dp)
private val AltoBoton = 50.dp

/** Contenido comun: icono opcional + texto, o un spinner mientras carga. */
@Composable
private fun ContenidoBoton(texto: String, icono: ImageVector?, cargando: Boolean, color: Color) {
    if (cargando) {
        CircularProgressIndicator(color = color, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            icono?.let { Icon(it, contentDescription = null, modifier = Modifier.size(20.dp)) }
            Text(texto, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** Accion principal de la pantalla (morado lleno). */
@Composable
fun BotonPrimario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    icono: ImageVector? = null,
    cargando: Boolean = false,
    habilitado: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = habilitado && !cargando,
        shape = FormaBoton,
        colors = coloresLlenos(Colores.Morado),
        modifier = modifier.height(AltoBoton)
    ) { ContenidoBoton(texto, icono, cargando, Color.White) }
}

/** Accion destructiva, ej. Limpiar cache (rojo con borde). */
@Composable
fun BotonPeligro(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    icono: ImageVector? = null,
    habilitado: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        shape = FormaBoton,
        border = BorderStroke(1.dp, Colores.Rojo.copy(alpha = 0.6f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Colores.Rojo),
        modifier = modifier.height(AltoBoton)
    ) { ContenidoBoton(texto, icono, false, Colores.Rojo) }
}

/** Accion secundaria (borde gris, texto claro). */
@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    icono: ImageVector? = null,
    activo: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        shape = FormaBoton,
        border = BorderStroke(1.dp, if (activo) Colores.Morado else Colores.Borde),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (activo) Colores.Morado.copy(alpha = 0.15f) else Color.Transparent,
            contentColor = if (activo) Colores.MoradoClaro else Colores.Texto
        ),
        modifier = modifier.height(AltoBoton)
    ) { ContenidoBoton(texto, icono, false, Colores.Texto) }
}

/** Boton redondo de icono (barra superior, favorito sobre la portada). */
@Composable
fun BotonIcono(
    icono: ImageVector,
    descripcion: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tinte: Color = Colores.Texto,
    fondo: Color = Colores.SuperficieAlta,
    tamano: Int = 40
) {
    FilledIconButton(
        onClick = onClick,
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(containerColor = fondo, contentColor = tinte),
        modifier = modifier.size(tamano.dp)
    ) {
        Icon(icono, contentDescription = descripcion, modifier = Modifier.size((tamano * 0.5f).dp))
    }
}

@Composable
private fun coloresLlenos(color: Color): ButtonColors = ButtonDefaults.buttonColors(
    containerColor = color,
    contentColor = Color.White,
    disabledContainerColor = color.copy(alpha = 0.45f),
    disabledContentColor = Color.White.copy(alpha = 0.8f)
)
