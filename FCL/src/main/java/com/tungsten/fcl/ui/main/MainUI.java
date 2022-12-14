package com.tungsten.fcl.ui.main;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.skin.MinecraftSkinRenderer;
import com.tungsten.fcllibrary.skin.SkinGLSurfaceView;

public class MainUI extends FCLCommonUI {

    private SkinGLSurfaceView skinGLSurfaceView;
    private MinecraftSkinRenderer renderer;

    private ObjectProperty<Account> currentAccount;

    public MainUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        renderer = new MinecraftSkinRenderer(getContext(), true);
        renderer.character.setRunning(true);
        skinGLSurfaceView = findViewById(R.id.skin_view);
        ViewGroup.LayoutParams layoutParamsSkin = skinGLSurfaceView.getLayoutParams();
        layoutParamsSkin.width = (int) (((View) skinGLSurfaceView.getParent().getParent()).getMeasuredWidth() * 0.5f);
        layoutParamsSkin.height = (int) Math.min(((View) skinGLSurfaceView.getParent().getParent()).getMeasuredWidth() * 0.5f, ((View) skinGLSurfaceView.getParent().getParent()).getMeasuredHeight());
        skinGLSurfaceView.setLayoutParams(layoutParamsSkin);

        skinGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        skinGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        skinGLSurfaceView.setZOrderOnTop(true);
        skinGLSurfaceView.setRenderer(renderer, 5f);
        skinGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        skinGLSurfaceView.setPreserveEGLContextOnPause(true);

        setupSkinDisplay();
    }

    @Override
    public void onStart() {
        super.onStart();
        skinGLSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        skinGLSurfaceView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShowing()) {
            skinGLSurfaceView.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        skinGLSurfaceView.onPause();
    }

    @Override
    public Task<?> refresh(Object... param) {
        return Task.runAsync(() -> {

        });
    }

    private void setupSkinDisplay() {
        currentAccount = new SimpleObjectProperty<Account>() {

            @Override
            protected void invalidated() {
                Account account = get();
                if (account == null) {
                    renderer.textureProperty().unbind();
                    renderer.updateTexture(BitmapFactory.decodeStream(MainUI.class.getResourceAsStream("/assets/img/alex.png")), null);
                } else {
                    renderer.textureProperty().bind(TexturesLoader.textureBinding(account));
                }
            }
        };
        currentAccount.bind(Accounts.selectedAccountProperty());
    }
}
