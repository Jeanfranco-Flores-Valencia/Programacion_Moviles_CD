#  NavLab — Portal Académico

Aplicación Android desarrollada con **Kotlin**, **Jetpack Compose**, **Material 3** y **Navigation Compose**. El proyecto partió de un laboratorio básico de navegación (Home, Lista, Detalle y Perfil) y fue reestructurado con ayuda de un agente de IA hasta convertirse en un portal académico con inicio de sesión por usuario, directorio de alumnos, expediente académico y perfil personal.

**Desarrollado por:** Jeanfranco Flores Valencia — Tecsup

---

##  Pantallas

| Pantalla | Descripción |
|---|---|
| **Login** | Tarjeta centrada con correo institucional y contraseña (con opción de mostrar u ocultar). Valida formato y credenciales de cada alumno. |
| **Home** | Degradado morado con saludo personalizado al alumno autenticado y accesos a *Directorio de Alumnos* y *Mi Perfil Académico*. Incluye *Cerrar Sesión Segura*. |
| **Directorio de Alumnos** | Lista de alumnos con avatar, nombre, carrera y acceso al detalle. |
| **Expediente Académico** | Encabezado con degradado, avatar superpuesto, ID de estudiante, correo, facultad y biografía. Recibe el `studentId` como argumento tipado. |
| **Configuración de Perfil** | Información personal y académica del alumno autenticado, con botón para cerrar sesión. |
---

##  Credenciales de prueba

| Alumno | Correo | Contraseña |
|---|---|---|
| Juan León | juan.leon@tecsup.edu.pe | Juan2024* |
| María García | maria.garcia@tecsup.edu.pe | Maria2024* |
| Carlos Perez | carlos.perez@tecsup.edu.pe | Carlos2024* |
| Ana Lopez | ana.lopez@tecsup.edu.pe | Ana2024* |
| Luis Ramirez | luis.ramirez@tecsup.edu.pe | Luis2024* |

> ⚠️ Las contraseñas se guardan en texto plano y la sesión vive en memoria **solo con fines académicos**. En un entorno real se usaría autenticación con backend y contraseñas cifradas (hash).

---

##  Flujo de navegación

```
Login ──(credenciales válidas)──► Home
                                   ├──► Directorio de Alumnos ──► Expediente Académico (detail/{studentId})
                                   └──► Configuración de Perfil
Home / Perfil ──(Cerrar sesión)──► Login  (se limpia el back stack)
```

- Las rutas se definen en una `sealed class Screen`, que funciona como contrato central de navegación.
- La ruta de detalle usa un argumento tipado `Int` (`detail/{studentId}`), construido con `Screen.Detail.createRoute(id)`.
- Al iniciar sesión se elimina Login del back stack; al cerrar sesión se limpia todo el historial.
- Las pantallas protegidas redirigen a Login si no hay una sesión activa.

---

##  Estructura del proyecto

```
com.floresvalencia.navlab
├── MainActivity.kt
├── data/
│   ├── Student.kt
│   ├── StudentRepository.kt
│   └── SessionManager.kt
├── navigation/
│   ├── Screen.kt
│   ├── AppNavigation.kt
│   └── NavExtensions.kt
├── screens/
│   ├── LoginScreen.kt
│   ├── HomeScreen.kt
│   ├── ListScreen.kt
│   ├── DetailScreen.kt
│   └── ProfileScreen.kt
├── validation/
│   └── LoginValidator.kt
└── ui/
    ├── components/
    │   ├── StudentAvatar.kt
    │   ├── MenuCard.kt
    │   ├── InfoRow.kt
    │   ├── GradientHeader.kt
    │   └── SectionTitle.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

---

##  Tecnologías

- Kotlin
- Jetpack Compose + Material 3
- Navigation Compose (rutas con argumentos tipados)
- Material Icons Extended
- Estado de sesión con `mutableStateOf` (en memoria)

---

## Cómo ejecutar

1. Clona el repositorio.
2. Ábrelo en Android Studio.
3. Sincroniza Gradle.
4. Ejecuta la app en un emulador o dispositivo físico.
5. Inicia sesión con cualquiera de las credenciales de prueba.

---

##  Desarrollo asistido por IA

La reestructuración del proyecto se realizó con un agente de IA trabajando directamente en el IDE. A continuación se incluye el prompt utilizado y el reporte entregado por el agente al finalizar.

###  Prompt utilizado

<details>
<summary>Ver prompt completo</summary>

```text
# ROL
Actúa como desarrollador Android senior experto en Kotlin, Jetpack Compose, Material 3 y Navigation Compose. Trabajarás directamente sobre el proyecto abierto en el IDE, editando y creando archivos en su lugar.

