# CampusGPS — Laboratorio 13: Localización Geográfica

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · Tecsup
**Docente:** Juan León S.

## Descripción

CampusGPS obtiene la ubicación del celular, la muestra en un mapa de Google y lista cinco lugares ficticios del campus (biblioteca, cafetería, laboratorio, auditorio y estacionamiento) con su distancia en metros, del más cercano al más lejano. El más cercano se marca en verde y un botón lleva la cámara hasta él. Si el usuario niega el permiso, apaga el GPS o el dispositivo no tiene Google Play Services, la app muestra un mensaje claro con una acción útil y no se cierra.

**Tecnologías:** Kotlin, Jetpack Compose + Material 3 (sin XML), Google Play Services Location (`FusedLocationProviderClient`), Maps Compose. Paquete: `com.floresv.campusgps`.

## Estructura de paquetes

```
com.floresv.campusgps/
├── MainActivity.kt
├── model/
│   └── Modelos.kt                  Posicion, Lugar, LugarConDistancia, UbicacionUi, TipoError
├── data/
│   ├── UbicacionRepository.kt      interface (+ UbicacionNoDisponibleException)
│   ├── UbicacionRepositoryGoogle.kt  FusedLocationProviderClient y Play Services
│   ├── LugaresRepository.kt        interface
│   └── LugaresRepositoryLocal.kt   los 5 lugares ficticios
├── util/
│   ├── Distancia.kt                distanciaMetros(desde, hasta)
│   └── Formato.kt                  formatearDistancia(metros)
├── viewmodel/
│   ├── MapaViewModel.kt
│   └── CampusViewModelFactory.kt
└── ui/
    ├── PantallaMapa.kt             permiso, mapa, botón al más cercano y lista
    ├── MapaUbicacion.kt            GoogleMap con los marcadores
    ├── ListaLugares.kt             tarjetas con la distancia
    ├── MensajeError.kt             error con Abrir ajustes o Reintentar
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaMapa)
   | evento: pulsar el botón          ^ collectAsState()
   v                                  |
ViewModel (MapaViewModel) ---- StateFlow ---->
   | llama a las interfaces
   v
UbicacionRepository ---> UbicacionRepositoryGoogle (FusedLocationProviderClient)
LugaresRepository   ---> LugaresRepositoryLocal (los 5 lugares fijos)
```

- La pantalla solo dibuja el estado y avisa los eventos. No usa el Repository ni calcula distancias.
- El `MapaViewModel` solo conoce las interfaces y no recibe `Context`: el `Context` se queda en `CampusViewModelFactory`.
- Las distancias se calculan en `util/` y se ordenan en el ViewModel (`actualizarDistancias`).

## Configurar la clave de Google Maps

1. En Google Cloud, habilitar **Maps SDK for Android** y crear una clave de API.
2. Restringirla a la app Android con el paquete `com.floresv.campusgps` y la huella SHA-1 de `./gradlew signingReport`.
3. Agregarla en `local.properties` (en la raíz del proyecto):
   ```
   MAPS_API_KEY=tu_clave
   ```
4. `app/build.gradle.kts` la lee de `local.properties` y la pasa al Manifest con `manifestPlaceholders["MAPS_API_KEY"]`; el Manifest la usa en `<meta-data android:name="com.google.android.geo.API_KEY" android:value="${MAPS_API_KEY}" />`.

`local.properties` está en `.gitignore`: la clave nunca se sube a GitHub.

**Versión de Maps Compose:** la guía indica `maps-compose:8.4.0`, pero esa versión exige `compileSdk 37` y Android Gradle Plugin 9.1. Se usa `maps-compose:8.0.0`, la más reciente compatible con `compileSdk 36`; las funciones del laboratorio (`GoogleMap`, `Marker`, `rememberUpdatedMarkerState`, `rememberCameraPositionState`) son las mismas.

## Probar en el emulador

Usar un emulador **con Google Play**. El emulador empieza en California: en **⋮ (Extended controls) → Location** escribir `-12.0464` y `-77.0428` y pulsar **Set location**.

## Pruebas de aceptación

