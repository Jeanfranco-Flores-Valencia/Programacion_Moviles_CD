# AuthFirebaseFlores — Laboratorio 11: Firebase Authentication

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · Tecsup
**Docente:** Juan José León Suiyon

## Descripción

App en Kotlin + Jetpack Compose con inicio de sesión y registro usando **Firebase Authentication (correo y contraseña)**.

| Pantalla | Qué hace |
|---|---|
| Login | Correo y contraseña. Valida campos vacíos y muestra el error si las credenciales fallan. Enlace a Registro. |
| Registro | Correo, contraseña y confirmación. Valida campos vacíos, que coincidan y mínimo 6 caracteres. Al crear la cuenta entra a la bienvenida. |
| Bienvenida | Muestra el correo de la sesión y el botón Cerrar sesión. |

- Si ya hay una sesión guardada, la app abre directo la bienvenida.
- Después de entrar, el botón atrás no vuelve al login.
- La contraseña tiene un botón para ver u ocultar lo escrito.
- Los errores de Firebase se muestran en español (credenciales incorrectas, correo ya registrado, sin internet).

## Mejora con IA (rama `mejora-ia-authfirebase`)

Rediseño de la interfaz con ambiente de **biblioteca** y los **colores de Tecsup** (azul marino y celeste). Detalle de los prompts en `PROMPTS.md`.

- **Fondo animado:** estantes con libros dibujados con `Canvas`, luz celeste que "respira" e íconos de estudio flotando.
- **Una sola tarjeta** con pestañas Iniciar sesión / Crear cuenta, subrayado que se desliza y formularios que cambian con animación.
- **Campos con íconos**, ver u ocultar la contraseña y teclado con Siguiente / Listo.
- **Errores dentro de la tarjeta** (la tarjeta se sacude) en lugar de Toast.
- **Indicador de seguridad** de la contraseña y check cuando la confirmación coincide.
- **¿Olvidaste tu contraseña?:** Firebase envía un enlace al correo.
- **Bienvenida:** saludo según la hora, avatar con la inicial, consejo de estudio del día, espacio "Mis cursos" y confirmación al cerrar sesión.
- **Transiciones** entre pantallas y tarjetas que aparecen en cascada.

## Archivos principales

```
app/src/main/java/com/tecsup/authfirebaseflores/
├── MainActivity.kt      punto de entrada: tema y AuthApp()
├── AuthApp.kt           NavHost con las rutas login y home (con transiciones)
├── AuthScreen.kt        tarjeta con pestañas Iniciar sesión / Crear cuenta
├── LoginScreen.kt       signInWithEmailAndPassword y sendPasswordResetEmail
├── RegisterScreen.kt    createUserWithEmailAndPassword
├── HomeScreen.kt        currentUser.email y signOut()
├── AuthComponentes.kt   campos, botón, avisos, indicador de clave y mensajeDeError
├── Theme.kt             Paleta con los colores de Tecsup
└── FondoEducativo.kt    fondo de biblioteca animado
```

- `AndroidManifest.xml`: declara la app, la actividad principal y el permiso de internet.
- `build.gradle.kts` (proyecto): declara el plugin `com.google.gms.google-services` con versión y `apply false`.
- `app/build.gradle.kts`: aplica el plugin y agrega el BOM de Firebase, `firebase-auth` y Navigation Compose.
- `app/google-services.json`: conecta la app con el proyecto de Firebase. **No se sube al repositorio** (está en `.gitignore`).

## Configurar Firebase

1. En https://console.firebase.google.com/ crear un proyecto (se puede desactivar Google Analytics).
2. Agregar una app Android con el paquete exacto **`com.tecsup.authfirebaseflores`**.
3. Descargar `google-services.json` y copiarlo en **`app/`** (vista Project).
4. En **Authentication → Método de acceso**, activar **Correo electrónico/contraseña**.

## Cómo ejecutar

1. Abrir la carpeta `Semana11/AuthFirebaseFlores` en Android Studio y sincronizar Gradle.
2. Ejecutar en un emulador con API 26 o superior (con Google Play de preferencia).
3. Registrar un usuario y verificar que aparece en **Authentication → Usuarios**.

## Pruebas

| Prueba | Resultado esperado |
|---|---|
| Login con campos vacíos | "Complete todos los campos" y la tarjeta se sacude |
| Login con contraseña incorrecta | "Correo o contraseña incorrectos" |
| Registro con contraseñas distintas | "Las contraseñas no coinciden" |
| Registro con menos de 6 caracteres | "Mínimo 6 caracteres" |
| Registro con un correo ya usado | "Ese correo ya está registrado" |
| Registro correcto | Entra a la bienvenida y el usuario aparece en Firebase |
| Cerrar y abrir la app con sesión | Abre directo la bienvenida |
| Cerrar sesión | Pide confirmación y vuelve al login |
| ¿Olvidaste tu contraseña? sin correo | Pide escribir el correo |
| ¿Olvidaste tu contraseña? con correo | Aviso verde y llega el enlace al correo |
