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

public interface EXT_texture_sRGB {

	/**
	 Accepted by the &lt;internalformat&gt; parameter of TexImage1D, TexImage2D,
	 TexImage3D, CopyTexImage1D, CopyTexImage2D.
	 */
	int GL_SRGB_EXT = 0x8C40;
	int GL_SRGB8_EXT = 0x8C41;
	int GL_SRGB_ALPHA_EXT = 0x8C42;
	int GL_SRGB8_ALPHA8_EXT = 0x8C43;
	int GL_SLUMINANCE_ALPHA_EXT = 0x8C44;
	int GL_SLUMINANCE8_ALPHA8_EXT = 0x8C45;
	int GL_SLUMINANCE_EXT = 0x8C46;
	int GL_SLUMINANCE8_EXT = 0x8C47;
	int GL_COMPRESSED_SRGB_EXT = 0x8C48;
	int GL_COMPRESSED_SRGB_ALPHA_EXT = 0x8C49;
	int GL_COMPRESSED_SLUMINANCE_EXT = 0x8C4A;
	int GL_COMPRESSED_SLUMINANCE_ALPHA_EXT = 0x8C4B;

	/**
	 Accepted by the &lt;internalformat&gt; parameter of TexImage2D,
	 CopyTexImage2D, and CompressedTexImage2DARB and the &lt;format&gt; parameter
	 of CompressedTexSubImage2DARB.
	 */
	int GL_COMPRESSED_SRGB_S3TC_DXT1_EXT = 0x8C4C;
	int GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT1_EXT = 0x8C4D;
	int GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT3_EXT = 0x8C4E;
	int GL_COMPRESSED_SRGB_ALPHA_S3TC_DXT5_EXT = 0x8C4F;

}
