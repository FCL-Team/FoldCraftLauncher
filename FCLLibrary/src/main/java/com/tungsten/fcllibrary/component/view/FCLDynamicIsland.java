package com.tungsten.fcllibrary.component.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fcllibrary.anim.DynamicIslandAnim;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLDynamicIsland extends AppCompatTextView {

    private DynamicIslandAnim anim;

    private Path outlinePath;
    private Paint outlinePaint;
    private Paint insidePaint;
    private Paint textPaint;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
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
            textPaint.setTextSize(ConvertUtils.dip2px(getContext(), 13));
            textPaint.setColor(ThemeEngine.getInstance().getTheme().getAutoTint());
            textPaint.setTextAlign(Paint.Align.CENTER);
            invalidate();
            setTextColor(ThemeEngine.getInstance().getTheme().getAutoTint());
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "theme";
        }
    };

    private void init() {
        anim = new DynamicIslandAnim(this);
        setGravity(Gravity.CENTER);
        int tb = ConvertUtils.dip2px(getContext(), 8f);
        int lr = ConvertUtils.dip2px(getContext(), 18f);
        setPadding(lr, tb, lr, tb);
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
        textPaint.setTextSize(ConvertUtils.dip2px(getContext(), 13));
        textPaint.setColor(ThemeEngine.getInstance().getTheme().getAutoTint());
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public FCLDynamicIsland(@NonNull Context context) {
        super(context);
        init();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLDynamicIsland(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    public FCLDynamicIsland(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float offset = ConvertUtils.dip2px(getContext(), 1.5f);
        float width = getMeasuredWidth() - 2 * offset;
        float height = getMeasuredHeight() - 2 * offset;
        outlinePath = new Path();
        outlinePath.moveTo(offset + (height / 2), offset);
        outlinePath.arcTo(offset, offset, offset + height, offset + height, 270, -180, false);
        outlinePath.lineTo(offset + width - (height / 2), height + offset);
        outlinePath.arcTo(offset + width - height, offset, width + offset, height + offset, 90, -180, false);
        outlinePath.lineTo(offset + (height / 2), offset);
        canvas.drawPath(outlinePath, insidePaint);
        canvas.drawPath(outlinePath, outlinePaint);
        canvas.drawText(getText().toString(), getWidth() / 2f, (getHeight() / 2f) + 21, textPaint);
    }

    public void refresh(String text) {
        setText(text);
        invalidate();
    }

    public void setTextWithAnim(String text) {
        post(() -> {
            anim.refresh((float) getMeasuredHeight() / (float) getMeasuredWidth());
            anim.run(text);
        });
    }
}
