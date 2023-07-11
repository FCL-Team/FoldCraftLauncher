/*
 * Copyright (c) 2002-2013 LWJGL Project
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

import java.nio.Buffer;

@Dependent
public interface ARB_buffer_storage {

	/**
	 * Accepted in the &lt;flags&gt; parameter of BufferStorage and
	 * NamedBufferStorageEXT:
	 */
	int GL_MAP_PERSISTENT_BIT  = 0x0040,
		GL_MAP_COHERENT_BIT    = 0x0080,
		GL_DYNAMIC_STORAGE_BIT = 0x0100,
		GL_CLIENT_STORAGE_BIT  = 0x0200;

	/** Accepted by the &lt;pname&gt; parameter of GetBufferParameter{i|i64}v:\ */

	int GL_BUFFER_IMMUTABLE_STORAGE = 0x821F,
		GL_BUFFER_STORAGE_FLAGS     = 0x8220;

	/** Accepted by the &lt;barriers&gt; parameter of MemoryBarrier: */
	int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 0x00004000;

	@Reuse("GL44")
	void glBufferStorage(@GLenum int target,
	                     @AutoSize("data") @GLsizeiptr long size,
	                     @Const
	                     @GLbyte
	                     @GLshort
	                     @GLint
	                     @GLuint64
	                     @GLfloat
	                     @GLdouble Buffer data,
	                     @GLbitfield int flags);

	@Reuse("GL44")
	@Alternate("glBufferStorage")
	void glBufferStorage2(@GLenum int target,
	                      @GLsizeiptr long size,
	                      @Constant("0L") @Const Buffer data,
	                      @GLbitfield int flags);

	@Dependent("GL_EXT_direct_state_access")
	void glNamedBufferStorageEXT(@GLuint int buffer,
	                             @AutoSize("data") @GLsizeiptr long size,
	                             @Const
	                             @GLbyte
	                             @GLshort
	                             @GLint
	                             @GLuint64
	                             @GLfloat
	                             @GLdouble Buffer data,
	                             @GLbitfield int flags);

	@Dependent("GL_EXT_direct_state_access")
	@Alternate("glNamedBufferStorageEXT")
	void glNamedBufferStorageEXT2(@GLuint int buffer,
	                              @GLsizeiptr long size,
	                              @Constant("0L") @Const Buffer data,
	                              @GLbitfield int flags);

}