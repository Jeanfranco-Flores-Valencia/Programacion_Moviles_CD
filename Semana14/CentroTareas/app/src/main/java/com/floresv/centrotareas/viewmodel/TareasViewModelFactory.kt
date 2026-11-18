package com.floresv.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.floresv.centrotareas.data.TareasRepositoryLocal

/** Crea el TareasViewModel con su Repository. */
class TareasViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TareasViewModel(TareasRepositoryLocal()) as T
}
