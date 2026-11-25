package com.tecsup.murocampus.model

/** Ejemplo resuelto: usuario del servicio (GET users). */
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

// TODO Paso 2: Post(userId, id, title, body), Comentario(postId, id, name, email, body) y NuevoPost(userId, title, body)
