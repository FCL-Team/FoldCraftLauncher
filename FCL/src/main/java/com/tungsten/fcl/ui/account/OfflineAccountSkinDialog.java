package com.tungsten.fcl.ui.account;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.tungsten.fcl.R;
import com.tungsten.fcllibrary.component.dialog.FCLDialog;
import com.tungsten.fcllibrary.component.view.FCLButton;
import com.tungsten.fcllibrary.skin.MinecraftSkinRenderer;
import com.tungsten.fcllibrary.skin.SkinGLSurfaceView;

public class OfflineAccountSkinDialog extends FCLDialog implements View.OnClickListener {

    private final AccountListItem accountListItem;

    private SkinGLSurfaceView skinGLSurfaceView;
    private MinecraftSkinRenderer renderer;

    private FCLButton positive;
    private FCLButton negative;

    public OfflineAccountSkinDialog(@NonNull Context context, AccountListItem accountListItem) {
        super(context);
        this.accountListItem = accountListItem;
        setContentView(R.layout.dialog_offline_account_skin);
        setCancelable(false);

        renderer = new MinecraftSkinRenderer(getContext(), true);
        renderer.character.setRunning(true);
        skinGLSurfaceView = findViewById(R.id.skin_view);

        skinGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        skinGLSurfaceView.setZOrderOnTop(true);
        skinGLSurfaceView.setRenderer(renderer, 5f);
        skinGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        skinGLSurfaceView.setPreserveEGLContextOnPause(true);

        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == positive) {
            
        }
        if (view == negative) {
            dismiss();
        }
    }
}
