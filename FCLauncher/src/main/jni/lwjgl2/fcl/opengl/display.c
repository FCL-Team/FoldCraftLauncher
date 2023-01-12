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
 * FCL specific library for display handling.
 *
 * @author Tungsten
 * @version $Revision$
 */

#include <fcl_bridge.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "common_tools.h"
#include "org_lwjgl_opengl_FCLDisplay.h"

typedef struct {
	int width;
	int height;
	int freq;
} mode_info;

static mode_info *getDisplayModes(int *num_modes) {
	mode_info *avail_modes = (mode_info *)malloc(sizeof(mode_info));
	if (avail_modes == NULL) {
		return NULL;
	}
	ANativeWindow* window = fclGetNativeWindow();
	avail_modes[0].width = ANativeWindow_getWidth(window);
	avail_modes[0].height = ANativeWindow_getHeight(window);
	avail_modes[0].freq = 0; // No frequency support in FCL
	*num_modes = 1;
	return avail_modes;
}

static jobjectArray getAvailableDisplayModes(JNIEnv * env) {
	int num_modes, i;
	mode_info *avail_modes;
	int bpp = 32;
	avail_modes = getDisplayModes(&num_modes);
	if (avail_modes == NULL) {
		printfDebugJava(env, "Could not get display modes");
		return NULL;
	}
	// Allocate an array of DisplayModes big enough
	jclass displayModeClass = (*env)->FindClass(env, "org/lwjgl/opengl/DisplayMode");
	jobjectArray ret = (*env)->NewObjectArray(env, num_modes, displayModeClass, NULL);
	jmethodID displayModeConstructor = (*env)->GetMethodID(env, displayModeClass, "<init>", "(IIII)V");

	for (i = 0; i < num_modes; i++) {
		jobject displayMode = (*env)->NewObject(env, displayModeClass, displayModeConstructor, avail_modes[i].width, avail_modes[i].height, bpp, avail_modes[i].freq);
		(*env)->SetObjectArrayElement(env, ret, i, displayMode);
	}
	free(avail_modes);
	return ret;
}

JNIEXPORT jobjectArray JNICALL Java_org_lwjgl_opengl_FCLDisplay_nGetAvailableDisplayModes(JNIEnv *env, jclass clazz) {
	return getAvailableDisplayModes(env);
}
