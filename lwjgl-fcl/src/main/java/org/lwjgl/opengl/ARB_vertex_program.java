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

import java.nio.*;

public interface ARB_vertex_program extends ARB_program {

	/**
	 * Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, by the
	 * &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev,
	 * and by the &lt;target&gt; parameter of ProgramStringARB, BindProgramARB,
	 * ProgramEnvParameter4[df][v]ARB, ProgramLocalParameter4[df][v]ARB,
	 * GetProgramEnvParameter[df]vARB, GetProgramLocalParameter[df]vARB,
	 * GetProgramivARB, and GetProgramStringARB.
	 */
	int GL_VERTEX_PROGRAM_ARB = 0x8620;

	/**
	 * Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 0x8642;
	int GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 0x8643;
	int GL_COLOR_SUM_ARB = 0x8458;

	/** Accepted by the &lt;pname&gt; parameter of GetVertexAttrib[dfi]vARB: */
	int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 0x8622;
	int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 0x8623;
	int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 0x8624;
	int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 0x8625;
	int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 0x886A;
	int GL_CURRENT_VERTEX_ATTRIB_ARB = 0x8626;

	/** Accepted by the &lt;pname&gt; parameter of GetVertexAttribPointervARB: */
	int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 0x8645;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramivARB: */
	int GL_PROGRAM_ADDRESS_REGISTERS_ARB = 0x88B0;
	int GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB = 0x88B1;
	int GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 0x88B2;
	int GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 0x88B3;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_MAX_VERTEX_ATTRIBS_ARB = 0x8869;

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib1sARB(@GLuint int index, short x);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib1fARB(@GLuint int index, float x);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib1dARB(@GLuint int index, double x);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib2sARB(@GLuint int index, short x, short y);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib2fARB(@GLuint int index, float x, float y);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib2dARB(@GLuint int index, double x, double y);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib3sARB(@GLuint int index, short x, short y, short z);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib3fARB(@GLuint int index, float x, float y, float z);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib3dARB(@GLuint int index, double x, double y, double z);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib4sARB(@GLuint int index, short x, short y, short z, short w);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib4fARB(@GLuint int index, float x, float y, float z, float w);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib4dARB(@GLuint int index, double x, double y, double z, double w);

	@Reuse("ARBVertexShader")
	@NoErrorCheck
	void glVertexAttrib4NubARB(@GLuint int index, @GLubyte byte x, @GLubyte byte y, @GLubyte byte z, @GLubyte byte w);

	@Reuse("ARBVertexShader")
	void glVertexAttribPointerARB(@GLuint int index, int size, @AutoType("buffer") @GLenum int type, boolean normalized, @GLsizei int stride,
	                              @CachedReference(index = "index", name = "glVertexAttribPointer_buffer")
	                              @BufferObject(BufferKind.ArrayVBO)
	                              @Check
	                              @Const
	                              @GLbyte
	                              @GLubyte
	                              @GLshort
	                              @GLushort
	                              @GLint
	                              @GLuint
	                              @GLfloat
	                              @GLdouble Buffer buffer);

	@Reuse("ARBVertexShader")
	void glEnableVertexAttribArrayARB(@GLuint int index);

	@Reuse("ARBVertexShader")
	void glDisableVertexAttribArrayARB(@GLuint int index);

	@Reuse("ARBVertexShader")
	@StripPostfix("params")
	void glGetVertexAttribfvARB(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@Reuse("ARBVertexShader")
	@StripPostfix("params")
	void glGetVertexAttribdvARB(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") DoubleBuffer params);

	@Reuse("ARBVertexShader")
	@StripPostfix("params")
	void glGetVertexAttribivARB(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Reuse("ARBVertexShader")
	@StripPostfix("result")
	void glGetVertexAttribPointervARB(@GLuint int index, @GLenum int pname, @Result @GLvoid ByteBuffer result);
}
