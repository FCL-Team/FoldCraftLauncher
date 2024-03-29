/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "lwjgl/common_tools.h"
#include "opengl.h"

typedef jlong (APIENTRY *glGetTextureHandleARBPROC) (jint);
typedef jlong (APIENTRY *glGetTextureSamplerHandleARBPROC) (jint, jint);
typedef void (APIENTRY *glMakeTextureHandleResidentARBPROC) (jlong);
typedef void (APIENTRY *glMakeTextureHandleNonResidentARBPROC) (jlong);
typedef jlong (APIENTRY *glGetImageHandleARBPROC) (jint, jint, jboolean, jint, jint);
typedef void (APIENTRY *glMakeImageHandleResidentARBPROC) (jlong, jint);
typedef void (APIENTRY *glMakeImageHandleNonResidentARBPROC) (jlong);
typedef void (APIENTRY *glUniformHandleui64ARBPROC) (jint, jlong);
typedef void (APIENTRY *glUniformHandleui64vARBPROC) (jint, jint, uintptr_t);
typedef void (APIENTRY *glProgramUniformHandleui64ARBPROC) (jint, jint, jlong);
typedef void (APIENTRY *glProgramUniformHandleui64vARBPROC) (jint, jint, jint, uintptr_t);
typedef jboolean (APIENTRY *glIsTextureHandleResidentARBPROC) (jlong);
typedef jboolean (APIENTRY *glIsImageHandleResidentARBPROC) (jlong);
typedef void (APIENTRY *glVertexAttribL1ui64ARBPROC) (jint, jlong);
typedef void (APIENTRY *glVertexAttribL1ui64vARBPROC) (jint, uintptr_t);
typedef void (APIENTRY *glGetVertexAttribLui64vARBPROC) (jint, jint, uintptr_t);

