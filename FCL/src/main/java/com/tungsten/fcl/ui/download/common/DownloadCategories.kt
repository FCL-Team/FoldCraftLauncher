package com.tungsten.fcl.ui.download.common

import com.tungsten.fcl.ui.download.DownloadUI

/**
 * 下载分类统一条目：同一分类在 CurseForge / Modrinth 的 id，null 表示该源无此分类。
 * 显示名走既有分类本地化（curse_category_* / modrinth_category_*），未命中时用 [fallback]。
 */
data class DownloadCategory(val cfId: Int?, val mrId: String?, val fallback: String) {

    /** 「全部」条目（两源均不过滤） */
    val isAll: Boolean
        get() = cfId == null && mrId == null

    /** 本地化资源 key */
    val key: String
        get() = if (cfId != null) "curse_category_$cfId"
        else "modrinth_category_" + mrId!!.replace("-", "_").replace("+", "")

    companion object {
        /** 全部 */
        @JvmField
        val ALL = DownloadCategory(null, null, "All")

        /** 由 PCL 格式的分类标记（"cfId/mrSlug"，缺侧留空）构建条目 */
        private fun entry(tag: String, fallback: String): DownloadCategory {
            val cf = tag.substringBefore('/')
            val mr = tag.substringAfter('/')
            return DownloadCategory(cf.toIntOrNull(), mr.ifEmpty { null }, fallback)
        }

        /** 按下载模式取统一分类表（首项恒为「全部」） */
        @JvmStatic
        fun forPageId(pageId: Int): List<DownloadCategory> = when (pageId) {
            DownloadUI.PAGE_ID_DOWNLOAD_MOD -> MOD
            DownloadUI.PAGE_ID_DOWNLOAD_MODPACK -> MODPACK
            DownloadUI.PAGE_ID_DOWNLOAD_RESOURCE_PACK -> RESOURCE_PACK
            DownloadUI.PAGE_ID_DOWNLOAD_SHADER_PACK -> SHADER
            else -> emptyList()
        }

        // 分类映射取自 PCL 的 PageDownloadMod / PageDownloadPack / PageDownloadResourcePack / PageDownloadShader 各 xaml
        // （https://github.com/Meloong-Git/PCL/blob/main/Plain%20Craft%20Launcher%202/Pages/PageDownload/Resource/PageDownloadMod.xaml）
        private val MOD = listOf(
            ALL,
            entry("406/worldgen", "World Gen"),
            entry("407/", "Biomes"),
            entry("410/", "Dimensions"),
            entry("408/", "Ores and Resources"),
            entry("409/", "Structures"),
            entry("412/technology", "Technology"),
            entry("415/", "Energy, Fluid, and Item Transport"),
            entry("4843/", "Automation"),
            entry("417/", "Energy"),
            entry("4558/", "Redstone"),
            entry("436/food", "Food"),
            entry("416/", "Farming"),
            entry("/game-mechanics", "Game Mechanics"),
            entry("414/transportation", "Transportation"),
            entry("420/storage", "Storage"),
            entry("419/magic", "Magic"),
            entry("422/adventure", "Adventure"),
            entry("424/decoration", "Decoration"),
            entry("411/mobs", "Mobs"),
            entry("5191/utility", "Utility"),
            entry("434/equipment", "Equipment"),
            entry("9026/", "Creative"),
            entry("6814/optimization", "Optimization"),
            entry("423/", "Map and Information"),
            entry("435/social", "Social"),
            entry("421/library", "Library"),
        )

        private val MODPACK = listOf(
            ALL,
            entry("4484/", "Multiplayer"),
            entry("/optimization", "Optimization"),
            entry("4479/challenging", "Challenging"),
            entry("4483/combat", "Combat"),
            entry("4478/quests", "Quests"),
            entry("4472/technology", "Technology"),
            entry("4473/magic", "Magic"),
            entry("4475/adventure", "Adventure and RPG"),
            entry("/kitchen-sink", "Kitchen Sink"),
            entry("4476/", "Exploration"),
            entry("4477/", "Mini Game"),
            entry("4474/", "Sci-Fi"),
            entry("4736/", "Skyblock"),
            entry("5128/", "Vanilla+"),
            entry("4487/", "FTB Official Pack"),
            entry("4480/", "Map Based"),
            entry("4481/lightweight", "Lightweight"),
            entry("4482/", "Extra Large"),
        )

        private val RESOURCE_PACK = listOf(
            ALL,
            entry("403/vanilla-like", "Vanilla-like"),
            entry("400/realistic", "Realistic"),
            entry("401/", "Modern"),
            entry("402/", "Medieval"),
            entry("399/", "Steampunk"),
            entry("/themed", "Themed"),
            entry("/simplistic", "Simplistic"),
            entry("/decoration", "Decoration"),
            entry("/combat", "Combat"),
            entry("/utility", "Utility"),
            entry("/tweaks", "Tweaks"),
            entry("/cursed", "Cursed"),
            entry("/entities", "Entities"),
            entry("/audio", "Audio"),
            entry("5244/fonts", "Font Pack"),
            entry("/models", "Models"),
            entry("/locale", "Locale"),
            entry("/gui", "GUI"),
            entry("/core-shaders", "Core Shaders"),
            entry("404/", "Animated"),
            entry("4465/modded", "Modded Support"),
            entry("/8x-", "8x or lower"),
            entry("393/16x", "16x"),
            entry("394/32x", "32x"),
            entry("/48x", "48x"),
            entry("395/64x", "64x"),
            entry("396/128x", "128x"),
            entry("397/256x", "256x"),
            entry("398/512x+", "512x or higher"),
        )

        private val SHADER = listOf(
            ALL,
            entry("6555/vanilla-like", "Vanilla-like"),
            entry("6554/fantasy", "Fantasy"),
            entry("6553/realistic", "Realistic"),
            entry("/semi-realistic", "Semi-realistic"),
            entry("/cartoon", "Cartoon"),
            entry("/colored-lighting", "Colored Lighting"),
            entry("/path-tracing", "Path Tracing"),
            entry("/pbr", "PBR"),
            entry("/reflections", "Reflections"),
            entry("/potato", "Potato"),
            entry("/low", "Low"),
            entry("/medium", "Medium"),
            entry("/high", "High"),
            entry("/vanilla", "Vanilla"),
            entry("/iris", "Iris"),
            entry("/optifine", "OptiFine"),
        )
    }
}
