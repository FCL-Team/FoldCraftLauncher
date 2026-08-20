package com.tungsten.fcl.setting

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.JsonAdapter
import com.tungsten.fcl.game.FCLCacheRepository
import com.tungsten.fcl.game.FCLGameRepository
import com.tungsten.fcl.util.WeakListenerHolder
import com.tungsten.fclcore.download.DefaultDependencyManager
import com.tungsten.fclcore.download.DownloadProvider
import com.tungsten.fclcore.event.EventBus
import com.tungsten.fclcore.event.EventPriority
import com.tungsten.fclcore.event.RefreshedVersionsEvent
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.util.ToStringBuilder
import java.io.File
import java.lang.reflect.Type

/**
 * 游戏目录配置。使用普通类型字段，不再依赖 fakefx property；
 * 选中版本变化通过 [addSelectedVersionListener] 通知。
 */
@JsonAdapter(Profile.Serializer::class)
class Profile {
    private val listenerHolder = WeakListenerHolder()

    /** 名称 */
    var name: String = ""
        set(value) {
            if (field == value) return
            field = value
            onChanged?.invoke()
        }

    /** 游戏目录（变化时切换仓库目录） */
    var gameDir: File = File("")
        set(value) {
            field = value
            repository.changeDirectory(value)
            onChanged?.invoke()
        }

    /** 游戏仓库（构造时按初始目录创建，目录变化时切换） */
    val repository: FCLGameRepository

    /** 选中版本（变化时校验有效性并通知监听者） */
    var selectedVersion: String? = null
        set(value) {
            if (field == value) return
            field = value
            checkSelectedVersion()
            // 复制后遍历：回调内可能增删监听（如 DownloadUI 切换监听对象），避免并发修改
            selectedVersionListeners.toList().forEach { it.run() }
            onChanged?.invoke()
        }

    /** 全局设置（变化时触发 [onChanged]） */
    val globalVersionSetting: VersionSetting

    /** 字段变化回调（由 Profiles 设置，用于触发配置保存） */
    var onChanged: (() -> Unit)? = null

    private val selectedVersionListeners = mutableListOf<Runnable>()

    /** 选中版本的设置（Java 侧访问 getVersionSetting()） */
    val versionSetting: VersionSetting
        get() = repository.getVersionSetting(selectedVersion)

    constructor(name: String, gameDir: File) : this(
        name,
        gameDir,
        VersionSetting(),
        null
    )

    constructor(
        name: String,
        gameDir: File,
        globalVersionSetting: VersionSetting?,
        selectedVersion: String?
    ) {
        this.name = name
        //必须放在gameDir前
        this.repository = FCLGameRepository(this, gameDir)
        this.gameDir = gameDir
        this.globalVersionSetting = globalVersionSetting ?: VersionSetting()
        this.globalVersionSetting.addOnChangeListener { onChanged?.invoke() }
        this.selectedVersion = selectedVersion

        listenerHolder.add(
            EventBus.EVENT_BUS.channel(RefreshedVersionsEvent::class.java)
                .registerWeak({ checkSelectedVersion() }, EventPriority.HIGHEST)
        )
    }

    /** 注册选中版本变化监听（setter 同步通知，调用线程即回调线程） */
    fun addSelectedVersionListener(listener: Runnable) {
        selectedVersionListeners.add(listener)
    }

    fun removeSelectedVersionListener(listener: Runnable) {
        selectedVersionListeners.remove(listener)
    }

    private fun checkSelectedVersion() {
        if (!repository.isLoaded) return
        val newValue = selectedVersion
        if (!repository.hasVersion(newValue)) {
            val version = repository.getVersions().stream().findFirst().map(Version::getId)
            if (version.isPresent) {
                selectedVersion = version.get()
            } else if (newValue != null) {
                selectedVersion = null
            }
        }
    }

    fun getDependency(): DefaultDependencyManager =
        getDependency(DownloadProviders.getDownloadProvider())

    fun getDependency(downloadProvider: DownloadProvider): DefaultDependencyManager =
        DefaultDependencyManager(repository, downloadProvider, FCLCacheRepository.REPOSITORY)

    fun getVersionSetting(id: String?): VersionSetting = repository.getVersionSetting(id)

    override fun toString(): String = ToStringBuilder(this)
        .append("gameDir", gameDir)
        .append("name", name)
        .toString()

    class ProfileVersion(val profile: Profile, val version: String?)

    class Serializer : JsonSerializer<Profile?>, JsonDeserializer<Profile?> {
        override fun serialize(
            src: Profile?,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            if (src == null) return JsonNull.INSTANCE
            return JsonObject().apply {
                add("global", context.serialize(src.globalVersionSetting))
                addProperty("gameDir", src.gameDir.path)
                addProperty("selectedMinecraftVersion", src.selectedVersion)
            }
        }

        @Throws(JsonParseException::class)
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): Profile? {
            if (json === JsonNull.INSTANCE || json !is JsonObject) return null
            return Profile(
                "Default",
                File(json["gameDir"]?.asString ?: ""),
                context.deserialize(json["global"], VersionSetting::class.java),
                json["selectedMinecraftVersion"]?.asString ?: ""
            )
        }
    }
}
