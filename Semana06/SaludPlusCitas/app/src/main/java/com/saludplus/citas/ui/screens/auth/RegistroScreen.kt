package com.saludplus.citas.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoFormulario
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun RegistroScreen(navController: NavHostController) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var errorNombre by rememberSaveable { mutableStateOf<String?>(null) }
    var errorTelefono by rememberSaveable { mutableStateOf<String?>(null) }
    var errorCorreo by rememberSaveable { mutableStateOf<String?>(null) }
    var errorPassword by rememberSaveable { mutableStateOf<String?>(null) }
    var errorGeneral by rememberSaveable { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        errorNombre = when {
            nombre.isBlank() -> "Ingresa tu nombre completo"
            nombre.trim().length < 3 -> "El nombre es muy corto"
            !nombre.trim().all { it.isLetter() || it == ' ' } -> "Solo se permiten letras"
            else -> null
        }
        errorTelefono = when {
            telefono.isBlank() -> "Ingresa tu teléfono"
            !Regex("^9\\d{8}$").matches(telefono) -> "Debe tener 9 dígitos y empezar con 9"
            else -> null
        }
        errorCorreo = if (correo.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
            "Correo no válido"
        } else null
        errorPassword = if (password.length < 6) "Mínimo 6 caracteres" else null

        return errorNombre == null && errorTelefono == null && errorCorreo == null && errorPassword == null
    }

    Scaffold(containerColor = Blanco) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium, color = TextoPrincipal)
            Spacer(Modifier.height(4.dp))
            Text(
                "Regístrate para agendar tus citas",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
            Spacer(Modifier.height(28.dp))

            CampoFormulario(
                etiqueta = "Nombre completo",
                valor = nombre,
                onValorCambio = { nombre = it; errorNombre = null },
                icono = Icons.Filled.Person,
                error = errorNombre,
                placeholder = "Juan Pérez"
            )
            Spacer(Modifier.height(12.dp))
            CampoFormulario(
                etiqueta = "Teléfono",
                valor = telefono,
                onValorCambio = { texto ->
                    telefono = texto.filter { it.isDigit() }.take(9)
                    errorTelefono = null
                },
                icono = Icons.Filled.Phone,
                error = errorTelefono,
                tipoTeclado = KeyboardType.Phone,
                placeholder = "987654321"
            )
            Spacer(Modifier.height(12.dp))
            CampoFormulario(
                etiqueta = "Correo (opcional)",
                valor = correo,
                onValorCambio = { correo = it.trim(); errorCorreo = null },
                icono = Icons.Filled.Email,
                error = errorCorreo,
                tipoTeclado = KeyboardType.Email,
                placeholder = "juan@correo.com"
            )
            Spacer(Modifier.height(12.dp))
            CampoFormulario(
                etiqueta = "Contraseña",
                valor = password,
                onValorCambio = { password = it; errorPassword = null },
                icono = Icons.Filled.Lock,
                error = errorPassword,
                tipoTeclado = KeyboardType.Password,
                esPassword = true
            )

            if (errorGeneral != null) {
                Spacer(Modifier.height(8.dp))
                Text(errorGeneral!!, color = Rojo, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(24.dp))
            BotonPrincipal(
                texto = "Registrarme",
                onClick = {
                    errorGeneral = null
                    if (validar()) {
                        val registrado = Repositorio.registrarUsuario(nombre, telefono, correo, password)
                        if (registrado) {
                            navController.navigate(Rutas.HOME) {
                                popUpTo(Rutas.SPLASH) { inclusive = true }
                            }
                        } else {
                            errorGeneral = "Ya existe una cuenta con ese teléfono o correo"
                        }
                    }
                }
            )

            Spacer(Modifier.height(16.dp))
            Text(
                "Al registrarte aceptas nuestros",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
            Text(
                "Términos y Condiciones",
                style = MaterialTheme.typography.bodyMedium,
                color = AzulPrimario,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController.navigate(Rutas.TERMINOS) }
            )

            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿Ya tienes cuenta? ", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                Text(
                    "Iniciar sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AzulPrimario,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.REGISTRO) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
