/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef jboolean (APIENTRY *glAcquireKeyedMutexWin32EXTPROC) (jint, jlong, jint);
typedef jboolean (APIENTRY *glReleaseKeyedMutexWin32EXTPROC) (jint, jlong);

EXTERN_C_ENTER

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_EXTWin32KeyedMutex_glAcquireKeyedMutexWin32EXT(JNIEnv *__env, jclass clazz, jint memory, jlong key, jint timeout) {
    glAcquireKeyedMutexWin32EXTPROC glAcquireKeyedMutexWin32EXT = (glAcquireKeyedMutexWin32EXTPROC)tlsGetFunction(1844);
    UNUSED_PARAM(clazz)
    return (jboolean)glAcquireKeyedMutexWin32EXT(memory, key, timeout);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_EXTWin32KeyedMutex_glReleaseKeyedMutexWin32EXT(JNIEnv *__env, jclass clazz, jint memory, jlong key) {
    glReleaseKeyedMutexWin32EXTPROC glReleaseKeyedMutexWin32EXT = (glReleaseKeyedMutexWin32EXTPROC)tlsGetFunction(1845);
    UNUSED_PARAM(clazz)
    return (jboolean)glReleaseKeyedMutexWin32EXT(memory, key);
}

EXTERN_C_EXIT
