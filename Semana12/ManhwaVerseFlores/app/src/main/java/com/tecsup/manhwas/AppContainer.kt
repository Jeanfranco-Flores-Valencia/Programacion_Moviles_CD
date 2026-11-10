package com.tecsup.manhwas

import android.content.Context
import com.tecsup.manhwas.data.auth.AuthRepository
import com.tecsup.manhwas.data.auth.FirebaseAuthRepository
import com.tecsup.manhwas.data.local.AppDatabase
import com.tecsup.manhwas.data.remote.RetrofitClient
import com.tecsup.manhwas.data.repository.ItemRepository
import com.tecsup.manhwas.data.repository.ItemRepositoryImpl
import com.tecsup.manhwas.notification.Notificador
import com.tecsup.manhwas.notification.NotificadorAndroid

/** Crea la API, la base de datos y los repositorios una sola vez. Los ViewModels solo ven las interfaces. */
class AppContainer(context: Context) {
    private val db = AppDatabase.obtener(context)

    val itemRepository: ItemRepository = ItemRepositoryImpl(RetrofitClient.api, db.itemDao())
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val notificador: Notificador = NotificadorAndroid(context.applicationContext)
}
