# PROMPTS.md — Fase 2 (rama `mejora-ia`)

Mejora obligatoria: **calendario dinámico en la Pantalla 6 (Fecha y hora)** con `java.time.LocalDate`.
Asistente usado: Claude (Anthropic).

---

## Prompt 1 — Generar los días hábiles

**Prompt:**
> Tengo una app en Jetpack Compose (minSdk 26). En la pantalla de fecha y hora los días están en una lista fija.
> Necesito una función en Kotlin que, usando `java.time.LocalDate`, devuelva los próximos 5 días hábiles a partir de hoy
> (sin sábados ni domingos, sin días pasados) y funciones para mostrar el nombre corto del día ("Lun", "Mar"...)
> y el título del mes y año ("Octubre 2026") en español.

**Respuesta resumida:**
Propuso un `object FechaUtils` con `diasHabiles(desde, cantidad)` usando `generateSequence(desde) { it.plusDays(1) }`
filtrando `DayOfWeek.SATURDAY` y `SUNDAY`, y nombres de día/mes con
`getDisplayName(TextStyle.SHORT, Locale("es", "PE"))`.

**Qué corregí:**
- `getDisplayName` con `Locale("es")` devuelve los nombres en minúscula y con punto en algunos equipos ("lun.") y
  escribe **"septiembre"**; en Perú se usa **"setiembre"**. Lo reemplacé por listas propias de días y meses.
- El constructor `Locale(String, String)` está deprecado en las versiones nuevas de Java; al usar listas propias ya no se necesita.
- Agregué `tituloMes()` para cuando la semana cruza de mes ("Octubre – Noviembre 2026"), caso que la IA no consideró.
