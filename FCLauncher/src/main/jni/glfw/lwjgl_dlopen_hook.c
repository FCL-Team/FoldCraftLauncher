//
// Created by maks on 06.01.2025.
//

#include <android/api-level.h>
#include <android/log.h>
#include <jni.h>

#include <dlfcn.h>
#include <string.h>
#include <stdlib.h>
#include "fcl/include/fcl_internal.h"

extern void* maybe_load_vulkan();

/**
 * Basically a verbatim implementation of ndlopen(), found at
 * https://github.com/PojavLauncherTeam/lwjgl3/blob/3.3.1/modules/lwjgl/core/src/generated/c/linux/org_lwjgl_system_linux_DynamicLinkLoader.c#L11
 * but with our own additions for stuff like vulkanmod.
 */
static jlong ndlopen_bugfix(__attribute__((unused)) JNIEnv *env,
                            __attribute__((unused)) jclass class,
                            jlong filename_ptr,
                            jint jmode) {
    const char* filename = (const char*) filename_ptr;
    // Oveeride vulkan loading to let us load vulkan ourselves
    if(strstr(filename, "libvulkan.so") == filename) {
        FCL_LOG("LWJGL linkerhook: replacing load for libvulkan.so with custom driver");
        return (jlong) maybe_load_vulkan();
    }
    if (getenv("RENDERER_HANDLE") != NULL && strstr(filename,"plugin")) {
        return (jlong) strtol(getenv("RENDERER_HANDLE"), NULL, 10);
    }

    // This hook also serves the task of mitigating a bug: the idea is that since, on Android 10 and
    // earlier, the linker doesn't really do namespace nesting.
    // It is not a problem as most of the libraries are in the launcher path, but when you try to run
    // VulkanMod which loads shaderc outside of the default jni libs directory through this method,
    // it can't load it because the path is not in the allowed paths for the anonymous namesapce.
    // This method fixes the issue by being in libpojavexec, and thus being in the classloader namespace

    int mode = (int)jmode;
    jlong handle = (jlong) dlopen(filename, mode);
    return handle;
}

/**
 * Install the LWJGL dlopen hook. This allows us to mitigate linker bugs and add custom library overrides.
 */
void installLwjglDlopenHook(JavaVM* vm) {
    FCL_LOG("LwjglLinkerHook:%s", "Installing LWJGL dlopen() hook");
    JNIEnv *env = NULL;
    (*vm)->GetEnv(vm, (void **) &env, JNI_VERSION_1_4);
    jclass dynamicLinkLoader = (*env)->FindClass(env, "org/lwjgl/system/linux/DynamicLinkLoader");
    if(dynamicLinkLoader == NULL) {
        FCL_LOG( "LwjglLinkerHook:%s", "Failed to find the target class");
        (*env)->ExceptionClear(env);
        return;
    }
    JNINativeMethod ndlopenMethod[] = {
            {"ndlopen", "(JI)J", &ndlopen_bugfix}
    };
    if((*env)->RegisterNatives(env, dynamicLinkLoader, ndlopenMethod, 1) != 0) {
        FCL_LOG( "LwjglLinkerHook:%s", "Failed to register the hooked method");
        (*env)->ExceptionClear(env);
    }
}