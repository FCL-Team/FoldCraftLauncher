package com.mio.util

import com.mio.controlconverter.CcConverter
import com.mio.controlconverter.CcJson
import java.io.File

/**
 * FCL 控制布局与 ZalithLauncher2 (ZL2) 控制布局之间的纯 Kotlin 转换门面。
 *
 * 语义基准为 control-converter 项目的 cc.py（Python 参考实现），
 * 替代旧的 libcc.so (Go/cgo) JNI 方案：不再依赖特定 ABI，全部平台可用。
 *
 * 调用约定与旧 JNI 版一致：同步阻塞，成功返回 null，失败返回错误信息；调用方需在后台线程执行。
 */
object LayoutConverter {

    /** 纯 Kotlin 实现无 ABI 限制，恒为 true（保留方法以兼容既有调用点）。 */
    @JvmStatic
    fun isSupported(): Boolean = true

    /**
     * 将 FCL 控制布局 JSON 转换为 ZL2 格式。
     *
     * @return 转换成功返回 null；失败返回错误信息
     */
    fun convertFclToZl2(input: File, output: File): String? = runCatching {
        val source = CcJson.loadJson(input.readText())
        val result = CcConverter.convertFclToZl(source)
        output.parentFile?.mkdirs()
        output.writeText(CcJson.encodePretty(result))
        null
    }.getOrElse { t -> "${t.javaClass.simpleName}: ${t.message}" }

    /**
     * 将 ZL2 控制布局 JSON 转换为 FCL 格式。
     *
     * @return 转换成功返回 null；失败返回错误信息
     */
    fun convertZl2ToFcl(input: File, output: File): String? = runCatching {
        val source = CcJson.loadJson(input.readText())
        val result = CcConverter.convertZlToFcl(source)
        output.parentFile?.mkdirs()
        output.writeText(CcJson.encodePretty(result))
        null
    }.getOrElse { t -> "${t.javaClass.simpleName}: ${t.message}" }

    /** 判断布局 JSON 文本是否为 ZL2 格式（导入时自动识别用）。 */
    @JvmStatic
    fun isZl2Layout(jsonText: String): Boolean = CcConverter.isZl2Layout(jsonText)
}
