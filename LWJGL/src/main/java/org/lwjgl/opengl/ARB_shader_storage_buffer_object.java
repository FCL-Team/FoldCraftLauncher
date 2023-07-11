/*
 * Copyright (c) 2002-2012 LWJGL Project
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
import org.lwjgl.util.generator.opengl.GLuint;

public interface ARB_shader_storage_buffer_object {

	/**
	 * Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 * BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and
	 * GetBufferPointerv:
	 */
	int GL_SHADER_STORAGE_BUFFER = 0x90D2;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetIntegeri_v,
	 * GetBooleanv, GetInteger64v, GetFloatv, GetDoublev, GetBooleani_v,
	 * GetIntegeri_v, GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	int GL_SHADER_STORAGE_BUFFER_BINDING = 0x90D3;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleani_v,
	 * GetIntegeri_v, GetFloati_v, GetDoublei_v, and GetInteger64i_v:
	 */
	int GL_SHADER_STORAGE_BUFFER_START = 0x90D4,
		GL_SHADER_STORAGE_BUFFER_SIZE  = 0x90D5;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS          = 0x90D6,
		GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS        = 0x90D7,
		GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS    = 0x90D8,
		GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 0x90D9,
		GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS        = 0x90DA,
		GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS         = 0x90DB,
		GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS        = 0x90DC,
		GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS        = 0x90DD,
		GL_MAX_SHADER_STORAGE_BLOCK_SIZE             = 0x90DE,
		GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT    = 0x90DF;

	/** Accepted in the &lt;barriers&gt; bitfield in glMemoryBarrier: */
	int GL_SHADER_STORAGE_BARRIER_BIT = 0x2000;

	/**
	 * Alias for the existing token
	 * MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS:
	 */
	int GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 0x8F39;

	@Reuse("GL43")
	void glShaderStorageBlockBinding(@GLuint int program, @GLuint int storageBlockIndex,
	                                 @GLuint int storageBlockBinding);
}