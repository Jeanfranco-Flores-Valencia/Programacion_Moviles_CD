package com.saludplus.citas.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.saludplus.citas.R
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.theme.Acento
import com.saludplus.citas.ui.theme.Borde
import com.saludplus.citas.ui.theme.Error
import com.saludplus.citas.ui.theme.Exito
import com.saludplus.citas.ui.theme.ExitoClaro
import com.saludplus.citas.ui.theme.Fondo
import com.saludplus.citas.ui.theme.Primario
import com.saludplus.citas.ui.theme.PrimarioClaro
import com.saludplus.citas.ui.theme.PrimarioOscuro
import com.saludplus.citas.ui.theme.Secundario
import com.saludplus.citas.ui.theme.SobrePrimario
import com.saludplus.citas.ui.theme.Superficie
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import kotlin.random.Random

// ======================================================================
// Hero Header
// ======================================================================

@Composable
fun HeroHeader(
    titulo: String,
    subtitulo: String? = null,
    onAtras: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Primario, PrimarioOscuro)
                )
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (onAtras != null) {
                    IconButton(
                        onClick = onAtras,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SobrePrimario.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = SobrePrimario)
                    }
                } else {
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically, content = acciones)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium,
                color = SobrePrimario,
                fontWeight = FontWeight.Bold
            )
            if (subtitulo != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SobrePrimario.copy(alpha = 0.85f)
                )
            }
        }
    }
}

// ======================================================================
// Stepper de Agendamiento
// ======================================================================

@Composable
fun StepperAgendamiento(pasoActual: Int) {
    val pasos = listOf("Sede", "Esp.", "Doctor", "Fecha", "Confirmar")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        pasos.forEachIndexed { index, nombre ->
            val num = index + 1
            val activo = num == pasoActual
            val completado = num < pasoActual

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            if (activo || completado) Primario else Borde
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = num.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (activo || completado) SobrePrimario else TextoSecundario,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (activo) Primario else TextoSecundario,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// ======================================================================
// Confeti Animado
// ======================================================================

@Composable
fun ConfetiAnimado() {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val random = Random(42)
        val colors = listOf(Primario, Exito, Acento, Secundario)
        for (i in 0..30) {
            val startX = random.nextFloat() * size.width
            val startY = random.nextFloat() * size.height * 0.5f
            val yOffset = (animProgress * size.height * 0.8f + startY) % size.height
            val color = colors[i % colors.size]
            drawCircle(
                color = color.copy(alpha = 0.8f),
                radius = random.nextFloat() * 6f + 4f,
                center = Offset(startX, yOffset)
            )
        }
    }
}

// ======================================================================
// Barra Superior
// ======================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    onAtras: (() -> Unit)? = null,
    acciones: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (onAtras != null) {
                IconButton(onClick = onAtras) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            }
        },
        actions = acciones,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Superficie,
            titleContentColor = TextoPrincipal,
            navigationIconContentColor = TextoPrincipal,
            actionIconContentColor = TextoPrincipal
        )
    )
}

// ======================================================================
// Botones
// ======================================================================

@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primario,
            contentColor = SobrePrimario,
            disabledContainerColor = Primario.copy(alpha = 0.35f),
            disabledContentColor = SobrePrimario
        )
    ) {
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Primario
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.5.dp, color),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color)
    ) {
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
}

// ======================================================================
// Ícono en Caja
// ======================================================================

@Composable
fun IconoEnCaja(
    icono: ImageVector,
    colorFondo: Color = PrimarioClaro,
    colorIcono: Color = Primario,
    tamano: Dp = 44.dp,
    circular: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(tamano)
            .clip(if (circular) CircleShape else RoundedCornerShape(12.dp))
            .background(colorFondo),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = colorIcono,
            modifier = Modifier.size(tamano * 0.52f)
        )
    }
}

// ======================================================================
// Campo de Formulario
// ======================================================================

@Composable
fun CampoFormulario(
    etiqueta: String,
    valor: String,
    onValorCambio: (String) -> Unit,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    error: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    esPassword: Boolean = false,
    placeholder: String = ""
) {
    var mostrarPassword by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.padding(top = 10.dp)) {
            IconoEnCaja(icono = icono)
        }
        Spacer(Modifier.width(12.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambio,
            modifier = Modifier.weight(1f),
            label = { Text(etiqueta) },
            placeholder = if (placeholder.isNotEmpty()) {
                { Text(placeholder, color = TextoSecundario) }
            } else null,
            singleLine = true,
            isError = error != null,
            supportingText = if (error != null) {
                { Text(error, color = Error) }
            } else null,
            keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
            visualTransformation = if (esPassword && !mostrarPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = if (esPassword) {
                {
                    IconButton(onClick = { mostrarPassword = !mostrarPassword }) {
                        Icon(
                            imageVector = if (mostrarPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (mostrarPassword) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = TextoSecundario
                        )
                    }
                }
            } else null,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primario,
                unfocusedBorderColor = Borde,
                focusedLabelColor = Primario,
                unfocusedLabelColor = TextoSecundario,
                focusedContainerColor = Superficie,
                unfocusedContainerColor = Superficie
            )
        )
    }
}

// ======================================================================
// Imágenes y Avatares
// ======================================================================

@Composable
fun ImagenDoctor(
    fotoUrl: String,
    esMujer: Boolean,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val fallback = if (esMujer) R.drawable.avatar_doctora else R.drawable.avatar_doctor

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(fotoUrl.ifBlank { null })
            .crossfade(true)
            .error(fallback)
            .fallback(fallback)
            .placeholder(fallback)
            .build(),
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}

