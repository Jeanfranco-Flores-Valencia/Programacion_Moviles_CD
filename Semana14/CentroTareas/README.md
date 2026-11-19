# CentroTareas — Laboratorio 14: Hilos en Android

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · Tecsup
**Docente:** Juan León S.

## Descripción

CentroTareas hace tres cosas que tardan: una tarea con barra de progreso, un cálculo pesado (contar números primos) y dos cargas de datos simuladas. Primero se provoca el congelamiento de la pantalla con `Thread.sleep` en el hilo principal (Paso 1) y después se resuelve con **corrutinas**: `viewModelScope`, `withContext`, cancelación con `Job`, cargas en paralelo con `async` y manejo de errores. Usa solo datos ficticios.

**Tecnologías:** Kotlin, Jetpack Compose + Material 3 (sin XML), corrutinas y ViewModel. Paquete: `com.floresv.centrotareas`.

## Fase 2: mejora con IA (rama `mejora-ia-centrotareas`)

Detalle de los prompts en `PROMPTS.md`.

| Mejora | Qué hace | Capas |
|---|---|---|
| Panel de tareas | Barra grande que se encoge, una tarjeta por tarea con icono e insignia de estado, anillo de progreso y un «pulso» animado que deja de latir si la pantalla se congela | View |
| Progreso desde el Repository | La tarea simulada vive en el Repository y emite su avance como `Flow` con `flowOn(Dispatchers.Default)` | Repository + ViewModel |
| Primos con progreso | Barra con el porcentaje y los primos encontrados mientras calcula; Cancelar lo corta al instante | Repository + ViewModel + View |
| Tiempo máximo | Interruptor que limita las cargas a 4 s con `withTimeout`: una por una se corta y a la vez entra | ViewModel + View |
| Comparación de tiempos | Las últimas 6 cargas en barras, con los promedios y cuánto más rápido fue cargar a la vez | ViewModel + View |

Componentes reutilizables en `ui/components`: `TarjetaTarea`, `InsigniaEstado`, `BotonPrincipal`, `BotonSecundario`, `FilaAcciones`, `Resultado`, `AnilloProgreso`, `PulsoInterfaz` y `BarraTiempo`.

## Estructura de paquetes

```
com.floresv.centrotareas/
├── MainActivity.kt
├── model/
│   └── Estados.kt                 PrimosUi, CargaUi
├── data/
│   ├── TareasRepository.kt        interface
│   └── TareasRepositoryLocal.kt   cargas simuladas y contarPrimos
├── viewmodel/
│   ├── TareasViewModel.kt         tarea con progreso, primos y cargas
│   └── TareasViewModelFactory.kt
└── ui/
    ├── PantallaTareas.kt          las tarjetas en una lista
    ├── TarjetaProgreso.kt, TarjetaPrimos.kt, TarjetaCargas.kt, TarjetaComparacion.kt   (Fase 2)
    ├── components/                componentes reutilizables (Fase 2)
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaTareas)
   | eventos: botones                 ^ collectAsState()
   v                                  |
ViewModel (TareasViewModel) ---- StateFlow ---->
   | viewModelScope.launch { ... }
   v
TareasRepository (interface) ---> TareasRepositoryLocal (cargas simuladas y contar primos)
```

- La pantalla no lanza corrutinas ni usa `Thread`: solo muestra el estado y avisa los eventos.
- El ViewModel solo conoce la interface `TareasRepository`, no recibe `Context` y nunca bloquea el hilo principal.
- El trabajo pesado (`contarPrimos`) va en el Repository con `withContext(Dispatchers.Default)`.

## Observación del Paso 1

Al tocar **Tarea larga (MAL)** y enseguida **Tócame**, la pantalla se congela durante 4 segundos: el contador no suma, los botones no responden y nada se mueve. Al terminar, el contador recién suma los toques que quedaron en cola y el texto pasa directamente a **Terminó**.

**¿Por qué no se ve «Trabajando...»?** Porque `Thread.sleep(4000)` corre en el **hilo principal**, que es el mismo que dibuja la pantalla. El mensaje sí cambia a «Trabajando...», pero el hilo queda dormido y no puede volver a dibujar hasta que termina la espera. Cuando despierta, el mensaje ya es «Terminó», así que solo se ve el valor final.

