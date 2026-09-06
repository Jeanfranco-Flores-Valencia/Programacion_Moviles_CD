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