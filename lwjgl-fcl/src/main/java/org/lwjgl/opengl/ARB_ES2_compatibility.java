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
import org.lwjgl.util.generator.opengl.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface ARB_ES2_compatibility {

	/**
	 * Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_SHADER_COMPILER = 0x8DFA,
		GL_NUM_SHADER_BINARY_FORMATS = 0x8DF9,
		GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB,
		GL_MAX_VARYING_VECTORS = 0x8DFC,
		GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD,
		GL_IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A,
		GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;

	/** Accepted by the &lt;type&gt; parameter of VertexAttribPointer: */
	int GL_FIXED = 0x140C;

	/**
	 * Accepted by the &lt;precisiontype&gt; parameter of
	 * GetShaderPrecisionFormat:
	 */
	int GL_LOW_FLOAT = 0x8DF0,
		GL_MEDIUM_FLOAT = 0x8DF1,
		GL_HIGH_FLOAT = 0x8DF2,
		GL_LOW_INT = 0x8DF3,
		GL_MEDIUM_INT = 0x8DF4,
		GL_HIGH_INT = 0x8DF5;

	/** Accepted by the &lt;format&gt; parameter of most commands taking sized internal formats: */
	int GL_RGB565 = 0x8D62;

	@Reuse("GL41")
	void glReleaseShaderCompiler();

	@Reuse("GL41")
	void glShaderBinary(@AutoSize("shaders") @GLsizei int count, @Const @GLuint IntBuffer shaders,
	                    @GLenum int binaryformat, @Const @GLvoid ByteBuffer binary, @AutoSize("binary") @GLsizei int length);

	@Reuse("GL41")
	void glGetShaderPrecisionFormat(@GLenum int shadertype, @GLenum int precisiontype,
	                                @OutParameter @Check("2") IntBuffer range,
	                                @OutParameter @Check("1") IntBuffer precision);

	@Reuse("GL41")
	void glDepthRangef(@GLclampf float n, @GLclampf float f);

	@Reuse("GL41")
	void glClearDepthf(@GLclampf float d);

}