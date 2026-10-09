# Clínica SaludPlus — App Paciente

Tarea complementaria al Laboratorio 6 · Programación en Móviles (Tecsup).
App de agendamiento de citas médicas en **Jetpack Compose**, sin base de datos:
usuarios, especialidades, médicos y citas viven en colecciones dentro del `object Repositorio`.

## Cómo ejecutar
1. Android Studio → **File > Open** → carpeta `SaludPlusCitas`.
2. Esperar la sincronización de Gradle y ejecutar en un emulador o celular (Android 8.0+).
3. Cuenta de prueba: teléfono **987654321** · contraseña **123456** (o regístrate).

## Ramas
| Rama | Contenido |
|------|-----------|
| `main` | Fase 1 — desarrollo sin IA (todas las pantallas, NavigationBar, LazyRow/LazyColumn/LazyVerticalGrid, navegación con parámetros y `popUpTo`). |
| `mejora-ia` | Fase 2 — calendario dinámico con `java.time.LocalDate` (ver `PROMPTS.md`). |

## Estructura
```
com.saludplus.citas
├── MainActivity.kt
├── data
│   ├── model        Usuario, Especialidad, Medico, Cita (+ Resultado, reto extra)
│   └── repository   Repositorio.kt (object con colecciones)
├── navigation       Rutas.kt, AppNavigation.kt
└── ui
    ├── theme        Color.kt, Theme.kt, Type.kt
    ├── components   Componentes.kt, BarraNavegacion.kt, IconosEspecialidad.kt, Formatos.kt
    └── screens
        ├── auth            SplashScreen, RegistroScreen, LoginScreen, TerminosScreen
        ├── home            HomeScreen
        ├── agendamiento    EspecialidadesScreen, MedicosScreen, FechaHoraScreen,
        │                   ConfirmarCitaScreen, CitaExitosaScreen
        ├── citas           MisCitasScreen, DetalleCitaScreen
        ├── perfil          PerfilScreen
        ├── resultados      ResultadosScreen
        └── notificaciones  NotificacionesScreen
```

## Flujo principal
Splash → Registro / Login → Inicio → Especialidades → Médicos (`especialidadId`) →
Fecha y hora (`medicoId`) → Confirmar cita (`medicoId`, `fecha`, `hora`) →
Cita agendada (`citaId`, con `popUpTo(Rutas.HOME)`) → Mis citas.

También: Perfil → Splash (cerrar sesión), campana de Inicio → Notificaciones,
enlace del Registro → Términos, Mis citas → Detalle de cita (cancelar con `AlertDialog`).
