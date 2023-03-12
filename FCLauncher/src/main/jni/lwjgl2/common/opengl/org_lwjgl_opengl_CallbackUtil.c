/*
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * JNI implementation of the ARB/AMD_debug_output & KHR_debug function callbacks.
 *
 * @author Spasi
 */

#include <jni.h>
#include "common_tools.h"
#include "extgl.h"
#include "org_lwjgl_opengl_CallbackUtil.h"

static jmethodID debugOutputCallbackARBJ;
static jmethodID debugOutputCallbackAMDJ;
static jmethodID debugCallbackKHRJ;

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_CallbackUtil_ncreateGlobalRef(JNIEnv *env, jclass clazz, jobject obj) {
    return (jlong)(intptr_t)(*env)->NewGlobalRef(env, obj);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_CallbackUtil_deleteGlobalRef(JNIEnv *env, jclass clazz, jlong globalRef) {
    (*env)->DeleteGlobalRef(env, (jobject)(intptr_t)globalRef);
}

// ----------------- [ ARB_debug_output ] -----------------

static void APIENTRY debugOutputCallbackARB(GLenum source, GLenum type, GLuint id, GLenum severity, GLsizei length, const GLchar* message, GLvoid* userParam) {
    JNIEnv *env = attachCurrentThread();

	if ( env != NULL && !(*env)->ExceptionOccurred(env) && debugOutputCallbackARBJ != NULL ) {
        (*env)->CallVoidMethod(env, (jobject)userParam, debugOutputCallbackARBJ,
            (jint)source,
            (jint)type,
            (jint)id,
            (jint)severity,
            NewStringNativeWithLength(env, message, length)
        );
    }

    detachCurrentThread();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_CallbackUtil_getDebugOutputCallbackARB(JNIEnv *env, jclass clazz) {
    // Cache the callback methodID
    jclass callbackClass;
    if ( debugOutputCallbackARBJ == NULL ) {
        callbackClass = (*env)->FindClass(env, "org/lwjgl/opengl/ARBDebugOutputCallback$Handler");
        if ( callbackClass != NULL )
            debugOutputCallbackARBJ = (*env)->GetMethodID(env, callbackClass, "handleMessage", "(IIIILjava/lang/String;)V");
    }

    return (jlong)(intptr_t)&debugOutputCallbackARB;
}

// ----------------- [ AMD_debug_output ] -----------------

static void APIENTRY debugOutputCallbackAMD(GLuint id, GLenum category, GLenum severity, GLsizei length, const GLchar* message, GLvoid* userParam) {
    JNIEnv *env = attachCurrentThread();

	if ( env != NULL && !(*env)->ExceptionOccurred(env) && debugOutputCallbackAMDJ != NULL ) {
        (*env)->CallVoidMethod(env, (jobject)userParam, debugOutputCallbackAMDJ,
            (jint)id,
            (jint)category,
            (jint)severity,
            NewStringNativeWithLength(env, message, length)
        );
    }

    detachCurrentThread();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_CallbackUtil_getDebugOutputCallbackAMD(JNIEnv *env, jclass clazz) {
    // Cache the callback methodID
    jclass callbackClass;
    if ( debugOutputCallbackAMDJ == NULL ) {
        callbackClass = (*env)->FindClass(env, "org/lwjgl/opengl/AMDDebugOutputCallback$Handler");
        if ( callbackClass != NULL )
            debugOutputCallbackAMDJ = (*env)->GetMethodID(env, callbackClass, "handleMessage", "(IIILjava/lang/String;)V");
    }

    return (jlong)(intptr_t)&debugOutputCallbackAMD;
}

// ----------------- [ KHR_debug ] -----------------

static void APIENTRY debugCallbackKHR(GLenum source, GLenum type, GLuint id, GLenum severity, GLsizei length, const GLchar* message, GLvoid* userParam) {
    JNIEnv *env = attachCurrentThread();

	if ( env != NULL && !(*env)->ExceptionOccurred(env) && debugCallbackKHRJ != NULL ) {
        (*env)->CallVoidMethod(env, (jobject)userParam, debugCallbackKHRJ,
            (jint)source,
            (jint)type,
            (jint)id,
            (jint)severity,
            NewStringNativeWithLength(env, message, length)
        );
    }

    detachCurrentThread();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_CallbackUtil_getDebugCallbackKHR(JNIEnv *env, jclass clazz) {
    // Cache the callback methodID
    jclass callbackClass;
    if ( debugCallbackKHRJ == NULL ) {
        callbackClass = (*env)->FindClass(env, "org/lwjgl/opengl/KHRDebugCallback$Handler");
        if ( callbackClass != NULL )
            debugCallbackKHRJ = (*env)->GetMethodID(env, callbackClass, "handleMessage", "(IIIILjava/lang/String;)V");
    }

    return (jlong)(intptr_t)&debugCallbackKHR;
}