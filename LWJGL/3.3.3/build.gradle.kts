plugins {
    java
}

val lwjglVersion = "3.3.3"
group = "org.lwjgl.glfw"

configurations {
    create("lwjglModules") {
        isCanBeResolved = true
    }
}

dependencies {
    compileOnly(fileTree(mapOf("dir" to "../compileOnly", "include" to listOf("*.jar"))))
    implementation(fileTree(mapOf("dir" to "libs/$lwjglVersion", "include" to listOf("*.jar"))))
    // jsr305 仅为编译期注解依赖（@Nullable 等），不得打入运行时 assets，
    // 否则会与 JDK 内置 java.annotation 模块导出同名包导致 ResolutionException
    val lwjglModules = fileTree("libs/$lwjglVersion") {
        include("*.jar")
        exclude("jsr305.jar")
    }
    add("lwjglModules", lwjglModules)
}

tasks.jar {
    // 被排除的模块只经 doLast 复制、不参与合并，需显式声明为输入，
    // 否则更新这些 jar 后任务会误判 UP-TO-DATE，导致 copy 与 version 不更新
    inputs.files(configurations["lwjglModules"])

    // Modules to copy over to the components directory instead of patching and merging
    val excludedModules = arrayOf(
        "lwjgl.jar",
        "lwjgl-freetype.jar",
//            "lwjgl-glfw.jar",
        "lwjgl-lwjglx.jar",
        "lwjgl-jemalloc.jar",
        "lwjgl-nanovg.jar",
        "lwjgl-openal.jar",
//            "lwjgl-opengl.jar",
        "lwjgl-sdl.jar",
        "lwjgl-shaderc.jar",
        "lwjgl-spng.jar",
        "lwjgl-spvc.jar",
        "lwjgl-stb.jar",
        "lwjgl-tinyfd.jar",
        "lwjgl-vma.jar",
        "lwjgl-vulkan.jar"
    )

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("lwjgl-${lwjglVersion}-merged-modules")
    destinationDirectory.set(file("$rootDir/FCL/src/main/assets/app_runtime/lwjgl/${lwjglVersion}"))

    from({
        // Ensure that the core lwjgl jar is processed first so duplicates in META-INF from other classes
        // are ignored. This avoids InvalidModuleDescriptorException due to say, using the module-info.class
        // from lwjgl-jemalloc.
        val includedModules = configurations["lwjglModules"].filter { dep ->
            !excludedModules.any { it == dep.name }
        }
        val coreJar = includedModules.find { it.name == "lwjgl.jar" }
        val jarList =
            if (coreJar != null) listOf(coreJar) + (includedModules - coreJar) else includedModules
        println("Merging LWJGL $lwjglVersion modules in the order: ")
        jarList.map {
            println(it.name)
            if (it.isDirectory) it else zipTree(it)
        }
    })

    // Makes the jar reproducible so the version file actually is a version file
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true

    val versionFile = File(destinationDirectory.get().asFile, "version")
    doLast {
        val excludedModulesFileList = excludedModules.flatMap { fileName ->
            configurations["lwjglModules"].filter { it.name == fileName }
        }
        copy {
            // Copy excluded modules to the lwjgl classes dir
            from(excludedModulesFileList)
            into(archiveFile.get().asFile.parentFile)
        }
        versionFile.writeText(System.currentTimeMillis().toString())
    }
    exclude("net/java/openjdk/cacio/ctc/**")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}