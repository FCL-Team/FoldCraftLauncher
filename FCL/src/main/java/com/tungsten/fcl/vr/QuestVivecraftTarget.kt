package com.tungsten.fcl.vr

import java.io.File

/** 描述一个待安装/检查 Vivecraft 的目标实例（版本、加载器与游戏目录） */
class QuestVivecraftTarget(
    val instanceId: String,
    minecraftVersion: String?,
    val loader: Loader,
    loaderVersion: String?,
    gameDirectory: File,
) {
    enum class Loader { FABRIC, QUILT, FORGE, NEOFORGE, NONE }

    val minecraftVersion: String = minecraftVersion.orEmpty()
    val loaderVersion: String = loaderVersion.orEmpty()
    val gameDirectory: File = gameDirectory.absoluteFile
}
