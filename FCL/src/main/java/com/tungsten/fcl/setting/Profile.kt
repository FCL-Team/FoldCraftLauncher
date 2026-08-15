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
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fclcore.download.DefaultDependencyManager
import com.tungsten.fclcore.download.DownloadProvider
import com.tungsten.fclcore.event.EventBus
import com.tungsten.fclcore.event.EventPriority
import com.tungsten.fclcore.event.RefreshedVersionsEvent
import com.tungsten.fclcore.game.Version
import com.tungsten.fclcore.util.ToStringBuilder
import java.io.File
import java.lang.reflect.Type
import java.util.Optional

/**
 * 游戏目录配置。使用普通类型字段，不再依赖 fakefx property；
 * 选中版本变化通过 [addSelectedVersionListener] 通知。
 */
@JsonAdapter(Profile.Serializer::class)
class Profile {
    private val listenerHolder = WeakListenerHolder()

    /** 游戏仓库（构造时按初始目录创建，目录变化时切换） */
    val repository: FCLGameRepository

    /** 选中版本（变化时校验有效性并通知监听者） */
    var selectedVersion: String? = null
        get() = field
        set(value) {
            if (field == value) return
            field = value
            checkSelectedVersion()
            // 复制后遍历：回调内可能增删监听（如 DownloadUI 切换监听对象），避免并发修改
            selectedVersionListeners.toList().forEach { it.run() }
        }

    /** 游戏目录（变化时切换仓库目录） */
    var gameDir: File = File("")
        set(value) {
            field = value
            repository.changeDirectory(value)
        }

    /** 全局设置（变化时触发 [onGlobalChanged]） */
    val global: VersionSetting

    /** 全局设置变化回调（由 Profiles 设置，用于触发配置保存） */
    var onGlobalChanged: (() -> Unit)? = null

    /** 名称 */
    var name: String

    private val selectedVersionListeners = mutableListOf<Runnable>()

    /** 注册选中版本变化监听（setter 同步通知，调用线程即回调线程） */
    fun addSelectedVersionListener(listener: Runnable) {
        selectedVersionListeners.add(listener)
    }

    fun removeSelectedVersionListener(listener: Runnable) {
        selectedVersionListeners.remove(listener)
    }

    constructor(name: String) : this(name, File(FCLPath.SHARED_COMMON_DIR))

    constructor(name: String, initialGameDir: File) : this(name, initialGameDir, VersionSetting())

    constructor(name: String, initialGameDir: File, global: VersionSetting) :
        this(name, initialGameDir, global, null)

    constructor(name: String, initialGameDir: File, global: VersionSetting?, selectedVersion: String?) {
        this.name = name
        this.repository = FCLGameRepository(this, initialGameDir)
        this.gameDir = initialGameDir
        this.global = global ?: VersionSetting()
        this.global.addOnChangeListener { onGlobalChanged?.invoke() }
        this.selectedVersion = selectedVersion

        listenerHolder.add(
            EventBus.EVENT_BUS.channel(RefreshedVersionsEvent::class.java)
                .registerWeak({ checkSelectedVersion() }, EventPriority.HIGHEST)
        )
    }

    private fun checkSelectedVersion() {
        if (!repository.isLoaded()) return
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

    fun getDependency(): DefaultDependencyManager = getDependency(DownloadProviders.getDownloadProvider())

    fun getDependency(downloadProvider: DownloadProvider): DefaultDependencyManager =
        DefaultDependencyManager(repository, downloadProvider, FCLCacheRepository.REPOSITORY)

    /** 选中版本的设置（Java 侧访问 getVersionSetting()） */
    val versionSetting: VersionSetting
        get() = repository.getVersionSetting(selectedVersion)

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
                add("global", context.serialize(src.global))
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
            val obj = json as JsonObject
            val gameDir = Optional.ofNullable(obj.get("gameDir")).map { it.asString }.orElse("")
            return Profile(
                "Default",
                File(gameDir),
                context.deserialize(obj.get("global"), VersionSetting::class.java),
                Optional.ofNullable(obj.get("selectedMinecraftVersion")).map { it.asString }.orElse("")
            )
        }
    }
}
