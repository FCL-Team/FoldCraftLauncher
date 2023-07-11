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

import java.nio.Buffer;
import java.nio.DoubleBuffer;

@Dependent
public interface EXT_vertex_attrib_64bit {

	/** Returned in the &lt;type&gt; parameter of GetActiveAttrib: */
	int GL_DOUBLE_VEC2_EXT = 0x8FFC;
	int GL_DOUBLE_VEC3_EXT = 0x8FFD;
	int GL_DOUBLE_VEC4_EXT = 0x8FFE;
	int GL_DOUBLE_MAT2_EXT = 0x8F46;
	int GL_DOUBLE_MAT3_EXT = 0x8F47;
	int GL_DOUBLE_MAT4_EXT = 0x8F48;
	int GL_DOUBLE_MAT2x3_EXT = 0x8F49;
	int GL_DOUBLE_MAT2x4_EXT = 0x8F4A;
	int GL_DOUBLE_MAT3x2_EXT = 0x8F4B;
	int GL_DOUBLE_MAT3x4_EXT = 0x8F4C;
	int GL_DOUBLE_MAT4x2_EXT = 0x8F4D;
	int GL_DOUBLE_MAT4x3_EXT = 0x8F4E;

	void glVertexAttribL1dEXT(@GLuint int index, double x);

	void glVertexAttribL2dEXT(@GLuint int index, double x, double y);

	void glVertexAttribL3dEXT(@GLuint int index, double x, double y, double z);

	void glVertexAttribL4dEXT(@GLuint int index, double x, double y, double z, double w);

	@StripPostfix("v")
	void glVertexAttribL1dvEXT(@GLuint int index, @Const @Check("1") DoubleBuffer v);

	@StripPostfix("v")
	void glVertexAttribL2dvEXT(@GLuint int index, @Const @Check("2") DoubleBuffer v);

	@StripPostfix("v")
	void glVertexAttribL3dvEXT(@GLuint int index, @Const @Check("3") DoubleBuffer v);

	@StripPostfix("v")
	void glVertexAttribL4dvEXT(@GLuint int index, @Const @Check("4") DoubleBuffer v);

	void glVertexAttribLPointerEXT(@GLuint int index, int size, @Constant("GL11.GL_DOUBLE") @GLenum int type, @GLsizei int stride,
	                               @CachedReference(index = "index", name = "glVertexAttribPointer_buffer")
	                               @BufferObject(BufferKind.ArrayVBO)
	                               @Check @Const @GLdouble Buffer pointer);

	@StripPostfix("params")
	void glGetVertexAttribLdvEXT(@GLuint int index, @GLenum int pname, @OutParameter @Check("4") DoubleBuffer params);

	@Reuse("ARBVertexAttrib64bit")
	@Dependent("GL_EXT_direct_state_access")
	void glVertexArrayVertexAttribLOffsetEXT(@GLuint int vaobj, @GLuint int buffer, @GLuint int index, int size, @GLenum int type, @GLsizei int stride, @GLintptr long offset);

}