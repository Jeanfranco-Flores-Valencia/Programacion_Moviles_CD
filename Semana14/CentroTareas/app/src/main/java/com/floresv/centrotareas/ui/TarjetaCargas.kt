package com.floresv.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Splitscreen
import androidx.compose.material.icons.outlined.ViewStream
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.floresv.centrotareas.model.CargaUi
import com.floresv.centrotareas.ui.components.BotonPrincipal
import com.floresv.centrotareas.ui.components.BotonSecundario
import com.floresv.centrotareas.ui.components.EstadoTarea
import com.floresv.centrotareas.ui.components.FilaAcciones
import com.floresv.centrotareas.ui.components.Resultado
import com.floresv.centrotareas.ui.components.TarjetaTarea

/** Tarjeta 3: dos cargas simuladas, una por una contra a la vez, con error simulado y reintento. */
@Composable
fun TarjetaCargas(
    carga: CargaUi,
    simularError: Boolean,
    onSimularError: (Boolean) -> Unit,
    onUnaPorUna: () -> Unit,
    onALaVez: () -> Unit
) {
    val estado = when (carga) {
        CargaUi.Inicial -> EstadoTarea.LISTA
        CargaUi.Cargando -> EstadoTarea.EN_CURSO
        is CargaUi.Exito -> EstadoTarea.COMPLETADA
        is CargaUi.Error -> EstadoTarea.ERROR
    }
    val cargando = carga is CargaUi.Cargando

    TarjetaTarea("Dos cargas", "Perfil (2 s) y pedidos (3 s)", Icons.Outlined.CloudDownload, estado) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Simular error", style = MaterialTheme.typography.bodyLarge)
                Text("Los pedidos fallan con «Sin conexión»", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = simularError, onCheckedChange = onSimularError, enabled = !cargando)
        }
        // Deshabilitados mientras carga: no se lanza una segunda carga
        FilaAcciones {
            BotonSecundario("Una por una", Icons.Outlined.ViewStream, habilitado = !cargando, onClick = onUnaPorUna)
            BotonPrincipal("A la vez", Icons.Outlined.Splitscreen, habilitado = !cargando, onClick = onALaVez)
        }
        when (carga) {
            CargaUi.Inicial -> Unit
            CargaUi.Cargando -> Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text("Cargando datos simulados...", style = MaterialTheme.typography.bodySmall)
            }
            is CargaUi.Exito -> Resultado(
                "${carga.modo}: ${carga.milisegundos} ms",
                "${carga.perfil} · ${carga.pedidos} pedidos",
                exito = true
            )
            is CargaUi.Error -> {
                Resultado("Error al cargar", carga.mensaje, exito = false)
                FilaAcciones { BotonPrincipal("Reintentar", Icons.Outlined.Refresh, onClick = onALaVez) }
            }
        }
    }
}
