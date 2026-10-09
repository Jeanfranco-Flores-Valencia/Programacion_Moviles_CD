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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
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
import com.saludplus.citas.ui.components.TarjetaEspecialidadFila
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun EspecialidadesScreen(navController: NavHostController) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    // Se recalcula sola en cada recomposición: al cambiar "busqueda" la lista se filtra
    val especialidades = Repositorio.buscarEspecialidades(busqueda)

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Especialidades", onAtras = { navController.popBackStack() }) }
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
                placeholder = { Text("Buscar especialidad...", color = TextoSecundario) },
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
                    unfocusedBorderColor = FondoApp,
                    focusedContainerColor = FondoApp,
                    unfocusedContainerColor = FondoApp
                )
            )

            if (especialidades.isEmpty()) {
                MensajeVacio(
                    icono = Icons.Filled.SearchOff,
                    titulo = "Sin resultados",
                    mensaje = "No encontramos especialidades para \"$busqueda\""
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(especialidades, key = { it.id }) { especialidad ->
                        TarjetaEspecialidadFila(especialidad) {
                            navController.navigate(Rutas.medicos(especialidad.id))
                        }
                    }
                }
            }
        }
    }
}
