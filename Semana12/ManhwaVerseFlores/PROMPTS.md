# Prompts utilizados

Rama: `mejora-ia-manhwaverse` · Herramienta: asistente de IA (Claude), con permiso del docente para usar IA en ambas fases.
Reglas respetadas en todas las consultas: las pantallas solo observan el estado y llaman funciones del ViewModel; ningún composable usa Retrofit, Room ni Firebase; las reglas (búsqueda, filtros) viven en el ViewModel.

## Consulta 1 — API del tema
- **Fecha:** 10/11/2026
- **Herramienta:** Claude
- **Prompt:** «Mi tema son manhwas de acción: sistemas, mazmorras, monstruos, apocalipsis. Si con ese link no se muestra nada, quizás esa API no funciona.»
- **Qué respondió (resumen):** la primera opción fue Jikan (MyAnimeList), pero no respondía. Probó Kitsu y MangaDex, y propuso Kitsu: `manga?filter[subtype]=manhwa&filter[categories]=action,fantasy&sort=-userCount` devuelve *Solo Leveling*, *Omniscient Reader*, *Dungeon Reset*… y `manga/{id}` sirve como segundo endpoint.
- **Qué usamos / qué cambiamos y por qué:** se cambió la URL base y se rehízo el DTO, porque Kitsu usa JSON:API: los géneros vienen aparte en `included` y `Mappers.kt` los une con cada manhwa. El id llega como texto y se convierte a `Int`; el puntaje viene sobre 100 y se muestra sobre 10. Se probó el DTO con respuestas reales antes de subirlo.
- **Qué aprendimos:** hay que abrir la URL antes de crear el DTO (como pide la guía), y con `ignoreUnknownKeys` y valores por defecto un campo que falta no tumba la app.

## Consulta 2 — Interfaz estilo web de lectura
- **Fecha:** 10/11/2026
- **Herramienta:** Claude
- **Prompt:** «Que la interfaz sea inspirada en webs reales de lectura, con colores negros y púrpuras, sin descuidar la correcta visualización. Usa componentes reutilizables para mantener la uniformidad de los botones y simplificar código.»
- **Qué respondió (resumen):** una paleta negra y morada en `Theme.kt` y componentes en `ui/components`: `BotonPrimario`, `BotonSecundario`, `BotonPeligro`, `BotonIcono`, `Chip`, `InsigniaPuntaje`, `InsigniaEstado`, `InsigniaCapitulos`, `Portada`, `CampoTexto`, `BarraBusqueda`, `EncabezadoSeccion`, `Aviso`, `TarjetaEstadistica`, `Logo`, `TarjetaManhwa`, `TarjetaDestacada` y `EsqueletoCatalogo`. El catálogo tiene el manhwa más leído destacado arriba, búsqueda, chips de género y portadas con el título debajo; el detalle tiene la portada de fondo y la sinopsis con «Leer más».
- **Qué usamos / qué cambiamos y por qué:** todas las pantallas pasaron a usar los mismos botones (mismo alto, forma y color). Cada texto tiene color claro explícito sobre fondo oscuro; se revisaron capturas de las pantallas para confirmar que todo se lee. La búsqueda y el filtro por género se calculan en el `ListaViewModel` (`visibles`), no en la pantalla.
- **Qué aprendimos:** un componente reutilizable evita repetir colores y tamaños en cada pantalla, y un cambio de estilo se hace en un solo lugar.

## Consulta 3 — Pruebas unitarias (R12)
- **Fecha:** 10/11/2026
- **Herramienta:** Claude
- **Prompt:** «Pruebas unitarias del ViewModel de la lista con un repositorio falso (mínimo 3 pruebas).»
- **Qué respondió (resumen):** `ListaViewModelTest` con `RepositorioFalso` (implementa `ItemRepository` en memoria) y `NotificadorFalso` (anota las notificaciones), usando `Dispatchers.setMain(UnconfinedTestDispatcher())`.
- **Qué usamos / qué cambiamos y por qué:** 6 pruebas: refresco exitoso con notificación «Se guardaron N elementos»; sin internet y sin datos → Error; sin internet con datos → lista + aviso; Reintentar después de un error; búsqueda y filtro por género; marcar favorito. Se ejecutan con `./gradlew testDebugUnitTest` y pasan las 6.
- **Qué aprendimos:** como el ViewModel depende de interfaces (`ItemRepository`, `Notificador`) y no de Room ni de Android, se puede probar sin emulador.
