/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
#include "common_tools.h"
#include "opengl.h"

typedef void (APIENTRY *glBeginPerfQueryINTELPROC) (jint);
typedef void (APIENTRY *glCreatePerfQueryINTELPROC) (jint, intptr_t);
typedef void (APIENTRY *glDeletePerfQueryINTELPROC) (jint);
typedef void (APIENTRY *glEndPerfQueryINTELPROC) (jint);
typedef void (APIENTRY *glGetFirstPerfQueryIdINTELPROC) (intptr_t);
typedef void (APIENTRY *glGetNextPerfQueryIdINTELPROC) (jint, intptr_t);
typedef void (APIENTRY *glGetPerfCounterInfoINTELPROC) (jint, jint, jint, intptr_t, jint, intptr_t, intptr_t, intptr_t, intptr_t, intptr_t, intptr_t);
typedef void (APIENTRY *glGetPerfQueryDataINTELPROC) (jint, jint, jint, intptr_t, intptr_t);
typedef void (APIENTRY *glGetPerfQueryIdByNameINTELPROC) (intptr_t, intptr_t);
typedef void (APIENTRY *glGetPerfQueryInfoINTELPROC) (jint, jint, intptr_t, intptr_t, intptr_t, intptr_t, intptr_t);

EXTERN_C_ENTER

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_glBeginPerfQueryINTEL(JNIEnv *__env, jclass clazz, jint queryHandle) {
    glBeginPerfQueryINTELPROC glBeginPerfQueryINTEL = (glBeginPerfQueryINTELPROC)tlsGetFunction(1854);
    UNUSED_PARAM(clazz)
    glBeginPerfQueryINTEL(queryHandle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_nglCreatePerfQueryINTEL__IJ(JNIEnv *__env, jclass clazz, jint queryId, jlong queryHandleAddress) {
    glCreatePerfQueryINTELPROC glCreatePerfQueryINTEL = (glCreatePerfQueryINTELPROC)tlsGetFunction(1855);
    intptr_t queryHandle = (intptr_t)queryHandleAddress;
    UNUSED_PARAM(clazz)
    glCreatePerfQueryINTEL(queryId, queryHandle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_glDeletePerfQueryINTEL(JNIEnv *__env, jclass clazz, jint queryHandle) {
    glDeletePerfQueryINTELPROC glDeletePerfQueryINTEL = (glDeletePerfQueryINTELPROC)tlsGetFunction(1856);
    UNUSED_PARAM(clazz)
    glDeletePerfQueryINTEL(queryHandle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_glEndPerfQueryINTEL(JNIEnv *__env, jclass clazz, jint queryHandle) {
    glEndPerfQueryINTELPROC glEndPerfQueryINTEL = (glEndPerfQueryINTELPROC)tlsGetFunction(1857);
    UNUSED_PARAM(clazz)
    glEndPerfQueryINTEL(queryHandle);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_nglGetFirstPerfQueryIdINTEL__J(JNIEnv *__env, jclass clazz, jlong queryIdAddress) {
    glGetFirstPerfQueryIdINTELPROC glGetFirstPerfQueryIdINTEL = (glGetFirstPerfQueryIdINTELPROC)tlsGetFunction(1858);
    intptr_t queryId = (intptr_t)queryIdAddress;
    UNUSED_PARAM(clazz)
    glGetFirstPerfQueryIdINTEL(queryId);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_nglGetNextPerfQueryIdINTEL__IJ(JNIEnv *__env, jclass clazz, jint queryId, jlong nextQueryIdAddress) {
    glGetNextPerfQueryIdINTELPROC glGetNextPerfQueryIdINTEL = (glGetNextPerfQueryIdINTELPROC)tlsGetFunction(1859);
    intptr_t nextQueryId = (intptr_t)nextQueryIdAddress;
    UNUSED_PARAM(clazz)
    glGetNextPerfQueryIdINTEL(queryId, nextQueryId);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_nglGetPerfCounterInfoINTEL__IIIJIJJJJJJ(JNIEnv *__env, jclass clazz, jint queryId, jint counterId, jint counterNameLength, jlong counterNameAddress, jint counterDescLength, jlong counterDescAddress, jlong counterOffsetAddress, jlong counterDataSizeAddress, jlong counterTypeEnumAddress, jlong counterDataTypeEnumAddress, jlong rawCounterMaxValueAddress) {
    glGetPerfCounterInfoINTELPROC glGetPerfCounterInfoINTEL = (glGetPerfCounterInfoINTELPROC)tlsGetFunction(1860);
    intptr_t counterName = (intptr_t)counterNameAddress;
    intptr_t counterDesc = (intptr_t)counterDescAddress;
    intptr_t counterOffset = (intptr_t)counterOffsetAddress;
    intptr_t counterDataSize = (intptr_t)counterDataSizeAddress;
    intptr_t counterTypeEnum = (intptr_t)counterTypeEnumAddress;
    intptr_t counterDataTypeEnum = (intptr_t)counterDataTypeEnumAddress;
    intptr_t rawCounterMaxValue = (intptr_t)rawCounterMaxValueAddress;
    UNUSED_PARAM(clazz)
    glGetPerfCounterInfoINTEL(queryId, counterId, counterNameLength, counterName, counterDescLength, counterDesc, counterOffset, counterDataSize, counterTypeEnum, counterDataTypeEnum, rawCounterMaxValue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_nglGetPerfQueryDataINTEL__IIIJJ(JNIEnv *__env, jclass clazz, jint queryHandle, jint flags, jint dataSize, jlong dataAddress, jlong bytesWrittenAddress) {
    glGetPerfQueryDataINTELPROC glGetPerfQueryDataINTEL = (glGetPerfQueryDataINTELPROC)tlsGetFunction(1861);
    intptr_t data = (intptr_t)dataAddress;
    intptr_t bytesWritten = (intptr_t)bytesWrittenAddress;
    UNUSED_PARAM(clazz)
    glGetPerfQueryDataINTEL(queryHandle, flags, dataSize, data, bytesWritten);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_nglGetPerfQueryIdByNameINTEL__JJ(JNIEnv *__env, jclass clazz, jlong queryNameAddress, jlong queryIdAddress) {
    glGetPerfQueryIdByNameINTELPROC glGetPerfQueryIdByNameINTEL = (glGetPerfQueryIdByNameINTELPROC)tlsGetFunction(1862);
    intptr_t queryName = (intptr_t)queryNameAddress;
    intptr_t queryId = (intptr_t)queryIdAddress;
    UNUSED_PARAM(clazz)
    glGetPerfQueryIdByNameINTEL(queryName, queryId);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_INTELPerformanceQuery_nglGetPerfQueryInfoINTEL__IIJJJJJ(JNIEnv *__env, jclass clazz, jint queryId, jint queryNameLength, jlong queryNameAddress, jlong dataSizeAddress, jlong noCountersAddress, jlong noInstancesAddress, jlong capsMaskAddress) {
    glGetPerfQueryInfoINTELPROC glGetPerfQueryInfoINTEL = (glGetPerfQueryInfoINTELPROC)tlsGetFunction(1863);
    intptr_t queryName = (intptr_t)queryNameAddress;
    intptr_t dataSize = (intptr_t)dataSizeAddress;
    intptr_t noCounters = (intptr_t)noCountersAddress;
    intptr_t noInstances = (intptr_t)noInstancesAddress;
    intptr_t capsMask = (intptr_t)capsMaskAddress;
    UNUSED_PARAM(clazz)
    glGetPerfQueryInfoINTEL(queryId, queryNameLength, queryName, dataSize, noCounters, noInstances, capsMask);
}

EXTERN_C_EXIT
