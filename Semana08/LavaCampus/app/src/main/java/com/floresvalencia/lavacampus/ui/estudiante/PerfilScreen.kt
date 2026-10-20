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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.data.local.entity.Usuario
import com.floresvalencia.lavacampus.model.Rol
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.EstiloBoton
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.components.numeroTexto
import com.floresvalencia.lavacampus.viewmodel.MisPedidosViewModel
import com.floresvalencia.lavacampus.viewmodel.SesionViewModel

@Composable
fun PerfilScreen(
    sesion: SesionViewModel,
    pedidosVm: MisPedidosViewModel,
    onAtras: () -> Unit,
    onPerfilCambiado: (Rol) -> Unit,
    onSesionCerrada: () -> Unit
) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val avisos by sesion.notificaciones.collectAsState()
    val stats by pedidosVm.estadisticas.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        AppTopBar(titulo = "Perfil", onAtras = onAtras)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            usuario?.let { TarjetaUsuario(it, etiqueta = "Estudiante") }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Contador(stats.activos.toString(), "Pedidos activos", Modifier.weight(1f))
                Contador(stats.totales.toString(), "Pedidos totales", Modifier.weight(1f))
                Contador("${numeroTexto(stats.kilos)} kg", "Lavados", Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            TarjetaCambiarPerfil("Modo práctica: ir a vista Administrador") {
                sesion.cambiarPerfil(onPerfilCambiado)
            }
            Spacer(Modifier.height(18.dp))
            SeccionTitulo("Preferencias")
            Tarjeta {
                FilaPreferencia("Modo oscuro", "Cambia el tema de la app", oscuro, sesion::cambiarModoOscuro)
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = colores.outlineVariant)
                FilaPreferencia("Avisos de pedido listo", "Notificar cuando puedas recoger", avisos, sesion::cambiarNotificaciones)
            }
            NotaDataStore()
            Spacer(Modifier.height(18.dp))
            LavaButton(texto = "Cerrar sesión", onClick = { sesion.cerrarSesion(onSesionCerrada) }, estilo = EstiloBoton.PELIGRO)
        }
    }
}

@Composable
private fun Contador(valor: String, etiqueta: String, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(colores.primaryContainer)
            .padding(vertical = 12.dp)
    ) {
        Text(valor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = colores.primary)
        Text(etiqueta, style = MaterialTheme.typography.labelSmall, color = colores.onPrimaryContainer)
    }
}

/** Datos del usuario con avatar y chip del perfil. Se usa en Perfil y en Ajustes. */
@Composable
internal fun TarjetaUsuario(usuario: Usuario, etiqueta: String) {
    val colores = MaterialTheme.colorScheme
    Tarjeta {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(colores.primaryContainer)
            ) {
                Icon(Icons.Outlined.Person, contentDescription = null, tint = colores.primary)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(usuario.nombre, style = MaterialTheme.typography.titleMedium, color = colores.onSurface)
                Text(usuario.correo, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text(
                    etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = colores.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(colores.primaryContainer)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
    }
}

/** Tarjeta de color primario para cambiar de perfil (modo practica). */
@Composable
internal fun TarjetaCambiarPerfil(subtitulo: String, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colores.primary)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = colores.onPrimary)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("Cambiar de perfil", style = MaterialTheme.typography.titleSmall, color = colores.onPrimary)
            Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = colores.onPrimary.copy(alpha = 0.85f))
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = colores.onPrimary)
    }
}

@Composable
internal fun FilaPreferencia(titulo: String, subtitulo: String, valor: Boolean, onCambio: (Boolean) -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
            Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
        }
        Switch(checked = valor, onCheckedChange = onCambio)
    }
}

@Composable
internal fun NotaDataStore() {
    val colores = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colores.primaryContainer.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Text("Se guardan en el dispositivo", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
        Text("con DataStore", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = colores.primary)
    }
}
