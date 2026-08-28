package com.floresvalencia.creditos_console

/**
 * Aplicación de consola para calcular el total a pagar por créditos de un estudiante,
 * determinar su carga académica y su forma de pago.
 * Desarrollada solo con condicionales (if/when) y repeticiones (while/for).
 * No utiliza clases, objetos ni ningún concepto de POO adicional al main() obligatorio de Kotlin.
 */

fun main() {
    println("========================================")
    println("   CALCULO DE PAGO POR CREDITOS")
    println("========================================")
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

    // ----- Mostrar el resumen de cursos matriculados -----
    println()
    println("========================================")
    println("        RESUMEN DE MATRÍCULA")
    println("========================================")
    println("Nombre del estudiante: \"$nombre\"")
    println("Cantidad de Cursos: $cantidadCursos")
    println("Valor de cada crédito: S/.${valorCredito}")
    println()

    var totalCreditos = 0

    i = 0
    while (i < cantidadCursos) {
        println("Curso ${i + 1} : ${nombresCursos[i]}")
        println("Créditos: ${creditosCursos[i]}")
        println()

        totalCreditos += creditosCursos[i]
        i++
    }

    // ----- Determinar carga académica según el total de créditos -----
    println("========================================")
    println("Total de créditos matriculados: $totalCreditos")

    var cargaAcademica = ""

    if (totalCreditos <= 12) {
        cargaAcademica = "Malla Regular"
    } else if (totalCreditos in 13..18) {
        cargaAcademica = "Carga Completa"
    } else {
        cargaAcademica = "Requiere autorización"
    }

    println("Carga académica: $cargaAcademica")

    // ----- Validación de interrupción cuando supera los 18 créditos -----
    if (totalCreditos > 18) {
        println()
        println("========================================")
        println("PROCESO INTERRUMPIDO")
        println("========================================")
        println("El estudiante \"$nombre\" supera los 18 créditos permitidos ($totalCreditos créditos).")
        println("Esta matrícula requiere autorización y debe ser gestionada")
        println("únicamente por personal administrativo.")
        println("Acérquese a la oficina de coordinación académica para continuar.")
        return
    }

    // ----- Cálculo del total a pagar -----
    val totalPagar = totalCreditos * valorCredito

    println()
    println("========================================")
    println("TOTAL A PAGAR: S/. $totalPagar")
    println("========================================")

    // ----- Determinar forma de pago según el monto total -----
    var numeroCuotas = 0

    if (totalPagar > 2500.0) {
        numeroCuotas = 3
    } else {
        numeroCuotas = 2
    }

    val valorCuota = totalPagar / numeroCuotas

    println()
    println("========================================")
    println("           FORMA DE PAGO")
    println("========================================")
    println("Número de cuotas: $numeroCuotas")

    var cuota = 1
    while (cuota <= numeroCuotas) {
        println("Cuota $cuota: S/. $valorCuota")
        cuota++
    }

    println("========================================")

    // ----- Mensaje adicional según el monto total -----
    when {
        totalPagar >= 3000.0 -> println("Nota: El monto es alto, considere revisar planes de pago.")
        totalPagar >= 1000.0 -> println("Nota: Monto dentro del rango medio de pago.")
        else -> println("Nota: Monto dentro del rango bajo de pago.")
    }
}