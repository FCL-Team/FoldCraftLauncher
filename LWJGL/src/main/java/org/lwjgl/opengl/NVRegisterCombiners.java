/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVRegisterCombiners {

	public static final int GL_REGISTER_COMBINERS_NV = 0x8522,
		GL_COMBINER0_NV = 0x8550,
		GL_COMBINER1_NV = 0x8551,
		GL_COMBINER2_NV = 0x8552,
		GL_COMBINER3_NV = 0x8553,
		GL_COMBINER4_NV = 0x8554,
		GL_COMBINER5_NV = 0x8555,
		GL_COMBINER6_NV = 0x8556,
		GL_COMBINER7_NV = 0x8557,
		GL_VARIABLE_A_NV = 0x8523,
		GL_VARIABLE_B_NV = 0x8524,
		GL_VARIABLE_C_NV = 0x8525,
		GL_VARIABLE_D_NV = 0x8526,
		GL_VARIABLE_E_NV = 0x8527,
		GL_VARIABLE_F_NV = 0x8528,
		GL_VARIABLE_G_NV = 0x8529,
		GL_CONSTANT_COLOR0_NV = 0x852A,
		GL_CONSTANT_COLOR1_NV = 0x852B,
		GL_PRIMARY_COLOR_NV = 0x852C,
		GL_SECONDARY_COLOR_NV = 0x852D,
		GL_SPARE0_NV = 0x852E,
		GL_SPARE1_NV = 0x852F,
		GL_UNSIGNED_IDENTITY_NV = 0x8536,
		GL_UNSIGNED_INVERT_NV = 0x8537,
		GL_EXPAND_NORMAL_NV = 0x8538,
		GL_EXPAND_NEGATE_NV = 0x8539,
		GL_HALF_BIAS_NORMAL_NV = 0x853A,
		GL_HALF_BIAS_NEGATE_NV = 0x853B,
		GL_SIGNED_IDENTITY_NV = 0x853C,
		GL_SIGNED_NEGATE_NV = 0x853D,
		GL_E_TIMES_F_NV = 0x8531,
		GL_SPARE0_PLUS_SECONDARY_COLOR_NV = 0x8532,
		GL_SCALE_BY_TWO_NV = 0x853E,
		GL_SCALE_BY_FOUR_NV = 0x853F,
		GL_SCALE_BY_ONE_HALF_NV = 0x8540,
		GL_BIAS_BY_NEGATIVE_ONE_HALF_NV = 0x8541,
		GL_DISCARD_NV = 0x8530,
		GL_COMBINER_INPUT_NV = 0x8542,
		GL_COMBINER_MAPPING_NV = 0x8543,
		GL_COMBINER_COMPONENT_USAGE_NV = 0x8544,
		GL_COMBINER_AB_DOT_PRODUCT_NV = 0x8545,
		GL_COMBINER_CD_DOT_PRODUCT_NV = 0x8546,
		GL_COMBINER_MUX_SUM_NV = 0x8547,
		GL_COMBINER_SCALE_NV = 0x8548,
		GL_COMBINER_BIAS_NV = 0x8549,
		GL_COMBINER_AB_OUTPUT_NV = 0x854A,
		GL_COMBINER_CD_OUTPUT_NV = 0x854B,
		GL_COMBINER_SUM_OUTPUT_NV = 0x854C,
		GL_NUM_GENERAL_COMBINERS_NV = 0x854E,
		GL_COLOR_SUM_CLAMP_NV = 0x854F,
		GL_MAX_GENERAL_COMBINERS_NV = 0x854D;

	private NVRegisterCombiners() {}

	public static void glCombinerParameterfNV(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCombinerParameterfNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCombinerParameterfNV(pname, param, function_pointer);
	}
	static native void nglCombinerParameterfNV(int pname, float param, long function_pointer);

	public static void glCombinerParameterNV(int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCombinerParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglCombinerParameterfvNV(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglCombinerParameterfvNV(int pname, long params, long function_pointer);

	public static void glCombinerParameteriNV(int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCombinerParameteriNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCombinerParameteriNV(pname, param, function_pointer);
	}
	static native void nglCombinerParameteriNV(int pname, int param, long function_pointer);

	public static void glCombinerParameterNV(int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCombinerParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglCombinerParameterivNV(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglCombinerParameterivNV(int pname, long params, long function_pointer);

	public static void glCombinerInputNV(int stage, int portion, int variable, int input, int mapping, int componentUsage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCombinerInputNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCombinerInputNV(stage, portion, variable, input, mapping, componentUsage, function_pointer);
	}
	static native void nglCombinerInputNV(int stage, int portion, int variable, int input, int mapping, int componentUsage, long function_pointer);

	public static void glCombinerOutputNV(int stage, int portion, int abOutput, int cdOutput, int sumOutput, int scale, int bias, boolean abDotProduct, boolean cdDotProduct, boolean muxSum) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCombinerOutputNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCombinerOutputNV(stage, portion, abOutput, cdOutput, sumOutput, scale, bias, abDotProduct, cdDotProduct, muxSum, function_pointer);
	}
	static native void nglCombinerOutputNV(int stage, int portion, int abOutput, int cdOutput, int sumOutput, int scale, int bias, boolean abDotProduct, boolean cdDotProduct, boolean muxSum, long function_pointer);

	public static void glFinalCombinerInputNV(int variable, int input, int mapping, int componentUsage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFinalCombinerInputNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFinalCombinerInputNV(variable, input, mapping, componentUsage, function_pointer);
	}
	static native void nglFinalCombinerInputNV(int variable, int input, int mapping, int componentUsage, long function_pointer);

	public static void glGetCombinerInputParameterNV(int stage, int portion, int variable, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerInputParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetCombinerInputParameterfvNV(stage, portion, variable, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetCombinerInputParameterfvNV(int stage, int portion, int variable, int pname, long params, long function_pointer);

	/** Overloads glGetCombinerInputParameterfvNV. */
	public static float glGetCombinerInputParameterfNV(int stage, int portion, int variable, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerInputParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetCombinerInputParameterfvNV(stage, portion, variable, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetCombinerInputParameterNV(int stage, int portion, int variable, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerInputParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetCombinerInputParameterivNV(stage, portion, variable, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetCombinerInputParameterivNV(int stage, int portion, int variable, int pname, long params, long function_pointer);

	/** Overloads glGetCombinerInputParameterivNV. */
	public static int glGetCombinerInputParameteriNV(int stage, int portion, int variable, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerInputParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetCombinerInputParameterivNV(stage, portion, variable, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetCombinerOutputParameterNV(int stage, int portion, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerOutputParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetCombinerOutputParameterfvNV(stage, portion, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetCombinerOutputParameterfvNV(int stage, int portion, int pname, long params, long function_pointer);

	/** Overloads glGetCombinerOutputParameterfvNV. */
	public static float glGetCombinerOutputParameterfNV(int stage, int portion, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerOutputParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetCombinerOutputParameterfvNV(stage, portion, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetCombinerOutputParameterNV(int stage, int portion, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerOutputParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetCombinerOutputParameterivNV(stage, portion, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetCombinerOutputParameterivNV(int stage, int portion, int pname, long params, long function_pointer);

	/** Overloads glGetCombinerOutputParameterivNV. */
	public static int glGetCombinerOutputParameteriNV(int stage, int portion, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCombinerOutputParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetCombinerOutputParameterivNV(stage, portion, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetFinalCombinerInputParameterNV(int variable, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFinalCombinerInputParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetFinalCombinerInputParameterfvNV(variable, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetFinalCombinerInputParameterfvNV(int variable, int pname, long params, long function_pointer);

	/** Overloads glGetFinalCombinerInputParameterfvNV. */
	public static float glGetFinalCombinerInputParameterfNV(int variable, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFinalCombinerInputParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetFinalCombinerInputParameterfvNV(variable, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetFinalCombinerInputParameterNV(int variable, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFinalCombinerInputParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetFinalCombinerInputParameterivNV(variable, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetFinalCombinerInputParameterivNV(int variable, int pname, long params, long function_pointer);

	/** Overloads glGetFinalCombinerInputParameterivNV. */
	public static int glGetFinalCombinerInputParameteriNV(int variable, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFinalCombinerInputParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetFinalCombinerInputParameterivNV(variable, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
