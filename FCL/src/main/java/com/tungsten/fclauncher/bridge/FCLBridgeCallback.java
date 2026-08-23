package com.tungsten.fclauncher.bridge;

public interface FCLBridgeCallback {

    void onCursorModeChange(int mode);
    void onLog(String log);
    void onExit(int code);

}
