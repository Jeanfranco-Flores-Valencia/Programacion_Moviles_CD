package com.floresvalencia.clinicasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.floresvalencia.clinicasalud.ui.screens.InicioScreen
import com.floresvalencia.clinicasalud.ui.theme.ClinicaSaludTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClinicaSaludTheme {
                Scaffold { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        InicioScreen(onMedicoClick = { })
                    }
                }
            }
        }
    }
}