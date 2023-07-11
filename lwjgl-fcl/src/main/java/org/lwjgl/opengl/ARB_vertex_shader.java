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

public interface ARB_vertex_shader {

	/**
	 * Accepted by the &lt;shaderType&gt; argument of CreateShaderObjectARB and
	 * returned by the &lt;params&gt; parameter of GetObjectParameter{if}vARB:
	 */
	int GL_VERTEX_SHADER_ARB = 0x8B31;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_MAX_VERTEX_UNIFORM_COMPONENTS_ARB    = 0x8B4A;
	int GL_MAX_VARYING_FLOATS_ARB               = 0x8B4B;
	int GL_MAX_VERTEX_ATTRIBS_ARB               = 0x8869;
	int GL_MAX_TEXTURE_IMAGE_UNITS_ARB          = 0x8872;
	int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS_ARB   = 0x8B4C;
	int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS_ARB = 0x8B4D;
	int GL_MAX_TEXTURE_COORDS_ARB               = 0x8871;

	/**
	 * Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and
	 * by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 0x8642;
	int GL_VERTEX_PROGRAM_TWO_SIDE_ARB   = 0x8643;

	/** Accepted by the &lt;pname&gt; parameter GetObjectParameter{if}vARB: */
	int GL_OBJECT_ACTIVE_ATTRIBUTES_ARB           = 0x8B89;
	int GL_OBJECT_ACTIVE_ATTRIBUTE_MAX_LENGTH_ARB = 0x8B8A;

	/** Accepted by the &lt;pname&gt; parameter of GetVertexAttrib{dfi}vARB: */
	int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB    = 0x8622;
	int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB       = 0x8623;
	int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB     = 0x8624;
	int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB       = 0x8625;
	int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 0x886A;
	int GL_CURRENT_VERTEX_ATTRIB_ARB          = 0x8626;

	/** Accepted by the &lt;pname&gt; parameter of GetVertexAttribPointervARB: */
	int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 0x8645;

	/** Returned by the &lt;type&gt; parameter of GetActiveAttribARB: */
	int GL_FLOAT_VEC2_ARB = 0x8B50;
	int GL_FLOAT_VEC3_ARB = 0x8B51;
	int GL_FLOAT_VEC4_ARB = 0x8B52;
	int GL_FLOAT_MAT2_ARB = 0x8B5A;
	int GL_FLOAT_MAT3_ARB = 0x8B5B;
	int GL_FLOAT_MAT4_ARB = 0x8B5C;

	@NoErrorCheck
	void glVertexAttrib1sARB(@GLuint int index, short v0);

	@NoErrorCheck
	void glVertexAttrib1fARB(@GLuint int index, float v0);

	@NoErrorCheck
	void glVertexAttrib1dARB(@GLuint int index, double v0);

	@NoErrorCheck
	void glVertexAttrib2sARB(@GLuint int index, short v0, short v1);

	@NoErrorCheck
	void glVertexAttrib2fARB(@GLuint int index, float v0, float v1);

	@NoErrorCheck
	void glVertexAttrib2dARB(@GLuint int index, double v0, double v1);

	@NoErrorCheck
	void glVertexAttrib3sARB(@GLuint int index, short v0, short v1, short v2);

	@NoErrorCheck
	void glVertexAttrib3fARB(@GLuint int index, float v0, float v1, float v2);

	@NoErrorCheck
	void glVertexAttrib3dARB(@GLuint int index, double v0, double v1, double v2);

	@NoErrorCheck
	void glVertexAttrib4sARB(@GLuint int index, short v0, short v1, short v2, short v3);

	@NoErrorCheck
	void glVertexAttrib4fARB(@GLuint int index, float v0, float v1, float v2, float v3);

	@NoErrorCheck
	void glVertexAttrib4dARB(@GLuint int index, double v0, double v1, double v2, double v3);

	@NoErrorCheck
	void glVertexAttrib4NubARB(@GLuint int index, @GLubyte byte x, @GLubyte byte y, @GLubyte byte z, @GLubyte byte w);

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

	@Alternate("glVertexAttribPointerARB")
	void glVertexAttribPointerARB(@GLuint int index, int size, @GLenum int type, boolean normalized, @GLsizei int stride,
	                              @CachedReference(index = "index", name = "glVertexAttribPointer_buffer")
	                              @BufferObject(BufferKind.ArrayVBO)
	                              @Check
	                              @Const ByteBuffer buffer);

