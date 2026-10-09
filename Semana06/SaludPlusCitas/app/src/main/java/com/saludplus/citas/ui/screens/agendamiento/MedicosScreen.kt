package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.MensajeVacio
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun MedicosScreen(navController: NavHostController, especialidadId: Int) {
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    var mostrarBusqueda by rememberSaveable { mutableStateOf(false) }
    var busqueda by rememberSaveable { mutableStateOf("") }

    // filter + sortedByDescending (mejor calificados primero)
    val medicos = if (busqueda.isBlank()) {
        Repositorio.medicosPorEspecialidad(especialidadId)
    } else {
        Repositorio.buscarMedicos(especialidadId, busqueda)
    }

    Scaffold(
        containerColor = Blanco,
        topBar = {
            BarraSuperior(
                titulo = "Médicos de ${especialidad?.nombre ?: "la especialidad"}",
                onAtras = { navController.popBackStack() },
                acciones = {
                    IconButton(onClick = {
                        mostrarBusqueda = !mostrarBusqueda
                        if (!mostrarBusqueda) busqueda = ""
                    }) {
                        Icon(
                            if (mostrarBusqueda) Icons.Filled.Close else Icons.Filled.Search,
                            contentDescription = "Buscar médico"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (mostrarBusqueda) {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    placeholder = { Text("Buscar por nombre...", color = TextoSecundario) },
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = TextoSecundario) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        unfocusedBorderColor = FondoApp,
                        focusedContainerColor = FondoApp,
                        unfocusedContainerColor = FondoApp
                    )
                )
            }

            if (medicos.isEmpty()) {
                MensajeVacio(
                    icono = Icons.Filled.PersonSearch,
                    titulo = "Sin médicos",
                    mensaje = "No hay médicos que coincidan con tu búsqueda"
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(medicos, key = { it.id }) { medico ->
                        TarjetaMedico(medico) {
                            navController.navigate(Rutas.fechaHora(medico.id))
                        }
                    }
                }
            }
        }
    }
}
