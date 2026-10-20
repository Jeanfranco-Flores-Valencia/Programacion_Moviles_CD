package com.floresvalencia.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.ui.components.EstiloBoton
import com.floresvalencia.lavacampus.ui.components.IconoServicio
import com.floresvalencia.lavacampus.ui.components.LavaButton

/**
 * Pantalla de bienvenida. No avanza sola: espera a que el usuario elija
 * "Comenzar" (crear cuenta) o "Ya tengo una cuenta" (iniciar sesion).
 */
@Composable
fun SplashScreen(
    onComenzar: () -> Unit,
    onTengoCuenta: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Spacer(Modifier.weight(1f))
        IconoServicio(
            icono = "wash",
            tamano = 96.dp,
            color = MaterialTheme.colorScheme.onPrimary,
            fondo = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(20.dp))
        Text("LavaCampus", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Text(
            "Tu ropa limpia sin salir del campus",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
            listOf("wash" to "Lavado", "iron" to "Planchado", "shirt" to "En seco").forEach { (icono, texto) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconoServicio(icono = icono, tamano = 52.dp)
                    Spacer(Modifier.height(6.dp))
                    Text(texto, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        LavaButton(texto = "Comenzar", onClick = onComenzar)
        Spacer(Modifier.height(10.dp))
        LavaButton(texto = "Ya tengo una cuenta", onClick = onTengoCuenta, estilo = EstiloBoton.CONTORNO)
        Spacer(Modifier.height(12.dp))
        Text(
            "Tecsup · Lavandería universitaria",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))
    }
}
