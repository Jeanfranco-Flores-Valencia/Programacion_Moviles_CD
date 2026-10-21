# LavaCampus — Laboratorio 08: MVVM + Room

**Estudiante:** Jeanfranco Flores Valencia
**Curso:** Programación en Móviles · 4to ciclo · Tecsup
**Docente:** Juan José León Suiyon

## Descripción

LavaCampus es la app de la lavandería de un campus universitario, con dos perfiles en el mismo dispositivo:

- **Estudiante:** arma un pedido eligiendo servicios y cantidades (kilos o prendas), ve el total y la fecha estimada de entrega (sin domingos), sigue el estado de su ropa y la recoge en un casillero.
- **Administrador:** recibe los pedidos, los avanza de estado (Recibido → En proceso → Listo → Entregado), asigna el casillero, cobra, administra las tarifas y consulta a sus clientes.

Ambos perfiles comparten la misma base de datos Room: cuando el administrador marca un pedido como **Listo**, el estudiante lo ve con su casillero al instante (las pantallas observan `Flow` de Room).

**Flujo de entrada:** el Splash no avanza solo; espera a que el usuario toque **Comenzar** o **Ya tengo una cuenta**. Si ya hay un perfil guardado, lo lleva directo a su pantalla principal; si no, al inicio de sesión.

**Tecnologías:** Kotlin, Jetpack Compose + Material 3 (sin XML), Room 2.6.1 (KSP), DataStore Preferences, Navigation Compose, `java.time` (minSdk 26).

**Datos de prueba (ficticios, Ley 29733):**

| Perfil | Correo | Clave |
|---|---|---|
| Estudiante | ana@correo.test · luis@correo.test · marta@correo.test | 1234 |
| Administrador | admin@lavacampus.test | admin |

## Arquitectura MVVM + Repository

```
VIEW (Composable)
   | eventos (clics)          ▲ collectAsState()
   ▼                          |
VIEWMODEL (StateFlow + reglas de negocio)
   | llama                    ▲ Flow / resultados
   ▼                          |
LavaRepository (interface)
   | la implementa
   ▼
LavaRepositoryRoom ──► DAO ──► SQLite (lavacampus.db)

DataStore (solo preferencias) ──► ViewModel
```

| Capa | Conoce a | NO debe |
|---|---|---|
| View (`ui/`) | Solo a su ViewModel | Usar DAO, Repository ni reglas de negocio |
| ViewModel | Solo al Repository (interface) y DataStore | Conocer Composables ni el DAO |
| Repository | Los DAOs (en la implementación) | Conocer pantallas ni ViewModels |
| DAO / Room | SQL y entidades | Conocer ViewModels |

**Regla de oro:** lectura que debe actualizar la pantalla → `Flow` (sin suspend). Escritura o acción única → `suspend`.

## Base de datos (4 tablas)

- `usuarios` — id, nombre, codigo, correo, clave, rol (CLIENTE | ADMIN)
- `servicios` — id, nombre, descripcion, unidad (KG | PRENDA | UNIDAD), precio, horasEntrega, activo, icono
- `pedidos` — id, usuarioId (FK), fechaRegistro, fechaEntrega, estado, total, casillero, pagado, nota, fechaCreacion
- `detalles_pedido` — id, pedidoId (FK, CASCADE), servicioId (FK, RESTRICT), cantidad, precioUnitario, subtotal

Relaciones: `PedidoConDetalle` (@Embedded Pedido + @Relation Usuario + @Relation anidada a `List<DetalleConServicio>`).

## Árbol de paquetes

```
com.floresvalencia.lavacampus
├── MainActivity.kt
├── LavaApp.kt                      crea la base, el repository y las preferencias; siembra datos
├── data
│   ├── local
│   │   ├── entity      Servicio, Usuario, Pedido, DetallePedido
│   │   ├── dao         ServicioDao, UsuarioDao, PedidoDao, DetalleDao
│   │   ├── relation    DetalleConServicio, PedidoConDetalle
│   │   ├── LavaDatabase.kt         singleton "lavacampus.db"
│   │   └── DatosIniciales.kt       datos de prueba (solo la primera vez)
│   ├── repository
│   │   ├── LavaRepository.kt       interface (el contrato)
│   │   └── LavaRepositoryRoom.kt   implementación con Room (transacción en crearPedido)
│   └── prefs
│       └── PreferenciasStore.kt    DataStore: perfil, usuario, modo oscuro, avisos, recibir pedidos
├── model           Rol, EstadoPedido, Unidad, LineaPedido, IngresoServicio, ClienteResumen, Casilleros, ServicioTexto
├── viewmodel       LavaViewModelFactory, Sesion, Inicio, NuevoPedido, MisPedidos, DetallePedido,
│                   Panel, Bandeja, GestionPedido, Servicios, Clientes
└── ui
    ├── theme       Color, Type, Theme (LavaTheme(rol, oscuro))
    ├── components  AppTopBar, LavaButton, LavaTextField, EstadoPedidoChip, PedidoCard,
    │               StepperEstado (Canvas), IconoServicio (Canvas), SelectorCantidad, Formatos
    │   └── graficos  DatoGrafico, GraficoDona, GraficoBarras
    ├── navigation  Rutas, NavLava (un grafo por perfil + NavigationBar)
    ├── estudiante  Splash, Login, Inicio, NuevoPedido, DetallePedido, MisPedidos, Tarifas, Perfil
    └── administrador  Panel, Bandeja, GestionPedido, Servicios, FormularioServicio, Clientes, Ajustes
```

## Reglas de negocio (viven en los ViewModels)

