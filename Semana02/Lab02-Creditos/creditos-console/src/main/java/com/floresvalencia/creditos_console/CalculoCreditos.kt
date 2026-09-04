package com.floresvalencia.creditos_console

/**
 * Aplicacion de consola para calcular el total a pagar por creditos de un estudiante,
 * determinar su carga academica, turno y forma de pago.
 * Desarrollada UNICAMENTE con condicionales (if/when) y repeticiones (while/for).
 * No utiliza arreglos, listas, clases ni objetos.
 * Usa solo caracteres ASCII simples en la salida (sin tildes ni simbolos especiales).
 *
 * COMMIT 1: Se agrega el turno del estudiante (Manana, Tarde, Noche), que aplica
 * un incremento porcentual (10%, 15%, 20% respectivamente) sobre el subtotal de creditos.
 */

import java.util.Locale

const val ANCHO_CAJA = 56

// ----- Funciones de apoyo solo para formatear texto (no son clases ni objetos) -----

fun formatoMoneda(valor: Double): String {
    return String.format(Locale.US, "S/. %.2f", valor)
}

fun textoAncho(texto: String, ancho: Int): String {
    var resultado = texto
    if (resultado.length > ancho) {
        resultado = resultado.substring(0, ancho - 3) + "..."
    }
    return resultado.padEnd(ancho)
}

fun numeroAncho(numero: String, ancho: Int): String {
    return numero.padStart(ancho)
}

fun centrar(texto: String, ancho: Int): String {
    var t = texto
    if (t.length > ancho) {
        t = t.substring(0, ancho)
    }
    val espacios = ancho - t.length
    val izquierda = espacios / 2
    val derecha = espacios - izquierda
    return " ".repeat(izquierda) + t + " ".repeat(derecha)
}

fun cajaSuperior(prefijo: String, ancho: Int): String {
    return prefijo + "+" + "=".repeat(ancho) + "+"
}

fun cajaInferior(prefijo: String, ancho: Int): String {
    return prefijo + "+" + "=".repeat(ancho) + "+"
}

fun filaCaja(prefijo: String, texto: String, ancho: Int): String {
    return prefijo + "|" + centrar(texto, ancho) + "|"
}

