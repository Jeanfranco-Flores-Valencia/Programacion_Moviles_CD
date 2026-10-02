package com.floresvalencia.lab04carritotecsup

import java.util.Locale

fun formatoSoles(monto: Double): String = "S/ %.2f".format(Locale.US, monto)