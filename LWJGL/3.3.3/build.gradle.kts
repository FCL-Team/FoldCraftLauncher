import java.io.File

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
    add("lwjglModules", fileTree(mapOf("dir" to "libs/$lwjglVersion", "include" to listOf("*.jar"))))
}

tasks.jar {
    // Modules to copy over to the components directory instead of patching and merging
    val excludedModules = arrayOf(
        "jsr305.jar",
        "lwjgl.jar",
        "lwjgl-freetype.jar",
        "lwjgl-jemalloc.jar",
        "lwjgl-lwjglx.jar",
        "lwjgl-nanovg.jar",
        "lwjgl-openal.jar",
        "lwjgl-opengl.jar",
        "lwjgl-stb.jar",
        "lwjgl-tinyfd.jar",
    )

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("lwjgl-${lwjglVersion}-merged-modules")
//    destinationDirectory.set(project(":jre_lwjgl3glfw").layout.buildDirectory.dir("libs/${lwjglVersion}"))
    destinationDirectory.set(file("$rootDir/FCL/src/main/assets/app_runtime/lwjgl/${lwjglVersion}"))

    // Mark as using multiple Java versions.
    manifest {
        attributes("Multi-Release" to "true")
        attributes("Automatic-Module-Name" to "org.lwjgl")
    }

    from({
        // Ensure that the core lwjgl jar is processed first so duplicates in META-INF from other classes
        // are ignored. This avoids InvalidModuleDescriptorException due to say, using the module-info.class
        // from lwjgl-jemalloc.
        val includedModules = configurations["lwjglModules"].filter { dep ->
            !excludedModules.any { it == dep.name }
        }
        val coreJar = includedModules.find { it.name == "lwjgl.jar" }
        val jarList = if (coreJar != null) listOf(coreJar) + (includedModules - coreJar) else includedModules
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
    val jarFile = archiveFile.get().asFile
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
    // Adds the jank to outputs
    outputs.file(versionFile)
    outputs.files(excludedModules.map { path -> File(destinationDirectory.get().asFile, path) })
////  Prints out the output.files for troubleshooting
//    println("Task outputs:")
//    outputs.files.forEach { file ->
//        println("  ${file}")
//    }
    exclude("net/java/openjdk/cacio/ctc/**")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}