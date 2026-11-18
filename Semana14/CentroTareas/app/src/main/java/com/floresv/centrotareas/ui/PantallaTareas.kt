package com.floresv.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.floresv.centrotareas.ui.components.PulsoInterfaz
import com.floresv.centrotareas.viewmodel.TareasViewModel

/**
 * Mejora IA: panel de tareas con una barra grande que se encoge al desplazarse, el "pulso" de la
 * interfaz arriba y una tarjeta por tarea. Solo lee el estado del ViewModel y le pasa los eventos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsState()
    val progreso by vm.progreso.collectAsState()
    val corriendo by vm.corriendo.collectAsState()
    val primos by vm.primos.collectAsState()
    val avancePrimos by vm.avancePrimos.collectAsState()
    val carga by vm.carga.collectAsState()
    val simularError by vm.simularError.collectAsState()
    val limiteTiempo by vm.limiteTiempo.collectAsState()
    val historial by vm.historial.collectAsState()
    val resumen by vm.resumen.collectAsState()
    // Valor por defecto ajustado para que el calculo tarde unos segundos en el emulador
    var numero by rememberSaveable { mutableStateOf("3000000") }
    // Prueba de vida: si deja de sumar, la pantalla esta congelada
    var toques by rememberSaveable { mutableIntStateOf(0) }
    val desplazamiento = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.nestedScroll(desplazamiento.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("CentroTareas") },
                scrollBehavior = desplazamiento,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            item { PulsoInterfaz(toques, onTocar = { toques++ }) }
            item { TarjetaProgreso(progreso, corriendo, mensaje, vm::tareaLarga, vm::cancelar) }
            item { TarjetaPrimos(primos, avancePrimos, numero, { numero = it }, vm::contar, vm::cancelarPrimos) }
            item { TarjetaCargas(carga, simularError, vm::cambiarSimularError, limiteTiempo, vm::cambiarLimiteTiempo, vm::cargarUnaPorUna, vm::cargarALaVez) }
            item { TarjetaComparacion(historial, resumen, vm::limpiarHistorial) }
        }
    }
}
