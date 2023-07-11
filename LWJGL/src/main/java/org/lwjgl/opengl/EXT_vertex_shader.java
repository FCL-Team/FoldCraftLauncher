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
import org.lwjgl.util.generator.opengl.*;

import java.nio.*;

public interface EXT_vertex_shader {
	int GL_VERTEX_SHADER_EXT = 0x8780;
	int GL_VERTEX_SHADER_BINDING_EXT = 0x8781;
	int GL_OP_INDEX_EXT = 0x8782;
	int GL_OP_NEGATE_EXT = 0x8783;
	int GL_OP_DOT3_EXT = 0x8784;
	int GL_OP_DOT4_EXT = 0x8785;
	int GL_OP_MUL_EXT = 0x8786;
	int GL_OP_ADD_EXT = 0x8787;
	int GL_OP_MADD_EXT = 0x8788;
	int GL_OP_FRAC_EXT = 0x8789;
	int GL_OP_MAX_EXT = 0x878A;
	int GL_OP_MIN_EXT = 0x878B;
	int GL_OP_SET_GE_EXT = 0x878C;
	int GL_OP_SET_LT_EXT = 0x878D;
	int GL_OP_CLAMP_EXT = 0x878E;
	int GL_OP_FLOOR_EXT = 0x878F;
	int GL_OP_ROUND_EXT = 0x8790;
	int GL_OP_EXP_BASE_2_EXT = 0x8791;
	int GL_OP_LOG_BASE_2_EXT = 0x8792;
	int GL_OP_POWER_EXT = 0x8793;
	int GL_OP_RECIP_EXT = 0x8794;
	int GL_OP_RECIP_SQRT_EXT = 0x8795;
	int GL_OP_SUB_EXT = 0x8796;
	int GL_OP_CROSS_PRODUCT_EXT = 0x8797;
	int GL_OP_MULTIPLY_MATRIX_EXT = 0x8798;
	int GL_OP_MOV_EXT = 0x8799;
	int GL_OUTPUT_VERTEX_EXT = 0x879A;
	int GL_OUTPUT_COLOR0_EXT = 0x879B;
	int GL_OUTPUT_COLOR1_EXT = 0x879C;
	int GL_OUTPUT_TEXTURE_COORD0_EXT = 0x879D;
	int GL_OUTPUT_TEXTURE_COORD1_EXT = 0x879E;
	int GL_OUTPUT_TEXTURE_COORD2_EXT = 0x879F;
	int GL_OUTPUT_TEXTURE_COORD3_EXT = 0x87A0;
	int GL_OUTPUT_TEXTURE_COORD4_EXT = 0x87A1;
	int GL_OUTPUT_TEXTURE_COORD5_EXT = 0x87A2;
	int GL_OUTPUT_TEXTURE_COORD6_EXT = 0x87A3;
	int GL_OUTPUT_TEXTURE_COORD7_EXT = 0x87A4;
	int GL_OUTPUT_TEXTURE_COORD8_EXT = 0x87A5;
	int GL_OUTPUT_TEXTURE_COORD9_EXT = 0x87A6;
	int GL_OUTPUT_TEXTURE_COORD10_EXT = 0x87A7;
	int GL_OUTPUT_TEXTURE_COORD11_EXT = 0x87A8;
	int GL_OUTPUT_TEXTURE_COORD12_EXT = 0x87A9;
	int GL_OUTPUT_TEXTURE_COORD13_EXT = 0x87AA;
	int GL_OUTPUT_TEXTURE_COORD14_EXT = 0x87AB;
	int GL_OUTPUT_TEXTURE_COORD15_EXT = 0x87AC;
	int GL_OUTPUT_TEXTURE_COORD16_EXT = 0x87AD;
	int GL_OUTPUT_TEXTURE_COORD17_EXT = 0x87AE;
	int GL_OUTPUT_TEXTURE_COORD18_EXT = 0x87AF;
	int GL_OUTPUT_TEXTURE_COORD19_EXT = 0x87B0;
	int GL_OUTPUT_TEXTURE_COORD20_EXT = 0x87B1;
	int GL_OUTPUT_TEXTURE_COORD21_EXT = 0x87B2;
	int GL_OUTPUT_TEXTURE_COORD22_EXT = 0x87B3;
	int GL_OUTPUT_TEXTURE_COORD23_EXT = 0x87B4;
	int GL_OUTPUT_TEXTURE_COORD24_EXT = 0x87B5;
	int GL_OUTPUT_TEXTURE_COORD25_EXT = 0x87B6;
	int GL_OUTPUT_TEXTURE_COORD26_EXT = 0x87B7;
	int GL_OUTPUT_TEXTURE_COORD27_EXT = 0x87B8;
	int GL_OUTPUT_TEXTURE_COORD28_EXT = 0x87B9;
	int GL_OUTPUT_TEXTURE_COORD29_EXT = 0x87BA;
	int GL_OUTPUT_TEXTURE_COORD30_EXT = 0x87BB;
	int GL_OUTPUT_TEXTURE_COORD31_EXT = 0x87BC;
	int GL_OUTPUT_FOG_EXT = 0x87BD;
	int GL_SCALAR_EXT = 0x87BE;
	int GL_VECTOR_EXT = 0x87BF;
	int GL_MATRIX_EXT = 0x87C0;
	int GL_VARIANT_EXT = 0x87C1;
	int GL_INVARIANT_EXT = 0x87C2;
	int GL_LOCAL_CONSTANT_EXT = 0x87C3;
	int GL_LOCAL_EXT = 0x87C4;
	int GL_MAX_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87C5;
	int GL_MAX_VERTEX_SHADER_VARIANTS_EXT = 0x87C6;
	int GL_MAX_VERTEX_SHADER_INVARIANTS_EXT = 0x87C7;
	int GL_MAX_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87C8;
	int GL_MAX_VERTEX_SHADER_LOCALS_EXT = 0x87C9;
	int GL_MAX_OPTIMIZED_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87CA;
	int GL_MAX_OPTIMIZED_VERTEX_SHADER_VARIANTS_EXT = 0x87CB;
	int GL_MAX_OPTIMIZED_VERTEX_SHADER_INVARIANTS_EXT = 0x87CC;
	int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87CD;
	int GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCALS_EXT = 0x87CE;
	int GL_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87CF;
	int GL_VERTEX_SHADER_VARIANTS_EXT = 0x87D0;
	int GL_VERTEX_SHADER_INVARIANTS_EXT = 0x87D1;
	int GL_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87D2;
	int GL_VERTEX_SHADER_LOCALS_EXT = 0x87D3;
	int GL_VERTEX_SHADER_OPTIMIZED_EXT = 0x87D4;
	int GL_X_EXT = 0x87D5;
	int GL_Y_EXT = 0x87D6;
	int GL_Z_EXT = 0x87D7;
	int GL_W_EXT = 0x87D8;
	int GL_NEGATIVE_X_EXT = 0x87D9;
	int GL_NEGATIVE_Y_EXT = 0x87DA;
	int GL_NEGATIVE_Z_EXT = 0x87DB;
	int GL_NEGATIVE_W_EXT = 0x87DC;
	int GL_ZERO_EXT = 0x87DD;
	int GL_ONE_EXT = 0x87DE;
	int GL_NEGATIVE_ONE_EXT = 0x87DF;
	int GL_NORMALIZED_RANGE_EXT = 0x87E0;
	int GL_FULL_RANGE_EXT = 0x87E1;
	int GL_CURRENT_VERTEX_EXT = 0x87E2;
	int GL_MVP_MATRIX_EXT = 0x87E3;
	int GL_VARIANT_VALUE_EXT = 0x87E4;
	int GL_VARIANT_DATATYPE_EXT = 0x87E5;
	int GL_VARIANT_ARRAY_STRIDE_EXT = 0x87E6;
	int GL_VARIANT_ARRAY_TYPE_EXT = 0x87E7;
	int GL_VARIANT_ARRAY_EXT = 0x87E8;
	int GL_VARIANT_ARRAY_POINTER_EXT = 0x87E9;
	int GL_INVARIANT_VALUE_EXT = 0x87EA;
	int GL_INVARIANT_DATATYPE_EXT = 0x87EB;
	int GL_LOCAL_CONSTANT_VALUE_EXT = 0x87EC;
	int GL_LOCAL_CONSTANT_DATATYPE_EXT = 0x87ED;

