plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // Kotlin 2.0+
    id("kotlin-kapt")
}

android {
    namespace = "com.faris165.mobilecomponentslab" // sesuaikan dgn package
    compileSdk = 36

    defaultConfig {
        applicationId = "com.faris165.mobilecomponentslab"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures { compose = true }

    // ✅ Samakan Java utk Java & Kotlin → 21
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

// ✅ Kotlin toolchain → 21 biar match dengan di atas
kotlin {
    jvmToolchain(21)
}

dependencies {
    // Compose BOM (atur versi komponen Compose)
    val composeBom = platform("androidx.compose:compose-bom:2025.01.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose core + Material3
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Activity Compose & Navigation
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Core
    implementation("androidx.core:core-ktx:1.13.1")

    // (Opsional) Material Design Views klasik — tidak wajib utk Compose
    implementation("com.google.android.material:material:1.13.0")
}