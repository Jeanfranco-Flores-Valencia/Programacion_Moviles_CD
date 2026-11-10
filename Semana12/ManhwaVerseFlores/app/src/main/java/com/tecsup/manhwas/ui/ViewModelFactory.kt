package com.tecsup.manhwas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.manhwas.AppContainer
import com.tecsup.manhwas.ui.admin.AdminViewModel
import com.tecsup.manhwas.ui.detalle.DetalleViewModel
import com.tecsup.manhwas.ui.favoritos.FavoritosViewModel
import com.tecsup.manhwas.ui.lista.ListaViewModel
import com.tecsup.manhwas.ui.login.LoginViewModel

/** Crea cada ViewModel con lo que necesita del AppContainer. itemId solo lo usa el detalle. */
class ViewModelFactory(
    private val container: AppContainer,
    private val itemId: Int = 0
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(container.authRepository)
        modelClass.isAssignableFrom(ListaViewModel::class.java) -> ListaViewModel(container.itemRepository, container.notificador)
        modelClass.isAssignableFrom(DetalleViewModel::class.java) -> DetalleViewModel(container.itemRepository, itemId)
        modelClass.isAssignableFrom(FavoritosViewModel::class.java) -> FavoritosViewModel(container.itemRepository)
        modelClass.isAssignableFrom(AdminViewModel::class.java) -> AdminViewModel(container.itemRepository, container.notificador)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}
