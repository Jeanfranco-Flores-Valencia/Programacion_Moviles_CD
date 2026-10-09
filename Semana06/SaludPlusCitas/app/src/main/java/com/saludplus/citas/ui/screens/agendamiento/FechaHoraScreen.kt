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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.EncabezadoMedico
import com.saludplus.citas.ui.components.FechaUtils
import com.saludplus.citas.ui.components.HeroHeader
import com.saludplus.citas.ui.components.StepperAgendamiento
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.theme.Borde
import com.saludplus.citas.ui.theme.Fondo
import com.saludplus.citas.ui.theme.Primario
import com.saludplus.citas.ui.theme.PrimarioClaro
import com.saludplus.citas.ui.theme.SaludPlusColors
import com.saludplus.citas.ui.theme.SemaforoColor
import com.saludplus.citas.ui.theme.SobrePrimario
import com.saludplus.citas.ui.theme.Superficie
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun FechaHoraScreen(navController: NavHostController, medicoId: Int, sedeId: Int) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val hoy = remember { LocalDate.now() }

    var semana by rememberSaveable { mutableIntStateOf(0) }
    var fechaSeleccionada by rememberSaveable { mutableStateOf<String?>(null) }
    var horaSeleccionada by rememberSaveable { mutableStateOf<String?>(null) }

    val dias = remember(semana) { FechaUtils.diasHabiles(hoy.plusWeeks(semana.toLong())) }

    fun cambiarSemana(nueva: Int) {
        semana = nueva
        fechaSeleccionada = null
        horaSeleccionada = null
    }

    Scaffold(
        containerColor = Fondo,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Superficie)
                    .padding(20.dp)
            ) {
                if (fechaSeleccionada != null && horaSeleccionada != null) {
                    Text(
                        text = "Seleccionado: ${FechaUtils.fechaLarga(fechaSeleccionada!!)} a las $horaSeleccionada",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primario,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                }
                BotonPrincipal(
                    texto = "Continuar",
                    habilitado = fechaSeleccionada != null && horaSeleccionada != null,
                    onClick = {
                        val fecha = fechaSeleccionada
                        val hora = horaSeleccionada
                        if (fecha != null && hora != null) {
                            navController.navigate(Rutas.confirmarCita(medicoId, sedeId, fecha, hora))
                        }
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            HeroHeader(
                titulo = "Fecha y Hora",
                subtitulo = "Elige cuándo deseas tu consulta",
                onAtras = { navController.popBackStack() }
            )

            StepperAgendamiento(pasoActual = 4)

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                if (medico != null) {
                    EncabezadoMedico(medico)
                    Spacer(Modifier.height(16.dp))
                }

                // ----- Calendario Semáforo con Indicadores -----
                TarjetaBase(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
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
                                color = TextoPrincipal,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { cambiarSemana(semana + 1) }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Semana siguiente")
                            }
                        }
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            dias.forEach { dia ->
                                val iso = dia.toString()
                                val esSeleccionado = iso == fechaSeleccionada
                                val noAtiende = medico != null && (dia.dayOfWeek !in medico.diasAtencion || dia.isBefore(hoy))

                                val semaforo = if (noAtiende) {
                                    SaludPlusColors.semaforoInactivo
                                } else {
                                    val libres = Repositorio.horariosDisponibles(medicoId, iso)
                                    val total = Repositorio.horariosBase.size
                                    when {
                                        libres.isEmpty() -> SaludPlusColors.semaforoLleno
                                        libres.size <= total / 2 -> SaludPlusColors.semaforoPoco
                                        else -> SaludPlusColors.semaforoLibre
                                    }
                                }

                                ChipDiaSemaforo(
                                    nombreCorto = FechaUtils.nombreDiaCorto(dia),
                                    numero = dia.dayOfMonth.toString(),
                                    seleccionado = esSeleccionado,
                                    deshabilitado = noAtiende,
                                    semaforo = semaforo,
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        if (!noAtiende) {
                                            if (iso != fechaSeleccionada) {
                                                fechaSeleccionada = iso
                                                horaSeleccionada = null
                                            }
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LeyendaItem(SaludPlusColors.semaforoLibre.indicador, "Libre >50%")
                            LeyendaItem(SaludPlusColors.semaforoPoco.indicador, "Poco 1-50%")
                            LeyendaItem(SaludPlusColors.semaforoLleno.indicador, "Lleno")
                            LeyendaItem(SaludPlusColors.semaforoInactivo.indicador, "No atiende")
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Text("Horarios disponibles", style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                Spacer(Modifier.height(10.dp))

                when {
                    fechaSeleccionada == null -> Text(
                        "Elige un día del calendario para ver los turnos disponibles.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                    else -> {
                        val fecha = fechaSeleccionada!!
                        val ocupados = Repositorio.citas.filter { it.medicoId == medicoId && it.fecha == fecha }.map { it.hora }
                        val esHoy = fecha == hoy.toString()
                        val horaActual = LocalTime.now()

                        val turnos = Repositorio.horariosBase
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                            items(turnos, key = { it }) { hora ->
                                val estaOcupado = hora in ocupados
                                val yaPasado = esHoy && try {
                                    LocalTime.parse(hora).isBefore(horaActual)
                                } catch (e: Exception) {
                                    false
                                }
                                val deshabilitado = estaOcupado || yaPasado

                                ChipHoraEstado(
                                    hora = hora,
                                    seleccionado = hora == horaSeleccionada,
                                    deshabilitado = deshabilitado,
                                    etiquetaEstado = if (estaOcupado) "Reservado" else if (yaPasado) "Pasado" else null,
                                    onClick = {
                                        if (!deshabilitado) {
                                            horaSeleccionada = hora
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ChipDiaSemaforo(
    nombreCorto: String,
    numero: String,
    seleccionado: Boolean,
    deshabilitado: Boolean,
    semaforo: SemaforoColor,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (seleccionado) Primario else semaforo.fondo)
            .border(
                1.5.dp,
                if (seleccionado) Primario.copy(alpha = 0.8f) else Borde,
                RoundedCornerShape(12.dp)
            )
            .clickable(enabled = !deshabilitado, onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            nombreCorto,
            style = MaterialTheme.typography.bodyMedium,
            color = if (seleccionado) SobrePrimario else semaforo.texto
        )
        Text(
            numero,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) SobrePrimario else TextoPrincipal
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (seleccionado) SobrePrimario else semaforo.indicador)
        )
    }
}

@Composable
private fun LeyendaItem(color: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(texto, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario, fontSize = 11.sp)
    }
}

@Composable
private fun ChipHoraEstado(
    hora: String,
    seleccionado: Boolean,
    deshabilitado: Boolean,
    etiquetaEstado: String?,
    onClick: () -> Unit
) {
    val forma = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(forma)
            .background(
                if (seleccionado) Primario
                else if (deshabilitado) Fondo
                else Superficie
            )
            .border(
                1.dp,
                if (seleccionado) Primario
                else if (deshabilitado) Borde.copy(alpha = 0.5f)
                else Borde,
                forma
            )
            .clickable(enabled = !deshabilitado, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                hora,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (seleccionado) SobrePrimario
                else if (deshabilitado) TextoSecundario.copy(alpha = 0.4f)
                else TextoPrincipal
            )
            if (etiquetaEstado != null) {
                Text(
                    etiquetaEstado,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 10.sp,
                    color = if (deshabilitado) TextoSecundario.copy(alpha = 0.5f) else TextoSecundario
                )
            }
        }
    }
}
