package com.mio.util

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.component.theme.ThemeEngine
import java.io.File

class GuideUtil {
    companion object {
        const val TAG_GUIDE_THEME_2 = "theme2"
        const val TAG_GUIDE_SHARE_LOG = "share log"
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
            TapTargetView.showFor(activity, view.guideTarget(title = title))
        }

        fun show(dialog: Dialog, view: View, title: String) {
            TapTargetView.showFor(dialog, view.guideTarget(title = title))
        }

        fun show(activity: Activity, targets: List<TapTarget>) {
            TapTargetSequence(activity).targets(targets).defaultConfig().start()
        }

        fun show(dialog: Dialog, targets: List<TapTarget>) {
            TapTargetSequence(dialog).targets(targets).defaultConfig().start()
        }

        private fun TapTargetSequence.defaultConfig() = this.apply {
            considerOuterCircleCanceled(false) //点击空白区域不取消队列
            continueOnCancel(true) // 点击空白区域时(TapTarget取消回调)，仍继续队列
        }

        private fun addTag(tag: String) {
            tagList.add(tag)
            file.writeText(tagList.joinToString("\n"))
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

        fun show(activity: Activity, vararg targetsWithTag: Pair<String, TapTarget>) {
            mapTargets(*targetsWithTag)?.let { targetToShow ->
                show(activity, targetToShow)
            }
        }

        fun show(dialog: Dialog, vararg targetsWithTag: Pair<String, TapTarget>) {
            mapTargets(*targetsWithTag)?.let { targetToShow ->
                show(dialog, targetToShow)
            }
        }

        private fun mapTargets(vararg targetsWithTag: Pair<String, TapTarget>): List<TapTarget>? {
            return targetsWithTag.mapNotNull { (tag, target) ->
                if (!tagList.contains(tag)) {
                    addTag(tag)
                    target
                } else {
                    null
                }
            }.takeIf { it.isNotEmpty() }
        }

        fun View.guideTarget(title: String, description: String? = null): TapTarget {
            return (if (description == null) {
                TapTarget.forView(this, title)
            } else {
                TapTarget.forView(this, title, description)
            })
                .transparentTarget(true)
                .titleTextColorInt(ThemeEngine.getInstance().getTheme().autoTint)
                .outerCircleColorInt(ThemeEngine.getInstance().getTheme().ltColor)
        }
    }
}