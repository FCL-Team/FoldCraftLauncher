package com.tungsten.fcl.activity;

import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tungsten.fcl.R;
import com.tungsten.fcl.control.Controller;
import com.tungsten.fcl.control.ControllerType;
import com.tungsten.fcl.control.GameController;
import com.tungsten.fcl.control.JavaGuiController;
import com.tungsten.fcl.onlytest.MioMouseKeyboard;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclcore.util.Logging;
import com.tungsten.fcllibrary.component.FCLActivity;

import java.util.logging.Level;

public class JVMActivity extends FCLActivity implements TextureView.SurfaceTextureListener {

    private TextureView textureView;

    private Controller controller;
    private static ControllerType controllerType;
    private static FCLBridge fclBridge;

    //only for test version
    private MioMouseKeyboard mioMouseKeyboard;
    private ImageView mouse;
    private EditText input;

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

        textureView = findViewById(R.id.texture_view);
        textureView.setSurfaceTextureListener(this);
        mouse=findViewById(R.id.mouse);
        input=findViewById(R.id.input);
        textureView.setFocusable(true);
        mioMouseKeyboard=new MioMouseKeyboard(this,mouse,textureView);
        mioMouseKeyboard.setFCLBridge(fclBridge);
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        Logging.LOG.log(Level.INFO, "surface ready, start jvm now!");
        fclBridge.execute(new Surface(surfaceTexture), controller.getCallbackBridge());
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

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
        if(getResources().getConfiguration().keyboard!= Configuration.KEYBOARD_NOKEYS) {
            mioMouseKeyboard.catchPointer();
        }
    }
}
