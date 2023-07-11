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

@Extension(postfix = "")
public interface ARB_timer_query {

	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 * GetQueryiv:
	 */
	int GL_TIME_ELAPSED = 0x88BF;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetQueryiv and QueryCounter.
	 * Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_TIMESTAMP = 0x8E28;

	@Reuse("GL33")
	void glQueryCounter(@GLuint int id, @GLenum int target);

	@Reuse("GL33")
	@StripPostfix("params")
	void glGetQueryObjecti64v(@GLuint int id, @GLenum int pname, @OutParameter @Check("1") @GLint64 LongBuffer params);

	@Reuse("GL33")
	@Alternate("glGetQueryObjecti64v")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetQueryObjecti64v2(@GLuint int id, @GLenum int pname, @OutParameter @GLint64 LongBuffer params);

	@Reuse("GL33")
	@StripPostfix("params")
	void glGetQueryObjectui64v(@GLuint int id, @GLenum int pname, @OutParameter @Check("1") @GLuint64 LongBuffer params);

	@Reuse("GL33")
	@Alternate("glGetQueryObjectui64v")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetQueryObjectui64v2(@GLuint int id, @GLenum int pname, @OutParameter @GLuint64 LongBuffer params);

}