package com.tecsup.manhwas.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tecsup.manhwas.domain.model.Item
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.components.Cargando
import com.tecsup.manhwas.ui.components.ErrorView
import com.tecsup.manhwas.ui.theme.Colores
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(vm: DetalleViewModel, onAtras: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val item = (estado as? UiState.Exito)?.datos

    Scaffold(
        containerColor = Colores.Fondo,
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onAtras) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                },
                actions = {
                    if (item != null) {
                        IconButton(onClick = vm::alternarFavorito) {
                            Icon(
                                if (item.esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                                tint = if (item.esFavorito) Colores.Rojo else Colores.Texto
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Colores.Fondo)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(e.mensaje, onReintentar = vm::cargar)
                is UiState.Exito -> Contenido(e.datos)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Contenido(item: Item) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(130.dp)
                    .height(195.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Colores.Superficie)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(item.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Colores.Texto)
                if (item.autores.isNotBlank()) {
                    Text(item.autores, style = MaterialTheme.typography.bodySmall, color = Colores.TextoSuave)
                }
                item.puntaje?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Colores.Dorado, modifier = Modifier.size(18.dp))
                        Text(" " + String.format(Locale.US, "%.2f", it), color = Colores.Texto, fontWeight = FontWeight.SemiBold)
                    }
                }
                Text("Estado: ${item.estado}", style = MaterialTheme.typography.bodyMedium, color = Colores.Texto)
                Text(
                    "Capítulos: ${item.capitulos ?: "en curso"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Colores.Texto
                )
            }
        }

        if (item.subtitulo.isNotBlank()) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item.subtitulo.split(" · ").forEach { genero ->
                    Text(
                        genero,
                        style = MaterialTheme.typography.labelMedium,
                        color = Colores.Cian,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Colores.Cian.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Text("Sinopsis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Colores.Texto)
        Text(
            item.sinopsis.ifBlank { "Sin sinopsis disponible." },
            style = MaterialTheme.typography.bodyMedium,
            color = Colores.TextoSuave,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
