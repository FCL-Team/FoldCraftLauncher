package com.tungsten.fcl.control.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.tungsten.fcl.FCLApplication;
import com.tungsten.fcl.R;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.util.AndroidUtils;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class MenuView extends View {

    private final int screenWidth;
    private final int screenHeight;

    private final int DEFAULT_WIDTH = ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 40);
    private final int DEFAULT_HEIGHT = ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 40);

    private GameMenu gameMenu;

    public MenuView(Context context) {
        super(context);
        this.screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
        this.screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
        this.screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());
    }

    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.screenWidth = AndroidUtils.getScreenWidth(FCLApplication.getCurrentActivity());
        this.screenHeight = AndroidUtils.getScreenHeight(FCLApplication.getCurrentActivity());
    }

    public void setup(GameMenu gameMenu) {
        this.gameMenu = gameMenu;

        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.DKGRAY);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 2));

        areaPaint = new Paint();
        areaPaint.setAntiAlias(true);

        iconPaint = new Paint();
        iconPaint.setAntiAlias(true);

        icon = BitmapFactory.decodeResource(FCLApplication.getCurrentActivity().getResources(), R.drawable.img_app);

        srcRect = new Rect(0, 0, icon.getWidth(), icon.getHeight());
        destRect = new Rect(ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 6),
                ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 6),
                ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 34),
                ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 34));
    }

    public void initPosition() {
        post(() -> {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = DEFAULT_WIDTH;
            layoutParams.height = DEFAULT_HEIGHT;
            setLayoutParams(layoutParams);
            setX((float) ((screenWidth - DEFAULT_WIDTH) * gameMenu.getMenuSetting().getMenuPositionX()));
            setY((float) ((screenHeight - DEFAULT_HEIGHT) * gameMenu.getMenuSetting().getMenuPositionY()));
        });
    }

    private boolean pressed = false;
    private Bitmap icon;

    private Paint strokePaint;
    private Paint areaPaint;
    private Paint iconPaint;

    private Rect srcRect;
    private Rect destRect;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pressed) {
            areaPaint.setColor(FCLApplication.getCurrentActivity().getColor(R.color.ui_bg_color));
        } else {
            areaPaint.setColor(Color.TRANSPARENT);
        }
        canvas.drawCircle(getMeasuredWidth() >> 1, getMeasuredHeight() >> 1, (getMeasuredWidth() >> 1) - ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 1), strokePaint);
        canvas.drawCircle(getMeasuredWidth() >> 1, getMeasuredHeight() >> 1, (getMeasuredWidth() >> 1) - ConvertUtils.dip2px(FCLApplication.getCurrentActivity(), 2), areaPaint);
        canvas.drawBitmap(icon, srcRect, destRect, iconPaint);
    }

    private float downX;
    private float downY;
    private long downTime;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downTime = System.currentTimeMillis();
                pressed = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!gameMenu.getMenuSetting().isLockMenuView()) {
                    float targetX = Math.max(0, Math.min(screenWidth - getMeasuredWidth(), getX() + event.getX() - downX));
                    float targetY = Math.max(0, Math.min(screenHeight - getMeasuredHeight(), getY() + event.getY() - downY));
                    setX(targetX);
                    setY(targetY);
                    gameMenu.getMenuSetting().setMenuPositionX(targetX / screenWidth);
                    gameMenu.getMenuSetting().setMenuPositionY(targetY / screenHeight);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (Math.abs(event.getX() - downX) <= 10
                        && Math.abs(event.getY() - downY) <= 10
                        && System.currentTimeMillis() - downTime <= 400) {
                    ((DrawerLayout) gameMenu.getLayout()).openDrawer(GravityCompat.START, true);
                    ((DrawerLayout) gameMenu.getLayout()).openDrawer(GravityCompat.END, true);
                }
                pressed = false;
                invalidate();
                break;
        }
        return true;
    }
}
