/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ATIFragmentShader {

	public static final int GL_FRAGMENT_SHADER_ATI = 0x8920,
		GL_REG_0_ATI = 0x8921,
		GL_REG_1_ATI = 0x8922,
		GL_REG_2_ATI = 0x8923,
		GL_REG_3_ATI = 0x8924,
		GL_REG_4_ATI = 0x8925,
		GL_REG_5_ATI = 0x8926,
		GL_REG_6_ATI = 0x8927,
		GL_REG_7_ATI = 0x8928,
		GL_REG_8_ATI = 0x8929,
		GL_REG_9_ATI = 0x892A,
		GL_REG_10_ATI = 0x892B,
		GL_REG_11_ATI = 0x892C,
		GL_REG_12_ATI = 0x892D,
		GL_REG_13_ATI = 0x892E,
		GL_REG_14_ATI = 0x892F,
		GL_REG_15_ATI = 0x8930,
		GL_REG_16_ATI = 0x8931,
		GL_REG_17_ATI = 0x8932,
		GL_REG_18_ATI = 0x8933,
		GL_REG_19_ATI = 0x8934,
		GL_REG_20_ATI = 0x8935,
		GL_REG_21_ATI = 0x8936,
		GL_REG_22_ATI = 0x8937,
		GL_REG_23_ATI = 0x8938,
		GL_REG_24_ATI = 0x8939,
		GL_REG_25_ATI = 0x893A,
		GL_REG_26_ATI = 0x893B,
		GL_REG_27_ATI = 0x893C,
		GL_REG_28_ATI = 0x893D,
		GL_REG_29_ATI = 0x893E,
		GL_REG_30_ATI = 0x893F,
		GL_REG_31_ATI = 0x8940,
		GL_CON_0_ATI = 0x8941,
		GL_CON_1_ATI = 0x8942,
		GL_CON_2_ATI = 0x8943,
		GL_CON_3_ATI = 0x8944,
		GL_CON_4_ATI = 0x8945,
		GL_CON_5_ATI = 0x8946,
		GL_CON_6_ATI = 0x8947,
		GL_CON_7_ATI = 0x8948,
		GL_CON_8_ATI = 0x8949,
		GL_CON_9_ATI = 0x894A,
		GL_CON_10_ATI = 0x894B,
		GL_CON_11_ATI = 0x894C,
		GL_CON_12_ATI = 0x894D,
		GL_CON_13_ATI = 0x894E,
		GL_CON_14_ATI = 0x894F,
		GL_CON_15_ATI = 0x8950,
		GL_CON_16_ATI = 0x8951,
		GL_CON_17_ATI = 0x8952,
		GL_CON_18_ATI = 0x8953,
		GL_CON_19_ATI = 0x8954,
		GL_CON_20_ATI = 0x8955,
		GL_CON_21_ATI = 0x8956,
		GL_CON_22_ATI = 0x8957,
		GL_CON_23_ATI = 0x8958,
		GL_CON_24_ATI = 0x8959,
		GL_CON_25_ATI = 0x895A,
		GL_CON_26_ATI = 0x895B,
		GL_CON_27_ATI = 0x895C,
		GL_CON_28_ATI = 0x895D,
		GL_CON_29_ATI = 0x895E,
		GL_CON_30_ATI = 0x895F,
		GL_CON_31_ATI = 0x8960,
		GL_MOV_ATI = 0x8961,
		GL_ADD_ATI = 0x8963,
		GL_MUL_ATI = 0x8964,
		GL_SUB_ATI = 0x8965,
		GL_DOT3_ATI = 0x8966,
		GL_DOT4_ATI = 0x8967,
		GL_MAD_ATI = 0x8968,
		GL_LERP_ATI = 0x8969,
		GL_CND_ATI = 0x896A,
		GL_CND0_ATI = 0x896B,
		GL_DOT2_ADD_ATI = 0x896C,
		GL_SECONDARY_INTERPOLATOR_ATI = 0x896D,
		GL_NUM_FRAGMENT_REGISTERS_ATI = 0x896E,
		GL_NUM_FRAGMENT_CONSTANTS_ATI = 0x896F,
		GL_NUM_PASSES_ATI = 0x8970,
		GL_NUM_INSTRUCTIONS_PER_PASS_ATI = 0x8971,
		GL_NUM_INSTRUCTIONS_TOTAL_ATI = 0x8972,
		GL_NUM_INPUT_INTERPOLATOR_COMPONENTS_ATI = 0x8973,
		GL_NUM_LOOPBACK_COMPONENTS_ATI = 0x8974,
		GL_COLOR_ALPHA_PAIRING_ATI = 0x8975,
		GL_SWIZZLE_STR_ATI = 0x8976,
		GL_SWIZZLE_STQ_ATI = 0x8977,
		GL_SWIZZLE_STR_DR_ATI = 0x8978,
		GL_SWIZZLE_STQ_DQ_ATI = 0x8979,
		GL_SWIZZLE_STRQ_ATI = 0x897A,
		GL_SWIZZLE_STRQ_DQ_ATI = 0x897B,
		GL_RED_BIT_ATI = 0x1,
		GL_GREEN_BIT_ATI = 0x2,
		GL_BLUE_BIT_ATI = 0x4,
		GL_2X_BIT_ATI = 0x1,
		GL_4X_BIT_ATI = 0x2,
		GL_8X_BIT_ATI = 0x4,
		GL_HALF_BIT_ATI = 0x8,
		GL_QUARTER_BIT_ATI = 0x10,
		GL_EIGHTH_BIT_ATI = 0x20,
		GL_SATURATE_BIT_ATI = 0x40,
		GL_COMP_BIT_ATI = 0x2,
		GL_NEGATE_BIT_ATI = 0x4,
		GL_BIAS_BIT_ATI = 0x8;

	private ATIFragmentShader() {}

	public static int glGenFragmentShadersATI(int range) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenFragmentShadersATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGenFragmentShadersATI(range, function_pointer);
		return __result;
	}
	static native int nglGenFragmentShadersATI(int range, long function_pointer);

	public static void glBindFragmentShaderATI(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFragmentShaderATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindFragmentShaderATI(id, function_pointer);
	}
	static native void nglBindFragmentShaderATI(int id, long function_pointer);

	public static void glDeleteFragmentShaderATI(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteFragmentShaderATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteFragmentShaderATI(id, function_pointer);
	}
	static native void nglDeleteFragmentShaderATI(int id, long function_pointer);

	public static void glBeginFragmentShaderATI() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginFragmentShaderATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginFragmentShaderATI(function_pointer);
	}
	static native void nglBeginFragmentShaderATI(long function_pointer);

	public static void glEndFragmentShaderATI() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndFragmentShaderATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndFragmentShaderATI(function_pointer);
	}
	static native void nglEndFragmentShaderATI(long function_pointer);

	public static void glPassTexCoordATI(int dst, int coord, int swizzle) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPassTexCoordATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPassTexCoordATI(dst, coord, swizzle, function_pointer);
	}
	static native void nglPassTexCoordATI(int dst, int coord, int swizzle, long function_pointer);

	public static void glSampleMapATI(int dst, int interp, int swizzle) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSampleMapATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSampleMapATI(dst, interp, swizzle, function_pointer);
	}
	static native void nglSampleMapATI(int dst, int interp, int swizzle, long function_pointer);

	public static void glColorFragmentOp1ATI(int op, int dst, int dstMask, int dstMod, int arg1, int arg1Rep, int arg1Mod) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorFragmentOp1ATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorFragmentOp1ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod, function_pointer);
	}
	static native void nglColorFragmentOp1ATI(int op, int dst, int dstMask, int dstMod, int arg1, int arg1Rep, int arg1Mod, long function_pointer);

	public static void glColorFragmentOp2ATI(int op, int dst, int dstMask, int dstMod, int arg1, int arg1Rep, int arg1Mod, int arg2, int arg2Rep, int arg2Mod) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorFragmentOp2ATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorFragmentOp2ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, function_pointer);
	}
	static native void nglColorFragmentOp2ATI(int op, int dst, int dstMask, int dstMod, int arg1, int arg1Rep, int arg1Mod, int arg2, int arg2Rep, int arg2Mod, long function_pointer);

	public static void glColorFragmentOp3ATI(int op, int dst, int dstMask, int dstMod, int arg1, int arg1Rep, int arg1Mod, int arg2, int arg2Rep, int arg2Mod, int arg3, int arg3Rep, int arg3Mod) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorFragmentOp3ATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorFragmentOp3ATI(op, dst, dstMask, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, arg3, arg3Rep, arg3Mod, function_pointer);
	}
	static native void nglColorFragmentOp3ATI(int op, int dst, int dstMask, int dstMod, int arg1, int arg1Rep, int arg1Mod, int arg2, int arg2Rep, int arg2Mod, int arg3, int arg3Rep, int arg3Mod, long function_pointer);

	public static void glAlphaFragmentOp1ATI(int op, int dst, int dstMod, int arg1, int arg1Rep, int arg1Mod) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAlphaFragmentOp1ATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglAlphaFragmentOp1ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod, function_pointer);
	}
	static native void nglAlphaFragmentOp1ATI(int op, int dst, int dstMod, int arg1, int arg1Rep, int arg1Mod, long function_pointer);

	public static void glAlphaFragmentOp2ATI(int op, int dst, int dstMod, int arg1, int arg1Rep, int arg1Mod, int arg2, int arg2Rep, int arg2Mod) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAlphaFragmentOp2ATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglAlphaFragmentOp2ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, function_pointer);
	}
	static native void nglAlphaFragmentOp2ATI(int op, int dst, int dstMod, int arg1, int arg1Rep, int arg1Mod, int arg2, int arg2Rep, int arg2Mod, long function_pointer);

	public static void glAlphaFragmentOp3ATI(int op, int dst, int dstMod, int arg1, int arg1Rep, int arg1Mod, int arg2, int arg2Rep, int arg2Mod, int arg3, int arg3Rep, int arg3Mod) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAlphaFragmentOp3ATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglAlphaFragmentOp3ATI(op, dst, dstMod, arg1, arg1Rep, arg1Mod, arg2, arg2Rep, arg2Mod, arg3, arg3Rep, arg3Mod, function_pointer);
	}
	static native void nglAlphaFragmentOp3ATI(int op, int dst, int dstMod, int arg1, int arg1Rep, int arg1Mod, int arg2, int arg2Rep, int arg2Mod, int arg3, int arg3Rep, int arg3Mod, long function_pointer);

	public static void glSetFragmentShaderConstantATI(int dst, FloatBuffer pfValue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetFragmentShaderConstantATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pfValue, 4);
		nglSetFragmentShaderConstantATI(dst, MemoryUtil.getAddress(pfValue), function_pointer);
	}
	static native void nglSetFragmentShaderConstantATI(int dst, long pfValue, long function_pointer);
}
