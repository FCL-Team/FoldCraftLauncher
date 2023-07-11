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

import org.lwjgl.util.generator.Reuse;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLintptr;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

public interface ARB_vertex_attrib_binding {

	/** Accepted by the &lt;pname&gt; parameter of GetVertexAttrib*v: */
	int GL_VERTEX_ATTRIB_BINDING         = 0x82D4,
		GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D5;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetBooleani_v, GetIntegeri_v,
	 * GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	int GL_VERTEX_BINDING_DIVISOR = 0x82D6,
		GL_VERTEX_BINDING_OFFSET  = 0x82D7,
		GL_VERTEX_BINDING_STRIDE  = 0x82D8;

	/** Accepted by the &lt;pname&gt; parameter of GetIntegerv, ... */
	int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D9,
		GL_MAX_VERTEX_ATTRIB_BINDINGS        = 0x82DA;

	@Reuse("GL43")
	void glBindVertexBuffer(@GLuint int bindingindex, @GLuint int buffer, @GLintptr long offset,
	                        @GLsizei int stride);

	@Reuse("GL43")
	void glVertexAttribFormat(@GLuint int attribindex, int size, @GLenum int type,
	                          boolean normalized, @GLuint int relativeoffset);

	@Reuse("GL43")
	void glVertexAttribIFormat(@GLuint int attribindex, int size, @GLenum int type,
	                           @GLuint int relativeoffset);

	@Reuse("GL43")
	void glVertexAttribLFormat(@GLuint int attribindex, int size, @GLenum int type,
	                           @GLuint int relativeoffset);

	@Reuse("GL43")
	void glVertexAttribBinding(@GLuint int attribindex, @GLuint int bindingindex);

	@Reuse("GL43")
	void glVertexBindingDivisor(@GLuint int bindingindex, @GLuint int divisor);

}