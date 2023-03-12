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
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @author kappaOne <one.kappa@gmail.com>
 * @version $Revision$
 */

#include <jni.h>
#ifdef __MACH__
#include <JavaVM/jawt_md.h>
#else
#ifndef PLATFORM_FCL
#include <jawt_md.h>
#endif
#endif
#include "org_lwjgl_opengl_AWTSurfaceLock.h"
#include "awt_tools.h"
#include "common_tools.h"

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_AWTSurfaceLock_createHandle
  (JNIEnv *env, jclass clazz) {
	return newJavaManagedByteBuffer(env, sizeof(AWTSurfaceLock));
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_AWTSurfaceLock_lockAndInitHandle
  (JNIEnv *env, jclass clazz, jobject lock_buffer_handle, jobject canvas) {
#ifndef PLATFORM_FCL
	JAWT awt;
	JAWT_DrawingSurface* ds;
	JAWT_DrawingSurfaceInfo *dsi;
	AWTSurfaceLock *awt_lock = (AWTSurfaceLock *)(*env)->GetDirectBufferAddress(env, lock_buffer_handle);
    
	jboolean result = JNI_FALSE;
	
	#ifdef __MACH__
	// try get JAWT with JAWT_MACOSX_USE_CALAYER Opt In
	awt.version = JAWT_VERSION_1_4 | 0x80000000;//JAWT_MACOSX_USE_CALAYER;
	result = JAWT_GetAWT(env, &awt);
	#endif
	
	if (result == JNI_FALSE) {
		// now try without CALAYER
		awt.version = JAWT_VERSION_1_4;
		if (JAWT_GetAWT(env, &awt) == JNI_FALSE) {
			throwException(env, "Could not get the JAWT interface");
			return JNI_FALSE;
		}
	}

	ds = awt.GetDrawingSurface(env, canvas);
	if (ds == NULL) {
		throwException(env, "Could not get the drawing surface");
		return JNI_FALSE;
	}

	if((ds->Lock(ds) & JAWT_LOCK_ERROR) != 0) {
		awt.FreeDrawingSurface(ds);
		throwException(env, "Could not lock the drawing surface");
		return JNI_FALSE;
	}

	dsi = ds->GetDrawingSurfaceInfo(ds);
	if (dsi != NULL) {
		awt_lock->awt = awt;
		awt_lock->ds = ds;
		awt_lock->dsi = dsi;
		return JNI_TRUE;
  	}
	ds->Unlock(ds);
	awt.FreeDrawingSurface(ds);
	return JNI_FALSE;
#else // PLATFORM_FCL
	return JNI_TRUE;
#endif
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AWTSurfaceLock_nUnlock
  (JNIEnv *env, jclass clazz, jobject lock_buffer_handle) {
#ifndef PLATFORM_FCL
	AWTSurfaceLock *awt_lock = (AWTSurfaceLock *)(*env)->GetDirectBufferAddress(env, lock_buffer_handle);
	awt_lock->ds->FreeDrawingSurfaceInfo(awt_lock->dsi);
	awt_lock->ds->Unlock(awt_lock->ds);
	awt_lock->awt.FreeDrawingSurface(awt_lock->ds);
#endif
}

