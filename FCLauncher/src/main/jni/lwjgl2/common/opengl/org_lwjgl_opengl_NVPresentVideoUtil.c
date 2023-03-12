/*
 * Copyright (c) 2002-2011 LWJGL Project
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
 * JNI implementation of the NVPresentVideoUtil class (GLX & WGL only).
 *
 * @author Spasi
 */

#include <jni.h>
#include "common_tools.h"
#include "extgl.h"
#include "org_lwjgl_opengl_NVPresentVideoUtil.h"

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_NVPresentVideoUtil_nglEnumerateVideoDevicesNV(
	JNIEnv *env, jclass clazz, jobject peer_info, jobject devices, jint devices_position
) {
	#ifdef __APPLE__
		return 0;
	#else
		return extgl_EnumerateVideoDevicesNV(env, peer_info, devices, devices_position);
	#endif
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVPresentVideoUtil_nglBindVideoDeviceNV(
	JNIEnv *env, jclass clazz, jobject peer_info, jint video_slot, jlong video_device, jobject attrib_list, jint attrib_list_position
) {
	#ifdef __APPLE__
		return false;
	#else
		return extgl_BindVideoDeviceNV(env, peer_info, video_slot, video_device, attrib_list, attrib_list_position);
	#endif
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_NVPresentVideoUtil_nglQueryContextNV(JNIEnv *env, jclass clazz, jobject peer_info, jobject context_handle, jint attrib, jobject value, jint value_position) {
	#ifdef __APPLE__
		return false;
	#else
		return extgl_QueryContextNV(env, peer_info, context_handle, attrib, value, value_position);
	#endif
}
