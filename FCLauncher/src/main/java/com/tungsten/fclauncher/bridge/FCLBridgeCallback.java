package com.tungsten.fclauncher.bridge;

public interface FCLBridgeCallback {

    void onCursorModeChange(int mode);
    void onHitResultTypeChange(int type);
    void onLog(String log);
    void onExit(int code);

}
