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
package com.tungsten.fclcore.mod.modrinth

import com.tungsten.fclcore.download.LibraryAnalyzer
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.FABRIC
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.FORGE
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.NEO_FORGE
import com.tungsten.fclcore.download.LibraryAnalyzer.LibraryType.QUILT
import com.tungsten.fclcore.game.DefaultGameRepository
import com.tungsten.fclcore.mod.ModAdviser
import com.tungsten.fclcore.mod.ModManager
import com.tungsten.fclcore.mod.Modpack
import com.tungsten.fclcore.mod.ModpackExportInfo
import com.tungsten.fclcore.mod.curse.CurseAddon
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository
import com.tungsten.fclcore.task.Task
import com.tungsten.fclcore.util.DigestUtils
import com.tungsten.fclcore.util.Logging.LOG
import com.tungsten.fclcore.util.gson.JsonUtils
import com.tungsten.fclcore.util.io.Zipper
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.logging.Level

/**
 * 将本地版本导出为 Modrinth 整合包（modrinth.index.json + client-overrides）。
 * mods/resourcepacks/shaderpacks 下白名单内的文件按 SHA-1 反查 Modrinth，
 * 命中的写入 files 列表，未命中的连同其余文件进入 client-overrides。
 * 允许开启 CurseForge 第二源：命中的文件把 CurseForge 下载地址并入 downloads。
 *
 * 参考 ModrinthModpackExportTask（https://github.com/HMCL-dev/HMCL/blob/main/HMCLCore/src/main/java/org/jackhuang/hmcl/modpack/modrinth/ModrinthModpackExportTask.java）
 * 参考 ModrinthPackExporter（https://github.com/ZalithLauncher/ZalithLauncher2/blob/main/ZalithLauncher/src/main/java/com/movtery/zalithlauncher/game/version/export/platform/ModrinthPackExporter.kt）
 */
