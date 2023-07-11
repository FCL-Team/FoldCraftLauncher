package com.tungsten.fcllibrary.skin;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SkinGLSurfaceView extends GLSurfaceView {
    private float density;
    private float previousX;
    private float previousY;
    private MinecraftSkinRenderer renderer;
    
    public SkinGLSurfaceView(final Context context) {
        super(context);
    }

    public SkinGLSurfaceView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() == 1) {
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            if (motionEvent.getAction() == 2 && this.renderer != null) {
                this.renderer.character.setRotateStep((x - this.previousX) / (this.density), (y - this.previousY) / (this.density));
            }
            this.previousX = x;
            this.previousY = y;
        }
        if (motionEvent.getPointerCount() == 2) {
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    priId = motionEvent.getPointerId(motionEvent.getActionIndex());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    secId = motionEvent.getPointerId(motionEvent.getActionIndex());
                    float deltaX = motionEvent.getX(motionEvent.findPointerIndex(priId)) - motionEvent.getX(motionEvent.findPointerIndex(secId));
                    float deltaY = motionEvent.getY(motionEvent.findPointerIndex(priId)) - motionEvent.getY(motionEvent.findPointerIndex(secId));
                    initDist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    initScale = renderer.character.scale;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dX = motionEvent.getX(motionEvent.findPointerIndex(priId)) - motionEvent.getX(motionEvent.findPointerIndex(secId));
                    float dY = motionEvent.getY(motionEvent.findPointerIndex(priId)) - motionEvent.getY(motionEvent.findPointerIndex(secId));
                    double dist = Math.sqrt(dX * dX + dY * dY);
                    double delta = dist - initDist;
                    if (initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))) <= 2 && initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))) >= 0.7) {
                        float scale = (float) (initScale + (delta / (1 * Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight()))));
                        renderer.character.setScale(scale);
                    }
                    break;
            }
        }

        return true;
    }

    private int priId;
    private int secId;
    private double initDist;
    private float initScale;

    public void setRenderer(final MinecraftSkinRenderer minecraftSkinRenderer, final float mDensity) {
        this.renderer = minecraftSkinRenderer;
        this.density = mDensity;
        super.setRenderer((Renderer)minecraftSkinRenderer);
    }
}
