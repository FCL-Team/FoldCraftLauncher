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
#include "extal.h"

typedef ALvoid* (ALAPIENTRY *alGetProcAddressPROC)(const ALubyte* fname);
/* alGetProcAddress is commented out, since we don't use it anyway */
//static alGetProcAddressPROC alGetProcAddress = NULL;

/**
 * Initializes OpenAL by loading the library
 */
/*void InitializeOpenAL(JNIEnv *env, jstring oalPath) {
	//load our library
	if (!extal_LoadLibrary(env, oalPath)) {
		throwException(env, "Could not load openal library.");
		return;
	}
	alGetProcAddress = (alGetProcAddressPROC)extal_NativeGetFunctionPointer("alGetProcAddress");
	if (alGetProcAddress == NULL) {
		extal_UnloadLibrary();
		throwException(env, "Could not load alGetProcAddress function pointer.");
		return;
	}
}*/

/**
 * Retrieves a pointer to the named function
 *
 * @param function Name of function
 * @return pointer to named function, or NULL if not found
 */
void* extal_GetProcAddress(const char* function) {
	void *p;
/*	p = alGetProcAddress((const ALubyte*)function);
	if (p == NULL) {*/
		p = extal_NativeGetFunctionPointer(function);
		if (p == NULL) {
			printfDebug("Could not locate symbol %s\n", function);
		}
//	}
	return p;
}

void extal_InitializeClass(JNIEnv *env, jclass clazz, int num_functions, JavaMethodAndExtFunction *functions) {
	ext_InitializeClass(env, clazz, &extal_GetProcAddress, num_functions, functions);
}

