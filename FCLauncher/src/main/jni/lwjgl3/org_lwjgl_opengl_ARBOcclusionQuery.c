/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glGenQueriesARBPROC) (jint, intptr_t);
typedef void (APIENTRY *glDeleteQueriesARBPROC) (jint, intptr_t);
typedef jboolean (APIENTRY *glIsQueryARBPROC) (jint);
typedef void (APIENTRY *glBeginQueryARBPROC) (jint, jint);
typedef void (APIENTRY *glEndQueryARBPROC) (jint);
typedef void (APIENTRY *glGetQueryivARBPROC) (jint, jint, intptr_t);
typedef void (APIENTRY *glGetQueryObjectivARBPROC) (jint, jint, intptr_t);
typedef void (APIENTRY *glGetQueryObjectuivARBPROC) (jint, jint, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglGenQueriesARB__IJ(JNIEnv *__env, jclass clazz, jint n, jlong idsAddress) {
    glGenQueriesARBPROC glGenQueriesARB = (glGenQueriesARBPROC)tlsGetFunction(1246);
    intptr_t ids = (intptr_t)idsAddress;
    UNUSED_PARAM(clazz)
    glGenQueriesARB(n, ids);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglDeleteQueriesARB__IJ(JNIEnv *__env, jclass clazz, jint n, jlong idsAddress) {
    glDeleteQueriesARBPROC glDeleteQueriesARB = (glDeleteQueriesARBPROC)tlsGetFunction(1247);
    intptr_t ids = (intptr_t)idsAddress;
    UNUSED_PARAM(clazz)
    glDeleteQueriesARB(n, ids);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_glIsQueryARB(JNIEnv *__env, jclass clazz, jint id) {
    glIsQueryARBPROC glIsQueryARB = (glIsQueryARBPROC)tlsGetFunction(1248);
    UNUSED_PARAM(clazz)
    return (jboolean)glIsQueryARB(id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_glBeginQueryARB(JNIEnv *__env, jclass clazz, jint target, jint id) {
    glBeginQueryARBPROC glBeginQueryARB = (glBeginQueryARBPROC)tlsGetFunction(1249);
    UNUSED_PARAM(clazz)
    glBeginQueryARB(target, id);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_glEndQueryARB(JNIEnv *__env, jclass clazz, jint target) {
    glEndQueryARBPROC glEndQueryARB = (glEndQueryARBPROC)tlsGetFunction(1250);
    UNUSED_PARAM(clazz)
    glEndQueryARB(target);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglGetQueryivARB__IIJ(JNIEnv *__env, jclass clazz, jint target, jint pname, jlong paramsAddress) {
    glGetQueryivARBPROC glGetQueryivARB = (glGetQueryivARBPROC)tlsGetFunction(1251);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetQueryivARB(target, pname, params);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglGetQueryObjectivARB__IIJ(JNIEnv *__env, jclass clazz, jint id, jint pname, jlong paramsAddress) {
    glGetQueryObjectivARBPROC glGetQueryObjectivARB = (glGetQueryObjectivARBPROC)tlsGetFunction(1252);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetQueryObjectivARB(id, pname, params);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBOcclusionQuery_nglGetQueryObjectuivARB__IIJ(JNIEnv *__env, jclass clazz, jint id, jint pname, jlong paramsAddress) {
    glGetQueryObjectuivARBPROC glGetQueryObjectuivARB = (glGetQueryObjectuivARBPROC)tlsGetFunction(1253);
    intptr_t params = (intptr_t)paramsAddress;
    UNUSED_PARAM(clazz)
    glGetQueryObjectuivARB(id, pname, params);
}

EXTERN_C_EXIT
