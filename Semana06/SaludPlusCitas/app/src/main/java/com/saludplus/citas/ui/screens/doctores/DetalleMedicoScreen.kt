package com.saludplus.citas.ui.screens.doctores

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.ImagenDoctor
import com.saludplus.citas.ui.theme.Acento
import com.saludplus.citas.ui.theme.Borde
import com.saludplus.citas.ui.theme.Fondo
import com.saludplus.citas.ui.theme.Primario
import com.saludplus.citas.ui.theme.PrimarioOscuro
import com.saludplus.citas.ui.theme.SobrePrimario
import com.saludplus.citas.ui.theme.Superficie
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun DetalleMedicoScreen(navController: NavHostController, medicoId: Int) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val sede = medico?.let { Repositorio.obtenerSede(it.sedeId) }
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    Scaffold(
        containerColor = Fondo,
        topBar = { BarraSuperior("Perfil del Médico", onAtras = { navController.popBackStack() }) }
    ) { padding ->
        if (medico == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Médico no encontrado", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // ----- Encabezado con Foto Real (Coil) y Gradiente -----
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    ImagenDoctor(
                        fotoUrl = medico.fotoUrl,
                        esMujer = medico.esMujer,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, PrimarioOscuro.copy(alpha = 0.85f)),
                                    startY = 100f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = medico.nombre,
                            style = MaterialTheme.typography.headlineMedium,
                            color = SobrePrimario
                        )
                        Text(
                            text = "${medico.titulo} · ${especialidad?.nombre.orEmpty()}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = SobrePrimario.copy(alpha = 0.9f)
                        )
                        Text(
                            text = medico.cmp,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SobrePrimario.copy(alpha = 0.75f)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Acento, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${medico.calificacion} (${medico.numResenas} reseñas)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoPrincipal,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Superficie),
                        border = BorderStroke(1.dp, Borde)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Información de Atención",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextoPrincipal
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Business, contentDescription = null, tint = Primario)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = sede?.nombre ?: "Sede Principal", style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal)
                                    Text(text = medico.direccion, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Phone, contentDescription = null, tint = Primario)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = medico.telefono, style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schedule, contentDescription = null, tint = Primario)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Horario: ${medico.rangoHoras}", style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal)
                                    val diasStr = medico.diasAtencion.joinToString(", ") { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
                                    Text(text = "Días: $diasStr", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    BotonPrincipal(
                        texto = "Agendar cita con este médico",
                        onClick = {
                            navController.navigate(Rutas.fechaHora(medico.id, medico.sedeId))
                        }
                    )
                }
            }
        }
    }
}
