package com.saludplus.citas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.saludplus.citas.R
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.Amarillo
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.BordeSuave
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoPrincipal
import com.saludplus.citas.ui.theme.TextoSecundario
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeExito

// ======================================================================
// Barra superior con flecha (estilo común de todas las pantallas internas)
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
            containerColor = Blanco,
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
            containerColor = AzulPrimario,
            contentColor = Blanco,
            disabledContainerColor = AzulPrimario.copy(alpha = 0.35f),
            disabledContentColor = Blanco
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
    color: Color = AzulPrimario
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
// Ícono dentro de una caja redondeada de color
// ======================================================================

@Composable
fun IconoEnCaja(
    icono: ImageVector,
    colorFondo: Color = AzulClaro,
    colorIcono: Color = AzulPrimario,
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
// Campo de formulario: ícono a la izquierda + OutlinedTextField con etiqueta
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
                { Text(error, color = Rojo) }
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
                focusedBorderColor = AzulPrimario,
                unfocusedBorderColor = BordeSuave,
                focusedLabelColor = AzulPrimario,
                unfocusedLabelColor = TextoSecundario,
                focusedContainerColor = Blanco,
                unfocusedContainerColor = Blanco
            )
        )
    }
}

// ======================================================================
// Avatar del médico
// ======================================================================

@Composable
fun AvatarMedico(medico: Medico, tamano: Dp = 64.dp) {
    Image(
        painter = painterResource(
            id = if (medico.esMujer) R.drawable.avatar_doctora else R.drawable.avatar_doctor
        ),
        contentDescription = medico.nombre,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(tamano)
            .clip(CircleShape)
    )
}

/** Avatar con iniciales para el paciente. */
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
            .background(AzulPrimario),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales.ifEmpty { "P" },
            color = Blanco,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano.value * 0.36f).sp
        )
    }
}

// ======================================================================
// Tarjeta base blanca con borde suave
// ======================================================================

@Composable
fun TarjetaBase(
    modifier: Modifier = Modifier,
    colorFondo: Color = Blanco,
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
        border = BorderStroke(1.dp, BordeSuave),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        contenido()
    }
}

// ======================================================================
// Especialidades
// ======================================================================

/** Fila de especialidad (pantalla Especialidades). */
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

/** Tarjeta pequeña de especialidad destacada (LazyRow de Inicio). */
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
        Icon(Icons.Filled.Star, contentDescription = null, tint = Amarillo, modifier = Modifier.size(16.dp))
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
        color = VerdeExito,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(VerdeClaro)
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

/** Encabezado con el médico (Fecha y hora, Confirmar cita, Detalle). */
@Composable
fun EncabezadoMedico(medico: Medico, mostrarCmp: Boolean = false) {
    TarjetaBase(modifier = Modifier.fillMaxWidth(), colorFondo = FondoApp) {
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
// Detalle (ícono + etiqueta + valor) usado en Confirmar cita / Detalle
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
                    .background(AzulClaro)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.CalendarMonth, null, tint = AzulPrimario, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    textoFecha(cita.fecha),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoPrincipal,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Filled.Schedule, null, tint = AzulPrimario, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(cita.hora, style = MaterialTheme.typography.bodyMedium, color = TextoPrincipal)
            }
        }
    }
}

// ======================================================================
// Mensaje para listas vacías
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
