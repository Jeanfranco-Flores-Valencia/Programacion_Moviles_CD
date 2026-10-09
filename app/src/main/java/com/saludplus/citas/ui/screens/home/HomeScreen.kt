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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.components.TarjetaEspecialidadDestacada
import com.saludplus.citas.ui.components.primerNombre
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.Morado
import com.saludplus.citas.ui.theme.MoradoClaro
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeExito

@Composable
fun HomeScreen(navController: NavHostController) {
    val usuario = Repositorio.usuarioActual
    val nombre = primerNombre(usuario?.nombre ?: "Paciente")
    val totalCitas = Repositorio.citasDelUsuario().size
    val destacadas = Repositorio.especialidadesDestacadas()
    var menuAbierto by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Blanco,
        bottomBar = { BarraNavegacionInferior(navController, Rutas.HOME) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // ----- Barra superior: menú y campana -----
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú", tint = TextoPrincipal)
                    }
                    DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                        DropdownMenuItem(
                            text = { Text("Mi perfil") },
                            leadingIcon = { Icon(Icons.Filled.Person, null) },
                            onClick = { menuAbierto = false; navController.navigate(Rutas.PERFIL) }
                        )
                        DropdownMenuItem(
                            text = { Text("Mis citas") },
                            leadingIcon = { Icon(Icons.Filled.EventAvailable, null) },
                            onClick = { menuAbierto = false; navController.navigate(Rutas.MIS_CITAS) }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Cerrar sesión", color = Rojo) },
                            leadingIcon = { Icon(Icons.Filled.PowerSettingsNew, null, tint = Rojo) },
                            onClick = {
                                menuAbierto = false
                                Repositorio.cerrarSesion()
                                navController.navigate(Rutas.SPLASH) {
                                    popUpTo(navController.graph.id) { inclusive = true }
                                }
                            }
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { navController.navigate(Rutas.NOTIFICACIONES) }) {
                    BadgedBox(
                        badge = {
                            if (totalCitas > 0) Badge { Text("$totalCitas") }
                        }
                    ) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones", tint = TextoPrincipal)
                    }
                }
            }

            // ----- Saludo -----
            Text("¡Hola, $nombre!", style = MaterialTheme.typography.headlineMedium, color = TextoPrincipal)
            Text("¿Qué deseas hacer hoy?", style = MaterialTheme.typography.bodyLarge, color = TextoSecundario)
            Spacer(Modifier.height(20.dp))

            // ----- Accesos rápidos (2 x 2) -----
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaAcceso(
                    titulo = "Agendar cita",
                    icono = Icons.Filled.CalendarMonth,
                    fondo = AzulClaro,
                    color = AzulPrimario,
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Rutas.ESPECIALIDADES) }
                )
                TarjetaAcceso(
                    titulo = "Mis citas",
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
                    titulo = "Mis datos",
                    icono = Icons.Filled.Person,
                    fondo = MoradoClaro,
                    color = Morado,
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Rutas.PERFIL) }
                )
                TarjetaAcceso(
                    titulo = "Resultados",
                    icono = Icons.Filled.Description,
                    fondo = NaranjaClaro,
                    color = Naranja,
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Rutas.RESULTADOS) }
                )
            }

            // ----- Especialidades destacadas (LazyRow) -----
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
                TextButton(onClick = { navController.navigate(Rutas.ESPECIALIDADES) }) {
                    Text("Ver todas", color = AzulPrimario, fontWeight = FontWeight.SemiBold)
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(destacadas, key = { it.id }) { especialidad ->
                    TarjetaEspecialidadDestacada(especialidad) {
                        navController.navigate(Rutas.medicos(especialidad.id))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TarjetaAcceso(
    titulo: String,
    icono: ImageVector,
    fondo: Color,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(118.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(fondo)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(10.dp))
        Text(titulo, style = MaterialTheme.typography.titleMedium, color = color)
    }
}
