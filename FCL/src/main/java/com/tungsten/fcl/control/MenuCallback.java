package com.tungsten.fcl.control;

import android.view.KeyEvent;
import android.view.View;

import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;

public interface MenuCallback {

    View getLayout();

    void setup(JVMActivity activity, FCLBridge fclBridge);

    FCLBridgeCallback getCallbackBridge();

    void onPause();

    void onResume();

    boolean onKeyDown(int keyCode, KeyEvent event);

    boolean onKeyUp(int keyCode, KeyEvent event);

    void onBackPressed();

    void onGraphicOutput();

    void onCursorModeChange(int mode);

    void onLog(String log);

    void onExit(int exitCode);

}
