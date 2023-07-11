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

public interface NV_texture_shader {

	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 * and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * and GetDoublev, and by the &lt;target&gt; parameter of TexEnvf, TexEnvfv,
	 * TexEnvi, TexEnviv, GetTexEnvfv, and GetTexEnviv.
	 */
	int GL_TEXTURE_SHADER_NV = 0x86DE;

	/**
	 * When the &lt;target&gt; parameter of TexEnvf, TexEnvfv, TexEnvi, TexEnviv,
	 * GetTexEnvfv, and GetTexEnviv is TEXTURE_SHADER_NV, then the value
	 * of &lt;pname&gt; may be:
	 */
	int GL_RGBA_UNSIGNED_DOT_PRODUCT_MAPPING_NV = 0x86D9;
	int GL_SHADER_OPERATION_NV = 0x86DF;
	int GL_OFFSET_TEXTURE_SCALE_NV = 0x86E2;
	int GL_OFFSET_TEXTURE_BIAS_NV = 0x86E3;
	int GL_OFFSET_TEXTURE_2D_SCALE_NV = GL_OFFSET_TEXTURE_SCALE_NV;
	int GL_OFFSET_TEXTURE_2D_BIAS_NV = GL_OFFSET_TEXTURE_BIAS_NV;
	int GL_PREVIOUS_TEXTURE_INPUT_NV = 0x86E4;

	/**
	 * When the &lt;target&gt; parameter of TexEnvfv, TexEnviv, GetTexEnvfv, and
	 * GetTexEnviv is TEXTURE_SHADER_NV, then the value of &lt;pname&gt; may be:
	 */
	int GL_CULL_MODES_NV = 0x86E0;
	int GL_OFFSET_TEXTURE_MATRIX_NV = 0x86E1;
	int GL_OFFSET_TEXTURE_2D_MATRIX_NV = GL_OFFSET_TEXTURE_MATRIX_NV;
	int GL_CONST_EYE_NV = 0x86E5;

	/**
	 * When the &lt;target&gt; parameter GetTexEnvfv and GetTexEnviv is
	 * TEXTURE_SHADER_NV, then the value of &lt;pname&gt; may be:
	 */
	int GL_SHADER_CONSISTENT_NV = 0x86DD;

	/**
	 * When the &lt;target&gt; and &lt;pname&gt; parameters of TexEnvf, TexEnvfv,
	 * TexEnvi, and TexEnviv are TEXTURE_SHADER_NV and SHADER_OPERATION_NV
	 * respectively, then the value of &lt;param&gt; or the value pointed to by
	 * &lt;params&gt; may be:
	 */
	int GL_PASS_THROUGH_NV = 0x86E6;
	int GL_CULL_FRAGMENT_NV = 0x86E7;

	int GL_OFFSET_TEXTURE_2D_NV = 0x86E8;
	int GL_OFFSET_TEXTURE_RECTANGLE_NV = 0x864C;
	int GL_OFFSET_TEXTURE_RECTANGLE_SCALE_NV = 0x864D;
	int GL_DEPENDENT_AR_TEXTURE_2D_NV = 0x86E9;
	int GL_DEPENDENT_GB_TEXTURE_2D_NV = 0x86EA;

	int GL_DOT_PRODUCT_NV = 0x86EC;
	int GL_DOT_PRODUCT_DEPTH_REPLACE_NV = 0x86ED;
	int GL_DOT_PRODUCT_TEXTURE_2D_NV = 0x86EE;
	int GL_DOT_PRODUCT_TEXTURE_RECTANGLE_NV = 0x864E;
	int GL_DOT_PRODUCT_TEXTURE_CUBE_MAP_NV = 0x86F0;
	int GL_DOT_PRODUCT_DIFFUSE_CUBE_MAP_NV = 0x86F1;
	int GL_DOT_PRODUCT_REFLECT_CUBE_MAP_NV = 0x86F2;
	int GL_DOT_PRODUCT_CONST_EYE_REFLECT_CUBE_MAP_NV = 0x86F3;

