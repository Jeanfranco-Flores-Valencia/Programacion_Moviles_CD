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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.saludplus.citas.ui.components.FechaUtils
import com.saludplus.citas.ui.components.MensajeVacio
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun FechaHoraScreen(navController: NavHostController, medicoId: Int) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val hoy = remember { LocalDate.now() }

    // 0 = semana actual; no se puede ir a valores negativos
    var semana by rememberSaveable { mutableIntStateOf(0) }
    var fechaSeleccionada by rememberSaveable { mutableStateOf<String?>(null) }
    var horaSeleccionada by rememberSaveable { mutableStateOf<String?>(null) }

    // Próximos 5 días hábiles a partir de hoy (o de hoy + N semanas)
    val dias = remember(semana) { FechaUtils.diasHabiles(hoy.plusWeeks(semana.toLong())) }

    // Horarios reactivos: se recalculan solos al cambiar el día o al reservarse una cita.
    // Si el día elegido es hoy, también se ocultan las horas que ya pasaron.
    val horarios = fechaSeleccionada?.let { fecha ->
        val libres = Repositorio.horariosDisponibles(medicoId, fecha)
        if (fecha == hoy.toString()) {
            val ahora = LocalTime.now()
            libres.filter { LocalTime.parse(it).isAfter(ahora) }
        } else libres
    } ?: emptyList()

    fun cambiarSemana(nueva: Int) {
        semana = nueva
        fechaSeleccionada = null   // la semana cambió: se limpia la selección
        horaSeleccionada = null
    }

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

            // ----- Calendario dinámico -----
            TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { cambiarSemana(semana - 1) }, enabled = semana > 0) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Semana anterior")
                        }
                        Text(
                            FechaUtils.tituloMes(dias),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextoPrincipal
                        )
                        IconButton(onClick = { cambiarSemana(semana + 1) }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dias.forEach { dia ->
                            val iso = dia.toString()
                            ChipDia(
                                nombreCorto = FechaUtils.nombreDiaCorto(dia),
                                numero = dia.dayOfMonth.toString(),
                                seleccionado = iso == fechaSeleccionada,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    if (iso != fechaSeleccionada) {
                                        fechaSeleccionada = iso
                                        horaSeleccionada = null // al cambiar de día se reinicia la hora
                                    }
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
    nombreCorto: String,
    numero: String,
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
            nombreCorto,
            style = MaterialTheme.typography.bodyMedium,
            color = if (seleccionado) Blanco else TextoSecundario
        )
        Text(
            numero,
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
