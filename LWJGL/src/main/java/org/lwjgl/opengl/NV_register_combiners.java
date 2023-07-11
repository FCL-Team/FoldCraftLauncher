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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.Alternate;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLreturn;

import java.nio.*;

public interface NV_register_combiners {
	int GL_REGISTER_COMBINERS_NV = 0x8522;
	int GL_COMBINER0_NV = 0x8550;
	int GL_COMBINER1_NV = 0x8551;
	int GL_COMBINER2_NV = 0x8552;
	int GL_COMBINER3_NV = 0x8553;
	int GL_COMBINER4_NV = 0x8554;
	int GL_COMBINER5_NV = 0x8555;
	int GL_COMBINER6_NV = 0x8556;
	int GL_COMBINER7_NV = 0x8557;
	int GL_VARIABLE_A_NV = 0x8523;
	int GL_VARIABLE_B_NV = 0x8524;
	int GL_VARIABLE_C_NV = 0x8525;
	int GL_VARIABLE_D_NV = 0x8526;
	int GL_VARIABLE_E_NV = 0x8527;
	int GL_VARIABLE_F_NV = 0x8528;
	int GL_VARIABLE_G_NV = 0x8529;
	int GL_CONSTANT_COLOR0_NV = 0x852A;
	int GL_CONSTANT_COLOR1_NV = 0x852B;
	int GL_PRIMARY_COLOR_NV = 0x852C;
	int GL_SECONDARY_COLOR_NV = 0x852D;
	int GL_SPARE0_NV = 0x852E;
	int GL_SPARE1_NV = 0x852F;
	int GL_UNSIGNED_IDENTITY_NV = 0x8536;
	int GL_UNSIGNED_INVERT_NV = 0x8537;
	int GL_EXPAND_NORMAL_NV = 0x8538;
	int GL_EXPAND_NEGATE_NV = 0x8539;
	int GL_HALF_BIAS_NORMAL_NV = 0x853A;
	int GL_HALF_BIAS_NEGATE_NV = 0x853B;
	int GL_SIGNED_IDENTITY_NV = 0x853C;
	int GL_SIGNED_NEGATE_NV = 0x853D;
	int GL_E_TIMES_F_NV = 0x8531;
	int GL_SPARE0_PLUS_SECONDARY_COLOR_NV = 0x8532;
	int GL_SCALE_BY_TWO_NV = 0x853E;
	int GL_SCALE_BY_FOUR_NV = 0x853F;
	int GL_SCALE_BY_ONE_HALF_NV = 0x8540;
	int GL_BIAS_BY_NEGATIVE_ONE_HALF_NV = 0x8541;
	int GL_DISCARD_NV = 0x8530;
	int GL_COMBINER_INPUT_NV = 0x8542;
	int GL_COMBINER_MAPPING_NV = 0x8543;
	int GL_COMBINER_COMPONENT_USAGE_NV = 0x8544;
	int GL_COMBINER_AB_DOT_PRODUCT_NV = 0x8545;
	int GL_COMBINER_CD_DOT_PRODUCT_NV = 0x8546;
	int GL_COMBINER_MUX_SUM_NV = 0x8547;
	int GL_COMBINER_SCALE_NV = 0x8548;
	int GL_COMBINER_BIAS_NV = 0x8549;
	int GL_COMBINER_AB_OUTPUT_NV = 0x854A;
	int GL_COMBINER_CD_OUTPUT_NV = 0x854B;
	int GL_COMBINER_SUM_OUTPUT_NV = 0x854C;
	int GL_NUM_GENERAL_COMBINERS_NV = 0x854E;
	int GL_COLOR_SUM_CLAMP_NV = 0x854F;
	int GL_MAX_GENERAL_COMBINERS_NV = 0x854D;

	void glCombinerParameterfNV(@GLenum int pname, float param);

	@StripPostfix("params")
	void glCombinerParameterfvNV(@GLenum int pname, @Check("4") @Const FloatBuffer params);

	void glCombinerParameteriNV(@GLenum int pname, int param);

	@StripPostfix("params")
	void glCombinerParameterivNV(@GLenum int pname, @Check("4") @Const IntBuffer params);

	void glCombinerInputNV(@GLenum int stage, @GLenum int portion, @GLenum int variable, @GLenum int input, @GLenum int mapping, @GLenum int componentUsage);

	void glCombinerOutputNV(@GLenum int stage, @GLenum int portion, @GLenum int abOutput, @GLenum int cdOutput, @GLenum int sumOutput, @GLenum int scale, @GLenum int bias, boolean abDotProduct, boolean cdDotProduct, boolean muxSum);

	void glFinalCombinerInputNV(@GLenum int variable, @GLenum int input, @GLenum int mapping, @GLenum int componentUsage);

	@StripPostfix("params")
	void glGetCombinerInputParameterfvNV(@GLenum int stage, @GLenum int portion, @GLenum int variable, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@Alternate("glGetCombinerInputParameterfvNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetCombinerInputParameterfvNV2(@GLenum int stage, @GLenum int portion, @GLenum int variable, @GLenum int pname, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	void glGetCombinerInputParameterivNV(@GLenum int stage, @GLenum int portion, @GLenum int variable, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Alternate("glGetCombinerInputParameterivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetCombinerInputParameterivNV2(@GLenum int stage, @GLenum int portion, @GLenum int variable, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	void glGetCombinerOutputParameterfvNV(@GLenum int stage, @GLenum int portion, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@Alternate("glGetCombinerOutputParameterfvNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetCombinerOutputParameterfvNV2(@GLenum int stage, @GLenum int portion, @GLenum int pname, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	void glGetCombinerOutputParameterivNV(@GLenum int stage, @GLenum int portion, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Alternate("glGetCombinerOutputParameterivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetCombinerOutputParameterivNV2(@GLenum int stage, @GLenum int portion, @GLenum int pname, @OutParameter IntBuffer params);

	@StripPostfix("params")
	void glGetFinalCombinerInputParameterfvNV(@GLenum int variable, @GLenum int pname, @OutParameter @Check("4") FloatBuffer params);

	@Alternate("glGetFinalCombinerInputParameterfvNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetFinalCombinerInputParameterfvNV2(@GLenum int variable, @GLenum int pname, @OutParameter FloatBuffer params);

	@StripPostfix("params")
	void glGetFinalCombinerInputParameterivNV(@GLenum int variable, @GLenum int pname, @OutParameter @Check("4") IntBuffer params);

	@Alternate("glGetFinalCombinerInputParameterivNV")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetFinalCombinerInputParameterivNV2(@GLenum int variable, @GLenum int pname, @OutParameter IntBuffer params);
}
