import java.util.Properties
plugins {
    id("com.android.library")
}

android {
    namespace = "com.github.marschall.ZipFileSystem"
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
}

dependencies {
}