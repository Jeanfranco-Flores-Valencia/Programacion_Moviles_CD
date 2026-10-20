package com.floresvalencia.lavacampus.data.repository

import com.floresvalencia.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de datos de LavaCampus. Los ViewModels solo conocen esta interface.
 * Hoy la implementa LavaRepositoryRoom; en la Semana 9 se podra implementar con un servicio web
 * sin cambiar ViewModels ni pantallas.
 */
interface LavaRepository {
    // Usuarios
    // TODO (Paso 4): suspend fun login(correo: String, clave: String): Usuario?
    // TODO (Paso 4): fun observarClientes(): Flow<List<ClienteResumen>>

    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    // TODO (Paso 4): suspend fun servicioTieneDetalles(id: Int): Boolean

    // Pedidos
    // TODO (Paso 4): crearPedido, contarActivos, observarPedidosDeUsuario, observarPedidos,
    //                observarPedido, cambiarEstado, marcarPagado, observarCasillerosOcupados

    // Panel
    // TODO (Paso 4): observarPedidosDeFecha, observarCantidadPorEstado,
    //                observarIngresosDesde, observarIngresosPorServicio
}
