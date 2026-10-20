package com.floresvalencia.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.model.Unidad
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.IconoServicio
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.components.LavaTextField
import com.floresvalencia.lavacampus.ui.components.SelectorCantidad
import com.floresvalencia.lavacampus.ui.components.cantidadTexto
import com.floresvalencia.lavacampus.ui.components.fechaCorta
import com.floresvalencia.lavacampus.ui.components.soles
import com.floresvalencia.lavacampus.viewmodel.NuevoPedidoViewModel

@Composable
fun NuevoPedidoScreen(
    vm: NuevoPedidoViewModel,
    onAtras: () -> Unit,
    onCreado: () -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val cantidades by vm.cantidades.collectAsState()
    val nota by vm.nota.collectAsState()
    val lineas by vm.lineas.collectAsState()
    val total by vm.total.collectAsState()
    val fechaEntrega by vm.fechaEntrega.collectAsState()
    val puedeConfirmar by vm.puedeConfirmar.collectAsState()
    val recibiendo by vm.recibiendoPedidos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val creado by vm.creado.collectAsState()
    val colores = MaterialTheme.colorScheme

    LaunchedEffect(creado) {
        if (creado) onCreado()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        AppTopBar(titulo = "Nuevo pedido", onAtras = onAtras)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            if (!recibiendo) {
                Aviso("La lavandería no está recibiendo pedidos", "Vuelve a intentarlo más tarde.")
                Spacer(Modifier.height(12.dp))
            }
            Text("¿Qué necesitas?", style = MaterialTheme.typography.titleMedium, color = colores.onBackground)
            Spacer(Modifier.height(10.dp))
            servicios.forEach { s ->
                ServicioElegible(
                    servicio = s,
                    cantidad = cantidades[s.id] ?: 0.0,
                    onCambiar = { delta -> vm.cambiarCantidad(s.id, delta) }
                )
                Spacer(Modifier.height(10.dp))
            }

            LavaTextField(
                valor = nota,
                onCambio = vm::cambiarNota,
                etiqueta = "Nota (opcional)",
                placeholder = "Ej. Ropa de color separada",
                unaLinea = false
            )
            Spacer(Modifier.height(14.dp))

            // Resumen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colores.primaryContainer.copy(alpha = 0.5f))
                    .padding(14.dp)
            ) {
                Text("Resumen", style = MaterialTheme.typography.titleSmall, color = colores.primary)
                Spacer(Modifier.height(6.dp))
                if (lineas.isEmpty()) {
                    Text(
                        "Elige al menos un servicio",
                        style = MaterialTheme.typography.bodySmall,
                        color = colores.onSurfaceVariant
                    )
                }
                lineas.forEach { l ->
                    Row(Modifier.padding(vertical = 2.dp)) {
                        Text(
                            "${l.servicio.nombre} · ${cantidadTexto(l.cantidad, Unidad.desde(l.servicio.unidad))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = colores.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Text(soles(l.subtotal), style = MaterialTheme.typography.bodySmall, color = colores.onSurface)
                    }
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = colores.outlineVariant)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Total", style = MaterialTheme.typography.titleSmall, color = colores.onSurface, modifier = Modifier.weight(1f))
                    Text(soles(total), style = MaterialTheme.typography.titleLarge, color = colores.primary)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                fechaEntrega?.let { "Entrega estimada: ${fechaCorta(it)}" } ?: "La fecha de entrega se calcula al elegir servicios",
                style = MaterialTheme.typography.bodySmall,
                color = colores.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (mensaje != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    mensaje ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colores.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colores.errorContainer)
                        .padding(10.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            LavaButton(texto = "Confirmar pedido", onClick = vm::confirmar, enabled = puedeConfirmar)
        }
    }
}

@Composable
private fun ServicioElegible(servicio: Servicio, cantidad: Double, onCambiar: (Double) -> Unit) {
    val colores = MaterialTheme.colorScheme
    val unidad = Unidad.desde(servicio.unidad)
    val elegido = cantidad > 0
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(if (elegido) 1.5.dp else 1.dp, if (elegido) colores.primary else colores.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconoServicio(
                    icono = servicio.icono,
                    tamano = 40.dp,
                    color = if (elegido) colores.primary else colores.onSurfaceVariant,
                    fondo = if (elegido) colores.primaryContainer else colores.surfaceVariant
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(servicio.nombre, style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
                    Text(
                        "${soles(servicio.precio)} ${unidad.sufijo} · ${servicio.horasEntrega} h",
                        style = MaterialTheme.typography.bodySmall,
                        color = colores.onSurfaceVariant
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                SelectorCantidad(
                    valor = cantidad,
                    paso = if (unidad == Unidad.KG) NuevoPedidoViewModel.PASO_KG else 1.0,
                    minimo = if (unidad == Unidad.KG) NuevoPedidoViewModel.MIN_KG else 1.0,
                    maximo = if (unidad == Unidad.KG) NuevoPedidoViewModel.MAX_KG else NuevoPedidoViewModel.MAX_PIEZAS,
                    onCambiar = onCambiar,
                    sufijo = if (unidad == Unidad.KG) "kg" else ""
                )
            }
        }
    }
}

@Composable
internal fun Aviso(titulo: String, texto: String, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colores.tertiaryContainer)
            .padding(12.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.titleSmall, color = colores.onTertiaryContainer)
        if (texto.isNotEmpty()) {
            Text(texto, style = MaterialTheme.typography.bodySmall, color = colores.onTertiaryContainer)
        }
    }
}
