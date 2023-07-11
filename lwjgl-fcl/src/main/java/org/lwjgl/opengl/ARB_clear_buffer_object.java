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

@Dependent
public interface ARB_clear_buffer_object {

	@Reuse("GL43")
	void glClearBufferData(@GLenum int target,
	                       @GLenum int internalformat,
	                       @GLenum int format,
	                       @GLenum int type,
	                       @Check("1") @Const @GLvoid ByteBuffer data);

	@Reuse("GL43")
	void glClearBufferSubData(@GLenum int target,
	                          @GLenum int internalformat,
	                          @GLintptr long offset,
	                          @GLsizeiptr long size,
	                          @GLenum int format,
	                          @GLenum int type,
	                          @Check("1") @Const @GLvoid ByteBuffer data);

	@Dependent("GL_EXT_direct_state_access")
	void glClearNamedBufferDataEXT(@GLuint int buffer,
	                               @GLenum int internalformat,
	                               @GLenum int format,
	                               @GLenum int type,
	                               @Check("1") @Const @GLvoid ByteBuffer data);

	@Dependent("GL_EXT_direct_state_access")
	void glClearNamedBufferSubDataEXT(@GLuint int buffer,
	                                  @GLenum int internalformat,
	                                  @GLintptr long offset,
	                                  @GLsizeiptr long size,
	                                  @GLenum int format,
	                                  @GLenum int type,
	                                  @Check("1") @GLvoid ByteBuffer data);

}