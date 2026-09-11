# Lab 03 - Registro de Producto

**Curso:** Diseño y Desarrollo de Software - Programación en Móviles
**Ciclo:** 4to Ciclo
**Alumno:** Flores Valencia Jeanfranco
**Docente:** Juan José León Suiyon

## Descripción

Aplicación desarrollada con Jetpack Compose que permite registrar un producto
mediante un formulario con tres campos: nombre, precio y cantidad. Al presionar
el botón **AGREGAR PRODUCTO**, se muestra una `Card` con el resumen de los datos
ingresados y el importe calculado (precio × cantidad, con 2 decimales).

La pantalla gestiona su estado con `remember` y `mutableStateOf`, de modo que
Compose redibuja automáticamente la interfaz cuando el usuario escribe en los
campos o presiona el botón.

## Conceptos aplicados

- `@Composable`, `Column`, `Row`, `Modifier`
- `OutlinedTextField` como control de ingreso
- `Button` como control de acción
- `Card` y `Text` como controles de visualización
- `remember` + `mutableStateOf` para el estado de la pantalla
- `toDoubleOrNull`, `toIntOrNull`, operador Elvis (`?:`) y `String.format("%.2f")`
## Capturas

### Pantalla inicial (formulario vacío)
![Pantalla inicial](capturas/pantalla_inicial.png)
### Después de presionar AGREGAR PRODUCTO
![Pantalla con resumen](capturas/pantalla_resumen.png)
## Reflexión: ¿qué pasa si declaras las variables SIN `remember`?

Sin remember, cada recomposición reinicia la variable desde cero.
Mientras escribes, el campo parece funcionar porque el cambio de estado fuerza a la pantalla a dibujarse otra vez. Pero en cuanto ocurre una recomposición completa (por ejemplo, al girar la pantalla o al actualizarse el componente padre), Compose vuelve a leer la línea mutableStateOf("") como si fuera la primera vez y borra todo lo que habías escrito.
# En resumen:

- Sin remember: El valor se borra y vuelve al inicio ("") cada vez que la UI se recompone.
- Con remember: Le dices a Compose: "guarda este valor en memoria y no lo reinicies al redibujar la pantalla".
## Estructura del proyecto

```
Semana03/
└── Lab03RegistroProducto/
    ├── app/
    │   └── src/main/java/.../MainActivity.kt
    ├── capturas/
    └── README.md
```
## Mejora con IA

Rama: `rama-ia`. IA utilizada: Claude  

| Prompt que usé | Qué generó la IA | Qué acepté o corregí (y por qué) |
|---|---|---|
| "En PantallaRegistro (MainActivity.kt), agrega validación: si nombre, precio o cantidad están vacíos al presionar AGREGAR, muestra un Text en rojo con un mensaje de error en vez de la Card. Agrega un botón Limpiar al lado de Agregar que vacíe los tres campos y oculte la Card/error. No toques el resto del diseño." | Agregó el estado `mensajeError` (String), validación con `isEmpty()` para detectar campos vacíos y con `toDoubleOrNull()`/`toIntOrNull()` para detectar valores no numéricos, un botón "LIMPIAR" junto al de agregar, y un `Text` en rojo fijo (`Color.Red`) que se muestra cuando hay error en vez de la Card. Sin embargo, anidó el `Spacer`, el `Text` de error y la `Card` dentro del mismo `Row` que contenía los botones. | **Acepté:** la estructura general (validar antes de mostrar la Card, botón Limpiar independiente, uso de `mensajeError` como bandera de error). **Corregí:** (1) `isEmpty()` → `isBlank()`, porque un campo con solo espacios (" ") pasaba como válido; (2) `Color.Red` fijo → `MaterialTheme.colorScheme.error`, para respetar el tema de Material Design en vez de un color hardcodeado; (3) mensaje de error genérico → mensajes específicos según qué falló (campo vacío, precio no numérico, cantidad no numérica), para que el usuario sepa exactamente qué corregir; (4) `mensajeError` de `String` vacío a `String?` (nullable), porque en Kotlin es más idiomático representar "sin error" como `null` que como cadena vacía, y permite usar `?.let {}` de forma segura; (5) agregué validación de que precio y cantidad deben ser mayores a cero, ya que la IA no contempló que se pudieran ingresar valores negativos o en cero. |

### Casos de prueba verificados
- Campos vacíos → muestra mensaje de error, no muestra la Card.
- Campo con solo espacios en blanco → detectado como vacío (gracias a `isBlank()`).
- Precio o cantidad con letras → mensaje de error específico indicando el campo inválido.
- Precio o cantidad negativos o en cero → mensaje de error específico.
- Botón "LIMPIAR" → vacía los tres campos y oculta tanto el error como la Card.
- Datos válidos → oculta el error y muestra la Card con el resumen correcto.