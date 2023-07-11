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
package org.lwjgl.opengl;

public interface ARB_texture_float {

	/**
	 * Accepted by the &lt;value&gt; parameter of GetTexLevelParameter:
	 */
	int GL_TEXTURE_RED_TYPE_ARB = 0x8C10;
	int GL_TEXTURE_GREEN_TYPE_ARB = 0x8C11;
	int GL_TEXTURE_BLUE_TYPE_ARB = 0x8C12;
	int GL_TEXTURE_ALPHA_TYPE_ARB = 0x8C13;
	int GL_TEXTURE_LUMINANCE_TYPE_ARB = 0x8C14;
	int GL_TEXTURE_INTENSITY_TYPE_ARB = 0x8C15;
	int GL_TEXTURE_DEPTH_TYPE_ARB = 0x8C16;

	/**
	 * Returned by the &lt;params&gt; parameter of GetTexLevelParameter:
	 */
	int GL_UNSIGNED_NORMALIZED_ARB = 0x8C17;

	/**
	 * Accepted by the &lt;internalFormat&gt; parameter of TexImage1D,
	 * TexImage2D, and TexImage3D:
	 */
	int GL_RGBA32F_ARB = 0x8814;
	int GL_RGB32F_ARB = 0x8815;
	int GL_ALPHA32F_ARB = 0x8816;
	int GL_INTENSITY32F_ARB = 0x8817;
	int GL_LUMINANCE32F_ARB = 0x8818;
	int GL_LUMINANCE_ALPHA32F_ARB = 0x8819;
	int GL_RGBA16F_ARB = 0x881A;
	int GL_RGB16F_ARB = 0x881B;
	int GL_ALPHA16F_ARB = 0x881C;
	int GL_INTENSITY16F_ARB = 0x881D;
	int GL_LUMINANCE16F_ARB = 0x881E;
	int GL_LUMINANCE_ALPHA16F_ARB = 0x881F;
}
