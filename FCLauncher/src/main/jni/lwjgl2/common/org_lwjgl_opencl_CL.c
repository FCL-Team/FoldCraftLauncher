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

#include <jni.h>
#include "org_lwjgl_opencl_CL.h"
#include "extcl.h"

JNIEXPORT void JNICALL Java_org_lwjgl_opencl_CL_nCreate(JNIEnv *env, jclass clazz, jstring oclPath) {
	extcl_LoadLibrary(env, oclPath);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opencl_CL_nDestroy(JNIEnv *env, jclass clazz) {
	extcl_UnloadLibrary();
}

JNIEXPORT jlong JNICALL Java_org_lwjgl_opencl_CL_ngetFunctionAddress(JNIEnv *env, jclass clazz, jlong function_name) {
	return (jlong)(intptr_t)extcl_GetProcAddress((char *)(intptr_t)function_name);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opencl_CL_getHostBuffer(JNIEnv *env, jclass clazz, jlong address, jint size) {
    return safeNewBuffer(env, (void *)(intptr_t)address, size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opencl_CL_resetNativeStubs(JNIEnv *env, jclass clazz, jclass cl_class) {
	(*env)->UnregisterNatives(env, cl_class);
}
