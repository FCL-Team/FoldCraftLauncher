/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glGetInternalformatSampleivNVPROC) (jint, jint, jint, jint, jint, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVInternalformatSampleQuery_nglGetInternalformatSampleivNV__IIIIIJ(JNIEnv *__env, jclass clazz, jint target, jint internalformat, jint samples, jint pname, jint bufSize, jlong paramsAddress) {
    glGetInternalformatSampleivNVPROC glGetInternalformatSampleivNV = (glGetInternalformatSampleivNVPROC)tlsGetFunction(2025);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetInternalformatSampleivNV(target, internalformat, samples, pname, bufSize, params);
}

EXTERN_C_EXIT
