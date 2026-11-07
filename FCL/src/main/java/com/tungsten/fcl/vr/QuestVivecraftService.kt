package com.tungsten.fcl.vr

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.FileAlreadyExistsException
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Collections
import java.util.Locale
import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Level
import java.util.logging.Logger
import java.util.regex.Pattern

/**
 * QuestCraft Vivecraft 发布版检索与安装：从 GitHub releases 列出适配当前
 * MC/加载器的 Android/OpenXR 构建，经哈希与描述符校验后装进实例 mods 目录。
 */
class QuestVivecraftService {
    enum class Error { NETWORK, UNSUPPORTED, INVALID_ASSET, INCOMPATIBLE, CONFLICT, DESTINATION, INTEGRITY }

    enum class Phase { DOWNLOADING, VERIFYING, INSTALLING }

    class ServiceException(val error: Error, message: String) : IOException(message)

    class Cancellation {
        private val cancelled = AtomicBoolean()

        fun cancel() {
            cancelled.set(true)
        }

        fun check() {
            if (cancelled.get() || Thread.currentThread().isInterrupted) throw CancellationException()
        }
    }

    fun interface Progress {
        fun onProgress(phase: Phase, completed: Long, total: Long)
    }

    class ReleaseAsset(
        val id: Long,
        val releaseName: String,
        val tag: String,
        val fileName: String,
        val url: String,
        val publishedAt: String,
        val sha256: String,
        val size: Long,
        val prerelease: Boolean,
    )

    class InstalledMod(
        val file: File,
        val version: String,
        val sha256: String,
        val compatible: Boolean,
    )

    class InstallResult(val mod: InstalledMod, val alreadyInstalled: Boolean)

    /** 分页拉取全部 release 并过滤出适配目标实例的构建（按发布时间降序） */
    @Throws(IOException::class)
    fun fetchReleases(target: QuestVivecraftTarget, cancellation: Cancellation): List<ReleaseAsset> {
        requireTarget(target)
        val result = ArrayList<ReleaseAsset>()
        try {
            for (page in 1..10) {
                cancellation.check()
                val json = openOfficial(URL(API + page), cancellation).use { input ->
                    String(readLimited(input, MAX_API_BYTES, cancellation), StandardCharsets.UTF_8)
                }
                val releases = JsonParser.parseString(json).asJsonArray
                result.addAll(parseReleases(json, target))
                if (releases.size() < 100) break
            }
        } catch (e: ServiceException) {
            throw e
        } catch (e: IOException) {
            throw ServiceException(Error.NETWORK, "Cannot read GitHub releases: " + e.message)
        } catch (e: IllegalStateException) {
            throw ServiceException(Error.NETWORK, "Cannot read GitHub releases: " + e.message)
        } catch (e: JsonParseException) {
            throw ServiceException(Error.NETWORK, "Cannot read GitHub releases: " + e.message)
        }
        result.sortWith(
            compareByDescending<ReleaseAsset> { it.publishedAt }.thenByDescending { it.id }
        )
        return Collections.unmodifiableList(result)
    }

    /** 解析 release JSON：仅收开放下载、状态 uploaded、命中 MC 版本与加载器提示的 jar 资产 */
    @Throws(IOException::class)
    fun parseReleases(json: String, target: QuestVivecraftTarget): List<ReleaseAsset> {
        requireTarget(target)
        val result = ArrayList<ReleaseAsset>()
        try {
            for (element in JsonParser.parseString(json).asJsonArray) {
                val release = element.asJsonObject
                if (bool(release, "draft")) continue
                val tag = text(release, "tag_name")
                val name = text(release, "name")
                val hints = "$name $tag ${text(release, "target_commitish")}"
                var minecraftHints = "$name $tag"
                if (!MC_VERSION.matcher(minecraftHints).find()) {
                    minecraftHints = text(release, "target_commitish")
                }
                if (!hints.lowercase(Locale.ROOT).contains("openxr")) continue
                val assets = release.getAsJsonArray("assets") ?: continue
                for (assetElement in assets) {
                    val asset = assetElement.asJsonObject
                    val fileName = text(asset, "name")
                    if (!safeFileName(fileName) || text(asset, "state") != "uploaded") continue
                    val fileHints = fileName.lowercase(Locale.ROOT)
                    if (fileHints.contains("sources") || fileHints.contains("javadoc")
                        || !matchesMinecraft(fileName, minecraftHints, target.minecraftVersion)
                        || !matchesLoaderHint(fileHints, target.loader)
                    ) continue
                    val url = text(asset, "browser_download_url")
                    if (!validAssetUrl(url, fileName)) continue
                    val size = asset.get("size").asLong
                    val id = asset.get("id").asLong
                    if (size <= 0 || size > MAX_ASSET_BYTES || id <= 0) continue
                    val digest = text(asset, "digest")
                    if (digest.isNotEmpty() && !digest.matches(Regex("sha256:[a-fA-F0-9]{64}"))) continue
                    result.add(
                        ReleaseAsset(id, name.ifEmpty { tag }, tag, fileName, url,
                            text(release, "published_at"),
                            if (digest.isEmpty()) "" else digest.substring(7).lowercase(Locale.ROOT),
                            size, bool(release, "prerelease"))
                    )
                }
            }
        } catch (e: RuntimeException) {
            throw ServiceException(Error.NETWORK,
                "Invalid GitHub release metadata: " + e.javaClass.simpleName)
        }
        return result
    }

