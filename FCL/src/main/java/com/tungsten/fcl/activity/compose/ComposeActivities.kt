package com.tungsten.fcl.activity.compose

/**
 * 阶段三 3.7 其余 Activity 处置 · 迁移开关
 * （对齐 ui/compose/dialog/ComposeDialogs 与 ComposeMainUI.USE_COMPOSE_MAIN_UI 模式）。
 *
 * true = 对应 Activity 的 onCreate 挂 Compose/Miuix 内容；
 * false = 回滚遗留 View 路径（旧布局 activity_web.xml / activity_shell.xml 与旧逻辑保留未删）。
 *
 * 无开关项（本步评估结论，详见 docs/migration/activity-migration.md）：
 * - ControllerActivity / JVMActivity：红线/红线邻近，仅做系统栏主题色对齐，结构不动；
 * - JVMCrashActivity：独立 :crash 进程兜底崩溃页，保留原生；
 * - SplashActivity（含 activity-alias ImportActivity）：启动链路保留原生。
 */
object ComposeActivities {
    const val USE_COMPOSE_WEB = true
    const val USE_COMPOSE_SHELL = true
}
