package com.floresvalencia.navlab.navigation

import androidx.navigation.NavController
import com.floresvalencia.navlab.data.SessionManager

// Función de extensión reutilizable para cerrar sesión de manera segura
fun NavController.logout() {
    SessionManager.logout()
    navigate(Screen.Login.route) {
        popUpTo(graph.id) { inclusive = true }
    }
}
