package com.floresv.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.floresv.centrotareas.model.CargaUi
import com.floresv.centrotareas.model.PrimosUi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.floresv.centrotareas.viewmodel.TareasViewModel

/** Pantalla con las tres tarjetas. Solo muestra el estado y avisa los eventos al ViewModel. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    // Valor por defecto ajustado para que el calculo tarde unos segundos en el emulador
    var numero by rememberSaveable { mutableStateOf("3000000") }
    // Prueba de vida: si deja de sumar, la pantalla esta congelada
    var toques by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CentroTareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Tarjeta("Tarea con progreso") {
                Button(onClick = { toques++ }) { Text("Tócame: $toques") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Iniciar se apaga mientras corre; Cancelar solo se enciende mientras corre
                    Button(onClick = vm::tareaLarga, enabled = !corriendo) { Text("Iniciar") }
                    OutlinedButton(onClick = vm::cancelar, enabled = corriendo) { Text("Cancelar") }
                }
                LinearProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth())
                Text("${(progreso * 100).toInt()} %")
                Text(mensaje)
            }
            Tarjeta("Contar números primos") {
                val calculando = primos is PrimosUi.Calculando
                // Solo numeros; textos no numericos o menores que 2 no habilitan Contar
                val hasta = numero.toIntOrNull()?.takeIf { it >= 2 }
                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it.filter(Char::isDigit).take(9) },
                    label = { Text("Contar primos hasta") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { hasta?.let(vm::contar) }, enabled = hasta != null && !calculando) { Text("Contar") }
                    OutlinedButton(onClick = vm::cancelarPrimos, enabled = calculando) { Text("Cancelar") }
                }
                when (val p = primos) {
                    PrimosUi.Inicial -> Unit
                    PrimosUi.Calculando -> Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Text("Calculando...")
                    }
                    is PrimosUi.Listo -> Recuadro("${p.cantidad} números primos · ${p.milisegundos} ms", exito = true)
                    PrimosUi.Cancelado -> Text("Cancelado")
                }
            }
            Tarjeta("Dos cargas") {
                val cargando = carga is CargaUi.Cargando
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Simular error", modifier = Modifier.weight(1f))
                    Switch(checked = simularError, onCheckedChange = vm::cambiarSimularError)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Deshabilitados mientras carga: no se lanza una segunda carga
                    OutlinedButton(onClick = vm::cargarUnaPorUna, enabled = !cargando) { Text("Una por una") }
                    Button(onClick = vm::cargarALaVez, enabled = !cargando) { Text("A la vez") }
                }
                when (val c = carga) {
                    CargaUi.Inicial -> Unit
                    CargaUi.Cargando -> CircularProgressIndicator()
                    is CargaUi.Exito -> Recuadro(
                        "${c.modo}: ${c.perfil} · ${c.pedidos} pedidos · ${c.milisegundos} ms",
                        exito = true
                    )
                    is CargaUi.Error -> {
                        Recuadro(c.mensaje, exito = false)
                        Button(onClick = vm::cargarALaVez) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}

/** Recuadro de resultado: verde si salio bien, color de error si fallo. */
@Composable
fun Recuadro(texto: String, exito: Boolean) {
    Text(
        texto,
        color = if (exito) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onErrorContainer,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (exito) Color(0xFFDFF3E0) else MaterialTheme.colorScheme.errorContainer)
            .padding(12.dp)
    )
}

/** Card con el titulo en azul. */
@Composable
fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            contenido()
        }
    }
}
