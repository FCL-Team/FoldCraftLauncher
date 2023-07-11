/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVTextureShader {

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 *  and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev, and by the &lt;target&gt; parameter of TexEnvf, TexEnvfv,
	 *  TexEnvi, TexEnviv, GetTexEnvfv, and GetTexEnviv.
	 */
	public static final int GL_TEXTURE_SHADER_NV = 0x86DE;

	/**
	 *  When the &lt;target&gt; parameter of TexEnvf, TexEnvfv, TexEnvi, TexEnviv,
	 *  GetTexEnvfv, and GetTexEnviv is TEXTURE_SHADER_NV, then the value
	 *  of &lt;pname&gt; may be:
	 */
	public static final int GL_RGBA_UNSIGNED_DOT_PRODUCT_MAPPING_NV = 0x86D9,
		GL_SHADER_OPERATION_NV = 0x86DF,
		GL_OFFSET_TEXTURE_SCALE_NV = 0x86E2,
		GL_OFFSET_TEXTURE_BIAS_NV = 0x86E3,
		GL_OFFSET_TEXTURE_2D_SCALE_NV = 0x86E2,
		GL_OFFSET_TEXTURE_2D_BIAS_NV = 0x86E3,
		GL_PREVIOUS_TEXTURE_INPUT_NV = 0x86E4;

	/**
	 *  When the &lt;target&gt; parameter of TexEnvfv, TexEnviv, GetTexEnvfv, and
	 *  GetTexEnviv is TEXTURE_SHADER_NV, then the value of &lt;pname&gt; may be:
	 */
	public static final int GL_CULL_MODES_NV = 0x86E0,
		GL_OFFSET_TEXTURE_MATRIX_NV = 0x86E1,
		GL_OFFSET_TEXTURE_2D_MATRIX_NV = 0x86E1,
		GL_CONST_EYE_NV = 0x86E5;

	/**
	 *  When the &lt;target&gt; parameter GetTexEnvfv and GetTexEnviv is
	 *  TEXTURE_SHADER_NV, then the value of &lt;pname&gt; may be:
	 */
	public static final int GL_SHADER_CONSISTENT_NV = 0x86DD;

	/**
	 *  When the &lt;target&gt; and &lt;pname&gt; parameters of TexEnvf, TexEnvfv,
	 *  TexEnvi, and TexEnviv are TEXTURE_SHADER_NV and SHADER_OPERATION_NV
	 *  respectively, then the value of &lt;param&gt; or the value pointed to by
	 *  &lt;params&gt; may be:
	 */
	public static final int GL_PASS_THROUGH_NV = 0x86E6,
		GL_CULL_FRAGMENT_NV = 0x86E7,
		GL_OFFSET_TEXTURE_2D_NV = 0x86E8,
		GL_OFFSET_TEXTURE_RECTANGLE_NV = 0x864C,
		GL_OFFSET_TEXTURE_RECTANGLE_SCALE_NV = 0x864D,
		GL_DEPENDENT_AR_TEXTURE_2D_NV = 0x86E9,
		GL_DEPENDENT_GB_TEXTURE_2D_NV = 0x86EA,
		GL_DOT_PRODUCT_NV = 0x86EC,
		GL_DOT_PRODUCT_DEPTH_REPLACE_NV = 0x86ED,
		GL_DOT_PRODUCT_TEXTURE_2D_NV = 0x86EE,
		GL_DOT_PRODUCT_TEXTURE_RECTANGLE_NV = 0x864E,
		GL_DOT_PRODUCT_TEXTURE_CUBE_MAP_NV = 0x86F0,
		GL_DOT_PRODUCT_DIFFUSE_CUBE_MAP_NV = 0x86F1,
		GL_DOT_PRODUCT_REFLECT_CUBE_MAP_NV = 0x86F2,
		GL_DOT_PRODUCT_CONST_EYE_REFLECT_CUBE_MAP_NV = 0x86F3;

	/**
	 *  Accepted by the &lt;format&gt; parameter of GetTexImage, TexImage1D,
	 *  TexImage2D, TexSubImage1D, and TexSubImage2D.
	 */
	public static final int GL_HILO_NV = 0x86F4,
		GL_DSDT_NV = 0x86F5,
		GL_DSDT_MAG_NV = 0x86F6,
		GL_DSDT_MAG_VIB_NV = 0x86F7;

	/**
	 *  Accepted by the &lt;type&gt; parameter of GetTexImage, TexImage1D,
	 *  TexImage2D, TexSubImage1D, and TexSubImage2D.
	 */
	public static final int GL_UNSIGNED_INT_S8_S8_8_8_NV = 0x86DA,
		GL_UNSIGNED_INT_8_8_S8_S8_REV_NV = 0x86DB;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of CopyTexImage1D,
	 *  CopyTexImage2D, TexImage1D, and TexImage2D.
	 */
	public static final int GL_SIGNED_RGBA_NV = 0x86FB,
		GL_SIGNED_RGBA8_NV = 0x86FC,
		GL_SIGNED_RGB_NV = 0x86FE,
		GL_SIGNED_RGB8_NV = 0x86FF,
		GL_SIGNED_LUMINANCE_NV = 0x8701,
		GL_SIGNED_LUMINANCE8_NV = 0x8702,
		GL_SIGNED_LUMINANCE_ALPHA_NV = 0x8703,
		GL_SIGNED_LUMINANCE8_ALPHA8_NV = 0x8704,
		GL_SIGNED_ALPHA_NV = 0x8705,
		GL_SIGNED_ALPHA8_NV = 0x8706,
		GL_SIGNED_INTENSITY_NV = 0x8707,
		GL_SIGNED_INTENSITY8_NV = 0x8708,
		GL_SIGNED_RGB_UNSIGNED_ALPHA_NV = 0x870C,
		GL_SIGNED_RGB8_UNSIGNED_ALPHA8_NV = 0x870D;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage1D and
	 *  TexImage2D.
	 */
	public static final int GL_HILO16_NV = 0x86F8,
		GL_SIGNED_HILO_NV = 0x86F9,
		GL_SIGNED_HILO16_NV = 0x86FA,
		GL_DSDT8_NV = 0x8709,
		GL_DSDT8_MAG8_NV = 0x870A,
		GL_DSDT_MAG_INTENSITY_NV = 0x86DC,
		GL_DSDT8_MAG8_INTENSITY8_NV = 0x870B;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, GetDoublev, PixelTransferf, and PixelTransferi.
	 */
	public static final int GL_HI_SCALE_NV = 0x870E,
		GL_LO_SCALE_NV = 0x870F,
		GL_DS_SCALE_NV = 0x8710,
		GL_DT_SCALE_NV = 0x8711,
		GL_MAGNITUDE_SCALE_NV = 0x8712,
		GL_VIBRANCE_SCALE_NV = 0x8713,
		GL_HI_BIAS_NV = 0x8714,
		GL_LO_BIAS_NV = 0x8715,
		GL_DS_BIAS_NV = 0x8716,
		GL_DT_BIAS_NV = 0x8717,
		GL_MAGNITUDE_BIAS_NV = 0x8718,
		GL_VIBRANCE_BIAS_NV = 0x8719;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of TexParameteriv, TexParameterfv,
	 *  GetTexParameterfv and GetTexParameteriv.
	 */
	public static final int GL_TEXTURE_BORDER_VALUES_NV = 0x871A;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetTexLevelParameterfv and
	 *  GetTexLevelParameteriv.
	 */
	public static final int GL_TEXTURE_HI_SIZE_NV = 0x871B,
		GL_TEXTURE_LO_SIZE_NV = 0x871C,
		GL_TEXTURE_DS_SIZE_NV = 0x871D,
		GL_TEXTURE_DT_SIZE_NV = 0x871E,
		GL_TEXTURE_MAG_SIZE_NV = 0x871F;

	private NVTextureShader() {}
}
