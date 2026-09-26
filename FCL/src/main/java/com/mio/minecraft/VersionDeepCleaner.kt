package com.mio.minecraft

import com.tungsten.fclcore.game.DefaultGameRepository
import com.tungsten.fclcore.game.SimpleVersionProvider
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.game.VersionNotFoundException
import com.tungsten.fclcore.util.Logging
import com.tungsten.fclcore.util.io.FileUtils
import java.io.File
import java.util.logging.Level

/**
 * 版本深度删除：在移除版本文件夹之外，一并清理 libraries / assets 中仅被该版本引用的文件。
 * 清理前全量解析磁盘上所有版本的 json 及其资产索引，其余版本仍引用到的文件一律保留。
 */
class VersionDeepCleaner(private val repository: DefaultGameRepository) {

    /** 清理统计：各类条目的删除数量与释放的字节数 */
    data class Stats(
        val libraries: Int,
        val indexes: Int,
        val assetObjects: Int,
        val virtualDirs: Int,
        val freedBytes: Long
    ) {
        val totalEntries: Int get() = libraries + indexes + assetObjects + virtualDirs
    }

    /**
     * 深度删除指定版本：先移除版本文件夹，再清理共享目录中仅被该版本引用的库与资产。
     * 版本 json 无法解析或文件夹移除失败时不会清理共享目录。
     */
    fun delete(id: String): Stats? {
        val provider = SimpleVersionProvider()
        val parsed = scanVersions(provider)
        val target = parsed[id]
        if (!repository.removeVersionFromDisk(id)) return null
        if (target == null) return null

        val protectedRefs = References()
        for ((versionId, version) in parsed) {
            if (versionId != id) collectReferences(versionId, version, provider, protectedRefs)
        }
        val targetRefs = References()
        collectReferences(id, target, provider, targetRefs)

        val librariesRoot = File(repository.baseDirectory, "libraries")
        val assetsRoot = File(repository.baseDirectory, "assets")
        val objectsRoot = File(assetsRoot, "objects")
        val indexesRoot = File(assetsRoot, "indexes")
        val virtualRoot = File(assetsRoot, "virtual")

        var libraries = 0
        var indexes = 0
        var assetObjects = 0
        var virtualDirs = 0
        var freed = 0L

        for (file in targetRefs.libraries) {
            if (file in protectedRefs.libraries) continue
            val freedNow = deleteFile(file)
            if (freedNow != null) {
                freed += freedNow
                libraries++
                pruneEmptyDirs(file, librariesRoot)
            }
        }

        // 引用不明的资产（索引损坏等）一律跳过，只清理枚举完整的
        val deletableAssetIds = targetRefs.assetIds - protectedRefs.assetIds - protectedRefs.unresolvedAssetIds
        val protectedObjects = protectedRefs.allObjectFiles()
        for (assetId in deletableAssetIds) {
            val indexFile = targetRefs.indexFiles[assetId]
            if (indexFile != null) {
                val freedNow = deleteFile(indexFile)
                if (freedNow != null) {
                    freed += freedNow
                    indexes++
                    pruneEmptyDirs(indexFile, indexesRoot)
                }
            }
            for (file in targetRefs.objects[assetId].orEmpty()) {
                if (file in protectedObjects) continue
                val freedNow = deleteFile(file)
                if (freedNow != null) {
                    freed += freedNow
                    assetObjects++
                    pruneEmptyDirs(file, objectsRoot)
                }
            }
            val virtualDir = File(virtualRoot, assetId)
            if (virtualDir.isDirectory) {
                val size = virtualDir.walkBottomUp().filter { it.isFile }.sumOf { it.length() }
                if (FileUtils.deleteDirectoryQuietly(virtualDir)) {
                    freed += size
                    virtualDirs++
                }
            }
        }
        return Stats(libraries, indexes, assetObjects, virtualDirs, freed)
    }

    /** 扫描磁盘上全部版本目录并解析 json，同时注册进 provider 以支持 inheritsFrom 链解析 */
    private fun scanVersions(provider: SimpleVersionProvider): Map<String, Version> {
        val versions = LinkedHashMap<String, Version>()
        val dirs = File(repository.baseDirectory, "versions").listFiles()?.filter { it.isDirectory }
            ?: return versions
        for (dir in dirs) {
            val id = dir.name
            var json = File(dir, "$id.json")
            if (!json.isFile) {
                // json 被误改名时，目录内唯一的 json 视为版本描述文件（与仓库刷新逻辑一致）
                val jsons = FileUtils.listFilesByExtension(dir, "json")
                if (jsons.size == 1) json = jsons[0] else continue
            }
            try {
                val version = repository.readVersionJson(json)
                if (id != version.id) version._setId(id)
                provider.addVersion(version)
                versions[id] = version
            } catch (e: Exception) {
                Logging.LOG.log(Level.WARNING, "Unable to parse version json for deep delete: $id", e)
            }
        }
        return versions
    }

    /** 收集一个版本在共享目录中的全部引用文件 */
    private fun collectReferences(id: String, version: Version, provider: SimpleVersionProvider, refs: References) {
        val resolved = try {
            version.resolve(provider)
        } catch (e: VersionNotFoundException) {
            // 父版本缺失时退化为该版本自身声明的依赖
            version
        }

        for (lib in resolved.libraries) {
            // hint=local 的库位于版本文件夹内，随文件夹删除，不参与共享目录清理
            if (lib.artifact == null || "local" == lib.hint) continue
            refs.libraries += repository.getLibraryFile(version, lib)
        }

        val assetId = resolved.assetIndex.id
        refs.assetIds += assetId
        try {
            val index = repository.getAssetIndex(id, assetId)
            val objects = mutableSetOf<File>()
            for (obj in index.objects.values) {
                val hash = obj.hash
                if (hash == null || hash.length < 2) continue
                objects += repository.getAssetObject(id, assetId, obj).toFile()
            }
            refs.indexFiles[assetId] = repository.getIndexFile(id, assetId).toFile()
            refs.objects[assetId] = objects
        } catch (e: Exception) {
            // 索引缺失或损坏时无法枚举对象，标记为引用不明
            Logging.LOG.log(Level.WARNING, "Unable to enumerate asset objects for deep delete: $id/$assetId", e)
            refs.unresolvedAssetIds += assetId
        }
    }

    /** 删除文件并返回其大小；文件不存在或删除失败时返回 null */
    private fun deleteFile(file: File): Long? {
        if (!file.isFile) return null
        val size = file.length()
        if (!file.delete()) {
            Logging.LOG.warning("Unable to delete file $file")
            return null
        }
        return size
    }

    /** 自底向上删除文件清理后留下的空目录，不越过 stopRoot */
    private fun pruneEmptyDirs(file: File, stopRoot: File) {
        var dir = file.parentFile ?: return
        while (dir != stopRoot && dir.absolutePath.startsWith(stopRoot.absolutePath + File.separator)) {
            if (!dir.isDirectory || !dir.listFiles().isNullOrEmpty()) return
            if (!dir.delete()) return
            dir = dir.parentFile ?: return
        }
    }

    /** 一个版本（或若干版本合集）在共享目录中的引用集合 */
    private class References {
        val libraries = mutableSetOf<File>()
        val assetIds = mutableSetOf<String>()
        val indexFiles = mutableMapOf<String, File>()
        val objects = mutableMapOf<String, Set<File>>()
        val unresolvedAssetIds = mutableSetOf<String>()

        fun allObjectFiles(): Set<File> = objects.values.flatten().toSet()
    }
}
