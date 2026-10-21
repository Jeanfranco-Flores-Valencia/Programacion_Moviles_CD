package com.floresvalencia.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.components.LavaTextField
import com.floresvalencia.lavacampus.viewmodel.CampoRegistro
import com.floresvalencia.lavacampus.viewmodel.SesionViewModel

/**
 * Registro de estudiantes. La View solo muestra los campos y los errores;
 * las validaciones viven en SesionViewModel. Al crear la cuenta lleva al login.
 */
@Composable
fun RegistroScreen(
    vm: SesionViewModel,
    onAtras: () -> Unit,
    onRegistrado: () -> Unit,
    onTengoCuenta: () -> Unit
) {
    val errores by vm.erroresRegistro.collectAsState()
    val registroOk by vm.registroOk.collectAsState()
    val registrando by vm.registrando.collectAsState()
    val colores = MaterialTheme.colorScheme

    var nombre by rememberSaveable { mutableStateOf("") }
    var codigo by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(registroOk) {
        if (registroOk) {
            vm.registroAtendido()
            onRegistrado()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        AppTopBar(titulo = "Crear cuenta", onAtras = onAtras)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            Text("Regístrate", style = MaterialTheme.typography.headlineMedium, color = colores.onBackground)
            Text(
                "Crea tu cuenta de estudiante para pedir tu lavado",
                style = MaterialTheme.typography.bodyMedium,
                color = colores.onSurfaceVariant
            )
            Spacer(Modifier.height(20.dp))

            LavaTextField(
                valor = nombre,
                onCambio = { nombre = it; vm.limpiarCampo(CampoRegistro.NOMBRE) },
                etiqueta = "Nombre y apellido",
                icono = Icons.Outlined.Person,
                mensajeError = errores.nombre
            )
            Spacer(Modifier.height(8.dp))
            LavaTextField(
                valor = codigo,
                onCambio = { codigo = it.uppercase().take(10); vm.limpiarCampo(CampoRegistro.CODIGO) },
                etiqueta = "Código de estudiante",
                placeholder = "U202310234",
                icono = Icons.Outlined.Badge,
                mensajeError = errores.codigo
            )
            Spacer(Modifier.height(8.dp))
            LavaTextField(
                valor = correo,
                onCambio = { correo = it.trim(); vm.limpiarCampo(CampoRegistro.CORREO) },
                etiqueta = "Correo",
                icono = Icons.Outlined.Email,
                teclado = KeyboardType.Email,
                mensajeError = errores.correo
            )
            Spacer(Modifier.height(8.dp))
            LavaTextField(
                valor = clave,
                onCambio = { clave = it; vm.limpiarCampo(CampoRegistro.CLAVE) },
                etiqueta = "Contraseña (mínimo 6, letras y números)",
                icono = Icons.Outlined.Lock,
                esClave = true,
                mensajeError = errores.clave
            )
            Spacer(Modifier.height(8.dp))
            LavaTextField(
                valor = confirmar,
                onCambio = { confirmar = it; vm.limpiarCampo(CampoRegistro.CONFIRMAR) },
                etiqueta = "Confirmar contraseña",
                icono = Icons.Outlined.Lock,
                esClave = true,
                mensajeError = errores.confirmar
            )
            Spacer(Modifier.height(20.dp))
            LavaButton(
                texto = if (registrando) "Creando cuenta..." else "Crear cuenta",
                onClick = { vm.registrar(nombre, codigo, correo, clave, confirmar) },
                enabled = !registrando
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "¿Ya tienes cuenta? Inicia sesión",
                style = MaterialTheme.typography.labelLarge,
                color = colores.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onTengoCuenta)
                    .padding(vertical = 8.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Modo práctica: usa datos ficticios (Ley 29733).",
                style = MaterialTheme.typography.bodySmall,
                color = colores.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
