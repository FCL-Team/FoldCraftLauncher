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

    /**
     * 运行时 JRE 索引（8/17/21/25），仅安装器流程提供；游戏/Jar 执行器流程不感知，保持 -1
     */
    var javaVersion: Int = -1

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
