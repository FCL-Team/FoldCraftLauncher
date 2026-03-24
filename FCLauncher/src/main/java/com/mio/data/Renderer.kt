package com.mio.data

data class Renderer(
    val name: String,
    val des: String,
    val glName: String,
    val eglName: String,
    val path: String,
    val boatEnv: List<String>?,
    val pojavEnv: List<String>?,
    val id: String,
    val minMCver: String = "",
    val maxMCver: String = ""
) {
    companion object {
        const val ID_GL4ES = "f7e985d8-6d4c-f63c-d9f1-06074dab823a"
        const val ID_VIRGL = "417a7a93-d9b4-98b9-ec6e-1ea400259c1f"
        const val ID_VGPU = "0fb718e4-64e3-83d4-a974-8204ea1d9f9f"
        const val ID_ZINK = "18d93f17-ff53-a319-fa61-58709a77bf87"
        const val ID_FREEDRENO = "8d427e6c-9d22-2d19-db0c-3b9ac2c1543f"
        const val ID_NGGL4ES = "e7b90ed6-e518-4d4e-93dc-5c7133cd5b31"
    }

    fun getGLPath(): String {
        if (path.isEmpty()) return glName
        return "$path/$glName"
    }

    fun isEqual(id: String): Boolean {
        return this.id == id
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is Renderer) false
        else id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
