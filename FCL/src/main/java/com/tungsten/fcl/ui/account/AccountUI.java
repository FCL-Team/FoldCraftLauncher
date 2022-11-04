package com.tungsten.fcl.ui.account;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.skin.MinecraftSkinRenderer;
import com.tungsten.fcllibrary.skin.SkinGLSurfaceView;

public class AccountUI extends FCLCommonUI {

    private SkinGLSurfaceView skinGLSurfaceView;

    public AccountUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MinecraftSkinRenderer renderer = new MinecraftSkinRenderer(getContext(), true);
        skinGLSurfaceView = findViewById(R.id.skin_view);
        ViewGroup.LayoutParams layoutParams = skinGLSurfaceView.getLayoutParams();
        layoutParams.width = (int) (((View) skinGLSurfaceView.getParent().getParent()).getMeasuredWidth() * 0.3f);
        layoutParams.height = (int) Math.min(((View) skinGLSurfaceView.getParent().getParent()).getMeasuredWidth() * 0.3f, ((View) skinGLSurfaceView.getParent().getParent()).getMeasuredHeight());
        skinGLSurfaceView.setLayoutParams(layoutParams);

        skinGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        skinGLSurfaceView.setZOrderOnTop(true);
        skinGLSurfaceView.setRenderer(renderer, 5f);
        skinGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        skinGLSurfaceView.setPreserveEGLContextOnPause(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void refresh() {

    }
}
