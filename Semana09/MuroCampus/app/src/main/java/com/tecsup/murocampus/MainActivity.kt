package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.murocampus.ui.PantallaCrear
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroCampusTheme {
                val vm: PostsViewModel = viewModel(factory = PostsViewModelFactory())
                // Navegacion simple: con null se ve la lista
                var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                var creando by rememberSaveable { mutableStateOf(false) }

                fun volver() {
                    if (creando) vm.reiniciarPublicacion()
                    idSeleccionado = null
                    creando = false
                }
                // Atras vuelve a la lista sin recargarla
                BackHandler(enabled = idSeleccionado != null || creando) { volver() }

                // Mejora IA: los avisos del ViewModel se muestran en un Snackbar
                val snackbar = remember { SnackbarHostState() }
                val aviso by vm.aviso.collectAsStateWithLifecycle()
                LaunchedEffect(aviso) {
                    aviso?.let {
                        snackbar.showSnackbar(it)
                        vm.avisoMostrado()
                    }
                }

                // Mejora IA: al borrar la publicacion abierta se vuelve al muro
                val eliminada by vm.eliminada.collectAsStateWithLifecycle()
                LaunchedEffect(eliminada) {
                    if (eliminada != null && eliminada == idSeleccionado) idSeleccionado = null
                    if (eliminada != null) vm.eliminacionVista()
                }

                val enLista = idSeleccionado == null && !creando
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbar) },
                    topBar = {
                        // Mejora IA: barra clara como en las redes sociales, con flecha para volver
                        TopAppBar(
                            title = {
                                Text(
                                    when {
                                        creando -> "Nueva publicación"
                                        idSeleccionado != null -> "Publicación"
                                        else -> "MuroCampus"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    color = if (enLista) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            navigationIcon = {
                                if (!enLista) IconButton(onClick = ::volver) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                        )
                    }
                ) { padding ->
                    val modificador = Modifier.padding(padding)
                    val id = idSeleccionado
                    when {
                        creando -> PantallaCrear(vm, modificador)
                        id != null -> PantallaDetalle(vm, id, modificador)
                        else -> PantallaLista(
                            vm,
                            onAbrir = { idSeleccionado = it },
                            onCrear = { creando = true },
                            modifier = modificador
                        )
                    }
                }
            }
        }
    }
}
