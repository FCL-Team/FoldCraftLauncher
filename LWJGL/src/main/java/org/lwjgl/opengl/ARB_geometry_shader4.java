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

import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLuint;

public interface ARB_geometry_shader4 {

	/**
	 * Accepted by the &lt;type&gt; parameter of CreateShader and returned by the
	 * &lt;params&gt; parameter of GetShaderiv:
	 */
	int GL_GEOMETRY_SHADER_ARB = 0x8DD9;

	/**
	 * Accepted by the &lt;pname&gt; parameter of ProgramParameteriEXT and
	 * GetProgramiv:
	 */
	int GL_GEOMETRY_VERTICES_OUT_ARB = 0x8DDA;
	int GL_GEOMETRY_INPUT_TYPE_ARB = 0x8DDB;
	int GL_GEOMETRY_OUTPUT_TYPE_ARB = 0x8DDC;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS_ARB = 0x8C29;
	int GL_MAX_GEOMETRY_VARYING_COMPONENTS_ARB = 0x8DDD;
	int GL_MAX_VERTEX_VARYING_COMPONENTS_ARB = 0x8DDE;
	int GL_MAX_VARYING_COMPONENTS_ARB = 0x8B4B;
	int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS_ARB = 0x8DDF;
	int GL_MAX_GEOMETRY_OUTPUT_VERTICES_ARB = 0x8DE0;
	int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS_ARB = 0x8DE1;

	/**
	 * Accepted by the &lt;mode&gt; parameter of Begin, DrawArrays,
	 * MultiDrawArrays, DrawElements, MultiDrawElements, and
	 * DrawRangeElements:
	 */
	int GL_LINES_ADJACENCY_ARB = 0xA;
	int GL_LINE_STRIP_ADJACENCY_ARB = 0xB;
	int GL_TRIANGLES_ADJACENCY_ARB = 0xC;
	int GL_TRIANGLE_STRIP_ADJACENCY_ARB = 0xD;

	/** Returned by CheckFramebufferStatusEXT: */
	int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS_ARB = 0x8DA8;
	int GL_FRAMEBUFFER_INCOMPLETE_LAYER_COUNT_ARB = 0x8DA9;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetFramebufferAttachment-
	 * ParameterivEXT:
	 */
	int GL_FRAMEBUFFER_ATTACHMENT_LAYERED_ARB = 0x8DA7;
	int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_ARB = 0x8CD4;

	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 * and by the &lt;pname&gt; parameter of GetIntegerv, GetFloatv, GetDoublev,
	 * and GetBooleanv:
	 */
	int GL_PROGRAM_POINT_SIZE_ARB = 0x8642;

	void glProgramParameteriARB(@GLuint int program, @GLenum int pname, int value);

	void glFramebufferTextureARB(@GLenum int target, @GLenum int attachment, @GLuint int texture, int level);

	void glFramebufferTextureLayerARB(@GLenum int target, @GLenum int attachment, @GLuint int texture, int level, int layer);

	void glFramebufferTextureFaceARB(@GLenum int target, @GLenum int attachment, @GLuint int texture, int level, @GLenum int face);

}