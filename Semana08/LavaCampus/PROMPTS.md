# Prompts utilizados

Rama: `mejora-ia-lavacampus` · IA utilizada: asistente de IA (Claude), con permiso del docente por falta de tiempo.
Condición respetada: la View no usa DAO ni Repository; el ViewModel solo conoce la interface `LavaRepository`;
toda función nueva se declaró primero en la interface y, al cambiar la entidad `Pedido`, se subió la versión de la base.

## Prompt 1 — Pedidos atrasados

**Texto enviado a la IA:**
> Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un LavaRepository (interface). Agrega en la bandeja del administrador una marca roja para los pedidos atrasados (fechaEntrega menor que hoy y estado distinto de ENTREGADO y CANCELADO). La consulta va en PedidoDao, la función en LavaRepository y su implementación, y el valor se expone como StateFlow en BandejaViewModel. El panel también debe contarlos. La View solo lo muestra. No uses XML ni librerías externas. Muéstrame solo los archivos que cambian.

**Qué generó la IA:** `PedidoDao.observarAtrasados(hoy): Flow<List<Int>>`, `observarPedidosAtrasados` en la interface y en `LavaRepositoryRoom`, `atrasados: StateFlow<Set<Int>>` en `BandejaViewModel`, `atrasados: StateFlow<Int>` en `PanelViewModel`, borde rojo + texto "ATRASADO" en la tarjeta de la bandeja y una alerta roja en el panel.

**Qué tuve que corregir yo:** revisé que la comparación de fechas funcione como texto: sirve porque las fechas se guardan como `aaaa-MM-dd` (orden lexicográfico = orden cronológico). Comprobé que el pedido LISTO de Marta de hace 2 días aparece como atrasado.

**¿Respeta MVVM?:** Sí. SQL en el DAO, delegación en el Repository, el cálculo de "hoy" y el StateFlow en el ViewModel; la pantalla solo pinta el borde.

## Prompt 2 — Descuento por volumen

**Texto enviado a la IA:**
> En la misma app, si el lavado por kilo supera 5 kg aplica 10 % de descuento sobre las líneas en KG. El descuento debe verse en el resumen de Nuevo pedido, guardarse en el pedido (nueva columna descuento en la entidad Pedido) y mostrarse en el detalle. La regla va en NuevoPedidoViewModel; el Repository solo guarda el valor dentro de la misma transacción. Recuerda subir la versión de la base.

**Qué generó la IA:** columna `descuento` en `Pedido`, versión de la base 2 con `fallbackToDestructiveMigration()`, parámetro `descuento` en `crearPedido` (interface + Room), `subtotal`, `descuento` y `total` como StateFlow en el ViewModel con la función `calcularDescuento`, y la línea verde de descuento en Nuevo pedido y en Detalle.

**Qué tuve que corregir yo:** verifiqué que el total guardado sea `subtotal - descuento` y que el descuento se redondee a céntimos; confirmé que con exactamente 5 kg no hay descuento ("supera" = mayor que 5). Al subir la versión, la base de práctica se recrea y se vuelven a sembrar los datos.

**¿Respeta MVVM?:** Sí. La regla (umbral y porcentaje) vive en el ViewModel; el Repository no decide nada, solo guarda.

## Prompt 3 — Repetir un pedido

**Texto enviado a la IA:**
> En Mis pedidos > Historial agrega un botón "Pedir de nuevo" que abra Nuevo pedido con las mismas líneas del pedido elegido, pero con los precios actuales. Si un servicio ya no está activo, omítelo y muestra un aviso. Usa un argumento opcional en la ruta de navegación; la carga de las líneas va en NuevoPedidoViewModel usando solo LavaRepository.

**Qué generó la IA:** ruta `nuevo_pedido?repetir={repetir}` con valor por defecto 0 y la función `Rutas.nuevoPedido(id)`, parámetro `repetirId` en `NuevoPedidoViewModel` que carga las cantidades con `observarPedido(id).first()` y los servicios activos, y el enlace "Pedir de nuevo" en las tarjetas del historial.

**Qué tuve que corregir yo:** al cambiar la constante de la ruta había que reemplazar todas las llamadas `navigate(Rutas.NUEVO_PEDIDO)` por `navigate(Rutas.nuevoPedido())`, porque navegar al patrón con llaves falla. También se limitan las cantidades al máximo permitido (10 kg / 30 prendas).

**¿Respeta MVVM?:** Sí. La pantalla solo avisa el clic; el ViewModel carga las líneas desde el Repository y el total se recalcula con los precios de hoy.

