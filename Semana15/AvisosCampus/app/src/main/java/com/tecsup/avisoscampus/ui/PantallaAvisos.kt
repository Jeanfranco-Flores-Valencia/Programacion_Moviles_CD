package com.tecsup.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.PermisoUi
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.receiver.CargadorReceiver
import com.tecsup.avisoscampus.receiver.ModoAvionReceiver
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

/** Pantalla unica de la app: una tarjeta por funcion. Solo dibuja el estado y avisa los eventos. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    val avisos by vm.avisos.collectAsState()
    val escuchandoCargador by vm.escuchandoCargador.collectAsState()

    // Mejora IA: el receptor del cargador sigue la misma regla: se registra con el switch y se quita en onDispose
    DisposableEffect(escuchandoCargador) {
        if (escuchandoCargador) {
            val receptor = CargadorReceiver { conectado -> vm.alCambiarCargador(conectado) }
            val filtro = IntentFilter().apply {
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
            }
            ContextCompat.registerReceiver(contexto, receptor, filtro, ContextCompat.RECEIVER_NOT_EXPORTED)
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }
    val mensaje by vm.mensaje.collectAsState()

    // El receptor solo existe mientras el switch esta activado; al salir siempre se quita
    DisposableEffect(escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver { activo -> vm.alCambiarModoAvion(activo) }
            ContextCompat.registerReceiver(
                contexto, receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else {
            onDispose { }
        }
    }

    // Android dibuja el cuadro de permiso; la app solo recibe la respuesta
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        vm.permisoRespondido(concedido)
    }

    // Si el permiso ya estaba concedido (o el celular es anterior a Android 13), se avisa al ViewModel
    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    // Ya viene hecho: el ViewModel emite el aviso y la pantalla (que tiene Context) lo muestra
    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            if (!mostrado) vm.noSePudoMostrar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Tarjeta("Permiso de notificaciones") {
                FilaEstado(permiso)
                Button(onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                    else vm.permisoRespondido(true)
                }) { Text("Permitir notificaciones") }
            }
            // Paso 8: mensaje de error y acceso a los ajustes de notificaciones de la app
            mensaje?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.errorContainer).padding(12.dp)
                )
                OutlinedButton(
                    onClick = {
                        contexto.startActivity(
                            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) { Text("Abrir ajustes") }
            }
            Tarjeta("Avisos") {
                Button(onClick = vm::avisarAhora) { Text("Mostrar aviso") }
            }
            Tarjeta("Recordatorio") {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Deshabilitado mientras corre la cuenta; Cancelar solo mientras corre
                    Button(onClick = { vm.recordarEn(10) }, enabled = faltan == null) { Text("Recordar en 10 s") }
                    OutlinedButton(onClick = vm::cancelarRecordatorio, enabled = faltan != null) { Text("Cancelar") }
                }
                faltan?.let { Text("Faltan $it s", fontWeight = FontWeight.Bold) }
            }
            Tarjeta("Receptor de anuncios") {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Escuchar el modo avión", modifier = Modifier.weight(1f))
                    Switch(checked = escuchando, onCheckedChange = vm::cambiarEscucha)
                }
                Text(
                    "Activa o desactiva el modo avión del celular",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Text("Escuchar el cargador", modifier = Modifier.weight(1f))
                    Switch(checked = escuchandoCargador, onCheckedChange = vm::cambiarEscuchaCargador)
                }
                Text(
                    "Conecta o desconecta el cargador del celular",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            HistorialAvisos(avisos)
        }
    }
}

/** Tarjeta con el titulo en azul. La usan todas las funciones de la pantalla. */
@Composable
fun Tarjeta(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(
                titulo,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            contenido()
        }
    }
}

/** "Estado:" con una etiqueta de color: Sin pedir (gris), Permitido (verde) o Denegado (rojo). */
@Composable
fun FilaEstado(permiso: PermisoUi) {
    val (texto, fondo, color) = when (permiso) {
        PermisoUi.SinPedir -> Triple("Sin pedir", Color(0xFFE0E0E0), Color(0xFF424242))
        PermisoUi.Concedido -> Triple("Permitido", Color(0xFFE8F5E9), Color(0xFF2E7D32))
        PermisoUi.Denegado -> Triple("Denegado", Color(0xFFFFE3E0), Color(0xFFC62828))
    }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Estado:")
        Text(
            texto,
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.clip(RoundedCornerShape(50)).background(fondo).padding(horizontal = 10.dp, vertical = 2.dp)
        )
    }
}
