/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tungsten.fclcore.mod.curse

import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.FABRIC
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.FORGE
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.NEO_FORGE
import com.tungsten.fclcore.game.DefaultGameRepository
import com.tungsten.fclcore.mod.ModAdviser
import com.tungsten.fclcore.mod.ModManager
import com.tungsten.fclcore.mod.Modpack
import com.tungsten.fclcore.mod.ModpackExportInfo
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.gson.JsonUtils
import com.tungsten.fclcore.util.io.Zipper
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.logging.Level

/**
 * 将本地版本导出为 CurseForge 整合包（manifest.json + overrides）。
 * mods/resourcepacks/shaderpacks 下白名单内的文件按指纹反查 CurseForge，
 * 命中的写入 files 列表，未命中的连同其余文件进入 overrides。
 */
class CurseForgeModpackExportTask(
    private val repository: DefaultGameRepository,
    private val version: String,
    info: ModpackExportInfo,
    private val modpackFile: File,
) : Task<Void>() {

    private val info = info.validate()

    init {
        onDone().register { event -> if (event.isFailed) modpackFile.delete() }
    }

    override fun execute() {
        val blackList = ArrayList(ModAdviser.MODPACK_BLACK_LIST)
        blackList.add("$version.jar")
        blackList.add("$version.json")

        val gameVersion = repository.getGameVersion(version)
            .orElseThrow { IOException("Cannot parse the version of $version") }
        val analyzer = LibraryAnalyzer.analyze(repository.getResolvedPreservingPatchesVersion(version), gameVersion)

        // OptiFine 与 LiteLoader 不被 CurseForge 整合包支持
        val modLoaders = ArrayList<CurseManifestModLoader>()
        analyzer.getVersion(FORGE).ifPresent { modLoaders.add(CurseManifestModLoader("forge-$it", true)) }
        analyzer.getVersion(NEO_FORGE).ifPresent { modLoaders.add(CurseManifestModLoader("neoforge-$it", true)) }
        analyzer.getVersion(FABRIC).ifPresent { modLoaders.add(CurseManifestModLoader("fabric-$it", true)) }

        val runDirectory = repository.getRunDirectory(version).toPath()
        val matchedFiles = matchRemoteFiles(runDirectory, blackList)

        Zipper(modpackFile.toPath()).use { zip ->
            zip.putDirectory(runDirectory, "overrides") { path ->
                Modpack.acceptFile(path, blackList, info.whitelist) && !matchedFiles.containsKey(path)
            }

            val files = matchedFiles.values.map { CurseManifestFile(it.modId, it.id, null, null, true) }
            val manifest = CurseManifest(
                CurseManifest.MINECRAFT_MODPACK, 1,
                info.name, info.version, info.author, "overrides",
                CurseManifestMinecraft(gameVersion, modLoaders), files
            )
            zip.putTextFile(JsonUtils.GSON.toJson(manifest), "manifest.json")
        }
    }

    /**
     * 对资源目录中白名单内的文件计算指纹并批量反查，返回 相对路径 → CurseForge 文件。
     * 反查失败或 API 不可用时降级为空映射（全部文件留在 overrides）。
     */
    private fun matchRemoteFiles(runDirectory: Path, blackList: List<String>): Map<String, CurseAddon.LatestFile> {
        if (!CurseForgeRemoteModRepository.isAvailable()) {
            LOG.warning("CurseForge API is unavailable, all files will be packed into overrides")
            return emptyMap()
        }

        val candidates = sortedMapOf<String, Path>()
        for (dir in RESOURCE_DIRS) {
            val dirPath = runDirectory.resolve(dir)
            if (!Files.isDirectory(dirPath)) continue
            Files.walk(dirPath).use { stream ->
                stream.filter(Files::isRegularFile).forEach { file ->
                    if (".DS_Store" == file.fileName.toString()) return@forEach
                    // files 列表只能表达"必装"，禁用态文件不参与反查，保留在 overrides 中维持禁用语义
                    if (file.fileName.toString().endsWith(ModManager.DISABLED_EXTENSION)
                        || file.fileName.toString().endsWith(ModManager.OLD_EXTENSION)
                    ) return@forEach
                    val relativePath = runDirectory.relativize(file).normalize().toString().replace('\\', '/')
                    if (info.whitelist.contains(relativePath) && Modpack.acceptFile(relativePath, blackList, info.whitelist)) {
                        candidates[relativePath] = file
                    }
                }
            }
        }
        if (candidates.isEmpty()) return emptyMap()

        val fingerprintByPath = HashMap<String, Long>()
        for ((path, file) in candidates) {
            try {
                fingerprintByPath[path] = CurseForgeRemoteModRepository.calculateFingerprint(file)
            } catch (e: IOException) {
                LOG.log(Level.WARNING, "Failed to calculate fingerprint of $path", e)
            }
        }

        val matches: Map<Long, CurseAddon.LatestFile> = try {
            CurseForgeRemoteModRepository.matchFingerprints(fingerprintByPath.values)
        } catch (e: IOException) {
            LOG.log(Level.WARNING, "Failed to match files via CurseForge fingerprints", e)
            emptyMap()
        }

        val result = LinkedHashMap<String, CurseAddon.LatestFile>()
        for (path in candidates.keys) {
            fingerprintByPath[path]?.let { fingerprint -> matches[fingerprint]?.let { result[path] = it } }
        }
        return result
    }

    companion object {
        private val RESOURCE_DIRS = arrayOf("mods", "resourcepacks", "shaderpacks")

        @JvmField
        val OPTION = ModpackExportInfo.Options()
    }
}
