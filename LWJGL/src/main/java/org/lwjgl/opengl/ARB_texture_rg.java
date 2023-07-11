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

public interface ARB_texture_rg {

	/**
	 * Accepted by the &lt;internalFormat&gt; parameter of TexImage1D, TexImage2D,
	 * TexImage3D, CopyTexImage1D, and CopyTexImage2D:
	 */
	int GL_R8 = 0x8229;
	int GL_R16 = 0x822A;

	int GL_RG8 = 0x822B;
	int GL_RG16 = 0x822C;

	int GL_R16F = 0x822D;
	int GL_R32F = 0x822E;

	int GL_RG16F = 0x822F;
	int GL_RG32F = 0x8230;

	int GL_R8I = 0x8231;
	int GL_R8UI = 0x8232;
	int GL_R16I = 0x8233;
	int GL_R16UI = 0x8234;
	int GL_R32I = 0x8235;
	int GL_R32UI = 0x8236;

	int GL_RG8I = 0x8237;
	int GL_RG8UI = 0x8238;
	int GL_RG16I = 0x8239;
	int GL_RG16UI = 0x823A;
	int GL_RG32I = 0x823B;
	int GL_RG32UI = 0x823C;

	/**
	 * Accepted by the &lt;format&gt; parameter of TexImage3D, TexImage2D,
	 * TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D,
	 * DrawPixels and ReadPixels:
	 */
	int GL_RG = 0x8227;
	int GL_RG_INTEGER = 0x8228;

}