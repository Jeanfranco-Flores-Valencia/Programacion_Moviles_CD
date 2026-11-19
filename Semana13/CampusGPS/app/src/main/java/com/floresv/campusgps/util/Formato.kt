package com.floresv.campusgps.util

import java.util.Locale

/** Menos de 1000: metros enteros ("350 m"). Desde 1000: kilometros con un decimal ("1.5 km"). */
fun formatearDistancia(metros: Float): String =
    if (metros < 1000f) "${metros.toInt()} m"
    else String.format(Locale.US, "%.1f km", metros / 1000f)

/** Mejora IA: minutos a pie, caminando unos 80 m por minuto (minimo 1). */
fun minutosCaminando(metros: Float): Int = (metros / 80f).toInt().coerceAtLeast(1)