EXTERN_C_ENTER

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glGetTextureHandleARB(JNIEnv *__env, jclass clazz, jint texture) {
    glGetTextureHandleARBPROC glGetTextureHandleARB = (glGetTextureHandleARBPROC)tlsGetFunction(1111);
    UNUSED_PARAM(clazz)
    return glGetTextureHandleARB(texture);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glGetTextureSamplerHandleARB(JNIEnv *__env, jclass clazz, jint texture, jint sampler) {
    glGetTextureSamplerHandleARBPROC glGetTextureSamplerHandleARB = (glGetTextureSamplerHandleARBPROC)tlsGetFunction(1112);
    UNUSED_PARAM(clazz)
    return glGetTextureSamplerHandleARB(texture, sampler);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glMakeTextureHandleResidentARB(JNIEnv *__env, jclass clazz, jlong handle) {
    glMakeTextureHandleResidentARBPROC glMakeTextureHandleResidentARB = (glMakeTextureHandleResidentARBPROC)tlsGetFunction(1113);
    UNUSED_PARAM(clazz)
    glMakeTextureHandleResidentARB(handle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glMakeTextureHandleNonResidentARB(JNIEnv *__env, jclass clazz, jlong handle) {
    glMakeTextureHandleNonResidentARBPROC glMakeTextureHandleNonResidentARB = (glMakeTextureHandleNonResidentARBPROC)tlsGetFunction(1114);
    UNUSED_PARAM(clazz)
    glMakeTextureHandleNonResidentARB(handle);
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glGetImageHandleARB(JNIEnv *__env, jclass clazz, jint texture, jint level, jboolean layered, jint layer, jint format) {
    glGetImageHandleARBPROC glGetImageHandleARB = (glGetImageHandleARBPROC)tlsGetFunction(1115);
    UNUSED_PARAM(clazz)
    return glGetImageHandleARB(texture, level, layered, layer, format);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glMakeImageHandleResidentARB(JNIEnv *__env, jclass clazz, jlong handle, jint access) {
    glMakeImageHandleResidentARBPROC glMakeImageHandleResidentARB = (glMakeImageHandleResidentARBPROC)tlsGetFunction(1116);
    UNUSED_PARAM(clazz)
    glMakeImageHandleResidentARB(handle, access);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glMakeImageHandleNonResidentARB(JNIEnv *__env, jclass clazz, jlong handle) {
    glMakeImageHandleNonResidentARBPROC glMakeImageHandleNonResidentARB = (glMakeImageHandleNonResidentARBPROC)tlsGetFunction(1117);
    UNUSED_PARAM(clazz)
    glMakeImageHandleNonResidentARB(handle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glUniformHandleui64ARB(JNIEnv *__env, jclass clazz, jint location, jlong value) {
    glUniformHandleui64ARBPROC glUniformHandleui64ARB = (glUniformHandleui64ARBPROC)tlsGetFunction(1118);
    UNUSED_PARAM(clazz)
    glUniformHandleui64ARB(location, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglUniformHandleui64vARB__IIJ(JNIEnv *__env, jclass clazz, jint location, jint count, jlong valuesAddress) {
    glUniformHandleui64vARBPROC glUniformHandleui64vARB = (glUniformHandleui64vARBPROC)tlsGetFunction(1119);
    uintptr_t values = (uintptr_t)valuesAddress;
    UNUSED_PARAM(clazz)
    glUniformHandleui64vARB(location, count, values);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glProgramUniformHandleui64ARB(JNIEnv *__env, jclass clazz, jint program, jint location, jlong value) {
    glProgramUniformHandleui64ARBPROC glProgramUniformHandleui64ARB = (glProgramUniformHandleui64ARBPROC)tlsGetFunction(1120);
    UNUSED_PARAM(clazz)
    glProgramUniformHandleui64ARB(program, location, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglProgramUniformHandleui64vARB__IIIJ(JNIEnv *__env, jclass clazz, jint program, jint location, jint count, jlong valuesAddress) {
    glProgramUniformHandleui64vARBPROC glProgramUniformHandleui64vARB = (glProgramUniformHandleui64vARBPROC)tlsGetFunction(1121);
    uintptr_t values = (uintptr_t)valuesAddress;
    UNUSED_PARAM(clazz)
    glProgramUniformHandleui64vARB(program, location, count, values);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glIsTextureHandleResidentARB(JNIEnv *__env, jclass clazz, jlong handle) {
    glIsTextureHandleResidentARBPROC glIsTextureHandleResidentARB = (glIsTextureHandleResidentARBPROC)tlsGetFunction(1122);
    UNUSED_PARAM(clazz)
    return glIsTextureHandleResidentARB(handle);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glIsImageHandleResidentARB(JNIEnv *__env, jclass clazz, jlong handle) {
    glIsImageHandleResidentARBPROC glIsImageHandleResidentARB = (glIsImageHandleResidentARBPROC)tlsGetFunction(1123);
    UNUSED_PARAM(clazz)
    return glIsImageHandleResidentARB(handle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_glVertexAttribL1ui64ARB(JNIEnv *__env, jclass clazz, jint index, jlong x) {
    glVertexAttribL1ui64ARBPROC glVertexAttribL1ui64ARB = (glVertexAttribL1ui64ARBPROC)tlsGetFunction(1124);
    UNUSED_PARAM(clazz)
    glVertexAttribL1ui64ARB(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglVertexAttribL1ui64vARB__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL1ui64vARBPROC glVertexAttribL1ui64vARB = (glVertexAttribL1ui64vARBPROC)tlsGetFunction(1125);
    uintptr_t v = (uintptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL1ui64vARB(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBBindlessTexture_nglGetVertexAttribLui64vARB__IIJ(JNIEnv *__env, jclass clazz, jint index, jint pname, jlong paramsAddress) {
    glGetVertexAttribLui64vARBPROC glGetVertexAttribLui64vARB = (glGetVertexAttribLui64vARBPROC)tlsGetFunction(1126);
    uintptr_t params = (uintptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetVertexAttribLui64vARB(index, pname, params);
}

EXTERN_C_EXIT
