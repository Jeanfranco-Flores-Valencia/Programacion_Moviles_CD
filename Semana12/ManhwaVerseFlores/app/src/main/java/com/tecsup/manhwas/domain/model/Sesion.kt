package com.tecsup.manhwas.domain.model

/** El rol se lee de Firestore (usuarios/{uid}, campo rol); la app no lo decide. */
enum class Rol { ADMIN, USUARIO }

data class Sesion(val uid: String, val correo: String, val rol: Rol)
