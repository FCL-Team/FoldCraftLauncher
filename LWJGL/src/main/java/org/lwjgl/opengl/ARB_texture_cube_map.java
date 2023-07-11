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

public interface ARB_texture_cube_map {
	int GL_NORMAL_MAP_ARB = 0x8511;
	int GL_REFLECTION_MAP_ARB = 0x8512;
	int GL_TEXTURE_CUBE_MAP_ARB = 0x8513;
	int GL_TEXTURE_BINDING_CUBE_MAP_ARB = 0x8514;
	int GL_TEXTURE_CUBE_MAP_POSITIVE_X_ARB = 0x8515;
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_X_ARB = 0x8516;
	int GL_TEXTURE_CUBE_MAP_POSITIVE_Y_ARB = 0x8517;
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y_ARB = 0x8518;
	int GL_TEXTURE_CUBE_MAP_POSITIVE_Z_ARB = 0x8519;
	int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z_ARB = 0x851A;
	int GL_PROXY_TEXTURE_CUBE_MAP_ARB = 0x851B;
	int GL_MAX_CUBE_MAP_TEXTURE_SIZE_ARB = 0x851C;
}