	void glBeginVertexShaderEXT();

	void glEndVertexShaderEXT();

	void glBindVertexShaderEXT(@GLuint int id);

	@GLuint
	int glGenVertexShadersEXT(@GLuint int range);

	void glDeleteVertexShaderEXT(@GLuint int id);

	void glShaderOp1EXT(@GLenum int op, @GLuint int res, @GLuint int arg1);

	void glShaderOp2EXT(@GLenum int op, @GLuint int res, @GLuint int arg1, @GLuint int arg2);

	void glShaderOp3EXT(@GLenum int op, @GLuint int res, @GLuint int arg1, @GLuint int arg2, @GLuint int arg3);

	void glSwizzleEXT(@GLuint int res, @GLuint int in, @GLenum int outX, @GLenum int outY, @GLenum int outZ, @GLenum int outW);

	void glWriteMaskEXT(@GLuint int res, @GLuint int in, @GLenum int outX, @GLenum int outY, @GLenum int outZ, @GLenum int outW);

	void glInsertComponentEXT(@GLuint int res, @GLuint int src, @GLuint int num);

	void glExtractComponentEXT(@GLuint int res, @GLuint int src, @GLuint int num);

	@GLuint
	int glGenSymbolsEXT(@GLenum int dataType, @GLenum int storageType, @GLenum int range, @GLuint int components);

