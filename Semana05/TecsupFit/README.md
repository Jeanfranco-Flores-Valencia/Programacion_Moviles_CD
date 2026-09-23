# TECSUP Fit

App Android de reserva de clases de gimnasio desarrollada con **Kotlin** y **Jetpack Compose**, como tarea integradora de las semanas 1 a 6 del curso Programación en Móviles (TECSUP).

El estado se maneja con `remember` / `mutableStateOf` / `mutableStateListOf`, **sin ViewModel ni MVVM**.

## Ramas

| Rama | Contenido |
|---|---|
| `main` | Fase 1: app completa con los requerimientos base |
| `mejora-ia` | Fase 2: mejora funcional desarrollada con ayuda de IA |

## Tecnologías

- Kotlin + Jetpack Compose (Material 3)
- Navigation Compose 2.8.9
- Material Icons Extended

## Requerimientos y cómo se cumplen

| Requerimiento | Implementación |
|---|---|
| Scaffold con topBar y padding | Un solo `Scaffold`; su padding se aplica al `NavHost` |
| Inicio con LazyRow y LazyColumn | Chips "Hoy" / "Esta semana" (LazyRow) y lista de clases con nombre y horario (LazyColumn) |
| Detalle con parámetro | Ruta `detalle/{claseId}`; botón "Reservar cupo" |
| Selección única de horario/cupo | Turnos de la clase como opciones únicas; los turnos llenos se deshabilitan |
| Confirmación | Resumen con clase, horario y sala recibidos por parámetros; botón "Ver mis reservas" |
| bottomBar | 4 pestañas (Inicio, Reservas, Rutinas, Perfil) con la pestaña activa resaltada según la ruta actual |
| Reservas con estados | LazyColumn con estados Confirmada (verde, barra lateral) y Completada (gris) |
| Perfil | Datos del usuario y estadísticas de clases tomadas y racha de asistencia |

## Flujo de navegación

Inicio → Detalle de clase (elige horario) → Confirmación → Mis reservas

## Estructura

    data/            Modelos y datos de ejemplo
    navigation/      Rutas y TecsupFitApp (Scaffold + bottomBar + NavHost)
    ui/components/   Componentes reutilizables (chips, badges, íconos)
    ui/screens/      Pantallas de la app
    ui/theme/        Tema y colores

## Mejora con IA (rama `mejora-ia`)

**Registrar asistencia** a una clase reservada:
- Botón "Registrar asistencia" en las reservas confirmadas
- AlertDialog de confirmación con el detalle de la clase
- La reserva pasa a Completada y aumentan las clases tomadas y la racha del Perfil
- Snackbar de felicitación con acceso directo al Perfil

### Prompts usados

1. *"En mi app de Jetpack Compose (sin ViewModel), la racha de asistencia es una constante. Conviértela en un estado compartido para que pueda aumentar y que el Perfil la muestre actualizada."*
2. *"En la pantalla Reservas agrega un botón 'Registrar asistencia' solo en las reservas confirmadas. Al tocarlo debe aparecer un AlertDialog de confirmación. Si confirma, la reserva pasa a Completada y la racha aumenta."*
3. *"Después de registrar la asistencia, muestra un Snackbar felicitando con la racha actual y un botón 'Ver perfil' que lleve a la pestaña Perfil."*

## Autor

Flores Valencia Jeanfranco  — TECSUP