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

#ifndef __EXTCL_H__
#define __EXTCL_H__

#include <jni.h>

#ifdef __APPLE__
    #include <OpenCL/cl_platform.h>
#else
    #include <CL/cl_platform.h>
#endif

#include "common_tools.h"

#ifdef __cplusplus
extern "C" {
#endif

#include "extgl_types.h"
#include "extcl_types.h"

// -----------------[ OpenGL-dependent typedefs ]-----------------
typedef GLsync              cl_GLsync;

// -----------------[ Callback function typedefs ]-----------------

#ifndef CL_CALLBACK
    #define CL_CALLBACK
#endif

typedef void (CL_CALLBACK * cl_create_context_callback)(const char *errinfo, const void *private_info, size_t cb, void *user_data);
typedef void (CL_CALLBACK * cl_mem_object_destructor_callback)(cl_mem memobj, void *user_data);
typedef void (CL_CALLBACK * cl_program_callback)(cl_program program, void *user_data);
typedef void (CL_CALLBACK * cl_event_callback)(cl_event event, cl_int event_command_exec_status, void *user_data);
typedef void (CL_CALLBACK * cl_native_kernel_func)(void *args);
typedef void (CL_CALLBACK * cl_printf_callback)(cl_context context, cl_uint printf_data_len, char *printf_data_ptr, void *user_data);

// -----------------[ Cross-platform functions ]-----------------

void* extcl_GetProcAddress(const char* function);
void extcl_InitializeClass(JNIEnv *env, jclass clazz, int num_functions, JavaMethodAndExtFunction *functions);
size_t extcl_CalculateImageSize(const size_t *region, size_t row_pitch, size_t slice_pitch);

// -----------------[ Platform dependent functions ]-----------------

void *extcl_NativeGetFunctionPointer(const char *function);
void extcl_LoadLibrary(JNIEnv *env, jstring path);
void extcl_UnloadLibrary();

#ifdef __cplusplus
}
#endif

#endif
