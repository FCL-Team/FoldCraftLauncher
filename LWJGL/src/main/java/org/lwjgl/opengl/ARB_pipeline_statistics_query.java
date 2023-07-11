/*
 * Copyright (c) 2002-2014 LWJGL Project
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

public interface ARB_pipeline_statistics_query {

	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, GetQueryiv,
	 * BeginQueryIndexed, EndQueryIndexed and GetQueryIndexediv:
	 */
	int GL_VERTICES_SUBMITTED_ARB                 = 0x82EE,
		GL_PRIMITIVES_SUBMITTED_ARB               = 0x82EF,
		GL_VERTEX_SHADER_INVOCATIONS_ARB          = 0x82F0,
		GL_TESS_CONTROL_SHADER_PATCHES_ARB        = 0x82F1,
		GL_TESS_EVALUATION_SHADER_INVOCATIONS_ARB = 0x82F2,
		GL_GEOMETRY_SHADER_INVOCATIONS            = 0x887F,
		GL_GEOMETRY_SHADER_PRIMITIVES_EMITTED_ARB = 0x82F3,
		GL_FRAGMENT_SHADER_INVOCATIONS_ARB        = 0x82F4,
		GL_COMPUTE_SHADER_INVOCATIONS_ARB         = 0x82F5,
		GL_CLIPPING_INPUT_PRIMITIVES_ARB          = 0x82F6,
		GL_CLIPPING_OUTPUT_PRIMITIVES_ARB         = 0x82F7;

}