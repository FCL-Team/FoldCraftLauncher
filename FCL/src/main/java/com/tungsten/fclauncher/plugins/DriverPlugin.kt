package com.tungsten.fclauncher.plugins

import android.content.Context
import com.mio.manager.PluginManager
import com.tungsten.fcl.FCLApp

object DriverPlugin {
    data class Driver(val driver: String, val path: String)

    private var isInit = false;

    @JvmStatic
    val driverList: MutableList<Driver> = mutableListOf()
        get() {
            if (!isInit) {
                init(FCLApp.getAppContext())
            }
            return field
        }

    @JvmStatic
    var selected: Driver = Driver("Turnip", "")

    @JvmStatic
    fun init(context: Context) {
        isInit = true
        driverList.add(Driver("Turnip", context.applicationInfo.nativeLibraryDir))
        selected = driverList.first()
        PluginManager.enabledApps(context).forEach {
            parse(it)
        }
    }

    @JvmStatic
    fun refresh(context: Context) {
        driverList.clear()
        isInit = false
        init(context)
    }

    private fun parse(app: PluginManager.PluginApp) {
        val metaData = app.appInfo.metaData ?: return
        if (metaData.getBoolean(PluginManager.META_PLUGIN, false)) {
            val driver = metaData.getString("driver") ?: return
            add(Driver(driver, app.appInfo.nativeLibraryDir))
        }
    }

    private fun add(driver: Driver) {
        driverList.removeIf { it.path == driver.path }
        driverList.add(driver)
    }
}
