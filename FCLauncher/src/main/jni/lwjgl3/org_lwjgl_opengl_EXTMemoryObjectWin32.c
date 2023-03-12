/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glImportMemoryWin32HandleEXTPROC) (jint, jlong, jint, intptr_t);
typedef void (APIENTRY *glImportMemoryWin32NameEXTPROC) (jint, jlong, jint, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTMemoryObjectWin32_nglImportMemoryWin32HandleEXT(JNIEnv *__env, jclass clazz, jint memory, jlong size, jint handleType, jlong handleAddress) {
    glImportMemoryWin32HandleEXTPROC glImportMemoryWin32HandleEXT = (glImportMemoryWin32HandleEXTPROC)tlsGetFunction(1775);
    intptr_t handle = (intptr_t)handleAddress;
    UNUSED_PARAM(clazz)
    glImportMemoryWin32HandleEXT(memory, size, handleType, handle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTMemoryObjectWin32_nglImportMemoryWin32NameEXT(JNIEnv *__env, jclass clazz, jint memory, jlong size, jint handleType, jlong nameAddress) {
    glImportMemoryWin32NameEXTPROC glImportMemoryWin32NameEXT = (glImportMemoryWin32NameEXTPROC)tlsGetFunction(1776);
    intptr_t name = (intptr_t)nameAddress;
    UNUSED_PARAM(clazz)
    glImportMemoryWin32NameEXT(memory, size, handleType, name);
}

EXTERN_C_EXIT
