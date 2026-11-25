package com.tecsup.murocampus.model

import kotlinx.serialization.Serializable

/** Ejemplo resuelto: usuario del servicio (GET users). */
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

/** Una publicacion (GET posts). Los nombres son iguales a los del JSON. */
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

/** Un comentario de una publicacion (GET posts/{id}/comments). */
@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

/** Lo que enviamos al crear (POST posts): no lleva id, el servicio lo asigna. */
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)
