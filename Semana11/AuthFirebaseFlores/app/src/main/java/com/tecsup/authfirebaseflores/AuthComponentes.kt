package com.tecsup.authfirebaseflores

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

/** Campo de contrasena con boton para ver u ocultar lo escrito. */
@Composable
fun CampoClave(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            TextButton(onClick = { visible = !visible }) {
                Text(if (visible) "Ocultar" else "Ver")
            }
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
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
