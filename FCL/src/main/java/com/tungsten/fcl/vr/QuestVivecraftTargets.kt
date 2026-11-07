package com.tungsten.fcl.vr

import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fclcore.download.LibraryAnalyzer

/** 从版本信息解析 Vivecraft 安装目标（MC 版本、唯一的模组加载器与实例目录） */
object QuestVivecraftTargets {
    private val LOADERS = listOf(
        LibraryAnalyzer.LibraryType.NEO_FORGE to QuestVivecraftTarget.Loader.NEOFORGE,
        LibraryAnalyzer.LibraryType.FORGE to QuestVivecraftTarget.Loader.FORGE,
        LibraryAnalyzer.LibraryType.QUILT to QuestVivecraftTarget.Loader.QUILT,
        LibraryAnalyzer.LibraryType.FABRIC to QuestVivecraftTarget.Loader.FABRIC,
    )

    /** 版本解析失败或同时存在多个加载器时，加载器记为 NONE */
    @JvmStatic
    fun capture(repository: FCLGameRepository, versionId: String): QuestVivecraftTarget {
        var minecraft = ""
        var loader = QuestVivecraftTarget.Loader.NONE
        var loaderVersion = ""
        try {
            val version = repository.getResolvedVersion(versionId)
            minecraft = repository.getGameVersion(version).orElse("")
            val analyzer = LibraryAnalyzer.analyze(version, minecraft)
            val installed = LOADERS.filter { (type, _) -> analyzer.has(type) }
            installed.singleOrNull()?.let { (type, found) ->
                loader = found
                loaderVersion = analyzer.getVersion(type).orElse("")
            }
        } catch (ignored: RuntimeException) {
            loader = QuestVivecraftTarget.Loader.NONE
        }
        return QuestVivecraftTarget(versionId, minecraft, loader, loaderVersion,
            repository.getRunDirectory(versionId))
    }
}
