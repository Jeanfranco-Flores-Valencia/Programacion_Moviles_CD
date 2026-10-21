package com.floresvalencia.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.model.Rol
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.components.LavaTextField
import com.floresvalencia.lavacampus.ui.theme.LavaTheme
import com.floresvalencia.lavacampus.viewmodel.SesionViewModel

/** Un solo Composable para ambos roles: el color cambia segun la pestana elegida. */
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onIngresar: (Rol) -> Unit,
    onRegistrarse: () -> Unit
) {
    val usuario by vm.usuarioActual.collectAsState()
    val error by vm.error.collectAsState()
    val oscuro by vm.modoOscuro.collectAsState()
    val correoRegistrado by vm.correoRegistrado.collectAsState()

    var rolElegido by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    // Viene del registro: el correo ya aparece escrito en la pestana Estudiante
    LaunchedEffect(correoRegistrado) {
        correoRegistrado?.let {
            correo = it
            rolElegido = Rol.CLIENTE
        }
    }

    // Exito: el ViewModel guardo la sesion; la vista solo navega
    LaunchedEffect(usuario) {
        val u = usuario ?: return@LaunchedEffect
        Rol.desde(u.rol)?.let(onIngresar)
    }

    LavaTheme(rol = rolElegido, oscuro = oscuro) {
        val colores = MaterialTheme.colorScheme
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colores.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text("Bienvenido", style = MaterialTheme.typography.headlineMedium, color = colores.onBackground)
            Text("Ingresa para continuar", style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))

            if (correoRegistrado != null) {
                Text(
                    "¡Cuenta creada! Ingresa con tu correo y contraseña.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colores.onPrimaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colores.primaryContainer)
                        .padding(10.dp)
                )
                Spacer(Modifier.height(14.dp))
            }

            // Selector Estudiante / Administrador
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colores.surfaceVariant)
                    .padding(4.dp)
            ) {
                Rol.entries.forEach { rol ->
                    val elegido = rol == rolElegido
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (elegido) colores.primary else colores.surfaceVariant)
                            .clickable {
                                rolElegido = rol
                                vm.limpiarError()
                            }
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            rol.etiqueta,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (elegido) colores.onPrimary else colores.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            LavaTextField(
                valor = correo,
                onCambio = { correo = it; vm.limpiarError() },
                etiqueta = "Correo",
                icono = Icons.Outlined.Email,
                teclado = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(12.dp))
            LavaTextField(
                valor = clave,
                onCambio = { clave = it; vm.limpiarError() },
                etiqueta = "Contraseña",
                icono = Icons.Outlined.Lock,
                esClave = true,
                imeAction = ImeAction.Done,
                onAccion = { vm.login(correo, clave, rolElegido) }
            )
            Text(
                "¿Olvidaste tu contraseña?",
                style = MaterialTheme.typography.labelMedium,
                color = colores.primary,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            if (error != null) {
                Text(
                    error ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.error,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colores.errorContainer)
                        .padding(10.dp)
                )
            }
            Spacer(Modifier.height(20.dp))
            LavaButton(texto = "Ingresar", onClick = { vm.login(correo, clave, rolElegido) })
            Spacer(Modifier.height(16.dp))

            if (rolElegido == Rol.CLIENTE) {
                Text(
                    "¿No tienes cuenta? Regístrate",
                    style = MaterialTheme.typography.labelLarge,
                    color = colores.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onRegistrarse)
                        .padding(vertical = 8.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colores.primaryContainer)
                    .padding(14.dp)
            ) {
                Text("Modo práctica", style = MaterialTheme.typography.titleSmall, color = colores.onPrimaryContainer)
                Text(
                    if (rolElegido == Rol.CLIENTE) "Prueba con ana@correo.test / 1234"
                    else "Prueba con admin@lavacampus.test / admin",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onPrimaryContainer
                )
                Text(
                    "Puedes cambiar de perfil desde Perfil o Ajustes para probar ambos roles.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onPrimaryContainer
                )
            }
        }
    }
}
