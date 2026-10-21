package com.floresvalencia.lavacampus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Campo de texto con el estilo de la app.
 * mensajeError: si no es null, el campo se marca en rojo y el texto aparece debajo.
 */
@Composable
fun LavaTextField(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    esClave: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text,
    unaLinea: Boolean = true,
    placeholder: String? = null,
    esError: Boolean = false,
    mensajeError: String? = null,
    imeAction: ImeAction = ImeAction.Default,
    onAccion: (() -> Unit)? = null
) {
    // Mejora IA: el ojo permite ver u ocultar la contrasena
    var claveVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        placeholder = if (placeholder != null) {
            { Text(placeholder) }
        } else null,
        leadingIcon = if (icono != null) {
            { Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        } else null,
        trailingIcon = if (esClave) {
            {
                IconButton(onClick = { claveVisible = !claveVisible }) {
                    Icon(
                        if (claveVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (claveVisible) "Ocultar contraseña" else "Ver contraseña",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else null,
        supportingText = if (mensajeError != null) {
            { Text(mensajeError) }
        } else null,
        singleLine = unaLinea,
        isError = esError || mensajeError != null,
        visualTransformation = if (esClave && !claveVisible) PasswordVisualTransformation() else VisualTransformation.None,
        // Mejora IA: "Siguiente" pasa al otro campo y "Listo" ejecuta la accion del formulario
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esClave) KeyboardType.Password else teclado,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onDone = { onAccion?.invoke() }),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier.fillMaxWidth()
    )
}
