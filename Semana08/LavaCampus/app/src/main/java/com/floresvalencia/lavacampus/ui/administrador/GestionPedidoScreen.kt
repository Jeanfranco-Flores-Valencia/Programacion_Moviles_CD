package com.floresvalencia.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.model.Casilleros
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.EstadoPedidoChip
import com.floresvalencia.lavacampus.ui.components.EstiloBoton
import com.floresvalencia.lavacampus.ui.components.LavaButton
import com.floresvalencia.lavacampus.ui.components.StepperEstado
import com.floresvalencia.lavacampus.ui.components.estado
import com.floresvalencia.lavacampus.ui.components.iniciales
import com.floresvalencia.lavacampus.ui.components.numeroPedido
import com.floresvalencia.lavacampus.ui.components.resumenLineas
import com.floresvalencia.lavacampus.ui.components.soles
import com.floresvalencia.lavacampus.ui.estudiante.SeccionTitulo
import com.floresvalencia.lavacampus.ui.estudiante.Tarjeta
import com.floresvalencia.lavacampus.ui.estudiante.textosSeguimiento
import com.floresvalencia.lavacampus.viewmodel.GestionPedidoViewModel

@Composable
fun GestionPedidoScreen(vm: GestionPedidoViewModel, onAtras: () -> Unit) {
    val pedido by vm.pedido.collectAsState()
    val ocupados by vm.casillerosOcupados.collectAsState()
    val elegido by vm.casilleroElegido.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val primerLibre by vm.primerLibre.collectAsState()
    var confirmarCancelar by remember { mutableStateOf(false) }
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        val p = pedido
        AppTopBar(
            titulo = if (p != null) "Gestionar pedido ${numeroPedido(p.pedido.id)}" else "Gestionar pedido",
            onAtras = onAtras
        )
        if (p == null) {
            Text("Cargando...", modifier = Modifier.padding(16.dp), color = colores.onSurfaceVariant)
            return@Column
        }
        val estado = p.estado
        val siguiente = estado.siguiente()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Datos del estudiante
            Tarjeta {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(colores.primaryContainer)
                    ) {
                        Text(iniciales(p.usuario.nombre), style = MaterialTheme.typography.titleSmall, color = colores.primary)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(p.usuario.nombre, style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
                        Text(p.usuario.codigo, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                        Text(p.resumenLineas(), style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                    }
                    EstadoPedidoChip(estado)
                }
            }
            Spacer(Modifier.height(12.dp))

            Tarjeta {
                if (estado == EstadoPedido.CANCELADO) {
                    Text("Pedido cancelado", color = EstadoPedido.CANCELADO.color, style = MaterialTheme.typography.bodyMedium)
                } else {
                    StepperEstado(estado = estado, vertical = true, textosLaterales = textosSeguimiento(p))
                }
            }
            Spacer(Modifier.height(16.dp))

            // Selector de casillero: se usa para pasar a LISTO
            if (estado == EstadoPedido.EN_PROCESO || estado == EstadoPedido.LISTO) {
                SeccionTitulo("Casillero para el recojo")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Casilleros.todos) { c ->
                        val ocupado = c in ocupados
                        FilterChip(
                            selected = c == elegido,
                            onClick = { vm.elegirCasillero(c) },
                            enabled = !ocupado && estado == EstadoPedido.EN_PROCESO,
                            label = { Text(c) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colores.primary,
                                selectedLabelColor = colores.onPrimary,
                                disabledContainerColor = colores.surfaceVariant,
                                disabledSelectedContainerColor = colores.primary.copy(alpha = 0.6f)
                            )
                        )
                    }
                }
                Text(
                    "Los casilleros en gris ya están ocupados",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                // Mejora IA: atajo para elegir el primer casillero libre
                if (estado == EstadoPedido.EN_PROCESO && elegido == null && primerLibre != null) {
                    TextButton(onClick = vm::usarPrimerLibre, contentPadding = PaddingValues(horizontal = 0.dp)) {
                        Text("Usar el primer casillero libre (${primerLibre})", style = MaterialTheme.typography.labelLarge)
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            // Total y cobro
            Tarjeta {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Total ${soles(p.pedido.total)}", style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
                        Text("Se cobra al entregar", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                    }
                    Text("Cobrado", style = MaterialTheme.typography.labelMedium, color = colores.onSurface)
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = p.pedido.pagado,
                        onCheckedChange = vm::marcarPagado,
                        enabled = !estado.esFinal
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colores.primaryContainer)
                    .padding(12.dp)
            ) {
                Text(
                    "Regla: no se entrega un pedido sin cobrar,",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onPrimaryContainer
                )
                Text(
                    "ni se pasa a Listo sin casillero.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = colores.onPrimaryContainer
                )
            }

            if (mensaje != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    mensaje ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = colores.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colores.surfaceVariant)
                        .padding(10.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            if (siguiente != null) {
                LavaButton(texto = "Marcar como ${siguiente.etiqueta}", onClick = vm::avanzar)
                Spacer(Modifier.height(10.dp))
            }
            if (!estado.esFinal) {
                LavaButton(
                    texto = "Cancelar pedido",
                    onClick = { confirmarCancelar = true },
                    enabled = GestionPedidoViewModel.puedeCancelar(p.pedido.estado),
                    estilo = EstiloBoton.PELIGRO
                )
            }
        }
    }

    if (confirmarCancelar) {
        AlertDialog(
            onDismissRequest = { confirmarCancelar = false },
            title = { Text("¿Cancelar este pedido?") },
            text = { Text("El estudiante verá su pedido como Cancelado.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar()
                    confirmarCancelar = false
                }) { Text("Sí, cancelar", color = colores.error) }
            },
            dismissButton = { TextButton(onClick = { confirmarCancelar = false }) { Text("No") } }
        )
    }
}
