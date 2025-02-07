plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.android.extension)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")

}

android {
    namespace = "com.example.copyovertest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.copyovertest"
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
    debugImplementation(libs.androidx.ui.tooling)
    kapt (libs.room.compiler)

    //implementation (libs.androidx.ui.tooling)
    //implementation (libs.androidx.runtime.dispatch)
    implementation (libs.androidx.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}