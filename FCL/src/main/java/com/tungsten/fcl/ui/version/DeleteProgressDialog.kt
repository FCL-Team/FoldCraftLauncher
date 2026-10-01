package com.tungsten.fcl.ui.version

import android.content.Context
import android.view.View
import android.widget.ScrollView
import com.tungsten.fcl.R
import com.tungsten.fclcore.task.Schedulers
import com.tungsten.fcllibrary.component.dialog.FCLDialog
import com.tungsten.fcllibrary.component.view.FCLTextView

/**
 * 版本删除进度对话框：实时滚动展示删除过程中的每一步动作，删除完成前不可关闭。
 */
class DeleteProgressDialog(context: Context, title: String) : FCLDialog(context) {

    private val logScroll: ScrollView
    private val logView: FCLTextView

    init {
        setContentView(R.layout.dialog_delete_version)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        findViewById<FCLTextView>(R.id.title)!!.text = title
        logScroll = findViewById(R.id.logScroll)!!
        logView = findViewById(R.id.logView)!!
        show()
    }

    /** 追加一行进度信息并滚动到底部，可在任意线程调用 */
    fun appendLog(message: String) {
        Schedulers.androidUIThread().execute {
            logView.append(message)
            logView.append("\n")
            logScroll.post { logScroll.fullScroll(View.FOCUS_DOWN) }
        }
    }
}
