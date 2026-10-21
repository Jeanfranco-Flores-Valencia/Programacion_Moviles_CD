package com.floresvalencia.lavacampus.ui.navigation

/** Rutas de navegacion. Cada perfil tiene su propio grafo. */
object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    /** Mejora IA: registro de estudiantes */
    const val REGISTRO = "registro"

    // Grafo del estudiante
    const val GRAFO_ESTUDIANTE = "grafo_estudiante"
    const val INICIO = "inicio"
    /** Mejora IA: argumento opcional "repetir" con el id del pedido a repetir (0 = pedido nuevo). */
    const val NUEVO_PEDIDO = "nuevo_pedido?repetir={repetir}"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val DETALLE_PEDIDO = "detalle_pedido/{id}"
    const val TARIFAS = "tarifas"
    const val PERFIL = "perfil"

    // Grafo del administrador
    const val GRAFO_ADMIN = "grafo_admin"
    const val PANEL = "panel"
    const val BANDEJA = "bandeja"
    const val GESTION_PEDIDO = "gestion_pedido/{id}"
    const val SERVICIOS = "servicios"
    const val FORMULARIO_SERVICIO = "formulario_servicio/{id}"
    const val CLIENTES = "clientes"
    const val AJUSTES = "ajustes"

    fun nuevoPedido(repetir: Int = 0) = "nuevo_pedido?repetir=$repetir"
    fun detallePedido(id: Int) = "detalle_pedido/$id"
    fun gestionPedido(id: Int) = "gestion_pedido/$id"
    /** id = 0 para un servicio nuevo */
    fun formularioServicio(id: Int) = "formulario_servicio/$id"
}
