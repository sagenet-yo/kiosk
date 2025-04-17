plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.kiosk"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kiosk"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Label
    //Icons
    implementation ("androidx.compose.material:material-icons-core:1.7.0")
    implementation ("androidx.compose.material:material-icons-extended:1.7.0")

    implementation("androidx.compose.ui:ui:1.7.0") // Replace with Hedgehog version
    implementation("androidx.compose.material:material:1.7.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.0")
    implementation("androidx.compose.foundation:foundation:1.7.0")

    implementation ("androidx.appcompat:appcompat:1.6.1") // Make sure you have AppCompat




    // Jetpack Compose dependencies
    implementation ("androidx.compose.ui:ui:1.7.0") // Update to the latest stable version
    implementation ("androidx.compose.material:material:1.7.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.7.0")
    implementation(mapOf("name" to "BrotherPrintLibrary", "ext" to "aar"))
    implementation("org.chromium.net:cronet-embedded:119.6045.31")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.7.0")

    // CameraX Core library
    implementation ("androidx.camera:camera-core:1.3.4")
    implementation ("androidx.camera:camera-camera2:1.3.4")
    implementation ("androidx.camera:camera-lifecycle:1.3.4")
    implementation ("androidx.camera:camera-view:1.3.4")
    implementation ("androidx.camera:camera-extensions:1.3.4")
    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")
    implementation ("androidx.activity:activity-compose:1.10.1")
    implementation ("androidx.camera:camera-core:1.2.0")
    implementation ("androidx.camera:camera-camera2:1.2.0")
    implementation ("androidx.camera:camera-lifecycle:1.2.0")
    implementation ("androidx.camera:camera-view:1.2.0")// for Compose

    // Coil for image loading
    implementation ("io.coil-kt:coil-compose:2.5.0") // Update to the latest stable version

    // Activity Compose integration
    implementation ("androidx.activity:activity-compose:1.10.1") // Update to the latest stable version



    //Compose ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")

    //Network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //Json to Kotlin object mapping
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //logging-interceptor
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //Image loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    //NavHost
    implementation("androidx.navigation:navigation-compose:2.8.0")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}