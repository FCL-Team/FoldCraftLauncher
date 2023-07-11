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

import java.nio.LongBuffer;

@Dependent
public interface NV_vertex_attrib_integer_64bit {

	/**
	 * Accepted by the &lt;type&gt; parameter of VertexAttribLPointerEXT,
	 * VertexArrayVertexAttribLOffsetEXT, and VertexAttribLFormatNV:
	 */
	int GL_INT64_NV = 0x140E;
	int GL_UNSIGNED_INT64_NV = 0x140F;

	void glVertexAttribL1i64NV(@GLuint int index, @GLint64EXT long x);

	void glVertexAttribL2i64NV(@GLuint int index, @GLint64EXT long x, @GLint64EXT long y);

	void glVertexAttribL3i64NV(@GLuint int index, @GLint64EXT long x, @GLint64EXT long y, @GLint64EXT long z);

	void glVertexAttribL4i64NV(@GLuint int index, @GLint64EXT long x, @GLint64EXT long y, @GLint64EXT long z, @GLint64EXT long w);

	@StripPostfix("v")
	void glVertexAttribL1i64vNV(@GLuint int index, @Const @GLint64EXT @Check("1") LongBuffer v);

	@StripPostfix("v")
	void glVertexAttribL2i64vNV(@GLuint int index, @Const @GLint64EXT @Check("2") LongBuffer v);

	@StripPostfix("v")
	void glVertexAttribL3i64vNV(@GLuint int index, @Const @GLint64EXT @Check("3") LongBuffer v);

	@StripPostfix("v")
	void glVertexAttribL4i64vNV(@GLuint int index, @Const @GLint64EXT @Check("4") LongBuffer v);

	void glVertexAttribL1ui64NV(@GLuint int index, @GLuint64EXT long x);

	void glVertexAttribL2ui64NV(@GLuint int index, @GLuint64EXT long x, @GLuint64EXT long y);

	void glVertexAttribL3ui64NV(@GLuint int index, @GLuint64EXT long x, @GLuint64EXT long y, @GLuint64EXT long z);

	void glVertexAttribL4ui64NV(@GLuint int index, @GLuint64EXT long x, @GLuint64EXT long y, @GLuint64EXT long z, @GLuint64EXT long w);

	@StripPostfix("v")
	void glVertexAttribL1ui64vNV(@GLuint int index, @Const @GLuint64EXT @Check("1") LongBuffer v);

	@StripPostfix("v")
	void glVertexAttribL2ui64vNV(@GLuint int index, @Const @GLuint64EXT @Check("2") LongBuffer v);

	@StripPostfix("v")
	void glVertexAttribL3ui64vNV(@GLuint int index, @Const @GLuint64EXT @Check("3") LongBuffer v);

	@StripPostfix("v")
	void glVertexAttribL4ui64vNV(@GLuint int index, @Const @GLuint64EXT @Check("4") LongBuffer v);

	@StripPostfix("params")
	void glGetVertexAttribLi64vNV(@GLuint int index, @GLenum int pname, @OutParameter @GLint64EXT @Check("4") LongBuffer params);

	@StripPostfix("params")
	void glGetVertexAttribLui64vNV(@GLuint int index, @GLenum int pname, @OutParameter @GLuint64EXT @Check("4") LongBuffer params);

	@Dependent("GL_NV_vertex_buffer_unified_memory")
	void glVertexAttribLFormatNV(@GLuint int index, int size, @GLenum int type, @GLsizei int stride);

}