## Prompt 4 — Registro de estudiantes

**Texto enviado a la IA:**
> Agrega un registro de estudiantes. El Splash debe esperar a que el usuario toque "Comenzar" o "Ya tengo una cuenta". "Comenzar" abre el registro; al registrarse debe llevar al inicio de sesión para que ingrese. Valida todos los campos (nombre y apellido, código U + 9 dígitos, correo, contraseña con letras y números y su confirmación) y que el correo y el código no estén repetidos. Las validaciones van en el ViewModel; las funciones nuevas primero en LavaRepository; la consulta en UsuarioDao.

**Qué generó la IA:** `RegistroScreen` con un error debajo de cada campo, `registrar()` y `ErroresRegistro` en `SesionViewModel`, `insertar`, `contarPorCorreo` y `contarPorCodigo` en `UsuarioDao`, `registrarEstudiante`, `correoRegistrado` y `codigoRegistrado` en la interface y en Room, la ruta `registro`, y el login que recibe el correo ya escrito con el aviso "¡Cuenta creada!".

**Qué tuve que corregir yo:** el registro siempre crea un usuario con rol CLIENTE (nadie puede registrarse como administrador). Revisé que al volver al Splash con "atrás" los botones sigan funcionando (se reinicia el destino elegido) y que, si ya hay un perfil guardado, los botones lleven directo a su pantalla principal.

**¿Respeta MVVM?:** Sí. La pantalla solo muestra los campos y errores; las reglas están en el ViewModel y la base solo guarda.

## Prompt 5 — Navegación más cómoda

**Texto enviado a la IA:**
> Mejora la navegación sin cambiar colores ni tipografías: insignias con la cantidad de pedidos en la barra inferior, campana con insignia que lleve a los pedidos, conteo en cada chip de la bandeja y que al tocar un cliente se vean sus pedidos. Los datos deben venir de StateFlow en los ViewModels.

**Qué generó la IA:** `insigniaPedidos` en `SesionViewModel` (pedidos activos del estudiante o recibidos para el administrador) con `BadgedBox` en la barra, `CampanaAvisos` en Inicio y Panel, `conteos` por filtro en `BandejaViewModel` y la ruta `pedidos_cliente/{nombre}` que reutiliza la bandeja con el buscador lleno.

**Qué tuve que corregir yo:** el nombre del cliente se codifica con `Uri.encode` porque tiene espacios y tildes; la insignia usa `flatMapLatest` para cambiar de consulta cuando se cambia de perfil.

**¿Respeta MVVM?:** Sí. Los conteos se calculan en los ViewModels a partir de Flows del Repository.

## Prompt 6 — Usabilidad en formularios y pedidos

**Texto enviado a la IA:**
> Sin cambiar el estilo: agrega el ojo para ver u ocultar la contraseña, que el teclado pase al siguiente campo y que "Listo" envíe el formulario; en Nuevo pedido deja fija abajo una barra con el total y el botón Confirmar; en Gestionar pedido un atajo para elegir el primer casillero libre; en el detalle de un pedido terminado un botón "Pedir de nuevo"; y en Mis pedidos vacío un acceso para hacer un pedido.

**Qué generó la IA:** parámetros `imeAction` y `onAccion` y el ícono de visibilidad en `LavaTextField`, la barra fija en `NuevoPedidoScreen`, `primerLibre` y `usarPrimerLibre()` en `GestionPedidoViewModel`, y los botones en Detalle y Mis pedidos.

**Qué tuve que corregir yo:** el atajo de casillero solo aparece cuando el pedido está EN_PROCESO y aún no hay casillero elegido; el contenido de Nuevo pedido usa `weight(1f)` para que la barra fija no lo tape.

**¿Respeta MVVM?:** Sí. La lógica del casillero libre está en el ViewModel; las pantallas solo muestran.

## Reflexión (5 líneas)

- La IA aceleró los cambios repetitivos que cruzan capas (DAO → Repository → ViewModel → View) sin romper el patrón.
- No reemplazó la revisión: hubo que comprobar fechas como texto, redondeos y la navegación con argumentos.
- El error más fácil de cometer fue dejar llamadas a la ruta antigua después de agregar un argumento opcional.
- Aprendí que una interface de Repository permite agregar funciones sin tocar las pantallas, y que cambiar una entidad obliga a subir la versión de Room.
- Las reglas de negocio (umbral de descuento, atrasos, límites) son más fáciles de cambiar cuando viven solo en el ViewModel.
