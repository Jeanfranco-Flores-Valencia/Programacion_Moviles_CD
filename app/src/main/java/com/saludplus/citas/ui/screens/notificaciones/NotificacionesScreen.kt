package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.IconoEnCaja
import com.saludplus.citas.ui.components.MensajeVacio
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.components.textoFecha
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

private data class Notificacion(val citaId: Int, val titulo: String, val mensaje: String)

@Composable
fun NotificacionesScreen(navController: NavHostController) {
    // map: cada cita del usuario se transforma en un recordatorio
    val notificaciones = Repositorio.citasDelUsuario().map { cita ->
        val medico = Repositorio.obtenerMedico(cita.medicoId)
        val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
        Notificacion(
            citaId = cita.id,
            titulo = "Recordatorio de cita",
            mensaje = "${especialidad?.nombre ?: "Consulta"} con ${medico?.nombre ?: "tu médico"} " +
                "el ${textoFecha(cita.fecha)} a las ${cita.hora}."
        )
    }

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Notificaciones", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        if (notificaciones.isEmpty()) {
            MensajeVacio(
                icono = Icons.Filled.NotificationsNone,
                titulo = "Sin notificaciones",
                mensaje = "Aquí verás los recordatorios de tus citas.",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notificaciones, key = { it.citaId }) { notificacion ->
                    TarjetaBase(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate(Rutas.detalleCita(notificacion.citaId)) }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconoEnCaja(icono = Icons.Filled.NotificationsActive, circular = true)
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text(notificacion.titulo, style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                                Text(notificacion.mensaje, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                            }
                        }
                    }
                }
            }
        }
    }
}
