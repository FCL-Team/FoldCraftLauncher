package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLTitleView extends View {

    private Path outlinePath;
    private Paint outlinePaint;
    private Paint insidePaint;
    private Paint textPaint;

    private String title;

    private final Runnable runnable = () -> {
        outlinePaint.setAntiAlias(true);
        outlinePaint.setColor(ThemeEngine.getInstance().getTheme().getDkColor());
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(ConvertUtils.dip2px(getContext(), 3));
        insidePaint.setAntiAlias(true);
        insidePaint.setColor(ThemeEngine.getInstance().getTheme().getColor());
        insidePaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(56);
        textPaint.setColor(ThemeEngine.getInstance().getTheme().getAutoTint());
        textPaint.setTextAlign(Paint.Align.CENTER);
        invalidate();
    };

    private void init(String title) {
        this.title = title;
        outlinePath = new Path();
        outlinePaint = new Paint();
        insidePaint = new Paint();
        textPaint = new Paint();
        outlinePaint.setAntiAlias(true);
        outlinePaint.setColor(ThemeEngine.getInstance().getTheme().getDkColor());
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(ConvertUtils.dip2px(getContext(), 3));
        insidePaint.setAntiAlias(true);
        insidePaint.setColor(ThemeEngine.getInstance().getTheme().getColor());
        insidePaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(56);
        textPaint.setColor(ThemeEngine.getInstance().getTheme().getAutoTint());
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public FCLTitleView(Context context) {
        super(context);
        Schedulers.androidUIThread().execute(() -> {
            init("");
            ThemeEngine.getInstance().registerEvent(this, runnable);
        });
    }

    public FCLTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Schedulers.androidUIThread().execute(() -> {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTitleView);
            String title = typedArray.getString(R.styleable.FCLTitleView_title);
            if (title == null) {
                title = "";
            }
            init(title);
            typedArray.recycle();
            ThemeEngine.getInstance().registerEvent(this, runnable);
        });
    }

    public FCLTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Schedulers.androidUIThread().execute(() -> {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLTitleView);
            String title = typedArray.getString(R.styleable.FCLTitleView_title);
            if (title == null) {
                title = "";
            }
            init(title);
            typedArray.recycle();
            ThemeEngine.getInstance().registerEvent(this, runnable);
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth() - ConvertUtils.dip2px(getContext(), 3);
        float height = getHeight() - ConvertUtils.dip2px(getContext(), 1.5f);
        outlinePath.moveTo(ConvertUtils.dip2px(getContext(), 1.5f),0);
        outlinePath.lineTo((int) (height * Math.sqrt(3) / 4) + ConvertUtils.dip2px(getContext(), 1.5f), (int) (height * 3 / 4));
        outlinePath.arcTo((int) (height * (Math.sqrt(3) - 1) / 2) + ConvertUtils.dip2px(getContext(), 1.5f), 0, (int) (height * (Math.sqrt(3) + 1) / 2) + ConvertUtils.dip2px(getContext(), 1.5f), height, 150, -60, false);
        outlinePath.lineTo((float) (width - (Math.sqrt(3) * height / 2) + ConvertUtils.dip2px(getContext(), 1.5f)), height);
        outlinePath.arcTo((int) (width - (height * (Math.sqrt(3) + 1) / 2)) + ConvertUtils.dip2px(getContext(), 1.5f), 0, (int) (width - (height * (Math.sqrt(3) - 1) / 2) + ConvertUtils.dip2px(getContext(), 1.5f)), height, 90, -60, false);
        outlinePath.lineTo(width + ConvertUtils.dip2px(getContext(), 1.5f), 0);
        canvas.drawPath(outlinePath, insidePaint);
        canvas.drawPath(outlinePath, outlinePaint);
        canvas.drawText(title, (int) (getWidth() / 2), (int) (getHeight() / 2) + 14, textPaint);
    }

    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }

    public String getTitle() {
        return title;
    }
}
