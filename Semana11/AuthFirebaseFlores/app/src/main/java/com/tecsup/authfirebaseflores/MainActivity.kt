package com.tecsup.authfirebaseflores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier

/**
 * Punto de entrada de la app. Firebase se inicializa solo con el plugin
 * google-services (lee app/google-services.json), por eso no hace falta initializeApp.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mejora IA: el fondo se dibuja tambien detras de las barras del sistema
        enableEdgeToEdge()
        setContent {
            TemaEducativo {
                // El fondo vive fuera de la navegacion: no parpadea al cambiar de pantalla
                FondoEducativo {
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        AuthApp() // Llamamos a nuestra app con navegacion
                    }
                }
            }
        }
    }
}
