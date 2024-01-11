//
// Created by Tungsten on 2022/10/11.
//

#include <fcl_internal.h>
#include <android/log.h>

int injector_mode = 0;

void fclSetInjectorMode(int mode) {
    injector_mode = mode;
}

int fclGetInjectorMode() {
    return injector_mode;
}

void fclSetHitResultType(int type) {
    if (!fcl->has_event_pipe) {
        return;
    }
    PrepareFCLBridgeJNI();
    CallFCLBridgeJNIFunc( , Void, setHitResultType, "(I)V", type);
}


JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_refreshHitResultType(JNIEnv *env, jobject thiz) {
    injector_mode = 1;
}