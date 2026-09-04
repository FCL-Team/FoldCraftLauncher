package com.mio.plugin

import android.content.Context
import com.tungsten.fcl.FCLApp

/**
 * 插件基类：统一列表懒初始化、插件扫描遍历（PluginManager.enabledApps）与刷新流程。
 * 子类（Kotlin object）实现 [parse] 解析各自 meta-data，通过 [items] 访问解析结果；
 * 对外属性由子类以 @JvmStatic 暴露（委托到 [items]），保持 Java 侧静态调用兼容。
 */
abstract class AbstractPlugin<T> {

    /** 解析结果（懒初始化：首次访问时扫描已启用的插件应用） */
    protected val items: MutableList<T> = mutableListOf()
        get() {
            if (!isInit) {
                init(FCLApp.getAppContext())
            }
            return field
        }

    private var isInit = false

    fun init(context: Context) {
        isInit = true
        onInit(context)
        PluginManager.enabledApps(context).forEach {
            parse(it)
        }
    }

    fun refresh(context: Context) {
        items.clear()
        isInit = false
        init(context)
    }

    /** 解析前的准备（如内置条目），默认无操作 */
    protected open fun onInit(context: Context) {}

    protected abstract fun parse(app: PluginManager.PluginApp)
}
