package com.floresvalencia.navlab.data

import androidx.annotation.DrawableRes

data class Student(
    val id: Int,
    val name: String,          // nombre corto, ej. "Juan León"
    val fullName: String,      // ej. "Juan León Suiyon"
    val career: String,
    val cycle: String,
    val studentCode: String,
    val email: String,         // correo institucional, también es el usuario de login
    val password: String,
    val phone: String,
    val faculty: String,
    val bio: String,
    @field:DrawableRes val photo: Int? = null
)
