package com.mio.util

import android.content.Context
import com.tungsten.fcllibrary.R
import com.tungsten.fcllibrary.component.dialog.FCLAlertDialog

fun showErrorDialog(context: Context, message: Int, vararg args: String) {
    showErrorDialog(context, context.getString(message, *args))
}

fun showErrorDialog(context: Context, message: String) {
    FCLAlertDialog.Builder(context)
        .setAlertLevel(FCLAlertDialog.AlertLevel.ALERT)
        .setMessage(message)
        .setNegativeButton(context.getString(R.string.dialog_positive)) { }
        .create()
        .show()
}