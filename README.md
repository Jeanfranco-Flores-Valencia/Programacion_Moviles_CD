# Programacion_Moviles_CD
# Laboratorio 02: Carrito de Compras en Kotlin
**Estudiante:** Jeanfranco Flores Valencia

**Curso:** Programación en Móviles  
**Ciclo:** 4to Ciclo  

## ¿De qué trata el proyecto?
Es un programa básico en consola que simula un carrito de compras. Registra los productos que compra un cliente, calcula el subtotal, le suma el IGV (18%), saca el total y aplica un descuento si la compra es alta.

### Funciones que usé:
* mostrarDetalle: Muestra la lista de productos ordenados en columnas.
* calcularSubtotal: Suma los precios multiplicados por su cantidad.
* calcularIGV: Saca el 18% del subtotal.
* calcularTotal: Suma el subtotal con el IGV.
* calcularDescuento: Aplica 5% o 10% de descuento usando "when" según cuánto gaste el cliente.

---

## Respuesta sobre val y var

**¿Por qué "nombre" y "precio" son "val" y "cantidad" es "var"?**
* **"val":** Se usa para cosas que no van a cambiar. El nombre y el precio del producto se quedan fijos.
* **"var":** Se usa para cosas que sí cambian. La cantidad de productos puede subir o bajar mientras el cliente compra.

**¿Qué pasa si intento cambiar el precio?**
El programa no te deja compilar y te da un error diciendo que a un "val" no se le puede cambiar el valor.

---

## Resultado en consola
<img width="709" height="543" alt="Captura de pantalla 2026-08-28 023651" src="https://github.com/user-attachments/assets/2420d007-e55a-46ec-8175-b8342d19cd68" />
