package com.tecsup.manhwas.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.components.Aviso
import com.tecsup.manhwas.ui.components.BotonPrimario
import com.tecsup.manhwas.ui.components.CampoTexto
import com.tecsup.manhwas.ui.components.Logo
import com.tecsup.manhwas.ui.components.TipoAviso
import com.tecsup.manhwas.ui.theme.Colores

/**
 * Ingreso con correo y contrasena. Los usuarios se crean en la consola de Firebase (no hay registro).
 * Mejora IA: tarjeta negra con brillo morado y componentes reutilizables (CampoTexto, BotonPrimario, Aviso).
 */
@Composable
fun LoginScreen(vm: LoginViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(listOf(Colores.MoradoOscuro, Colores.Fondo), radius = 1400f))
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Colores.Superficie.copy(alpha = 0.92f))
                .border(1.dp, Colores.Morado.copy(alpha = 0.45f), RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(72.dp).clip(CircleShape).background(Brush.linearGradient(listOf(Colores.Morado, Colores.MoradoOscuro)))
            ) {
                Icon(Icons.Outlined.AutoStories, contentDescription = null, tint = Colores.Texto, modifier = Modifier.size(38.dp))
            }
            Logo(tamano = 28.sp)
            Text(
                "Sistemas, mazmorras y monstruos. Inicia sesión para entrar al catálogo.",
                style = MaterialTheme.typography.bodyMedium,
                color = Colores.TextoSuave,
                textAlign = TextAlign.Center
            )

            CampoTexto(
                valor = correo,
                onCambio = { correo = it; vm.limpiarError() },
                etiqueta = "Correo electrónico",
                icono = Icons.Outlined.Email,
                teclado = KeyboardType.Email
            )
            CampoTexto(
                valor = clave,
                onCambio = { clave = it; vm.limpiarError() },
                etiqueta = "Contraseña",
                icono = Icons.Outlined.Lock,
                esClave = true,
                imeAction = ImeAction.Done,
                onAccion = { vm.iniciarSesion(correo, clave) }
            )

            (estado as? UiState.Error)?.let { Aviso(it.mensaje, TipoAviso.Error) }

            BotonPrimario(
                texto = "Ingresar",
                onClick = { vm.iniciarSesion(correo, clave) },
                icono = Icons.AutoMirrored.Outlined.Login,
                cargando = estado is UiState.Cargando
            )
        }
    }
}
