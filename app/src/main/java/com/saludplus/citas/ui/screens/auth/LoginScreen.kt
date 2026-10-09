package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.R
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.CampoFormulario
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulOscuro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun LoginScreen(navController: NavHostController) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var errorUsuario by rememberSaveable { mutableStateOf<String?>(null) }
    var errorPassword by rememberSaveable { mutableStateOf<String?>(null) }
    var errorLogin by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior(titulo = "", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo_saludplus),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text("Clínica SaludPlus", style = MaterialTheme.typography.titleLarge, color = AzulOscuro)
            Spacer(Modifier.height(24.dp))
            Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium, color = TextoPrincipal)
            Spacer(Modifier.height(4.dp))
            Text(
                "Ingresa con tu teléfono o correo",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
            Spacer(Modifier.height(28.dp))

            CampoFormulario(
                etiqueta = "Teléfono o correo",
                valor = usuario,
                onValorCambio = { usuario = it.trim(); errorUsuario = null; errorLogin = null },
                icono = Icons.Filled.Person,
                error = errorUsuario,
                tipoTeclado = KeyboardType.Email,
                placeholder = "987654321"
            )
            Spacer(Modifier.height(12.dp))
            CampoFormulario(
                etiqueta = "Contraseña",
                valor = password,
                onValorCambio = { password = it; errorPassword = null; errorLogin = null },
                icono = Icons.Filled.Lock,
                error = errorPassword,
                tipoTeclado = KeyboardType.Password,
                esPassword = true
            )

            if (errorLogin != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    errorLogin!!,
                    color = Rojo,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(24.dp))
            BotonPrincipal(
                texto = "Ingresar",
                onClick = {
                    errorUsuario = if (usuario.isBlank()) "Ingresa tu teléfono o correo" else null
                    errorPassword = if (password.isBlank()) "Ingresa tu contraseña" else null
                    if (errorUsuario == null && errorPassword == null) {
                        if (Repositorio.iniciarSesion(usuario, password)) {
                            navController.navigate(Rutas.HOME) {
                                popUpTo(Rutas.SPLASH) { inclusive = true }
                            }
                        } else {
                            errorLogin = "Teléfono/correo o contraseña incorrectos"
                        }
                    }
                }
            )

            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿No tienes cuenta? ", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                Text(
                    "Regístrate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AzulPrimario,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        navController.navigate(Rutas.REGISTRO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            Spacer(Modifier.height(24.dp))
            // Ayuda para probar la app (usuario precargado en el Repositorio)
            Text(
                "Cuenta de prueba: 987654321 · contraseña 123456",
                style = MaterialTheme.typography.bodyMedium,
                color = AzulOscuro,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AzulClaro)
                    .padding(12.dp)
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}
