//
// Created by Tungsten on 2023/1/30.
//

#include <jni.h>
#include <fcl_bridge.h>

JNIEXPORT void JNICALL Java_org_lwjgl_LWJGLUtil_nLog(JNIEnv *env, jclass jclass, jstring msg) {
    char const* buffer = (*env)->GetStringUTFChars(env, msg, 0);
    fclLog(buffer);
}