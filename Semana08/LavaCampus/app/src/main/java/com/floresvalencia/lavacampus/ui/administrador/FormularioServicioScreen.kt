package com.floresvalencia.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.model.Unidad
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.EstiloBoton
import com.floresvalencia.lavacampus.ui.components.IconoServicio
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.components.LavaTextField
import com.floresvalencia.lavacampus.ui.estudiante.Pestanas
import com.floresvalencia.lavacampus.ui.estudiante.SeccionTitulo
import com.floresvalencia.lavacampus.viewmodel.ServiciosViewModel
import java.util.Locale

/** Formulario para crear (servicioId = 0) o editar un servicio. */
@Composable
fun FormularioServicioScreen(
    vm: ServiciosViewModel,
    servicioId: Int,
    onCerrar: () -> Unit
) {
    val editando by vm.editando.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val terminado by vm.terminado.collectAsState()
    val colores = MaterialTheme.colorScheme

    var nombre by rememberSaveable { mutableStateOf("") }
    var unidad by rememberSaveable { mutableStateOf(Unidad.KG) }
    var precio by rememberSaveable { mutableStateOf("") }
    var horas by rememberSaveable { mutableStateOf(24) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var icono by rememberSaveable { mutableStateOf("wash") }
    var activo by rememberSaveable { mutableStateOf(true) }
    var cargado by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(servicioId) { vm.cargar(servicioId) }
    // Al llegar el servicio desde Room se llenan los campos una sola vez
    LaunchedEffect(editando) {
        val s = editando
        if (s != null && !cargado) {
            nombre = s.nombre
            unidad = Unidad.desde(s.unidad)
            precio = String.format(Locale.US, "%.2f", s.precio)
            horas = s.horasEntrega
            descripcion = s.descripcion
            icono = s.icono
            activo = s.activo
            cargado = true
        }
    }
    LaunchedEffect(terminado) { if (terminado) onCerrar() }

    fun construir() = Servicio(
        id = servicioId,
        nombre = nombre,
        descripcion = descripcion,
        unidad = unidad.name,
        precio = precio.replace(",", ".").toDoubleOrNull() ?: 0.0,
        horasEntrega = horas,
        activo = activo,
        icono = icono
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        AppTopBar(titulo = if (servicioId == 0) "Nuevo servicio" else "Editar servicio", onAtras = onCerrar)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            LavaTextField(valor = nombre, onCambio = { nombre = it; vm.limpiarMensaje() }, etiqueta = "Nombre del servicio")
            Spacer(Modifier.height(14.dp))

            SeccionTitulo("Se cobra por")
            Pestanas(
                opciones = Unidad.entries.map { it.etiqueta },
                elegida = unidad.ordinal,
                onElegir = { unidad = Unidad.entries[it] }
            )
            Spacer(Modifier.height(14.dp))

            LavaTextField(
                valor = precio,
                onCambio = { precio = it; vm.limpiarMensaje() },
                etiqueta = "Precio (S/)",
                teclado = KeyboardType.Decimal
            )
            Spacer(Modifier.height(14.dp))

            SeccionTitulo("Tiempo de entrega")
            val opcionesHoras = listOf(24, 48, 72)
            Pestanas(
                opciones = opcionesHoras.map { "$it h" },
                elegida = opcionesHoras.indexOf(horas).coerceAtLeast(0),
                onElegir = { horas = opcionesHoras[it] }
            )
            Spacer(Modifier.height(14.dp))

            LavaTextField(valor = descripcion, onCambio = { descripcion = it }, etiqueta = "Descripción", unaLinea = false)
            Spacer(Modifier.height(14.dp))

            SeccionTitulo("Ícono")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("wash", "iron", "shirt").forEach { opcion ->
                    val elegido = opcion == icono
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (elegido) colores.primaryContainer else colores.surface)
                            .border(
                                BorderStroke(if (elegido) 2.dp else 1.dp, if (elegido) colores.primary else colores.outlineVariant),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { icono = opcion }
                            .padding(10.dp)
                    ) {
                        IconoServicio(
                            icono = opcion,
                            tamano = 40.dp,
                            color = if (elegido) colores.primary else colores.onSurfaceVariant,
                            fondo = if (elegido) colores.primaryContainer else colores.surface
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colores.surface)
                    .border(1.dp, colores.outlineVariant, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Servicio activo", style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
                    Text("Los estudiantes podrán pedirlo", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                }
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            if (mensaje != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    mensaje ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colores.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colores.errorContainer)
                        .padding(10.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            LavaButton(texto = "Guardar servicio", onClick = { vm.guardar(construir()) })
            Spacer(Modifier.height(10.dp))
            LavaButton(texto = "Cancelar", onClick = onCerrar, estilo = EstiloBoton.CONTORNO)
            if (servicioId != 0) {
                Spacer(Modifier.height(10.dp))
                LavaButton(
                    texto = "Eliminar servicio",
                    onClick = { editando?.let { vm.eliminar(it) } },
                    estilo = EstiloBoton.PELIGRO
                )
            }
        }
    }
}
