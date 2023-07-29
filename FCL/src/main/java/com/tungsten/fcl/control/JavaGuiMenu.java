package com.tungsten.fcl.control;

import android.view.View;

import androidx.annotation.Nullable;

import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fcllibrary.component.FCLActivity;
import com.tungsten.fcllibrary.component.view.FCLImageView;

public class JavaGuiMenu implements MenuCallback {

    @Override
    public void setup(FCLActivity activity, FCLBridge fclBridge) {

    }

    @Override
    public View getLayout() {
        return null;
    }

    @Override
    @Nullable
    public FCLBridge getBridge() {
        return null;
    }

    @Override
    public FCLBridgeCallback getCallbackBridge() {
        return null;
    }

    @Override
    public FCLInput getInput() {
        return null;
    }

    @Override
    public FCLImageView getCursor() {
        return null;
    }

    @Override
    public int getCursorMode() {
        return 0;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onGraphicOutput() {

    }

    @Override
    public void onCursorModeChange(int mode) {

    }

    @Override
    public void onLog(String log) {

    }

    @Override
    public void onExit(int exitCode) {

    }
}
