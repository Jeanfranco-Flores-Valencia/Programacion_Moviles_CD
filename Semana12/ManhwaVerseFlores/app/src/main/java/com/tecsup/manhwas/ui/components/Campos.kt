package com.tecsup.manhwas.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.tecsup.manhwas.ui.theme.Colores
import androidx.compose.ui.unit.dp

/** Colores de todos los campos: fondo oscuro, borde morado al escribir y texto claro. */
@Composable
private fun coloresCampo(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Colores.Superficie,
    unfocusedContainerColor = Colores.Superficie,
    focusedBorderColor = Colores.Morado,
    unfocusedBorderColor = Colores.Borde,
    focusedTextColor = Colores.Texto,
    unfocusedTextColor = Colores.Texto,
    focusedLabelColor = Colores.MoradoClaro,
    unfocusedLabelColor = Colores.TextoSuave,
    focusedLeadingIconColor = Colores.MoradoClaro,
    unfocusedLeadingIconColor = Colores.TextoSuave,
    focusedTrailingIconColor = Colores.TextoSuave,
    unfocusedTrailingIconColor = Colores.TextoSuave,
    focusedPlaceholderColor = Colores.TextoSuave,
    unfocusedPlaceholderColor = Colores.TextoSuave,
    cursorColor = Colores.Morado
)

/** Mejora IA: campo de texto reutilizable. Con esClave = true agrega el ojo para ver u ocultar. */
@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    esClave: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAccion: () -> Unit = {}
) {
    var ver by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        trailingIcon = if (esClave) {
            {
                IconButton(onClick = { ver = !ver }) {
                    Icon(
                        if (ver) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (ver) "Ocultar contraseña" else "Ver contraseña"
                    )
                }
            }
        } else null,
        visualTransformation = if (esClave && !ver) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (esClave) KeyboardType.Password else teclado, imeAction = imeAction),
        keyboardActions = KeyboardActions(onAny = { onAccion() }),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = coloresCampo(),
        modifier = modifier.fillMaxWidth()
    )
}

/** Barra de busqueda del catalogo, con boton para borrar. */
@Composable
fun BarraBusqueda(valor: String, onCambio: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        placeholder = { Text("Buscar manhwa...") },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
        trailingIcon = if (valor.isNotEmpty()) {
            { IconButton(onClick = { onCambio("") }) { Icon(Icons.Outlined.Close, contentDescription = "Borrar búsqueda") } }
        } else null,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        singleLine = true,
        shape = RoundedCornerShape(50),
        colors = coloresCampo(),
        modifier = modifier.fillMaxWidth()
    )
}
