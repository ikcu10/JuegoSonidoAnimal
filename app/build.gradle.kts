plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.chaquo.python") // Plugin de Python
}

android {
    namespace = "com.iker.sonidoanimal"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.iker.sonidoanimal"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ESTO ES VITAL PARA CHAQUOPY (Reduce tamaño APK)
        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
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

    // MANTENEMOS JAVA 11 (Igual que en tu rama que funciona)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // He quitado 'buildToolsVersion' para que Gradle use la defecto y no falle.
}

chaquopy {
    defaultConfig {
        version = "3.8"

        // Paquetes pip
        pip {
            install("numpy")
            install("pandas")
            // install("matplotlib") // Opcional, si no haces gráficos quítalo para ir más rápido
            // install("scikit-learn") // Opcional
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}