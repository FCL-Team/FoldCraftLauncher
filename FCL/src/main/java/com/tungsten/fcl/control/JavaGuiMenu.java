package com.tungsten.fcl.control;

import android.view.KeyEvent;
import android.view.View;

import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;

public class JavaGuiMenu implements MenuCallback {

    @Override
    public View getLayout() {
        return null;
    }

    @Override
    public void setup(JVMActivity activity, FCLBridge fclBridge) {

    }

    @Override
    public FCLBridgeCallback getCallbackBridge() {
        return null;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {

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
