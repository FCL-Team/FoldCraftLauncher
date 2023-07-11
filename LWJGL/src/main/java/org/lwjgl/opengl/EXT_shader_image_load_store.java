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

import org.lwjgl.util.generator.opengl.GLbitfield;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLuint;

public interface EXT_shader_image_load_store {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	int GL_MAX_IMAGE_UNITS_EXT = 0x8F38;
	int GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS_EXT = 0x8F39;
	int GL_MAX_IMAGE_SAMPLES_EXT = 0x906D;

	/** Accepted by the &lt;target&gt; parameter of GetIntegeri_v and GetBooleani_v: */
	int GL_IMAGE_BINDING_NAME_EXT = 0x8F3A;
	int GL_IMAGE_BINDING_LEVEL_EXT = 0x8F3B;
	int GL_IMAGE_BINDING_LAYERED_EXT = 0x8F3C;
	int GL_IMAGE_BINDING_LAYER_EXT = 0x8F3D;
	int GL_IMAGE_BINDING_ACCESS_EXT = 0x8F3E;
	int GL_IMAGE_BINDING_FORMAT_EXT = 0x906E;

	/** Accepted by the &lt;barriers&gt; parameter of MemoryBarrierEXT: */
	int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT_EXT = 0x00000001;
	int GL_ELEMENT_ARRAY_BARRIER_BIT_EXT = 0x00000002;
	int GL_UNIFORM_BARRIER_BIT_EXT = 0x00000004;
	int GL_TEXTURE_FETCH_BARRIER_BIT_EXT = 0x00000008;
	int GL_SHADER_IMAGE_ACCESS_BARRIER_BIT_EXT = 0x00000020;
	int GL_COMMAND_BARRIER_BIT_EXT = 0x00000040;
	int GL_PIXEL_BUFFER_BARRIER_BIT_EXT = 0x00000080;
	int GL_TEXTURE_UPDATE_BARRIER_BIT_EXT = 0x00000100;
	int GL_BUFFER_UPDATE_BARRIER_BIT_EXT = 0x00000200;
	int GL_FRAMEBUFFER_BARRIER_BIT_EXT = 0x00000400;
	int GL_TRANSFORM_FEEDBACK_BARRIER_BIT_EXT = 0x00000800;
	int GL_ATOMIC_COUNTER_BARRIER_BIT_EXT = 0x00001000;
	int GL_ALL_BARRIER_BITS_EXT = 0xFFFFFFFF;

	/** Returned by the &lt;type&gt; parameter of GetActiveUniform: */
	int GL_IMAGE_1D_EXT = 0x904C;
	int GL_IMAGE_2D_EXT = 0x904D;
	int GL_IMAGE_3D_EXT = 0x904E;
	int GL_IMAGE_2D_RECT_EXT = 0x904F;
	int GL_IMAGE_CUBE_EXT = 0x9050;
	int GL_IMAGE_BUFFER_EXT = 0x9051;
	int GL_IMAGE_1D_ARRAY_EXT = 0x9052;
	int GL_IMAGE_2D_ARRAY_EXT = 0x9053;
	int GL_IMAGE_CUBE_MAP_ARRAY_EXT = 0x9054;
	int GL_IMAGE_2D_MULTISAMPLE_EXT = 0x9055;
	int GL_IMAGE_2D_MULTISAMPLE_ARRAY_EXT = 0x9056;
	int GL_INT_IMAGE_1D_EXT = 0x9057;
	int GL_INT_IMAGE_2D_EXT = 0x9058;
	int GL_INT_IMAGE_3D_EXT = 0x9059;
	int GL_INT_IMAGE_2D_RECT_EXT = 0x905A;
	int GL_INT_IMAGE_CUBE_EXT = 0x905B;
	int GL_INT_IMAGE_BUFFER_EXT = 0x905C;
	int GL_INT_IMAGE_1D_ARRAY_EXT = 0x905D;
	int GL_INT_IMAGE_2D_ARRAY_EXT = 0x905E;
	int GL_INT_IMAGE_CUBE_MAP_ARRAY_EXT = 0x905F;
	int GL_INT_IMAGE_2D_MULTISAMPLE_EXT = 0x9060;
	int GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY_EXT = 0x9061;
	int GL_UNSIGNED_INT_IMAGE_1D_EXT = 0x9062;
	int GL_UNSIGNED_INT_IMAGE_2D_EXT = 0x9063;
	int GL_UNSIGNED_INT_IMAGE_3D_EXT = 0x9064;
	int GL_UNSIGNED_INT_IMAGE_2D_RECT_EXT = 0x9065;
	int GL_UNSIGNED_INT_IMAGE_CUBE_EXT = 0x9066;
	int GL_UNSIGNED_INT_IMAGE_BUFFER_EXT = 0x9067;
	int GL_UNSIGNED_INT_IMAGE_1D_ARRAY_EXT = 0x9068;
	int GL_UNSIGNED_INT_IMAGE_2D_ARRAY_EXT = 0x9069;
	int GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY_EXT = 0x906A;
	int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_EXT = 0x906B;
	int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY_EXT = 0x906C;

	void glBindImageTextureEXT(@GLuint int index, @GLuint int texture, int level, boolean layered, int layer, @GLenum int access, int format);

	void glMemoryBarrierEXT(@GLbitfield int barriers);

}