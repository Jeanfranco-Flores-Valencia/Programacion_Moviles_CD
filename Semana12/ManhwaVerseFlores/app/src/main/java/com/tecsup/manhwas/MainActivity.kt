package com.tecsup.manhwas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.tecsup.manhwas.ui.navigation.AppNav
import com.tecsup.manhwas.ui.theme.ManhwaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as ManhwaApp).container
        setContent {
            ManhwaTheme {
                // Surface da el color de fondo y el color de texto claro a toda la app
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNav(container)
                }
            }
        }
    }
}
