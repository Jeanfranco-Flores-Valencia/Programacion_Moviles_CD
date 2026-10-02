package com.floresvalencia.lab06tecsupstore

import java.util.Locale

fun formatoSoles(monto: Double): String = "S/ %.2f".format(Locale.US, monto)