- El estudiante solo ve **sus** pedidos; el administrador ve todos.
- Nuevo pedido: KG de 1.0 a 10.0 en pasos de 0.5; PRENDA/UNIDAD de 1 a 30. Solo servicios activos.
- Máximo **3 pedidos activos** por estudiante.
- `subtotal = cantidad × precio`; `total = Σ subtotales`. Precios congelados en el detalle.
- Entrega = hoy + mayor tiempo de entrega (24 h = 1 día). Si cae domingo, pasa al lunes.
- El pedido y sus líneas se guardan en **una transacción** (`db.withTransaction`).
- Estudiante cancela solo en RECIBIDO; administrador en RECIBIDO o EN_PROCESO.
- Estados: solo se avanza un paso. LISTO exige casillero libre (C-01 a C-30); ENTREGADO exige pagado.
- Un servicio usado en algún pedido no se elimina (solo se desactiva).

## Mejoras con IA (rama `mejora-ia-lavacampus`)

- **Pedidos atrasados:** marca roja en la bandeja y alerta en el panel.
- **Descuento por volumen:** 10 % del lavado cuando supera 5 kg; se ve en el resumen y se guarda en el pedido (base versión 2).
- **Repetir un pedido:** "Pedir de nuevo" desde el historial con los precios actuales.
- **Registro de estudiantes:** "Comenzar" lleva a crear la cuenta; al registrarse va al login con el correo ya escrito. Validaciones en `SesionViewModel`: nombre y apellido solo con letras, código `U` + 9 dígitos, correo válido, contraseña de 6+ caracteres con letras y números, confirmación igual y correo/código no repetidos.

## Pruebas de aceptación

| Prueba | Resultado esperado | OK |
|---|---|---|
| Splash | No avanza hasta tocar Comenzar o Ya tengo una cuenta | ☐ |
| Login con clave incorrecta | Mensaje de error, no entra | ☐ |
| Estudiante: nuevo pedido sin servicios | El botón Confirmar está apagado | ☐ |
| Estudiante: 3.5 kg de lavado + 5 prendas de planchado | Total S/ 40.50 y entrega a 2 días (saltando domingo) | ☐ |
| Estudiante: crear un cuarto pedido activo | Mensaje de límite y no se crea | ☐ |
| Estudiante: cancelar un pedido EN_PROCESO | No se permite cancelar | ☐ |
| Administrador: pasar a Listo sin casillero | Se rechaza con un mensaje | ☐ |
| Administrador: elegir un casillero ocupado | Aparece gris y no se puede elegir | ☐ |
| Administrador: entregar un pedido sin cobrar | Se rechaza; al marcar Cobrado sí se entrega | ☐ |
| Estudiante: ver el cambio de estado | El seguimiento se actualiza sin cerrar la pantalla | ☐ |
| Administrador: ingresos de la semana | Coinciden con la suma de los pedidos pagados | ☐ |
| Administrador: eliminar un servicio usado | No se elimina; se muestra un mensaje | ☐ |
| Cerrar y abrir la app | Los pedidos siguen ahí y el perfil y el modo oscuro se recuerdan | ☐ |

## Para pensar

**1. ¿Por qué un pedido tiene sus detalles en otra tabla?**
Porque un pedido puede tener varios servicios (lavado y planchado, por ejemplo). Guardar cada línea como una fila de `detalles_pedido` evita columnas repetidas (servicio1, servicio2...) y permite consultar el ingreso por servicio con un `GROUP BY`. Es la relación uno a muchos más común.

**2. ¿Por qué guardar `precioUnitario` y `subtotal` en el detalle si el servicio ya tiene precio?**
Es una "foto" del momento de la compra. Si mañana la lavandería sube el precio del kilo, los pedidos antiguos conservan lo que costaron y los ingresos del panel no cambian.

**3. ¿Por qué `LavaRepository` es una interface?**
Porque los ViewModels dependen del contrato y no de Room. En la Semana 9 se podrá crear otra implementación (servicio web o nube) sin tocar ViewModels ni pantallas.

**4. ¿Cuándo uso `Flow` y cuándo `suspend`?**
`Flow` para lecturas que deben actualizar la pantalla sola (por eso el estudiante ve el cambio de estado al instante). `suspend` para escrituras o consultas de una sola vez (crear pedido, contar activos, login). No se reemplazan, se complementan.

**5. ¿Para qué sirve la transacción en `crearPedido`?**
Para que el pedido y todas sus líneas se guarden juntos. Si falla el insert de un detalle, se deshace también el del pedido y no queda un pedido sin detalles.

**6. ¿Por qué las reglas de negocio están en el ViewModel y no en el Composable ni en el DAO?**
El Composable solo dibuja y avisa eventos; el DAO solo ejecuta SQL. El ViewModel sobrevive a la rotación, se puede probar sin interfaz y es el único lugar donde se decide si una acción está permitida (límite de pedidos, casillero libre, cobro previo).

**7. ¿Qué va en DataStore y qué va en Room?**
DataStore guarda preferencias pequeñas clave-valor (perfil activo, modo oscuro, avisos, recibir pedidos). Los usuarios, servicios y pedidos son datos relacionados y consultables: van en Room.

## Cómo ejecutar

1. Abrir la carpeta `Semana08/LavaCampus` en Android Studio y sincronizar Gradle.
2. Ejecutar en un emulador con API 26 o superior.
3. Si se cambia una entidad después de ejecutar, desinstalar la app o subir la versión de la base.
4. Base de datos: View > Tool Windows > App Inspection > Database Inspector.
