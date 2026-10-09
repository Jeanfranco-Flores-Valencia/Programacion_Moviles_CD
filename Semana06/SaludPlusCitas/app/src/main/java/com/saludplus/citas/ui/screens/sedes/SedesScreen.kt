package com.saludplus.citas.ui.screens.sedes

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.model.Sede
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.components.HeroHeader
import com.saludplus.citas.ui.components.MensajeVacio
import com.saludplus.citas.ui.components.StepperAgendamiento
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun SedesScreen(navController: NavHostController) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    val sedes = Repositorio.buscarSedes(busqueda)
    val rutaActual = Rutas.SEDES

    Scaffold(
        containerColor = FondoApp,
        bottomBar = { BarraNavegacionInferior(navController, rutaActual) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HeroHeader(
                titulo = "Nuestras Sedes",
                subtitulo = "Selecciona una sede para iniciar tu agendamiento"
            )

            StepperAgendamiento(pasoActual = 1)

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                placeholder = { Text("Buscar por distrito o nombre de sede...", color = TextoSecundario) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextoSecundario) },
                trailingIcon = if (busqueda.isNotEmpty()) {
                    {
                        IconButton(onClick = { busqueda = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Limpiar", tint = TextoSecundario)
                        }
                    }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = BordeSuave,
                    focusedContainerColor = Blanco,
                    unfocusedContainerColor = Blanco
                )
            )

            if (sedes.isEmpty()) {
                MensajeVacio(
                    icono = Icons.Filled.SearchOff,
                    titulo = "Sin sedes encontradas",
                    mensaje = "No hay sedes que coincidan con \"$busqueda\""
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sedes, key = { it.id }) { sede ->
                        TarjetaSede(sede) {
                            navController.navigate(Rutas.especialidades(sede.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaSede(sede: Sede, onClick: () -> Unit) {
    TarjetaBase(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Business,
                        contentDescription = null,
                        tint = AzulPrimario
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sede.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextoPrincipal
                    )
                    Text(
                        text = "Distrito: ${sede.distrito}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AzulPrimario
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = sede.direccion, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Phone, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = sede.telefono, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Schedule, contentDescription = null, tint = TextoSecundario, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = sede.horarioAtencion, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AzulClaro)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${sede.especialidadesIds.size} especialidades disponibles – Toca para agendar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AzulPrimario
                )
            }
        }
    }
}
