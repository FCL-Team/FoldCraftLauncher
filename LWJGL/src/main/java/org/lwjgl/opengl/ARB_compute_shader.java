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
import org.lwjgl.util.generator.opengl.GLintptr;
import org.lwjgl.util.generator.opengl.GLuint;

public interface ARB_compute_shader {

	/**
	 * Accepted by the &lt;type&gt; parameter of CreateShader and returned in the
	 * &lt;params&gt; parameter by GetShaderiv:
	 */
	int GL_COMPUTE_SHADER = 0x91B9;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv, GetFloatv,
	 * GetDoublev and GetInteger64v:
	 */
	int GL_MAX_COMPUTE_UNIFORM_BLOCKS              = 0x91BB,
		GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS         = 0x91BC,
		GL_MAX_COMPUTE_IMAGE_UNIFORMS              = 0x91BD,
		GL_MAX_COMPUTE_SHARED_MEMORY_SIZE          = 0x8262,
		GL_MAX_COMPUTE_UNIFORM_COMPONENTS          = 0x8263,
		GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS      = 0x8264,
		GL_MAX_COMPUTE_ATOMIC_COUNTERS             = 0x8265,
		GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 0x8266,
		GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS      = 0x90EB;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleani_v,
	 * GetFloati_v, GetDoublei_v and GetInteger64i_v:
	 */

	int GL_MAX_COMPUTE_WORK_GROUP_COUNT = 0x91BE,
		GL_MAX_COMPUTE_WORK_GROUP_SIZE  = 0x91BF;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramiv: */
	int GL_COMPUTE_WORK_GROUP_SIZE = 0x8267;

	/** Accepted by the &lt;pname&gt; parameter of GetActiveUniformBlockiv: */
	int GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER = 0x90EC;

	/** Accepted by the &lt;pname&gt; parameter of GetActiveAtomicCounterBufferiv: */
	int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER = 0x90ED;

	/**
	 * Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 * BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and
	 * GetBufferPointerv:
	 */
	int GL_DISPATCH_INDIRECT_BUFFER = 0x90EE;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetIntegerv, GetBooleanv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_DISPATCH_INDIRECT_BUFFER_BINDING = 0x90EF;

	/** Accepted by the &lt;stages&gt; parameter of UseProgramStages: */
	int GL_COMPUTE_SHADER_BIT = 0x00000020;

	@Reuse("GL43")
	void glDispatchCompute(@GLuint int num_groups_x,
	                       @GLuint int num_groups_y,
	                       @GLuint int num_groups_z);

	@Reuse("GL43")
	void glDispatchComputeIndirect(@GLintptr long indirect);

}