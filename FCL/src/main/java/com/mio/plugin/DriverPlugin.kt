package com.mio.plugin

import android.content.Context

object DriverPlugin : AbstractPlugin<DriverPlugin.Driver>() {

    /** 内置 Turnip 的驱动库文件名，也是插件未声明 driver_lib 时的默认值 */
    const val DEFAULT_DRIVER_LIB = "libvulkan_freedreno.so"

    /** 驱动适配的 GPU 家族（插件 metaData driver_gpu），门控自定义驱动加载路径 */
    const val GPU_ADRENO = "adreno"
    const val GPU_MALI = "mali"
    const val GPU_ANY = "any"

    data class Driver(
        /** 启动器内显示的驱动名 */
        val driver: String,
        /** 驱动库所在目录（插件 APK 的 nativeLibraryDir） */
        val path: String,
        /** 驱动库文件名（如 libvulkan_panfrost.so），由 linkerhook 经 DRIVER_LIB 加载 */
        val lib: String = DEFAULT_DRIVER_LIB,
        /** 适配的 GPU 家族：[GPU_ADRENO] / [GPU_MALI] / [GPU_ANY] */
        val gpu: String = GPU_ADRENO,
    )

    /** 当前选中的驱动（内置 Turnip 为兜底，启动游戏时按版本设置回填） */
    @JvmStatic
    var selected: Driver = Driver("Turnip", "")

    /** 插件驱动列表（含内置 Turnip，懒初始化） */
    @JvmStatic
    val driverList: List<Driver>
        get() = items

    override fun onInit(context: Context) {
        items.add(Driver("Turnip", context.applicationInfo.nativeLibraryDir))
        selected = items.first()
    }

    override fun parse(app: PluginManager.PluginApp) {
        val metaData = app.appInfo.metaData ?: return
        if (metaData.getBoolean(PluginManager.META_PLUGIN, false)) {
            val driver = metaData.getString("driver") ?: return
            val lib = metaData.getString("driver_lib")?.takeIf { it.isNotEmpty() } ?: DEFAULT_DRIVER_LIB
            val gpu = metaData.getString("driver_gpu")?.trim()?.lowercase()?.takeIf { it.isNotEmpty() } ?: GPU_ADRENO
            add(Driver(driver, app.appInfo.nativeLibraryDir, lib, gpu))
        }
    }

    private fun add(driver: Driver) {
        items.removeIf { it.path == driver.path }
        items.add(driver)
    }
}
