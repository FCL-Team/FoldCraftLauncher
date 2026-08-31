package com.tungsten.fcl.control.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.mio.util.getScreenHeight
import com.mio.util.getScreenWidth
import com.tungsten.fcl.R
import com.tungsten.fcl.control.GameMenu
import com.tungsten.fclauncher.utils.FCLPath
import com.tungsten.fcllibrary.util.ConvertUtils
import java.io.File
import kotlin.math.abs

/**
 * 游戏内菜单悬浮按钮：轻点打开两侧抽屉，未锁定时可拖动换位置（位置按屏幕比例持久化）。
 */
class MenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val screenWidth = getScreenWidth()
    private val screenHeight = getScreenHeight()

    private val defaultWidth = ConvertUtils.dip2px(context, 40f)
    private val defaultHeight = ConvertUtils.dip2px(context, 40f)

    /** 点击判定位移阈值，随屏幕密度缩放（原硬编码 10px 在高密度屏上过小，点击易失灵） */
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private lateinit var gameMenu: GameMenu

    private var isGif = false
    private var pressed = false

    private var icon: Bitmap? = null
    private var srcRect: Rect? = null
    private var destRect: Rect? = null

    private lateinit var strokePaint: Paint
    private lateinit var areaPaint: Paint
    private lateinit var iconPaint: Paint

    fun setup(gameMenu: GameMenu) {
        this.gameMenu = gameMenu

        strokePaint = Paint().apply {
            isAntiAlias = true
            color = Color.DKGRAY
            style = Paint.Style.STROKE
            strokeWidth = ConvertUtils.dip2px(context, 2f).toFloat()
        }

        areaPaint = Paint().apply { isAntiAlias = true }
        iconPaint = Paint().apply { isAntiAlias = true }

        initIcon()
        if (!isGif) {
            val icon = checkNotNull(icon)
            srcRect = Rect(0, 0, icon.width, icon.height)
            destRect = Rect(
                ConvertUtils.dip2px(context, 6f),
                ConvertUtils.dip2px(context, 6f),
                ConvertUtils.dip2px(context, 34f),
                ConvertUtils.dip2px(context, 34f),
            )
        }
    }

    fun initPosition() {
        post {
            layoutParams.width = defaultWidth
            layoutParams.height = defaultHeight
            layoutParams = layoutParams
            x = (screenWidth * gameMenu.menuSetting.menuPositionX).toFloat()
            y = (screenHeight * gameMenu.menuSetting.menuPositionY).toFloat()
        }
    }

    private fun initIcon() {
        if (File(FCLPath.FILES_DIR, "menu_icon.png").exists()) {
            icon = BitmapFactory.decodeFile(File(FCLPath.FILES_DIR, "menu_icon.png").absolutePath)
        } else if (File(FCLPath.FILES_DIR, "menu_icon.gif").exists()) {
            isGif = true
            Glide.with(this).asGif().skipMemoryCache(true)
                .load(File(FCLPath.FILES_DIR, "menu_icon.gif"))
                .into(object : CustomViewTarget<MenuView, GifDrawable>(this) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                    }

                    override fun onResourceReady(
                        resource: GifDrawable,
                        transition: Transition<in GifDrawable>?,
                    ) {
                        background = resource
                        resource.start()
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {
                    }
                })
        }
        if (!isGif && icon == null) {
            icon = BitmapFactory.decodeResource(context.resources, R.drawable.img_app)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isGif) {
            return
        }
        val icon = icon ?: return
        val srcRect = srcRect ?: return
        val destRect = destRect ?: return
        if (pressed) {
            areaPaint.color = context.getColor(R.color.ui_bg_color)
        } else {
            areaPaint.color = Color.TRANSPARENT
        }
        val radius = (measuredWidth shr 1).toFloat()
        canvas.drawCircle(radius, radius, radius - ConvertUtils.dip2px(context, 1f), strokePaint)
        canvas.drawCircle(radius, radius, radius - ConvertUtils.dip2px(context, 2f), areaPaint)
        canvas.drawBitmap(icon, srcRect, destRect, iconPaint)
    }

    // 按下点的屏幕坐标：拖动会 setX/setY 移动 View 本身，松手时相对坐标差恒为 0，
    // 点击判定必须用不受 View 位移影响的 raw 坐标，否则快速拖动会被误判为点击
    private var downX = 0f
    private var downY = 0f
    private var downRawX = 0f
    private var downRawY = 0f
    private var downTime = 0L

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                downRawX = event.rawX
                downRawY = event.rawY
                downTime = event.eventTime
                pressed = true
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if (!gameMenu.menuSetting.isLockMenuView) {
                    val targetX = (x + event.x - downX)
                        .coerceIn(0f, (screenWidth - measuredWidth).toFloat())
                    val targetY = (y + event.y - downY)
                        .coerceIn(0f, (screenHeight - measuredHeight).toFloat())
                    x = targetX
                    y = targetY
                    gameMenu.menuSetting.menuPositionX = (targetX / screenWidth).toDouble()
                    gameMenu.menuSetting.menuPositionY = (targetY / screenHeight).toDouble()
                }
            }

            MotionEvent.ACTION_UP -> {
                // 点击 = 手指实际位移未超 touchSlop 且未到长按时长，打开两侧抽屉
                if (abs(event.rawX - downRawX) <= touchSlop
                    && abs(event.rawY - downRawY) <= touchSlop
                    && event.eventTime - downTime <= ViewConfiguration.getLongPressTimeout()
                ) {
                    performClick()
                    (gameMenu.layout as DrawerLayout).apply {
                        openDrawer(GravityCompat.START, true)
                        openDrawer(GravityCompat.END, true)
                    }
                }
                pressed = false
                invalidate()
            }

            MotionEvent.ACTION_CANCEL -> {
                // 父容器拦截/系统打断，不算点击，只复位按压态
                pressed = false
                invalidate()
            }
        }
        return true
    }
}
