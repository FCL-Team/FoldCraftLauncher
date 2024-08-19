//
// Created by Tungsten on 2022/10/11.
//

#include <fcl_internal.h>
#include <android/log.h>

FCLinjectorfun injectorCallback;

void fclSetInjectorCallback(FCLinjectorfun callback) {
    injectorCallback = callback;
}

void fclSetHitResultType(int type) {
    if (!fcl->has_event_pipe) {
        return;
    }
    PrepareFCLBridgeJNI();
    CallFCLBridgeJNIFunc( , Void, setHitResultType, "(I)V", type);
}

void fclSetCursorMode(int mode) {
    if (!fcl->has_event_pipe) {
        return;
    }
    PrepareFCLBridgeJNI();
    CallFCLBridgeJNIFunc( , Void, setCursorMode, "(I)V", mode);
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_refreshHitResultType(JNIEnv *env, jobject thiz) {
    if (injectorCallback)
        injectorCallback();
}