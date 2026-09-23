package com.floresvalencia.navlab.navigation

// Clase sellada que actúa como contrato central de navegación.
// Recibe "route" como parámetro - es el identificador único de cada pantalla.
// Al ser sealed, el compilador conoce todas las rutas posibles en tiempo de compilación.
sealed class Screen(val route: String) {
    // Desarrollado por : Jeanfranco Flores

    // Pantalla de inicio de sesión - punto de entrada de la app
    object Login : Screen("login")

    // Pantalla principal del portal académico
    object Home : Screen("home")

    // Directorio de alumnos
    object List : Screen("list")

    // Configuración del perfil del alumno
    object Profile : Screen("profile")

    //-----------------------------------------------------
    // RUTA CON ARGUMENTO
    // {studentId} es el placeholder que Navigation reemplaza
    // con el valor real al momento de navegar
    //-----------------------------------------------------
    object Detail : Screen("detail/{studentId}") {

        // Construye la ruta final sustituyendo el placeholder por el valor real.
        // Ejemplo: createRoute(1) -> devuelve "detail/1"
        // Este String es el que se pasa a navController.navigate(...)
        fun createRoute(studentId: Int): String = "detail/$studentId"
    }
}
