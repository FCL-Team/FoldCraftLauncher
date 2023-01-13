package com.tungsten.fcl.control;

import android.view.View;

import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;

public interface ControllerCallback {

    View getLayout();

    void setup(JVMActivity activity);

    FCLBridgeCallback getCallbackBridge();

}
