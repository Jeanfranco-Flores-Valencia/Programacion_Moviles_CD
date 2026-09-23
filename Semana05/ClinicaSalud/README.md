# Clínica Salud+

App Android de reserva de citas médicas desarrollada con **Kotlin** y **Jetpack Compose**, como tarea integradora de las semanas 1 a 6 del curso Programación en Móviles (TECSUP).

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
| Scaffold en todas las pantallas | Un solo `Scaffold` con topBar; su padding se aplica al `NavHost` |
| Inicio con LazyRow y LazyColumn | Chips de especialidad (LazyRow) y lista de 5 médicos con nombre, especialidad y calificación (LazyColumn) |
| Perfil del médico con parámetro | Ruta `perfil_medico/{medicoId}`; botón "Agendar cita" |
| Agendar cita con selección única | Fecha (4 opciones) y hora (5 opciones); cada grupo guarda una sola selección |
| Confirmación | Resumen con médico, fecha y hora recibidos por parámetros; botones "Ver mis citas" y "Volver al inicio" |
| Menú lateral (drawer) | `ModalNavigationDrawer` con ícono ☰ y 4 destinos: Inicio, Mis citas, Historial médico y Perfil |
| Mis citas con estados | LazyColumn con estados Confirmada (verde, barra morada) y Completada (gris) |

## Flujo de navegación

Inicio → Perfil del médico → Agendar cita → Confirmación → Mis citas

## Estructura

    data/            Modelos y datos de ejemplo
    navigation/      Rutas y ClinicaApp (drawer + Scaffold + NavHost)
    ui/components/   Componentes reutilizables (chips, badges, avatares)
    ui/screens/      Pantallas de la app
    ui/theme/        Tema y colores

## Mejora con IA (rama `mejora-ia-clinica`)

**Cancelar una cita** desde Mis citas:
- Botón "Cancelar" solo en citas confirmadas
- AlertDialog de confirmación con el detalle de la cita
- Nuevo estado "Cancelada" (badge rojo y fecha tachada)
- Snackbar con opción "Deshacer"

### Prompts usados

1. *"Quiero agregar la opción de cancelar citas en mi app de Jetpack Compose (sin ViewModel). Primero agrega un estado CANCELADA al enum EstadoCita y actualiza el badge de estado para que se vea en rojo."*
2. *"En la pantalla Mis citas agrega un botón 'Cancelar' solo en las citas confirmadas. Al tocarlo debe aparecer un AlertDialog de confirmación con el médico, la fecha y la hora. Si confirma, la cita pasa a estado Cancelada."*
3. *"Después de cancelar, muestra un Snackbar con el mensaje de la cita cancelada y un botón 'Deshacer' que la regrese a Confirmada."*


## Autor

Flores Valencia Jeanfranco — TECSUP