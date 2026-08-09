package com.tungsten.fcl.ui

import android.content.Context
import com.tungsten.fcl.game.HMCLModpackInstallTask
import com.tungsten.fcl.util.AndroidUtils.getLocalizedText
import com.tungsten.fclcore.download.cleanroom.CleanroomInstallTask
import com.tungsten.fclcore.download.fabric.FabricAPIInstallTask
import com.tungsten.fclcore.download.fabric.FabricInstallTask
import com.tungsten.fclcore.download.forge.ForgeNewInstallTask
import com.tungsten.fclcore.download.forge.ForgeOldInstallTask
import com.tungsten.fclcore.download.game.GameAssetDownloadTask
import com.tungsten.fclcore.download.game.GameInstallTask
import com.tungsten.fclcore.download.liteloader.LiteLoaderInstallTask
import com.tungsten.fclcore.download.neoforge.NeoForgeInstallTask
import com.tungsten.fclcore.download.neoforge.NeoForgeOldInstallTask
import com.tungsten.fclcore.download.optifine.OptiFineInstallTask
import com.tungsten.fclcore.mod.MinecraftInstanceTask
import com.tungsten.fclcore.mod.ModpackInstallTask
import com.tungsten.fclcore.mod.ModpackUpdateTask
import com.tungsten.fclcore.mod.curse.CurseCompletionTask
import com.tungsten.fclcore.mod.curse.CurseInstallTask
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackCompletionTask
import com.tungsten.fclcore.mod.mcbbs.McbbsModpackExportTask
import com.tungsten.fclcore.mod.modrinth.ModrinthCompletionTask
import com.tungsten.fclcore.mod.modrinth.ModrinthInstallTask
import com.tungsten.fclcore.mod.multimc.MultiMCModpackExportTask
import com.tungsten.fclcore.mod.multimc.MultiMCModpackInstallTask
import com.tungsten.fclcore.mod.server.ServerModpackCompletionTask
import com.tungsten.fclcore.mod.server.ServerModpackExportTask
import com.tungsten.fclcore.mod.server.ServerModpackLocalInstallTask
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.StringUtils

/**
 * 任务/阶段显示名解析（小步骤 3.2）：从遗留 [TaskListPane] 原样抽取的纯迁移，
 * 供遗留 TaskListPane 与 Miuix 版任务对话框（ui/compose/FCLTaskDialog.kt）共用，
 * 避免 20+ 个 instanceof 分支两处维护。
 *
 * 行为与抽取前完全一致（含 task.setName 的副作用时机）。
 */
object TaskLabels {

    /**
     * 按任务类型把任务名改写为本地化文案（对应 TaskListPane.onRunning 的 instanceof 链）。
     * 未命中的类型保持原名，与遗留行为一致。
     */
    @JvmStatic
    fun resolveTaskName(context: Context, task: Task<*>) {
        when (task) {
            is GameAssetDownloadTask -> task.setName(getLocalizedText(context, "assets_download_all"))
            is GameInstallTask -> task.setName(getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_game")))
            is CleanroomInstallTask -> task.setName(getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_cleanroom")))
            is ForgeNewInstallTask, is ForgeOldInstallTask -> task.setName(getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_forge")))
            is NeoForgeInstallTask, is NeoForgeOldInstallTask -> task.setName(getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_neoforge")))
            is LiteLoaderInstallTask -> task.setName(getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_liteloader")))
            is OptiFineInstallTask -> task.setName(getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_optifine")))
            is FabricInstallTask -> task.setName(getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_fabric")))
            is FabricAPIInstallTask -> task.setName(getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_fabric_api")))
            is CurseCompletionTask, is ModrinthCompletionTask, is ServerModpackCompletionTask, is McbbsModpackCompletionTask -> task.setName(getLocalizedText(context, "modpack_completion"))
            is ModpackInstallTask<*> -> task.setName(getLocalizedText(context, "modpack_installing"))
            is ModpackUpdateTask -> task.setName(getLocalizedText(context, "modpack_update"))
            is CurseInstallTask -> task.setName(getLocalizedText(context, "modpack_install", getLocalizedText(context, "modpack_type_curse")))
            is MultiMCModpackInstallTask -> task.setName(getLocalizedText(context, "modpack_install", getLocalizedText(context, "modpack_type_multimc")))
            is ModrinthInstallTask -> task.setName(getLocalizedText(context, "modpack_install", getLocalizedText(context, "modpack_type_modrinth")))
            is ServerModpackLocalInstallTask -> task.setName(getLocalizedText(context, "modpack_install", getLocalizedText(context, "modpack_type_server")))
            is HMCLModpackInstallTask -> task.setName(getLocalizedText(context, "modpack_install", getLocalizedText(context, "modpack_type_hmcl")))
            is McbbsModpackExportTask, is MultiMCModpackExportTask, is ServerModpackExportTask -> task.setName(getLocalizedText(context, "modpack_export"))
            is MinecraftInstanceTask<*> -> task.setName(getLocalizedText(context, "modpack_scan"))
            else -> Unit
        }
    }

    /**
     * 阶段（stage key）→ 本地化文案（对应 TaskListPane.StageNode 的 stageKey switch）。
     */
    @JvmStatic
    fun resolveStageMessage(context: Context, stage: String): String {
        val stageKey = StringUtils.substringBefore(stage, ':')
        val stageValue = StringUtils.substringAfter(stage, ':')

        // @formatter:off
        return when (stageKey) {
            "fcl.modpack" -> getLocalizedText(context, "install_modpack")
            "fcl.modpack.download" -> getLocalizedText(context, "launch_state_modpack")
            "fcl.install.assets" -> getLocalizedText(context, "assets_download")
            "fcl.install.game" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_game") + " " + stageValue)
            "fcl.install.forge" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_forge") + " " + stageValue)
            "fcl.install.cleanroom" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_cleanroom") + " " + stageValue)
            "fcl.install.neoforge" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_neoforge") + " " + stageValue)
            "fcl.install.liteloader" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_liteloader") + " " + stageValue)
            "fcl.install.optifine" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_optifine") + " " + stageValue)
            "fcl.install.fabric" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_fabric") + " " + stageValue)
            "fcl.install.fabric-api" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_fabric-api") + " " + stageValue)
            "fcl.install.quilt" -> getLocalizedText(context, "install_installer_install", getLocalizedText(context, "install_installer_quilt") + " " + stageValue)
            else -> getLocalizedText(context, stageKey.replace(".", "_").replace("-", "_"))
        }
        // @formatter:on
    }
}
