// SDL JNI_OnLoad isolation for the embedded game JVM.
// Reference: https://github.com/AngelAuraMC/Amethyst-Android/commit/e4c79084d
// 移植自 ZalithLauncher2 feat/sdl3：游戏 JVM 加载 libSDL3.so 时跳过 SDL 的 JNI_OnLoad 注册，
// 避免在游戏 VM 里注册 SDLActivity 系列 native 干扰双 VM 边界

#include <dlfcn.h>
#include <jni.h>
#include <bytehook.h>
#include <stdlib.h>
#include <string.h>

#include "environ/environ.h"
#include "native_hooks.h"

typedef void *(*dlsym_func_t)(void *handle, const char *symbol);
typedef jint (*jni_on_load_func_t)(JavaVM *vm, void *reserved);

static void *sdlHandle;
static jni_on_load_func_t originalSdlJniOnLoad;

static jint isolatedSdlJniOnLoad(JavaVM *vm, void *reserved) {
    if (originalSdlJniOnLoad != NULL && pojav_environ->dalvikJavaVMPtr == vm) {
        return originalSdlJniOnLoad(vm, reserved);
    }
    return JNI_VERSION_1_4;
}

static void *customDlsym(void *handle, const char *symbol) {
    void *result = BYTEHOOK_CALL_PREV(customDlsym, dlsym_func_t, handle, symbol);
    BYTEHOOK_POP_STACK();
    if (sdlHandle == NULL) {
        sdlHandle = dlopen("libSDL3.so", RTLD_LOCAL | RTLD_NOW);
    }
    if (sdlHandle != NULL && handle == sdlHandle && symbol != NULL && strcmp(symbol, "JNI_OnLoad") == 0) {
        originalSdlJniOnLoad = (jni_on_load_func_t) result;
        result = (void *) isolatedSdlJniOnLoad;
    }
    return result;
}

void create_sdl_dlopen_hooks(bytehook_hook_all_t hookAll) {
    if (hookAll == NULL) return;
    hookAll(NULL, "dlsym", (void *) customDlsym, NULL, NULL);
}