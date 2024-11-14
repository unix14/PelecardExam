plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")   // Kotlin KAPT for annotation processing
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.eyal.exam.pelecard"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.eyal.exam.pelecard"
        minSdk = 24
        targetSdk = 35
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

    // Enable Jetpack Compose
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compiler.get()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Compose dependencies
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compiler)

    // Navigation for Jetpack Compose
    implementation(libs.androidx.navigation.compose)

    // Other Compose libraries I may need
    implementation(libs.androidx.activity.compose)

    // For debugging UI
    debugImplementation(libs.androidx.ui.tooling)

    //Hilt - DI (Dependency Injection)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // For Hilt ViewModel
    implementation(libs.androidx.hilt.navigation.compose)

    // For Flow support in ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Retrofit dependencies
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // or any other converter depending on your data format (e.g., Moshi)

    // OkHttp for logging (optional but recommended for debugging)
    implementation(libs.logging.interceptor)

}

kapt {
    correctErrorTypes = true // Add this block here
}