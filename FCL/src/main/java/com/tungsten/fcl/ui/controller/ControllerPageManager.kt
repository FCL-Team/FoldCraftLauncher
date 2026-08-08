package com.tungsten.fcl.ui.controller

import android.content.Context
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIListener
import com.tungsten.fcl.ui.controller.compose.ComposeControllerManagePage
import com.tungsten.fcl.ui.controller.compose.ComposeControllerRepoPage
import com.tungsten.fcllibrary.component.ui.FCLCommonPage
import com.tungsten.fcllibrary.component.view.FCLUILayout

class ControllerPageManager(
    context: Context?,
    parent: FCLUILayout?,
    defaultPageId: Int,
    listener: UIListener?
) : PageManager(context, parent, defaultPageId, listener) {
    companion object {
        const val PAGE_ID_CONTROLLER_MANAGER: Int = 15040
        const val PAGE_ID_CONTROLLER_REPO: Int = 15041

        @JvmStatic
        var instance: ControllerPageManager? = null
    }

    private lateinit var controllerManagePage: FCLCommonPage
    private val controllerRepoPage: FCLCommonPage by lazy {
        ComposeControllerRepoPage(context!!, PAGE_ID_CONTROLLER_REPO, parent!!)
    }

    init {
        instance = this
    }

    override fun init(listener: UIListener?) {
        // 旧 View 页面（ControllerManagePage/ControllerRepoPage 及 XML）已随批3固化删除。
        controllerManagePage = ComposeControllerManagePage(context!!, PAGE_ID_CONTROLLER_MANAGER, parent!!)
        listener?.onLoad()
    }

    override fun getAllPages(): ArrayList<FCLCommonPage> {
        return ArrayList<FCLCommonPage>().apply {
            add(controllerManagePage)
        }
    }

    override fun createPageById(id: Int): FCLCommonPage? {
        val page: FCLCommonPage? = when (id) {
            PAGE_ID_CONTROLLER_REPO -> controllerRepoPage
            else -> null
        }
        if (page != null) {
            allPages.add(page)
        }
        return page
    }
}
