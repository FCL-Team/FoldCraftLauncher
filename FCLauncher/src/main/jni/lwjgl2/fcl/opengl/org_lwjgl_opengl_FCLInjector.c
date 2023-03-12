//
// Created by Tungsten on 2023/1/29.
//

#include <jni.h>
#include <fcl_bridge.h>

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_FCLInjector_nGetInjectorMode(JNIEnv *env, jclass jclass) {
    return fclGetInjectorMode();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLInjector_nSetInjectorMode(JNIEnv *env, jclass jclass, jint mode) {
    fclSetInjectorMode(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLInjector_nSetHitResultType(JNIEnv *env, jclass jclass, jint type) {
    fclSetHitResultType(type);
}