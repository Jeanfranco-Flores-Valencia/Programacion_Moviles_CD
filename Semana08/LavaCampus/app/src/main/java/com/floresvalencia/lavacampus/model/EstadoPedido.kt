package com.floresvalencia.lavacampus.model

import androidx.compose.ui.graphics.Color

/**
 * Maquina de estados del pedido:
 * RECIBIDO -> EN_PROCESO -> LISTO -> ENTREGADO. CANCELADO y ENTREGADO son finales.
 * El orden de declaracion importa: se usa para el seguimiento (StepperEstado).
 */
enum class EstadoPedido(val etiqueta: String, val color: Color) {
    RECIBIDO("Recibido", Color(0xFFD97706)),
    EN_PROCESO("En proceso", Color(0xFF2563EB)),
    LISTO("Listo", Color(0xFF16A34A)),
    ENTREGADO("Entregado", Color(0xFF475569)),
    CANCELADO("Cancelado", Color(0xFFDC2626));

    /** Siguiente estado permitido (solo se avanza un paso). Null si es final. */
    fun siguiente(): EstadoPedido? = when (this) {
        RECIBIDO -> EN_PROCESO
        EN_PROCESO -> LISTO
        LISTO -> ENTREGADO
        ENTREGADO, CANCELADO -> null
    }

    val esActivo: Boolean get() = this == RECIBIDO || this == EN_PROCESO || this == LISTO
    val esFinal: Boolean get() = this == ENTREGADO || this == CANCELADO

    companion object {
        /** Las 4 etapas que se dibujan en el seguimiento. */
        val etapas = listOf(RECIBIDO, EN_PROCESO, LISTO, ENTREGADO)
        val activos = listOf(RECIBIDO, EN_PROCESO, LISTO)

        fun desde(texto: String): EstadoPedido =
            entries.firstOrNull { it.name == texto } ?: RECIBIDO
    }
}
