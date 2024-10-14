package com.tungsten.fcl.ui.controller

import android.content.Context
import com.tungsten.fcl.R
import com.tungsten.fcl.ui.PageManager
import com.tungsten.fcl.ui.UIListener
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

    private lateinit var controllerManagePage: ControllerManagePage
    private val controllerRepoPage: ControllerRepoPage by lazy {
        ControllerRepoPage(
            context,
            PAGE_ID_CONTROLLER_REPO,
            parent,
            R.layout.page_controller_repo
        )
    }

    init {
        instance = this
    }

    override fun init(listener: UIListener?) {
        controllerManagePage = ControllerManagePage(
            context,
            PAGE_ID_CONTROLLER_MANAGER,
            parent,
            R.layout.page_controller_manager
        )
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
