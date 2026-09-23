# PROMPTS.md — Fase 2 (rama mejora-ia-clinica)

**Proyecto:** Clínica Salud+
**Asistente de IA usado:** Claude (Anthropic)
**Mejora implementada:** Cancelar una cita desde "Mis citas" con un AlertDialog de confirmación, estado "Cancelada" y opción de deshacer.

---

## Prompt 1 — Estado "Cancelada"

**Qué le pedí:**
> Quiero agregar la opción de cancelar citas en mi app de Jetpack Compose (sin ViewModel). Primero agrega un estado CANCELADA al enum EstadoCita y actualiza el badge de estado para que se vea en rojo.

**Qué me dio:** el nuevo valor del enum, dos colores rojos y la función EstadoBadge con un `when` para los tres estados.

---

## Prompt 2 — Botón Cancelar con AlertDialog

**Qué le pedí:**
> En la pantalla Mis citas agrega un botón "Cancelar" solo en las citas confirmadas. Al tocarlo debe aparecer un AlertDialog de confirmación con el médico, la fecha y la hora. Si confirma, la cita pasa a estado Cancelada.

**Qué me dio:** una variable de estado `citaACancelar` para controlar el diálogo, el AlertDialog con dos botones, el callback `onCancelarCita` y el reemplazo en la lista con `copy()`.

---

## Prompt 3 — Snackbar con Deshacer

**Qué le pedí:**
> Después de cancelar, muestra un Snackbar con el mensaje de la cita cancelada y un botón "Deshacer" que la regrese a Confirmada.

**Qué me dio:** SnackbarHostState en el Scaffold y `showSnackbar` dentro de `scope.launch`, revisando si el resultado es `ActionPerformed`.

---
- **Qué revisé antes de aceptar el código:** Verifique que los cambios fueran aplicados y no hubieran errores de compilacion a la hora de ejecutar la app antes de cada commit.