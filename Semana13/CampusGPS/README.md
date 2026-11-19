# CampusGPS — Laboratorio 13: Localización Geográfica

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · Tecsup
**Docente:** Juan León S.

## Descripción

CampusGPS obtiene la ubicación del celular, la muestra en un mapa de Google y lista cinco lugares ficticios del campus (biblioteca, cafetería, laboratorio, auditorio y estacionamiento) con su distancia en metros, del más cercano al más lejano. El más cercano se marca en verde y un botón lleva la cámara hasta él. Si el usuario niega el permiso, apaga el GPS o el dispositivo no tiene Google Play Services, la app muestra un mensaje claro con una acción útil y no se cierra.

**Tecnologías:** Kotlin, Jetpack Compose + Material 3 (sin XML), Google Play Services Location (`FusedLocationProviderClient`) y OpenStreetMap con osmdroid para el mapa. Paquete: `com.floresv.campusgps`.

## Fase 2: mejora con IA (rama `mejora-ia-campusgps`)

Detalle de los prompts en `PROMPTS.md`.

| Mejora | Qué hace | Capas |
|---|---|---|
| Diseño estilo app de mapas | Mapa a pantalla completa, barra flotante con el estado, hoja inferior deslizable con el más cercano y la lista, y botón flotante «Mi ubicación» | View |
| Punto azul nativo | `MyLocationNewOverlay` de osmdroid con tu ubicación en vivo, solo si el permiso fue concedido | View |
| Filtrar por categoría | Chips con icono y color (Estudio, Comida, Eventos, Servicios) que filtran el mapa y la lista | ViewModel + View |
| Radio de cercanía | 100, 200 o 500 m: círculo verde (`Polygon.pointsAsCircle`) alrededor de tu posición y la etiqueta «Dentro del radio» en la lista | ViewModel + View |
| Último lugar visitado | DataStore guarda el último lugar elegido; al abrir la app el mapa viaja hasta él | Repository + ViewModel |
| Ficha del lugar | Foto, horario, descripción y minutos caminando. Se abre al tocar una tarjeta o un marcador, con los botones Cómo llegar, Centrar y Compartir | Repository + View |

Componentes reutilizables en `ui/components`: `BarraSuperior`, `ChipsCategorias`, `SelectorRadio`, `TarjetaMasCercano`, `TarjetaLugar`, `FichaLugar`, `FotoLugar`, `InsigniaRadio` y `Categorias.kt` (color e icono por categoría).

### Créditos de las fotos

Las fotos de los lugares son referenciales (los lugares son ficticios) y están en `res/drawable-nodpi`.