    @Throws(IOException::class)
    fun scanInstalled(target: QuestVivecraftTarget): List<InstalledMod> =
        scanInstalled(target, Cancellation())

    /** 扫描实例 mods 目录中的 Vivecraft（含版本与 Android/OpenXR 兼容标记） */
    @Throws(IOException::class)
    fun scanInstalled(target: QuestVivecraftTarget, cancellation: Cancellation): List<InstalledMod> {
        val mods = try {
            modsDirectory(target, false)
        } catch (e: ServiceException) {
            throw e
        } catch (e: IOException) {
            throw destination(e)
        }
        if (!Files.exists(mods)) return emptyList()
        val files = mods.toFile().listFiles()
            ?: throw ServiceException(Error.DESTINATION, "Cannot list $mods")
        val result = ArrayList<InstalledMod>()
        for (file in files) {
            cancellation.check()
            if (!file.name.lowercase(Locale.ROOT).endsWith(".jar")) continue
            try {
                JarView.open(file.toPath()).use { jar ->
                    if (!QuestVivecraftMetadata.containsVivecraft(jar)) return@use
                    var version = ""
                    var compatible = false
                    try {
                        version = QuestVivecraftMetadata.validate(jar, target)
                        compatible = true
                    } catch (ignored: ServiceException) {
                    }
                    result.add(InstalledMod(file, version, sha256(file.toPath(), cancellation), compatible))
                }
            } catch (e: IOException) {
                throw if (e is ServiceException) e
                else ServiceException(Error.CONFLICT,
                    "Cannot inspect existing mod " + file.name + ": " + e.message)
            } catch (e: RuntimeException) {
                throw ServiceException(Error.CONFLICT,
                    "Cannot inspect existing mod " + file.name + ": " + e.message)
            }
        }
        return Collections.unmodifiableList(result)
    }

