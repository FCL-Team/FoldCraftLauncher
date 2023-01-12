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
 * $Id: org_lwjgl_openal_ALC.c 2279 2006-02-23 19:22:00Z elias_naur $
 *
 * This is the actual JNI implementation of the OpenAL context/device library.
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 2279 $
 */

/* OpenAL includes */
#include "extal.h"

//alc
typedef ALCdevice *    (ALCAPIENTRY *alcCaptureOpenDevicePROC)( const ALCchar *devicename, ALCuint frequency, ALCenum format, ALCsizei buffersize );
typedef ALCboolean     (ALCAPIENTRY *alcCaptureCloseDevicePROC)( ALCdevice *device );
typedef void           (ALCAPIENTRY *alcCaptureStartPROC)( ALCdevice *device );
typedef void           (ALCAPIENTRY *alcCaptureStopPROC)( ALCdevice *device );
typedef void           (ALCAPIENTRY *alcCaptureSamplesPROC)( ALCdevice *device, ALCvoid *buffer, ALCsizei samples );

static alcCaptureOpenDevicePROC alcCaptureOpenDevice;
static alcCaptureCloseDevicePROC alcCaptureCloseDevice;
static alcCaptureStartPROC alcCaptureStart;
static alcCaptureStopPROC alcCaptureStop;
static alcCaptureSamplesPROC alcCaptureSamples;

/*
 * Class:     org_lwjgl_openal_ALC11
 * Method:    nalcCaptureOpenDevice
 * Signature: (Ljava/lang/String;III)J
 */
static jlong JNICALL Java_org_lwjgl_openal_ALC11_nalcCaptureOpenDevice(JNIEnv *env, jclass clazz, jlong devicename, jint frequency, jint format, jint buffersize) {
	return (jlong)(intptr_t)alcCaptureOpenDevice((const char *)(intptr_t)devicename, (unsigned int) frequency, format, buffersize);
}

/*
 * Class:     org_lwjgl_openal_ALC11
 * Method:    nalcCaptureCloseDevice
 * Signature: (J)Z
 */
static jboolean JNICALL Java_org_lwjgl_openal_ALC11_nalcCaptureCloseDevice(JNIEnv *env, jclass clazz, jlong device) {
	return (jboolean) alcCaptureCloseDevice((ALCdevice*) ((intptr_t)device));
}

/*
 * Class:     org_lwjgl_openal_ALC11
 * Method:    nalcCaptureStart
 * Signature: (J)V
 */
static void JNICALL Java_org_lwjgl_openal_ALC11_nalcCaptureStart(JNIEnv *env, jclass clazz, jlong device) {
	alcCaptureStart((ALCdevice*) ((intptr_t)device));
}

/*
 * Class:     org_lwjgl_openal_ALC11
 * Method:    nalcCaptureStop
 * Signature: (J)V
 */
static void JNICALL Java_org_lwjgl_openal_ALC11_nalcCaptureStop(JNIEnv * env, jclass clazz, jlong device) {
	alcCaptureStop((ALCdevice*) ((intptr_t)device));
}

/*
 * Class:     org_lwjgl_openal_ALC11
 * Method:    nalcCaptureSamples
 * Signature: (JLjava/nio/ByteBuffer;I)V
 */
static void JNICALL Java_org_lwjgl_openal_ALC11_nalcCaptureSamples(JNIEnv *env, jclass clazz, jlong device, jlong buffer, jint samples) {
	ALvoid *buffer_address = (ALbyte *)(intptr_t)buffer;
	alcCaptureSamples((ALCdevice*) ((intptr_t)device), buffer_address, samples);
}

/**
 * Loads the context OpenAL functions
 *
 * @return true if all methods were loaded, false if one of the methods could not be loaded
 */
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL Java_org_lwjgl_openal_ALC11_initNativeStubs(JNIEnv *env, jclass clazz) {
	JavaMethodAndExtFunction functions[] = {
		{"nalcCaptureOpenDevice", "(JIII)J", (void*)&Java_org_lwjgl_openal_ALC11_nalcCaptureOpenDevice, "alcCaptureOpenDevice", (void*)&alcCaptureOpenDevice, false},
		{"nalcCaptureCloseDevice", "(J)Z", (void*)&Java_org_lwjgl_openal_ALC11_nalcCaptureCloseDevice, "alcCaptureCloseDevice", (void*)&alcCaptureCloseDevice, false},
		{"nalcCaptureStart", "(J)V", (void*)&Java_org_lwjgl_openal_ALC11_nalcCaptureStart, "alcCaptureStart", (void*)&alcCaptureStart, false},
		{"nalcCaptureStop", "(J)V", (void*)&Java_org_lwjgl_openal_ALC11_nalcCaptureStop, "alcCaptureStop", (void*)&alcCaptureStop, false},
		{"nalcCaptureSamples", "(JJI)V", (void*)&Java_org_lwjgl_openal_ALC11_nalcCaptureSamples, "alcCaptureSamples", (void*)&alcCaptureSamples, false}
	};
	int num_functions = NUMFUNCTIONS(functions);
	extal_InitializeClass(env, clazz, num_functions, functions);
}
#ifdef __cplusplus
}
#endif
