package com.floresvalencia.navlab.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.floresvalencia.navlab.screens.DetailScreen
import com.floresvalencia.navlab.screens.HomeScreen
import com.floresvalencia.navlab.screens.ListScreen
import com.floresvalencia.navlab.screens.LoginScreen
import com.floresvalencia.navlab.screens.ProfileScreen
import com.floresvalencia.navlab.ui.theme.NavLabTheme

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavLabTheme(dynamicColor = false) {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.List.route) {
                ListScreen(navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("studentId") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->
                val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
                DetailScreen(navController, studentId)
            }
        }
    }
}
