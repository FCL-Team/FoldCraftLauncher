/*
 * Copyright (c) 2002-2012 LWJGL Project
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

public interface KHR_texture_compression_astc_ldr {

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of CompressedTexImage2D,
	 * CompressedTexSubImage2D, TexStorage2D, TextureStorage2D, TexStorage3D,
	 * and TextureStorage3D:
	 */
	int GL_COMPRESSED_RGBA_ASTC_4x4_KHR           = 0x93B0,
		GL_COMPRESSED_RGBA_ASTC_5x4_KHR           = 0x93B1,
		GL_COMPRESSED_RGBA_ASTC_5x5_KHR           = 0x93B2,
		GL_COMPRESSED_RGBA_ASTC_6x5_KHR           = 0x93B3,
		GL_COMPRESSED_RGBA_ASTC_6x6_KHR           = 0x93B4,
		GL_COMPRESSED_RGBA_ASTC_8x5_KHR           = 0x93B5,
		GL_COMPRESSED_RGBA_ASTC_8x6_KHR           = 0x93B6,
		GL_COMPRESSED_RGBA_ASTC_8x8_KHR           = 0x93B7,
		GL_COMPRESSED_RGBA_ASTC_10x5_KHR          = 0x93B8,
		GL_COMPRESSED_RGBA_ASTC_10x6_KHR          = 0x93B9,
		GL_COMPRESSED_RGBA_ASTC_10x8_KHR          = 0x93BA,
		GL_COMPRESSED_RGBA_ASTC_10x10_KHR         = 0x93BB,
		GL_COMPRESSED_RGBA_ASTC_12x10_KHR         = 0x93BC,
		GL_COMPRESSED_RGBA_ASTC_12x12_KHR         = 0x93BD,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_4x4_KHR   = 0x93D0,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x4_KHR   = 0x93D1,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x5_KHR   = 0x93D2,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x5_KHR   = 0x93D3,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x6_KHR   = 0x93D4,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x5_KHR   = 0x93D5,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x6_KHR   = 0x93D6,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x8_KHR   = 0x93D7,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x5_KHR  = 0x93D8,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x6_KHR  = 0x93D9,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x8_KHR  = 0x93DA,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x10_KHR = 0x93DB,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x10_KHR = 0x93DC,
		GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x12_KHR = 0x93DD;

}