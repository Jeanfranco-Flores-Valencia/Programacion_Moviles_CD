package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.MensajeVacio
import com.saludplus.citas.ui.components.TarjetaCita
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun MisCitasScreen(navController: NavHostController) {
    // citasDelUsuario lee una lista observable: si se agenda o cancela una cita, se redibuja sola
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        containerColor = Blanco,
        topBar = {
            BarraSuperior("Mis citas", onAtras = { navController.popBackStack(Rutas.HOME, inclusive = false) })
        },
        bottomBar = { BarraNavegacionInferior(navController, Rutas.MIS_CITAS) }
    ) { padding ->
        if (citas.isEmpty()) {
            MensajeVacio(
                icono = Icons.Filled.EventBusy,
                titulo = "Aún no tienes citas",
                mensaje = "Cuando agendes una cita aparecerá aquí.",
                modifier = Modifier.padding(padding),
                accion = {
                    BotonPrincipal(
                        texto = "Agendar una cita",
                        onClick = { navController.navigate(Rutas.ESPECIALIDADES) },
                        modifier = Modifier.width(240.dp)
                    )
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "Tienes ${citas.size} cita(s) programada(s)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
                items(citas, key = { it.id }) { cita ->
                    TarjetaCita(cita) { navController.navigate(Rutas.detalleCita(cita.id)) }
                }
            }
        }
    }
}
