/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glPointParameterfEXTPROC) (jint, jfloat);
typedef void (APIENTRY *glPointParameterfvEXTPROC) (jint, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPointParameters_glPointParameterfEXT(JNIEnv *__env, jclass clazz, jint pname, jfloat param) {
    glPointParameterfEXTPROC glPointParameterfEXT = (glPointParameterfEXTPROC)tlsGetFunction(1777);
    UNUSED_PARAM(clazz)
    glPointParameterfEXT(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_EXTPointParameters_nglPointParameterfvEXT__IJ(JNIEnv *__env, jclass clazz, jint pname, jlong paramsAddress) {
    glPointParameterfvEXTPROC glPointParameterfvEXT = (glPointParameterfvEXTPROC)tlsGetFunction(1778);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glPointParameterfvEXT(pname, params);
}

EXTERN_C_EXIT
