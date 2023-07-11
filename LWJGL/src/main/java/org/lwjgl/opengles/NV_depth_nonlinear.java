/*
 * Copyright (c) 2002-2011 LWJGL Project
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
package org.lwjgl.opengles;

public interface NV_depth_nonlinear {

	/**
	 * Accepted as a valid sized internal format by all functions accepting
	 * sized internal formats with a base format of DEPTH_COMPONENT:
	 */
	int GL_DEPTH_COMPONENT16_NONLINEAR_NV = 0x8553;

	/**
	 * Accepted by the &lt;attrib_list&gt; parameter of eglChooseConfig,
	 * and by the &lt;attribute&gt; parameter of eglGetConfigAttrib:
	 */
	int EGL_DEPTH_ENCODING_NV = 0x30E2;

	/**
	 * Accepted as a value in the &lt;attrib_list&gt; parameter of eglChooseConfig
	 * and eglCreatePbufferSurface, and returned in the &lt;value&gt; parameter
	 * of eglGetConfigAttrib:
	 */
	int EGL_DEPTH_ENCODING_NONE_NV      = 0,
		EGL_DEPTH_ENCODING_NONLINEAR_NV = 0x30E3;

}