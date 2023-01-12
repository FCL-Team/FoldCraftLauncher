/* Small parts were taken from Mesa's glext.h and gl.h, here's the license: */

/*
 * Mesa 3-D graphics library
 * Version:  6.5.1
 *
 * Copyright (C) 1999-2006  Brian Paul   All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * BRIAN PAUL BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*  Some parts derived from files copyright (c) 2001-2002 Lev Povalahev under this license: */

/* ----------------------------------------------------------------------------
Copyright (c) 2002, Lev Povalahev
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.
    * The name of the author may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
THE POSSIBILITY OF SUCH DAMAGE.
------------------------------------------------------------------------------*/
/*
    GL_draw_range_elements support added by Benjamin Karaban

    Lev Povalahev contact information:

    levp@gmx.net

    http://www.uni-karlsruhe.de/~uli2/
*/

#ifndef __EXTGL_H__
#define __EXTGL_H__

#include <jni.h>

#include <string.h>
#include <stddef.h>

#ifdef __APPLE__
    #include <OpenCL/cl_platform.h>
#else
    #include <CL/cl_platform.h>
#endif

#include "common_tools.h"

#if defined(_WIN32) || defined(_WIN64)
    #include <windows.h>	// fix APIENTRY macro redefinition
#endif

#ifndef APIENTRY
    #define APIENTRY
#endif

#include "extcl_types.h"
#include "extgl_types.h"

/* AMD_debug_output callback function pointer. */
typedef void (APIENTRY *GLDEBUGPROCAMD)(GLuint id, GLenum category, GLenum severity, GLsizei length, const GLchar* message, GLvoid* userParam);

/* ARB_debug_output callback function pointer. */
typedef void (APIENTRY *GLDEBUGPROCARB)(GLenum source, GLenum type, GLuint id, GLenum severity, GLsizei length, const GLchar* message, GLvoid* userParam);

/* KHR_debug callback function pointer. */
typedef void (APIENTRY *GLDEBUGPROC)(GLenum source, GLenum type, GLuint id, GLenum severity, GLsizei length, const GLchar* message, GLvoid* userParam);

/* initializes everything, call this right after the rc is created. the function returns true if successful */
extern bool extgl_Open(JNIEnv *env);
extern void extgl_Close(void);
extern void extgl_InitializeClass(JNIEnv *env, jclass clazz, int num_functions, JavaMethodAndExtFunction *functions);
extern bool extgl_InitializeFunctions(int num_functions, ExtFunction *functions);
extern bool extgl_QueryExtension(const GLubyte*extensions, const char *name);
extern void *extgl_GetProcAddress(const char *name);


#ifndef __APPLE__
	/* NV_present_video functions (GLX & WGL only) */
	extern jint extgl_EnumerateVideoDevicesNV(JNIEnv *env, jobject peer_info_handle, jobject devices, jint devices_position);
	extern jboolean extgl_BindVideoDeviceNV(JNIEnv *env, jobject peer_info_handle, jint video_slot, jlong video_device, jobject attrib_list, jint attrib_list_position);
	extern jboolean extgl_QueryContextNV(JNIEnv *env, jobject peer_info_handle, jobject context_handle, jint attrib, jobject value, jint value_position);

	/* NV_video_capture functions (GLX & WGL only) */
	extern jboolean extgl_BindVideoCaptureDeviceNV(JNIEnv *env, jobject peer_info_handle, jint video_slot, jlong device);
	extern jint extgl_EnumerateVideoCaptureDevicesNV(JNIEnv *env, jobject peer_info_handle, jobject devices, jint devices_position);
	extern jboolean extgl_LockVideoCaptureDeviceNV(JNIEnv *env, jobject peer_info_handle, jlong device);
	extern jboolean extgl_QueryVideoCaptureDeviceNV(JNIEnv *env, jobject peer_info_handle, jlong device, jint attribute, jobject value, jint value_position);
	extern jboolean extgl_ReleaseVideoCaptureDeviceNV(JNIEnv *env, jobject peer_info_handle, jlong device);
#endif

#endif /* __EXTGL_H__ */
