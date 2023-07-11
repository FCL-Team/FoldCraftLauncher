/*
 * Copyright (c) 2002-2011 LWJGL Project
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
package org.lwjgl.opengles;

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

public interface EXT_occlusion_query_boolean {

	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQueryEXT, EndQueryEXT,
	 * and GetQueryivEXT:
	 */
	int GL_ANY_SAMPLES_PASSED_EXT              = 0x8C2F,
		GL_ANY_SAMPLES_PASSED_CONSERVATIVE_EXT = 0x8D6A;

	/** Accepted by the &lt;pname&gt; parameter of GetQueryivEXT: */
	int GL_CURRENT_QUERY_EXT = 0x8865;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetQueryObjectivEXT and
	 * GetQueryObjectuivEXT:
	 */
	int GL_QUERY_RESULT_EXT           = 0x8866,
		GL_QUERY_RESULT_AVAILABLE_EXT = 0x8867;

	void glGenQueriesEXT(@AutoSize("ids") @GLsizei int n, @OutParameter @GLuint IntBuffer ids);

	@Alternate("glGenQueriesEXT")
	@GLreturn("ids")
	void glGenQueriesEXT2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer ids);

	void glDeleteQueriesEXT(@AutoSize("ids") @GLsizei int n, @GLuint IntBuffer ids);

	@Alternate("glDeleteQueriesEXT")
	void glDeleteQueriesEXT(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(id)", keepParam = true) int id);

	boolean glIsQueryEXT(@GLuint int id);

	void glBeginQueryEXT(@GLenum int target, @GLuint int id);

	void glEndQueryEXT(@GLenum int target);

	@StripPostfix("params")
	void glGetQueryivEXT(@GLenum int target, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetQueryivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetQueryivEXT2(@GLenum int target, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	void glGetQueryObjectuivEXT(@GLuint int id, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetQueryObjectuivEXT")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetQueryObjectuivEXT2(@GLuint int id, @GLenum int pname, @OutParameter IntBuffer params);

}