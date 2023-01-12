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
 * Include file to access public window features
 *
 * @author Tungsten
 * @version $Revision$
 */

#ifndef _LWJGL_CONTEXT_H_INCLUDED_
#define _LWJGL_CONTEXT_H_INCLUDED_

#include <jni.h>
#include <fcl_bridge.h>
#include "extgl_egl.h"

typedef struct {
	EGLDisplay display;
	EGLSurface drawable;
	EGLint config_id;
} FCLPeerInfo;

/* EGL 1.4 chooser */
extern EGLConfig *chooseVisualEGL(JNIEnv *env, EGLDisplay disp, jobject pixel_format, bool use_display_bpp, int drawable_type);

extern EGLConfig *getFBConfigFromPeerInfo(JNIEnv *env, FCLPeerInfo *peer_info);

extern bool initPeerInfo(JNIEnv *env, jobject peer_info_handle, EGLDisplay display, jobject pixel_format, bool use_display_bpp, int drawable_type);

#endif /* _LWJGL_CONTEXT_H_INCLUDED_ */
