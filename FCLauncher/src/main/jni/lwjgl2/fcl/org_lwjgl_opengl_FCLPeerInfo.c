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
#include "org_lwjgl_opengl_FCLPeerInfo.h"
#include "context.h"
#include "common_tools.h"

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_FCLPeerInfo_createHandle
  (JNIEnv *env, jclass clazz) {
	return newJavaManagedByteBuffer(env, sizeof(FCLPeerInfo));
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_FCLPeerInfo_nGetDisplay(JNIEnv *env, jclass unused, jobject handle) {
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, handle);
	return (jlong)(intptr_t)peer_info->display;
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opengl_FCLPeerInfo_nGetDrawable(JNIEnv *env, jclass unused, jobject handle) {
	FCLPeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, handle);
	return (jlong)(intptr_t)peer_info->drawable;
}
