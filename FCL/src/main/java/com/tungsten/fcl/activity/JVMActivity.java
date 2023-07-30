package com.tungsten.fcl.activity;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.MenuCallback;
import com.tungsten.fcl.control.MenuType;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.JavaGuiMenu;
import com.tungsten.fcl.setting.GameOption;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.keycodes.FCLKeycodes;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.FCLActivity;

import java.util.Objects;
import java.util.logging.Level;

public class JVMActivity extends FCLActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;

    private MenuCallback menu;
    private static MenuType menuType;
    private static FCLBridge fclBridge;
    private boolean isTranslated = false;

    public static void setFCLBridge(FCLBridge fclBridge, MenuType menuType) {
        JVMActivity.fclBridge = fclBridge;
        JVMActivity.menuType = menuType;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jvm);

        if (menuType == null || fclBridge == null) {
            Logging.LOG.log(Level.WARNING, "Failed to get ControllerType or FCLBridge, task canceled.");
            return;
        }

        menu = menuType == MenuType.GAME ? new GameMenu() : new JavaGuiMenu();
        menu.setup(this, fclBridge);
        textureView = findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(this);
        menu.getInput().initExternalController(textureView);

        addContentView(menu.getLayout(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int screenHeight = getWindow().getDecorView().getHeight();
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            if (screenHeight * 2 / 3 > rect.bottom) {
                textureView.setTranslationY(rect.bottom - screenHeight);
                isTranslated = true;
            } else if (isTranslated) {
                isTranslated = false;
                textureView.setTranslationY(0);
            }
        });
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        Logging.LOG.log(Level.INFO, "surface ready, start jvm now!");
        int width = (int) (i * fclBridge.getScaleFactor());
        int height = (int) (i1 * fclBridge.getScaleFactor());
        GameOption gameOption = new GameOption(Objects.requireNonNull(menu.getBridge()).getGameDir());
        gameOption.set("fullscreen", "false");
        gameOption.set("overrideWidth", "" + width);
        gameOption.set("overrideHeight", "" + height);
        gameOption.save();
        surfaceTexture.setDefaultBufferSize(width, height);
        fclBridge.execute(new Surface(surfaceTexture), menu.getCallbackBridge());
        fclBridge.pushEventWindow(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        int width = (int) (i * fclBridge.getScaleFactor());
        int height = (int) (i1 * fclBridge.getScaleFactor());
        surfaceTexture.setDefaultBufferSize(width, height);
        fclBridge.pushEventWindow(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        return false;
    }

    private int output = 0;

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
        if (textureView != null && textureView.getSurfaceTexture() != null) {
            textureView.post(() -> onSurfaceTextureSizeChanged(textureView.getSurfaceTexture(), textureView.getWidth(), textureView.getHeight()));
        }
        if (output == 1) {
            menu.onGraphicOutput();
            output++;
        }
        if (output < 1) {
            output++;
        }
    }

    @Override
    protected void onPause() {
        menu.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        menu.onResume();
        super.onResume();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handleEvent = true;
        if (menu != null) {
            if (!(handleEvent = menu.getInput().handleKeyEvent(event))) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && !((GameMenu) menu).getTouchCharInput().isEnabled()) {
                    if (event.getAction() != KeyEvent.ACTION_UP)
                        return true;
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ESC, true);
                    menu.getInput().sendKeyEvent(FCLKeycodes.KEY_ESC, false);
                    return true;
                }
            }
        }
        return handleEvent;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (textureView != null && textureView.getSurfaceTexture() != null) {
            textureView.post(() -> onSurfaceTextureSizeChanged(textureView.getSurfaceTexture(), textureView.getWidth(), textureView.getHeight()));
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (textureView != null && textureView.getSurfaceTexture() != null) {
            textureView.post(() -> onSurfaceTextureSizeChanged(textureView.getSurfaceTexture(), textureView.getWidth(), textureView.getHeight()));
        }
    }
}
