package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.saludplus.citas.data.model.Resultado
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.IconoEnCaja
import com.saludplus.citas.ui.components.TarjetaBase
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeExito

// Lista fija (reto extra: modelo propio)
private val resultados = listOf(
    Resultado(1, "Hemograma completo", "Laboratorio", "02/10/2026", "Dr. Carlos Mendoza", listo = true),
    Resultado(2, "Perfil lipídico", "Laboratorio", "02/10/2026", "Dr. Carlos Mendoza", listo = true),
    Resultado(3, "Electrocardiograma", "Cardiología", "28/09/2026", "Dr. Ricardo Vargas", listo = true),
    Resultado(4, "Ecografía pélvica", "Imágenes", "07/10/2026", "Dra. Ana Torres", listo = false),
    Resultado(5, "Glucosa en ayunas", "Laboratorio", "07/10/2026", "Dra. Lucía Fernández", listo = false)
)

@Composable
fun ResultadosScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Blanco,
        topBar = {
            BarraSuperior("Resultados", onAtras = { navController.popBackStack(Rutas.HOME, inclusive = false) })
        },
        bottomBar = { BarraNavegacionInferior(navController, Rutas.RESULTADOS) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(resultados, key = { it.id }) { resultado ->
                TarjetaResultado(resultado)
            }
        }
    }
}

@Composable
private fun TarjetaResultado(resultado: Resultado) {
    val icono = when (resultado.tipo) {
        "Laboratorio" -> Icons.Filled.Biotech
        "Cardiología" -> Icons.Filled.MonitorHeart
        else -> Icons.Filled.Description
    }
    TarjetaBase(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoEnCaja(icono = icono, colorFondo = NaranjaClaro, colorIcono = Naranja)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(resultado.titulo, style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                Text(
                    "${resultado.tipo} · ${resultado.fecha}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
                Text(resultado.medico, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            }
            Text(
                text = if (resultado.listo) "Disponible" else "En proceso",
                color = if (resultado.listo) VerdeExito else Naranja,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (resultado.listo) VerdeClaro else NaranjaClaro)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}
