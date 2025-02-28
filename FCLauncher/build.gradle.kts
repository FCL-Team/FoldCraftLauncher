plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tungsten.fclauncher"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        create("fordebug") {
            initWith(getByName("debug"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    externalNativeBuild {
        ndkBuild {
            path = file("src/main/jni/Android.mk")
        }
    }

    ndkVersion = "27.0.12077973"

    buildFeatures {
        prefab = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.bytedance:bytehook:1.0.10")
    implementation("com.jaredrummler:android-device-names:2.1.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}