plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.android.extension)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id ("kotlinx-serialization")
   id ("org.jetbrains.kotlin.plugin.serialization")
    id("dagger.hilt.android.plugin")

}

android {
    namespace = "com.example.asteroidComposed"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.asteroidComposed"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    buildFeatures {
        dataBinding = true
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        create("staging"){
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    // (Java only)
    //implementation (libs.androidx.work.work.runtime)

    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation (libs.picasso)
    implementation(libs.moshi)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.scalars)
    implementation (libs.retrofit2.kotlin.coroutines.adapter)
    implementation (libs.converter.moshi)
    implementation (libs.squareup.moshi)
    implementation (libs.androidx.ui)
    implementation (libs.androidx.runtime.livedata)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.navigation.compose)
    /*
    not sure why Coil wasn't able to load images since I was using coil and not coil3, which
    was where the creator said the issue was. Will try coil3-okHttp attachment to load images
    instead
    */

    implementation("io.coil-kt.coil3:coil-video:3.0.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-video:2.4.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0")
    implementation("io.coil-kt.coil3:coil-compose:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.hilt.android)
    kapt("com.google.dagger:hilt-android-compiler:2.52")
    implementation(libs.androidx.hilt.work)
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation("androidx.compose.ui:ui-tooling")

    kapt (libs.room.compiler)

    //implementation (libs.androidx.ui.tooling)
    //implementation (libs.androidx.runtime.dispatch)
    implementation (libs.androidx.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}