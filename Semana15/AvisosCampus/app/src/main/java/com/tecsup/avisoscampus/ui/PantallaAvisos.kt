package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.PermisoUi
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

/** Pantalla unica de la app: una tarjeta por funcion. Solo dibuja el estado y avisa los eventos. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current

    // Ya viene hecho: el ViewModel emite el aviso y la pantalla (que tiene Context) lo muestra
    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: si no se mostro, avisar al ViewModel
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Tarjeta("Permiso de notificaciones") {
                // TODO Paso 2: estado del permiso y boton "Permitir notificaciones"
                FilaEstado(PermisoUi.SinPedir)
            }
            Tarjeta("Avisos") {
                // TODO Paso 3: boton "Mostrar aviso" -> vm.avisarAhora()
                Button(onClick = {}) { Text("Mostrar aviso") }
            }
            Tarjeta("Recordatorio") {
                // TODO Paso 5: "Recordar en 10 s", "Cancelar" y "Faltan N s"
                Text("Próximamente", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Tarjeta("Receptor de anuncios") {
                // TODO Paso 6: Switch "Escuchar el modo avión"
                Text("Próximamente", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            // TODO Paso 7: HistorialAvisos(avisos)
        }
    }
}

/** Tarjeta con el titulo en azul. La usan todas las funciones de la pantalla. */
@Composable
fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(
                titulo,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            contenido()
        }
    }
}

/** "Estado:" con una etiqueta de color: Sin pedir (gris), Permitido (verde) o Denegado (rojo). */
@Composable
fun FilaEstado(permiso: PermisoUi) {
    val (texto, fondo, color) = when (permiso) {
        PermisoUi.SinPedir -> Triple("Sin pedir", Color(0xFFE0E0E0), Color(0xFF424242))
        PermisoUi.Concedido -> Triple("Permitido", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        PermisoUi.Denegado -> Triple("Denegado", Color(0xFFFFE3E0), Color(0xFFC62828))
    }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Estado:")
        Text(
            texto,
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.clip(RoundedCornerShape(50)).background(fondo).padding(horizontal = 10.dp, vertical = 2.dp)
        )
    }
}
