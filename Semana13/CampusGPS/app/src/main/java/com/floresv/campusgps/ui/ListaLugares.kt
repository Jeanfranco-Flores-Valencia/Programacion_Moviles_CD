package com.floresv.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.floresv.campusgps.model.LugarConDistancia
import com.floresv.campusgps.ui.components.InsigniaRadio
import com.floresv.campusgps.ui.components.TarjetaLugar

/** Lista de lugares con su distancia. No conoce el ViewModel: recibe datos y avisa el clic. */
@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onClick: (Int) -> Unit,
    dentroDelRadio: Set<Int> = emptySet(),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { item ->
            TarjetaLugar(
                item,
                seleccionado = item.lugar.id == seleccionado,
                onClick = { onClick(item.lugar.id) },
                detalle = if (item.lugar.id in dentroDelRadio) { { InsigniaRadio() } } else null
            )
        }
    }
}
