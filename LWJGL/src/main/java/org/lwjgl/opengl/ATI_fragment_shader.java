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
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLuint;

import java.nio.*;

public interface ATI_fragment_shader {

	int GL_FRAGMENT_SHADER_ATI = 0x8920;
	int GL_REG_0_ATI = 0x8921;
	int GL_REG_1_ATI = 0x8922;
	int GL_REG_2_ATI = 0x8923;
	int GL_REG_3_ATI = 0x8924;
	int GL_REG_4_ATI = 0x8925;
	int GL_REG_5_ATI = 0x8926;
	int GL_REG_6_ATI = 0x8927;
	int GL_REG_7_ATI = 0x8928;
	int GL_REG_8_ATI = 0x8929;
	int GL_REG_9_ATI = 0x892A;
	int GL_REG_10_ATI = 0x892B;
	int GL_REG_11_ATI = 0x892C;
	int GL_REG_12_ATI = 0x892D;
	int GL_REG_13_ATI = 0x892E;
	int GL_REG_14_ATI = 0x892F;
	int GL_REG_15_ATI = 0x8930;
	int GL_REG_16_ATI = 0x8931;
	int GL_REG_17_ATI = 0x8932;
	int GL_REG_18_ATI = 0x8933;
	int GL_REG_19_ATI = 0x8934;
	int GL_REG_20_ATI = 0x8935;
	int GL_REG_21_ATI = 0x8936;
	int GL_REG_22_ATI = 0x8937;
	int GL_REG_23_ATI = 0x8938;
	int GL_REG_24_ATI = 0x8939;
	int GL_REG_25_ATI = 0x893A;
	int GL_REG_26_ATI = 0x893B;
	int GL_REG_27_ATI = 0x893C;
	int GL_REG_28_ATI = 0x893D;
	int GL_REG_29_ATI = 0x893E;
	int GL_REG_30_ATI = 0x893F;
	int GL_REG_31_ATI = 0x8940;
	int GL_CON_0_ATI = 0x8941;
	int GL_CON_1_ATI = 0x8942;
	int GL_CON_2_ATI = 0x8943;
	int GL_CON_3_ATI = 0x8944;
	int GL_CON_4_ATI = 0x8945;
	int GL_CON_5_ATI = 0x8946;
	int GL_CON_6_ATI = 0x8947;
	int GL_CON_7_ATI = 0x8948;
	int GL_CON_8_ATI = 0x8949;
	int GL_CON_9_ATI = 0x894A;
	int GL_CON_10_ATI = 0x894B;
	int GL_CON_11_ATI = 0x894C;
	int GL_CON_12_ATI = 0x894D;
	int GL_CON_13_ATI = 0x894E;
	int GL_CON_14_ATI = 0x894F;
	int GL_CON_15_ATI = 0x8950;
	int GL_CON_16_ATI = 0x8951;
	int GL_CON_17_ATI = 0x8952;
	int GL_CON_18_ATI = 0x8953;
	int GL_CON_19_ATI = 0x8954;
	int GL_CON_20_ATI = 0x8955;
	int GL_CON_21_ATI = 0x8956;
	int GL_CON_22_ATI = 0x8957;
	int GL_CON_23_ATI = 0x8958;
	int GL_CON_24_ATI = 0x8959;
	int GL_CON_25_ATI = 0x895A;
	int GL_CON_26_ATI = 0x895B;
	int GL_CON_27_ATI = 0x895C;
	int GL_CON_28_ATI = 0x895D;
	int GL_CON_29_ATI = 0x895E;
	int GL_CON_30_ATI = 0x895F;
	int GL_CON_31_ATI = 0x8960;
	int GL_MOV_ATI = 0x8961;
	int GL_ADD_ATI = 0x8963;
	int GL_MUL_ATI = 0x8964;
	int GL_SUB_ATI = 0x8965;
	int GL_DOT3_ATI = 0x8966;
	int GL_DOT4_ATI = 0x8967;
	int GL_MAD_ATI = 0x8968;
	int GL_LERP_ATI = 0x8969;
	int GL_CND_ATI = 0x896A;
	int GL_CND0_ATI = 0x896B;
	int GL_DOT2_ADD_ATI = 0x896C;
	int GL_SECONDARY_INTERPOLATOR_ATI = 0x896D;
	int GL_NUM_FRAGMENT_REGISTERS_ATI = 0x896E;
	int GL_NUM_FRAGMENT_CONSTANTS_ATI = 0x896F;
	int GL_NUM_PASSES_ATI = 0x8970;
	int GL_NUM_INSTRUCTIONS_PER_PASS_ATI = 0x8971;
	int GL_NUM_INSTRUCTIONS_TOTAL_ATI = 0x8972;
	int GL_NUM_INPUT_INTERPOLATOR_COMPONENTS_ATI = 0x8973;
	int GL_NUM_LOOPBACK_COMPONENTS_ATI = 0x8974;
	int GL_COLOR_ALPHA_PAIRING_ATI = 0x8975;
	int GL_SWIZZLE_STR_ATI = 0x8976;
	int GL_SWIZZLE_STQ_ATI = 0x8977;
	int GL_SWIZZLE_STR_DR_ATI = 0x8978;
	int GL_SWIZZLE_STQ_DQ_ATI = 0x8979;
	int GL_SWIZZLE_STRQ_ATI = 0x897A;
	int GL_SWIZZLE_STRQ_DQ_ATI = 0x897B;
	int GL_RED_BIT_ATI = 0x00000001;
	int GL_GREEN_BIT_ATI = 0x00000002;
	int GL_BLUE_BIT_ATI = 0x00000004;
	int GL_2X_BIT_ATI = 0x00000001;
	int GL_4X_BIT_ATI = 0x00000002;
	int GL_8X_BIT_ATI = 0x00000004;
	int GL_HALF_BIT_ATI = 0x00000008;
	int GL_QUARTER_BIT_ATI = 0x00000010;
	int GL_EIGHTH_BIT_ATI = 0x00000020;
	int GL_SATURATE_BIT_ATI = 0x00000040;
	int GL_COMP_BIT_ATI = 0x00000002;
	int GL_NEGATE_BIT_ATI = 0x00000004;
	int GL_BIAS_BIT_ATI = 0x00000008;

