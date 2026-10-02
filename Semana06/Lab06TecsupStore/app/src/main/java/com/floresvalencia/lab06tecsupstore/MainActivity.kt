package com.floresvalencia.lab06tecsupstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.floresvalencia.lab06tecsupstore.ui.theme.Lab06TecsupStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab06TecsupStoreTheme {
                AppNavegacion()
            }
        }
    }
}