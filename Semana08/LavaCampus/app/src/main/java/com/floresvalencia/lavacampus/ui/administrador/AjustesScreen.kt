package com.floresvalencia.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.model.Rol
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.EstiloBoton
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.estudiante.FilaPreferencia
import com.floresvalencia.lavacampus.ui.estudiante.NotaDataStore
import com.floresvalencia.lavacampus.ui.estudiante.SeccionTitulo
import com.floresvalencia.lavacampus.ui.estudiante.Tarjeta
import com.floresvalencia.lavacampus.ui.estudiante.TarjetaCambiarPerfil
import com.floresvalencia.lavacampus.ui.estudiante.TarjetaUsuario
import com.floresvalencia.lavacampus.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(
    sesion: SesionViewModel,
    onPerfilCambiado: (Rol) -> Unit,
    onSesionCerrada: () -> Unit
) {
    val usuario by sesion.usuarioActual.collectAsState()
    val oscuro by sesion.modoOscuro.collectAsState()
    val notificaciones by sesion.notificaciones.collectAsState()
    val recibir by sesion.recibirPedidos.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        AppTopBar(titulo = "Ajustes")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            usuario?.let { TarjetaUsuario(it, etiqueta = "Perfil activo") }
            Spacer(Modifier.height(12.dp))
            TarjetaCambiarPerfil("Modo práctica: ir a vista Estudiante") {
                sesion.cambiarPerfil(onPerfilCambiado)
            }
            Spacer(Modifier.height(18.dp))
            SeccionTitulo("Preferencias")
            Tarjeta {
                FilaPreferencia("Modo oscuro", "Cambia el tema de la app", oscuro, sesion::cambiarModoOscuro)
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = colores.outlineVariant)
                FilaPreferencia("Notificaciones", "Avisar pedidos nuevos", notificaciones, sesion::cambiarNotificaciones)
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = colores.outlineVariant)
                FilaPreferencia("Recibir pedidos", "Abrir o cerrar la recepción", recibir, sesion::cambiarRecibirPedidos)
            }
            NotaDataStore()
            Spacer(Modifier.height(18.dp))
            LavaButton(texto = "Cerrar sesión", onClick = { sesion.cerrarSesion(onSesionCerrada) }, estilo = EstiloBoton.PELIGRO)
        }
    }
}
