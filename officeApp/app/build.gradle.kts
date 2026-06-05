plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.officeapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.officeapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Source: https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation(libs.gson)
    // Source: https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
    implementation(libs.okhttp3.logging.interceptor)
    // Source: https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    implementation(libs.retrofit2.retrofit)
    // Source: https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
    implementation(libs.retrofit2.convertor.gson)
    // Source: https://mvnrepository.com/artifact/androidx.datastore/datastore-preferences
    implementation(libs.datastore.datastore.preferences)
    // Source: https://mvnrepository.com/artifact/androidx.navigation/navigation-compose
    implementation(libs.navigation.navigation.compose)
    // Source: https://mvnrepository.com/artifact/com.google.dagger/hilt-android
    implementation(libs.dagger.hilt.android)
    // Source: https://mvnrepository.com/artifact/com.google.dagger/hilt-android
    ksp(libs.dagger.hilt.compiler)
    // Source: https://mvnrepository.com/artifact/androidx.hilt/hilt-navigation-compose
    implementation(libs.androidx.hilt.navigation.compose)
    // Source: https://mvnrepository.com/artifact/androidx.compose.material/material-icons-extended
    implementation(libs.androidx.compose.material.icons.extended)
    // Source: https://mvnrepository.com/artifact/com.google.android.gms/play-services-location
    implementation(libs.play.services.location)
    // Source: https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-play-services
    implementation(libs.kotlinx.coroutines.play.services)
}