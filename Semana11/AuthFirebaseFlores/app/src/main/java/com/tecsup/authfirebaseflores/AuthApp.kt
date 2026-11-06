package com.tecsup.authfirebaseflores

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

object Destinations {
    // Mejora IA: el registro es una pestana dentro de la pantalla de login (AuthScreen)
    const val LOGIN = "login"
    const val HOME = "home"
}

@Composable
fun AuthApp() {
    val navController = rememberNavController()
    AuthNavGraph(navController)
}

@Composable
fun AuthNavGraph(navController: NavHostController) {
    // Si Firebase ya tiene una sesion guardada, se entra directo a la bienvenida
    val inicio = if (FirebaseAuth.getInstance().currentUser != null) Destinations.HOME else Destinations.LOGIN

    NavHost(
        navController = navController,
        startDestination = inicio,
        // Mejora IA: transicion suave entre pantallas (aparece con un leve zoom, sale desvaneciendose)
        enterTransition = { fadeIn(tween(450)) + scaleIn(tween(450), initialScale = 0.96f) },
        exitTransition = { fadeOut(tween(250)) + scaleOut(tween(250), targetScale = 1.02f) },
        popEnterTransition = { fadeIn(tween(450)) + scaleIn(tween(450), initialScale = 0.96f) },
        popExitTransition = { fadeOut(tween(250)) + scaleOut(tween(250), targetScale = 1.02f) }
    ) {
        composable(Destinations.LOGIN) {
            AuthScreen(
                onAutenticado = {
                    navController.navigate(Destinations.HOME) {
                        popUpTo(Destinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(Destinations.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}
