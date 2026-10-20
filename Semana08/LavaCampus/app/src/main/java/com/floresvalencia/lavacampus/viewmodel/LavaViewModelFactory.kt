package com.floresvalencia.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Fabrica generica: recibe una funcion que crea el ViewModel con sus dependencias
 * (Repository, preferencias, id del usuario...).
 * Ejemplo: LavaViewModelFactory { ServiciosViewModel(app.repo) }
 */
class LavaViewModelFactory(
    private val crear: () -> ViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = crear() as T
}
