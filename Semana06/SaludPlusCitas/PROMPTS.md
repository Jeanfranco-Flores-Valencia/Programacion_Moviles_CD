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

---

## Prompt 2 — Calendario con flechas por semana

**Prompt:**
> Con `FechaUtils.diasHabiles`, modifica mi `FechaHoraScreen` para que las flechas < y > avancen o retrocedan una semana,
> que no se pueda retroceder antes de la semana actual, que el título del mes cambie según la semana mostrada,
> y que al cambiar de día se recalculen los horarios con `Repositorio.horariosDisponibles(medicoId, fecha)`
> y se reinicie la hora seleccionada. No cambies los parámetros de la función ni del Repositorio.

**Respuesta resumida:**
Usó un estado `semana` (Int) con `remember`, calculó los días con `hoy.plusWeeks(semana)` dentro de `remember(semana)`,
habilitó la flecha izquierda solo si `semana > 0` y guardó la fecha seleccionada como `LocalDate`.

**Qué corregí:**
- Guardaba la fecha seleccionada como `LocalDate` en `rememberSaveable`, lo que no se puede guardar en el `Bundle`
  y se pierde al girar la pantalla. La guardé como `String` en formato `yyyy-MM-dd` (el mismo formato de `Cita.fecha`),
  así `horariosDisponibles` sigue funcionando sin cambios y el bloqueo de horarios reservados se mantiene.
- Al cambiar de semana dejaba seleccionado un día que ya no se veía en pantalla. Ahora al cambiar de semana se limpian el día y la hora.
- Las citas de ejemplo del Repositorio tenían una fecha fija (octubre 2026) que ya no aparece en el calendario dinámico,
  por lo que no se podía comprobar el bloqueo. Cambié la cita de ejemplo al próximo día hábil desde mañana usando `LocalDate.now()`.

---

## Prompt 3 — Fecha en texto en la Pantalla 7

**Prompt:**
> En `ConfirmarCitaScreen` recibo la fecha como "2026-09-16". Quiero mostrarla como "Martes 16 de setiembre 2026".

**Respuesta resumida:**
Sugirió `DateTimeFormatter.ofPattern("EEEE d 'de' MMMM yyyy", Locale("es", "ES"))` y capitalizar la primera letra.

**Qué corregí:**
- Con el `Locale` de España el mes sale como "septiembre". Usé `FechaUtils.fechaLarga()` con mi lista de meses
  ("setiembre") y días con mayúscula inicial.
- No controlaba fechas mal formadas: si `LocalDate.parse` falla, ahora se muestra la fecha original en vez de cerrarse la app.
- Apliqué el mismo formato en Cita agendada y Detalle de cita para que la app sea consistente.

---

## Prompt 4 — Horarios de hoy que ya pasaron

**Prompt:**
> Si el día seleccionado es hoy, en el grid aparecen horas que ya pasaron (por ejemplo 08:00 cuando son las 15:00).
> ¿Cómo las oculto sin modificar `horariosDisponibles` del Repositorio?

**Respuesta resumida:**
Filtrar en la pantalla, después de llamar a `horariosDisponibles`, con `LocalTime.parse(hora).isAfter(LocalTime.now())`
solo cuando la fecha elegida es igual a `LocalDate.now()`.

**Qué corregí:**
- La IA comparaba `LocalDate.now()` directamente con el `String` de la fecha (siempre era falso). Lo cambié a `hoy.toString()`.
- Si ya no quedan horas en el día se muestra el mensaje "Sin horarios" en lugar de un grid vacío.
