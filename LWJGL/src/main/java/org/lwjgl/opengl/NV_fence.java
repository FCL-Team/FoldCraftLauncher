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
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.*;

public interface NV_fence {
	int GL_ALL_COMPLETED_NV = 0x84F2;
	int GL_FENCE_STATUS_NV = 0x84F3;
	int GL_FENCE_CONDITION_NV = 0x84F4;

	void glGenFencesNV(@AutoSize("piFences") @GLsizei int n, @OutParameter @GLuint IntBuffer piFences);

	@Alternate("glGenFencesNV")
	@GLreturn("piFences")
	void glGenFencesNV2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer piFences);

	void glDeleteFencesNV(@AutoSize("piFences") @GLsizei int n, @Const @GLuint IntBuffer piFences);

	@Alternate("glDeleteFencesNV")
	void glDeleteFencesNV(@Constant("1") @GLsizei int n, @Const @GLuint @Constant(value = "APIUtil.getInt(caps, fence)", keepParam = true) int fence);

	void glSetFenceNV(@GLuint int fence, @GLenum int condition);

	boolean glTestFenceNV(@GLuint int fence);

	void glFinishFenceNV(@GLuint int fence);

	boolean glIsFenceNV(@GLuint int fence);

	void glGetFenceivNV(@GLuint int fence, @GLenum int pname, @OutParameter @Check("4") IntBuffer piParams);
}
