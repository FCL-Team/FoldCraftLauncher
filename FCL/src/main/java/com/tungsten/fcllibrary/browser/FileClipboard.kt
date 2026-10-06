package com.tungsten.fcllibrary.browser

import java.io.File

/**
 * 文件浏览器的会话级剪贴板，进程存活期间全局共享，保存待复制或剪切的文件集合
 */
object FileClipboard {

    @JvmStatic
    var files: List<File> = emptyList()
        private set

    @JvmStatic
    var isCut = false
        private set

    @JvmStatic
    val isEmpty: Boolean
        get() = files.isEmpty()

    @JvmStatic
    fun copy(files: List<File>) {
        this.files = files.toList()
        isCut = false
    }

    @JvmStatic
    fun cut(files: List<File>) {
        this.files = files.toList()
        isCut = true
    }

    @JvmStatic
    fun clear() {
        files = emptyList()
    }
}
