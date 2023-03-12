/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glVertexAttribL1dEXTPROC) (jint, jdouble);
typedef void (APIENTRY *glVertexAttribL2dEXTPROC) (jint, jdouble, jdouble);
typedef void (APIENTRY *glVertexAttribL3dEXTPROC) (jint, jdouble, jdouble, jdouble);
typedef void (APIENTRY *glVertexAttribL4dEXTPROC) (jint, jdouble, jdouble, jdouble, jdouble);
typedef void (APIENTRY *glVertexAttribL1dvEXTPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL2dvEXTPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL3dvEXTPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribL4dvEXTPROC) (jint, intptr_t);
typedef void (APIENTRY *glVertexAttribLPointerEXTPROC) (jint, jint, jint, jint, intptr_t);
typedef void (APIENTRY *glGetVertexAttribLdvEXTPROC) (jint, jint, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_glVertexAttribL1dEXT(JNIEnv *__env, jclass clazz, jint index, jdouble x) {
    glVertexAttribL1dEXTPROC glVertexAttribL1dEXT = (glVertexAttribL1dEXTPROC)tlsGetFunction(1834);
    UNUSED_PARAM(clazz)
    glVertexAttribL1dEXT(index, x);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_glVertexAttribL2dEXT(JNIEnv *__env, jclass clazz, jint index, jdouble x, jdouble y) {
    glVertexAttribL2dEXTPROC glVertexAttribL2dEXT = (glVertexAttribL2dEXTPROC)tlsGetFunction(1835);
    UNUSED_PARAM(clazz)
    glVertexAttribL2dEXT(index, x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_glVertexAttribL3dEXT(JNIEnv *__env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z) {
    glVertexAttribL3dEXTPROC glVertexAttribL3dEXT = (glVertexAttribL3dEXTPROC)tlsGetFunction(1836);
    UNUSED_PARAM(clazz)
    glVertexAttribL3dEXT(index, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_glVertexAttribL4dEXT(JNIEnv *__env, jclass clazz, jint index, jdouble x, jdouble y, jdouble z, jdouble w) {
    glVertexAttribL4dEXTPROC glVertexAttribL4dEXT = (glVertexAttribL4dEXTPROC)tlsGetFunction(1837);
    UNUSED_PARAM(clazz)
    glVertexAttribL4dEXT(index, x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL1dvEXT__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL1dvEXTPROC glVertexAttribL1dvEXT = (glVertexAttribL1dvEXTPROC)tlsGetFunction(1838);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL1dvEXT(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL2dvEXT__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL2dvEXTPROC glVertexAttribL2dvEXT = (glVertexAttribL2dvEXTPROC)tlsGetFunction(1839);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL2dvEXT(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL3dvEXT__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL3dvEXTPROC glVertexAttribL3dvEXT = (glVertexAttribL3dvEXTPROC)tlsGetFunction(1840);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL3dvEXT(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribL4dvEXT__IJ(JNIEnv *__env, jclass clazz, jint index, jlong vAddress) {
    glVertexAttribL4dvEXTPROC glVertexAttribL4dvEXT = (glVertexAttribL4dvEXTPROC)tlsGetFunction(1841);
    intptr_t v = (intptr_t)vAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribL4dvEXT(index, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglVertexAttribLPointerEXT(JNIEnv *__env, jclass clazz, jint index, jint size, jint type, jint stride, jlong pointerAddress) {
    glVertexAttribLPointerEXTPROC glVertexAttribLPointerEXT = (glVertexAttribLPointerEXTPROC)tlsGetFunction(1842);
    intptr_t pointer = (intptr_t)pointerAddress;
    UNUSED_PARAM(clazz)
    glVertexAttribLPointerEXT(index, size, type, stride, pointer);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTVertexAttrib64bit_nglGetVertexAttribLdvEXT__IIJ(JNIEnv *__env, jclass clazz, jint index, jint pname, jlong paramsAddress) {
    glGetVertexAttribLdvEXTPROC glGetVertexAttribLdvEXT = (glGetVertexAttribLdvEXTPROC)tlsGetFunction(1843);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetVertexAttribLdvEXT(index, pname, params);
}

EXTERN_C_EXIT
