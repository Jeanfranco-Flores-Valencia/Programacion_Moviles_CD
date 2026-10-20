package com.floresvalencia.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresvalencia.lavacampus.model.textoPrecio
import com.floresvalencia.lavacampus.ui.components.AppTopBar
import com.floresvalencia.lavacampus.ui.components.IconoServicio
import com.floresvalencia.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun TarifasScreen(vm: MisPedidosViewModel, onAtras: () -> Unit) {
    val servicios by vm.tarifas.collectAsState()
    val colores = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        AppTopBar(titulo = "Tarifas y servicios", onAtras = onAtras)
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(servicios, key = { it.id }) { s ->
                Tarjeta {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconoServicio(icono = s.icono, tamano = 48.dp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(s.nombre, style = MaterialTheme.typography.titleSmall, color = colores.onSurface)
                            Text(
                                s.textoPrecio(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = colores.primary
                            )
                            Text(
                                "Entrega en ${s.horasEntrega} h",
                                style = MaterialTheme.typography.bodySmall,
                                color = colores.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            item {
                Tarjeta {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Outlined.Schedule, contentDescription = null, tint = colores.primary)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Horario de atención", style = MaterialTheme.typography.titleSmall, color = colores.primary)
                            Text("Lunes a sábado · 8:00 a 18:00", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                            Text("Recojo en casilleros del pabellón A", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                        }
                    }
                }
            }
            item {
                Aviso(
                    titulo = "Mínimo 1 kg por servicio de lavado.",
                    texto = "Máximo 3 pedidos activos por estudiante."
                )
            }
        }
    }
}