	/**
	 * Accepted by the &lt;format&gt; parameter of GetTexImage, TexImage1D,
	 * TexImage2D, TexSubImage1D, and TexSubImage2D.
	 */
	int GL_HILO_NV = 0x86F4;
	int GL_DSDT_NV = 0x86F5;
	int GL_DSDT_MAG_NV = 0x86F6;
	int GL_DSDT_MAG_VIB_NV = 0x86F7;

	/**
	 * Accepted by the &lt;type&gt; parameter of GetTexImage, TexImage1D,
	 * TexImage2D, TexSubImage1D, and TexSubImage2D.
	 */
	int GL_UNSIGNED_INT_S8_S8_8_8_NV = 0x86DA;
	int GL_UNSIGNED_INT_8_8_S8_S8_REV_NV = 0x86DB;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of CopyTexImage1D,
	 * CopyTexImage2D, TexImage1D, and TexImage2D.
	 */
	int GL_SIGNED_RGBA_NV = 0x86FB;
	int GL_SIGNED_RGBA8_NV = 0x86FC;
	int GL_SIGNED_RGB_NV = 0x86FE;
	int GL_SIGNED_RGB8_NV = 0x86FF;
	int GL_SIGNED_LUMINANCE_NV = 0x8701;
	int GL_SIGNED_LUMINANCE8_NV = 0x8702;
	int GL_SIGNED_LUMINANCE_ALPHA_NV = 0x8703;
	int GL_SIGNED_LUMINANCE8_ALPHA8_NV = 0x8704;
	int GL_SIGNED_ALPHA_NV = 0x8705;
	int GL_SIGNED_ALPHA8_NV = 0x8706;
	int GL_SIGNED_INTENSITY_NV = 0x8707;
	int GL_SIGNED_INTENSITY8_NV = 0x8708;
	int GL_SIGNED_RGB_UNSIGNED_ALPHA_NV = 0x870C;
	int GL_SIGNED_RGB8_UNSIGNED_ALPHA8_NV = 0x870D;

	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D and
	 * TexImage2D.
	 */
	int GL_HILO16_NV = 0x86F8;
	int GL_SIGNED_HILO_NV = 0x86F9;
	int GL_SIGNED_HILO16_NV = 0x86FA;
	int GL_DSDT8_NV = 0x8709;
	int GL_DSDT8_MAG8_NV = 0x870A;
	int GL_DSDT_MAG_INTENSITY_NV = 0x86DC;
	int GL_DSDT8_MAG8_INTENSITY8_NV = 0x870B;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, GetDoublev, PixelTransferf, and PixelTransferi.
	 */
	int GL_HI_SCALE_NV = 0x870E;
	int GL_LO_SCALE_NV = 0x870F;
	int GL_DS_SCALE_NV = 0x8710;
	int GL_DT_SCALE_NV = 0x8711;
	int GL_MAGNITUDE_SCALE_NV = 0x8712;
	int GL_VIBRANCE_SCALE_NV = 0x8713;
	int GL_HI_BIAS_NV = 0x8714;
	int GL_LO_BIAS_NV = 0x8715;
	int GL_DS_BIAS_NV = 0x8716;
	int GL_DT_BIAS_NV = 0x8717;
	int GL_MAGNITUDE_BIAS_NV = 0x8718;
	int GL_VIBRANCE_BIAS_NV = 0x8719;

	/**
	 * Accepted by the &lt;pname&gt; parameter of TexParameteriv, TexParameterfv,
	 * GetTexParameterfv and GetTexParameteriv.
	 */
	int GL_TEXTURE_BORDER_VALUES_NV = 0x871A;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameterfv and
	 * GetTexLevelParameteriv.
	 */
	int GL_TEXTURE_HI_SIZE_NV = 0x871B;
	int GL_TEXTURE_LO_SIZE_NV = 0x871C;
	int GL_TEXTURE_DS_SIZE_NV = 0x871D;
	int GL_TEXTURE_DT_SIZE_NV = 0x871E;
	int GL_TEXTURE_MAG_SIZE_NV = 0x871F;
}