plugins {
    java
}

val lwjglVersions = arrayOf("3.3.3", "3.4.1")

tasks.register("buildLwjgl") {
    dependsOn(lwjglVersions.map { ":LWJGL:$it:jar" })
}