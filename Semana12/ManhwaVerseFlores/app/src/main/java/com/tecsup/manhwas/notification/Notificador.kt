package com.tecsup.manhwas.notification

/** El ViewModel avisa por esta interface y asi no necesita Context. */
interface Notificador {
    fun mostrar(titulo: String, texto: String)
}