fun main() {
    println(cajaSuperior("", ANCHO_CAJA))
    println(filaCaja("", "CALCULO DE PAGO POR CREDITOS", ANCHO_CAJA))
    println(cajaInferior("", ANCHO_CAJA))
    println()

    // ----- Nombre del estudiante -----
    print("Nombre del estudiante: ")
    var nombre = readLine()
    while (nombre == null || nombre.trim().isEmpty()) {
        print("El nombre no puede estar vacio. Ingrese el nombre: ")
        nombre = readLine()
    }
    nombre = nombre.trim()

    // =========================================================
    // COMMIT 1: Turno del estudiante (Manana, Tarde o Noche)
    // =========================================================
    var turno = ""
    var porcentajeTurno = 0.0
    var turnoValido = false
    while (!turnoValido) {
        print("Turno (1=Manana, 2=Tarde, 3=Noche): ")
        val entrada = readLine()
        if (entrada != null && entrada.trim() == "1") {
            turno = "Manana"
            porcentajeTurno = 0.10
            turnoValido = true
        } else if (entrada != null && entrada.trim() == "2") {
            turno = "Tarde"
            porcentajeTurno = 0.15
            turnoValido = true
        } else if (entrada != null && entrada.trim() == "3") {
            turno = "Noche"
            porcentajeTurno = 0.20
            turnoValido = true
        } else {
            println("-> Opcion invalida. Ingrese 1, 2 o 3.")
        }
    }

    // ----- Cantidad de cursos -----
    var cantidadCursos = 0
    var cantidadValida = false
    while (!cantidadValida) {
        print("Cantidad de cursos: ")
        val entrada = readLine()
        val numero = entrada?.toIntOrNull()
        if (numero != null && numero > 0) {
            cantidadCursos = numero
            cantidadValida = true
        } else {
            println("-> Debe ingresar un numero entero mayor a 0.")
        }
    }

    // ----- Valor de cada credito -----
    var valorCredito = 0.0
    var valorValido = false
    while (!valorValido) {
        print("Valor de cada credito (S/.): ")
        val entrada = readLine()
        val numero = entrada?.toDoubleOrNull()
        if (numero != null && numero > 0) {
            valorCredito = numero
            valorValido = true
        } else {
            println("-> Debe ingresar un valor numerico mayor a 0.")
        }
    }

    println()
    println(cajaSuperior("", ANCHO_CAJA))
    println(filaCaja("", "RESUMEN DE MATRICULA", ANCHO_CAJA))
    println(cajaInferior("", ANCHO_CAJA))
    println()
    println("  Estudiante        : $nombre")
    println("  Turno             : $turno")
    println("  Cantidad de Cursos: $cantidadCursos")
    println("  Valor por Credito : ${formatoMoneda(valorCredito)}")
    println()

    // ----- Un unico recorrido: lee cada curso y lo muestra de inmediato en la tabla -----
    var totalCreditos = 0

    println("  +------+--------------------------------+-----------+")
    println("  |  N.  | Curso                          | Creditos  |")
    println("  +------+--------------------------------+-----------+")

    var i = 1
    while (i <= cantidadCursos) {
        print("  Nombre del curso $i: ")
        var nombreCurso = readLine()
        while (nombreCurso == null || nombreCurso.trim().isEmpty()) {
            print("  El nombre del curso no puede estar vacio. Ingrese nuevamente: ")
            nombreCurso = readLine()
        }
        nombreCurso = nombreCurso.trim()

        var creditosCurso = 0
        var creditosValidos = false
        while (!creditosValidos) {
            print("  Creditos del curso $i: ")
            val entradaCreditos = readLine()
            val numeroCreditos = entradaCreditos?.toIntOrNull()
            if (numeroCreditos != null && numeroCreditos > 0) {
                creditosCurso = numeroCreditos
                creditosValidos = true
            } else {
                println("  -> Debe ingresar un numero entero de creditos mayor a 0.")
            }
        }

        totalCreditos += creditosCurso

        val numeroCol = numeroAncho(i.toString(), 4)
        val nombreCol = textoAncho(nombreCurso, 30)
        val creditosCol = numeroAncho(creditosCurso.toString(), 9)
        println("  | $numeroCol | $nombreCol | $creditosCol |")

        i++
    }

    println("  +------+--------------------------------+-----------+")
    println()
    println("  Total de creditos matriculados: $totalCreditos")

    // ----- Determinar carga academica segun el total de creditos -----
    var cargaAcademica: String
    if (totalCreditos <= 12) {
        cargaAcademica = "Malla Regular"
    } else if (totalCreditos in 13..18) {
        cargaAcademica = "Carga Completa"
    } else {
        cargaAcademica = "Requiere autorizacion"
    }

    println("  Carga academica                : $cargaAcademica")
    println()

    // ----- Validacion de interrupcion cuando supera los 18 creditos -----
    if (totalCreditos > 18) {
        println(cajaSuperior("  ", ANCHO_CAJA))
        println(filaCaja("  ", "PROCESO INTERRUMPIDO", ANCHO_CAJA))
        println(cajaInferior("  ", ANCHO_CAJA))
        println()
        println("  El estudiante \"$nombre\" supera los 18 creditos permitidos")
        println("  ($totalCreditos creditos matriculados).")
        println()
        println("  Esta matricula requiere autorizacion y debe ser gestionada")
        println("  unicamente por personal administrativo.")
        println("  Acerquese a la oficina de coordinacion academica para continuar.")
        println()
        return
    }

    // ----- Calculo del subtotal por creditos -----
    val subtotalCreditos = totalCreditos * valorCredito

    // =========================================================
    // COMMIT 1: Se aplica el incremento por turno sobre el subtotal
    // =========================================================
    val incrementoTurno = subtotalCreditos * porcentajeTurno
    val totalPagar = subtotalCreditos + incrementoTurno

    println(cajaSuperior("  ", ANCHO_CAJA))
    println(filaCaja("  ", "DETALLE DE PAGO", ANCHO_CAJA))
    println(cajaInferior("  ", ANCHO_CAJA))
    println()
    println("  Subtotal por creditos      : ${formatoMoneda(subtotalCreditos)}")
    println("  Incremento por turno ($turno): ${formatoMoneda(incrementoTurno)}")
    println()

    println(cajaSuperior("  ", ANCHO_CAJA))
    println(filaCaja("  ", "TOTAL A PAGAR", ANCHO_CAJA))
    println(filaCaja("  ", formatoMoneda(totalPagar), ANCHO_CAJA))
    println(cajaInferior("  ", ANCHO_CAJA))
    println()

    // ----- Determinar forma de pago segun el monto total -----
    var numeroCuotas: Int
    if (totalPagar > 2500.0) {
        numeroCuotas = 3
    } else {
        numeroCuotas = 2
    }

    val valorCuota = totalPagar / numeroCuotas

    println(cajaSuperior("  ", ANCHO_CAJA))
    println(filaCaja("  ", "FORMA DE PAGO", ANCHO_CAJA))
    println(cajaInferior("  ", ANCHO_CAJA))
    println()
    println("  Numero de cuotas: $numeroCuotas")
    println()
    println("  +----------+-------------------------+")
    println("  |  Cuota   | Monto                   |")
    println("  +----------+-------------------------+")

    var cuota = 1
    while (cuota <= numeroCuotas) {
        val cuotaCol = numeroAncho(cuota.toString(), 8)
        val montoCol = textoAncho(formatoMoneda(valorCuota), 23)
        println("  | $cuotaCol | $montoCol |")
        cuota++
    }

    println("  +----------+-------------------------+")
    println()

    // ----- Mensaje adicional segun el monto total -----
    when {
        totalPagar >= 3000.0 -> println("  Nota: El monto es alto, considere revisar planes de pago.")
        totalPagar >= 1000.0 -> println("  Nota: Monto dentro del rango medio de pago.")
        else -> println("  Nota: Monto dentro del rango bajo de pago.")
    }
    println()
}