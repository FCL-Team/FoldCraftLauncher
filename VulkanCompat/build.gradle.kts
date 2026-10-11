plugins {
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.ow2.asm:asm:9.10.1")
    testImplementation("junit:junit:4.13.2")
}

val agentJar = tasks.register<Jar>("agentJar") {
    archiveFileName.set("FCLVulkanCompat.jar")
    destinationDirectory.set(layout.buildDirectory.dir("agent"))
    manifest.attributes("Premain-Class" to "com.tungsten.fcl.vulkan.AgentBootstrap")
    from(sourceSets.main.get().output)
    from({ configurations.runtimeClasspath.get().map { zipTree(it) } })
    exclude("META-INF/*.SF", "META-INF/*.RSA", "META-INF/*.DSA", "META-INF/*.EC", "module-info.class")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

val agentElements = configurations.create("agentElements") {
    isCanBeConsumed = true
    isCanBeResolved = false
}
artifacts.add(agentElements.name, agentJar)

tasks.test {
    dependsOn(agentJar)
    inputs.file(agentJar.flatMap { it.archiveFile })
    doFirst {
        systemProperty("fcl.compat.test.agentJar", agentJar.get().archiveFile.get().asFile.absolutePath)
    }
}
tasks.assemble {
    dependsOn(agentJar)
}
