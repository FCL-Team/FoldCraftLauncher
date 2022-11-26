package com.tungsten.fcl.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.tungsten.fcl.R;
import com.tungsten.fcl.game.TexturesLoader;
import com.tungsten.fcl.setting.Accounts;
import com.tungsten.fcl.ui.account.AccountUI;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.SimpleObjectProperty;
import com.tungsten.fclcore.task.Task;
import com.tungsten.fclcore.util.skin.InvalidSkinException;
import com.tungsten.fclcore.util.skin.NormalizedSkin;
import com.tungsten.fcllibrary.component.ui.FCLCommonUI;
import com.tungsten.fcllibrary.component.view.FCLUILayout;
import com.tungsten.fcllibrary.skin.GameCharacter;
import com.tungsten.fcllibrary.skin.MinecraftSkinRenderer;
import com.tungsten.fcllibrary.skin.SkinGLSurfaceView;

public class MainUI extends FCLCommonUI {

    private SkinGLSurfaceView skinGLSurfaceView;
    private MinecraftSkinRenderer renderer;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh().start();
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
            try {
                if (Accounts.getSelectedAccount() == null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(AccountUI.class.getResourceAsStream("/assets/img/alex.png"));
                    NormalizedSkin normalizedSkin = new NormalizedSkin(bitmap);
                    renderer.character = new GameCharacter(normalizedSkin.isSlim());
                    renderer.character.setRunning(true);
                    renderer.updateTexture(normalizedSkin.isOldFormat() ? normalizedSkin.getNormalizedTexture() : normalizedSkin.getOriginalTexture(), Accounts.getSelectedAccount() == null ? null : TexturesLoader.capeBinding(Accounts.getSelectedAccount()).get());
                } else {
                    ObjectProperty<Bitmap> skinProperty = new SimpleObjectProperty<>();
                    skinProperty.bind(TexturesLoader.skinBitmapBinding(Accounts.getSelectedAccount()));
                    ObjectProperty<Bitmap> capeProperty = new SimpleObjectProperty<>();
                    capeProperty.bind(TexturesLoader.capeBinding(Accounts.getSelectedAccount()));
                    if (renderer.getSkinProperty() == null || renderer.getCapeProperty() == null) {
                        renderer.bindTexture(skinProperty, capeProperty);
                    } else if (param[0] != null) {
                        renderer.bindTexture((ObjectProperty<Bitmap>) param[0], (ObjectProperty<Bitmap>) param[1] == null ? capeProperty : (ObjectProperty<Bitmap>) param[1]);
                    }
                }
            } catch (InvalidSkinException e) {
                e.printStackTrace();
            }
        });
    }
}