| Prueba | Resultado esperado | OK |
|---|---|---|
| Abrir la app por primera vez y pulsar el botón | Aparece el cuadro de permiso | ☐ |
| Aceptar el permiso | Se ven latitud, longitud y el marcador Estás aquí | ☐ |
| Negar el permiso | Mensaje de error con Abrir ajustes, la app no se cierra | ☐ |
| Ubicación en el punto base | Los 5 marcadores azules aparecen cerca de tu posición | ☐ |
| Tocar un marcador | Se ve el nombre y la categoría del lugar | ☐ |
| Lista de lugares | Ordenada de menor a mayor distancia, con m o km | ☐ |
| Tocar una tarjeta de la lista | La cámara viaja a ese lugar y la tarjeta queda resaltada | ☐ |
| Ir al más cercano | La cámara va al lugar verde; el botón está deshabilitado antes de buscar | ☐ |
| Cambiar la ubicación del emulador y buscar otra vez | Las distancias, el orden y el lugar verde se actualizan | ☐ |
| Ubicación apagada | Mensaje de GPS con Reintentar | ☐ |
| Rotar la pantalla | No se pierden la ubicación ni la lista | ☐ |
| Revisar el repositorio | `git grep "tu_clave"` no encuentra nada y `local.properties` no está subido | ☐ |

Con el emulador en el punto base, las distancias esperadas son: Biblioteca Central 88 m, Cafetería Campus 110 m, Laboratorio de Cómputo 173 m, Auditorio Principal 221 m y Estacionamiento Norte 268 m.

## Capturas

Guardar las imágenes en la carpeta `capturas/` con estos nombres:

| Permiso | Mapa con mi ubicación | Marcadores |
|---|---|---|
| ![Permiso](capturas/1_permiso.png) | ![Mapa](capturas/2_mapa.png) | ![Marcadores](capturas/3_marcadores.png) |

| Lista ordenada | Error |
|---|---|
| ![Lista](capturas/4_lista.png) | ![Error](capturas/5_error.png) |

## Para pensar

**1. ¿Por qué `FusedLocationProviderClient` está dentro del Repository y no en el ViewModel?**
Porque es un detalle de cómo se obtiene la ubicación (Google Play Services) y necesita un `Context`. Si estuviera en el ViewModel, este dependería de Android y no se podría cambiar la fuente ni probarlo con un repositorio falso. El ViewModel solo conoce la interface `UbicacionRepository` y recibe un `Result<Posicion>`.

**2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps? ¿Qué pasa si falta cada uno?**
El permiso lo da el **usuario** en su celular y permite leer dónde está; la clave identifica a la **app** ante Google para dibujar el mapa. Sin permiso, el mapa se ve pero no se puede obtener la posición (la app muestra el error con Abrir ajustes). Sin clave (o con una clave mal restringida), la ubicación funciona pero el mapa sale gris y Logcat muestra un error de autorización.

**3. ¿Por qué la clave va en `local.properties` y no en el código? ¿Qué harías si se subiera a GitHub por error?**
Porque `local.properties` está en `.gitignore` y no se sube; si la clave estuviera en el código o en el Manifest, cualquiera que vea el repositorio podría usarla y consumir la cuota del proyecto. Si se subiera por error, la borraría en Google Cloud, crearía una nueva con sus restricciones y revisaría el `.gitignore`; borrarla del repositorio no basta porque queda en el historial.

**4. ¿Dónde calculas la distancia y por qué no en el Composable?**
En `util/Distancia.kt` (`distanciaMetros` con `Location.distanceBetween`) y el ViewModel la aplica y ordena en `actualizarDistancias`, una sola vez cuando llega la ubicación. Un Composable se redibuja muchas veces; calcular ahí repetiría el trabajo, mezclaría lógica con la vista y rompería MVVM.

**5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces? ¿Cómo lo manejas?**
Android deja de mostrar el cuadro de permiso y el lanzador responde "negado" al instante. La app no se cierra: `permisoDenegado()` pone el error `SIN_PERMISO` y `MensajeError` muestra **Abrir ajustes**, que abre la pantalla de la app en Ajustes (`ACTION_APPLICATION_DETAILS_SETTINGS`) para que el usuario conceda el permiso a mano.
