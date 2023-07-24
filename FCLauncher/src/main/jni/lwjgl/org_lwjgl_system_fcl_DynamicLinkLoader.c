/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "FCLLWJGL.h"
#include <dlfcn.h>

EXTERN_C_ENTER

static void* glesHandle;

JNIEXPORT jlong JNICALL Java_org_lwjgl_system_fcl_DynamicLinkLoader_ndlopen(JNIEnv *__env, jclass clazz, jlong filenameAddress, jint mode) {
    char const *filename = (char const *)(intptr_t)filenameAddress;
    UNUSED_PARAMS(__env, clazz)
    if (!glesHandle) {
        glesHandle = dlopen("libGLESv2.so", RTLD_LAZY | RTLD_GLOBAL);
    }
    return (jlong)(intptr_t)dlopen(filename, RTLD_LAZY | RTLD_GLOBAL);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_system_fcl_DynamicLinkLoader_ndlerror(JNIEnv *__env, jclass clazz) {
    UNUSED_PARAMS(__env, clazz)
    return (jlong)(intptr_t)dlerror();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_system_fcl_DynamicLinkLoader_ndlsym(JNIEnv *__env, jclass clazz, jlong handleAddress, jlong nameAddress) {
    void *handle = (void *)(intptr_t)handleAddress;
    char const *name = (char const *)(intptr_t)nameAddress;
    UNUSED_PARAMS(__env, clazz)
    jlong retval = (jlong)(intptr_t)dlsym(handle, name);
    if (!retval && name[0] == 'g' && name[1] == 'l') {
        retval = (jlong)(intptr_t)dlsym(glesHandle, name);
    }
    return retval;
}

JNIEXPORT jint JNICALL Java_org_lwjgl_system_fcl_DynamicLinkLoader_ndlclose(JNIEnv *__env, jclass clazz, jlong handleAddress) {
    void *handle = (void *)(intptr_t)handleAddress;
    UNUSED_PARAMS(__env, clazz)
    return (jint)dlclose(handle);
}

EXTERN_C_EXIT
