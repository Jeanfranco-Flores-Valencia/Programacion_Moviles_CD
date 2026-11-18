import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// La clave de Maps se lee de local.properties (si existe); asi no queda en el codigo ni en git
val localProps = Properties().apply {
    val archivo = rootProject.file("local.properties")
    if (archivo.exists()) archivo.inputStream().use { load(it) }
}

android {
    namespace = "com.floresv.campusgps"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.floresv.campusgps"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // Llega al Manifest como ${MAPS_API_KEY}
        manifestPlaceholders["MAPS_API_KEY"] = localProps.getProperty("MAPS_API_KEY", "")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation(platform("androidx.compose:compose-bom:2025.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    // Mejora IA: iconos de categoria (libro, cubiertos, evento, estacionamiento) y de ubicacion
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Ubicacion, corrutinas con Play Services (await) y mapa en Compose
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")
    implementation("com.google.maps.android:maps-compose:8.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Mejora IA: guardar el ultimo lugar visitado
    implementation("androidx.datastore:datastore-preferences:1.1.7")
}
