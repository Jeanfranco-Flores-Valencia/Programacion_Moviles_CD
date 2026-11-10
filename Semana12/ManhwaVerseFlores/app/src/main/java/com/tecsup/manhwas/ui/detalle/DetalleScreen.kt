package com.tecsup.manhwas.ui.detalle

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.manhwas.domain.model.Item
import com.tecsup.manhwas.ui.UiState
import com.tecsup.manhwas.ui.components.BotonIcono
import com.tecsup.manhwas.ui.components.BotonPrimario
import com.tecsup.manhwas.ui.components.BotonSecundario
import com.tecsup.manhwas.ui.components.Cargando
import com.tecsup.manhwas.ui.components.Chip
import com.tecsup.manhwas.ui.components.EncabezadoSeccion
import com.tecsup.manhwas.ui.components.ErrorView
import com.tecsup.manhwas.ui.components.InsigniaCapitulos
import com.tecsup.manhwas.ui.components.InsigniaEstado
import com.tecsup.manhwas.ui.components.InsigniaPuntaje
import com.tecsup.manhwas.ui.components.Portada
import com.tecsup.manhwas.ui.theme.Colores

/**
 * Detalle con el segundo endpoint. Mejora IA: cabecera con la portada de fondo como en
 * las webs de lectura, insignias, chips de genero y sinopsis que se expande con "Leer mas".
 */
@Composable
fun DetalleScreen(vm: DetalleViewModel, onAtras: () -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize().background(Colores.Fondo)) {
        when (val e = estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(e.mensaje, onReintentar = vm::cargar)
            is UiState.Exito -> Contenido(e.datos, onFavorito = vm::alternarFavorito)
        }
        BotonIcono(
            Icons.AutoMirrored.Filled.ArrowBack,
            "Volver",
            onAtras,
            fondo = Color(0xD90B0B10),
            modifier = Modifier.statusBarsPadding().padding(12.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Contenido(item: Item, onFavorito: () -> Unit) {
    var sinopsisCompleta by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // ---------- Cabecera: portada de fondo difuminada con degradado ----------
        Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
            Portada(item.imagenUrl, item.titulo, Modifier.matchParentSize().alpha(0.4f))
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Brush.verticalGradient(listOf(Colores.MoradoOscuro.copy(alpha = 0.5f), Colores.Fondo)))
            )
        }

        // ---------- Portada + datos, montados sobre la cabecera ----------
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.offset(y = (-140).dp).padding(horizontal = 16.dp)
        ) {
            Portada(item.imagenUrl, item.titulo, Modifier.width(130.dp).height(195.dp).clip(RoundedCornerShape(12.dp)))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 70.dp)) {
                Text(item.titulo, style = MaterialTheme.typography.titleLarge, color = Colores.Texto, maxLines = 3, overflow = TextOverflow.Ellipsis)
                if (item.publicadoEn.isNotBlank()) {
                    Text("Publicado en ${item.publicadoEn}", style = MaterialTheme.typography.bodySmall, color = Colores.TextoSuave)
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    item.puntaje?.let { InsigniaPuntaje(it) }
                    InsigniaEstado(item.estado)
                    InsigniaCapitulos(item.capitulos)
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.offset(y = (-124).dp).padding(horizontal = 16.dp)
        ) {
            if (item.esFavorito) {
                BotonSecundario("En tus favoritos", onFavorito, icono = Icons.Filled.Favorite, activo = true)
            } else {
                BotonPrimario("Agregar a favoritos", onFavorito, icono = Icons.Filled.FavoriteBorder)
            }

            if (item.subtitulo.isNotBlank()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item.subtitulo.split(" · ").forEach { Chip(it) }
                }
            }

            EncabezadoSeccion("Sinopsis")
            Text(
                item.sinopsis.ifBlank { "Sin sinopsis disponible." },
                style = MaterialTheme.typography.bodyMedium,
                color = Colores.TextoSuave,
                maxLines = if (sinopsisCompleta) Int.MAX_VALUE else 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.animateContentSize()
            )
            if (item.sinopsis.length > 280) {
                TextButton(onClick = { sinopsisCompleta = !sinopsisCompleta }) {
                    Text(if (sinopsisCompleta) "Leer menos" else "Leer más", color = Colores.MoradoClaro)
                }
            }
        }
    }
}
