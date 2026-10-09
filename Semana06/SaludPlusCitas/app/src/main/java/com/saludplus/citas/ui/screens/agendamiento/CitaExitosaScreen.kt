package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.BotonSecundario
import com.saludplus.citas.ui.components.ConfetiAnimado
import com.saludplus.citas.ui.components.FechaUtils
import com.saludplus.citas.ui.components.HeroHeader
import com.saludplus.citas.ui.components.ImagenDoctor
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.components.rangoHora
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.VerdeExito
import java.util.Locale

@Composable
fun CitaExitosaScreen(navController: NavHostController, citaId: Int) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val especialidad = cita?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }
    val sede = cita?.let { Repositorio.obtenerSede(it.sedeId) }
    val usuario = Repositorio.usuarioActual

    val volverAlInicio = { navController.popBackStack(Rutas.HOME, inclusive = false) }

    val scaleAnim by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 600),
        label = "scale"
    )

    val codigoCita = String.format(Locale.getDefault(), "SP-%06d", citaId)

    Box(modifier = Modifier.fillMaxSize()) {
        ConfetiAnimado()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                HeroHeader(
                    titulo = "¡Cita Confirmada!",
                    subtitulo = "Tu ticket digital ha sido generado",
                    onAtras = { volverAlInicio() }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = VerdeExito,
                    modifier = Modifier
                        .size(64.dp)
                        .scale(scaleAnim)
                )
                Spacer(Modifier.height(16.dp))

                // ----- Tarjeta Tipo Ticket -----
                TarjetaBase(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, AzulClaro, RoundedCornerShape(20.dp)),
                    colorFondo = Blanco
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TICKET MÉDICO",
                                style = MaterialTheme.typography.titleMedium,
                                color = AzulPrimario
                            )
                            Text(
                                text = codigoCita,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BordeSuave)

                        // Paciente
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Person, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Paciente: ${usuario?.nombre ?: "Invitado"}", style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Médico
                        if (medico != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ImagenDoctor(
                                    fotoUrl = medico.fotoUrl,
                                    esMujer = medico.esMujer,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = medico.nombre, style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                                    Text(text = "${medico.titulo} · ${especialidad?.nombre.orEmpty()}", style = MaterialTheme.typography.bodyMedium, color = AzulPrimario)
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = BordeSuave)

                        // Detalles de Fecha, Sede, Hora
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Business, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = sede?.nombre ?: "Sede Principal", style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = sede?.direccion ?: medico?.direccion.orEmpty(), style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (cita != null) FechaUtils.fechaLarga(cita.fecha) else "", style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (cita != null) rangoHora(cita.hora) else "", style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Recomendaciones
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AzulClaro)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = AzulPrimario)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Recomendaciones:\n• Llega 15 minutos antes de tu cita.\n• Trae tu DNI físico para el ingreso.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoPrincipal
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))
                BotonPrincipal(
                    texto = "Ver mis citas",
                    onClick = {
                        navController.navigate(Rutas.MIS_CITAS) {
                            popUpTo(Rutas.HOME)
                        }
                    }
                )
                Spacer(Modifier.height(10.dp))
                BotonSecundario(texto = "Volver al inicio", onClick = { volverAlInicio() })
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
