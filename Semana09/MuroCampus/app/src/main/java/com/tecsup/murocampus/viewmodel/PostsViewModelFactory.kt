package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient

/** Crea el PostsViewModel con su Repository. Aqui (y no en el ViewModel) se conoce Retrofit. */
class PostsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PostsViewModel(PostRepositoryRemote(RetrofitClient.api)) as T
}