	void glEnableVertexAttribArrayARB(@GLuint int index);

	void glDisableVertexAttribArrayARB(@GLuint int index);

	void glBindAttribLocationARB(@GLhandleARB int programObj, @GLuint int index, @NullTerminated @Const @GLcharARB ByteBuffer name);

	@Alternate("glBindAttribLocationARB")
	void glBindAttribLocationARB(@GLhandleARB int programObj, @GLuint int index, @NullTerminated CharSequence name);

	void glGetActiveAttribARB(@GLhandleARB int programObj, @GLuint int index, @AutoSize("name") @GLsizei int maxLength,
	                          @OutParameter @GLsizei @Check(value = "1", canBeNull = true) IntBuffer length,
	                          @OutParameter @Check("1") IntBuffer size,
	                          @OutParameter @GLenum @Check("1") IntBuffer type,
	                          @OutParameter @GLcharARB ByteBuffer name);

	/** Overloads glGetActiveAttribARB. This version returns both size and type in the sizeType buffer (at .position() and .position() + 1). */
	@Alternate("glGetActiveAttribARB")
	@GLreturn(value = "name", maxLength = "maxLength")
	void glGetActiveAttribARB2(@GLhandleARB int programObj, @GLuint int index, @GLsizei int maxLength,
	                           @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(name_length)") IntBuffer length,
	                           @OutParameter @Check("2") IntBuffer sizeType,
	                           @OutParameter @GLenum @Constant("MemoryUtil.getAddress(sizeType, sizeType.position() + 1)") IntBuffer type,
	                           @OutParameter @GLcharARB ByteBuffer name);

	/** Overloads glGetActiveAttribARB. This version returns only the attrib name. */
	@Alternate(value = "glGetActiveAttribARB", javaAlt = true)
	@GLreturn(value = "name", maxLength = "maxLength")
	void glGetActiveAttribARB(@GLhandleARB int programObj, @GLuint int index, @GLsizei int maxLength,
	                          @OutParameter @GLsizei @Constant("MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1)") IntBuffer length,
	                          @OutParameter @GLcharARB ByteBuffer name);

	/** Overloads glGetActiveAttribARB. This version returns only the attrib size. */
	@Alternate(value = "glGetActiveAttribARB", javaAlt = true)
	@GLreturn(value = "size")
	void glGetActiveAttribSizeARB(@GLhandleARB int programObj, @GLuint int index, @Constant("0") @GLsizei int maxLength,
	                              @OutParameter @GLsizei @Constant("0L") IntBuffer length,
	                              @OutParameter IntBuffer size,
	                              @OutParameter @GLenum @Constant("MemoryUtil.getAddress(size, 1)") IntBuffer type, // Reuse size buffer and ignore
	                              @OutParameter @GLcharARB @Constant("APIUtil.getBufferByte0(caps)") ByteBuffer name);

	/** Overloads glGetActiveAttribARB. This version returns only the attrib type. */
	@Alternate(value = "glGetActiveAttribARB", javaAlt = true)
	@GLreturn(value = "type")
	void glGetActiveAttribTypeARB(@GLhandleARB int programObj, @GLuint int index, @Constant("0") @GLsizei int maxLength,
	                              @OutParameter @GLsizei @Constant("0L") IntBuffer length,
	                              @OutParameter @Constant("MemoryUtil.getAddress(type, 1)") IntBuffer size, // Reuse type buffer and ignore
	                              @OutParameter @GLenum IntBuffer type,
	                              @OutParameter @GLcharARB @Constant("APIUtil.getBufferByte0(caps)") ByteBuffer name);

	int glGetAttribLocationARB(@GLhandleARB int programObj, @NullTerminated @Const @GLcharARB ByteBuffer name);

	@Alternate("glGetAttribLocationARB")
	int glGetAttribLocationARB(@GLhandleARB int programObj, @NullTerminated CharSequence name);

	@StripPostfix("params")
	void glGetVertexAttribfvARB(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribdvARB(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") DoubleBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribivARB(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@StripPostfix("result")
	void glGetVertexAttribPointervARB(@GLuint int index, @GLenum int pname, @Result @GLvoid ByteBuffer result);

}
