package com.floresvalencia.creditos_console

/**
 * Aplicación de consola para calcular el total a pagar por créditos de un estudiante.
 * Desarrollada solo con condicionales (if/when) y repeticiones (while/for).
 * No utiliza clases, objetos ni ningún concepto de POO adicional al main() obligatorio de Kotlin.
 *
 * Cómo ejecutarla en Android Studio:
 * 1. Crea un proyecto de tipo "Kotlin" (No Activity / Console App), o simplemente
 *    un archivo .kt suelto dentro de un proyecto Kotlin/JVM.
 * 2. Pega este código en un archivo llamado CalculoCreditos.kt
 * 3. Haz clic derecho sobre el archivo -> Run 'CalculoCreditosKt'
 *    (o usa el botón de play que aparece junto a fun main())
 * 4. La ejecución se realizará en la pestaña "Run" (consola) de Android Studio.
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

    // ----- Mostrar el resumen con la estructura solicitada -----
    println()
    println("========================================")
    println("        RESUMEN DE MATRÍCULA")
    println("========================================")
    println("Nombre del estudiante: \"$nombre\"")
    println("Cantidad de Cursos: $cantidadCursos")
    println("Valor de cada crédito: S/.${valorCredito}")
    println()

    var totalCreditos = 0
    var totalPagar = 0.0

    i = 0
    while (i < cantidadCursos) {
        println("Curso ${i + 1} : ${nombresCursos[i]}")
        println("Créditos: ${creditosCursos[i]}")
        println()

        totalCreditos += creditosCursos[i]
        i++
    }

    totalPagar = totalCreditos * valorCredito

    // ----- Totales finales -----
    println("========================================")
    println("Total de créditos matriculados: $totalCreditos")
    println("TOTAL A PAGAR: S/. $totalPagar")
    println("========================================")

    // ----- Ejemplo de condicional adicional: mensaje según el monto -----
    when {
        totalPagar >= 3000.0 -> println("Nota: El monto es alto, considere revisar planes de pago.")
        totalPagar >= 1000.0 -> println("Nota: Monto dentro del rango medio de pago.")
        else -> println("Nota: Monto dentro del rango bajo de pago.")
    }
}