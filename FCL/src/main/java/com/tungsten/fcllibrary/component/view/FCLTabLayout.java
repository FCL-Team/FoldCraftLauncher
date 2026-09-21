package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import com.google.android.material.tabs.TabLayout;
import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;

public class FCLTabLayout extends TabLayout {

    /** 滑动指示箭头半宽/半高、线宽与距边缘间距（dp） */
    private static final float ARROW_SIZE_DP = 6f;
    private static final float ARROW_STROKE_DP = 2.5f;
    private static final float ARROW_MARGIN_DP = 3f;

    private final Paint arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path arrowPath = new Path();
    private boolean canScrollLeft;
    private boolean canScrollRight;

    private BooleanProperty visibilityProperty;
    private final boolean followTheme;
    private final boolean autoTextTint;

    /** 主题刷新回调（registerEvent 注册，主题变化时全量执行） */
    private void refreshTheme() {
            int[][] state = {
                    {
                            android.R.attr.state_selected
                    },
                    {

                    }
            };
            // 图标 Tab 与界面文字同色系：选中为对比色，未选中为半透明对比色（当前仅游戏菜单使用图标 Tab）
            int[] iconColor = {
                    ThemeEngine.getInstance().getTheme().getAutoTint(),
                    ThemeEngine.getInstance().getTheme().getAutoHintTint()
            };
            // 文字 Tab 默认选中为主题深色、未选中灰；autoTextTint 开启后与图标 Tab 同色系
            int[] color = autoTextTint ? iconColor : new int[]{
                    ThemeEngine.getInstance().getTheme().getDkColor(),
                    followTheme ? ThemeEngine.getInstance().getTheme().getAutoTint() : Color.GRAY
            };
            int[][] bgState = {
                    {

                    }
            };
            int[] bgColor = {
                    ThemeEngine.getInstance().getTheme().getLtColor()
            };
            setSelectedTabIndicatorColor(ThemeEngine.getInstance().getTheme().getDkColor());
            setTabTextColors(new ColorStateList(state, color));
            setTabIconTint(new ColorStateList(state, iconColor));
            if (followTheme) {
                setBackgroundTintList(new ColorStateList(bgState, bgColor));
            }
            // 滑动指示箭头颜色在 dispatchDraw 按主题现取，这里触发重绘
            invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateScrollIndicatorState();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        updateScrollIndicatorState();
    }

    /** 更新左右可滑动状态，变化时重绘指示箭头 */
    private void updateScrollIndicatorState() {
        boolean left = canScrollHorizontally(-1);
        boolean right = canScrollHorizontally(1);
        if (left != canScrollLeft || right != canScrollRight) {
            canScrollLeft = left;
            canScrollRight = right;
            invalidate();
        }
    }

    /** 绘制在 tab 内容之上，箭头固定于边缘不随滑动移动 */
    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!canScrollLeft && !canScrollRight) {
            return;
        }
        // dispatchDraw 画布被框架平移了 (mLeft - mScrollX, mTop - mScrollY)，抵消回视图固定坐标
        int save = canvas.save();
        canvas.translate(getScrollX(), getScrollY());
        float cy = getHeight() / 2f;
        float size = dp(ARROW_SIZE_DP);
        float margin = dp(ARROW_MARGIN_DP);
        // 箭头 dkColor 与 tab 栏 ltColor 底色明暗相对，强制不透明避免随主题 colorAlpha 变淡
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setStrokeWidth(dp(ARROW_STROKE_DP));
        arrowPaint.setStrokeCap(Paint.Cap.ROUND);
        arrowPaint.setStrokeJoin(Paint.Join.ROUND);
        arrowPaint.setColor(ColorUtils.setAlphaComponent(
                ThemeEngine.getInstance().getTheme().getDkColor(), 255));
        if (canScrollLeft) {
            drawChevron(canvas, margin, cy, size, false);
        }
        if (canScrollRight) {
            drawChevron(canvas, getWidth() - margin, cy, size, true);
        }
        canvas.restoreToCount(save);
    }

    private void drawChevron(Canvas canvas, float cx, float cy, float size, boolean right) {
        float dx = right ? -size : size;
        arrowPath.reset();
        arrowPath.moveTo(cx + dx, cy - size);
        arrowPath.lineTo(cx, cy);
        arrowPath.lineTo(cx + dx, cy + size);
        canvas.drawPath(arrowPath, arrowPaint);
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }

    public FCLTabLayout(@NonNull Context context) {
        super(context);
        followTheme = false;
        autoTextTint = false;
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTabLayout);
        followTheme = typedArray.getBoolean(R.styleable.FCLTabLayout_follow_theme, false);
        autoTextTint = typedArray.getBoolean(R.styleable.FCLTabLayout_auto_text_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public FCLTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTabLayout);
        followTheme = typedArray.getBoolean(R.styleable.FCLTabLayout_follow_theme, false);
        autoTextTint = typedArray.getBoolean(R.styleable.FCLTabLayout_auto_text_tint, false);
        typedArray.recycle();
        ThemeEngine.getInstance().registerEvent(this, this::refreshTheme);
    }

    public boolean isFollowTheme() {
        return followTheme;
    }

    public final void setVisibilityValue(boolean visibility) {
        visibilityProperty().set(visibility);
    }

    public final boolean getVisibilityValue() {
        return visibilityProperty == null || visibilityProperty.get();
    }

    public final BooleanProperty visibilityProperty() {
        if (visibilityProperty == null) {
            visibilityProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean visible = get();
                        setVisibility(visible ? VISIBLE : GONE);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "visibility";
                }
            };
        }

        return visibilityProperty;
    }
}