class ModrinthModpackExportTask(
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

        val dependencies = LinkedHashMap<String, String>()
        dependencies["minecraft"] = gameVersion
        analyzer.getVersion(FORGE).ifPresent { dependencies["forge"] = it }
        analyzer.getVersion(NEO_FORGE).ifPresent { dependencies["neoforge"] = it }
        analyzer.getVersion(FABRIC).ifPresent { dependencies["fabric-loader"] = it }
        analyzer.getVersion(QUILT).ifPresent { dependencies["quilt-loader"] = it }

        val runDirectory = repository.getRunDirectory(version).toPath()
        val matchedFiles = matchRemoteFiles(runDirectory, blackList)

        Zipper(modpackFile.toPath()).use { zip ->
            zip.putDirectory(runDirectory, "client-overrides") { path ->
                if (!Modpack.acceptFile(path, blackList, info.whitelist)) return@putDirectory false
                if (matchedFiles.containsKey(path)) return@putDirectory false
                // 禁用态文件已按去后缀路径写入索引，同样要排除
                if (path.endsWith(ModManager.DISABLED_EXTENSION)
                    && matchedFiles.containsKey(path.removeSuffix(ModManager.DISABLED_EXTENSION))
                ) return@putDirectory false
                true
            }

            val manifest = ModrinthManifest(
                "minecraft", 1,
                info.version, info.name, info.description,
                matchedFiles.values.toList(), dependencies
            )
            zip.putTextFile(JsonUtils.GSON.toJson(manifest), "modrinth.index.json")
        }
    }

    /**
     * 对资源目录中白名单内的文件计算 SHA-1 并批量反查，返回 相对路径 → 索引文件条目。
     * 禁用态（.disabled）文件反查命中后以 client: optional 写入，索引内路径去掉后缀。
     * 反查失败时降级为空映射（全部文件留在 client-overrides）。
     */
    private fun matchRemoteFiles(runDirectory: Path, blackList: List<String>): Map<String, ModrinthManifest.File> {
        val candidates = sortedMapOf<String, Path>()
        for (dir in RESOURCE_DIRS) {
            val dirPath = runDirectory.resolve(dir)
            if (!Files.isDirectory(dirPath)) continue
            Files.walk(dirPath).use { stream ->
                stream.filter(Files::isRegularFile).forEach { file ->
                    if (".DS_Store" == file.fileName.toString()) return@forEach
                    val relativePath = runDirectory.relativize(file).normalize().toString().replace('\\', '/')
                    if (info.whitelist.contains(relativePath) && Modpack.acceptFile(relativePath, blackList, info.whitelist)) {
                        candidates[relativePath] = file
                    }
                }
            }
        }
        if (candidates.isEmpty()) return emptyMap()

        val sha1ByPath = HashMap<String, String>()
        for ((path, file) in candidates) {
            sha1ByPath[path] = DigestUtils.digestToString("SHA-1", file)
        }

        val matches: Map<String, ModrinthRemoteModRepository.ProjectVersionFile> = try {
            ModrinthRemoteModRepository.matchFilesBySha1(sha1ByPath.values)
        } catch (e: IOException) {
            LOG.log(Level.WARNING, "Failed to match files via Modrinth sha1", e)
            emptyMap()
        }

        // 可选的 CurseForge 第二源：命中的文件把 CurseForge 下载地址并入 downloads
        var fingerprintByPath: Map<String, Long> = emptyMap()
        var curseForgeMatches: Map<Long, CurseAddon.LatestFile> = emptyMap()
        if (info.isPackCurseForge) {
            if (!CurseForgeRemoteModRepository.isAvailable()) {
                LOG.warning("CurseForge API is unavailable, skipping CurseForge source")
            } else {
                val fingerprints = HashMap<String, Long>()
                for ((path, file) in candidates) {
                    try {
                        fingerprints[path] = CurseForgeRemoteModRepository.calculateFingerprint(file)
                    } catch (e: IOException) {
                        LOG.log(Level.WARNING, "Failed to calculate fingerprint of $path", e)
                    }
                }
                fingerprintByPath = fingerprints
                curseForgeMatches = try {
                    CurseForgeRemoteModRepository.matchFingerprints(fingerprints.values)
                } catch (e: IOException) {
                    LOG.log(Level.WARNING, "Failed to match files via CurseForge fingerprints", e)
                    emptyMap()
                }
            }
        }

        val result = LinkedHashMap<String, ModrinthManifest.File>()
        for (path in candidates.keys) {
            val file = candidates[path] ?: continue

            val downloads = mutableListOf<URL>()
            matches[sha1ByPath[path]]?.let { downloads.add(URL(it.url)) }
            fingerprintByPath[path]?.let { fingerprint ->
                curseForgeMatches[fingerprint]?.let { downloads.add(URL(it.downloadUrl())) }
            }
            if (downloads.isEmpty()) continue

            // 禁用态文件以原内容匹配远程，索引内写回启用路径并标记 client: optional
            val isDisabled = path.endsWith(ModManager.DISABLED_EXTENSION) || path.endsWith(ModManager.OLD_EXTENSION)
            val indexPath = if (isDisabled) path.removeSuffix(ModManager.DISABLED_EXTENSION) else path

            val hashes = LinkedHashMap<String, String>()
            hashes["sha1"] = sha1ByPath[path]!!
            hashes["sha512"] = DigestUtils.digestToString("SHA-512", file)

            val env = if (isDisabled) mapOf("client" to "optional") else null
            result[indexPath] = ModrinthManifest.File(
                indexPath, hashes, env,
                downloads, Files.size(file).toInt()
            )
        }
        return result
    }

    companion object {
        private val RESOURCE_DIRS = arrayOf("mods", "resourcepacks", "shaderpacks")

        @JvmField
        val OPTION = ModpackExportInfo.Options().requirePackCurseForge()
    }
}
