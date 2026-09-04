package com.mio.plugin

import android.content.Context

object DriverPlugin : AbstractPlugin<DriverPlugin.Driver>() {

    data class Driver(val driver: String, val path: String)

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
            add(Driver(driver, app.appInfo.nativeLibraryDir))
        }
    }

    private fun add(driver: Driver) {
        items.removeIf { it.path == driver.path }
        items.add(driver)
    }
}
