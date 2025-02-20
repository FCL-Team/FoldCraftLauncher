package com.mio.util

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.io.File

class GuideUtil {
    companion object {
        const val TAG_GUIDE_THEME_2 = "theme2"
        private val tagList = mutableListOf<String>()
        private val file = File(FCLPath.FILES_DIR + "/guide_tag.txt")

        init {
            if (!file.exists()) {
                file.createNewFile()
            }
            file.readLines().forEach {
                tagList.add(it)
            }
        }

        fun show(activity: Activity, view: View, title: String) {
            TapTargetView.showFor(
                activity, TapTarget.forView(view, title)
                    .transparentTarget(true)
                    .titleTextColorInt(ThemeEngine.getInstance().getTheme().autoTint)
                    .outerCircleColorInt(ThemeEngine.getInstance().getTheme().ltColor)
            )
        }

        fun show(dialog: Dialog, view: View, title: String) {
            TapTargetView.showFor(
                dialog, TapTarget.forView(view, title)
                    .transparentTarget(true)
                    .titleTextColorInt(ThemeEngine.getInstance().getTheme().autoTint)
                    .outerCircleColorInt(ThemeEngine.getInstance().getTheme().ltColor)
            )
        }

        private fun addTag(tag:String) {
            tagList.add(tag)
            tagList.forEach {
                file.writeText(it)
            }
        }

        fun show(activity: Activity, view: View, title: String, tag: String) {
            if (!tagList.contains(tag)) {
                addTag(tag)
                show(activity, view, title)
            }
        }

        fun show(dialog: Dialog, view: View, title: String, tag: String) {
            if (!tagList.contains(tag)) {
                addTag(tag)
                show(dialog, view, title)
            }
        }

    }
}