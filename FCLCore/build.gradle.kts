plugins {
    id("com.android.library")
}

android {
    namespace = "com.tungsten.fclcore"
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
    implementation(project(":FCLauncher"))
    implementation(project(":ZipFileSystem"))
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation("com.github.steveice10:opennbt:1.5")
    implementation("org.tukaani:xz:1.9")
    implementation("commons-io:commons-io:2.15.1")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("org.jenkins-ci:constant-pool-scanner:1.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}