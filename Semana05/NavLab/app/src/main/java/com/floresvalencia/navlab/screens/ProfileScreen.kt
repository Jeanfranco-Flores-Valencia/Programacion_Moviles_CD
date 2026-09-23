package com.floresvalencia.navlab.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.floresvalencia.navlab.data.SessionManager
import com.floresvalencia.navlab.data.Student
import com.floresvalencia.navlab.data.StudentRepository
import com.floresvalencia.navlab.navigation.Screen
import com.floresvalencia.navlab.navigation.logout
import com.floresvalencia.navlab.ui.components.GradientHeader
import com.floresvalencia.navlab.ui.components.InfoRow
import com.floresvalencia.navlab.ui.components.SectionTitle
import com.floresvalencia.navlab.ui.components.StudentAvatar
import com.floresvalencia.navlab.ui.theme.LavenderBackground
import com.floresvalencia.navlab.ui.theme.LavenderCard
import com.floresvalencia.navlab.ui.theme.LogoutRed
import com.floresvalencia.navlab.ui.theme.LogoutRedContainer
import com.floresvalencia.navlab.ui.theme.NavLabTheme

@Composable
fun ProfileScreen(navController: NavController) {
    val currentUser = SessionManager.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (currentUser != null) {
        ProfileScreenContent(
            student = currentUser,
            onBackClick = { navController.popBackStack() },
            onLogoutClick = { navController.logout() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    student: Student,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuración de Perfil",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LavenderBackground
                )
            )
        },
        containerColor = LavenderBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado con degradado horizontal morado -> malva y avatar centrado
            GradientHeader(
                height = 200.dp,
                horizontalGradient = true,
                bottomCornerRadius = 24.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    StudentAvatar(
                        name = student.fullName,
                        photoRes = student.photo,
                        size = 96.dp,
                        border = BorderStroke(3.dp, Color.White)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = student.fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Sección Información Personal
                SectionTitle(title = "Información Personal")

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LavenderCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(
                            icon = Icons.Default.Person,
                            label = "Nombre Completo",
                            value = student.fullName
                        )

                        InfoRow(
                            icon = Icons.Default.Email,
                            label = "Correo",
                            value = student.email
                        )

                        InfoRow(
                            icon = Icons.Default.Phone,
                            label = "Teléfono",
                            value = student.phone
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sección Académico
                SectionTitle(title = "Académico")

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LavenderCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(
                            icon = Icons.Default.School,
                            label = "Carrera",
                            value = student.career
                        )

                        InfoRow(
                            icon = Icons.Default.CalendarMonth,
                            label = "Ciclo Actual",
                            value = student.cycle
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Botón de Cerrar Sesión al fondo
                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LogoutRedContainer,
                        contentColor = LogoutRed
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = LogoutRed
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = "Cerrar Sesión",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = LogoutRed
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    NavLabTheme(dynamicColor = false) {
        ProfileScreenContent(
            student = StudentRepository.getById(1)!!,
            onBackClick = {},
            onLogoutClick = {}
        )
    }
}
