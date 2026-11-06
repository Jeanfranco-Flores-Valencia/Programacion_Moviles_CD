// Archivo de build principal (nivel proyecto)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Plugin de Google Services para Firebase (version aqui, se aplica en el modulo app)
    id("com.google.gms.google-services") version "4.4.2" apply false
}
