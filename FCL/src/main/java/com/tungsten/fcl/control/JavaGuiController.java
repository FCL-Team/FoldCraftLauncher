package com.tungsten.fcl.control;

import android.view.View;

import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;

public class JavaGuiController implements ControllerCallback {

    @Override
    public View getLayout() {
        return null;
    }

    @Override
    public void setup(JVMActivity activity) {

    }

    @Override
    public FCLBridgeCallback getCallbackBridge() {
        return null;
    }
}
