package com.tungsten.fcl.ui.compose.dialog

/**
 * 阶段三 3.2 对话框批量迁移 · 批 1（通用/简单对话框）的逐点开关
 * （对齐 MiuixTaskDialog.USE_COMPOSE_TASK_DIALOG 模式）。
 *
 * true = 调用点使用本包内的 Miuix 实现；false = 回滚遗留 View 实现（旧类与旧 XML 保留未删）。
 * 按 dialog 细分，可单个回退。
 */
object ComposeDialogs {
    const val USE_COMPOSE_TRANSLATION = true
    const val USE_COMPOSE_UPDATE = true
    const val USE_COMPOSE_DOWNLOAD_ADDON = true
    const val USE_COMPOSE_CONTROLLER_OLD_VERSION = true
    const val USE_COMPOSE_MODPACK_URL = true
    const val USE_COMPOSE_DUPLICATE_VERSION = true
    const val USE_COMPOSE_RENAME_VERSION = true
    const val USE_COMPOSE_WORLD_EXPORT = true
    const val USE_COMPOSE_SKIP_LOGIN = true
    const val USE_COMPOSE_TIP_RELOGIN = true
}