    /** 下载→哈希校验→描述符校验→原子暂存安装；已装同哈希时幂等返回 */
    @Throws(IOException::class)
    fun install(target: QuestVivecraftTarget, asset: ReleaseAsset, privateCacheDirectory: File,
                cancellation: Cancellation, progress: Progress?): InstallResult {
        requireTarget(target)
        cancellation.check()
        if (!validAssetUrl(asset.url, asset.fileName) || asset.size <= 0 || asset.size > MAX_ASSET_BYTES) {
            throw ServiceException(Error.INVALID_ASSET, "Invalid official release asset.")
        }
        var existing = scanInstalled(target, cancellation)
        if (existing.size == 1 && existing[0].compatible && asset.sha256.isNotEmpty()
            && asset.sha256 == existing[0].sha256
        ) return InstallResult(existing[0], true)
        if (existing.isNotEmpty() && (asset.sha256.isNotEmpty() || existing.size > 1)) {
            throw conflict(existing)
        }
        val temporary: Path
        try {
            Files.createDirectories(privateCacheDirectory.toPath())
            temporary = Files.createTempFile(privateCacheDirectory.toPath(), "quest-vivecraft-", ".download")
        } catch (e: IOException) {
            throw destination(e)
        }
        try {
            notify(progress, Phase.DOWNLOADING, 0, asset.size)
            var completed = 0L
            openDownload(URL(asset.url), cancellation).use { input ->
                Files.newOutputStream(temporary).use { output ->
                    val buffer = ByteArray(64 * 1024)
                    while (true) {
                        val count = input.read(buffer)
                        if (count == -1) break
                        cancellation.check()
                        completed += count
                        if (completed > asset.size || completed > MAX_ASSET_BYTES) {
                            throw ServiceException(Error.INTEGRITY, "Asset exceeds its published size.")
                        }
                        output.write(buffer, 0, count)
                        notify(progress, Phase.DOWNLOADING, completed, asset.size)
                    }
                }
            }
            if (completed != asset.size) {
                throw ServiceException(Error.INTEGRITY, "Incomplete release asset.")
            }
            cancellation.check()
            notify(progress, Phase.VERIFYING, 0, 0)
            val hash = sha256(temporary, cancellation)
            if (asset.sha256.isNotEmpty() && asset.sha256 != hash) {
                throw ServiceException(Error.INTEGRITY,
                    "GitHub SHA-256 does not match the downloaded asset.")
            }
            val metadataVersion: String
            try {
                JarView.open(temporary).use { jar ->
                    metadataVersion = QuestVivecraftMetadata.validate(jar, target)
                }
            } catch (e: ServiceException) {
                throw e
            } catch (e: IOException) {
                throw ServiceException(Error.INVALID_ASSET, "Invalid JAR archive: " + e.message)
            }
            synchronized(INSTALL_LOCK) {
                cancellation.check()
                existing = scanInstalled(target, cancellation)
                if (existing.size == 1 && existing[0].compatible && hash == existing[0].sha256) {
                    return InstallResult(existing[0], true)
                }
                if (existing.isNotEmpty()) throw conflict(existing)
                val mods = modsDirectory(target, true)
                val destinationPath = mods.resolve(asset.fileName)
                if (Files.exists(destinationPath, LinkOption.NOFOLLOW_LINKS)) {
                    throw ServiceException(Error.CONFLICT, "A file already exists: " + asset.fileName)
                }
                notify(progress, Phase.INSTALLING, 0, 0)
                val staged = Files.createTempFile(mods, ".quest-vivecraft-", ".part")
                try {
                    Files.newInputStream(temporary).use { input ->
                        Files.newOutputStream(staged).use { output ->
                            val buffer = ByteArray(64 * 1024)
                            while (true) {
                                val count = input.read(buffer)
                                if (count == -1) break
                                cancellation.check()
                                output.write(buffer, 0, count)
                            }
                        }
                    }
                    cancellation.check()
                    try {
                        Files.move(staged, destinationPath)
                    } catch (e: FileAlreadyExistsException) {
                        throw ServiceException(Error.CONFLICT, "A file already exists: " + asset.fileName)
                    }
                    return InstallResult(InstalledMod(destinationPath.toFile(), metadataVersion, hash, true), false)
                } finally {
                    deleteTemporary(staged)
                }
            }
        } catch (e: ServiceException) {
            throw e
        } catch (e: IOException) {
            throw destination(e)
        } finally {
            deleteTemporary(temporary)
        }
    }

    /** 下载流包装：读/关异常统一转 NETWORK 错误 */
    @Throws(IOException::class)
    private fun openDownload(url: URL, cancellation: Cancellation): InputStream {
        try {
            return object : FilterInputStream(openOfficial(url, cancellation)) {
                override fun read(): Int {
                    return try {
                        super.read()
                    } catch (e: IOException) {
                        throw network(e)
                    }
                }

                override fun read(bytes: ByteArray, offset: Int, length: Int): Int {
                    return try {
                        super.read(bytes, offset, length)
                    } catch (e: IOException) {
                        throw network(e)
                    }
                }

                override fun close() {
                    try {
                        super.close()
                    } catch (e: IOException) {
                        throw network(e)
                    }
                }
            }
        } catch (e: IOException) {
            throw network(e)
        }
    }

    private fun network(e: IOException): IOException =
        e as? ServiceException
            ?: ServiceException(Error.NETWORK, "Release download failed: " + e.message)

    private fun destination(e: IOException): IOException =
        ServiceException(Error.DESTINATION,
            "Cannot write/read the local download or instance directory: " + e.message)

    private fun deleteTemporary(path: Path) {
        try {
            Files.deleteIfExists(path)
        } catch (e: IOException) {
            Logger.getLogger(QuestVivecraftService::class.java.name).log(Level.WARNING,
                "Cannot remove Quest Vivecraft temporary file $path", e)
        }
    }

