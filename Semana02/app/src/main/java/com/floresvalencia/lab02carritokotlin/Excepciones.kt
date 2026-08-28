package com.floresvalencia.lab02carritokotlin

sealed class CarritoException(mensaje: String) : Exception(mensaje) {
    class PrecioInvalidoException(precio: Double) :
        CarritoException("El precio S/ %.2f no es valido. Debe ser mayor o igual a 0.".format(precio))
    class CantidadInvalidaException(cantidad: Int) :
        CarritoException("La cantidad %d debe ser estrictamente mayor a 0.".format(cantidad))
    class CarritoVacioException :
        CarritoException("No se pueden calcular reportes sobre un carrito vacio.")
}
