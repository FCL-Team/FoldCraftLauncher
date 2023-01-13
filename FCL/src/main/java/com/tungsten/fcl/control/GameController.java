package com.tungsten.fcl.control;

import android.content.Intent;
import android.view.View;

import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclcore.util.Logging;

import java.util.logging.Level;

public class GameController implements ControllerCallback {

    @Override
    public View getLayout() {
        return null;
    }

    @Override
    public void setup(JVMActivity activity) {

    }

    @Override
    public FCLBridgeCallback getCallbackBridge() {
        return new FCLProcessListener();
    }

    static class FCLProcessListener implements FCLBridgeCallback {

        @Override
        public void onCursorModeChange(int mode) {
            // TODO: Handle mouse event
        }

        @Override
        public void onExit(int code) {
            if (code != 0) {
                Logging.LOG.log(Level.INFO, "JVM crashed, start jvm crash activity to show errors now!");
                Intent intent = new Intent(FCLPath.CONTEXT, JVMCrashActivity.class);
                FCLPath.CONTEXT.startActivity(intent);
            }
        }
    }
}
