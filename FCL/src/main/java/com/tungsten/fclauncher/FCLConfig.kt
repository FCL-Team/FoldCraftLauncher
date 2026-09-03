package com.tungsten.fclauncher

import android.content.Context
import com.mio.data.Renderer
import java.io.Serializable

/**
 * FCL 启动配置，由启动器构建后传给 FCLauncher
 */
class FCLConfig(
    val context: Context,
    val logDir: String,
    val javaPath: String,
    val workingDir: String,
    val renderer: Renderer,
    val args: Array<String>
) : Serializable {

    var useVKDriverSystem = false

    var installedModLoaders: InstalledModLoaders? = null

    var lwjglVersion: String = "3.3.3"

    /**
     * 已安装的模组加载器信息
     */
    data class InstalledModLoaders(
        val installForge: Boolean,
        val installCleanroom: Boolean,
        val installNeoForge: Boolean,
        val installOptiFine: Boolean,
        val installLiteLoader: Boolean,
        val installFabric: Boolean,
        val installQuilt: Boolean
    )
}
