package com.tungsten.fcl.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.ControllerCallback;
import com.tungsten.fcl.control.ControllerType;
import com.tungsten.fcl.control.GameController;
import com.tungsten.fcl.control.JavaGuiController;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.FCLActivity;

import java.util.logging.Level;

public class JVMActivity extends FCLActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;

    private ControllerCallback controller;
    private static ControllerType controllerType;
    private static FCLBridge fclBridge;

    public static void setFClBridge(FCLBridge fclBridge, ControllerType controllerType) {
        JVMActivity.fclBridge = fclBridge;
        JVMActivity.controllerType = controllerType;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jvm);

        if (controllerType == null || fclBridge == null) {
            Logging.LOG.log(Level.WARNING, "Failed to get ControllerType or FCLBridge, task canceled.");
            return;
        }

        controller = controllerType == ControllerType.GAME ? new GameController() : new JavaGuiController();
        controller.setup(this);
        textureView = findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(this);
        textureView.setFocusable(true);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        Logging.LOG.log(Level.INFO, "surface ready, start jvm now!");
        surfaceTexture.setDefaultBufferSize((int) (i * fclBridge.getScaleFactor()), (int) (i1 * fclBridge.getScaleFactor()));
        fclBridge.execute(new Surface(surfaceTexture), controller.getCallbackBridge());
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        surfaceTexture.setDefaultBufferSize((int) (i * fclBridge.getScaleFactor()), (int) (i1 * fclBridge.getScaleFactor()));
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onBackPressed() {

    }
}
