package com.tungsten.fcl.control;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.tungsten.fcl.activity.JVMActivity;
import com.tungsten.fcl.activity.JVMCrashActivity;
import com.tungsten.fclauncher.FCLPath;
import com.tungsten.fclauncher.bridge.FCLBridge;
import com.tungsten.fclauncher.bridge.FCLBridgeCallback;
import com.tungsten.fclcore.util.Logging;

import java.util.logging.Level;

public class GameController implements Controller {
    private JVMActivity activity;

    @Override
    public View getLayout() {
        return null;
    }

    @Override
    public void setup(JVMActivity activity) {
        this.activity=activity;
    }

    @Override
    public FCLBridgeCallback getCallbackBridge() {
        return new FCLProcessListener(activity);
    }

    static class FCLProcessListener implements FCLBridgeCallback {
        private JVMActivity activity;
        FCLProcessListener(JVMActivity activity){
            this.activity=activity;
        }

        @Override
        public void onCursorModeChange(int mode) {
            // TODO: Handle mouse event
            activity.mouse.post(()->{
                if (mode== FCLBridge.CursorEnabled) {
                    activity.mouse.setVisibility(View.VISIBLE);
                } else {
                    activity.mouse.setVisibility(View.INVISIBLE);
                }
            });
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
