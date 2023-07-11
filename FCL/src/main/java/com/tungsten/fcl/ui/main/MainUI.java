package com.tungsten.fcl.ui.main;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fclcore.auth.Account;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.skin.MinecraftSkinRenderer;
import com.tungsten.fcllibrary.skin.SkinGLSurfaceView;

public class MainUI extends FCLCommonUI {

    private RelativeLayout skinContainer;
    private SkinGLSurfaceView skinGLSurfaceView;
    private MinecraftSkinRenderer renderer;

    private ObjectProperty<Account> currentAccount;

    public MainUI(Context context, FCLUILayout parent, int id) {
        super(context, parent, id);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        skinContainer = findViewById(R.id.skin_container);
        renderer = new MinecraftSkinRenderer(getContext(), true);
        renderer.character.setRunning(true);
        ViewGroup.LayoutParams layoutParamsSkin = skinContainer.getLayoutParams();
        layoutParamsSkin.width = (int) (((View) skinContainer.getParent().getParent()).getMeasuredWidth() * 0.5f);
        layoutParamsSkin.height = (int) Math.min(((View) skinContainer.getParent().getParent()).getMeasuredWidth() * 0.5f, ((View) skinContainer.getParent().getParent()).getMeasuredHeight());
        skinContainer.setLayoutParams(layoutParamsSkin);

        setupSkinDisplay();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (skinGLSurfaceView == null) {
            skinGLSurfaceView = new SkinGLSurfaceView(getContext());
            skinGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            skinGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
            skinGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            skinGLSurfaceView.setZOrderOnTop(true);
            skinGLSurfaceView.setRenderer(renderer, 5f);
            skinGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            skinGLSurfaceView.setPreserveEGLContextOnPause(true);
        } else {
            skinGLSurfaceView.onResume();
            renderer.refresh(renderer.getTexture());
        }
        skinContainer.addView(skinGLSurfaceView);
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
        skinContainer.removeView(skinGLSurfaceView);
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
                renderer.textureProperty().unbind();
                if (account == null) {
                    renderer.updateTexture(BitmapFactory.decodeStream(MainUI.class.getResourceAsStream("/assets/img/alex.png")), null);
                } else {
                    renderer.textureProperty().bind(TexturesLoader.textureBinding(account));
                }
            }
        };
        currentAccount.bind(Accounts.selectedAccountProperty());
    }

    public void refreshSkin(Account account) {
        Schedulers.androidUIThread().execute(() -> {
            if (currentAccount.get() == account) {
                renderer.textureProperty().unbind();
                renderer.textureProperty().bind(TexturesLoader.textureBinding(currentAccount.get()));
            }
        });
    }
}