# OBJETIVO
Reestructurar el proyecto "NavLab" (paquete base: com.floresvalencia.navlab) para convertirlo en un "Portal Académico" de 5 pantallas: Login, Home, Directorio de Alumnos, Expediente Académico y Configuración de Perfil. Debe replicar fielmente el diseño descrito en este documento (paleta morada/lavanda, degradados, tarjetas redondeadas), manteniendo la arquitectura de navegación basada en sealed class con argumentos tipados.

Cada alumno tiene sus propias credenciales (correo institucional y contraseña). El login valida contra esos datos y, tras iniciar sesión, Home y Perfil muestran la información del alumno autenticado.

No tienes acceso a las imágenes de referencia. Las especificaciones de la sección 5 las describen por completo; síguelas al pie de la letra.

---

# 1. EXPLORA EL PROYECTO ANTES DE EDITAR
- Revisa la estructura de carpetas del proyecto.
- Lee los archivos existentes:
  - navigation/Screen.kt → sealed class Screen(val route: String) con Home, List, Profile y Detail("detail/{itemId}") con createRoute(itemId: Int).
  - navigation/AppNavigation.kt → NavHost con rememberNavController.
  - screens/HomeScreen.kt, ListScreen.kt, DetailScreen.kt, ProfileScreen.kt.
  - MainActivity.kt → solo llama a AppNavigation() dentro de setContent, con enableEdgeToEdge().
  - ui/theme/Color.kt, Theme.kt, Type.kt.
- Revisa app/build.gradle.kts y gradle/libs.versions.toml (si existe) para conocer las versiones de Compose, Material 3 y Navigation. Si el proyecto usa version catalog, úsalo.
- No borres archivos que no se mencionan en estas instrucciones.

---

# 2. RESTRICCIONES
- NO modifiques MainActivity.kt: debe quedar exactamente como está, solo llamando a AppNavigation().
- NO actualices versiones de AGP, Kotlin ni de dependencias existentes.
- NO generes, descargues ni referencies imágenes que no existan en el proyecto.
- NO agregues bases de datos, Retrofit, Firebase ni DataStore: los datos y la sesión se manejan en memoria (proyecto académico).
- La contraseña nunca debe mostrarse en ninguna pantalla ni escribirse en Log.
- Todos los textos visibles deben estar en español.
- Mantén comentarios explicativos breves en español, en el mismo estilo que el código actual.

---

