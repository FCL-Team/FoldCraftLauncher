package com.tungsten.fcl.ui.setting

/** 设置行分组：同组相邻行连成一块（行间无间距、去中间圆角），启动器/版本设置页适配器共用 */
sealed class SettingGroup {
    // 通用分组
    object Common : SettingGroup()
    // 启动器设置分组
    object Theme : SettingGroup()
    object Background : SettingGroup()
    object InGame : SettingGroup()
    object Launcher : SettingGroup()
    object TouchController : SettingGroup()
    object Download : SettingGroup()

    // 版本设置分组
    object Render : SettingGroup()
    object Check : SettingGroup()
    object Argument : SettingGroup()
}