plugins {
    java
}

val lwjglVersions = arrayOf("3.3.3", "3.4.1")

tasks.register("buildLwjgl") {
    description = "build lwjgl 3.3.3&3.4.1"
    dependsOn(lwjglVersions.map { ":LWJGL:lwjgl-$it:jar" })
}

tasks.register("buildAwtHelper") {
    description = "build awt-helper"
    dependsOn(":LWJGL:awt-helper:jar")
}
