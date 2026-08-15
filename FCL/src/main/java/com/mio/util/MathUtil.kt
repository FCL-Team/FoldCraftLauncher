package com.mio.util

import android.content.Context
import com.tungsten.fcllibrary.util.LocaleUtils

fun Long.format(context: Context): String {
    val isChinese = LocaleUtils.isChinese(context)
    val absNum = kotlin.math.abs(this)
    val sign = if (this < 0) "-" else ""

    return when {
        // 中文格式
        isChinese && absNum >= 100_000_000 -> {
            val value = absNum / 100_000_000.0
            sign + formatNumber(value) + "亿"
        }

        isChinese && absNum >= 10_000 -> {
            val value = absNum / 10_000.0
            sign + formatNumber(value) + "万"
        }
        // 英文格式
        !isChinese && absNum >= 1_000_000_000 -> {
            val value = absNum / 1_000_000_000.0
            sign + formatNumber(value) + "B"
        }

        !isChinese && absNum >= 1_000_000 -> {
            val value = absNum / 1_000_000.0
            sign + formatNumber(value) + "M"
        }

        !isChinese && absNum >= 1_000 -> {
            val value = absNum / 1_000.0
            sign + formatNumber(value) + "K"
        }

        else -> this.toString()
    }
}

fun Int.format(context: Context): String =
    toLong().format(context)

/** 保留两位小数，去掉末尾无意义的零和小数点 */
private fun formatNumber(value: Double): String {
    val formatted = "%.2f".format(value)
    return formatted.trimEnd('0').trimEnd('.')
}