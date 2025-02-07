buildscript {

    dependencies {

        //classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.5")
        classpath ("com.android.tools.build:gradle:${libs.versions.gradle}")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin}")
        classpath (libs.navigation.safe.args.gradle.plugin)
    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

}