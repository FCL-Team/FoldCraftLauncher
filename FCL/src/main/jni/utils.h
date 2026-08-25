#pragma once

#include <stdbool.h>
#include <jni.h>
#include <android/log.h>

#define CLIPBOARD_COPY 2000
#define CLIPBOARD_PASTE 2001
#define CLIPBOARD_OPEN 2002

// SDL launcher integration
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
            __android_log_print(ANDROID_LOG_ERROR, "FCL", "dlsym(" #fn ") failed: %s", \
                                _e ? _e : "unknown error"); \
        }                                            \
        fn##_p = (fn##_t)_p;                         \
    } while (0)

#define TRY_ATTACH_ENV(env_name, vm, error_message, then) JNIEnv* env_name;\
do {                                                                       \
    env_name = get_attached_env(vm);                                       \
    if(env_name == NULL) {                                                 \
        printf(error_message);                                             \
        then                                                               \
    }                                                                      \
} while(0)

char** convert_to_char_array(JNIEnv *env, jobjectArray jstringArray);
jobjectArray convert_from_char_array(JNIEnv *env, char **charArray, int num_rows);
void free_char_array(JNIEnv *env, jobjectArray jstringArray, const char **charArray);
jstring convertStringJVM(JNIEnv* srcEnv, JNIEnv* dstEnv, jstring srcStr);
jintArray convertIntArrayJVM(JNIEnv* srcEnv, JNIEnv* dstEnv, jintArray srcIntArray);

JNIEnv* get_attached_env(JavaVM* jvm);

/** 通过 launcher 侧 CallbackBridge.notifyLauncher 通知 Java（如 SDL 集成初始化） */
bool notifyLauncher(JNIEnv *dvm_env, int type, int actions[], int len);

JNIEXPORT jstring JNICALL Java_org_lwjgl_glfw_CallbackBridge_nativeClipboard(JNIEnv* env, jclass clazz, jint action, jbyteArray copySrc);