package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.saludplus.citas.R
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.AzulMuyClaro
import com.saludplus.citas.ui.theme.AzulOscuro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoSecundario

@Composable
fun SplashScreen(navController: NavHostController) {
    Scaffold(containerColor = Blanco) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Blanco, AzulMuyClaro)))
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.ic_logo_saludplus),
                contentDescription = "Logo Clínica SaludPlus",
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text("Clínica", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
            Text("SaludPlus", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = AzulOscuro)
            Spacer(Modifier.height(4.dp))
            Text(
                "Tu salud, nuestra prioridad",
                style = MaterialTheme.typography.bodyLarge,
                color = TextoSecundario
            )

            // La ilustración del médico ocupa el espacio central
            Image(
                painter = painterResource(R.drawable.ilustracion_doctor),
                contentDescription = "Médico de SaludPlus",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            BotonPrincipal(
                texto = "Comenzar",
                onClick = { navController.navigate(Rutas.REGISTRO) }
            )
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = { navController.navigate(Rutas.LOGIN) }) {
                Text("Ya tengo una cuenta", color = AzulPrimario, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
