package com.floresvalencia.creditos_console

/**
 * Aplicación de consola para calcular el total a pagar por créditos de un estudiante,
 * determinar su carga académica y su forma de pago.
 * Desarrollada solo con condicionales (if/when) y repeticiones (while/for).
 * No utiliza clases ni objetos; solo variables, arreglos y funciones simples de apoyo
 * para dar formato de tabla a la salida (sin ningún concepto de POO).
 */

import java.util.Locale

// ----- Funciones de apoyo solo para formatear texto (no son clases ni objetos) -----

const val ANCHO_CAJA = 56

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

fun cajaSuperiorDoble(prefijo: String, ancho: Int): String {
    return prefijo + "╔" + "═".repeat(ancho) + "╗"
}

fun cajaInferiorDoble(prefijo: String, ancho: Int): String {
    return prefijo + "╚" + "═".repeat(ancho) + "╝"
}

fun filaDoble(prefijo: String, texto: String, ancho: Int): String {
    return prefijo + "║" + centrar(texto, ancho) + "║"
}

fun cajaSuperiorSimple(prefijo: String, ancho: Int): String {
    return prefijo + "┌" + "─".repeat(ancho) + "┐"
}

fun cajaInferiorSimple(prefijo: String, ancho: Int): String {
    return prefijo + "└" + "─".repeat(ancho) + "┘"
}

fun filaSimple(prefijo: String, texto: String, ancho: Int): String {
    return prefijo + "│" + centrar(texto, ancho) + "│"
}

