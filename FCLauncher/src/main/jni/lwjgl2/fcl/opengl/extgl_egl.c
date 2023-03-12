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
#include <dlfcn.h>
#include "extgl_egl.h"

eglGetErrorPROC lwjgl_eglGetError = NULL;
eglGetDisplayPROC lwjgl_eglGetDisplay = NULL;
eglInitializePROC lwjgl_eglInitialize = NULL;
eglTerminatePROC lwjgl_eglTerminate = NULL;
eglQueryStringPROC lwjgl_eglQueryString = NULL;
eglGetConfigsPROC lwjgl_eglGetConfigs = NULL;
eglChooseConfigPROC lwjgl_eglChooseConfig = NULL;
eglGetConfigAttribPROC lwjgl_eglGetConfigAttrib = NULL;
eglCreateWindowSurfacePROC lwjgl_eglCreateWindowSurface = NULL;
eglCreatePbufferSurfacePROC lwjgl_eglCreatePbufferSurface = NULL;
eglCreatePixmapSurfacePROC lwjgl_eglCreatePixmapSurface = NULL;
eglDestroySurfacePROC lwjgl_eglDestroySurface = NULL;
eglQuerySurfacePROC lwjgl_eglQuerySurface = NULL;
eglBindAPIPROC lwjgl_eglBindAPI = NULL;
eglQueryAPIPROC lwjgl_eglQueryAPI = NULL;
eglWaitClientPROC lwjgl_eglWaitClient = NULL;
eglReleaseThreadPROC lwjgl_eglReleaseThread = NULL;
eglCreatePbufferFromClientBufferPROC lwjgl_eglCreatePbufferFromClientBuffer = NULL;
eglSurfaceAttribPROC lwjgl_eglSurfaceAttrib = NULL;
eglBindTexImagePROC lwjgl_eglBindTexImage = NULL;
eglReleaseTexImagePROC lwjgl_eglReleaseTexImage = NULL;
eglSwapIntervalPROC lwjgl_eglSwapInterval = NULL;
eglCreateContextPROC lwjgl_eglCreateContext = NULL;
eglDestroyContextPROC lwjgl_eglDestroyContext = NULL;
eglMakeCurrentPROC lwjgl_eglMakeCurrent = NULL;
eglGetCurrentContextPROC lwjgl_eglGetCurrentContext = NULL;
eglGetCurrentSurfacePROC lwjgl_eglGetCurrentSurface = NULL;
eglGetCurrentDisplayPROC lwjgl_eglGetCurrentDisplay = NULL;
eglQueryContextPROC lwjgl_eglQueryContext = NULL;
eglWaitGLPROC lwjgl_eglWaitGL = NULL;
eglWaitNativePROC lwjgl_eglWaitNative = NULL;
eglSwapBuffersPROC lwjgl_eglSwapBuffers = NULL;
eglCopyBuffersPROC lwjgl_eglCopyBuffers = NULL;
eglGetProcAddressPROC lwjgl_eglGetProcAddress = NULL;

static void * lib_gl_handle = NULL;
static void * lib_egl_handle = NULL;

typedef void * (APIENTRY * glXGetProcAddressARBPROC) (const GLubyte *procName);

static glXGetProcAddressARBPROC lwjgl_glXGetProcAddressARB;

static void *extgl_eglSym(const char *name) {
	void *t = dlsym(lib_egl_handle, name);
	if (t == NULL) {
	        printfDebug("Could not locate symbol %s\n", name);
	}
	return t;
}

