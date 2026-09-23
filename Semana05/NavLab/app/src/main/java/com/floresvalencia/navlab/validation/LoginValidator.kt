package com.floresvalencia.navlab.validation

import android.util.Patterns

data class ValidationResult(
    val isValid: Boolean,
    val emailError: String? = null,
    val passwordError: String? = null
)

object LoginValidator {
    fun validate(email: String, password: String): ValidationResult {
        val cleanEmail = email.trim()
        val emailError = when {
            cleanEmail.isEmpty() -> "Ingresa tu correo"
            !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> "Formato de correo inválido"
            else -> null
        }

        val passwordError = when {
            password.isEmpty() -> "Ingresa tu contraseña"
            else -> null
        }

        val isValid = emailError == null && passwordError == null
        return ValidationResult(isValid, emailError, passwordError)
    }
}
