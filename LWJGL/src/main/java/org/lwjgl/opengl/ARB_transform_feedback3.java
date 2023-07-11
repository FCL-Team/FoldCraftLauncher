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
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

@Extension(postfix = "")
public interface ARB_transform_feedback3 {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * and GetFloatv:
	 */
	int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 0x8E70;
	int GL_MAX_VERTEX_STREAMS = 0x8E71;

	@Reuse("GL40")
	void glDrawTransformFeedbackStream(@GLenum int mode, @GLuint int id, @GLuint int stream);

	@Reuse("GL40")
	void glBeginQueryIndexed(@GLenum int target, @GLuint int index, @GLuint int id);

	@Reuse("GL40")
	void glEndQueryIndexed(@GLenum int target, @GLuint int index);

	@Reuse("GL40")
	@StripPostfix("params")
	void glGetQueryIndexediv(@GLenum int target, @GLuint int index, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Reuse("GL40")
	@Alternate("glGetQueryIndexediv")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetQueryIndexediv2(@GLenum int target, @GLuint int index, @GLenum int pname, @OutParameter IntBuffer params);

}