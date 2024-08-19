//
// Created by Tungsten on 2022/10/11.
//

#include "fcl_internal.h"

#include <android/native_window_jni.h>
#include <jni.h>
#include <android/log.h>
#include <assert.h>

struct FCLInternal *fcl;

__attribute__((constructor)) void env_init() {
    char* strptr_env = getenv("FCL_ENVIRON");
    if (strptr_env == NULL) {
        __android_log_print(ANDROID_LOG_INFO, "Environ", "No FCL environ found, creating...");
        fcl = malloc(sizeof(struct FCLInternal));
        assert(fcl);
        memset(fcl, 0 , sizeof(struct FCLInternal));
        if (asprintf(&strptr_env, "%p", fcl) == -1)
            abort();
        setenv("FCL_ENVIRON", strptr_env, 1);
        free(strptr_env);
    } else {
        __android_log_print(ANDROID_LOG_INFO, "Environ", "Found existing FCL environ: %s", strptr_env);
        fcl = (void*) strtoul(strptr_env, NULL, 0x10);
    }
    __android_log_print(ANDROID_LOG_INFO, "Environ", "%p", fcl);
}

void fclSetPrimaryClipString(const char* string) {
    PrepareFCLBridgeJNI();
    CallFCLBridgeJNIFunc( , Void, setPrimaryClipString, "(Ljava/lang/String;)V", (*env)->NewStringUTF(env, string));
}

const char* fclGetPrimaryClipString() {
    PrepareFCLBridgeJNI();
    if (fcl->clipboard_string != NULL) {
        free(fcl->clipboard_string);
        fcl->clipboard_string = NULL;
    }
    CallFCLBridgeJNIFunc(jstring clipstr = , Object, getPrimaryClipString, "()Ljava/lang/String;");
    const char* string = NULL;
    if (clipstr != NULL) {
        string = (*env)->GetStringUTFChars(env, clipstr, NULL);
        if (string != NULL) {
            fcl->clipboard_string = strdup(string);
        }
    }
    return fcl->clipboard_string;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setFCLBridge(JNIEnv *env, jobject thiz, jobject fcl_bridge) {
    fcl->object_FCLBridge = (jclass)(*env)->NewGlobalRef(env, thiz);
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    if (fcl->android_jvm == NULL) {
        fcl->android_jvm = vm;
        JNIEnv* env = 0;
        jint result = (*fcl->android_jvm)->AttachCurrentThread(fcl->android_jvm, &env, 0);
        if (result != JNI_OK || env == 0) {
            FCL_INTERNAL_LOG("Failed to attach thread to JavaVM.");
            abort();
        }
        jclass class_FCLBridge = (*env)->FindClass(env, "com/tungsten/fclauncher/bridge/FCLBridge");
        if (class_FCLBridge == 0) {
            FCL_INTERNAL_LOG("Failed to find class: com/tungsten/fclauncher/bridge/FCLBridge.");
            abort();
        }
        fcl->class_FCLBridge = (jclass)(*env)->NewGlobalRef(env, class_FCLBridge);
    }
    return JNI_VERSION_1_2;
}
