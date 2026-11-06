package com.tecsup.authfirebaseflores

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Mejora IA: pantalla de acceso con una sola tarjeta y dos pestanas (Iniciar sesion / Crear cuenta).
 * Los formularios siguen siendo LoginScreen y RegisterScreen; aqui solo se cambia entre ellos con animacion.
 */
@Composable
fun AuthScreen(onAutenticado: () -> Unit) {
    var pestana by rememberSaveable { mutableIntStateOf(0) }
    // La tarjeta sube y aparece la primera vez que se muestra la pantalla
    val aparecer = remember { MutableTransitionState(false).apply { targetState = true } }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // Alto de la pantalla: permite centrar la tarjeta y aun asi desplazar cuando sale el teclado
        val altoPantalla = maxHeight
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .heightIn(min = altoPantalla)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                AnimatedVisibility(
                    visibleState = aparecer,
                    enter = fadeIn(tween(700)) + slideInVertically(tween(700, easing = FastOutSlowInEasing)) { it / 5 }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .widthIn(max = 440.dp)
                            .fillMaxWidth()
                            .tarjeta()
                            // Antes del padding: asi el recorte de la animacion incluye el margen
                            // y el logo que flota no se corta por arriba
                            .animateContentSize()
                            .padding(24.dp)
                    ) {
                        Encabezado()
                        Spacer(Modifier.height(24.dp))
                        Pestanas(seleccionada = pestana, onElegir = { pestana = it })
                        Spacer(Modifier.height(24.dp))

                        AnimatedContent(
                            targetState = pestana,
                            transitionSpec = {
                                // Se desliza hacia el lado de la pestana elegida
                                val dir = if (targetState > initialState) 1 else -1
                                (slideInHorizontally(tween(350)) { it * dir / 3 } + fadeIn(tween(350))) togetherWith
                                    (slideOutHorizontally(tween(350)) { -it * dir / 3 } + fadeOut(tween(200))) using
                                    SizeTransform(clip = false)
                            },
                            label = "formulario"
                        ) { actual ->
                            if (actual == 0) {
                                LoginScreen(onNavigateToRegister = { pestana = 1 }, onLoginSuccess = onAutenticado)
                            } else {
                                RegisterScreen(onRegisterSuccess = onAutenticado, onNavigateBack = { pestana = 0 })
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Jeanfranco Flores · Tecsup",
                    style = MaterialTheme.typography.bodySmall,
                    color = Paleta.TextoSuave,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/** Logo con un birrete que flota suave, el nombre y el curso. */
@Composable
private fun Encabezado() {
    val transicion = rememberInfiniteTransition(label = "logo")
    val flotar by transicion.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "flotar"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(68.dp)
            .graphicsLayer { translationY = flotar * density }
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(Paleta.Celeste, Paleta.Pizarra)))
    ) {
        Icon(Icons.Outlined.School, contentDescription = "Logo", tint = Color.White, modifier = Modifier.size(36.dp))
    }
    Spacer(Modifier.height(14.dp))
    Text("Tecsup", color = Paleta.Celeste, fontSize = 28.sp, fontWeight = FontWeight.Bold)
    Text(
        "Programación Móvil · Tu espacio para aprender",
        style = MaterialTheme.typography.bodySmall,
        color = Paleta.TextoSuave,
        textAlign = TextAlign.Center
    )
}

/** Dos pestanas con un subrayado celeste que se desliza a la elegida. */
@Composable
private fun Pestanas(seleccionada: Int, onElegir: (Int) -> Unit) {
    val titulos = listOf("Iniciar sesión", "Crear cuenta")
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val mitad = maxWidth / 2
        val desplazamiento by animateDpAsState(
            targetValue = mitad * seleccionada,
            animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow),
            label = "subrayado"
        )
        Column {
            Row {
                titulos.forEachIndexed { i, titulo ->
                    val color by animateColorAsState(
                        if (i == seleccionada) Paleta.Texto else Paleta.TextoSuave,
                        label = "color"
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onElegir(i) }
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            titulo,
                            color = color,
                            fontWeight = if (i == seleccionada) FontWeight.SemiBold else FontWeight.Normal,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth().height(2.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.Center)
                        .background(Paleta.Borde)
                )
                Box(
                    modifier = Modifier
                        .offset(x = desplazamiento)
                        .width(mitad)
                        .height(2.dp)
                        .background(Paleta.Celeste)
                )
            }
        }
    }
}
