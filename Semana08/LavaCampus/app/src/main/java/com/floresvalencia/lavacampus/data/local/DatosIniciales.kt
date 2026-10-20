package com.floresvalencia.lavacampus.data.local

import androidx.room.withTransaction
import com.floresvalencia.lavacampus.data.local.entity.DetallePedido
import com.floresvalencia.lavacampus.data.local.entity.Pedido
import com.floresvalencia.lavacampus.data.local.entity.Servicio
import com.floresvalencia.lavacampus.data.local.entity.Usuario
import com.floresvalencia.lavacampus.model.EstadoPedido
import com.floresvalencia.lavacampus.model.Rol
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * Datos de prueba (ficticios, Ley 29733: nombres, codigos y correos inventados).
 * Se cargan solo la primera vez. Las fechas se calculan desde hoy para que siempre
 * haya pedidos de hoy y de esta semana.
 */
object DatosIniciales {

    val servicios = listOf(
        Servicio(nombre = "Lavado y secado", descripcion = "Lavado, secado y doblado por kilo", unidad = "KG", precio = 8.00, horasEntrega = 24, activo = true, icono = "wash"),
        Servicio(nombre = "Planchado", descripcion = "Planchado de camisas, pantalones y polos", unidad = "PRENDA", precio = 2.50, horasEntrega = 48, activo = true, icono = "iron"),
        Servicio(nombre = "Lavado en seco", descripcion = "Sacos, abrigos y prendas delicadas", unidad = "PRENDA", precio = 12.00, horasEntrega = 48, activo = true, icono = "shirt"),
        Servicio(nombre = "Edredón / frazada", descripcion = "Edredones, frazadas y cobertores", unidad = "UNIDAD", precio = 20.00, horasEntrega = 48, activo = false, icono = "wash")
    )

    private val usuarios = listOf(
        Usuario(nombre = "Ana García", codigo = "U202310234", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
        Usuario(nombre = "Luis Ramos", codigo = "U202310877", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
        Usuario(nombre = "Marta Paredes", codigo = "U202410022", correo = "marta@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
        Usuario(nombre = "Administración", codigo = "", correo = "admin@lavacampus.test", clave = "admin", rol = Rol.ADMIN.name)
    )

    /** Pedido de ejemplo: indice del estudiante (0 Ana, 1 Luis, 2 Marta), dias atras y lineas (servicio, cantidad). */
    private class Semilla(
        val estudiante: Int,
        val diasAtras: Long,
        val estado: EstadoPedido,
        val lineas: List<Pair<String, Double>>,
        val pagado: Boolean = false,
        val casillero: String? = null,
        val nota: String = "",
        val estaSemana: Boolean = false
    )

    private val pedidos = listOf(
        // Entregados y pagados (ingresos). Los de "estaSemana" nunca caen antes del lunes.
        Semilla(0, 8, EstadoPedido.ENTREGADO, listOf("Lavado y secado" to 4.5), pagado = true),
        Semilla(1, 3, EstadoPedido.ENTREGADO, listOf("Lavado y secado" to 3.0, "Planchado" to 4.0), pagado = true, casillero = "C-02", estaSemana = true),
        Semilla(2, 2, EstadoPedido.ENTREGADO, listOf("Lavado en seco" to 2.0), pagado = true, casillero = "C-03", estaSemana = true),
        Semilla(1, 1, EstadoPedido.ENTREGADO, listOf("Lavado y secado" to 2.5), pagado = true, casillero = "C-10", estaSemana = true),
        Semilla(0, 0, EstadoPedido.ENTREGADO, listOf("Planchado" to 6.0), pagado = true, casillero = "C-11", estaSemana = true),
        // Listos con casillero
        Semilla(2, 2, EstadoPedido.LISTO, listOf("Lavado y secado" to 3.0), casillero = "C-05"),
        Semilla(0, 1, EstadoPedido.LISTO, listOf("Lavado y secado" to 2.0), casillero = "C-07"),
        // En proceso
        Semilla(1, 1, EstadoPedido.EN_PROCESO, listOf("Lavado y secado" to 2.0, "Planchado" to 6.0)),
        Semilla(0, 0, EstadoPedido.EN_PROCESO, listOf("Lavado y secado" to 3.5, "Planchado" to 5.0), nota = "Ropa de color separada"),
        // Recibidos de hoy
        Semilla(2, 0, EstadoPedido.RECIBIDO, listOf("Lavado y secado" to 1.5)),
        Semilla(1, 0, EstadoPedido.RECIBIDO, listOf("Lavado y secado" to 2.0))
    )

    suspend fun sembrar(db: LavaDatabase) {
        db.withTransaction {
            if (db.servicioDao().contar() == 0) {
                db.servicioDao().insertarTodos(servicios)
            }
            if (db.usuarioDao().contar() == 0) {
                sembrarUsuariosYPedidos(db)
            }
        }
    }

    private suspend fun sembrarUsuariosYPedidos(db: LavaDatabase) {
        val idsUsuarios = db.usuarioDao().insertarTodos(usuarios).map { it.toInt() }
        // Servicios por nombre (los ids los genero Room)
        val catalogo = (1..20).mapNotNull { db.servicioDao().obtener(it) }.associateBy { it.nombre }

        val hoy = LocalDate.now()
        val lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        pedidos.forEachIndexed { i, s ->
            var registro = hoy.minusDays(s.diasAtras)
            if (s.estaSemana && registro.isBefore(lunes)) registro = lunes

            val lineas = s.lineas.mapNotNull { (nombre, cantidad) ->
                catalogo[nombre]?.let { it to cantidad }
            }
            val horas = lineas.maxOfOrNull { it.first.horasEntrega } ?: 24
            var entrega = registro.plusDays(((horas + 23) / 24).toLong())
            if (entrega.dayOfWeek == DayOfWeek.SUNDAY) entrega = entrega.plusDays(1)

            // El total del pedido coincide con la suma de sus detalles
            val total = lineas.sumOf { (servicio, cantidad) -> servicio.precio * cantidad }
            val creacion = registro.atTime(8 + (i % 9), 15 + (i % 4) * 10)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val pedidoId = db.pedidoDao().insertar(
                Pedido(
                    usuarioId = idsUsuarios[s.estudiante],
                    fechaRegistro = registro.toString(),
                    fechaEntrega = entrega.toString(),
                    estado = s.estado.name,
                    total = total,
                    casillero = s.casillero,
                    pagado = s.pagado,
                    nota = s.nota,
                    fechaCreacion = creacion
                )
            ).toInt()

            db.detalleDao().insertarTodos(
                lineas.map { (servicio, cantidad) ->
                    DetallePedido(
                        pedidoId = pedidoId,
                        servicioId = servicio.id,
                        cantidad = cantidad,
                        precioUnitario = servicio.precio,
                        subtotal = servicio.precio * cantidad
                    )
                }
            )
        }
    }
}
