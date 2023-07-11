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

import org.lwjgl.util.generator.opengl.GLuint;

public interface ARB_compute_variable_group_size {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv, GetFloatv,
	 * GetDoublev and GetInteger64v:
	 */
	int GL_MAX_COMPUTE_VARIABLE_GROUP_INVOCATIONS_ARB = 0x9344,
		GL_MAX_COMPUTE_FIXED_GROUP_INVOCATIONS_ARB    = 0x90EB;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleani_v,
	 * GetFloati_v, GetDoublei_v and GetInteger64i_v:
	 */
	int GL_MAX_COMPUTE_VARIABLE_GROUP_SIZE_ARB = 0x9345,
		GL_MAX_COMPUTE_FIXED_GROUP_SIZE_ARB    = 0x91BF;

	void glDispatchComputeGroupSizeARB(@GLuint int num_groups_x, @GLuint int num_groups_y,
	                                   @GLuint int num_groups_z, @GLuint int group_size_x,
	                                   @GLuint int group_size_y, @GLuint int group_size_z);

}