package com.floresvalencia.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.floresvalencia.navlab.data.SessionManager
import com.floresvalencia.navlab.data.Student
import com.floresvalencia.navlab.data.StudentRepository
import com.floresvalencia.navlab.navigation.Screen
import com.floresvalencia.navlab.navigation.logout
import com.floresvalencia.navlab.ui.components.MenuCard
import com.floresvalencia.navlab.ui.theme.LogoutRed
import com.floresvalencia.navlab.ui.theme.NavLabTheme
import com.floresvalencia.navlab.ui.theme.PurplePrimary

@Composable
fun HomeScreen(navController: NavController) {
    val currentUser = SessionManager.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (currentUser != null) {
        HomeScreenContent(
            student = currentUser,
            onNavigateToList = { navController.navigate(Screen.List.route) },
            onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
            onLogout = { navController.logout() }
        )
    }
}

@Composable
fun HomeScreenContent(
    student: Student,
    onNavigateToList: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PurplePrimary, Color.White)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Encabezado con mensaje de bienvenida
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 48.dp)
            ) {
                Text(
                    text = "Bienvenido,\n${student.name}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "¿Qué deseas gestionar hoy?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }

            // Menú de opciones centrales
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                MenuCard(
                    title = "Directorio de Alumnos",
                    subtitle = "Ver y gestionar estudiantes",
                    icon = Icons.Default.Groups,
                    onClick = onNavigateToList
                )

                MenuCard(
                    title = "Mi Perfil Académico",
                    subtitle = "Datos personales y progreso",
                    icon = Icons.Default.Person,
                    onClick = onNavigateToProfile
                )
            }

            // Botón inferior para cerrar sesión
            TextButton(
                onClick = onLogout,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = LogoutRed
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = "Cerrar Sesión Segura",
                    color = LogoutRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    NavLabTheme(dynamicColor = false) {
        HomeScreenContent(
            student = StudentRepository.getById(1)!!,
            onNavigateToList = {},
            onNavigateToProfile = {},
            onLogout = {}
        )
    }
}
