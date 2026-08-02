package com.tungsten.fcl.ui.version.compose

/**
 * 阶段三 3.3 版本域页面 Miuix 迁移开关（对齐 SettingPageManager.USE_COMPOSE_SETTING_PAGES 模式）。
 *
 * true = 挂载 Compose 页面（ComposeVersionListPage / ComposeManagePage）；
 * false = 整体回滚旧 View 页面（VersionListPage / ManagePage，旧类与旧 XML 保留未删）。
 *
 * 版本列表页与版本管理入口页共用一个开关：二者同属"版本域列表与管理入口框架"，
 * 需同时新旧、避免跨体系跳转（版本列表"设置"按钮 → ManageUI Tab0 即管理页）。
 * page_version_setting（VersionSettingPage）不在本步骤，始终走旧 View 页面。
 */
object ComposeVersionPages {
    const val USE_COMPOSE_VERSION_PAGES = true
}