	void glSetInvariantEXT(@GLuint int id, @AutoType("pAddr") @GLenum int type,
	                       @Check("4")
	                       @Const
	                       @GLbyte
	                       @GLubyte
	                       @GLshort
	                       @GLushort
	                       @GLint
	                       @GLuint
	                       @GLfloat
	                       @GLdouble Buffer pAddr);

	void glSetLocalConstantEXT(@GLuint int id, @AutoType("pAddr") @GLenum int type,
	                           @Check("4")
	                           @Const
	                           @GLbyte
	                           @GLubyte
	                           @GLshort
	                           @GLushort
	                           @GLint
	                           @GLuint
	                           @GLfloat
	                           @GLdouble Buffer pAddr);

	@StripPostfix("pAddr")
	void glVariantbvEXT(@GLuint int id, @Check("4") @Const ByteBuffer pAddr);

	@StripPostfix("pAddr")
	void glVariantsvEXT(@GLuint int id, @Check("4") @Const ShortBuffer pAddr);

	@StripPostfix("pAddr")
	void glVariantivEXT(@GLuint int id, @Check("4") @Const IntBuffer pAddr);

	@StripPostfix("pAddr")
	void glVariantfvEXT(@GLuint int id, @Check("4") @Const FloatBuffer pAddr);

	@StripPostfix("pAddr")
	void glVariantdvEXT(@GLuint int id, @Check("4") @Const DoubleBuffer pAddr);

	@StripPostfix("pAddr")
	void glVariantubvEXT(@GLuint int id, @Check("4") @Const @GLubyte ByteBuffer pAddr);

	@StripPostfix("pAddr")
	void glVariantusvEXT(@GLuint int id, @Check("4") @Const @GLushort ShortBuffer pAddr);

	@StripPostfix("pAddr")
	void glVariantuivEXT(@GLuint int id, @Check("4") @Const @GLuint IntBuffer pAddr);

	void glVariantPointerEXT(@GLuint int id, @AutoType("pAddr") @GLenum int type, @GLuint int stride,
	                         @CachedReference
	                         @BufferObject(BufferKind.ArrayVBO)
	                         @Check
	                         @Const
	                         @GLbyte
	                         @GLshort
	                         @GLint
	                         @GLubyte
	                         @GLushort
	                         @GLuint
	                         @GLfloat
	                         @GLdouble Buffer pAddr);

	void glEnableVariantClientStateEXT(@GLuint int id);

	void glDisableVariantClientStateEXT(@GLuint int id);

	@GLuint
	int glBindLightParameterEXT(@GLenum int light, @GLenum int value);

	@GLuint
	int glBindMaterialParameterEXT(@GLenum int face, @GLenum int value);

	@GLuint
	int glBindTexGenParameterEXT(@GLenum int unit, @GLenum int coord, @GLenum int value);

	@GLuint
	int glBindTextureUnitParameterEXT(@GLenum int unit, @GLenum int value);

	@GLuint
	int glBindParameterEXT(@GLenum int value);

	boolean glIsVariantEnabledEXT(@GLuint int id, @GLenum int cap);

	@StripPostfix("pbData")
	void glGetVariantBooleanvEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") ByteBuffer pbData);

	@StripPostfix("pbData")
	void glGetVariantIntegervEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") IntBuffer pbData);

	@StripPostfix("pbData")
	void glGetVariantFloatvEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") FloatBuffer pbData);

	@StripPostfix("pbData")
	void glGetVariantPointervEXT(@GLuint int id, @GLenum int value, @OutParameter @Result @GLvoid ByteBuffer pbData);

	@StripPostfix("pbData")
	void glGetInvariantBooleanvEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") ByteBuffer pbData);

	@StripPostfix("pbData")
	void glGetInvariantIntegervEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") IntBuffer pbData);

	@StripPostfix("pbData")
	void glGetInvariantFloatvEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") FloatBuffer pbData);

	@StripPostfix("pbData")
	void glGetLocalConstantBooleanvEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") ByteBuffer pbData);

	@StripPostfix("pbData")
	void glGetLocalConstantIntegervEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") IntBuffer pbData);

	@StripPostfix("pbData")
	void glGetLocalConstantFloatvEXT(@GLuint int id, @GLenum int value, @OutParameter @Check("4") FloatBuffer pbData);
}
