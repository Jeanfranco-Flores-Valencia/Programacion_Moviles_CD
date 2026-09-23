package com.floresvalencia.navlab.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.floresvalencia.navlab.data.SessionManager
import com.floresvalencia.navlab.data.Student
import com.floresvalencia.navlab.data.StudentRepository
import com.floresvalencia.navlab.navigation.Screen
import com.floresvalencia.navlab.ui.components.StudentAvatar
import com.floresvalencia.navlab.ui.theme.LavenderBackground
import com.floresvalencia.navlab.ui.theme.LavenderCard
import com.floresvalencia.navlab.ui.theme.NavLabTheme
import com.floresvalencia.navlab.ui.theme.PurplePrimary

@Composable
fun ListScreen(navController: NavController) {
    val currentUser = SessionManager.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (currentUser != null) {
        ListScreenContent(
            students = StudentRepository.getAll(),
            onBackClick = { navController.popBackStack() },
            onStudentClick = { studentId ->
                navController.navigate(Screen.Detail.createRoute(studentId))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenContent(
    students: List<Student>,
    onBackClick: () -> Unit,
    onStudentClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Directorio de Alumnos",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(students, key = { it.id }) { student ->
                StudentCardItem(
                    student = student,
                    onClick = { onStudentClick(student.id) }
                )
            }
        }
    }
}

@Composable
fun StudentCardItem(
    student: Student,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LavenderCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudentAvatar(
                name = student.name,
                photoRes = student.photo,
                size = 48.dp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = student.career,
                    style = MaterialTheme.typography.bodyMedium,
                    color = PurplePrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ver expediente",
                tint = PurplePrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    NavLabTheme(dynamicColor = false) {
        ListScreenContent(
            students = StudentRepository.getAll(),
            onBackClick = {},
            onStudentClick = {}
        )
    }
}
