/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glGenSemaphoresEXTPROC) (jint, intptr_t);
typedef void (APIENTRY *glDeleteSemaphoresEXTPROC) (jint, intptr_t);
typedef jboolean (APIENTRY *glIsSemaphoreEXTPROC) (jint);
typedef void (APIENTRY *glSemaphoreParameterui64vEXTPROC) (jint, jint, intptr_t);
typedef void (APIENTRY *glGetSemaphoreParameterui64vEXTPROC) (jint, jint, intptr_t);
typedef void (APIENTRY *glWaitSemaphoreEXTPROC) (jint, jint, intptr_t, jint, intptr_t, intptr_t);
typedef void (APIENTRY *glSignalSemaphoreEXTPROC) (jint, jint, intptr_t, jint, intptr_t, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSemaphore_nglGenSemaphoresEXT__IJ(JNIEnv *__env, jclass clazz, jint n, jlong semaphoresAddress) {
    glGenSemaphoresEXTPROC glGenSemaphoresEXT = (glGenSemaphoresEXTPROC)tlsGetFunction(1799);
    intptr_t semaphores = (intptr_t)semaphoresAddress;
    UNUSED_PARAM(clazz)
    glGenSemaphoresEXT(n, semaphores);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSemaphore_nglDeleteSemaphoresEXT__IJ(JNIEnv *__env, jclass clazz, jint n, jlong semaphoresAddress) {
    glDeleteSemaphoresEXTPROC glDeleteSemaphoresEXT = (glDeleteSemaphoresEXTPROC)tlsGetFunction(1800);
    intptr_t semaphores = (intptr_t)semaphoresAddress;
    UNUSED_PARAM(clazz)
    glDeleteSemaphoresEXT(n, semaphores);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_EXTSemaphore_glIsSemaphoreEXT(JNIEnv *__env, jclass clazz, jint semaphore) {
    glIsSemaphoreEXTPROC glIsSemaphoreEXT = (glIsSemaphoreEXTPROC)tlsGetFunction(1801);
    UNUSED_PARAM(clazz)
    return (jboolean)glIsSemaphoreEXT(semaphore);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSemaphore_nglSemaphoreParameterui64vEXT__IIJ(JNIEnv *__env, jclass clazz, jint semaphore, jint pname, jlong paramsAddress) {
    glSemaphoreParameterui64vEXTPROC glSemaphoreParameterui64vEXT = (glSemaphoreParameterui64vEXTPROC)tlsGetFunction(1802);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glSemaphoreParameterui64vEXT(semaphore, pname, params);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSemaphore_nglGetSemaphoreParameterui64vEXT__IIJ(JNIEnv *__env, jclass clazz, jint semaphore, jint pname, jlong paramsAddress) {
    glGetSemaphoreParameterui64vEXTPROC glGetSemaphoreParameterui64vEXT = (glGetSemaphoreParameterui64vEXTPROC)tlsGetFunction(1803);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetSemaphoreParameterui64vEXT(semaphore, pname, params);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSemaphore_nglWaitSemaphoreEXT__IIJIJJ(JNIEnv *__env, jclass clazz, jint semaphore, jint numBufferBarriers, jlong buffersAddress, jint numTextureBarriers, jlong texturesAddress, jlong srcLayoutsAddress) {
    glWaitSemaphoreEXTPROC glWaitSemaphoreEXT = (glWaitSemaphoreEXTPROC)tlsGetFunction(1804);
    intptr_t buffers = (intptr_t)buffersAddress;
    intptr_t textures = (intptr_t)texturesAddress;
    intptr_t srcLayouts = (intptr_t)srcLayoutsAddress;
    UNUSED_PARAM(clazz)
    glWaitSemaphoreEXT(semaphore, numBufferBarriers, buffers, numTextureBarriers, textures, srcLayouts);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTSemaphore_nglSignalSemaphoreEXT__IIJIJJ(JNIEnv *__env, jclass clazz, jint semaphore, jint numBufferBarriers, jlong buffersAddress, jint numTextureBarriers, jlong texturesAddress, jlong dstLayoutsAddress) {
    glSignalSemaphoreEXTPROC glSignalSemaphoreEXT = (glSignalSemaphoreEXTPROC)tlsGetFunction(1805);
    intptr_t buffers = (intptr_t)buffersAddress;
    intptr_t textures = (intptr_t)texturesAddress;
    intptr_t dstLayouts = (intptr_t)dstLayoutsAddress;
    UNUSED_PARAM(clazz)
    glSignalSemaphoreEXT(semaphore, numBufferBarriers, buffers, numTextureBarriers, textures, dstLayouts);
}

EXTERN_C_EXIT
