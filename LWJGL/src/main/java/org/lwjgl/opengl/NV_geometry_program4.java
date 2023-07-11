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

import org.lwjgl.util.generator.Reuse;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLuint;

public interface NV_geometry_program4 {

	/**
	 * Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	int GL_GEOMETRY_PROGRAM_NV = 0x8C26;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramivARB: */
	int GL_MAX_PROGRAM_OUTPUT_VERTICES_NV = 0x8C27;
	int GL_MAX_PROGRAM_TOTAL_OUTPUT_COMPONENTS_NV = 0x8C28;

	void glProgramVertexLimitNV(@GLenum int target, int limit);

	@Reuse("EXTGeometryShader4")
	void glFramebufferTextureEXT(@GLenum int target, @GLenum int attachment, @GLuint int texture, int level);

	@Reuse("EXTGeometryShader4")
	void glFramebufferTextureLayerEXT(@GLenum int target, @GLenum int attachment, @GLuint int texture, int level, int layer);

	@Reuse("EXTGeometryShader4")
	void glFramebufferTextureFaceEXT(@GLenum int target, @GLenum int attachment, @GLuint int texture, int level, @GLenum int face);

}