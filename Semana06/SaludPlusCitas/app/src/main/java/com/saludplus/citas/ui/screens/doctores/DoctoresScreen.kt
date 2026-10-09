package com.saludplus.citas.ui.screens.doctores

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
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.MensajeVacio
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun DoctoresScreen(navController: NavHostController) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    val todosLosMedicos = Repositorio.medicos
    val medicos = if (busqueda.isBlank()) {
        todosLosMedicos
    } else {
        todosLosMedicos.filter { it.nombre.contains(busqueda, ignoreCase = true) || it.titulo.contains(busqueda, ignoreCase = true) }
    }

    val rutaActual = Rutas.DOCTORES_LISTA

    Scaffold(
        containerColor = FondoApp,
        topBar = { BarraSuperior("Nuestros Doctores", onAtras = { navController.popBackStack() }) },
        bottomBar = { BarraNavegacionInferior(navController, rutaActual) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                placeholder = { Text("Buscar por nombre o especialidad...", color = TextoSecundario) },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = TextoSecundario) },
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
                    unfocusedBorderColor = FondoApp,
                    focusedContainerColor = Blanco,
                    unfocusedContainerColor = Blanco
                )
            )

            if (medicos.isEmpty()) {
                MensajeVacio(
                    icono = Icons.Filled.PersonSearch,
                    titulo = "Sin médicos",
                    mensaje = "No hay médicos que coincidan con \"$busqueda\""
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(medicos, key = { it.id }) { medico ->
                        TarjetaMedico(medico) {
                            navController.navigate(Rutas.detalleMedico(medico.id))
                        }
                    }
                }
            }
        }
    }
}
