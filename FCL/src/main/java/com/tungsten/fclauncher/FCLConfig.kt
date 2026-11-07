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

    /** 本会话以 Quest VR 模式启动（渲染环境与退出链路均按 VR 处理） */
    @Transient
    var isVrEnabled = false

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
