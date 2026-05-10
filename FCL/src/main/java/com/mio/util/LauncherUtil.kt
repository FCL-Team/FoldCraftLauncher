package com.mio.util

import android.content.Context
import com.tungsten.fcl.R

fun getLauncherName(context: Context): String {
    val appName = context.getString(R.string.app_name)
    val appVersion = context.getString(R.string.app_version)
    val versionType = context.getSharedPreferences("launcher", Context.MODE_PRIVATE)
        .getString("custom_launcher_name", appName)
    return if (versionType!!.isEmpty()) "$appName/$appVersion" else versionType.replace(
        $$"${launcher_version}",
        appVersion
    )
}