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
    PrepareFCLBridgeJNI();
    CallFCLBridgeJNIFunc( , Void, setHitResultType, "(I)V", type);
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_refreshHitResultType(JNIEnv *env, jobject thiz) {
    if (injectorCallback)
        injectorCallback();
}