	@GLuint
	int glGenFragmentShadersATI(@GLuint int range);

	void glBindFragmentShaderATI(@GLuint int id);

	void glDeleteFragmentShaderATI(@GLuint int id);

	void glBeginFragmentShaderATI();

	void glEndFragmentShaderATI();

	void glPassTexCoordATI(@GLuint int dst, @GLuint int coord, @GLenum int swizzle);

	void glSampleMapATI(@GLuint int dst, @GLuint int interp, @GLenum int swizzle);

	void glColorFragmentOp1ATI(@GLenum int op, @GLuint int dst, @GLuint int dstMask, @GLuint int dstMod, @GLuint int arg1, @GLuint int arg1Rep, @GLuint int arg1Mod);

	void glColorFragmentOp2ATI(@GLenum int op, @GLuint int dst, @GLuint int dstMask, @GLuint int dstMod, @GLuint int arg1, @GLuint int arg1Rep, @GLuint int arg1Mod, @GLuint int arg2, @GLuint int arg2Rep, @GLuint int arg2Mod);

	void glColorFragmentOp3ATI(@GLenum int op, @GLuint int dst, @GLuint int dstMask, @GLuint int dstMod, @GLuint int arg1, @GLuint int arg1Rep, @GLuint int arg1Mod, @GLuint int arg2, @GLuint int arg2Rep, @GLuint int arg2Mod, @GLuint int arg3, @GLuint int arg3Rep, @GLuint int arg3Mod);

	void glAlphaFragmentOp1ATI(@GLenum int op, @GLuint int dst, @GLuint int dstMod, @GLuint int arg1, @GLuint int arg1Rep, @GLuint int arg1Mod);

	void glAlphaFragmentOp2ATI(@GLenum int op, @GLuint int dst, @GLuint int dstMod, @GLuint int arg1, @GLuint int arg1Rep, @GLuint int arg1Mod, @GLuint int arg2, @GLuint int arg2Rep, @GLuint int arg2Mod);

	void glAlphaFragmentOp3ATI(@GLenum int op, @GLuint int dst, @GLuint int dstMod, @GLuint int arg1, @GLuint int arg1Rep, @GLuint int arg1Mod, @GLuint int arg2, @GLuint int arg2Rep, @GLuint int arg2Mod, @GLuint int arg3, @GLuint int arg3Rep, @GLuint int arg3Mod);

	// TODO:is the @Check correct?
	void glSetFragmentShaderConstantATI(@GLuint int dst, @Check("4") @Const FloatBuffer pfValue);
}
