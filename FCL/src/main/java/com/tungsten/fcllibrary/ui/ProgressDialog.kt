package com.tungsten.fcllibrary.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLProgressBar
import androidx.core.graphics.drawable.toDrawable

class ProgressDialog(context: Context) : FCLDialog(context) {

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        // 覆盖 FCLDialog 的圆角卡片背景：进度框直接浮于全屏模糊背景上
        window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        setContentView(FCLProgressBar(context))
        show()
    }
}