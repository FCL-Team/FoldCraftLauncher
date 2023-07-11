/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBShaderImageLoadStore {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, GetDoublev, and GetInteger64v:
	 */
	public static final int GL_MAX_IMAGE_UNITS = 0x8F38,
		GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS = 0x8F39,
		GL_MAX_IMAGE_SAMPLES = 0x906D,
		GL_MAX_VERTEX_IMAGE_UNIFORMS = 0x90CA,
		GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS = 0x90CB,
		GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS = 0x90CC,
		GL_MAX_GEOMETRY_IMAGE_UNIFORMS = 0x90CD,
		GL_MAX_FRAGMENT_IMAGE_UNIFORMS = 0x90CE,
		GL_MAX_COMBINED_IMAGE_UNIFORMS = 0x90CF;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetIntegeri_v and GetBooleani_v: 
	 */
	public static final int GL_IMAGE_BINDING_NAME = 0x8F3A,
		GL_IMAGE_BINDING_LEVEL = 0x8F3B,
		GL_IMAGE_BINDING_LAYERED = 0x8F3C,
		GL_IMAGE_BINDING_LAYER = 0x8F3D,
		GL_IMAGE_BINDING_ACCESS = 0x8F3E,
		GL_IMAGE_BINDING_FORMAT = 0x906E;

	/**
	 * Accepted by the &lt;barriers&gt; parameter of MemoryBarrier: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = 0x1,
		GL_ELEMENT_ARRAY_BARRIER_BIT = 0x2,
		GL_UNIFORM_BARRIER_BIT = 0x4,
		GL_TEXTURE_FETCH_BARRIER_BIT = 0x8,
		GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = 0x20,
		GL_COMMAND_BARRIER_BIT = 0x40,
		GL_PIXEL_BUFFER_BARRIER_BIT = 0x80,
		GL_TEXTURE_UPDATE_BARRIER_BIT = 0x100,
		GL_BUFFER_UPDATE_BARRIER_BIT = 0x200,
		GL_FRAMEBUFFER_BARRIER_BIT = 0x400,
		GL_TRANSFORM_FEEDBACK_BARRIER_BIT = 0x800,
		GL_ATOMIC_COUNTER_BARRIER_BIT = 0x1000,
		GL_ALL_BARRIER_BITS = 0xFFFFFFFF;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_IMAGE_1D = 0x904C,
		GL_IMAGE_2D = 0x904D,
		GL_IMAGE_3D = 0x904E,
		GL_IMAGE_2D_RECT = 0x904F,
		GL_IMAGE_CUBE = 0x9050,
		GL_IMAGE_BUFFER = 0x9051,
		GL_IMAGE_1D_ARRAY = 0x9052,
		GL_IMAGE_2D_ARRAY = 0x9053,
		GL_IMAGE_CUBE_MAP_ARRAY = 0x9054,
		GL_IMAGE_2D_MULTISAMPLE = 0x9055,
		GL_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9056,
		GL_INT_IMAGE_1D = 0x9057,
		GL_INT_IMAGE_2D = 0x9058,
		GL_INT_IMAGE_3D = 0x9059,
		GL_INT_IMAGE_2D_RECT = 0x905A,
		GL_INT_IMAGE_CUBE = 0x905B,
		GL_INT_IMAGE_BUFFER = 0x905C,
		GL_INT_IMAGE_1D_ARRAY = 0x905D,
		GL_INT_IMAGE_2D_ARRAY = 0x905E,
		GL_INT_IMAGE_CUBE_MAP_ARRAY = 0x905F,
		GL_INT_IMAGE_2D_MULTISAMPLE = 0x9060,
		GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9061,
		GL_UNSIGNED_INT_IMAGE_1D = 0x9062,
		GL_UNSIGNED_INT_IMAGE_2D = 0x9063,
		GL_UNSIGNED_INT_IMAGE_3D = 0x9064,
		GL_UNSIGNED_INT_IMAGE_2D_RECT = 0x9065,
		GL_UNSIGNED_INT_IMAGE_CUBE = 0x9066,
		GL_UNSIGNED_INT_IMAGE_BUFFER = 0x9067,
		GL_UNSIGNED_INT_IMAGE_1D_ARRAY = 0x9068,
		GL_UNSIGNED_INT_IMAGE_2D_ARRAY = 0x9069,
		GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY = 0x906A,
		GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE = 0x906B,
		GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x906C;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetTexParameteriv, GetTexParameterfv,
	 *  GetTexParameterIiv, and GetTexParameterIuiv:
	 */
	public static final int GL_IMAGE_FORMAT_COMPATIBILITY_TYPE = 0x90C7;

	/**
	 *  Returned in the &lt;data&gt; parameter of GetTexParameteriv, GetTexParameterfv,
	 *  GetTexParameterIiv, and GetTexParameterIuiv when &lt;value&gt; is
	 *  IMAGE_FORMAT_COMPATIBILITY_TYPE:
	 */
	public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE = 0x90C8,
		IMAGE_FORMAT_COMPATIBILITY_BY_CLASS = 0x90C9;

	private ARBShaderImageLoadStore() {}

	public static void glBindImageTexture(int unit, int texture, int level, boolean layered, int layer, int access, int format) {
		GL42.glBindImageTexture(unit, texture, level, layered, layer, access, format);
	}

	public static void glMemoryBarrier(int barriers) {
		GL42.glMemoryBarrier(barriers);
	}
}
