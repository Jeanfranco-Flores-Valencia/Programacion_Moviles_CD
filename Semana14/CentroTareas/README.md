# CentroTareas — Laboratorio 14: Hilos en Android

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · Tecsup
**Docente:** Juan León S.

## Observación del Paso 1

Al tocar **Tarea larga (MAL)** y enseguida **Tócame**, la pantalla se congela durante 4 segundos: el contador no suma, los botones no responden y nada se mueve. Al terminar, el contador recién suma los toques que quedaron en cola y el texto pasa directamente a **Terminó**.

**¿Por qué no se ve «Trabajando...»?** Porque `Thread.sleep(4000)` corre en el **hilo principal**, que es el mismo que dibuja la pantalla. El mensaje sí cambia a «Trabajando...», pero el hilo queda dormido y no puede volver a dibujar hasta que termina la espera. Cuando despierta, el mensaje ya es «Terminó», así que solo se ve el valor final.

**¿Qué pasaría con 10 segundos?** Android muestra el diálogo **«La aplicación no responde» (ANR)**, que ofrece cerrar la app o esperar, porque el hilo principal estuvo bloqueado más de unos 5 segundos sin atender los toques del usuario.
