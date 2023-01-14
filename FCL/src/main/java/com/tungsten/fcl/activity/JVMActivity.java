package com.tungsten.fcl.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.MenuCallback;
import com.tungsten.fcl.control.MenuType;
import com.tungsten.fcl.control.GameMenu;
import com.tungsten.fcl.control.JavaGuiMenu;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.FCLActivity;

import java.util.logging.Level;

public class JVMActivity extends FCLActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;

    private MenuCallback menuCallback;
    private static MenuType menuType;
    private static FCLBridge fclBridge;

    public static void setFClBridge(FCLBridge fclBridge, MenuType menuType) {
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

        menuCallback = menuType == MenuType.GAME ? new GameMenu() : new JavaGuiMenu();
        menuCallback.setup(this, fclBridge);
        textureView = findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        Logging.LOG.log(Level.INFO, "surface ready, start jvm now!");
        surfaceTexture.setDefaultBufferSize((int) (i * fclBridge.getScaleFactor()), (int) (i1 * fclBridge.getScaleFactor()));
        fclBridge.execute(new Surface(surfaceTexture), menuCallback.getCallbackBridge());
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        surfaceTexture.setDefaultBufferSize((int) (i * fclBridge.getScaleFactor()), (int) (i1 * fclBridge.getScaleFactor()));
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        return false;
    }

    private int output = 0;

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
        if (output == 1) {
            menuCallback.onGraphicOutput();
            output++;
        }
        if (output < 1) {
            output++;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        menuCallback.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        menuCallback.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return menuCallback.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return menuCallback.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        menuCallback.onBackPressed();
    }
}
