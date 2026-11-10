// Archivo de build principal (nivel proyecto): solo se declaran los plugins con apply false
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
