/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glTextureBufferRangeEXTPROC) (jint, jint, jint, jint, intptr_t, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBTextureBufferRange_glTextureBufferRangeEXT(JNIEnv *__env, jclass clazz, jint texture, jint target, jint internalformat, jint buffer, jlong offset, jlong size) {
    glTextureBufferRangeEXTPROC glTextureBufferRangeEXT = (glTextureBufferRangeEXTPROC)tlsGetFunction(1332);
    UNUSED_PARAM(clazz)
    glTextureBufferRangeEXT(texture, target, internalformat, buffer, (intptr_t)offset, (intptr_t)size);
}

EXTERN_C_EXIT
