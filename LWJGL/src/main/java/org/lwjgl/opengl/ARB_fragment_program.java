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

public interface ARB_fragment_program extends ARB_program {

	/**
	 * Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, by the
	 * &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev,
	 * and by the &lt;target&gt; parameter of ProgramStringARB, BindProgramARB,
	 * ProgramEnvParameter4[df][v]ARB, ProgramLocalParameter4[df][v]ARB,
	 * GetProgramEnvParameter[df]vARB, GetProgramLocalParameter[df]vARB,
	 * GetProgramivARB and GetProgramStringARB.
	 */
	int GL_FRAGMENT_PROGRAM_ARB = 0x8804;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramivARB:
	 */
	int GL_PROGRAM_ALU_INSTRUCTIONS_ARB = 0x8805;
	int GL_PROGRAM_TEX_INSTRUCTIONS_ARB = 0x8806;
	int GL_PROGRAM_TEX_INDIRECTIONS_ARB = 0x8807;
	int GL_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB = 0x8808;
	int GL_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB = 0x8809;
	int GL_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB = 0x880A;
	int GL_MAX_PROGRAM_ALU_INSTRUCTIONS_ARB = 0x880B;
	int GL_MAX_PROGRAM_TEX_INSTRUCTIONS_ARB = 0x880C;
	int GL_MAX_PROGRAM_TEX_INDIRECTIONS_ARB = 0x880D;
	int GL_MAX_PROGRAM_NATIVE_ALU_INSTRUCTIONS_ARB = 0x880E;
	int GL_MAX_PROGRAM_NATIVE_TEX_INSTRUCTIONS_ARB = 0x880F;
	int GL_MAX_PROGRAM_NATIVE_TEX_INDIRECTIONS_ARB = 0x8810;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	int GL_MAX_TEXTURE_COORDS_ARB = 0x8871;
	int GL_MAX_TEXTURE_IMAGE_UNITS_ARB = 0x8872;
}

