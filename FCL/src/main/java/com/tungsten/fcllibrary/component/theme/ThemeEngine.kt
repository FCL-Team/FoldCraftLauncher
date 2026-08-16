package com.tungsten.fcllibrary.component.theme

import android.app.WallpaperManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.mio.util.ImageUtil
import com.tungsten.fcl.R
import com.tungsten.fcllibrary.util.ConvertUtils
import com.tungsten.fclauncher.utils.FCLPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

/**
 * 主题单例（Repository）：持有 [ThemeData] 的 StateFlow，
 * 控件/页面通过 [registerEvent] 注册刷新回调（全量刷新，替代原 fakefx 属性绑定）。
 */
object ThemeEngine {

    private val _theme = MutableStateFlow<ThemeData?>(null)

    /** 当前主题（StateFlow，初始化前为 null，新代码可 collect 感知主题变化）。
     *  getter 命名为 getThemeFlow 避免与 getTheme()（返回 ThemeData）在 Java 侧签名冲突 */
    @get:JvmName("getThemeFlow")
    val theme: StateFlow<ThemeData?> = _theme.asStateFlow()

    val handler = Handler(Looper.getMainLooper())
    private val runnables = HashMap<View, Runnable>()
    private val refreshListeners = ArrayList<Runnable>()

    /** Java 兼容：返回单例自身（替代原 getInstance()） */
    @JvmStatic
    fun getInstance(): ThemeEngine = this

    /** 初始化主题（幂等，FCLActivity.onCreate 首行调用） */
    @JvmStatic
    fun setupThemeEngine(context: Context) {
        if (_theme.value != null) return
        _theme.value = ThemeData.getTheme(context)
    }

    /** 当前主题（调用方遵循先 setup 的约定；未初始化时抛异常，与原 getTheme() 返回 null 后使用崩等价）。
     *  非 @JvmStatic：Java 侧经 getInstance().getTheme() 实例调用（@JvmStatic 会同时生成静态方法导致链式调用歧义） */
    fun getTheme(): ThemeData = _theme.value!!

    /** 注册控件/页面的主题刷新回调（注册后立即执行一次） */
    @JvmStatic
    fun registerEvent(view: View, runnable: Runnable) {
        runnables[view] = runnable
        handler.post(runnable)
    }

    @JvmStatic
    fun unregisterEvent(view: View) {
        runnables.remove(view)
    }

    @JvmStatic
    fun addRefreshListener(runnable: Runnable) {
        refreshListeners.add(runnable)
    }

    @JvmStatic
    fun removeRefreshListener(runnable: Runnable) {
        refreshListeners.remove(runnable)
    }

    /** 全量刷新：控件回调 + 全局刷新监听 */
    @JvmStatic
    fun refreshTheme() {
        notifyThemeChanged()
    }

    private fun notifyThemeChanged() {
        for ((view, runnable) in runnables) {
            if (view != null) handler.post(runnable)
        }
        for (runnable in refreshListeners) {
            handler.post(runnable)
        }
    }

    /** 更新主题数据并全量刷新 */
    private fun updateTheme(transform: (ThemeData) -> ThemeData) {
        val current = _theme.value ?: return
        _theme.value = transform(current)
        notifyThemeChanged()
    }

    /** 亮暗判断：FCL 自身主题模式（0 跟随系统 / 1 强制亮 / 2 强制暗）优先于 uiMode */
    @JvmStatic
    fun isNightMode(context: Context): Boolean {
        val themeMode = context.getSharedPreferences("launcher", Context.MODE_PRIVATE).getInt("themeMode", 0)
        if (themeMode == 1) return false
        if (themeMode == 2) return true
        return (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    @JvmStatic
    fun getSystemAutoTint(context: Context): Int =
        if (isNightMode(context)) Color.WHITE else Color.BLACK

    @JvmStatic
    fun applyColor(color: Int) {
        updateTheme { it.copy(color = color) }
    }

    @JvmStatic
    fun applyColor2(color: Int) {
        updateTheme { it.copy(color2 = color) }
    }

    @JvmStatic
    fun applyColor2Dark(color: Int) {
        updateTheme { it.copy(color2Dark = color) }
    }

    @JvmStatic
    fun applyFullscreen(window: Window?, fullscreen: Boolean) {
        updateTheme { it.copy(fullscreen = fullscreen) }
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val params = window.attributes
                params.layoutInDisplayCutoutMode = if (fullscreen)
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                else
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
                window.attributes = params
            }
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private fun applyBackground(context: Context, view: View?, ltPath: String?, dkPath: String?) {
        runCatching {
            if (ltPath != null && File(ltPath).exists()) {
                File(ltPath).copyTo(File(FCLPath.LT_BACKGROUND_PATH), overwrite = true)
            }
            if (dkPath != null && File(dkPath).exists()) {
                File(dkPath).copyTo(File(FCLPath.DK_BACKGROUND_PATH), overwrite = true)
            }
        }
        val ltBitmap = ImageUtil.load(FCLPath.LT_BACKGROUND_PATH)
            .orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_light))
        val dkBitmap = ImageUtil.load(FCLPath.DK_BACKGROUND_PATH)
            .orElse(ConvertUtils.getBitmapFromRes(context, R.drawable.background_dark))
        val lt = BitmapDrawable(context.resources, ltBitmap)
        val dk = BitmapDrawable(context.resources, dkBitmap)
        updateTheme { it.copy(backgroundLt = lt, backgroundDk = dk) }
        if (view != null) {
            val isNight = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            ImageUtil.loadInto(view, if (isNight) dk else lt)
        }
    }

    @JvmStatic
    fun applyAndSave(context: Context, color: Int) {
        applyColor(color)
        getTheme()?.let { ThemeData.saveTheme(context, it) }
    }

    @JvmStatic
    fun applyAndSave2(context: Context, color: Int) {
        applyColor2(color)
        getTheme()?.let { ThemeData.saveTheme(context, it) }
    }

    @JvmStatic
    fun applyAndSave2Dark(context: Context, color: Int) {
        applyColor2Dark(color)
        getTheme()?.let { ThemeData.saveTheme(context, it) }
    }

    @JvmStatic
    fun applyAndSave(context: Context, window: Window, fullscreen: Boolean) {
        applyFullscreen(window, fullscreen)
        getTheme()?.let { ThemeData.saveTheme(context, it) }
    }

    @JvmStatic
    fun applyAndSave(context: Context, view: View, lt: String?, dk: String?) {
        applyBackground(context, view, lt, dk)
        getTheme()?.let { ThemeData.saveTheme(context, it) }
    }

    /** 关闭皮肤模型开关（替代原 Theme.setiIgnoreSkinContainer 字段直改） */
    @JvmStatic
    fun setCloseSkinModel(closeSkinModel: Boolean) {
        updateTheme { it.copy(closeSkinModel = closeSkinModel) }
    }

    /** 动画速度（替代原 animationSpeedProperty().set 字段直改） */
    @JvmStatic
    fun setAnimationSpeed(animationSpeed: Int) {
        updateTheme { it.copy(animationSpeed = animationSpeed) }
    }

    @JvmStatic
    fun getWallpaperColor(context: Context): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val colors = WallpaperManager.getInstance(context).getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
            if (colors != null && colors.primaryColor != null) {
                return colors.primaryColor!!.toArgb()
            }
        }
        return Color.parseColor("#7797CF")
    }
}
