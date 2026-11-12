# ManhwaVerse — Laboratorio 11-12: Compose + MVVM + Retrofit + Room + Firebase Auth

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · Tecsup
**Docente:** Juan José León Suiyon

## Tema

Catálogo de **manhwas** (cómics coreanos) de acción y fantasía: sistemas, mazmorras, monstruos y apocalipsis.

| | Detalle |
|---|---|
| API | [Kitsu](https://kitsu.docs.apiary.io/) (`https://kitsu.io/api/edge/`), pública y sin clave |
| Lista | `GET manga?filter[subtype]=manhwa&filter[categories]=action,fantasy&sort=-userCount&page[limit]=20&include=categories` (Acción + Fantasía, los más leídos) |
| Detalle | `GET manga/{id}?include=categories` |
| Paquete | `com.tecsup.manhwas` |

Para comprobar que la API responde, abrir en el navegador:
https://kitsu.io/api/edge/manga?filter[subtype]=manhwa&filter[categories]=action,fantasy&sort=-userCount&page[limit]=5

Kitsu responde en formato JSON:API: los géneros vienen aparte en `included` y `Mappers.kt` los une con cada manhwa. El id llega como texto (`"54114"`) y se guarda como `Int`. El puntaje viene sobre 100 y se muestra sobre 10.

## Qué hace la app

| | Usuario | Admin |
|---|---|---|
| Ingresar con correo y contraseña | Sí | Sí |
| Ver lista y detalle | Sí | Sí |
| Marcar favoritos y ver Favoritos | Sí | Sí |
| Cerrar sesión | Sí | Sí |
| Panel Admin: totales, Sincronizar y Limpiar caché | No | Sí |

- La lista sale siempre de **Room**; Retrofit solo la actualiza. Sin internet se ve lo guardado con un aviso.
- Al sincronizar, los favoritos se conservan y llega la notificación «Se guardaron N elementos».
- La sesión se mantiene al reabrir la app.

## Mejora con IA (rama `mejora-ia-manhwaverse`)

Detalle de las consultas en `PROMPTS.md`.

- **Interfaz estilo web de lectura:** fondo negro, acentos morados, manhwa más leído destacado, portadas con puntaje, estado y capítulos, y esqueletos animados mientras carga.
- **Búsqueda y filtros por género** en el catálogo (calculados en el `ListaViewModel`).
- **Detalle** con la portada de fondo, chips de género y sinopsis con «Leer más».
- **Componentes reutilizables** en `ui/components`: botones (`BotonPrimario`, `BotonSecundario`, `BotonPeligro`, `BotonIcono`), `Chip`, insignias, `Portada`, `CampoTexto`, `BarraBusqueda`, `Aviso`, `EncabezadoSeccion`, `TarjetaEstadistica`, `TarjetaManhwa` y `TarjetaDestacada`.
- **Pruebas unitarias (R12):** `ListaViewModelTest` con un repositorio falso, 6 pruebas. Ejecutar con `./gradlew testDebugUnitTest`.

## Arquitectura MVVM

```
UI (Compose) --eventos--> ViewModel (StateFlow<UiState>) --> Repository (interface)
     ^                          |                               |-- Retrofit (Kitsu)
     |-------- estado ----------|                               |-- Room (fuente única de verdad)
                                |--> Notificador (interface)    |-- Firebase (Auth + Firestore: rol)
```

- Las pantallas solo observan con `collectAsStateWithLifecycle()` y llaman funciones del ViewModel.
- Ningún composable usa Retrofit, Room ni Firebase.
- El ViewModel no recibe `Context`: la notificación se pide por la interface `Notificador`.
- Tres modelos: `ItemDto` (API), `ItemEntity` (Room) e `Item` (UI). `Mappers.kt` los convierte.
- Los ViewModels se crean con `ViewModelFactory`, que recibe el `AppContainer`.

```
app/src/main/java/com/tecsup/manhwas/
├── MainActivity.kt
├── ManhwaApp.kt              guarda el AppContainer
├── AppContainer.kt           crea API, base de datos y repositorios
├── data/
│   ├── remote/               ApiService, RetrofitClient, dto/ItemDto (Kitsu)
│   ├── local/                ItemEntity, ItemDao, AppDatabase
│   ├── auth/                 AuthRepository, FirebaseAuthRepository
│   ├── repository/           ItemRepository, ItemRepositoryImpl
│   └── mapper/Mappers.kt
├── domain/model/             Item, Sesion + Rol
├── notification/             Notificador, NotificadorAndroid
└── ui/
    ├── navigation/AppNav.kt
    ├── UiState.kt, ViewModelFactory.kt, Mensajes.kt
    ├── login/ lista/ detalle/ favoritos/ admin/
    ├── components/           botones, chips, insignias, campos, tarjetas, Cargando, ErrorView
    └── theme/
```

## Configurar Firebase

1. En https://console.firebase.google.com/ crear un proyecto.
2. Registrar una app Android con el paquete exacto **`com.tecsup.manhwas`**.
3. Descargar `google-services.json` y copiarlo en **`app/`**. No se sube al repositorio (está en `.gitignore`).
4. **Authentication → Método de acceso:** activar **Correo electrónico/contraseña**.
5. **Authentication → Usuarios:** crear dos usuarios de prueba, por ejemplo `admin@manhwaverse.com` y `usuario@manhwaverse.com`.
6. **Firestore Database:** crear la base y la colección **`usuarios`** con un documento por usuario. El **ID del documento es el UID** (se copia de Authentication → Usuarios) y tiene los campos `rol` (`admin` o `usuario`) y `nombre`.
7. **Firestore → Reglas:** pegar y publicar:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

Validar el rol en la app solo cambia lo que se muestra; lo que protege los datos son las reglas de Firestore.

## Cómo ejecutar

1. Abrir `Semana12/ManhwaVerseFlores` en Android Studio y sincronizar Gradle.
2. Ejecutar en un emulador con Google Play (API 26 o superior).

## Pruebas de aceptación

| Cód. | Caso | Resultado esperado |
|---|---|---|
| PA-01 | Ingresar con el usuario `usuario` | Entra al catálogo. No ve el Panel |
| PA-02 | Ingresar con el usuario `admin` | Entra al catálogo y ve la pestaña Panel |
| PA-03 | Contraseña incorrecta | «Correo o contraseña incorrectos». No entra |
| PA-04 | Correo o clave vacíos | «Completa el correo y la contraseña». No llama a Firebase |
| PA-05 | Primera apertura con internet | Cargando, luego la lista y la notificación «Se guardaron N elementos» |
| PA-06 | Tocar un manhwa | Detalle con datos del segundo endpoint |
| PA-07 | Marcar un favorito y cerrar la app | Al reabrir sigue en Favoritos |
| PA-08 | Modo avión con datos guardados | La lista se ve con el aviso de sin conexión |
| PA-09 | Modo avión sin datos (app recién instalada) | Error con botón Reintentar |
| PA-10 | Sincronizar después de marcar un favorito | El favorito no se pierde |
| PA-11 | Admin pulsa Limpiar caché | Lista vacía y totales en 0; al sincronizar vuelven |
| PA-12 | Cerrar sesión y reabrir | Vuelve al login |
| PA-13 | Girar la pantalla | No se pierde el estado ni se vuelve a descargar |
| PA-14 | Negar el permiso de notificaciones | La app funciona normal, sin avisos |
