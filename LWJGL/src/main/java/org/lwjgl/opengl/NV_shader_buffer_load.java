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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.opengl.*;

import java.nio.LongBuffer;

public interface NV_shader_buffer_load {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBufferParameterui64vNV,
	 * GetNamedBufferParameterui64vNV:
	 */
	int GL_BUFFER_GPU_ADDRESS_NV = 0x8F1D;

	/** Returned by the &lt;type&gt; parameter of GetActiveUniform: */
	int GL_GPU_ADDRESS_NV = 0x8F34;

	/** Accepted by the &lt;value&gt; parameter of GetIntegerui64vNV: */
	int GL_MAX_SHADER_BUFFER_ADDRESS_NV = 0x8F35;

	void glMakeBufferResidentNV(@GLenum int target, @GLenum int access);

	void glMakeBufferNonResidentNV(@GLenum int target);

	boolean glIsBufferResidentNV(@GLenum int target);

	void glMakeNamedBufferResidentNV(@GLuint int buffer, @GLenum int access);

	void glMakeNamedBufferNonResidentNV(@GLuint int buffer);

	boolean glIsNamedBufferResidentNV(@GLuint int buffer);

	@StripPostfix("params")
	void glGetBufferParameterui64vNV(@GLenum int target, @GLenum int pname, @OutParameter @Check("1") @GLuint64EXT LongBuffer params);

	@Alternate("glGetBufferParameterui64vNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetBufferParameterui64vNV2(@GLenum int target, @GLenum int pname, @OutParameter @GLuint64EXT LongBuffer params);

	@StripPostfix("params")
	void glGetNamedBufferParameterui64vNV(@GLuint int buffer, @GLenum int pname, @OutParameter @Check("1") @GLuint64EXT LongBuffer params);

	@Alternate("glGetNamedBufferParameterui64vNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetNamedBufferParameterui64vNV2(@GLuint int buffer, @GLenum int pname, @OutParameter @GLuint64EXT LongBuffer params);

	@StripPostfix("result")
	void glGetIntegerui64vNV(@GLenum int value, @OutParameter @Check("1") @GLuint64EXT LongBuffer result);

	@Alternate("glGetIntegerui64vNV")
	@GLreturn("result")
	@StripPostfix(value = "result", hasPostfix = false)
	void glGetIntegerui64vNV2(@GLenum int value, @OutParameter @GLuint64EXT LongBuffer result);

	void glUniformui64NV(int location, @GLuint64EXT long value);

	@StripPostfix("value")
	void glUniformui64vNV(int location, @AutoSize("value") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

	@Reuse("NVGpuShader5")
	@StripPostfix("params")
	void glGetUniformui64vNV(@GLuint int program, int location, @OutParameter @Check("1") @GLuint64EXT LongBuffer params);

	void glProgramUniformui64NV(@GLuint int program, int location, @GLuint64EXT long value);

	@StripPostfix("value")
	void glProgramUniformui64vNV(@GLuint int program, int location, @AutoSize("value") @GLsizei int count, @Const @GLuint64EXT LongBuffer value);

}