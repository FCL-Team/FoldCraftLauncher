/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTessellationShader {

	/**
	 *  Accepted by the &lt;mode&gt; parameter of Begin and all vertex array functions
	 *  that implicitly call Begin:
	 */
	public static final int GL_PATCHES = 0xE;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of PatchParameteri, GetBooleanv,
	 *  GetDoublev, GetFloatv, GetIntegerv, and GetInteger64v:
	 */
	public static final int GL_PATCH_VERTICES = 0x8E72;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of PatchParameterfv, GetBooleanv,
	 *  GetDoublev, GetFloatv, and GetIntegerv, and GetInteger64v:
	 */
	public static final int GL_PATCH_DEFAULT_INNER_LEVEL = 0x8E73,
		GL_PATCH_DEFAULT_OUTER_LEVEL = 0x8E74;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_TESS_CONTROL_OUTPUT_VERTICES = 0x8E75,
		GL_TESS_GEN_MODE = 0x8E76,
		GL_TESS_GEN_SPACING = 0x8E77,
		GL_TESS_GEN_VERTEX_ORDER = 0x8E78,
		GL_TESS_GEN_POINT_MODE = 0x8E79;

	/**
	 * Returned by GetProgramiv when &lt;pname&gt; is TESS_GEN_MODE: 
	 */
	public static final int GL_ISOLINES = 0x8E7A;

	/**
	 * Returned by GetProgramiv when &lt;pname&gt; is TESS_GEN_SPACING: 
	 */
	public static final int GL_FRACTIONAL_ODD = 0x8E7B,
		GL_FRACTIONAL_EVEN = 0x8E7C;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetFloatv,
	 *  GetIntegerv, and GetInteger64v:
	 */
	public static final int GL_MAX_PATCH_VERTICES = 0x8E7D,
		GL_MAX_TESS_GEN_LEVEL = 0x8E7E,
		GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E7F,
		GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E80,
		GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 0x8E81,
		GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 0x8E82,
		GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 0x8E83,
		GL_MAX_TESS_PATCH_COMPONENTS = 0x8E84,
		GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 0x8E85,
		GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 0x8E86,
		GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 0x8E89,
		GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 0x8E8A,
		GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 0x886C,
		GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 0x886D,
		GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E1E,
		GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E1F;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformBlockiv: 
	 */
	public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 0x84F0,
		GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x84F1;

	/**
	 *  Accepted by the &lt;type&gt; parameter of CreateShader and returned by the
	 *  &lt;params&gt; parameter of GetShaderiv:
	 */
	public static final int GL_TESS_EVALUATION_SHADER = 0x8E87,
		GL_TESS_CONTROL_SHADER = 0x8E88;

	private ARBTessellationShader() {}

	public static void glPatchParameteri(int pname, int value) {
		GL40.glPatchParameteri(pname, value);
	}

	public static void glPatchParameter(int pname, FloatBuffer values) {
		GL40.glPatchParameter(pname, values);
	}
}
