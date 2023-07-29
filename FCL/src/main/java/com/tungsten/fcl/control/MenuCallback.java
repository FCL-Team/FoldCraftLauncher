package com.tungsten.fcl.control;

import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.view.FCLImageView;

public interface MenuCallback {

    void setup(FCLActivity activity, FCLBridge fclBridge);

    View getLayout();

    @Nullable
    FCLBridge getBridge();

    FCLBridgeCallback getCallbackBridge();

    FCLInput getInput();

    FCLImageView getCursor();

    int getCursorMode();

    void onPause();

    void onResume();

    void onGraphicOutput();

    void onCursorModeChange(int mode);

    void onLog(String log);

    void onExit(int exitCode);

}
