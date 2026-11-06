package com.tecsup.authfirebaseflores

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.LocalTime

/** Mejora IA: un consejo de estudio distinto cada dia. */
private val consejos = listOf(
    "Estudia en bloques de 25 minutos y descansa 5: tu mente rinde más (técnica Pomodoro).",
    "Explica el tema en voz alta como si se lo enseñaras a alguien: así detectas lo que no entiendes.",
    "Repasa hoy lo que aprendiste ayer. Repetir en días distintos fija mejor lo aprendido.",
    "Guarda el celular lejos mientras estudias: cada notificación rompe tu concentración.",
    "Practica con ejercicios, no solo leyendo. Programar se aprende programando.",
    "Duerme bien antes de un examen: el sueño ayuda a ordenar lo que estudiaste.",
    "Divide un proyecto grande en tareas pequeñas y celebra cada una que termines."
)

/**
 * Pantalla de bienvenida: muestra el correo de la sesion y permite cerrarla.
 * Mejora IA: saludo segun la hora, avatar con la inicial, consejo del dia, espacio para
 * "Mis cursos" y confirmacion antes de cerrar sesion. Las tarjetas aparecen una tras otra.
 */
@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val email = auth.currentUser?.email ?: "Usuario"
    val saludo = when (LocalTime.now().hour) {
        in 5..11 -> "¡Buenos días!"
        in 12..18 -> "¡Buenas tardes!"
        else -> "¡Buenas noches!"
    }
    val consejo = consejos[LocalDate.now().dayOfYear % consejos.size]
    var confirmarSalida by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // ---------- Saludo ----------
            Aparecer(orden = 0) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Paleta.Celeste, Paleta.Pizarra)))
                            .border(2.dp, Paleta.Borde, CircleShape)
                    ) {
                        Text(
                            email.firstOrNull()?.uppercase() ?: "U",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(saludo, style = MaterialTheme.typography.bodyMedium, color = Paleta.TextoSuave)
                        Text("Bienvenido/a 🎉", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // ---------- Sesion ----------
            Aparecer(orden = 1) {
                Column(modifier = Modifier.fillMaxWidth().tarjeta().padding(20.dp)) {
                    Text("Sesión iniciada como:", style = MaterialTheme.typography.labelLarge, color = Paleta.TextoSuave)
                    Text(email, style = MaterialTheme.typography.titleMedium, color = Paleta.CelesteClaro)
                }
            }

            // ---------- Consejo del dia ----------
            Aparecer(orden = 2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth().tarjeta().padding(20.dp)
                ) {
                    Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = Paleta.Ambar, modifier = Modifier.size(28.dp))
                    Column {
                        Text("Consejo de estudio del día", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(consejo, style = MaterialTheme.typography.bodyMedium, color = Paleta.TextoSuave)
                    }
                }
            }

            // ---------- Mis cursos (proximamente) ----------
            Aparecer(orden = 3) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().tarjeta().padding(20.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Outlined.MenuBook,
                        contentDescription = null,
                        tint = Paleta.Celeste,
                        modifier = Modifier.size(28.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mis cursos", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Muy pronto podrás registrar y organizar tus cursos aquí.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Paleta.TextoSuave
                        )
                    }
                    Text(
                        "Pronto",
                        style = MaterialTheme.typography.labelSmall,
                        color = Paleta.CelesteClaro,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Paleta.Celeste.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // ---------- Cerrar sesion ----------
            Aparecer(orden = 4) {
                OutlinedButton(
                    onClick = { confirmarSalida = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Paleta.Texto),
                    border = BorderStroke(1.dp, Paleta.Borde),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                    Text("  Cerrar sesión")
                }
            }
        }
    }

    if (confirmarSalida) {
        AlertDialog(
            onDismissRequest = { confirmarSalida = false },
            title = { Text("¿Cerrar sesión?") },
            text = { Text("Tendrás que ingresar tu correo y contraseña otra vez.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarSalida = false
                    auth.signOut()
                    onLogout()
                }) { Text("Cerrar sesión", color = Paleta.Error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarSalida = false }) { Text("Cancelar") }
            },
            containerColor = Paleta.Pizarra
        )
    }
}

/** Hace aparecer su contenido subiendo, con un pequeno retraso segun el orden (efecto cascada). */
@Composable
private fun Aparecer(orden: Int, content: @Composable () -> Unit) {
    val estado = remember { MutableTransitionState(false).apply { targetState = true } }
    val retraso = orden * 110
    AnimatedVisibility(
        visibleState = estado,
        enter = fadeIn(tween(500, delayMillis = retraso)) +
            slideInVertically(tween(500, delayMillis = retraso, easing = FastOutSlowInEasing)) { it / 3 }
    ) {
        content()
    }
}
