package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.EncabezadoMedico
import com.saludplus.citas.ui.components.MensajeVacio
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

/** Día que se muestra en el calendario (fecha en formato yyyy-MM-dd). */
private data class DiaCalendario(val nombreCorto: String, val numero: String, val fecha: String)

// Fase 1: semana fija de días hábiles
private const val MES_FIJO = "Octubre 2026"
private val diasFijos = listOf(
    DiaCalendario("Lun", "12", "2026-10-12"),
    DiaCalendario("Mar", "13", "2026-10-13"),
    DiaCalendario("Mié", "14", "2026-10-14"),
    DiaCalendario("Jue", "15", "2026-10-15"),
    DiaCalendario("Vie", "16", "2026-10-16")
)

@Composable
fun FechaHoraScreen(navController: NavHostController, medicoId: Int) {
    val medico = Repositorio.obtenerMedico(medicoId)
    var fechaSeleccionada by rememberSaveable { mutableStateOf<String?>(null) }
    var horaSeleccionada by rememberSaveable { mutableStateOf<String?>(null) }

    // Horarios reactivos: si cambia el día (o se agenda una cita) se recalculan solos
    val horarios = fechaSeleccionada?.let { Repositorio.horariosDisponibles(medicoId, it) } ?: emptyList()

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Seleccionar fecha y hora", onAtras = { navController.popBackStack() }) },
        bottomBar = {
            BotonPrincipal(
                texto = "Continuar",
                habilitado = fechaSeleccionada != null && horaSeleccionada != null,
                onClick = {
                    val fecha = fechaSeleccionada
                    val hora = horaSeleccionada
                    if (fecha != null && hora != null) {
                        navController.navigate(Rutas.confirmarCita(medicoId, fecha, hora))
                    }
                },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            if (medico != null) {
                Spacer(Modifier.height(8.dp))
                EncabezadoMedico(medico)
            }
            Spacer(Modifier.height(16.dp))

            // ----- Calendario -----
            TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // En la Fase 1 la semana es fija: las flechas están deshabilitadas
                        IconButton(onClick = {}, enabled = false) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                        }
                        Text(
                            MES_FIJO,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextoPrincipal
                        )
                        IconButton(onClick = {}, enabled = false) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        diasFijos.forEach { dia ->
                            ChipDia(
                                dia = dia,
                                seleccionado = dia.fecha == fechaSeleccionada,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    fechaSeleccionada = dia.fecha
                                    horaSeleccionada = null // al cambiar de día se reinicia la hora
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Horarios disponibles", style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
            Spacer(Modifier.height(8.dp))

            when {
                fechaSeleccionada == null -> Text(
                    "Elige un día para ver los horarios del médico.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
                horarios.isEmpty() -> MensajeVacio(
                    icono = Icons.Filled.EventBusy,
                    titulo = "Sin horarios",
                    mensaje = "No quedan horarios libres para este día. Prueba con otro."
                )
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(horarios, key = { it }) { hora ->
                        ChipHora(
                            hora = hora,
                            seleccionado = hora == horaSeleccionada,
                            onClick = { horaSeleccionada = hora }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipDia(
    dia: DiaCalendario,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (seleccionado) AzulPrimario else FondoApp)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            dia.nombreCorto,
            style = MaterialTheme.typography.bodyMedium,
            color = if (seleccionado) Blanco else TextoSecundario
        )
        Text(
            dia.numero,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) Blanco else TextoPrincipal
        )
    }
}

@Composable
private fun ChipHora(hora: String, seleccionado: Boolean, onClick: () -> Unit) {
    val forma = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(forma)
            .background(if (seleccionado) AzulPrimario else Blanco)
            .border(1.dp, if (seleccionado) AzulPrimario else BordeSuave, forma)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            hora,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = if (seleccionado) Blanco else TextoPrincipal
        )
    }
}
