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

import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLuint;

public interface EXT_texture_buffer_object {

	/**
	 * Accepted by the &lt;target&gt; parameter of BindBuffer, BufferData,
	 * BufferSubData, MapBuffer, BindTexture, UnmapBuffer, GetBufferSubData,
	 * GetBufferParameteriv, GetBufferPointerv, and TexBufferEXT, and
	 * the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetFloatv, and
	 * GetIntegerv:
	 */
	int GL_TEXTURE_BUFFER_EXT = 0x8C2A;

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetDoublev,
	 * GetFloatv, and GetIntegerv:
	 */
	int GL_MAX_TEXTURE_BUFFER_SIZE_EXT = 0x8C2B;
	int GL_TEXTURE_BINDING_BUFFER_EXT = 0x8C2C;
	int GL_TEXTURE_BUFFER_DATA_STORE_BINDING_EXT = 0x8C2D;
	int GL_TEXTURE_BUFFER_FORMAT_EXT = 0x8C2E;

	void glTexBufferEXT(@GLenum int target, @GLenum int internalformat, @GLuint int buffer);

}