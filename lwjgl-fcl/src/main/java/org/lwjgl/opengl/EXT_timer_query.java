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

import java.nio.*;

public interface EXT_timer_query {

	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 * GetQueryiv:
	 */
	int GL_TIME_ELAPSED_EXT = 0x88BF;

	@StripPostfix("params")
	void glGetQueryObjecti64vEXT(@GLuint int id, @GLenum int pname, @OutParameter @Check("1") @GLint64EXT LongBuffer params);

	@Alternate("glGetQueryObjecti64vEXT")
	@GLreturn("params")
	@StripPostfix("params")
	void glGetQueryObjecti64vEXT2(@GLuint int id, @GLenum int pname, @OutParameter @GLint64EXT LongBuffer params);

	@StripPostfix("params")
	void glGetQueryObjectui64vEXT(@GLuint int id, @GLenum int pname, @OutParameter @Check("1") @GLuint64EXT LongBuffer params);

	@Alternate("glGetQueryObjectui64vEXT")
	@GLreturn("params")
	@StripPostfix("params")
	void glGetQueryObjectui64vEXT2(@GLuint int id, @GLenum int pname, @OutParameter @GLuint64EXT LongBuffer params);

}