fun main() {
    println(cajaSuperiorDoble("", ANCHO_CAJA))
    println(filaDoble("", "CALCULO DE PAGO POR CREDITOS", ANCHO_CAJA))
    println(cajaInferiorDoble("", ANCHO_CAJA))
    println()

    // ----- Nombre del estudiante -----
    print("Nombre del estudiante: ")
    var nombre = readLine()
    while (nombre == null || nombre.trim().isEmpty()) {
        print("El nombre no puede estar vacío. Ingrese el nombre: ")
        nombre = readLine()
    }
    nombre = nombre.trim()

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
            println("-> Debe ingresar un número entero mayor a 0.")
        }
    }

    // ----- Valor de cada crédito -----
    var valorCredito = 0.0
    var valorValido = false
    while (!valorValido) {
        print("Valor de cada crédito (S/.): ")
        val entrada = readLine()
        val numero = entrada?.toDoubleOrNull()
        if (numero != null && numero > 0) {
            valorCredito = numero
            valorValido = true
        } else {
            println("-> Debe ingresar un valor numérico mayor a 0.")
        }
    }

    // ----- Arreglos para guardar los datos de cada curso -----
    val nombresCursos = arrayOfNulls<String>(cantidadCursos)
    val creditosCursos = IntArray(cantidadCursos)

    // ----- Captura de datos de cada curso -----
    var i = 0
    while (i < cantidadCursos) {
        println()
        println("--- Curso ${i + 1} ---")

        print("Nombre del curso ${i + 1}: ")
        var nombreCurso = readLine()
        while (nombreCurso == null || nombreCurso.trim().isEmpty()) {
            print("El nombre del curso no puede estar vacío. Ingrese nuevamente: ")
            nombreCurso = readLine()
        }
        nombresCursos[i] = nombreCurso.trim()

        var creditosValidos = false
        while (!creditosValidos) {
            print("Créditos del curso ${i + 1}: ")
            val entradaCreditos = readLine()
            val numeroCreditos = entradaCreditos?.toIntOrNull()
            if (numeroCreditos != null && numeroCreditos > 0) {
                creditosCursos[i] = numeroCreditos
                creditosValidos = true
            } else {
                println("-> Debe ingresar un número entero de créditos mayor a 0.")
            }
        }

        i++
    }

    // ----- Calcular el total de créditos -----
    var totalCreditos = 0
    i = 0
    while (i < cantidadCursos) {
        totalCreditos += creditosCursos[i]
        i++
    }

    // =========================================================
    //                 SALIDA FINAL EN FORMATO TABLA
    // =========================================================

    println()
    println(cajaSuperiorDoble("", ANCHO_CAJA))
    println(filaDoble("", "RESUMEN DE MATRÍCULA", ANCHO_CAJA))
    println(cajaInferiorDoble("", ANCHO_CAJA))
    println()
    println("  Estudiante        : $nombre")
    println("  Cantidad de Cursos: $cantidadCursos")
    println("  Valor por Crédito : ${formatoMoneda(valorCredito)}")
    println()

    // ----- Tabla de cursos -----
    println("  ┌──────┬────────────────────────────────┬───────────┐")
    println("  │  N°  │ Curso                          │ Créditos  │")
    println("  ├──────┼────────────────────────────────┼───────────┤")

    i = 0
    while (i < cantidadCursos) {
        val numeroCol = numeroAncho((i + 1).toString(), 4)
        val nombreCol = textoAncho(nombresCursos[i] ?: "", 30)
        val creditosCol = numeroAncho(creditosCursos[i].toString(), 9)
        println("  │ $numeroCol │ $nombreCol │ $creditosCol │")
        i++
    }

    println("  └──────┴────────────────────────────────┴───────────┘")
    println()
    println("  Total de créditos matriculados: $totalCreditos")

    // ----- Determinar carga académica según el total de créditos -----
    var cargaAcademica: String
    if (totalCreditos <= 12) {
        cargaAcademica = "Malla Regular"
    } else if (totalCreditos in 13..18) {
        cargaAcademica = "Carga Completa"
    } else {
        cargaAcademica = "Requiere autorización"
    }

    println("  Carga académica                : $cargaAcademica")
    println()

    // ----- Validación de interrupción cuando supera los 18 créditos -----
    if (totalCreditos > 18) {
        println(cajaSuperiorDoble("  ", ANCHO_CAJA))
        println(filaDoble("  ", "PROCESO INTERRUMPIDO", ANCHO_CAJA))
        println(cajaInferiorDoble("  ", ANCHO_CAJA))
        println()
        println("  El estudiante \"$nombre\" supera los 18 créditos permitidos")
        println("  ($totalCreditos créditos matriculados).")
        println()
        println("  Esta matrícula requiere autorización y debe ser gestionada")
        println("  únicamente por personal administrativo.")
        println("  Acérquese a la oficina de coordinación académica para continuar.")
        println()
        return
    }

    // ----- Cálculo del total a pagar -----
    val totalPagar = totalCreditos * valorCredito

    println(cajaSuperiorSimple("  ", ANCHO_CAJA))
    println(filaSimple("  ", "TOTAL A PAGAR", ANCHO_CAJA))
    println(filaSimple("  ", formatoMoneda(totalPagar), ANCHO_CAJA))
    println(cajaInferiorSimple("  ", ANCHO_CAJA))
    println()

    // ----- Determinar forma de pago según el monto total -----
    var numeroCuotas: Int
    if (totalPagar > 2500.0) {
        numeroCuotas = 3
    } else {
        numeroCuotas = 2
    }

    val valorCuota = totalPagar / numeroCuotas

    println(cajaSuperiorDoble("  ", ANCHO_CAJA))
    println(filaDoble("  ", "FORMA DE PAGO", ANCHO_CAJA))
    println(cajaInferiorDoble("  ", ANCHO_CAJA))
    println()
    println("  Número de cuotas: $numeroCuotas")
    println()
    println("  ┌──────────┬─────────────────────────┐")
    println("  │  Cuota   │ Monto                   │")
    println("  ├──────────┼─────────────────────────┤")

    var cuota = 1
    while (cuota <= numeroCuotas) {
        val cuotaCol = numeroAncho(cuota.toString(), 8)
        val montoCol = textoAncho(formatoMoneda(valorCuota), 23)
        println("  │ $cuotaCol │ $montoCol │")
        cuota++
    }

    println("  └──────────┴─────────────────────────┘")
    println()

    // ----- Mensaje adicional según el monto total -----
    when {
        totalPagar >= 3000.0 -> println("  Nota: El monto es alto, considere revisar planes de pago.")
        totalPagar >= 1000.0 -> println("  Nota: Monto dentro del rango medio de pago.")
        else -> println("  Nota: Monto dentro del rango bajo de pago.")
    }
    println()
}