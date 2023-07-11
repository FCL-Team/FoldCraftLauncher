/*
 * Copyright (c) 2002-2013 LWJGL Project
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

import org.lwjgl.util.generator.Dependent;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLsizei;
import org.lwjgl.util.generator.opengl.GLuint;

@Dependent
public interface ARB_sparse_texture {

	/**
	 * Accepted by the &lt;pname&gt; parameter to TexParameter{i f}{v},
	 * TexParameterI{u}v, GetTexParameter{if}v and GetTexParameterIi{u}v:
	 */
	int GL_TEXTURE_SPARSE_ARB          = 0x91A6,
		GL_VIRTUAL_PAGE_SIZE_INDEX_ARB = 0x91A7;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexParameter{if}v and
	 * GetTexParameterIi{u}v:
	 */
	int GL_NUM_SPARSE_LEVELS_ARB = 0x91AA;

	/** Accepted by the &lt;pname&gt; parameter to GetInternalformativ: */
	int GL_NUM_VIRTUAL_PAGE_SIZES_ARB = 0x91A8,
		GL_VIRTUAL_PAGE_SIZE_X_ARB    = 0x9195,
		GL_VIRTUAL_PAGE_SIZE_Y_ARB    = 0x9196,
		GL_VIRTUAL_PAGE_SIZE_Z_ARB    = 0x9197;

	/**
	 * Accepted by the &lt;pname&gt; parameter to GetIntegerv, GetFloatv, GetDoublev,
	 * GetInteger64v, and GetBooleanv:
	 */
	int GL_MAX_SPARSE_TEXTURE_SIZE_ARB                = 0x9198,
		GL_MAX_SPARSE_3D_TEXTURE_SIZE_ARB             = 0x9199,
		GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS_ARB        = 0x919A,
		GL_SPARSE_TEXTURE_FULL_ARRAY_CUBE_MIPMAPS_ARB = 0x91A9;

	void glTexPageCommitmentARB(@GLenum int target,
	                            int level,
	                            int xoffset,
	                            int yoffset,
	                            int zoffset,
	                            @GLsizei int width,
	                            @GLsizei int height,
	                            @GLsizei int depth,
	                            boolean commit);

	@Dependent("GL_EXT_direct_state_access")
	void glTexturePageCommitmentEXT(@GLuint int texture,
	                                @GLenum int target,
	                                int level,
	                                int xoffset,
	                                int yoffset,
	                                int zoffset,
	                                @GLsizei int width,
	                                @GLsizei int height,
	                                @GLsizei int depth,
	                                boolean commit);

}