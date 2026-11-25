# MuroCampus — Laboratorio 09: Servicios Web y plataforma REST

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · Tecsup
**Docente:** Juan León S.

## Descripción

MuroCampus es un muro de publicaciones que viene de internet. Consume el servicio web REST de práctica [JSONPlaceholder](https://jsonplaceholder.typicode.com/): lista las 100 publicaciones, muestra el detalle de una publicación con sus comentarios, filtra por usuario y crea una publicación nueva. Cuando no hay internet o el servidor falla, la app muestra un mensaje claro con **Reintentar** y no se cierra.

**Tecnologías:** Kotlin, Jetpack Compose + Material 3 (sin XML), Retrofit, OkHttp, kotlinx.serialization, corrutinas y ViewModel. Paquete: `com.tecsup.murocampus`.

## La API que se usa

| Para... | Verbo y ruta | Anotación | Qué devuelve |
|---|---|---|---|
| Listar publicaciones | `GET posts` | — | 100 publicaciones |
| Ver una publicación | `GET posts/{id}` | `@Path("id")` | Una publicación (404 si no existe) |
| Ver sus comentarios | `GET posts/{id}/comments` | `@Path("id")` | 5 comentarios |
| Filtrar por usuario | `GET posts?userId=2` | `@Query("userId")` | 10 publicaciones del usuario |
| Crear una publicación | `POST posts` | `@Body` | La publicación creada con `id` = 101 |

## Estructura de paquetes

```
com.tecsup.murocampus/
├── MainActivity.kt                navegación simple: lista, detalle (idSeleccionado) y crear
├── model/
│   └── Modelos.kt                 Usuario (ejemplo), Post, Comentario, NuevoPost  (@Serializable)
├── data/
│   ├── remote/
│   │   ├── PostApi.kt             interface de Retrofit: @GET, @POST, @Path, @Query, @Body
│   │   └── RetrofitClient.kt      Retrofit + OkHttp (logging BODY) + conversor JSON, creado una sola vez
│   └── PostRepository.kt          interface + PostRepositoryRemote (todo devuelve Result)
├── viewmodel/
│   ├── UiState.kt                 Cargando / Exito / Error y mensajeDe(e)
│   ├── PostsViewModel.kt          lista, filtro, detalle y crear
│   └── PostsViewModelFactory.kt   crea el ViewModel con su Repository
└── ui/
    ├── Componentes.kt             TarjetaPost, EtiquetaUsuario, Cargando, ErrorConReintento
    ├── PantallaLista.kt           chips de usuario, lista y botón +
    ├── PantallaDetalle.kt         publicación y Comentarios (N)
    ├── PantallaCrear.kt           Título, Contenido y Publicar
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaLista, PantallaDetalle, PantallaCrear)
   |  eventos: cargar, filtrar, abrir, publicar     ^ collectAsStateWithLifecycle()
   v                                                |
ViewModel (PostsViewModel) --- StateFlow<UiState> (Cargando / Exito / Error) --->
   |  viewModelScope.launch
   v
PostRepository (interface)        <- devuelve Result<...>
   -> PostRepositoryRemote        (captura IOException, HttpException, SerializationException...)
   |
   v
PostApi (Retrofit) --- OkHttp ---> https://jsonplaceholder.typicode.com/
```

- **La pantalla nunca llama a la API:** solo dibuja el estado con un `when` y avisa los eventos al ViewModel.
- **El ViewModel** solo conoce la interface `PostRepository`. No usa Retrofit, `PostApi`, `HttpException` ni `Context`; convierte el `Result` en un `UiState`.
- **El Repository** nunca deja escapar una excepción: la convierte en `Result.failure`. Una función privada `llamar { }` evita repetir el `try/catch` cinco veces, y siempre vuelve a lanzar la `CancellationException`.
- **`PostsViewModelFactory`** es el único lugar donde se juntan el Repository y `RetrofitClient`.
- El detalle pide la publicación y sus comentarios **a la vez** con `async` dentro de `coroutineScope`; si una de las dos llamadas falla, todo el detalle muestra el error.

## Pruebas de aceptación

| Prueba | Cómo provocarla | Resultado esperado | OK |
|---|---|---|---|
| Sin internet | Activa el modo avión y abre la app | **Sin conexión a internet** y botón **Reintentar** | ☐ |
| Reintentar | Desactiva el modo avión y pulsa Reintentar | Carga la lista | ☐ |
| Recurso inexistente | Cambia `posts/{id}` por `posts/9999` en `detalle` (temporal) | **Error del servidor (404)** | ☐ |
| JSON inesperado | Quita un campo de `Post` que sí llega | Funciona (por `ignoreUnknownKeys`); si agregas uno que no llega, falla con **Respuesta inesperada** | ☐ |
| Cancelación | Abre el detalle y vuelve rápido | No hay errores en Logcat | ☐ |
| Lista y detalle | Abre varias publicaciones | Siempre 5 comentarios | ☐ |
| Filtro | Elige cada usuario | Siempre 10 publicaciones | ☐ |
| Crear | Envía una publicación | **Publicado con id 101** | ☐ |
| Código | Revisa `viewmodel/` | No hay Retrofit, `PostApi` ni `HttpException` en el ViewModel | ☐ |

En Logcat, el filtro `OkHttp` muestra cada petición (por ejemplo `GET posts?userId=2` o el `POST` con su JSON y la respuesta 201).

## Para pensar

**1. ¿Qué diferencia hay entre GET y POST? ¿Por qué `crear` lleva `@Body` y `porUsuario` lleva `@Query`?**
GET **lee** un recurso y no cambia nada en el servidor; POST **crea** uno nuevo. `crear` envía un objeto completo (título, contenido y usuario), así que va como JSON en el **cuerpo** de la petición con `@Body`. `porUsuario` solo necesita un dato para filtrar lo que se lee, así que va en la URL como parámetro después del `?` con `@Query("userId")` (`posts?userId=2`).

**2. ¿Por qué la ruta es `"posts"` y no `"/posts"`?**
Porque Retrofit une la ruta con la URL base. Sin la barra, `posts` se agrega al final de `https://jsonplaceholder.typicode.com/`. Una ruta que empieza con `/` es absoluta: reemplaza el camino de la URL base y solo conserva el dominio, así que si la URL base tuviera un camino (por ejemplo `/api/v1/`), se perdería. Por eso la URL base termina en `/` y las rutas no empiezan con `/`.

**3. ¿Por qué el Repository devuelve `Result` en vez de dejar que la excepción llegue al ViewModel?**
Para que el ViewModel no tenga que conocer las excepciones de la red (`IOException`, `HttpException`, `SerializationException`), que son detalles de Retrofit. El ViewModel solo recibe éxito o fallo y lo convierte en `UiState`. Así nunca se cierra la app por una excepción olvidada, y se puede cambiar la fuente de datos (otra API, una base de datos local o un repositorio falso para pruebas) sin tocar el ViewModel ni la pantalla.

**4. ¿Qué pasa si olvidas el permiso `INTERNET`? ¿Y si olvidas `ignoreUnknownKeys` y el servidor agrega un campo nuevo?**
Sin el permiso la app compila y abre, pero **nunca se conecta**: toda llamada falla con un error de red y la pantalla muestra "Sin conexión a internet" aunque el celular tenga internet. Sin `ignoreUnknownKeys`, kotlinx.serialization rechaza cualquier campo del JSON que no exista en la `data class`: si el servidor agrega un campo nuevo, todas las llamadas fallan con `SerializationException` y la app muestra "Respuesta inesperada".

**5. ¿Por qué la pantalla de lista usa un `when` sobre `UiState` y no un `if` con tres variables?**
Porque `UiState` es una `sealed interface`: la pantalla solo puede estar en **un** estado a la vez (Cargando, Exito o Error) y el `when` obliga a cubrir los tres. Con tres variables (`cargando`, `datos`, `error`) podrían quedar combinaciones imposibles, como cargando y error al mismo tiempo o datos viejos junto a un error, y habría que recordar actualizar las tres en cada cambio.

**6. JSONPlaceholder no guarda los POST. ¿Cómo cambiaría tu app si usaras un servicio real?**
La pantalla y el ViewModel casi no cambiarían: solo cambiarían la URL base en `RetrofitClient` y, si el servicio lo pide, los modelos y la autenticación (por ejemplo, un token en un `Interceptor`). Además, después de publicar habría que actualizar la lista: agregar la publicación devuelta al principio de la lista del ViewModel o volver a llamar a `cargar()`, porque esta vez el servidor sí la guardaría y devolvería un `id` real.
