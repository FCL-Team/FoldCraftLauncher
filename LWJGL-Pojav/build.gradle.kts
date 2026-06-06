plugins {
    java
}

group = "org.lwjgl"

val runtimeConfig by configurations.creating {
    isCanBeResolved = true
}

tasks.register("buildLwjgl") {
    dependsOn("jar")
}

tasks.jar {
    archiveBaseName.set("lwjgl")
    destinationDirectory.set(file("${rootDir}/FCL/src/main/assets/app_runtime/lwjgl"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(runtimeConfig.map {
        println(it.name)
        if (it.isDirectory) it else zipTree(it)
    })
    exclude("net/java/openjdk/cacio/ctc/**")
    manifest {
        attributes("Manifest-Version" to "3.3.6")
        attributes("Automatic-Module-Name" to "org.lwjgl")
    }
    doLast {
        val versionFile = file("../FCL/src/main/assets/app_runtime/lwjgl/version")
        versionFile.writeText(System.currentTimeMillis().toString())
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    runtimeConfig(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    compileOnly(fileTree("dir" to "compileOnly", "include" to listOf("*.jar")))
}