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
#include "extcl.h"

typedef CL_API_ENTRY void * (CL_API_CALL *clGetExtensionFunctionAddressPROC) (const char * func_name);
static clGetExtensionFunctionAddressPROC clGetExtensionFunctionAddress;

/**
 * Retrieves a pointer to the named function
 *
 * @param function Name of function
 * @return pointer to named function, or NULL if not found
 */
void* extcl_GetProcAddress(const char * function) {
    void *p = NULL;

    if ( clGetExtensionFunctionAddress == NULL )
        clGetExtensionFunctionAddress = extcl_NativeGetFunctionPointer("clGetExtensionFunctionAddress");

    p = clGetExtensionFunctionAddress(function);
    if ( p == NULL )
	    p = extcl_NativeGetFunctionPointer(function);

	return p;
}

void extcl_InitializeClass(JNIEnv *env, jclass clazz, int num_functions, JavaMethodAndExtFunction *functions) {
	ext_InitializeClass(env, clazz, &extcl_GetProcAddress, num_functions, functions);
}

size_t extcl_CalculateImageSize(const size_t *region, size_t row_pitch, size_t slice_pitch) {
    if ( slice_pitch == 0 )
        return region[1] * row_pitch;
    else
        return region[2] * slice_pitch;
}
