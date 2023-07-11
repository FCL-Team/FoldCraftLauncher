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

public interface ARB_depth_texture {

	/**
	 * Accepted by the &lt;internalFormat&gt; parameter of TexImage1D, TexImage2D,
	 * CopyTexImage1D and CopyTexImage2D:
	 */
	int GL_DEPTH_COMPONENT16_ARB = 0x81A5;
	int GL_DEPTH_COMPONENT24_ARB = 0x81A6;
	int GL_DEPTH_COMPONENT32_ARB = 0x81A7;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameterfv and
	 * GetTexLevelParameteriv:
	 */
	int GL_TEXTURE_DEPTH_SIZE_ARB = 0x884A;

	/**
	 * Accepted by the &lt;pname&gt; parameter of TexParameterf, TexParameteri,
	 * TexParameterfv, TexParameteriv, GetTexParameterfv, and GetTexParameteriv:
	 */
	int GL_DEPTH_TEXTURE_MODE_ARB = 0x884B;
}