@Composable
fun AvatarMedico(medico: Medico, tamano: Dp = 64.dp) {
    ImagenDoctor(
        fotoUrl = medico.fotoUrl,
        esMujer = medico.esMujer,
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
    )
}

@Composable
fun AvatarIniciales(nombre: String, tamano: Dp = 72.dp) {
    val iniciales = nombre.trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
    Box(
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(Primario),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales.ifEmpty { "P" },
            color = SobrePrimario,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano.value * 0.36f).sp
        )
    }
}

// ======================================================================
// Tarjeta Base
// ======================================================================

@Composable
fun TarjetaBase(
    modifier: Modifier = Modifier,
    colorFondo: Color = Superficie,
    onClick: (() -> Unit)? = null,
    contenido: @Composable () -> Unit
) {
    val forma = RoundedCornerShape(16.dp)
    val modificador = if (onClick != null) {
        modifier.clip(forma).clickable(onClick = onClick)
    } else modifier
    Card(
        modifier = modificador,
        shape = forma,
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        border = BorderStroke(1.dp, Borde),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        contenido()
    }
}

// ======================================================================
// Especialidades
// ======================================================================

@Composable
fun TarjetaEspecialidadFila(especialidad: Especialidad, onClick: () -> Unit) {
    val (fondo, colorIcono) = coloresEspecialidad(especialidad.id)
    TarjetaBase(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoEnCaja(
                icono = iconoEspecialidad(especialidad.id),
                colorFondo = fondo,
                colorIcono = colorIcono,
                circular = true
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    especialidad.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextoPrincipal
                )
                Text(
                    especialidad.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = TextoSecundario
            )
        }
    }
}

@Composable
fun TarjetaEspecialidadDestacada(especialidad: Especialidad, onClick: () -> Unit) {
    val (fondo, colorIcono) = coloresEspecialidad(especialidad.id)
    TarjetaBase(modifier = Modifier.width(108.dp), onClick = onClick) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconoEnCaja(
                icono = iconoEspecialidad(especialidad.id),
                colorFondo = fondo,
                colorIcono = colorIcono,
                tamano = 48.dp,
                circular = true
            )
            Spacer(Modifier.height(8.dp))
            Text(
                especialidad.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                color = TextoPrincipal
            )
        }
    }
}

// ======================================================================
// Médicos
// ======================================================================

@Composable
fun Calificacion(medico: Medico) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Filled.Star, contentDescription = null, tint = Acento, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            "${medico.calificacion} (${medico.numResenas})",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )
    }
}

@Composable
fun EtiquetaDisponibilidad(texto: String) {
    Text(
        text = texto,
        color = Exito,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ExitoClaro)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
fun TarjetaMedico(medico: Medico, onClick: () -> Unit) {
    TarjetaBase(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarMedico(medico, tamano = 64.dp)
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(medico.nombre, style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                    Text(medico.titulo, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                    Spacer(Modifier.height(2.dp))
                    Calificacion(medico)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                EtiquetaDisponibilidad(medico.disponibilidad)
            }
        }
    }
}

@Composable
fun EncabezadoMedico(medico: Medico, mostrarCmp: Boolean = false) {
    TarjetaBase(modifier = Modifier.fillMaxWidth(), colorFondo = Fondo) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarMedico(medico, tamano = 64.dp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(medico.nombre, style = MaterialTheme.typography.titleMedium, color = TextoPrincipal)
                Text(medico.titulo, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                if (mostrarCmp) {
                    Text(medico.cmp, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
                }
            }
        }
    }
}

// ======================================================================
// Fila Detalle
// ======================================================================

@Composable
fun FilaDetalle(icono: ImageVector, etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconoEnCaja(icono = icono, tamano = 40.dp)
        Spacer(Modifier.width(14.dp))
        Column {
            Text(etiqueta, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            Text(valor, style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal, fontWeight = FontWeight.Medium)
        }
    }
}

// ======================================================================
// Citas
// ======================================================================

@Composable
fun TarjetaCita(cita: Cita, onClick: () -> Unit) {
    val medico = Repositorio.obtenerMedico(cita.medicoId)
    val especialidad = Repositorio.obtenerEspecialidad(cita.especialidadId)
    TarjetaBase(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (medico != null) AvatarMedico(medico, tamano = 52.dp)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        medico?.nombre ?: "Médico",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextoPrincipal
                    )
                    Text(
                        especialidad?.nombre ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                }
                EtiquetaDisponibilidad("Programada")
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimarioClaro)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.CalendarMonth, null, tint = Primario, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    textoFecha(cita.fecha),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoPrincipal,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Filled.Schedule, null, tint = Primario, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(cita.hora, style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal)
            }
        }
    }
}

// ======================================================================
// Mensaje Vacío
// ======================================================================

@Composable
fun MensajeVacio(
    icono: ImageVector,
    titulo: String,
    mensaje: String,
    modifier: Modifier = Modifier,
    accion: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconoEnCaja(icono = icono, tamano = 80.dp, circular = true)
        Spacer(Modifier.height(16.dp))
        Text(titulo, style = MaterialTheme.typography.titleLarge, color = TextoPrincipal, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Text(mensaje, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario, textAlign = TextAlign.Center)
        if (accion != null) {
            Spacer(Modifier.height(20.dp))
            accion()
        }
    }
}
