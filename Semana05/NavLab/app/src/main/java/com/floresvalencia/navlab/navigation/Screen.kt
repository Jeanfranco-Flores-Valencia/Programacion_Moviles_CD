package com.floresvalencia.navlab.navigation
//Clase sellada que actua como contrato central de navegacion.
//Recibe "Route" como parametro - es el identificador único de cada pantalla.
// Al ser sealed, el compilador conoce todas las rutas posibles en tiempo de compilación.
sealed class Screen(val route: String){
    //Desarrollado por : Jeanfranco Flores
    //Pantalla de inicio - punto de entrada de la app
    object Home : Screen("home")
    //Pantalla que muestra la lista de elementos
    object List: Screen("list")
    //Pantalla que muestra el perfil de usuario
    object Profile : Screen("profile")
    //-----------------------------------------------------
    // RUTA CON ARGUMENTO
    // {itemId} es el placeholder que Navigation reemplaza
    // con el valor real al momento de navegar
    //-----------------------------------------------------
    object Detail: Screen("detail/{itemId}"){

        // Construye la ruta final sustituyendo el placeholder por el valor real.
        // Ejemplo: createRoute(5) -> devuelve "detail/5"
        // Este String es el que se pasa a navController.navigate(...)
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}