// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.1.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:+")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:+")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:+")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }
//}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}