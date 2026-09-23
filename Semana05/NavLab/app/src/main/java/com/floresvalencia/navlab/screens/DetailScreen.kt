package com.floresvalencia.navlab.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.floresvalencia.navlab.ui.components.GradientHeader
import com.floresvalencia.navlab.ui.components.InfoRow
import com.floresvalencia.navlab.ui.components.StudentAvatar
import com.floresvalencia.navlab.ui.theme.LavenderBackground
import com.floresvalencia.navlab.ui.theme.LavenderCard
import com.floresvalencia.navlab.ui.theme.LavenderContainer
import com.floresvalencia.navlab.ui.theme.NavLabTheme
import com.floresvalencia.navlab.ui.theme.PurplePrimary
import com.floresvalencia.navlab.ui.theme.TextSecondary

@Composable
fun DetailScreen(navController: NavController, studentId: Int) {
    val currentUser = SessionManager.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (currentUser != null) {
        val student = StudentRepository.getById(studentId)
        DetailScreenContent(
            student = student,
            onBackClick = { navController.popBackStack() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    student: Student?,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Expediente Académico",
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
        if (student == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Alumno no encontrado",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Encabezado con degradado y avatar superpuesto
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    GradientHeader(
                        height = 130.dp,
                        bottomCornerRadius = 24.dp
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 70.dp)
                    ) {
                        StudentAvatar(
                            name = student.name,
                            photoRes = student.photo,
                            size = 120.dp,
                            border = BorderStroke(4.dp, Color.White)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = student.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = student.career,
                            style = MaterialTheme.typography.bodyLarge,
                            color = PurplePrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tarjeta de información académica
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = LavenderCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(
                            icon = Icons.Default.Badge,
                            label = "ID Estudiante",
                            value = student.studentCode
                        )

                        InfoRow(
                            icon = Icons.Default.Email,
                            label = "Correo Electrónico",
                            value = student.email
                        )

                        InfoRow(
                            icon = Icons.Default.School,
                            label = "Facultad",
                            value = student.faculty
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = LavenderContainer)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Biografía",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = student.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    NavLabTheme(dynamicColor = false) {
        DetailScreenContent(
            student = StudentRepository.getById(1),
            onBackClick = {}
        )
    }
}
