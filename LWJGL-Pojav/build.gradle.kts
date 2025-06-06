plugins {
    java
}

group = "org.lwjgl"

configurations.default.get().apply {
    isCanBeResolved = true
}

project.setProperty("archivesBaseName", "lwjgl")

project.setProperty("libsDirName", "${rootDir}/FCL/src/main/assets/app_runtime/lwjgl")

tasks.register("buildLwjgl") {
    dependsOn("jar")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.default.get().map {
        println(it.name)
        if (it.isDirectory) it else zipTree(it)
    })
    exclude("net/java/openjdk/cacio/ctc/**")
    manifest {
        attributes("Manifest-Version" to "3.3.3")
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
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    compileOnly(fileTree("dir" to "compileOnly", "include" to listOf("*.jar")))
}