package com.tungsten.fcllibrary.skin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SkinCanvas extends GLSurfaceView {

    private float density;
    private float previousX;
    private float previousY;
    private SkinRenderer renderer;
    private int priPointerId;
    private int secPointerId;
    private double initDistance;
    private float initScale;

    public SkinCanvas(Context context) {
        super(context);
        init();
    }

    public SkinCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (event.getPointerCount() == 1) {
                final float x = event.getX();
                final float y = event.getY();
                if (event.getAction() == MotionEvent.ACTION_MOVE && this.renderer != null) {
                    this.renderer.getSkinModel().setRotateStep((x - this.previousX) / (this.density), (y - this.previousY) / (this.density));
                }
                this.previousX = x;
                this.previousY = y;
            }
            if (event.getPointerCount() == 2) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        priPointerId = event.getPointerId(event.getActionIndex());
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        secPointerId = event.getPointerId(event.getActionIndex());
                        float deltaX = event.getX(event.findPointerIndex(priPointerId)) - event.getX(event.findPointerIndex(secPointerId));
                        float deltaY = event.getY(event.findPointerIndex(priPointerId)) - event.getY(event.findPointerIndex(secPointerId));
                        initDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                        initScale = renderer.getSkinModel().getScale();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dX = event.getX(event.findPointerIndex(priPointerId)) - event.getX(event.findPointerIndex(secPointerId));
                        float dY = event.getY(event.findPointerIndex(priPointerId)) - event.getY(event.findPointerIndex(secPointerId));
                        double dist = Math.sqrt(dX * dX + dY * dY);
                        double delta = dist - initDistance;
                        if (initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))) <= 2
                                && initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))) >= 0.7) {
                            float scale = (float) (initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))));
                            renderer.getSkinModel().setScale(scale);
                        }
                        break;
                }
            }
        } catch (Throwable ignore){
        }
        return true;
    }

    private void init() {
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        setPreserveEGLContextOnPause(true);
    }

    public void setRenderer(SkinRenderer renderer, float density) {
        this.renderer = renderer;
        this.density = density;
        super.setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
