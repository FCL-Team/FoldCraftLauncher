#pragma once

#include <stdbool.h>
#include <jni.h>
#include <dlfcn.h>
#include <android/log.h>
#include "environ/environ.h"

#define CLIPBOARD_COPY 2000
#define CLIPBOARD_PASTE 2001
#define CLIPBOARD_OPEN 2002

#define NOTIF_TYPE_SDL 0
#define ACTION_INIT_LAUNCHER_INTEGRATION 0
#define ACTION_SEND_TEXTBOX_RECT 1

#define DECL_DLSYM(fn) typedef typeof(&fn) fn##_t;

#define SET_DLSYM_PTR(handle, fn)                     \
    fn##_t fn##_p;                                   \
    do {                                             \
        dlerror();                                   \
        void *_p = dlsym((handle), #fn);             \
        const char *_e = dlerror();                  \
        if (_e || !_p) {                             \
            __android_log_print(ANDROID_LOG_ERROR, "SDL", "dlsym(%s) failed: %s", \
                                #fn, _e ? _e : "unknown error"); \
        }                                            \
        fn##_p = (fn##_t)_p;                         \
    } while (0)

#define TRY_ATTACH_ENV(env_name, vm, error_message, then) JNIEnv* env_name;\
do {                                                                       \
    JavaVM *_vm = (vm);                                                    \
    jint _env_result = (*_vm)->GetEnv(_vm, (void**) &env_name, JNI_VERSION_1_4); \
    if (_env_result == JNI_EDETACHED) {                                    \
        _env_result = (*_vm)->AttachCurrentThread(_vm, &env_name, NULL);   \
    }                                                                      \
    if (env_name == NULL || _env_result != JNI_OK) {                       \
        __android_log_print(ANDROID_LOG_ERROR, "SDL", "%s", error_message); \
        then                                                               \
    }                                                                      \
} while(0)

static bool notifyLauncher(JNIEnv *dvm_env, int type, int actions[], int len){
    jintArray actionArray = (*dvm_env)->NewIntArray(dvm_env, len);
    (*dvm_env)->SetIntArrayRegion(dvm_env, actionArray, 0, len, actions);
    return (*dvm_env)->CallStaticBooleanMethod(dvm_env, pojav_environ->bridgeClazz,
            pojav_environ->method_notifyLauncher, type, actionArray);
}

JNIEXPORT jstring JNICALL Java_org_lwjgl_glfw_CallbackBridge_nativeClipboard(JNIEnv* env, jclass clazz, jint action, jbyteArray copySrc);
