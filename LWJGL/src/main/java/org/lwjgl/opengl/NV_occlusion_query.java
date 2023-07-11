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

import java.nio.IntBuffer;

public interface NV_occlusion_query {

	int GL_OCCLUSION_TEST_HP = 0x8165;
	int GL_OCCLUSION_TEST_RESULT_HP = 0x8166;
	/* HP_occlusion_test */
	int GL_PIXEL_COUNTER_BITS_NV = 0x8864;
	int GL_CURRENT_OCCLUSION_QUERY_ID_NV = 0x8865;
	int GL_PIXEL_COUNT_NV = 0x8866;
	int GL_PIXEL_COUNT_AVAILABLE_NV = 0x8867;

	void glGenOcclusionQueriesNV(@AutoSize("piIDs") @GLsizei int n, @OutParameter @GLuint IntBuffer piIDs);

	@Alternate("glGenOcclusionQueriesNV")
	@GLreturn("piIDs")
	void glGenOcclusionQueriesNV2(@Constant("1") @GLsizei int n, @OutParameter @GLuint IntBuffer piIDs);

	void glDeleteOcclusionQueriesNV(@AutoSize("piIDs") @GLsizei int n, @Const @GLuint IntBuffer piIDs);

	@Alternate("glDeleteOcclusionQueriesNV")
	void glDeleteOcclusionQueriesNV(@Constant("1") @GLsizei int n, @Constant(value = "APIUtil.getInt(caps, piID)", keepParam = true) int piID);

	boolean glIsOcclusionQueryNV(@GLuint int id);

	void glBeginOcclusionQueryNV(@GLuint int id);

	void glEndOcclusionQueryNV();

	@StripPostfix("params")
	void glGetOcclusionQueryuivNV(@GLuint int id, @GLenum int pname, @OutParameter @Check("1") @GLuint IntBuffer params);

	@Alternate("glGetOcclusionQueryuivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetOcclusionQueryuivNV2(@GLuint int id, @GLenum int pname, @OutParameter @GLuint IntBuffer params);

	@StripPostfix("params")
	void glGetOcclusionQueryivNV(@GLuint int id, @GLenum int pname, @OutParameter @Check("1") IntBuffer params);

	@Alternate("glGetOcclusionQueryivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetOcclusionQueryivNV2(@GLuint int id, @GLenum int pname, @OutParameter IntBuffer params);
}
