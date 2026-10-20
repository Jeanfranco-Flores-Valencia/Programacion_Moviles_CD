package com.floresvalencia.lavacampus.data.repository

import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.data.local.entity.Usuario
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.model.ClienteResumen
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.model.IngresoServicio
import com.floresvalencia.lavacampus.model.LineaPedido
import com.floresvalencia.lavacampus.model.Rol
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de datos de LavaCampus. Los ViewModels solo conocen esta interface.
 * Hoy la implementa LavaRepositoryRoom; en la Semana 9 se podra implementar con un servicio web
 * sin cambiar ViewModels ni pantallas.
 * Regla de oro: lectura que actualiza la pantalla -> Flow. Escritura o accion unica -> suspend.
 */
interface LavaRepository {
    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun obtenerUsuario(id: Int): Usuario?
    suspend fun primerUsuarioDeRol(rol: Rol): Usuario?
    fun observarClientes(): Flow<List<ClienteResumen>>

    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneDetalles(id: Int): Boolean

    // Pedidos
    suspend fun crearPedido(
        usuarioId: Int, lineas: List<LineaPedido>,
        fechaEntrega: String, nota: String,
        descuento: Double = 0.0   // Mejora IA: descuento por volumen calculado en el ViewModel
    ): Long
    suspend fun contarActivos(usuarioId: Int): Int
    fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>>
    fun observarPedidos(): Flow<List<PedidoConDetalle>>
    fun observarPedido(id: Int): Flow<PedidoConDetalle?>
    suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?)
    suspend fun marcarPagado(id: Int, pagado: Boolean)
    fun observarCasillerosOcupados(): Flow<List<String>>

    // Panel
    fun observarPedidosDeFecha(fecha: String): Flow<Int>
    fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int>
    fun observarIngresosDesde(fechaInicio: String): Flow<Double>
    fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>>

    // Mejora IA: pedidos atrasados
    fun observarPedidosAtrasados(hoy: String): Flow<List<Int>>
}
