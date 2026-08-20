#include "fcl/include/fcl_internal.h"
#include "native_hooks.h"

#include <android/log.h>
#include <bytehook.h>
#include <dlfcn.h>
#include <jni.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include "SDL3/SDL.h"
#include "SDL3/SDL_main.h"

#define LOG_TAG "SDL"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#define NOTIF_TYPE_SDL 0
#define ACTION_INIT_LAUNCHER_INTEGRATION 0

// 声明 SDL 库中的符号类型，hook 触发时才动态解析
#define DECL_SDL_SYM(fn) typedef typeof(&fn) fn##_t;
DECL_SDL_SYM(SDL_Init)
DECL_SDL_SYM(SDL_InitSubSystem)
DECL_SDL_SYM(SDL_SetMainReady)
DECL_SDL_SYM(SDL_SetHint)
DECL_SDL_SYM(SDL_SetError)
DECL_SDL_SYM(SDL_GetError)

#define SET_DLSYM_PTR(handle, fn)                     \
    fn##_t fn##_p;                                   \
    do {                                             \
        dlerror();                                   \
        void *_p = dlsym((handle), #fn);             \
        const char *_e = dlerror();                  \
        if (_e || !_p) {                             \
            LOGE("dlsym(%s) failed: %s",             \
                 #fn, _e ? _e : "unknown error");    \
        }                                            \
        fn##_p = (fn##_t)_p;                         \
    } while (0)

// 调用 SDL_SetMainReady，绕过 SDL3.2+ 对 SDL_main.h 入口的检查。
// MC 等从 Java 侧调用 SDL_Init 的应用没有经过 SDL_main 包装，
// 不调用该函数会导致 SDL_Init 失败并报
// "Application didn't initialize properly, did you include SDL_main.h..."
static void ensureMainReady(void) {
    void *sdl_handle = dlopen("libSDL3.so", RTLD_NOLOAD);
    if (sdl_handle == NULL) {
        LOGE("Failed to find loaded libSDL3.so");
        return;
    }
    SET_DLSYM_PTR(sdl_handle, SDL_SetMainReady);
    if (SDL_SetMainReady_p) SDL_SetMainReady_p();
}

static bool custom_SDL_InitSubSystem_Func(SDL_InitFlags flags) {
    // SDL_InitSubSystem 也可能检查 SDL_main 入口，先确保就绪
    ensureMainReady();

    // 在 dalvik VM 中调用 CallbackBridge.notifyLauncher，
    // 让启动器初始化 SDL 所需的 JNI 环境（System.loadLibrary + SDL.setupJNI 等）。
    // SDL3 库已由 APK 打包，System.loadLibrary("SDL3") 在 dalvik 线程执行，
    // ART 会以应用 classloader 调用 JNI_OnLoad，正确注册 SDLActivity 等类。
    JavaVM *jvm = fcl->android_jvm;
    JNIEnv *dvm_env = NULL;
    jint env_result = (*jvm)->GetEnv(jvm, (void **) &dvm_env, JNI_VERSION_1_4);
    if (env_result == JNI_EDETACHED) {
        env_result = (*jvm)->AttachCurrentThread(jvm, &dvm_env, NULL);
    }
    if (dvm_env == NULL || env_result != JNI_OK) {
        LOGE("SDL_InitSubSystem failed! Cannot attach to dalvik VM");
        SET_DLSYM_PTR(dlopen("libSDL3.so", RTLD_NOLOAD), SDL_SetError);
        if (SDL_SetError_p) SDL_SetError_p("Failed to load SDL launcher integration android-side. This is not an SDL bug, please contact the launcher developer.");
        return false;
    }

    // 防止溢出转换为 jint
    jint safeFlags;
    if (flags > INT32_MAX) {
        safeFlags = -1;
    } else safeFlags = (jint)flags;

    // 注意：此 hook 由 JVM 线程触发（LWJGL FFM 调用 SDL_Init），attach 到 dalvik
    // 后 FindClass 使用系统 classloader，找不到应用类。必须通过应用 classloader
    // 显式 loadClass 获取 CallbackBridge。
    jclass bridgeClazz = NULL;
    if (fcl->class_FCLBridge != NULL) {
        jclass classClazz = (*dvm_env)->GetObjectClass(dvm_env, fcl->class_FCLBridge);
        jmethodID getClassLoaderMethod = (*dvm_env)->GetMethodID(dvm_env, classClazz, "getClassLoader",
                                                                 "()Ljava/lang/ClassLoader;");
        jobject appClassLoader = (*dvm_env)->CallObjectMethod(dvm_env, fcl->class_FCLBridge, getClassLoaderMethod);
        (*dvm_env)->DeleteLocalRef(dvm_env, classClazz);
        if (appClassLoader != NULL) {
            jclass classLoaderClazz = (*dvm_env)->FindClass(dvm_env, "java/lang/ClassLoader");
            jmethodID loadClassMethod = (*dvm_env)->GetMethodID(dvm_env, classLoaderClazz, "loadClass",
                                                                "(Ljava/lang/String;)Ljava/lang/Class;");
            jstring bridgeName = (*dvm_env)->NewStringUTF(dvm_env, "org.lwjgl.glfw.CallbackBridge");
            jobject bridgeObj = (*dvm_env)->CallObjectMethod(dvm_env, appClassLoader, loadClassMethod, bridgeName);
            bridgeClazz = (jclass) bridgeObj;
            (*dvm_env)->DeleteLocalRef(dvm_env, bridgeName);
            (*dvm_env)->DeleteLocalRef(dvm_env, classLoaderClazz);
            (*dvm_env)->DeleteLocalRef(dvm_env, appClassLoader);
        }
    }
    if (bridgeClazz != NULL) {
        jmethodID method_notifyLauncher = (*dvm_env)->GetStaticMethodID(dvm_env, bridgeClazz,
                                                                        "notifyLauncher", "(I[I)Z");
        if (method_notifyLauncher != NULL) {
            jintArray actionArray = (*dvm_env)->NewIntArray(dvm_env, 2);
            jint actions[2] = {ACTION_INIT_LAUNCHER_INTEGRATION, safeFlags};
            (*dvm_env)->SetIntArrayRegion(dvm_env, actionArray, 0, 2, actions);
            (*dvm_env)->CallStaticBooleanMethod(dvm_env, bridgeClazz, method_notifyLauncher,
                                                NOTIF_TYPE_SDL, actionArray);
        } else {
            // 方法可能尚未加载（Java 侧未就绪），清除异常避免影响后续 JNI 调用
            (*dvm_env)->ExceptionClear(dvm_env);
            LOGE("Failed to find CallbackBridge.notifyLauncher");
        }
        (*dvm_env)->DeleteLocalRef(dvm_env, bridgeClazz);
    } else {
        (*dvm_env)->ExceptionClear(dvm_env);
        LOGE("Failed to find org/lwjgl/glfw/CallbackBridge via app classloader");
    }

    // 启动器侧默认开启该 hint，SDL 默认关闭
    SET_DLSYM_PTR(dlopen("libSDL3.so", RTLD_NOLOAD), SDL_SetHint);
    if (SDL_SetHint_p) SDL_SetHint_p("SDL_RETURN_KEY_HIDES_IME", "true");

    // 调用原函数完成初始化
    bool r = BYTEHOOK_CALL_PREV(custom_SDL_InitSubSystem_Func, SDL_InitSubSystem_t, flags);
    if (!r){
        SET_DLSYM_PTR(dlopen("libSDL3.so", RTLD_NOLOAD), SDL_GetError);
        LOGE("SDL_InitSubsystem Error: %s", SDL_GetError_p());
    }
    BYTEHOOK_POP_STACK();
    return r;
}

static bool custom_SDL_Init_Func(SDL_InitFlags flags) {
    // 部分应用只调用 SDL_Init（内部再调 SDL_InitSubSystem），
    // 检查 SDL_main 入口的逻辑可能位于 SDL_Init 内部，先确保就绪
    ensureMainReady();
    bool r = BYTEHOOK_CALL_PREV(custom_SDL_Init_Func, SDL_Init_t, flags);
    BYTEHOOK_POP_STACK();
    return r;
}

void create_sdl_hooks(bytehook_hook_all_t bytehook_hook_all_p) {
    // callee_path_name 必须为 NULL，否则无法找到该符号
    bytehook_stub_t stub_SDL_InitSubSystem = bytehook_hook_all_p(NULL, "SDL_InitSubSystem", &custom_SDL_InitSubSystem_Func, NULL, NULL);
    bytehook_stub_t stub_SDL_Init = bytehook_hook_all_p(NULL, "SDL_Init", &custom_SDL_Init_Func, NULL, NULL);
    LOGI("Successfully initialized SDL hooks, stub: %p %p", stub_SDL_InitSubSystem, stub_SDL_Init);
}
