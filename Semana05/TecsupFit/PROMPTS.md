# PROMPTS.md — Fase 2 (rama mejora-ia-fit)

**Proyecto:** TECSUP Fit
**Asistente de IA usado:** Claude (Anthropic)
**Mejora implementada:** Registrar la asistencia a una clase reservada, con AlertDialog de confirmación, actualización de estadísticas del perfil y Snackbar de felicitación.

---

## Prompt 1 — Racha como estado

**Qué le pedí:**
> En mi app de Jetpack Compose (sin ViewModel), la racha de asistencia es una constante. Conviértela en un estado compartido para que pueda aumentar y que el Perfil la muestre actualizada.

**Qué me dio:** la racha con `mutableIntStateOf` en TecsupFitApp y un nuevo parámetro `racha` en PerfilScreen.

**Commit:** `feat: racha de asistencia como estado compartido`

---

## Prompt 2 — Registrar asistencia con AlertDialog

**Qué le pedí:**
> En la pantalla Reservas agrega un botón "Registrar asistencia" solo en las reservas confirmadas. Al tocarlo debe aparecer un AlertDialog de confirmación. Si confirma, la reserva pasa a Completada y la racha aumenta.

**Qué me dio:** la variable `reservaPorConfirmar` para controlar el diálogo, el AlertDialog, el callback `onRegistrarAsistencia` y el reemplazo en la lista con `copy()`.

**Commit:** `feat: registrar asistencia a una clase con AlertDialog de confirmación`

---

## Prompt 3 — Snackbar de felicitación

**Qué le pedí:**
> Después de registrar la asistencia, muestra un Snackbar felicitando con la racha actual y un botón "Ver perfil" que lleve a la pestaña Perfil.

**Qué me dio:** SnackbarHostState en el Scaffold y `showSnackbar` dentro de `scope.launch`, navegando si el resultado es `ActionPerformed`.

**Commit:** `feat: Snackbar de felicitación al registrar asistencia`

---