# 3. TEMA Y ESTILO GENERAL
- Color primario: morado (#6750A4 aprox.).
- Fondos: lavanda muy claro (#F5F0FF aprox.).
- Acento malva para degradados (#7D5260 aprox.).
- Rojo para cerrar sesión (#B3261E aprox.) y rojo muy claro para fondos de botón (#FCEEEE aprox.).
- Define todos los colores nuevos en ui/theme/Color.kt y úsalos desde ahí; no repitas valores hex en las pantallas.
- En ui/theme/Theme.kt: asegúrate de que NavLabTheme acepte el parámetro dynamicColor y que el lightColorScheme use estos colores.
- El tema se aplica DENTRO de AppNavigation.kt, envolviendo el NavHost:
    NavLabTheme(dynamicColor = false) {
        NavHost(...) { ... }
    }
  (dynamicColor = false es obligatorio: en Android 12+ el color dinámico reemplazaría la paleta morada por los colores del fondo de pantalla).
- Estilo visual: tarjetas con esquinas de 16–20dp, fondo blanco o gris lavanda, sombra suave; degradados en los encabezados.

---

# 4. ESTRUCTURA FINAL ESPERADA
com.floresvalencia.navlab
├── MainActivity.kt                (sin cambios)
├── data/
│   ├── Student.kt
│   ├── StudentRepository.kt
│   └── SessionManager.kt
├── navigation/
│   ├── Screen.kt
│   ├── AppNavigation.kt
│   └── NavExtensions.kt
├── screens/
│   ├── LoginScreen.kt
│   ├── HomeScreen.kt
│   ├── ListScreen.kt
│   ├── DetailScreen.kt
│   └── ProfileScreen.kt
├── validation/
│   └── LoginValidator.kt
└── ui/
    ├── components/
    │   ├── StudentAvatar.kt
    │   ├── MenuCard.kt
    │   ├── InfoRow.kt
    │   ├── GradientHeader.kt
    │   └── SectionTitle.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt

---

# 5. ESPECIFICACIÓN DE PANTALLAS

## 5.1 LoginScreen (NUEVA, startDestination)
### Diseño
- Fondo lavanda claro a pantalla completa.
- Tarjeta centrada con sombra y esquinas redondeadas que contiene:
  - Título "Portal Académico" (morado, negrita, grande).
  - Subtítulo "Accede a tu cuenta".
  - OutlinedTextField "Correo Institucional" con ícono Email al inicio.
  - OutlinedTextField "Contraseña" con ícono Lock al inicio y botón Visibility / VisibilityOff al final para mostrar u ocultar (PasswordVisualTransformation).
  - Botón de ancho completo "INICIAR SESIÓN", morado.
  - Texto clicable "¿Olvidaste tu contraseña?" que muestra un Toast "Contacta a soporte académico".

### Validación de formato (validation/LoginValidator.kt)
- Correo: no vacío ("Ingresa tu correo") y formato válido con android.util.Patterns.EMAIL_ADDRESS ("Formato de correo inválido").
- Contraseña: no vacía ("Ingresa tu contraseña").
- Los errores de formato se muestran en cada campo (isError + supportingText) solo después de tocar el campo o presionar el botón.

### Autenticación por usuario
- Si el formato es válido, llama a SessionManager.login(email, password).
- La comparación del correo ignora mayúsculas y espacios al inicio/final (trim + ignoreCase). La contraseña se compara exactamente (distingue mayúsculas).
- Si las credenciales no coinciden con ningún alumno: muestra un mensaje general "Correo o contraseña incorrectos" debajo de los campos (en rojo), sin indicar cuál de los dos falló, y limpia el campo de contraseña.
- Si coinciden: navega a Home con popUpTo(Screen.Login.route) { inclusive = true }.

### Teclado
- Correo: KeyboardType.Email con ImeAction.Next.
- Contraseña: KeyboardType.Password con ImeAction.Done (Done ejecuta el mismo intento de inicio de sesión).

## 5.2 HomeScreen (REDISEÑAR)
- Obtiene el alumno autenticado desde SessionManager.currentUser. Si es null, redirige a Login (ver sección 7).
- Fondo con degradado vertical morado (arriba) → blanco (abajo), a pantalla completa.
- Texto centrado "Bienvenido,\n{nombre del alumno}" en blanco, negrita, grande (usa el nombre corto, ej. "Juan León").
- Subtítulo "¿Qué deseas gestionar hoy?" en blanco semitransparente.
- Dos MenuCard blancas de ancho completo, cada una con ícono dentro de un cuadrado redondeado lavanda, título en negrita y subtítulo pequeño gris:
  a) Groups — "Directorio de Alumnos" / "Ver y gestionar estudiantes" → Screen.List
  b) Person — "Mi Perfil Académico" / "Datos personales y progreso" → Screen.Profile
- Al fondo, centrado: TextButton rojo con ícono Logout y texto "Cerrar Sesión Segura" → cerrar sesión.

## 5.3 ListScreen → "Directorio de Alumnos" (REDISEÑAR)
- TopAppBar con fondo lavanda, título "Directorio de Alumnos" y flecha atrás (popBackStack).
- LazyColumn con items(students, key = { it.id }) tomando los datos de StudentRepository.getAll().
- Cada alumno es una tarjeta gris lavanda con:
  - StudentAvatar circular de 48dp a la izquierda.
  - Nombre corto en negrita y, debajo, la carrera en morado.
  - Ícono ChevronRight a la derecha.
- Al tocar la tarjeta: navController.navigate(Screen.Detail.createRoute(student.id)).

## 5.4 DetailScreen → "Expediente Académico" (REDISEÑAR)
- TopAppBar "Expediente Académico" con flecha atrás.
- GradientHeader morado con esquinas inferiores redondeadas.
- StudentAvatar de ~120dp con borde blanco, superpuesto a mitad entre el encabezado y el contenido (usa offset).
- Nombre corto (negrita, grande) y carrera (morado) centrados debajo del avatar.
- Tarjeta gris lavanda con InfoRow (ícono + etiqueta pequeña + valor):
  - Badge — "ID Estudiante" — studentCode
  - Email — "Correo Electrónico" — email
  - School — "Facultad" — faculty
- HorizontalDivider y sección "Biografía" (título en negrita) con el texto bio.
- Obtén el alumno con StudentRepository.getById(studentId). Si es null, muestra "Alumno no encontrado".

## 5.5 ProfileScreen → "Configuración de Perfil" (REDISEÑAR)
- Muestra los datos del alumno autenticado (SessionManager.currentUser). Si es null, redirige a Login.
- TopAppBar "Configuración de Perfil" con flecha atrás.
- GradientHeader con degradado horizontal morado → malva, con StudentAvatar centrado y el nombre completo en blanco debajo.
- SectionTitle "INFORMACIÓN PERSONAL" (mayúsculas, pequeño, morado) seguido de:
  - Person — "Nombre Completo" — fullName
  - Email — "Correo" — email
  - Phone — "Teléfono" — phone
- SectionTitle "ACADÉMICO" seguido de:
  - School — "Carrera" — career
  - CalendarMonth — "Ciclo Actual" — cycle
- Cada InfoRow: ícono dentro de un cuadrado redondeado gris lavanda, etiqueta gris pequeña y valor en negrita.
- Al fondo: botón de ancho completo con fondo rojo muy claro, texto e ícono Logout en rojo, "Cerrar Sesión" → cerrar sesión.
- La pantalla debe ser desplazable (verticalScroll) para dispositivos pequeños.

---

# 6. DATOS Y SESIÓN

## 6.1 Modelo (data/Student.kt)
    data class Student(
        val id: Int,
        val name: String,          // nombre corto, ej. "Juan León"
        val fullName: String,      // ej. "Juan León Suiyon"
        val career: String,
        val cycle: String,
        val studentCode: String,
        val email: String,         // correo institucional, también es el usuario de login
        val password: String,
        val phone: String,
        val faculty: String,
        val bio: String,
        @DrawableRes val photo: Int? = null
    )

## 6.2 Repositorio (data/StudentRepository.kt)
- object con:
    fun getAll(): List<Student>
    fun getById(id: Int): Student?
    fun findByCredentials(email: String, password: String): Student?
- Alumnos con sus credenciales (inventa phone, faculty y bio coherentes donde falten):
  | id | Nombre corto  | Nombre completo      | Carrera                 | Ciclo    | Código    | Correo                        | Contraseña   |
  |----|---------------|----------------------|-------------------------|----------|-----------|-------------------------------|--------------|
  | 1  | Juan León     | Juan León Suiyon     | Ingeniería de Software  | VI Ciclo | 2024-0001 | juan.leon@tecsup.edu.pe       | Juan2024*    |
  | 2  | María García  | María García Torres  | Arquitectura            | IV Ciclo | 2024-0002 | maria.garcia@tecsup.edu.pe    | Maria2024*   |
  | 3  | Carlos Perez  | Carlos Perez Rojas   | Medicina                | VIII Ciclo | 2024-0003 | carlos.perez@tecsup.edu.pe  | Carlos2024*  |
  | 4  | Ana Lopez     | Ana Lopez Mendoza    | Derecho                 | II Ciclo | 2024-0004 | ana.lopez@tecsup.edu.pe       | Ana2024*     |
  | 5  | Luis Ramirez  | Luis Ramirez Castillo| Administración          | V Ciclo  | 2024-0005 | luis.ramirez@tecsup.edu.pe    | Luis2024*    |
- Juan León: teléfono +51 987 654 321, facultad "Ingeniería y Tecnología", bio "Estudiante destacado con interés en desarrollo Android."
- Deja photo = null en todos por ahora.
- Agrega un comentario indicando que las contraseñas en texto plano son solo para fines académicos y que en producción se usaría autenticación con backend y contraseñas hasheadas.

## 6.3 Sesión (data/SessionManager.kt)
- object SessionManager con:
    var currentUser by mutableStateOf<Student?>(null)
        private set
    fun login(email: String, password: String): Boolean   // busca con findByCredentials; si existe lo guarda en currentUser
    fun logout()                                           // currentUser = null
- La sesión vive solo en memoria (se pierde al cerrar la app); es el comportamiento esperado.

## 6.4 Avatar (ui/components/StudentAvatar.kt)
- Si photo no es null usa painterResource(photo) recortado en círculo; si es null, muestra un círculo de color con las iniciales del nombre. Recibe tamaño y un borde opcional como parámetros.

---

# 7. NAVEGACIÓN
- Screen.kt: Login("login"), Home("home"), List("list"), Profile("profile") y Detail("detail/{studentId}") con createRoute(studentId: Int). Conserva los comentarios en español y la línea "Desarrollado por : Jeanfranco Flores".
- AppNavigation.kt:
  - startDestination = Screen.Login.route.
  - NavHost envuelto en NavLabTheme(dynamicColor = false).
  - Registra las 5 pantallas.
- BUG ACTUAL A CORREGIR: navArgument("ItemId") no coincide con el placeholder "{itemId}" (Navigation distingue mayúsculas), por eso el detalle siempre recibe 0. El nombre en la ruta, en navArgument y en getInt debe ser exactamente "studentId".
- navigation/NavExtensions.kt: extensión reutilizable para cerrar sesión:
    fun NavController.logout() {
        SessionManager.logout()
        navigate(Screen.Login.route) {
            popUpTo(graph.id) { inclusive = true }
        }
    }
  Úsala en HomeScreen y ProfileScreen.
- Protección de rutas: en Home, List, Detail y Profile, si SessionManager.currentUser es null, redirige a Login limpiando el back stack (hazlo dentro de un LaunchedEffect para no navegar durante la composición).

---

# 8. COMPONENTES Y LIMPIEZA
- Implementa en ui/components/: StudentAvatar, MenuCard, InfoRow, GradientHeader y SectionTitle, y úsalos en las pantallas en lugar de repetir código.
- Elimina imports incorrectos o sin uso, en particular:
  - import org.w3c.dom.Text en DetailScreen.kt
  - import android.widget.Space en ProfileScreen.kt
- Agrega un @Preview por pantalla usando rememberNavController() y envolviendo el contenido en NavLabTheme(dynamicColor = false). Para las pantallas que dependen de la sesión, separa el contenido visual en un composable que reciba el Student como parámetro, y en el Preview pásale StudentRepository.getById(1)!!.

---

# 9. DEPENDENCIAS
- Los íconos Groups, Badge, School, Logout, Visibility, VisibilityOff, CalendarMonth y ChevronRight requieren androidx.compose.material:material-icons-extended. Agrégala si no está, usando el BOM de Compose del proyecto (sin versión fija).
- Usa las variantes AutoMirrored cuando existan (ArrowBack, Logout, ChevronRight).

---

# 10. FORMA DE TRABAJO
Trabaja por fases y compila al terminar cada una (./gradlew assembleDebug o la tarea de build del IDE). Si hay errores, corrígelos antes de pasar a la siguiente fase:
  Fase 1: dependencias, colores y tema (dynamicColor), modelo, repositorio y SessionManager.
  Fase 2: LoginValidator y componentes reutilizables.
  Fase 3: Screen.kt, NavExtensions.kt y AppNavigation.kt.
  Fase 4: las 5 pantallas con sus @Preview.
  Fase 5: limpieza final de imports y código sin uso (MainActivity.kt no se toca).

---

# 11. REPORTE FINAL
Al terminar, informa:
- Archivos creados, modificados y eliminados.
- Resultado de la última compilación.
- Tabla de credenciales de prueba (correo y contraseña de cada alumno).
- Decisiones que hayas tomado por tu cuenta.
- Tareas manuales pendientes para mí (por ejemplo, agregar fotos reales en res/drawable y asignarlas en StudentRepository).
```
</details>



