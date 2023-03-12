/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glStringMarkerGREMEDYPROC) (jint, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GREMEDYStringMarker_nglStringMarkerGREMEDY(JNIEnv *__env, jclass clazz, jint len, jlong stringAddress) {
    glStringMarkerGREMEDYPROC glStringMarkerGREMEDY = (glStringMarkerGREMEDYPROC)tlsGetFunction(1849);
    intptr_t string = (intptr_t)stringAddress;
    UNUSED_PARAM(clazz)
    glStringMarkerGREMEDY(len, string);
}

EXTERN_C_EXIT
