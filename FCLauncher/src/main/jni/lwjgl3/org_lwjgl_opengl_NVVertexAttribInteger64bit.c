/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glVertexAttribL1i64NVPROC) (jint, jlong);
typedef void (APIENTRY *glVertexAttribL2i64NVPROC) (jint, jlong, jlong);
typedef void (APIENTRY *glVertexAttribL3i64NVPROC) (jint, jlong, jlong, jlong);
typedef void (APIENTRY *glVertexAttribL4i64NVPROC) (jint, jlong, jlong, jlong, jlong);
typedef void (APIENTRY *glVertexAttribL1i64vNVPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL2i64vNVPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL3i64vNVPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL4i64vNVPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL1ui64NVPROC) (jint, jlong);
typedef void (APIENTRY *glVertexAttribL2ui64NVPROC) (jint, jlong, jlong);
typedef void (APIENTRY *glVertexAttribL3ui64NVPROC) (jint, jlong, jlong, jlong);
typedef void (APIENTRY *glVertexAttribL4ui64NVPROC) (jint, jlong, jlong, jlong, jlong);
typedef void (APIENTRY *glVertexAttribL1ui64vNVPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL2ui64vNVPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL3ui64vNVPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL4ui64vNVPROC) (jint, intptr_t);
typedef void (APIENTRY *glGetVertexAttribLi64vNVPROC) (jint, jint, intptr_t);
typedef void (APIENTRY *glGetVertexAttribLui64vNVPROC) (jint, jint, intptr_t);
typedef void (APIENTRY *glVertexAttribLFormatNVPROC) (jint, jint, jint, jint);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribL1i64NV(JNIEnv *__env, jclass clazz, jint index, jlong x) {
    glVertexAttribL1i64NVPROC glVertexAttribL1i64NV = (glVertexAttribL1i64NVPROC)tlsGetFunction(2164);
    UNUSED_PARAM(clazz)
    glVertexAttribL1i64NV(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribL2i64NV(JNIEnv *__env, jclass clazz, jint index, jlong x, jlong y) {
    glVertexAttribL2i64NVPROC glVertexAttribL2i64NV = (glVertexAttribL2i64NVPROC)tlsGetFunction(2165);
    UNUSED_PARAM(clazz)
    glVertexAttribL2i64NV(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribL3i64NV(JNIEnv *__env, jclass clazz, jint index, jlong x, jlong y, jlong z) {
    glVertexAttribL3i64NVPROC glVertexAttribL3i64NV = (glVertexAttribL3i64NVPROC)tlsGetFunction(2166);
    UNUSED_PARAM(clazz)
    glVertexAttribL3i64NV(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribL4i64NV(JNIEnv *__env, jclass clazz, jint index, jlong x, jlong y, jlong z, jlong w) {
    glVertexAttribL4i64NVPROC glVertexAttribL4i64NV = (glVertexAttribL4i64NVPROC)tlsGetFunction(2167);
    UNUSED_PARAM(clazz)
    glVertexAttribL4i64NV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL1i64vNV__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL1i64vNVPROC glVertexAttribL1i64vNV = (glVertexAttribL1i64vNVPROC)tlsGetFunction(2168);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL1i64vNV(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL2i64vNV__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL2i64vNVPROC glVertexAttribL2i64vNV = (glVertexAttribL2i64vNVPROC)tlsGetFunction(2169);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL2i64vNV(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL3i64vNV__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL3i64vNVPROC glVertexAttribL3i64vNV = (glVertexAttribL3i64vNVPROC)tlsGetFunction(2170);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL3i64vNV(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL4i64vNV__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL4i64vNVPROC glVertexAttribL4i64vNV = (glVertexAttribL4i64vNVPROC)tlsGetFunction(2171);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL4i64vNV(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribL1ui64NV(JNIEnv *__env, jclass clazz, jint index, jlong x) {
    glVertexAttribL1ui64NVPROC glVertexAttribL1ui64NV = (glVertexAttribL1ui64NVPROC)tlsGetFunction(2172);
    UNUSED_PARAM(clazz)
    glVertexAttribL1ui64NV(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribL2ui64NV(JNIEnv *__env, jclass clazz, jint index, jlong x, jlong y) {
    glVertexAttribL2ui64NVPROC glVertexAttribL2ui64NV = (glVertexAttribL2ui64NVPROC)tlsGetFunction(2173);
    UNUSED_PARAM(clazz)
    glVertexAttribL2ui64NV(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribL3ui64NV(JNIEnv *__env, jclass clazz, jint index, jlong x, jlong y, jlong z) {
    glVertexAttribL3ui64NVPROC glVertexAttribL3ui64NV = (glVertexAttribL3ui64NVPROC)tlsGetFunction(2174);
    UNUSED_PARAM(clazz)
    glVertexAttribL3ui64NV(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribL4ui64NV(JNIEnv *__env, jclass clazz, jint index, jlong x, jlong y, jlong z, jlong w) {
    glVertexAttribL4ui64NVPROC glVertexAttribL4ui64NV = (glVertexAttribL4ui64NVPROC)tlsGetFunction(2175);
    UNUSED_PARAM(clazz)
    glVertexAttribL4ui64NV(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL1ui64vNV__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL1ui64vNVPROC glVertexAttribL1ui64vNV = (glVertexAttribL1ui64vNVPROC)tlsGetFunction(2176);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL1ui64vNV(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL2ui64vNV__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL2ui64vNVPROC glVertexAttribL2ui64vNV = (glVertexAttribL2ui64vNVPROC)tlsGetFunction(2177);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL2ui64vNV(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL3ui64vNV__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL3ui64vNVPROC glVertexAttribL3ui64vNV = (glVertexAttribL3ui64vNVPROC)tlsGetFunction(2178);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL3ui64vNV(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglVertexAttribL4ui64vNV__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL4ui64vNVPROC glVertexAttribL4ui64vNV = (glVertexAttribL4ui64vNVPROC)tlsGetFunction(2179);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL4ui64vNV(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglGetVertexAttribLi64vNV__IIJ(JNIEnv *__env, jclass clazz, jint index, jint pname, jlong paramsAddress) {
    glGetVertexAttribLi64vNVPROC glGetVertexAttribLi64vNV = (glGetVertexAttribLi64vNVPROC)tlsGetFunction(2180);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetVertexAttribLi64vNV(index, pname, params);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_nglGetVertexAttribLui64vNV__IIJ(JNIEnv *__env, jclass clazz, jint index, jint pname, jlong paramsAddress) {
    glGetVertexAttribLui64vNVPROC glGetVertexAttribLui64vNV = (glGetVertexAttribLui64vNVPROC)tlsGetFunction(2181);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetVertexAttribLui64vNV(index, pname, params);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexAttribInteger64bit_glVertexAttribLFormatNV(JNIEnv *__env, jclass clazz, jint index, jint size, jint type, jint stride) {
    glVertexAttribLFormatNVPROC glVertexAttribLFormatNV = (glVertexAttribLFormatNVPROC)tlsGetFunction(2182);
    UNUSED_PARAM(clazz)
    glVertexAttribLFormatNV(index, size, type, stride);
}

EXTERN_C_EXIT
