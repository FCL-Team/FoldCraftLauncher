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
 * EGL extension stubs.
 *
 * @author Tungsten
 */
#include "EGL.h"

/* NV_present_video functions */

jint extgl_EnumerateVideoDevicesNV(JNIEnv *env, jobject peer_info_handle, jobject devices, jint devices_position) {
	return 0;
}

jboolean extgl_BindVideoDeviceNV(JNIEnv *env, jobject peer_info_handle, jint video_slot, jlong video_device, jobject attrib_list, jint attrib_list_position) {
	return false;
}

jboolean extgl_QueryContextNV(JNIEnv *env, jobject peer_info_handle, jobject context_handle, jint attrib, jobject value, jint value_position) {
	return false;
}

/* NV_video_capture functions */

jboolean extgl_BindVideoCaptureDeviceNV(JNIEnv *env, jobject peer_info_handle, jint video_slot, jlong device) {
	return false;
}

jint extgl_EnumerateVideoCaptureDevicesNV(JNIEnv *env, jobject peer_info_handle, jobject devices, jint devices_position) {
	return 0;
}

jboolean extgl_LockVideoCaptureDeviceNV(JNIEnv *env, jobject peer_info_handle, jlong device) {
	return false;
}

jboolean extgl_QueryVideoCaptureDeviceNV(JNIEnv *env, jobject peer_info_handle, jlong device, jint attribute, jobject value, jint value_position) {
	return false;
}

jboolean extgl_ReleaseVideoCaptureDeviceNV(JNIEnv *env, jobject peer_info_handle, jlong device) {
	return false;
}
