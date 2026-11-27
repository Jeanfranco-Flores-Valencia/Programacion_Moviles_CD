# AvisosCampus — Laboratorio 15: Notificaciones en Android

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · Tecsup
**Docente:** Juan León S.

## Descripción

AvisosCampus muestra notificaciones locales y escucha lo que pasa en el celular. Con un botón muestra el aviso **Tarea lista**, con otro programa un **Recordatorio** que llega a los 10 segundos (con cuenta regresiva y opción de cancelar) y, si el usuario lo activa, avisa cuando se **activa o desactiva el modo avión**. Todo queda anotado en un **historial**. Si el usuario niega el permiso, la app no se cierra: guarda el aviso en el historial y ofrece **Abrir ajustes**. Usa solo datos ficticios y no necesita internet.

**Tecnologías:** Kotlin, Jetpack Compose + Material 3 (sin XML), NotificationCompat, PendingIntent, BroadcastReceiver, corrutinas y ViewModel. Paquete: `com.tecsup.avisoscampus`.

## Fase 2: mejora con IA (rama `mejora-ia-avisoscampus`)

Detalle de los prompts en `PROMPTS.md`.

| Mejora | Qué hace | Capas |
|---|---|---|
| Dos canales | Canal **Urgentes** con `IMPORTANCE_HIGH` (emergente) además de **Avisos**; el recordatorio usa el urgente | Notificador |
| Texto largo | Al expandir la notificación se lee el texto completo, el origen y la hora (`BigTextStyle`) | Notificador |
| Receptor del cargador | Switch **Escuchar el cargador**: avisa **Conectado** o **Desconectado** con `ACTION_POWER_CONNECTED` / `ACTION_POWER_DISCONNECTED` | Receiver + ViewModel + View |

## Estructura de paquetes

```
com.tecsup.avisoscampus/
├── AvisosApp.kt                  Application: crea el canal «Avisos» al iniciar
├── MainActivity.kt
├── model/
│   └── Modelos.kt                Aviso, Origen, PermisoUi
├── data/
│   └── AvisosRepository.kt       interface + AvisosRepositoryMemoria (historial)
├── notificacion/
│   └── Notificador.kt            canal, permiso y mostrar (NotificationCompat + PendingIntent)
├── receiver/
│   ├── ModoAvionReceiver.kt      BroadcastReceiver del modo avión
│   └── CargadorReceiver.kt       BroadcastReceiver del cargador (Fase 2)
├── viewmodel/
│   ├── AvisosViewModel.kt        permiso, avisos, recordatorio, receptor y errores
│   └── AvisosViewModelFactory.kt
└── ui/
    ├── PantallaAvisos.kt         tarjetas: permiso, avisos, recordatorio y receptor
    ├── HistorialAvisos.kt        tarjeta Historial
    └── theme/
```

## Arquitectura MVVM + Repository

```
View (PantallaAvisos)
   |  eventos: botones, switch          ^ collectAsState()
   v                                    |
ViewModel (AvisosViewModel) --- StateFlow (avisos, permiso, faltan, mensaje) --->
   |  registra el aviso         |  emite: eventos (SharedFlow<Aviso>)
   v                            v
AvisosRepository (interface)   View recibe el evento y llama a
   -> AvisosRepositoryMemoria  Notificador.mostrar(context, aviso)
```

- El **ViewModel decide** qué avisar y lo anota en el Repository; después **emite un evento**. No usa `Context`, `NotificationManager` ni `BroadcastReceiver`.
- La **pantalla** (que sí tiene `Context`) recibe el evento y llama al `Notificador`. Si no se pudo mostrar, avisa al ViewModel con `noSePudoMostrar()`.
- El **receptor** se registra en un `DisposableEffect` solo mientras el switch está activado, y se quita siempre en `onDispose`.
- El **recordatorio** corre en `viewModelScope`: sigue aunque se minimice la app o se rote la pantalla, y se cancela con su `Job`.

## Pruebas de aceptación

| Prueba | Resultado esperado | OK |
|---|---|---|
| Abrir Ajustes → Apps → AvisosCampus → Notificaciones | Existe el canal **Avisos** | ☐ |
| Pulsar **Permitir notificaciones** | Aparece el cuadro de Android y la etiqueta cambia | ☐ |
| Pulsar **Mostrar aviso** | Aparece la notificación **Tarea lista** | ☐ |
| Pulsar **Mostrar aviso** dos veces | Aparecen dos notificaciones distintas | ☐ |
| Tocar la notificación | Se abre la app y la notificación desaparece | ☐ |
| Recordar en 10 s | Cuenta regresiva y aviso al llegar a 0 | ☐ |
| Recordar y minimizar la app | El aviso llega igual | ☐ |
| Recordar y cancelar | No llega ningún aviso | ☐ |
| Switch activado y cambiar el modo avión | Aviso **Activado** y luego **Desactivado** | ☐ |
| Switch desactivado y cambiar el modo avión | No hay aviso | ☐ |
| Permiso denegado y **Mostrar aviso** | Mensaje de error, aviso en el historial y la app no se cierra | ☐ |
| Rotar la pantalla durante el recordatorio | La cuenta regresiva continúa | ☐ |
| Revisar el código | No hay `Context` en `viewmodel/` y todo receptor registrado se quita en `onDispose` | ☐ |

## Para pensar

**1. ¿Por qué una notificación necesita un canal y un permiso? ¿Qué pasa si falta cada uno?**
El **canal** (Android 8+) agrupa las notificaciones de la app y permite que el usuario decida su sonido e importancia desde Ajustes; sin canal, en Android 8 o superior la notificación simplemente no se muestra. El **permiso** `POST_NOTIFICATIONS` (Android 13+) es la autorización del usuario para que la app le envíe notificaciones; sin él tampoco aparece nada. Por eso `Notificador.mostrar` revisa el permiso y devuelve `false`, y la app muestra el mensaje con **Abrir ajustes** en vez de cerrarse.

**2. ¿Por qué el ViewModel emite un evento y no muestra la notificación él mismo?**
Porque mostrar una notificación necesita un `Context` y el `NotificationManager`, que son del sistema Android. Si el ViewModel los usara, dependería de Android, podría filtrar memoria al guardar un `Context` y no se podría probar solo. Así, el ViewModel solo **decide** qué avisar y lo anota; la pantalla, que sí tiene `Context`, se encarga de mostrarlo.

**3. ¿Qué pasa si usas el mismo id en dos notificaciones?**
La segunda **reemplaza** a la primera: en el panel queda una sola. Por eso cada notificación usa el `id` del aviso, que el Repository aumenta en uno cada vez, y al pulsar **Mostrar aviso** dos veces aparecen dos notificaciones.

**4. ¿Por qué `onReceive` debe ser corto y por qué hay que quitar el receptor al salir?**
`onReceive` corre en el hilo principal: si tarda, congela la app y Android puede cancelarlo o mostrar un ANR. Por eso solo lee el estado del modo avión y se lo pasa al ViewModel. El receptor hay que quitarlo con `unregisterReceiver` porque, si se queda registrado, el sistema mantiene una referencia a él y a la pantalla aunque ya no se use: se filtra memoria y podría seguir recibiendo anuncios.

**5. ¿En qué casos usarías una notificación local y en cuáles una push de Firebase?**
Una notificación **local** la crea la propia app con información que ya tiene: un recordatorio, el fin de una descarga o un cambio del celular como el modo avión. No necesita internet ni servidor. Una **push** de Firebase la envía un **servidor** cuando pasa algo fuera del celular: un mensaje nuevo, una nota publicada por el docente o una oferta, y llega aunque la app esté cerrada.
