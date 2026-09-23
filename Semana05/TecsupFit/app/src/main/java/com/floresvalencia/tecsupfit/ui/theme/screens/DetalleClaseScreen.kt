package com.floresvalencia.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.tecsupfit.data.ClaseGym
import com.floresvalencia.tecsupfit.ui.components.IconoClase

@Composable
fun DetalleClaseScreen(clase: ClaseGym, onReservarClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Banner grande con la pesa
        IconoClase(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            tamanoIcono = 64.dp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Datos de la clase elegida (llegó por parámetro de navegación)
        Text(clase.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(
            text = "${clase.horarioTexto} · ${clase.duracionMin} min",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(clase.descripcion, style = MaterialTheme.typography.bodyMedium)

        // Empuja el botón hasta abajo
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onReservarClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Reservar cupo", fontWeight = FontWeight.SemiBold)
        }
    }
}