    private fun conflict(existing: List<InstalledMod>): IOException =
        ServiceException(Error.CONFLICT, "Existing Vivecraft mod(s): "
                + existing.joinToString(", ") { it.file.name })

    /** 实例 mods 目录解析：拒绝符号链接与越界路径（防安装到实例目录之外） */
    @Throws(IOException::class)
    private fun modsDirectory(target: QuestVivecraftTarget, create: Boolean): Path {
        val game = target.gameDirectory.toPath().toAbsolutePath().normalize()
        if (!Files.isDirectory(game)) {
            throw ServiceException(Error.DESTINATION, "Instance directory no longer exists: $game")
        }
        val realGame = game.toRealPath()
        val mods = realGame.resolve("mods")
        if (Files.isSymbolicLink(mods)) {
            throw ServiceException(Error.DESTINATION, "The mods directory is a symbolic link.")
        }
        if (create && !Files.exists(mods)) Files.createDirectory(mods)
        if (Files.exists(mods) && (!Files.isDirectory(mods) || mods.toRealPath().parent != realGame)) {
            throw ServiceException(Error.DESTINATION, "Invalid instance mods directory.")
        }
        return mods
    }

    private fun requireTarget(target: QuestVivecraftTarget) {
        if (target.loader == QuestVivecraftTarget.Loader.NONE || target.loaderVersion.isEmpty()
            || !target.minecraftVersion.matches(Regex("[0-9]+(?:\\.[0-9]+)*"))
        ) {
            throw ServiceException(Error.UNSUPPORTED,
                "Cannot identify this instance's Minecraft and mod loader versions.")
        }
    }

    /** 文件名中的 MC 版本命中目标版本（文件名无版本时回落 release 提示文本） */
    private fun matchesMinecraft(fileName: String, releaseHints: String, version: String): Boolean {
        var hasFileVersion = false
        val fileVersions = MC_VERSION.matcher(fileName)
        while (fileVersions.find()) {
            hasFileVersion = true
            if (QuestModVersionRules.matches(version, fileVersions.group())) return true
        }
        if (hasFileVersion) return false
        val releaseVersions = MC_VERSION.matcher(releaseHints)
        while (releaseVersions.find()) {
            if (QuestModVersionRules.matches(version, releaseVersions.group())) return true
        }
        return false
    }

    private fun matchesLoaderHint(name: String, loader: QuestVivecraftTarget.Loader): Boolean {
        if (name.contains("neoforge")) return loader == QuestVivecraftTarget.Loader.NEOFORGE
        if (name.contains("forge")) return loader == QuestVivecraftTarget.Loader.FORGE
        if (name.contains("fabric")) return loader == QuestVivecraftTarget.Loader.FABRIC
        if (name.contains("quilt")) return loader == QuestVivecraftTarget.Loader.QUILT
        return true
    }

