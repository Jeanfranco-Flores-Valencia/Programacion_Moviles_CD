package com.floresvalencia.lavacampus.data.repository

import androidx.room.withTransaction
import com.floresvalencia.lavacampus.data.local.LavaDatabase
import com.floresvalencia.lavacampus.data.local.entity.DetallePedido
import com.floresvalencia.lavacampus.data.local.entity.Pedido
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.data.local.entity.Usuario
import com.floresvalencia.lavacampus.data.local.relation.PedidoConDetalle
import com.floresvalencia.lavacampus.model.ClienteResumen
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.model.IngresoServicio
import com.floresvalencia.lavacampus.model.LineaPedido
import com.floresvalencia.lavacampus.model.Rol
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Implementacion del Repository con Room. Solo delega en los DAOs: las reglas viven en los ViewModels. */
class LavaRepositoryRoom(private val db: LavaDatabase) : LavaRepository {

    private val servicioDao = db.servicioDao()
    private val usuarioDao = db.usuarioDao()
    private val pedidoDao = db.pedidoDao()
    private val detalleDao = db.detalleDao()

    // ---------- Usuarios ----------
    override suspend fun login(correo: String, clave: String): Usuario? = usuarioDao.login(correo, clave)

    override suspend fun obtenerUsuario(id: Int): Usuario? = usuarioDao.obtener(id)

    override suspend fun primerUsuarioDeRol(rol: Rol): Usuario? = usuarioDao.primeroDeRol(rol.name)

    override fun observarClientes(): Flow<List<ClienteResumen>> = usuarioDao.observarClientes()

    // ---------- Servicios (ejemplo resuelto) ----------
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) servicioDao.insertar(s) else servicioDao.actualizar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) = servicioDao.eliminar(s)

    override suspend fun cambiarActivo(id: Int, activo: Boolean) = servicioDao.cambiarActivo(id, activo)

    override suspend fun servicioTieneDetalles(id: Int): Boolean = detalleDao.contarPorServicio(id) > 0

    // ---------- Pedidos ----------
    /**
     * Guarda el pedido y todas sus lineas en UNA transaccion.
     * Copia el precio actual de cada servicio (precio congelado). Si algo falla, no queda nada guardado.
     */
    override suspend fun crearPedido(
        usuarioId: Int,
        lineas: List<LineaPedido>,
        fechaEntrega: String,
        nota: String,
        descuento: Double
    ): Long = db.withTransaction {
        val conServicio = lineas.map { linea ->
            val servicio = servicioDao.obtener(linea.servicioId)
                ?: throw IllegalStateException("Servicio ${linea.servicioId} no existe")
            servicio to linea.cantidad
        }
        val subtotal = conServicio.sumOf { (s, cantidad) -> s.precio * cantidad }
        val total = (subtotal - descuento).coerceAtLeast(0.0)
        val pedidoId = pedidoDao.insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = LocalDate.now().toString(),
                fechaEntrega = fechaEntrega,
                estado = EstadoPedido.RECIBIDO.name,
                total = total,
                casillero = null,
                pagado = false,
                nota = nota,
                descuento = descuento
            )
        )
        detalleDao.insertarTodos(
            conServicio.map { (s, cantidad) ->
                DetallePedido(
                    pedidoId = pedidoId.toInt(),
                    servicioId = s.id,
                    cantidad = cantidad,
                    precioUnitario = s.precio,
                    subtotal = s.precio * cantidad
                )
            }
        )
        pedidoId
    }

    override suspend fun contarActivos(usuarioId: Int): Int = pedidoDao.contarActivos(usuarioId)

    override fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>> =
        pedidoDao.observarPorUsuario(usuarioId)

    override fun observarPedidos(): Flow<List<PedidoConDetalle>> = pedidoDao.observarTodos()

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> = pedidoDao.observarUno(id)

    /** Solo guarda: las reglas de la maquina de estados viven en el ViewModel. */
    override suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?) =
        pedidoDao.actualizarEstado(id, estado.name, casillero)

    override suspend fun marcarPagado(id: Int, pagado: Boolean) = pedidoDao.actualizarPagado(id, pagado)

    override fun observarCasillerosOcupados(): Flow<List<String>> = pedidoDao.observarCasillerosOcupados()

    // ---------- Panel ----------
    override fun observarPedidosDeFecha(fecha: String): Flow<Int> = pedidoDao.contarPorFecha(fecha)

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> = pedidoDao.contarPorEstado(estado.name)

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> = pedidoDao.ingresosDesde(fechaInicio)

    override fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>> =
        detalleDao.observarIngresosPorServicio(fechaInicio)

    // ---------- Mejora IA ----------
    override fun observarPedidosAtrasados(hoy: String): Flow<List<Int>> = pedidoDao.observarAtrasados(hoy)
}
