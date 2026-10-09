package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.AvatarIniciales
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonSecundario
import com.saludplus.citas.ui.components.FilaDetalle
import com.saludplus.citas.ui.components.IconoEnCaja
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun PerfilScreen(navController: NavHostController) {
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size

    Scaffold(
        containerColor = Blanco,
        topBar = {
            BarraSuperior("Mi perfil", onAtras = { navController.popBackStack(Rutas.HOME, inclusive = false) })
        },
        bottomBar = { BarraNavegacionInferior(navController, Rutas.PERFIL) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            AvatarIniciales(usuario?.nombre ?: "Paciente", tamano = 88.dp)
            Spacer(Modifier.height(10.dp))
            Text(usuario?.nombre ?: "Paciente", style = MaterialTheme.typography.titleLarge, color = TextoPrincipal)
            Text("Paciente", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            Spacer(Modifier.height(20.dp))

            // ----- Mis datos -----
            Text(
                "Mis datos",
                style = MaterialTheme.typography.titleMedium,
                color = TextoPrincipal,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                    FilaDetalle(Icons.Filled.Person, "Nombre completo", usuario?.nombre ?: "-")
                    HorizontalDivider(color = BordeSuave)
                    FilaDetalle(Icons.Filled.Phone, "Teléfono", usuario?.telefono ?: "-")
                    HorizontalDivider(color = BordeSuave)
                    FilaDetalle(Icons.Filled.Email, "Correo", usuario?.correo?.ifBlank { "No registrado" } ?: "-")
                    HorizontalDivider(color = BordeSuave)
                    FilaDetalle(Icons.Filled.EventAvailable, "Citas programadas", "$totalCitas")
                }
            }

            // ----- Opciones -----
            Spacer(Modifier.height(16.dp))
            TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                Column {
                    OpcionPerfil(Icons.Filled.EventAvailable, "Mis citas") {
                        navController.navigate(Rutas.MIS_CITAS) {
                            popUpTo(Rutas.HOME)
                            launchSingleTop = true
                        }
                    }
                    HorizontalDivider(color = BordeSuave)
                    OpcionPerfil(Icons.Filled.Notifications, "Notificaciones") {
                        navController.navigate(Rutas.NOTIFICACIONES)
                    }
                    HorizontalDivider(color = BordeSuave)
                    OpcionPerfil(Icons.Filled.Gavel, "Términos y condiciones") {
                        navController.navigate(Rutas.TERMINOS)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            BotonSecundario(
                texto = "Cerrar sesión",
                color = Rojo,
                onClick = {
                    Repositorio.cerrarSesion()
                    // Se limpia todo el historial y se vuelve al Splash
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OpcionPerfil(icono: ImageVector, texto: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconoEnCaja(icono = icono, tamano = 40.dp)
        Spacer(Modifier.width(14.dp))
        Text(texto, style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextoSecundario)
    }
}
