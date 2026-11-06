package com.tecsup.authfirebaseflores

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Formulario de inicio de sesion con correo y contrasena (Firebase Authentication).
 * Mejora IA: vive dentro de la tarjeta de AuthScreen; los errores se muestran en la tarjeta
 * y se puede pedir un enlace para recuperar la contrasena.
 */
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val foco = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val sacudida = remember { Animatable(0f) }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var aviso by remember { mutableStateOf<Aviso?>(null) }

    fun avisar(nuevo: Aviso) {
        aviso = nuevo
        if (nuevo.esError) scope.launch { sacudida.sacudir() }
    }

    fun ingresar() {
        foco.clearFocus()
        val correo = email.trim()
        when {
            correo.isBlank() || password.isBlank() -> avisar(Aviso("Complete todos los campos"))
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> avisar(Aviso("El correo no tiene un formato válido"))
            else -> {
                isLoading = true
                aviso = null
                auth.signInWithEmailAndPassword(correo, password)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) onLoginSuccess()
                        else avisar(Aviso(mensajeDeError(task.exception)))
                    }
            }
        }
    }

    /** Mejora IA: Firebase envia al correo un enlace para crear una nueva contrasena. */
    fun recuperarClave() {
        foco.clearFocus()
        val correo = email.trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            avisar(Aviso("Escribe tu correo arriba para enviarte el enlace"))
            return
        }
        isLoading = true
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { task ->
                isLoading = false
                avisar(
                    if (task.isSuccessful) Aviso("Si $correo tiene una cuenta, te llegará un enlace para cambiar tu contraseña", esError = false)
                    else Aviso(mensajeDeError(task.exception))
                )
            }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(sacudida.value.roundToInt(), 0) }
    ) {
        CampoTexto(
            valor = email,
            onCambio = { email = it; aviso = null },
            etiqueta = "Correo electrónico",
            icono = Icons.Outlined.Email,
            placeholder = "tu.nombre@tecsup.edu.pe",
            teclado = KeyboardType.Email,
            onAccion = { foco.moveFocus(FocusDirection.Down) }
        )
        CampoClave(
            valor = password,
            onCambio = { password = it; aviso = null },
            etiqueta = "Contraseña",
            imeAction = ImeAction.Done,
            onAccion = { ingresar() }
        )
        TextButton(
            onClick = { recuperarClave() },
            enabled = !isLoading,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("¿Olvidaste tu contraseña?", color = Paleta.TextoSuave, style = MaterialTheme.typography.labelLarge)
        }

        AnimatedVisibility(
            visible = aviso != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            aviso?.let { MensajeAviso(it) }
        }

        Spacer(Modifier.height(4.dp))
        BotonPrincipal(texto = "Ingresar", cargando = isLoading, onClick = { ingresar() })

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta?", style = MaterialTheme.typography.bodyMedium, color = Paleta.TextoSuave)
            TextButton(onClick = onNavigateToRegister) {
                Text("Regístrate aquí", color = Paleta.CelesteClaro, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