static void extgl_InitEGL14(void) {
	ExtFunction functions[] = {
		{"eglGetError", (void*)&lwjgl_eglGetError},
		{"eglGetDisplay", (void*)&lwjgl_eglGetDisplay},
		{"eglInitialize", (void*)&lwjgl_eglInitialize},
		{"eglTerminate", (void*)&lwjgl_eglTerminate},
		{"eglQueryString", (void*)&lwjgl_eglQueryString},
		{"eglGetConfigs", (void*)&lwjgl_eglGetConfigs},
		{"eglChooseConfig", (void*)&lwjgl_eglChooseConfig},
		{"eglGetConfigAttrib", (void*)&lwjgl_eglGetConfigAttrib},
		{"eglCreateWindowSurface", (void*)&lwjgl_eglCreateWindowSurface},
		{"eglCreatePbufferSurface", (void*)&lwjgl_eglCreatePbufferSurface},
		{"eglCreatePixmapSurface", (void*)&lwjgl_eglCreatePixmapSurface},
		{"eglDestroySurface", (void*)&lwjgl_eglDestroySurface},
		{"eglQuerySurface", (void*)&lwjgl_eglQuerySurface},
		{"eglBindAPI", (void*)&lwjgl_eglBindAPI},
		{"eglQueryAPI", (void*)&lwjgl_eglQueryAPI},
		{"eglWaitClient", (void*)&lwjgl_eglWaitClient},
		{"eglReleaseThread", (void*)&lwjgl_eglReleaseThread},
		{"eglCreatePbufferFromClientBuffer", (void*)&lwjgl_eglCreatePbufferFromClientBuffer},
		{"eglSurfaceAttrib", (void*)&lwjgl_eglSurfaceAttrib},
		{"eglBindTexImage", (void*)&lwjgl_eglBindTexImage},
		{"eglReleaseTexImage", (void*)&lwjgl_eglReleaseTexImage},
		{"eglSwapInterval", (void*)&lwjgl_eglSwapInterval},
		{"eglCreateContext", (void*)&lwjgl_eglCreateContext},
		{"eglDestroyContext", (void*)&lwjgl_eglDestroyContext},
		{"eglMakeCurrent", (void*)&lwjgl_eglMakeCurrent},
		{"eglGetCurrentContext", (void*)&lwjgl_eglGetCurrentContext},
		{"eglGetCurrentSurface", (void*)&lwjgl_eglGetCurrentSurface},
		{"eglGetCurrentDisplay", (void*)&lwjgl_eglGetCurrentDisplay},
		{"eglQueryContext", (void*)&lwjgl_eglQueryContext},
		{"eglWaitGL", (void*)&lwjgl_eglWaitGL},
		{"eglWaitNative", (void*)&lwjgl_eglWaitNative},
		{"eglSwapBuffers", (void*)&lwjgl_eglSwapBuffers},
		{"eglCopyBuffers", (void*)&lwjgl_eglCopyBuffers},
		{"eglGetProcAddress", (void*)&lwjgl_eglGetProcAddress}};
	ext_InitializeFunctions(&extgl_eglSym, sizeof(functions)/sizeof(ExtFunction), functions);
}

bool extgl_Open(JNIEnv *env) {
	const char* egl_name = "libEGL.so.1";
	egl_name = getenv("LIBEGL_NAME");

	const char* gl_name = "libGL.so.1";
	gl_name = getenv("LIBGL_NAME");

	if (lib_gl_handle != NULL && lib_egl_handle != NULL)
		return true;
	/*
	 * Actually we don't need the RTLD_GLOBAL flag, since the symbols
	 * we load should be private to us. However, according to the
	 * documentation at
	 *
	 * http://dri.sourceforge.net/doc/DRIuserguide.html
	 *
	 * DRI drivers need this flag to work properly
	 */
	lib_egl_handle = dlopen(egl_name, RTLD_LAZY | RTLD_LOCAL);

	if (lib_egl_handle == NULL) {
		throwFormattedException(env, "Error loading %s: %s", egl_name, dlerror());
		return false;
	}

	lib_gl_handle = dlopen(gl_name, RTLD_LAZY | RTLD_GLOBAL);

	if (lib_gl_handle == NULL) {
		throwFormattedException(env, "Error loading %s: %s", gl_name, dlerror());
		return false;
	}
	lwjgl_glXGetProcAddressARB = (glXGetProcAddressARBPROC)dlsym(lib_gl_handle, "glXGetProcAddressARB");
	if (lwjgl_glXGetProcAddressARB == NULL) {
		extgl_Close();
		throwException(env, "Could not get address of glXGetProcAddressARB");
		return false;
	}
	/* Unlike Windows, EGL function addresses are context-independent
	 * so we only have to initialize the addresses once at load
	 */
	extgl_InitEGL14();
	return true;
}

void *extgl_GetProcAddress(const char *name) {
	void *t = (void*)lwjgl_glXGetProcAddressARB((const GLubyte*)name);
	if (t == NULL) {
		t = dlsym(lib_gl_handle, name);
		if (t == NULL) {
			printfDebug("Could not locate symbol %s\n", name);
		}
	}
	return t;
}

void extgl_Close(void) {
	dlclose(lib_gl_handle);
	lib_gl_handle = NULL;
	dlclose(lib_egl_handle);
	lib_egl_handle = NULL;
}

bool extgl_InitEGL(EGLDisplay disp) {
	int major, minor;
	if (lwjgl_eglInitialize(disp, &major, &minor) != EGL_TRUE)
		return false;
	bool egl14 = major > 1 || (major == 1 && minor >= 4);
	// Check EGL 1.4 version
	if (!egl14)
		return false;
	return true;
}
