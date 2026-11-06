package com.tecsup.authfirebaseflores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

/**
 * Punto de entrada de la app. Firebase se inicializa solo con el plugin
 * google-services (lee app/google-services.json), por eso no hace falta initializeApp.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Evita que el contenido quede debajo de la barra de estado (Android 15)
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        AuthApp() // Llamamos a nuestra app con navegacion
                    }
                }
            }
        }
    }
}
