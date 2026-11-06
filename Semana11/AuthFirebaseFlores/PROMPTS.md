# Prompts utilizados

Rama: `mejora-ia-authfirebase` · IA utilizada: asistente de IA (Claude), con permiso del docente.
Objetivo: mejorar la interfaz y la experiencia de uso (UI/UX) del login de la Fase 1 sin cambiar cómo se conecta con Firebase.
Condición respetada: el inicio de sesión, el registro y el cierre de sesión siguen usando `signInWithEmailAndPassword`, `createUserWithEmailAndPassword` y `signOut()` igual que en `main`.

## Prompt 1 — Estilo visual

**Texto enviado a la IA:**
> Ahora procederemos con una mejora de UI/UX. Inspírate en esta imagen (tarjeta oscura translúcida sobre una foto, pestañas Sign In / Sign Up, campos con íconos y botón claro), pero enfócalo en que la app es educativa, ya que permitiría ver mis cursos si se añadiera. Ponle transiciones y detalles que veas convenientes.
>
> Luego: no necesariamente en bosque, sino quizás bibliotecas o algo relacionado al estudio. También puedes considerar los tonos de Tecsup, ya que debe combinar en la medida de lo posible.

**Qué generó la IA:** `Theme.kt` con una paleta propia (`Paleta`) en azul marino y celeste Tecsup, y `FondoEducativo.kt`: un fondo dibujado con `Canvas` con estantes de biblioteca (lomos de libros de colores sobre tablas de madera), una luz celeste que "respira" con `rememberInfiniteTransition` e íconos de estudio (libro, birrete, foco, ciencia) flotando muy suaves. El fondo se dibuja detrás de las barras del sistema con `enableEdgeToEdge()`.

**Qué tuve que corregir yo:** la primera versión era un bosque al atardecer como la imagen; pedí cambiarla a una biblioteca y luego a los colores de Tecsup. No se usaron los botones de Google y Apple de la imagen porque no funcionarían sin configurar esos proveedores en Firebase.

**¿Qué aprendí?:** con `Canvas` se puede dibujar un fondo sin imágenes, y la "semilla" fija de `Random(11)` hace que los estantes salgan siempre iguales. Un `Canvas` que no lee estados animados no se redibuja en cada cuadro.

## Prompt 2 — Tarjeta de acceso con pestañas

**Texto enviado a la IA:** (mismo pedido) una sola tarjeta con las pestañas Iniciar sesión / Crear cuenta como en la imagen.

**Qué generó la IA:** `AuthScreen.kt` con la tarjeta translúcida, el logo con un birrete que flota, dos pestañas con un subrayado celeste que se desliza (`animateDpAsState` con `spring`) y `AnimatedContent` que desliza el formulario hacia el lado de la pestaña elegida. `LoginScreen` y `RegisterScreen` quedaron como los formularios dentro de la tarjeta. En `AuthComponentes.kt`: campos con íconos, ojo para ver/ocultar la contraseña, botón que se encoge al presionarlo y muestra un spinner, errores dentro de la tarjeta (en lugar de `Toast`) que la sacuden con `keyframes`, indicador de seguridad de la contraseña y un check verde cuando la confirmación coincide.

**Qué tuve que corregir yo:** el primer build falló porque `maxHeight` de `BoxWithConstraints` no se podía leer dentro del `Column` hijo; se guardó en una variable `altoPantalla` antes. La función `sacudir()` necesitaba el tipo exacto `Animatable<Float, AnimationVector1D>` para aceptar `keyframes`. La ruta `register` se quitó porque ahora el registro es una pestaña.

**¿Qué aprendí?:** `AnimatedContent` con `SizeTransform` anima el cambio de alto de la tarjeta entre los dos formularios, y los `ImeAction` (Siguiente / Listo) permiten pasar de campo en campo y enviar desde el teclado.

## Prompt 3 — Recuperar contraseña

**Qué generó la IA:** el enlace "¿Olvidaste tu contraseña?" usa `sendPasswordResetEmail` de Firebase y muestra un aviso verde.

**Qué tuve que corregir yo:** el mensaje dice "Si el correo tiene una cuenta, te llegará un enlace", porque Firebase (con la protección contra enumeración de correos) responde con éxito aunque el correo no exista.

## Prompt 4 — Bienvenida y transiciones

**Qué generó la IA:** `HomeScreen` con saludo según la hora (`LocalTime`), avatar con la inicial del correo, la sesión iniciada, un consejo de estudio distinto cada día (`dayOfYear`), un espacio "Mis cursos — Pronto" y un `AlertDialog` para confirmar el cierre de sesión. Las tarjetas aparecen en cascada y `NavHost` tiene transiciones de entrada y salida.

**Qué tuve que corregir yo:** la inicial del avatar usa `firstOrNull()` para no fallar si el correo viniera vacío.
