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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public interface EXT_gpu_shader4 {

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetVertexAttribdv,
	 * GetVertexAttribfv, GetVertexAttribiv, GetVertexAttribIivEXT, and
	 * GetVertexAttribIuivEXT:
	 */
	int GL_VERTEX_ATTRIB_ARRAY_INTEGER_EXT = 0x88FD;

	/** Returned by the &lt;type&gt; parameter of GetActiveUniform: */

	int GL_SAMPLER_1D_ARRAY_EXT = 0x8DC0;
	int GL_SAMPLER_2D_ARRAY_EXT = 0x8DC1;
	int GL_SAMPLER_BUFFER_EXT = 0x8DC2;
	int GL_SAMPLER_1D_ARRAY_SHADOW_EXT = 0x8DC3;
	int GL_SAMPLER_2D_ARRAY_SHADOW_EXT = 0x8DC4;
	int GL_SAMPLER_CUBE_SHADOW_EXT = 0x8DC5;
	int GL_UNSIGNED_INT_VEC2_EXT = 0x8DC6;
	int GL_UNSIGNED_INT_VEC3_EXT = 0x8DC7;
	int GL_UNSIGNED_INT_VEC4_EXT = 0x8DC8;
	int GL_INT_SAMPLER_1D_EXT = 0x8DC9;
	int GL_INT_SAMPLER_2D_EXT = 0x8DCA;
	int GL_INT_SAMPLER_3D_EXT = 0x8DCB;
	int GL_INT_SAMPLER_CUBE_EXT = 0x8DCC;
	int GL_INT_SAMPLER_2D_RECT_EXT = 0x8DCD;
	int GL_INT_SAMPLER_1D_ARRAY_EXT = 0x8DCE;
	int GL_INT_SAMPLER_2D_ARRAY_EXT = 0x8DCF;
	int GL_INT_SAMPLER_BUFFER_EXT = 0x8DD0;
	int GL_UNSIGNED_INT_SAMPLER_1D_EXT = 0x8DD1;
	int GL_UNSIGNED_INT_SAMPLER_2D_EXT = 0x8DD2;
	int GL_UNSIGNED_INT_SAMPLER_3D_EXT = 0x8DD3;
	int GL_UNSIGNED_INT_SAMPLER_CUBE_EXT = 0x8DD4;
	int GL_UNSIGNED_INT_SAMPLER_2D_RECT_EXT = 0x8DD5;
	int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY_EXT = 0x8DD6;
	int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY_EXT = 0x8DD7;
	int GL_UNSIGNED_INT_SAMPLER_BUFFER_EXT = 0x8DD8;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	int GL_MIN_PROGRAM_TEXEL_OFFSET_EXT = 0x8904;
	int GL_MAX_PROGRAM_TEXEL_OFFSET_EXT = 0x8905;

	@NoErrorCheck
	void glVertexAttribI1iEXT(@GLuint int index, int x);

	@NoErrorCheck
	void glVertexAttribI2iEXT(@GLuint int index, int x, int y);

	@NoErrorCheck
	void glVertexAttribI3iEXT(@GLuint int index, int x, int y, int z);

	@NoErrorCheck
	void glVertexAttribI4iEXT(@GLuint int index, int x, int y, int z, int w);

	@NoErrorCheck
	void glVertexAttribI1uiEXT(@GLuint int index, @GLuint int x);

	@NoErrorCheck
	void glVertexAttribI2uiEXT(@GLuint int index, @GLuint int x, @GLuint int y);

	@NoErrorCheck
	void glVertexAttribI3uiEXT(@GLuint int index, @GLuint int x, @GLuint int y, @GLuint int z);

	@NoErrorCheck
	void glVertexAttribI4uiEXT(@GLuint int index, @GLuint int x, @GLuint int y, @GLuint int z, @GLuint int w);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI1ivEXT(@GLuint int index, @Check("1") @Const IntBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI2ivEXT(@GLuint int index, @Check("2") @Const IntBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI3ivEXT(@GLuint int index, @Check("3") @Const IntBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI4ivEXT(@GLuint int index, @Check("4") @Const IntBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI1uivEXT(@GLuint int index, @Check("1") @Const @GLuint IntBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI2uivEXT(@GLuint int index, @Check("2") @Const @GLuint IntBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI3uivEXT(@GLuint int index, @Check("3") @Const @GLuint IntBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI4uivEXT(@GLuint int index, @Check("4") @Const @GLuint IntBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI4bvEXT(@GLuint int index, @Check("4") @Const ByteBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI4svEXT(@GLuint int index, @Check("4") @Const ShortBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI4ubvEXT(@GLuint int index, @Check("4") @Const @GLubyte ByteBuffer v);

	@NoErrorCheck
	@StripPostfix("v")
	void glVertexAttribI4usvEXT(@GLuint int index, @Check("4") @Const @GLushort ShortBuffer v);

	void glVertexAttribIPointerEXT(@GLuint int index, int size, @GLenum int type, @GLsizei int stride,
	                               @CachedReference(index = "index", name = "glVertexAttribPointer_buffer")
								   @BufferObject(BufferKind.ArrayVBO)
								   @Check
								   @Const
								   @GLbyte
								   @GLubyte
								   @GLshort
								   @GLushort
								   @GLint
								   @GLuint Buffer buffer);

	@StripPostfix("params")
	void glGetVertexAttribIivEXT(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribIuivEXT(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") @GLuint IntBuffer params);

	void glUniform1uiEXT(int location, @GLuint int v0);

	void glUniform2uiEXT(int location, @GLuint int v0, @GLuint int v1);

	void glUniform3uiEXT(int location, @GLuint int v0, @GLuint int v1, @GLuint int v2);

	void glUniform4uiEXT(int location, @GLuint int v0, @GLuint int v1, @GLuint int v2, @GLuint int v3);

	@StripPostfix("value")
	void glUniform1uivEXT(int location, @AutoSize("value") @GLsizei int count, @Const @GLuint IntBuffer value);

	@StripPostfix("value")
	void glUniform2uivEXT(int location, @AutoSize(value = "value", expression = " >> 1") @GLsizei int count, @Const @GLuint IntBuffer value);

	@StripPostfix("value")
	void glUniform3uivEXT(int location, @AutoSize(value = "value", expression = " / 3") @GLsizei int count, @Const @GLuint IntBuffer value);

	@StripPostfix("value")
	void glUniform4uivEXT(int location, @AutoSize(value = "value", expression = " >> 2") @GLsizei int count, @Const @GLuint IntBuffer value);

	@StripPostfix("params")
	void glGetUniformuivEXT(@GLuint int program, int location, @OutParameter @Check @GLuint IntBuffer params);

	void glBindFragDataLocationEXT(@GLuint int program, @GLuint int colorNumber, @NullTerminated @Const @GLchar ByteBuffer name);

	@Alternate("glBindFragDataLocationEXT")
	void glBindFragDataLocationEXT(@GLuint int program, @GLuint int colorNumber, @NullTerminated CharSequence name);

	int glGetFragDataLocationEXT(@GLuint int program, @NullTerminated @Const @GLchar ByteBuffer name);

	@Alternate("glGetFragDataLocationEXT")
	int glGetFragDataLocationEXT(@GLuint int program, @NullTerminated CharSequence name);

}
