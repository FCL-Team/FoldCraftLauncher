/*
 * Copyright (c) 2002-2011 LWJGL Project
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
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.IntBuffer;

@Extension(postfix = "")
public interface ARB_shader_atomic_counters {

	/** Accepted by the &lt;target&gt; parameter of BindBufferBase and BindBufferRange: */
	int GL_ATOMIC_COUNTER_BUFFER = 0x92C0;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleani_v, GetIntegeri_v,
	 * GetFloati_v, GetDoublei_v, GetInteger64i_v, GetBooleanv, GetIntegerv,
	 * GetInteger64v, GetFloatv, GetDoublev, and GetActiveAtomicCounterBufferiv:
	 */
	int GL_ATOMIC_COUNTER_BUFFER_BINDING = 0x92C1;

	/** Accepted by the &lt;pname&gt; parameter of GetIntegeri_64v: */
	int GL_ATOMIC_COUNTER_BUFFER_START = 0x92C2,
		GL_ATOMIC_COUNTER_BUFFER_SIZE  = 0x92C3;

	/** Accepted by the &lt;pname&gt; parameter of GetActiveAtomicCounterBufferiv: */
	int GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE                            = 0x92C4,
		GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS               = 0x92C5,
		GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES        = 0x92C6,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER          = 0x92C7,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER    = 0x92C8,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x92C9,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER        = 0x92CA,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER        = 0x92CB;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS          = 0x92CC,
		GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS    = 0x92CD,
		GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 0x92CE,
		GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS        = 0x92CF,
		GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS        = 0x92D0,
		GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS        = 0x92D1,
		GL_MAX_VERTEX_ATOMIC_COUNTERS                 = 0x92D2,
		GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS           = 0x92D3,
		GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS        = 0x92D4,
		GL_MAX_GEOMETRY_ATOMIC_COUNTERS               = 0x92D5,
		GL_MAX_FRAGMENT_ATOMIC_COUNTERS               = 0x92D6,
		GL_MAX_COMBINED_ATOMIC_COUNTERS               = 0x92D7,
		GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE             = 0x92D8,
		GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS         = 0x92DC;

	/** Accepted by the &lt;pname&gt; parameter of GetProgramiv: */
	int GL_ACTIVE_ATOMIC_COUNTER_BUFFERS = 0x92D9;

	/** Accepted by the &lt;pname&gt; parameter of GetActiveUniformsiv: */
	int GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX = 0x92DA;

	/** Returned in &lt;params&gt; by GetActiveUniform and GetActiveUniformsiv: */
	int GL_UNSIGNED_INT_ATOMIC_COUNTER = 0x92DB;

	@StripPostfix("params")
	@Reuse("GL42")
	void glGetActiveAtomicCounterBufferiv(@GLuint int program, @GLuint int bufferIndex, @GLenum int pname, @Check("1") @OutParameter IntBuffer params);

	@Alternate("glGetActiveAtomicCounterBufferiv")
	@StripPostfix("params")
	@GLreturn("params")
	@Reuse("GL42")
	void glGetActiveAtomicCounterBufferiv2(@GLuint int program, @GLuint int bufferIndex, @GLenum int pname, @OutParameter IntBuffer params);

}