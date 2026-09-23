package com.floresvalencia.navlab.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SessionManager {
    var currentUser by mutableStateOf<Student?>(null)
        private set

    fun login(email: String, password: String): Boolean {
        val student = StudentRepository.findByCredentials(email, password)
        if (student != null) {
            currentUser = student
            return true
        }
        return false
    }

    fun logout() {
        currentUser = null
    }
}
