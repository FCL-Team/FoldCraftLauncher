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
package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface INTEL_map_texture {

	/** Accepted by the &lt;pname&gt; parameter of TexParameteri, for target TEXTURE_2D */
	int GL_TEXTURE_MEMORY_LAYOUT_INTEL = 0x83FF;

	/**
	 * Accepted by the &lt;params&gt; when &lt;pname&gt; is set to
	 * &lt;TEXTURE_MEMORY_LAYOUT_INTEL&gt;:
	 */
	int GL_LAYOUT_DEFAULT_INTEL           = 0,
		GL_LAYOUT_LINEAR_INTEL            = 1,
		GL_LAYOUT_LINEAR_CPU_CACHED_INTEL = 2;

	/**
	 * The length parameter does not exist in the native API. It used by LWJGL to return a ByteBuffer
	 * with a proper capacity.
	 */
	@CachedResult(isRange = true)
	@GLvoid
	@AutoSize("length")
	ByteBuffer glMapTexture2DINTEL(@GLuint int texture, int level, @Helper(passToNative = true) @GLsizeiptr long length, @GLbitfield int access,
	                               @Check("1") @OutParameter IntBuffer stride, @Check("1") @OutParameter @GLenum IntBuffer layout);

	void glUnmapTexture2DINTEL(@GLuint int texture, int level);

	void glSyncTextureINTEL(@GLuint int texture);

}
