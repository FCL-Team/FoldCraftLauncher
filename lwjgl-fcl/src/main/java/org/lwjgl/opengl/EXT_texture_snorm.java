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

public interface EXT_texture_snorm {

	/**
	 * Accepted by the &lt;internalFormat&gt; parameter of TexImage1D,
	 * TexImage2D, and TexImage3D:
	 */
	int GL_RED_SNORM = 0x8F90;
	int GL_RG_SNORM = 0x8F91;
	int GL_RGB_SNORM = 0x8F92;
	int GL_RGBA_SNORM = 0x8F93;
	int GL_ALPHA_SNORM = 0x9010;
	int GL_LUMINANCE_SNORM = 0x9011;
	int GL_LUMINANCE_ALPHA_SNORM = 0x9012;
	int GL_INTENSITY_SNORM = 0x9013;

	int GL_R8_SNORM = 0x8F94;
	int GL_RG8_SNORM = 0x8F95;
	int GL_RGB8_SNORM = 0x8F96;
	int GL_RGBA8_SNORM = 0x8F97;
	int GL_ALPHA8_SNORM = 0x9014;
	int GL_LUMINANCE8_SNORM = 0x9015;
	int GL_LUMINANCE8_ALPHA8_SNORM = 0x9016;
	int GL_INTENSITY8_SNORM = 0x9017;

	int GL_R16_SNORM = 0x8F98;
	int GL_RG16_SNORM = 0x8F99;
	int GL_RGB16_SNORM = 0x8F9A;
	int GL_RGBA16_SNORM = 0x8F9B;
	int GL_ALPHA16_SNORM = 0x9018;
	int GL_LUMINANCE16_SNORM = 0x9019;
	int GL_LUMINANCE16_ALPHA16_SNORM = 0x901A;
	int GL_INTENSITY16_SNORM = 0x901B;

	/** Returned by GetTexLevelParmeter */
	int GL_SIGNED_NORMALIZED = 0x8F9C;

}