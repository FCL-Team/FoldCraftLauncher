import com.android.build.api.variant.FilterConfiguration.FilterType.ABI
import com.android.build.gradle.tasks.MergeSourceSetFolders
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tungsten.fcl"
    compileSdk = libs.versions.compileSdk.get().toInt()

    var localProperty: Properties? = null
    if (file("${rootDir}/local.properties").exists()) {
        localProperty = Properties()
        file("${rootDir}/local.properties").inputStream().use { localProperty.load(it) }
    }
    val pwd = System.getenv("FCL_KEYSTORE_PASSWORD") ?: localProperty?.getProperty("pwd")
    val curseApiKey = System.getenv("CURSE_API_KEY") ?: localProperty?.getProperty("curse.api.key")
    val oauthApiKey = System.getenv("OAUTH_API_KEY") ?: localProperty?.getProperty("oauth.api.key")

    signingConfigs {
        create("FCLKey") {
            storeFile = file("../key-store.jks")
            storePassword = pwd
            keyAlias = "FCL-Key"
            keyPassword = pwd
        }
        create("FCLDebugKey") {
            storeFile = file("../debug-key.jks")
            storePassword = "FCL-Debug"
            keyAlias = "FCL-Debug"
            keyPassword = "FCL-Debug"
        }
    }

    defaultConfig {
        applicationId = "com.tungsten.fcl"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1264
        versionName = "1.2.6.4"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("FCLKey")
        }
        create("fordebug") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("FCLDebugKey")
        }
        configureEach {
            resValue("string", "app_version", defaultConfig.versionName.toString())
            resValue("string", "curse_api_key", curseApiKey.toString())
            resValue("string", "oauth_api_key", oauthApiKey.toString())
        }
    }

    androidComponents {
        onVariants { variant ->
            variant.outputs.forEach { output ->
                if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                    (output.getFilter(ABI)?.identifier ?: "all").let { abi ->
                        output.outputFileName =
                            "FCL-${variant.buildType}-${defaultConfig.versionName}-${abi}.apk"
                    }

                    val variantName = variant.name.replaceFirstChar { it.uppercaseChar() }
                    afterEvaluate {
                        val task =
                            tasks.named("merge${variantName}Assets").get() as MergeSourceSetFolders
                        task.doLast {
                            val arch = System.getProperty("arch", "all")
                            val assetsDir = task.outputDir.get().asFile
                            val jreList = listOf("jre8", "jre11", "jre17", "jre21")
                            println("arch:$arch")
                            jreList.forEach { jre ->
                                val runtimeDir = "$assetsDir/app_runtime/java/$jre"
                                println("runtimeDir:$runtimeDir")
                                File(runtimeDir).listFiles().forEach {
                                    if (arch != "all" && it.name != "version" && !it.name.contains("universal") && it.name != "bin-${arch}.tar.xz") {
                                        println("delete:${it} : ${it.delete()}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
            pickFirsts += listOf("**/libbytehook.so")
        }
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    splits {
        val arch = System.getProperty("arch", "all")
        if (arch != "all") {
            abi {
                isEnable = true
                reset()
                when (arch) {
                    "arm" -> include("armeabi-v7a")
                    "arm64" -> include("arm64-v8a")
                    "x86" -> include("x86")
                    "x86_64" -> include("x86_64")
                }
            }
        }
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":FCLCore"))
    implementation(project(":FCLLibrary"))
    implementation(project(":FCLauncher"))
    implementation(project(":NG-GL4ES"))
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.14.0")
    implementation("org.nanohttpd:nanohttpd:2.3.1")
    implementation("org.apache.commons:commons-compress:1.26.0")
    implementation("org.tukaani:xz:1.9")
    implementation("com.github.steveice10:opennbt:1.5")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("top.fifthlight.touchcontroller:proxy-client-android:0.0.5")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("com.github.Mathias-Boulay:android_gamepad_remapper:2.0.3")
    implementation("com.github.addisonElliott:SegmentedButton:3.1.9")
}

tasks.register("updateMap") {
    doLast {
        val list = mutableListOf<String>()
        val mapFile = file("${rootDir}/version_map.json")
        mapFile.forEachLine {
            list.add(
                when {
                    it.contains("versionCode") -> it.replace(
                        Regex("[0-9]+"),
                        android.defaultConfig.versionCode.toString()
                    )

                    it.contains("versionName") -> it.replace(
                        Regex("\\d+(\\.\\d+)+"),
                        android.defaultConfig.versionName.toString()
                    )

                    it.contains("date") -> it.replace(
                        Regex("\\d{4}\\.\\d{2}\\.\\d{2}"),
                        SimpleDateFormat("yyyy.MM.dd").format(Date())
                    )

                    it.contains("url") -> it.replace(
                        Regex("\\d+(\\.\\d+)+"),
                        android.defaultConfig.versionName.toString()
                    )

                    else -> it
                }
            )
        }
        mapFile.writeText(list.joinToString("\n"), Charsets.UTF_8)
    }
}