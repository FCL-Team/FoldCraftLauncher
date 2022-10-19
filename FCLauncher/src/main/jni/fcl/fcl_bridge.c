//
// Created by Tungsten on 2022/10/11.
//

#include <fcl_internal.h>

#include <android/native_window_jni.h>
#include <jni.h>

FCLInternal fcl;

ANativeWindow* fclGetNativeWindow() {
    return fcl.window;
}

void fclSetPrimaryClipString(const char* string) {
    PrepareFCLBridgeJNI();
    CallFCLBridgeJNIFunc( , Void, setPrimaryClipString, "(Ljava/lang/String;)V", (*env)->NewStringUTF(env, string));
}

const char* fclGetPrimaryClipString() {
    PrepareFCLBridgeJNI();
    if (fcl.clipboard_string != NULL) {
        free(fcl.clipboard_string);
        fcl.clipboard_string = NULL;
    }
    CallFCLBridgeJNIFunc(jstring clipstr = , Object, getPrimaryClipString, "()Ljava/lang/String;");
    const char* string = NULL;
    if (clipstr != NULL) {
        string = (*env)->GetStringUTFChars(env, clipstr, NULL);
        if (string != NULL) {
            fcl.clipboard_string = strdup(string);
        }
    }
    return fcl.clipboard_string;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setFCLNativeWindow(JNIEnv* env, jclass clazz, jobject surface) {
    fcl.window = ANativeWindow_fromSurface(env, surface);
    FCL_INTERNAL_LOG("setFCLNativeWindow : %p", fcl.window);
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    memset(&fcl, 0, sizeof(fcl));
    fcl.android_jvm = vm;
    JNIEnv* env = 0;
    jint result = (*fcl.android_jvm)->AttachCurrentThread(fcl.android_jvm, &env, 0);
    if (result != JNI_OK || env == 0) {
        FCL_INTERNAL_LOG("Failed to attach thread to JavaVM.");
        abort();
    }
    jclass class_FCLBridge = (*env)->FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge");
    if (class_FCLBridge == 0) {
        FCL_INTERNAL_LOG("Failed to find class: com/tungsten/fclauncher/bridge/FCLBridge.");
        abort();
    }
    fcl.class_FCLBridge = (jclass)(*env)->NewGlobalRef(env, class_FCLBridge);
    return JNI_VERSION_1_2;
}