**¿Qué pasaría con 10 segundos?** Android muestra el diálogo **«La aplicación no responde» (ANR)**, que ofrece cerrar la app o esperar, porque el hilo principal estuvo bloqueado más de unos 5 segundos sin atender los toques del usuario.

## Pruebas de aceptación

| Prueba | Resultado esperado | OK |
|---|---|---|
| Tarea larga: tocar Tócame mientras trabaja | El contador sigue sumando | ☐ |
| Logcat con filtro HILO | main antes y después; DefaultDispatcher-worker dentro | ☐ |
| Iniciar la tarea con progreso | La barra avanza de 0 a 100 % en ≈ 4 s | ☐ |
| Cancelar a mitad | La barra se detiene, dice Cancelado y se puede iniciar otra vez | ☐ |
| Pulsar Iniciar dos veces seguidas | No se lanzan dos tareas (el botón está deshabilitado) | ☐ |
| Primos hasta 1000 | Resultado 168 | ☐ |
| Primos con N grande | La pantalla no se congela y Cancelar funciona | ☐ |
| Una por una | ≈ 5 s y resultado correcto | ☐ |
| A la vez | ≈ 3 s y resultado correcto | ☐ |
| Error simulado + Reintentar | Recuadro rojo; al desactivar el interruptor y reintentar, éxito | ☐ |
| Rotar la pantalla durante la tarea | La tarea continúa y el progreso no se reinicia | ☐ |
| Revisar el código | `git grep` no encuentra GlobalScope, runBlocking ni Thread.sleep en el código final | ☐ |

**Valor por defecto del cálculo de primos:** se dejó en **3 000 000** porque con 500 000 el cálculo terminaba en menos de 1 s, y la guía pide que tarde entre 2 y 5 s para que se note que la pantalla no se congela. Si en otro equipo tarda más de 5 s, se puede bajar escribiendo otro número en el campo.

Valores de referencia comprobados: 100 → 25, 1000 → 168, 10 000 → 1229, 100 000 → 9592 y 500 000 → 41 538 primos.

## Para pensar

**1. ¿Por qué no se veía «Trabajando...» en el Paso 1?**
Porque `Thread.sleep` bloqueó el hilo principal, que es el que dibuja. El estado cambió a «Trabajando...», pero la pantalla no pudo redibujarse hasta que terminó la espera, y para entonces el mensaje ya era «Terminó».

**2. ¿Qué diferencia hay entre `Thread.sleep` y `delay`?**
`Thread.sleep` duerme el **hilo** completo: nadie más puede usarlo y, en el hilo principal, congela la app. `delay` **suspende solo la corrutina**: libera el hilo para que haga otras cosas (dibujar, atender toques) y la corrutina continúa después. Además, `delay` respeta la cancelación.

**3. ¿Por qué `Dispatchers.Default` para los primos y `Dispatchers.IO` para la red?**
`Default` tiene tantos hilos como núcleos del procesador y está pensado para trabajo que **usa la CPU**, como contar primos. `IO` tiene muchos más hilos porque está pensado para operaciones que pasan la mayor parte del tiempo **esperando** (red, disco, base de datos), sin ocupar la CPU.

**4. ¿Qué pasaría con la corrutina si el usuario cierra la pantalla? ¿Qué papel cumple `viewModelScope`?**
`viewModelScope` se cancela solo cuando el ViewModel se destruye (al salir de la pantalla de forma definitiva), así que las corrutinas no siguen trabajando en vano ni causan fugas de memoria. Al rotar la pantalla el ViewModel sobrevive, por eso la tarea continúa y el progreso no se reinicia.

**5. ¿Por qué `async` tarda 3 s y no 5 s? ¿Qué pasa si una de las dos cargas falla?**
Porque las dos cargas corren **al mismo tiempo**: el perfil (2 s) y los pedidos (3 s) se esperan en paralelo, así que el total es el de la más lenta. Si una falla dentro de `coroutineScope`, la excepción **cancela la otra** y se propaga; el `try/catch` del ViewModel la atrapa y muestra el error con Reintentar.
