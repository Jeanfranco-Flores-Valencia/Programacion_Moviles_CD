package com.tecsup.authfirebaseflores

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

/** Mejora IA: estilo de tarjeta translucida que se usa en todas las pantallas. */
fun Modifier.tarjeta(): Modifier = this
    .clip(RoundedCornerShape(24.dp))
    .background(Paleta.Tarjeta)
    .border(1.dp, Paleta.Borde, RoundedCornerShape(24.dp))

/** Colores de los campos de texto sobre la tarjeta oscura. */
@Composable
private fun coloresCampo(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Paleta.Celeste,
    unfocusedBorderColor = Paleta.Borde,
    focusedLabelColor = Paleta.CelesteClaro,
    unfocusedLabelColor = Paleta.TextoSuave,
    cursorColor = Paleta.Celeste,
    focusedTextColor = Paleta.Texto,
    unfocusedTextColor = Paleta.Texto,
    focusedLeadingIconColor = Paleta.CelesteClaro,
    unfocusedLeadingIconColor = Paleta.TextoSuave,
    focusedTrailingIconColor = Paleta.TextoSuave,
    unfocusedTrailingIconColor = Paleta.TextoSuave,
    focusedPlaceholderColor = Paleta.TextoSuave.copy(alpha = 0.6f),
    unfocusedPlaceholderColor = Paleta.TextoSuave.copy(alpha = 0.6f),
    focusedContainerColor = Color.White.copy(alpha = 0.04f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.02f)
)

/** Campo de texto con icono a la izquierda. */
@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    placeholder: String = "",
    teclado: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAccion: () -> Unit = {}
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = teclado, imeAction = imeAction),
        keyboardActions = KeyboardActions(onAny = { onAccion() }),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = coloresCampo(),
        modifier = Modifier.fillMaxWidth()
    )
}

/** Campo de contrasena con candado, ojo para ver/ocultar y check opcional cuando es correcta. */
@Composable
fun CampoClave(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    imeAction: ImeAction = ImeAction.Done,
    onAccion: () -> Unit = {},
    correcta: Boolean = false
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(visible = correcta, enter = scaleIn() + fadeIn(), exit = scaleOut() + fadeOut()) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = "Coincide", tint = Paleta.Exito)
                }
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (visible) "Ocultar contraseña" else "Ver contraseña"
                    )
                }
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions = KeyboardActions(onAny = { onAccion() }),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = coloresCampo(),
        modifier = Modifier.fillMaxWidth()
    )
}

/** Boton principal celeste: se encoge un poco al presionarlo y muestra un spinner mientras carga. */
@Composable
fun BotonPrincipal(texto: String, cargando: Boolean, onClick: () -> Unit) {
    val interaccion = remember { MutableInteractionSource() }
    val presionado by interaccion.collectIsPressedAsState()
    val escala by animateFloatAsState(if (presionado) 0.97f else 1f, label = "escala")

    Button(
        onClick = onClick,
        enabled = !cargando,
        interactionSource = interaccion,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Paleta.Celeste,
            contentColor = Color.White,
            disabledContainerColor = Paleta.Celeste.copy(alpha = 0.6f),
            disabledContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
            }
    ) {
        AnimatedContent(targetState = cargando, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "boton") { carga ->
            if (carga) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.5.dp, modifier = Modifier.size(22.dp))
            } else {
                Text(texto, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/** Aviso dentro de la tarjeta: error en rojo suave o informacion en verde. */
data class Aviso(val texto: String, val esError: Boolean = true)

@Composable
fun MensajeAviso(aviso: Aviso) {
    val color = if (aviso.esError) Paleta.Error else Paleta.Exito
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Icon(
            if (aviso.esError) Icons.Outlined.ErrorOutline else Icons.Outlined.MarkEmailRead,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Text(aviso.texto, style = MaterialTheme.typography.bodySmall, color = Paleta.Texto)
    }
}

/** Barra animada que indica que tan segura es la contrasena mientras se escribe. */
@Composable
fun IndicadorClave(clave: String) {
    var nivel = 0
    if (clave.length >= 6) nivel++
    if (clave.length >= 10) nivel++
    if (clave.any { it.isDigit() } && clave.any { it.isLetter() }) nivel++
    if (clave.any { it.isUpperCase() } || clave.any { !it.isLetterOrDigit() }) nivel++

    val (texto, color) = when (nivel) {
        0, 1 -> "Débil" to Paleta.Error
        2 -> "Aceptable" to Paleta.Ambar
        3 -> "Buena" to Paleta.CelesteClaro
        else -> "Fuerte" to Paleta.Exito
    }
    val progreso by animateFloatAsState(maxOf(nivel, 1) / 4f, animationSpec = tween(400), label = "progreso")
    val colorAnimado by animateColorAsState(color, animationSpec = tween(400), label = "color")

    Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Paleta.Borde)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progreso)
                    .fillMaxHeight()
                    .background(colorAnimado)
            )
        }
        Text(
            "Seguridad: $texto",
            style = MaterialTheme.typography.labelSmall,
            color = colorAnimado,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/** Sacude un elemento de lado a lado (se usa en la tarjeta cuando hay un error). */
suspend fun Animatable<Float, AnimationVector1D>.sacudir() {
    animateTo(0f, keyframes {
        durationMillis = 360
        -14f at 40
        14f at 100
        -10f at 160
        10f at 220
        -4f at 280
        0f at 360
    })
}

/** Convierte el error de Firebase en un mensaje claro en espanol. */
fun mensajeDeError(e: Exception?): String = when (e) {
    is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil (mínimo 6 caracteres)"
    is FirebaseAuthUserCollisionException -> "Ese correo ya está registrado"
    is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo"
    is FirebaseAuthInvalidCredentialsException ->
        if (e.errorCode == "ERROR_INVALID_EMAIL") "El correo no tiene un formato válido"
        else "Correo o contraseña incorrectos"
    is FirebaseNetworkException -> "Sin conexión a internet"
    is FirebaseTooManyRequestsException -> "Demasiados intentos. Espera un momento"
    else -> "Error: ${e?.message ?: "desconocido"}"
}
