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
 * $Id$
 *
 * @author Tungsten
 * @version $Revision$
 */

#include <jni.h>
#include "org_lwjgl_opengl_FCLContextImplementation.h"
#include "extgl_egl.h"
#include "context.h"
#include "common_tools.h"

typedef struct {
	EGLContext context;
} FCLContext;

static bool checkContext(JNIEnv *env, EGLDisplay display, EGLContext context) {
	if (context == NULL) {
		throwException(env, "Could not create EGL context");
		return false;
	}
	return true;
}

static void createContextEGL(JNIEnv *env, FCLPeerInfo *peer_info, FCLContext *context_info, jobject attribs, EGLContext shared_context) {
	EGLConfig *config = getFBConfigFromPeerInfo(env, peer_info);
	if (config == NULL)
		return;
//	if (!lwjgl_eglBindAPI(EGL_OPENGL_API)) {
//		throwException(env, "Could not bind OpenGL API");
//		return;
//	}
	EGLContext context;
	const EGLint egl_context_attributes[] = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL_NONE };
    context = lwjgl_eglCreateContext(peer_info->display, *config, shared_context, egl_context_attributes);
	if (!checkContext(env, peer_info->display, context))
		return;
	context_info->context = context;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_getEGLContext(JNIEnv *env, jclass clazz, jobject context_handle) {
   FCLContext *context_info = (*env)->GetDirectBufferAddress(env, context_handle);
    return (intptr_t)context_info->context;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_getDisplay(JNIEnv *env, jclass clazz, jobject peer_info_handle) {
    FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_info_handle);
    return (intptr_t)peer_info->display;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_nSetSwapInterval(JNIEnv *env, jclass clazz, jobject peer_info_handle, jobject context_handle, jint value) {
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_info_handle);
	FCLContext *context_info = (*env)->GetDirectBufferAddress(env, context_handle);

	lwjgl_eglSwapInterval(peer_info->display, value);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_nCreate
  (JNIEnv *env , jclass clazz, jobject peer_handle, jobject attribs, jobject shared_context_handle) {
	jobject context_handle = newJavaManagedByteBuffer(env, sizeof(FCLContext));
	if (context_handle == NULL) {
		throwException(env, "Could not allocate handle buffer");
		return NULL;
	}
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_handle);
	FCLContext *context_info = (*env)->GetDirectBufferAddress(env, context_handle);
	if (!extgl_InitEGL(peer_info->display)) {
		throwException(env, "Could not initialize EGL");
		return NULL;
	}
	EGLContext shared_context = NULL;
	if (shared_context_handle != NULL) {
		FCLContext *shared_context_info = (*env)->GetDirectBufferAddress(env, shared_context_handle);
		shared_context = shared_context_info->context;
	}
	createContextEGL(env, peer_info, context_info, attribs, shared_context);
	return context_handle;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_nDestroy
  (JNIEnv *env, jclass clazz, jobject peer_handle, jobject context_handle) {
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_handle);
	FCLContext *context_info = (*env)->GetDirectBufferAddress(env, context_handle);
	lwjgl_eglDestroyContext(peer_info->display, context_info->context);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_nReleaseCurrentContext
  (JNIEnv *env , jclass clazz, jobject peer_info_handle) {
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_info_handle);
	bool result;
	result = lwjgl_eglMakeCurrent(peer_info->display, EGL_NO_SURFACE, EGL_NO_SURFACE, NULL);
	if (!result)
		throwException(env, "Could not release current context");
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_nMakeCurrent
  (JNIEnv *env, jclass clazz, jobject peer_info_handle, jobject context_handle) {
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_info_handle);
	FCLContext *context_info = (*env)->GetDirectBufferAddress(env, context_handle);
	bool result;
	result = lwjgl_eglMakeCurrent(peer_info->display, peer_info->drawable, peer_info->drawable, context_info->context);
	if (!result)
		throwException(env, "Could not make context current");
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_nIsCurrent
  (JNIEnv *env, jclass clazz, jobject context_handle) {
	FCLContext *context_info = (*env)->GetDirectBufferAddress(env, context_handle);
	return context_info->context == lwjgl_eglGetCurrentContext();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_FCLContextImplementation_nSwapBuffers
  (JNIEnv *env, jclass clazz, jobject peer_info_handle) {
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_info_handle);
	lwjgl_eglSwapBuffers(peer_info->display, peer_info->drawable);
}
