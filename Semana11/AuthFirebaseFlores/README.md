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

## Archivos principales

```
app/src/main/java/com/tecsup/authfirebaseflores/
├── MainActivity.kt      punto de entrada: tema y AuthApp()
├── AuthApp.kt           NavHost con las rutas login, register y home
├── LoginScreen.kt       signInWithEmailAndPassword
├── RegisterScreen.kt    createUserWithEmailAndPassword
├── HomeScreen.kt        currentUser.email y signOut()
└── AuthComponentes.kt   CampoClave y mensajeDeError
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
| Login con campos vacíos | "Complete todos los campos" |
| Login con contraseña incorrecta | "Correo o contraseña incorrectos" |
| Registro con contraseñas distintas | "Las contraseñas no coinciden" |
| Registro con menos de 6 caracteres | "Mínimo 6 caracteres" |
| Registro con un correo ya usado | "Ese correo ya está registrado" |
| Registro correcto | "Usuario creado", entra a la bienvenida y el usuario aparece en Firebase |
| Cerrar y abrir la app con sesión | Abre directo la bienvenida |
| Cerrar sesión | Vuelve al login |