| Lugar | Foto | Autor | Licencia |
|---|---|---|---|
| Biblioteca Central | [American Library reading area](https://commons.wikimedia.org/wiki/File:American_Library_reading_area.jpg) | Liudmila | Dominio público |
| Cafetería Campus | [Batensteinbad-kantine](https://commons.wikimedia.org/wiki/File:Batensteinbad-kantine.jpg) | Edoderoo | CC BY-SA 3.0 |
| Laboratorio de Cómputo | [AI Lab 1](https://commons.wikimedia.org/wiki/File:AI_Lab_1.jpg) | ManoBV16 | CC0 |
| Auditorio Principal | Foto propia del estudiante | — | — |
| Estacionamiento Norte | [Parking in the night](https://commons.wikimedia.org/wiki/File:20251107_Parking_in_the_night.jpg) | Tn.kuvat | CC BY 4.0 |

Las fotos se recortaron a 800 × 500 px.

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
│   ├── LugaresRepositoryLocal.kt   los 5 lugares ficticios
│   ├── PreferenciasRepository.kt   interface (Fase 2)
│   └── PreferenciasRepositoryDataStore.kt  último lugar visitado (Fase 2)
├── util/
│   ├── Distancia.kt                distanciaMetros(desde, hasta)
│   └── Formato.kt                  formatearDistancia(metros), minutosCaminando(metros)
├── viewmodel/
│   ├── MapaViewModel.kt
│   └── CampusViewModelFactory.kt
└── ui/
    ├── PantallaMapa.kt             permiso, mapa, botón al más cercano y lista
    ├── MapaUbicacion.kt            mapa de OpenStreetMap (osmdroid) con los marcadores
    ├── ListaLugares.kt             tarjetas con la distancia
    ├── MensajeError.kt             error con Abrir ajustes o Reintentar
    ├── components/                 componentes reutilizables (Fase 2)
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

## Mapa: OpenStreetMap en lugar de Google Maps

El mapa usa **OpenStreetMap con la librería osmdroid** (`org.osmdroid:osmdroid-android:6.1.20`) en lugar de Google Maps. Google Cloud exige registrar una cuenta de facturación con tarjeta para crear la clave de Maps; OpenStreetMap es gratuito y **no necesita clave de API ni cuenta**.

- `MapaUbicacion` muestra un `MapView` de osmdroid dentro de Compose con `AndroidView` y sigue el ciclo de vida de la pantalla (`onResume`, `onPause`, `onDetach`).
- Los marcadores mantienen los colores de la guía: **rojo** para «Estás aquí», **azul** para los lugares y **verde** para el más cercano. Al tocar uno se ve su nombre y su categoría.
- La cámara empieza en Lima (zoom 12), viaja a tu posición al obtener la ubicación (zoom 16) y al lugar elegido en la lista (zoom 17).
- La **ubicación** sigue siendo de Google Play Services (`FusedLocationProviderClient`), que es gratis y no usa clave.
- El Manifest pide `INTERNET` y `ACCESS_NETWORK_STATE` para descargar los mosaicos del mapa, y osmdroid se identifica ante OpenStreetMap con el nombre del paquete, como pide su política de uso.

En el Paso 1 se configuró la clave de Maps en `local.properties` (commit `chore: configura permisos y clave de Maps con local.properties`). Al cambiar a OpenStreetMap esa configuración se retiró porque ya no hace falta; `local.properties` sigue en `.gitignore`.

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
| Sin internet | La ubicación funciona; el mapa muestra solo los mosaicos ya descargados | ☐ |

Con el emulador en el punto base, las distancias esperadas son: Biblioteca Central 88 m, Cafetería Campus 110 m, Laboratorio de Cómputo 173 m, Auditorio Principal 221 m y Estacionamiento Norte 268 m.

## Para pensar

**1. ¿Por qué `FusedLocationProviderClient` está dentro del Repository y no en el ViewModel?**
Porque es un detalle de cómo se obtiene la ubicación (Google Play Services) y necesita un `Context`. Si estuviera en el ViewModel, este dependería de Android y no se podría cambiar la fuente ni probarlo con un repositorio falso. El ViewModel solo conoce la interface `UbicacionRepository` y recibe un `Result<Posicion>`.

**2. ¿Qué diferencia hay entre el permiso de ubicación y la clave de API de Maps? ¿Qué pasa si falta cada uno?**
El permiso lo da el **usuario** en su celular y permite leer dónde está; la clave identifica a la **app** ante Google para dibujar el mapa. Sin permiso, el mapa se ve pero no se puede obtener la posición (la app muestra el error con Abrir ajustes). Con Google Maps, sin clave (o con una clave mal restringida) la ubicación funciona pero el mapa sale gris y Logcat muestra un error de autorización. Por eso esta app usa OpenStreetMap: el mapa no necesita clave, mientras que el permiso de ubicación sigue siendo obligatorio.

**3. ¿Por qué la clave va en `local.properties` y no en el código? ¿Qué harías si se subiera a GitHub por error?**
Porque `local.properties` está en `.gitignore` y no se sube (así se configuró en el Paso 1); si la clave estuviera en el código o en el Manifest, cualquiera que vea el repositorio podría usarla y consumir la cuota del proyecto. Si se subiera por error, la borraría en Google Cloud, crearía una nueva con sus restricciones y revisaría el `.gitignore`; borrarla del repositorio no basta porque queda en el historial.

**4. ¿Dónde calculas la distancia y por qué no en el Composable?**
En `util/Distancia.kt` (`distanciaMetros` con `Location.distanceBetween`) y el ViewModel la aplica y ordena en `actualizarDistancias`, una sola vez cuando llega la ubicación. Un Composable se redibuja muchas veces; calcular ahí repetiría el trabajo, mezclaría lógica con la vista y rompería MVVM.

**5. ¿Qué pasa con tu app si el usuario niega el permiso dos veces? ¿Cómo lo manejas?**
Android deja de mostrar el cuadro de permiso y el lanzador responde "negado" al instante. La app no se cierra: `permisoDenegado()` pone el error `SIN_PERMISO` y `MensajeError` muestra **Abrir ajustes**, que abre la pantalla de la app en Ajustes (`ACTION_APPLICATION_DETAILS_SETTINGS`) para que el usuario conceda el permiso a mano.