    private fun validAssetUrl(value: String, fileName: String): Boolean {
        if (!safeFileName(fileName)) return false
        return try {
            val uri = URI.create(value)
            if (uri.scheme != "https" || uri.host != "github.com"
                || uri.port != -1 || uri.userInfo != null || uri.query != null || uri.fragment != null
            ) return false
            val path = uri.path
            if (!path.startsWith(DOWNLOAD_PATH)) return false
            val rest = path.substring(DOWNLOAD_PATH.length)
            val parts = rest.split("/")
            parts.size == 2 && parts[0].isNotEmpty() && parts[0] != "." && parts[0] != ".."
                    && parts[1] == fileName && !rest.contains("\\")
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun safeFileName(name: String): Boolean {
        return name.length in 5..180 && name.lowercase(Locale.ROOT).endsWith(".jar")
                && !name.startsWith(".") && !INVALID_FILE_CHARACTER.matcher(name).find()
    }

    companion object {
        private const val API = "https://api.github.com/repos/QuestCraftPlusPlus/VivecraftMod/releases?per_page=100&page="
        private const val DOWNLOAD_PATH = "/QuestCraftPlusPlus/VivecraftMod/releases/download/"
        private const val MAX_ASSET_BYTES = 64L * 1024 * 1024
        private const val MAX_API_BYTES = 4 * 1024 * 1024
        private val INSTALL_LOCK = Any()
        private val MC_VERSION: Pattern =
            Pattern.compile("(?<![0-9.])1\\.(?:1[0-9]|[2-9][0-9])(?:\\.[0-9]+)?(?![0-9.])")
        private val INVALID_FILE_CHARACTER: Pattern = Pattern.compile("[\\\\/:*?\"<>|\\p{Cntrl}]")

        /** GitHub 直连：手动跟随重定向（release 资产最终落 OSS 域），域名白名单校验 */
        @JvmStatic
        @Throws(IOException::class)
        private fun openOfficial(initial: URL, cancellation: Cancellation): InputStream {
            var url = initial
            for (redirects in 0 until 6) {
                cancellation.check()
                if (!allowedNetworkUrl(url)) {
                    throw ServiceException(Error.INVALID_ASSET, "Unexpected download host.")
                }
                val connection = url.openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
                connection.connectTimeout = 15000
                connection.readTimeout = 30000
                connection.setRequestProperty("User-Agent", "FCL/quest-vr")
                connection.setRequestProperty("Accept",
                    if (url.host == "api.github.com") "application/vnd.github+json"
                    else "application/octet-stream")
                connection.setRequestProperty("Accept-Encoding", "identity")
                try {
                    val code = connection.responseCode
                    if (code in 300..399) {
                        val location = connection.getHeaderField("Location")
                            ?: throw ServiceException(Error.NETWORK, "GitHub returned an empty redirect.")
                        url = URL(url, location)
                        connection.disconnect()
                        continue
                    }
                    if (code != 200) {
                        throw ServiceException(Error.NETWORK, "GitHub HTTP $code"
                                + (if (code == 403 || code == 429) " (rate limit or access denied; retry later)" else ""))
                    }
                    val stream = connection.inputStream
                    return object : FilterInputStream(stream) {
                        override fun close() {
                            try {
                                super.close()
                            } finally {
                                connection.disconnect()
                            }
                        }
                    }
                } catch (e: IOException) {
                    connection.disconnect()
                    throw e
                } catch (e: RuntimeException) {
                    connection.disconnect()
                    throw e
                }
            }
            throw ServiceException(Error.NETWORK, "Too many GitHub redirects.")
        }

        private fun allowedNetworkUrl(url: URL): Boolean {
            if (url.protocol != "https" || url.port != -1 || url.userInfo != null) return false
            return when (url.host) {
                "api.github.com" -> url.path == "/repos/QuestCraftPlusPlus/VivecraftMod/releases"
                "github.com" -> url.path.startsWith(DOWNLOAD_PATH)
                "release-assets.githubusercontent.com",
                "objects.githubusercontent.com",
                "github-releases.githubusercontent.com" -> true
                else -> false
            }
        }

        @Throws(IOException::class)
        private fun readLimited(input: InputStream, maximum: Int, cancellation: Cancellation): ByteArray {
            ByteArrayOutputStream().use { output ->
                val buffer = ByteArray(8192)
                while (true) {
                    val count = input.read(buffer)
                    if (count == -1) break
                    cancellation.check()
                    if (output.size() + count > maximum) {
                        throw ServiceException(Error.NETWORK, "GitHub response is too large.")
                    }
                    output.write(buffer, 0, count)
                }
                return output.toByteArray()
            }
        }

        @Throws(IOException::class)
        private fun sha256(path: Path, cancellation: Cancellation): String {
            val digest = try {
                MessageDigest.getInstance("SHA-256")
            } catch (e: NoSuchAlgorithmException) {
                throw AssertionError(e)
            }
            Files.newInputStream(path).use { input ->
                val buffer = ByteArray(64 * 1024)
                while (true) {
                    val count = input.read(buffer)
                    if (count == -1) break
                    cancellation.check()
                    digest.update(buffer, 0, count)
                }
            }
            return digest.digest().joinToString("") { String.format(Locale.ROOT, "%02x", it) }
        }

        private fun notify(progress: Progress?, phase: Phase, completed: Long, total: Long) {
            progress?.onProgress(phase, completed, total)
        }

        private fun text(`object`: JsonObject, key: String): String =
            QuestVivecraftMetadata.string(`object`, key)

        private fun bool(`object`: JsonObject, key: String): Boolean =
            `object`.has(key) && !`object`.get(key).isJsonNull && `object`.get(key).asBoolean
    }
}
