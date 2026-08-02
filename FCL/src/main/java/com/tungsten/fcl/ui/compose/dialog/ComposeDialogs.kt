package com.tungsten.fcl.ui.compose.dialog

/**
 * 对话框 Miuix 迁移开关。
 *
 * 6.1 清理后，除联机菜单外全部弹窗开关已固化（真机验收通过），
 * 旧 View 实现与回滚分支已删除，仅保留本常量。
 */
object ComposeDialogs {

    /**
     * 联机功能菜单（MultiplayerDialog + dialog_multiplyer_menu）保留原生：
     * 游戏内 7 状态 View 体系（6 个 view_multiplayer_* 子布局 + fakefx 属性绑定 +
     * ListView 玩家列表），运行在渲染 surface 之上，无真机验证条件下整体重写风险过高。
     * 其内部的邀请码输入弹窗已单独迁移（MiuixInviteCodeInputDialog，开关已固化删除）。
     */
    const val USE_COMPOSE_MULTIPLAYER_MENU = false
}
