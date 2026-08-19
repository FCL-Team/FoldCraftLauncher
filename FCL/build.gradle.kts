import com.android.build.api.variant.FilterConfiguration.FilterType.ABI
import com.android.build.gradle.tasks.MergeSourceSetFolders
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.20"
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
    // 命令行 -Darch 优先；local.properties 仅在命令行未指定时生效
    if (System.getProperty("arch") == null && localProperty != null && localProperty.getProperty(
            "arch",
            "all"
        ) == "arm64"
    )
        System.setProperty("arch", "arm64")

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
        versionCode = 1327
        versionName = "1.3.2.7"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testBuildType = "fordebug"

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("FCLKey")
        }
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("FCLKey")
        }
        create("fordebug") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("FCLDebugKey")
            // 与 FileProvider authority（${applicationId}.provider）保持一致（原 FCLLibrary 模块的 resValue）
            resValue("string", "file_browser_provider", "com.tungsten.fcl.debug.provider")
        }
        configureEach {
            resValue("string", "app_version", defaultConfig.versionName.toString())
            resValue("string", "curse_api_key", curseApiKey.toString())
            resValue("string", "oauth_api_key", oauthApiKey.toString())
        }
    }



    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
            pickFirsts += listOf("**/libbytehook.so")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        resValues = true
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

/**
 * 按 -Darch 过滤 JRE 压缩包，生成当前架构需要的资产目录。
 * 非 all 构建只保留 version、universal 和 bin-<arch>.tar.xz（运行时两者都需要，见 RuntimeUtils#installJava）。
 */
abstract class FilterJreAssets : Sync() {
    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty
}

val filterJreAssets = tasks.register<FilterJreAssets>("filterJreAssets") {
    val arch = System.getProperty("arch", "all")
    // Copy/Sync 的 up-to-date 检查不包含 copy spec 的过滤规则，必须显式声明 arch 输入，
    // 否则切换架构时任务不会重跑，产物会残留上一架构的 JRE 包
    inputs.property("arch", arch)
    from(layout.projectDirectory.dir("src/main/jreAssets"))
    into(outputDir)
    if (arch != "all") {
        exclude { it.name.startsWith("bin-") && it.name != "bin-$arch.tar.xz" }
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                (output.getFilter(ABI)?.identifier ?: "all").let { abi ->
                    output.outputFileName =
                        "FCL-${variant.buildType}-${project.android.defaultConfig.versionName}-${abi}.apk"
                }
            }
        }

        // JRE 资产不放在 src/main/assets 里（AGP 的 mergeAssets 不应用 source set 的 exclude 过滤），
        // 而是按架构注册为生成源；arch 变化会让 Sync 任务重新执行，mergeAssets 随之重跑，避免产物残留旧架构文件。
        if (System.getProperty("arch", "all") != "all") {
            variant.sources.assets?.addGeneratedSourceDirectory(filterJreAssets) { it.outputDir }
        } else {
            variant.sources.assets?.addStaticSourceDirectory("src/main/jreAssets")
        }

        // LWJGL natives 打包在 lwjgl-*-natives aar 的 assets/app_runtime/lwjgl/<版本>/natives/<abi> 下，
        // 不走 AGP 的 abiFilters，需在 mergeAssets 后手动按架构删除其他 ABI 的 natives 目录。
        val variantName = variant.name.replaceFirstChar { it.uppercaseChar() }
        afterEvaluate {
            val mergeAssets =
                tasks.named("merge${variantName}Assets", MergeSourceSetFolders::class.java)
            val arch = System.getProperty("arch", "all")
            // 显式声明 arch 输入，arch 变化时任务重跑，避免产物残留上一架构的 natives
            mergeAssets.configure { inputs.property("lwjglArch", arch) }
            mergeAssets.configure {
                doLast {
                    if (arch == "all") return@doLast
                    val abi = when (arch) {
                        "arm" -> "armeabi-v7a"
                        "arm64" -> "arm64-v8a"
                        "x86" -> "x86"
                        "x86_64" -> "x86_64"
                        else -> return@doLast
                    }
                    val assetsDir = outputDir.get().asFile
                    // 版本列表从 libs 下的 lwjgl-*-natives-release.aar 文件名推导，避免新增版本时忘记同步
                    val lwjglVersions = project.file("libs").listFiles { f ->
                        f.name.matches(Regex("lwjgl-\\d+\\.\\d+\\.\\d+-natives-release\\.aar"))
                    }
                        ?.map { Regex("lwjgl-(\\d+\\.\\d+\\.\\d+)-natives-release\\.aar").find(it.name)!!.groupValues[1] }
                        ?: emptyList()
                    lwjglVersions.forEach { version ->
                        val nativesDir = File(assetsDir, "app_runtime/lwjgl/$version/natives")
                        if (nativesDir.isDirectory) {
                            nativesDir.listFiles()?.forEach { dir ->
                                if (dir.isDirectory && dir.name != abi) {
                                    logger.lifecycle("删除非目标架构 natives: $dir")
                                    dir.deleteRecursively()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(project(":FCLCore"))
    implementation(project(":FCLauncher"))
    implementation(project(":Terracotta"))
    implementation(libs.commons.io)
    implementation(libs.jelf)
    implementation(libs.taptargetview)
    implementation(libs.nanohttpd)
    implementation(libs.commons.compress)
    implementation(libs.xz)
    implementation(libs.opennbt)
    implementation(libs.gson)
    implementation(libs.appcompat)
    implementation(libs.core.splashscreen)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.glide)
    implementation(libs.touchcontroller)
    implementation(libs.palette.ktx)
    implementation(libs.gamepad.remapper)
    implementation(libs.segmented.button)
    implementation(libs.datastore)
    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.ext.junit)
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