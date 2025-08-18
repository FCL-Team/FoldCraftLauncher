plugins {
    id("com.android.library")
}

android {
    namespace = "com.bzlzhh.ng_gl4es"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    lint {
        targetSdk = libs.versions.targetSdk.get().toInt()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }

        create("fordebug") {
            initWith(getByName("debug"))
        }
    }

    externalNativeBuild {
        cmake {
            path = file("NG-GL4ES/CMakeLists.txt")
        }
    }

    ndkVersion = "27.0.12077973"
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.13.1")
}
