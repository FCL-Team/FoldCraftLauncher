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

    // ---- 批 2：版本/管理域 + mio/游戏内杂项 ----
    const val USE_COMPOSE_ADD_PROFILE = true
    const val USE_COMPOSE_MOD_INFO = true
    const val USE_COMPOSE_ROLLBACK_MOD = true
    const val USE_COMPOSE_JAVA_MANAGE = true
    const val USE_COMPOSE_RENDERER_SELECT = true
    const val USE_COMPOSE_GAMEPAD_MAP = true
    const val USE_COMPOSE_ITEMBAR_SETTING = true
    const val USE_COMPOSE_OPEN_FOLDER = true
    const val USE_COMPOSE_INVITE_CODE = true

    /**
     * 联机功能菜单（MultiplayerDialog + dialog_multiplyer_menu）保留原生：
     * 游戏内 7 状态 View 体系（6 个 view_multiplayer_* 子布局 + fakefx 属性绑定 +
     * ListView 玩家列表），运行在渲染 surface 之上，无真机验证条件下整体重写风险过高。
     * 其内部的邀请码输入弹窗已单独迁移（见 [USE_COMPOSE_INVITE_CODE]）。
     */
    const val USE_COMPOSE_MULTIPLAYER_MENU = false

    // ---- 批 3：账户域 + 杂项 ----
    const val USE_COMPOSE_CREATE_ACCOUNT = true

    /**
     * 角色选择子对话框（CharacterSelector 阻塞式）。
     * 与 [USE_COMPOSE_CREATE_ACCOUNT] 独立开关，可交叉搭配
     * （Miuix 创建账户弹窗可回退用遗留 DialogCharacterSelector，反之亦然）。
     */
    const val USE_COMPOSE_CHARACTER_SELECTOR = true
    const val USE_COMPOSE_ADD_AUTHLIB_INJECTOR_SERVER = true
    const val USE_COMPOSE_RELOGIN_OAUTH = true
    const val USE_COMPOSE_OFFLINE_ACCOUNT_SKIN = true
    const val USE_COMPOSE_QUICK_INPUT = true
    const val USE_COMPOSE_ADD_INPUT_TEXT = true

    // ---- 批 4：手柄/控件样式域（control/ + ui/controller/）----
    const val USE_COMPOSE_ADD_BUTTON_STYLE = true
    const val USE_COMPOSE_ADD_DIRECTION_STYLE = true
    const val USE_COMPOSE_BUTTON_STYLE = true
    const val USE_COMPOSE_DIRECTION_STYLE = true
    const val USE_COMPOSE_EDIT_VIEW = true
    const val USE_COMPOSE_EDIT_VIEW_GROUP = true
    const val USE_COMPOSE_VIEW_GROUP = true
    const val USE_COMPOSE_SELECT_CONTROLLER = true
    const val USE_COMPOSE_CONTROLLER_INFO = true
    const val USE_COMPOSE_CONTROLLER_UPLOAD = true
}
