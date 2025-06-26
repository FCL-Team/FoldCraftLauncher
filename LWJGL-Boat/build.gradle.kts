plugins {
    java
}

group = "org.lwjgl"

project.setProperty("archivesBaseName", "lwjgl")

project.setProperty("libsDirName", "${rootDir}/FCL/src/main/assets/app_runtime/lwjgl-boat")

tasks.register("buildLwjglBoat") {
    dependsOn("jar")
}

tasks.jar {
    manifest {
        attributes("Manifest-Version" to "3.3.3")
        attributes("Automatic-Module-Name" to "org.lwjgl")
    }
    doLast {
        val versionFile = file("../FCL/src/main/assets/app_runtime/lwjgl-boat/version")
        versionFile.writeText(System.currentTimeMillis().toString())
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

dependencies {
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    compileOnly(fileTree("dir" to "libs", "include" to listOf("*.jar")))
}