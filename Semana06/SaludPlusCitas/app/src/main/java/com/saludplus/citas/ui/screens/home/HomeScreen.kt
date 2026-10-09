package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.components.Calificacion
import com.saludplus.citas.ui.components.HeroHeader
import com.saludplus.citas.ui.components.ImagenDoctor
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.components.TarjetaEspecialidadDestacada
import com.saludplus.citas.ui.components.primerNombre
import com.saludplus.citas.ui.components.rangoHora
import com.saludplus.citas.ui.components.textoFecha
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.Morado
import com.saludplus.citas.ui.theme.MoradoClaro
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeExito
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope) {
    val usuario = Repositorio.usuarioActual
    val nombre = primerNombre(usuario?.nombre ?: "Paciente")
    val citas = Repositorio.citasDelUsuario()
    val proximaCita = citas.firstOrNull()
    val destacadas = Repositorio.especialidadesDestacadas()
    val mejoresMedicos = Repositorio.medicos.sortedByDescending { it.calificacion }.take(5)

    Scaffold(
        containerColor = FondoApp,
        bottomBar = { BarraNavegacionInferior(navController, Rutas.HOME) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ----- Hero Header -----
            HeroHeader(
                titulo = "¡Hola, $nombre! ",
                subtitulo = "Tu salud y bienestar en las mejores manos",
                onAtras = null,
                acciones = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = Blanco)
                    }
                    IconButton(onClick = { navController.navigate(Rutas.NOTIFICACIONES) }) {
                        BadgedBox(
                            badge = {
                                if (citas.isNotEmpty()) Badge { Text("${citas.size}") }
                            }
                        ) {
                            Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = Blanco)
                        }
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // ----- Próxima Cita Banner / Tarjeta -----
                if (proximaCita != null) {
                    val medico = Repositorio.obtenerMedico(proximaCita.medicoId)
                    val especialidad = Repositorio.obtenerEspecialidad(proximaCita.especialidadId)
                    val sede = Repositorio.obtenerSede(proximaCita.sedeId)

                    Text("Próxima cita médica", style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                    Spacer(Modifier.height(8.dp))
                    TarjetaBase(
                        modifier = Modifier.fillMaxWidth(),
                        colorFondo = Blanco,
                        onClick = { navController.navigate(Rutas.detalleCita(proximaCita.id)) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (medico != null) {
                                    ImagenDoctor(
                                        fotoUrl = medico.fotoUrl,
                                        esMujer = medico.esMujer,
                                        modifier = Modifier.size(54.dp).clip(CircleShape)
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(medico?.nombre ?: "Médico", style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                                    Text(especialidad?.nombre ?: "", style = MaterialTheme.typography.bodyMedium, color = AzulPrimario)
                                    Text(sede?.nombre ?: "Sede Principal", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(AzulClaro)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.CalendarMonth, null, tint = AzulPrimario, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(textoFecha(proximaCita.fecha), style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal, modifier = Modifier.weight(1f))
                                Icon(Icons.Filled.Schedule, null, tint = AzulPrimario, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(proximaCita.hora, style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }

                // ----- Accesos rápidos (Grid 2x2) -----
                Text("¿Qué deseas hacer hoy?", style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAcceso(
                        titulo = "Agendar cita",
                        subtitulo = "Elige tu sede",
                        icono = Icons.Filled.Business,
                        fondo = AzulClaro,
                        color = AzulPrimario,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Rutas.SEDES) }
                    )
                    TarjetaAcceso(
                        titulo = "Mis citas",
                        subtitulo = "${citas.size} programadas",
                        icono = Icons.Filled.EventAvailable,
                        fondo = VerdeClaro,
                        color = VerdeExito,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Rutas.MIS_CITAS) }
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAcceso(
                        titulo = "Mi Perfil",
                        subtitulo = "Datos personales",
                        icono = Icons.Filled.Person,
                        fondo = MoradoClaro,
                        color = Morado,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Rutas.PERFIL) }
                    )
                    TarjetaAcceso(
                        titulo = "Resultados",
                        subtitulo = "Exámenes médicos",
                        icono = Icons.Filled.Description,
                        fondo = NaranjaClaro,
                        color = Naranja,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Rutas.RESULTADOS) }
                    )
                }

                // ----- Especialidades destacadas -----
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Especialidades destacadas",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextoPrincipal,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { navController.navigate(Rutas.SEDES) }) {
                        Text("Ver sedes", color = AzulPrimario, fontWeight = FontWeight.SemiBold)
                    }
                }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(destacadas, key = { it.id }) { especialidad ->
                        TarjetaEspecialidadDestacada(especialidad) {
                            navController.navigate(Rutas.SEDES)
                        }
                    }
                }

                // ----- Doctores Mejor Calificados -----
                Spacer(Modifier.height(24.dp))
                Text(
                    "Doctores destacados",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrincipal
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(mejoresMedicos, key = { it.id }) { medico ->
                        TarjetaDoctorCarrusel(medico) {
                            navController.navigate(Rutas.detalleMedico(medico.id))
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TarjetaAcceso(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    fondo: Color,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(112.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(fondo)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
        }
        Column {
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = TextoPrincipal, fontWeight = FontWeight.Bold)
            Text(subtitulo, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario, fontSize = 12.sp)
        }
    }
}

@Composable
private fun TarjetaDoctorCarrusel(medico: Medico, onClick: () -> Unit) {
    TarjetaBase(
        modifier = Modifier.width(140.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagenDoctor(
                fotoUrl = medico.fotoUrl,
                esMujer = medico.esMujer,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                medico.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = TextoPrincipal
            )
            Text(
                medico.titulo,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = TextoSecundario
            )
            Spacer(Modifier.height(4.dp))
            Calificacion(medico)
        }
    }
}
