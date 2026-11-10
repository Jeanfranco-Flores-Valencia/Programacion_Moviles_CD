package com.tecsup.manhwas.data.auth

import com.tecsup.manhwas.domain.model.Sesion

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>
    suspend fun sesionActual(): Sesion?      // null si no hay sesion
    fun cerrarSesion()
}
