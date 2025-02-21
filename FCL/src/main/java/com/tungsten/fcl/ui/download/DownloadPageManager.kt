package com.tungsten.fcl.ui.download

import android.content.Context
import com.tungsten.fcl.R
import com.tungsten.fcl.setting.Profile
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIListener
import com.tungsten.fcl.ui.manage.ManageUI.VersionLoadable
import com.tungsten.fclcore.mod.curse.CurseForgeRemoteModRepository
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout

class DownloadPageManager(
    context: Context?,
    parent: FCLUILayout?,
    defaultPageId: Int,
    listener: UIListener?
) : PageManager(context, parent, defaultPageId, listener) {
    companion object {
        @JvmStatic
        var instance: DownloadPageManager? = null
        const val PAGE_ID_DOWNLOAD_GAME: Int = 15010
        const val PAGE_ID_DOWNLOAD_MODPACK: Int = 15011
        const val PAGE_ID_DOWNLOAD_MOD: Int = 15012
        const val PAGE_ID_DOWNLOAD_RESOURCE_PACK: Int = 15013
        const val PAGE_ID_DOWNLOAD_WORLD: Int = 15014
        const val PAGE_ID_DOWNLOAD_SHADER_PACK: Int = 15015
    }

    var profile: Profile? = null
    var version: String? = null
    private lateinit var installVersionPage: InstallVersionPage
    private val downloadModpackPage: ModpackDownloadPage by lazy {
        ModpackDownloadPage(context, PAGE_ID_DOWNLOAD_MODPACK, parent, R.layout.page_download)
    }
    private val downloadModPage: ModDownloadPage by lazy {
        ModDownloadPage(context, PAGE_ID_DOWNLOAD_MOD, parent, R.layout.page_download)
    }
    private val downloadResourcePackPage: ResourcePackDownloadPage by lazy {
        ResourcePackDownloadPage(
            context,
            PAGE_ID_DOWNLOAD_RESOURCE_PACK,
            parent,
            R.layout.page_download
        )
    }
    private val downloadWorldPage: DownloadPage by lazy {
        DownloadPage(
            context,
            PAGE_ID_DOWNLOAD_WORLD,
            parent,
            R.layout.page_download,
            CurseForgeRemoteModRepository.WORLDS
        )
    }
    private val downloadShaderPackPage: ShaderPackDownloadPage by lazy {
        ShaderPackDownloadPage(
            context,
            PAGE_ID_DOWNLOAD_SHADER_PACK,
            parent,
            R.layout.page_download
        )
    }

    init {
        instance = this
    }

    override fun init(listener: UIListener?) {
        installVersionPage = InstallVersionPage(
            context,
            PAGE_ID_DOWNLOAD_GAME,
            parent,
            R.layout.page_install_version
        )
        listener?.onLoad()
    }

    override fun getAllPages(): ArrayList<FCLCommonPage> {
        return ArrayList<FCLCommonPage>().apply {
            add(installVersionPage)
        }
    }

    override fun createPageById(id: Int): FCLCommonPage? {
        val page: FCLCommonPage? = when (id) {
            PAGE_ID_DOWNLOAD_MODPACK -> downloadModpackPage
            PAGE_ID_DOWNLOAD_MOD -> downloadModPage
            PAGE_ID_DOWNLOAD_RESOURCE_PACK -> downloadResourcePackPage
            PAGE_ID_DOWNLOAD_WORLD -> downloadWorldPage
            PAGE_ID_DOWNLOAD_SHADER_PACK -> downloadShaderPackPage
            else -> null
        }
        if (page != null) {
            allPages.add(page)
            (page as VersionLoadable).loadVersion(profile, version)
        }
        return page
    }

    fun loadVersion(profile: Profile?, version: String?) {
        this.profile = profile
        this.version = version
        allPages.forEach {
            if (it is VersionLoadable) {
                (it as VersionLoadable).loadVersion(profile, version)
            }
        }
    }
}
