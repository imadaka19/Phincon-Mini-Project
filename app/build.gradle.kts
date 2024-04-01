plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.pokemonapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokemonapp"
        minSdk = 28
        targetSdk = 34
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

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:+")
    implementation ("androidx.core:core-ktx:+")
    implementation ("androidx.appcompat:appcompat:+")
    implementation ("com.google.android.material:material:+")
    implementation ("androidx.constraintlayout:constraintlayout:+")
    implementation ("com.google.android.gms:play-services-maps:+")
    testImplementation ("junit:junit:+")
    androidTestImplementation ("androidx.test.ext:junit:+")
    androidTestImplementation ("androidx.test.espresso:espresso-core:+")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:+")
    implementation ("com.squareup.retrofit2:converter-gson:+")

    //navigation components
    implementation ("androidx.navigation:navigation-fragment-ktx:+")
    implementation ("androidx.navigation:navigation-ui-ktx:+")
    implementation ("androidx.lifecycle:lifecycle-extensions:+")

    //architecture components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:+")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:+")

    //room persistence library
    implementation ("androidx.room:room-runtime:+")
    implementation ("androidx.legacy:legacy-support-v4:+")
    kapt ("androidx.room:room-compiler:+")
    implementation ("androidx.room:room-ktx:+") //support for Coroutines with Room

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:+")

    //dagger-hilt
    implementation ("com.google.dagger:hilt-android:+")
    kapt ("com.google.dagger:hilt-compiler:+")

    //glide
    implementation ("com.github.bumptech.glide:glide:+")
    kapt ("com.github.bumptech.glide:compiler:+")

    //Circular Imgview
    implementation ("com.mikhaellopez:circularimageview:+")
}

kapt {
    correctErrorTypes = true
}