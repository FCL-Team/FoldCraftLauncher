/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTVertexShader {

	public static final int GL_VERTEX_SHADER_EXT = 0x8780,
		GL_VERTEX_SHADER_BINDING_EXT = 0x8781,
		GL_OP_INDEX_EXT = 0x8782,
		GL_OP_NEGATE_EXT = 0x8783,
		GL_OP_DOT3_EXT = 0x8784,
		GL_OP_DOT4_EXT = 0x8785,
		GL_OP_MUL_EXT = 0x8786,
		GL_OP_ADD_EXT = 0x8787,
		GL_OP_MADD_EXT = 0x8788,
		GL_OP_FRAC_EXT = 0x8789,
		GL_OP_MAX_EXT = 0x878A,
		GL_OP_MIN_EXT = 0x878B,
		GL_OP_SET_GE_EXT = 0x878C,
		GL_OP_SET_LT_EXT = 0x878D,
		GL_OP_CLAMP_EXT = 0x878E,
		GL_OP_FLOOR_EXT = 0x878F,
		GL_OP_ROUND_EXT = 0x8790,
		GL_OP_EXP_BASE_2_EXT = 0x8791,
		GL_OP_LOG_BASE_2_EXT = 0x8792,
		GL_OP_POWER_EXT = 0x8793,
		GL_OP_RECIP_EXT = 0x8794,
		GL_OP_RECIP_SQRT_EXT = 0x8795,
		GL_OP_SUB_EXT = 0x8796,
		GL_OP_CROSS_PRODUCT_EXT = 0x8797,
		GL_OP_MULTIPLY_MATRIX_EXT = 0x8798,
		GL_OP_MOV_EXT = 0x8799,
		GL_OUTPUT_VERTEX_EXT = 0x879A,
		GL_OUTPUT_COLOR0_EXT = 0x879B,
		GL_OUTPUT_COLOR1_EXT = 0x879C,
		GL_OUTPUT_TEXTURE_COORD0_EXT = 0x879D,
		GL_OUTPUT_TEXTURE_COORD1_EXT = 0x879E,
		GL_OUTPUT_TEXTURE_COORD2_EXT = 0x879F,
		GL_OUTPUT_TEXTURE_COORD3_EXT = 0x87A0,
		GL_OUTPUT_TEXTURE_COORD4_EXT = 0x87A1,
		GL_OUTPUT_TEXTURE_COORD5_EXT = 0x87A2,
		GL_OUTPUT_TEXTURE_COORD6_EXT = 0x87A3,
		GL_OUTPUT_TEXTURE_COORD7_EXT = 0x87A4,
		GL_OUTPUT_TEXTURE_COORD8_EXT = 0x87A5,
		GL_OUTPUT_TEXTURE_COORD9_EXT = 0x87A6,
		GL_OUTPUT_TEXTURE_COORD10_EXT = 0x87A7,
		GL_OUTPUT_TEXTURE_COORD11_EXT = 0x87A8,
		GL_OUTPUT_TEXTURE_COORD12_EXT = 0x87A9,
		GL_OUTPUT_TEXTURE_COORD13_EXT = 0x87AA,
		GL_OUTPUT_TEXTURE_COORD14_EXT = 0x87AB,
		GL_OUTPUT_TEXTURE_COORD15_EXT = 0x87AC,
		GL_OUTPUT_TEXTURE_COORD16_EXT = 0x87AD,
		GL_OUTPUT_TEXTURE_COORD17_EXT = 0x87AE,
		GL_OUTPUT_TEXTURE_COORD18_EXT = 0x87AF,
		GL_OUTPUT_TEXTURE_COORD19_EXT = 0x87B0,
		GL_OUTPUT_TEXTURE_COORD20_EXT = 0x87B1,
		GL_OUTPUT_TEXTURE_COORD21_EXT = 0x87B2,
		GL_OUTPUT_TEXTURE_COORD22_EXT = 0x87B3,
		GL_OUTPUT_TEXTURE_COORD23_EXT = 0x87B4,
		GL_OUTPUT_TEXTURE_COORD24_EXT = 0x87B5,
		GL_OUTPUT_TEXTURE_COORD25_EXT = 0x87B6,
		GL_OUTPUT_TEXTURE_COORD26_EXT = 0x87B7,
		GL_OUTPUT_TEXTURE_COORD27_EXT = 0x87B8,
		GL_OUTPUT_TEXTURE_COORD28_EXT = 0x87B9,
		GL_OUTPUT_TEXTURE_COORD29_EXT = 0x87BA,
		GL_OUTPUT_TEXTURE_COORD30_EXT = 0x87BB,
		GL_OUTPUT_TEXTURE_COORD31_EXT = 0x87BC,
		GL_OUTPUT_FOG_EXT = 0x87BD,
		GL_SCALAR_EXT = 0x87BE,
		GL_VECTOR_EXT = 0x87BF,
		GL_MATRIX_EXT = 0x87C0,
		GL_VARIANT_EXT = 0x87C1,
		GL_INVARIANT_EXT = 0x87C2,
		GL_LOCAL_CONSTANT_EXT = 0x87C3,
		GL_LOCAL_EXT = 0x87C4,
		GL_MAX_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87C5,
		GL_MAX_VERTEX_SHADER_VARIANTS_EXT = 0x87C6,
		GL_MAX_VERTEX_SHADER_INVARIANTS_EXT = 0x87C7,
		GL_MAX_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87C8,
		GL_MAX_VERTEX_SHADER_LOCALS_EXT = 0x87C9,
		GL_MAX_OPTIMIZED_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87CA,
		GL_MAX_OPTIMIZED_VERTEX_SHADER_VARIANTS_EXT = 0x87CB,
		GL_MAX_OPTIMIZED_VERTEX_SHADER_INVARIANTS_EXT = 0x87CC,
		GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87CD,
		GL_MAX_OPTIMIZED_VERTEX_SHADER_LOCALS_EXT = 0x87CE,
		GL_VERTEX_SHADER_INSTRUCTIONS_EXT = 0x87CF,
		GL_VERTEX_SHADER_VARIANTS_EXT = 0x87D0,
		GL_VERTEX_SHADER_INVARIANTS_EXT = 0x87D1,
		GL_VERTEX_SHADER_LOCAL_CONSTANTS_EXT = 0x87D2,
		GL_VERTEX_SHADER_LOCALS_EXT = 0x87D3,
		GL_VERTEX_SHADER_OPTIMIZED_EXT = 0x87D4,
		GL_X_EXT = 0x87D5,
		GL_Y_EXT = 0x87D6,
		GL_Z_EXT = 0x87D7,
		GL_W_EXT = 0x87D8,
		GL_NEGATIVE_X_EXT = 0x87D9,
		GL_NEGATIVE_Y_EXT = 0x87DA,
		GL_NEGATIVE_Z_EXT = 0x87DB,
		GL_NEGATIVE_W_EXT = 0x87DC,
		GL_ZERO_EXT = 0x87DD,
		GL_ONE_EXT = 0x87DE,
		GL_NEGATIVE_ONE_EXT = 0x87DF,
		GL_NORMALIZED_RANGE_EXT = 0x87E0,
		GL_FULL_RANGE_EXT = 0x87E1,
		GL_CURRENT_VERTEX_EXT = 0x87E2,
		GL_MVP_MATRIX_EXT = 0x87E3,
		GL_VARIANT_VALUE_EXT = 0x87E4,
		GL_VARIANT_DATATYPE_EXT = 0x87E5,
		GL_VARIANT_ARRAY_STRIDE_EXT = 0x87E6,
		GL_VARIANT_ARRAY_TYPE_EXT = 0x87E7,
		GL_VARIANT_ARRAY_EXT = 0x87E8,
		GL_VARIANT_ARRAY_POINTER_EXT = 0x87E9,
		GL_INVARIANT_VALUE_EXT = 0x87EA,
		GL_INVARIANT_DATATYPE_EXT = 0x87EB,
		GL_LOCAL_CONSTANT_VALUE_EXT = 0x87EC,
		GL_LOCAL_CONSTANT_DATATYPE_EXT = 0x87ED;

	private EXTVertexShader() {}

	public static void glBeginVertexShaderEXT() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginVertexShaderEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginVertexShaderEXT(function_pointer);
	}
	static native void nglBeginVertexShaderEXT(long function_pointer);

	public static void glEndVertexShaderEXT() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndVertexShaderEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndVertexShaderEXT(function_pointer);
	}
	static native void nglEndVertexShaderEXT(long function_pointer);

	public static void glBindVertexShaderEXT(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindVertexShaderEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindVertexShaderEXT(id, function_pointer);
	}
	static native void nglBindVertexShaderEXT(int id, long function_pointer);

	public static int glGenVertexShadersEXT(int range) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenVertexShadersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGenVertexShadersEXT(range, function_pointer);
		return __result;
	}
	static native int nglGenVertexShadersEXT(int range, long function_pointer);

	public static void glDeleteVertexShaderEXT(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteVertexShaderEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteVertexShaderEXT(id, function_pointer);
	}
	static native void nglDeleteVertexShaderEXT(int id, long function_pointer);

	public static void glShaderOp1EXT(int op, int res, int arg1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShaderOp1EXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglShaderOp1EXT(op, res, arg1, function_pointer);
	}
	static native void nglShaderOp1EXT(int op, int res, int arg1, long function_pointer);

	public static void glShaderOp2EXT(int op, int res, int arg1, int arg2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShaderOp2EXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglShaderOp2EXT(op, res, arg1, arg2, function_pointer);
	}
	static native void nglShaderOp2EXT(int op, int res, int arg1, int arg2, long function_pointer);

	public static void glShaderOp3EXT(int op, int res, int arg1, int arg2, int arg3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShaderOp3EXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglShaderOp3EXT(op, res, arg1, arg2, arg3, function_pointer);
	}
	static native void nglShaderOp3EXT(int op, int res, int arg1, int arg2, int arg3, long function_pointer);

	public static void glSwizzleEXT(int res, int in, int outX, int outY, int outZ, int outW) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSwizzleEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSwizzleEXT(res, in, outX, outY, outZ, outW, function_pointer);
	}
	static native void nglSwizzleEXT(int res, int in, int outX, int outY, int outZ, int outW, long function_pointer);

	public static void glWriteMaskEXT(int res, int in, int outX, int outY, int outZ, int outW) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWriteMaskEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglWriteMaskEXT(res, in, outX, outY, outZ, outW, function_pointer);
	}
	static native void nglWriteMaskEXT(int res, int in, int outX, int outY, int outZ, int outW, long function_pointer);

	public static void glInsertComponentEXT(int res, int src, int num) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInsertComponentEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglInsertComponentEXT(res, src, num, function_pointer);
	}
	static native void nglInsertComponentEXT(int res, int src, int num, long function_pointer);

	public static void glExtractComponentEXT(int res, int src, int num) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glExtractComponentEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglExtractComponentEXT(res, src, num, function_pointer);
	}
	static native void nglExtractComponentEXT(int res, int src, int num, long function_pointer);

	public static int glGenSymbolsEXT(int dataType, int storageType, int range, int components) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenSymbolsEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGenSymbolsEXT(dataType, storageType, range, components, function_pointer);
		return __result;
	}
	static native int nglGenSymbolsEXT(int dataType, int storageType, int range, int components, long function_pointer);

	public static void glSetInvariantEXT(int id, DoubleBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetInvariantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetInvariantEXT(id, GL11.GL_DOUBLE, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glSetInvariantEXT(int id, FloatBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetInvariantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetInvariantEXT(id, GL11.GL_FLOAT, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glSetInvariantEXT(int id, boolean unsigned, ByteBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetInvariantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetInvariantEXT(id, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glSetInvariantEXT(int id, boolean unsigned, IntBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetInvariantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetInvariantEXT(id, unsigned ? GL11.GL_UNSIGNED_INT : GL11.GL_INT, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glSetInvariantEXT(int id, boolean unsigned, ShortBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetInvariantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetInvariantEXT(id, unsigned ? GL11.GL_UNSIGNED_SHORT : GL11.GL_SHORT, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglSetInvariantEXT(int id, int type, long pAddr, long function_pointer);

	public static void glSetLocalConstantEXT(int id, DoubleBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetLocalConstantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetLocalConstantEXT(id, GL11.GL_DOUBLE, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glSetLocalConstantEXT(int id, FloatBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetLocalConstantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetLocalConstantEXT(id, GL11.GL_FLOAT, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glSetLocalConstantEXT(int id, boolean unsigned, ByteBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetLocalConstantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetLocalConstantEXT(id, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glSetLocalConstantEXT(int id, boolean unsigned, IntBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetLocalConstantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetLocalConstantEXT(id, unsigned ? GL11.GL_UNSIGNED_INT : GL11.GL_INT, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glSetLocalConstantEXT(int id, boolean unsigned, ShortBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetLocalConstantEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglSetLocalConstantEXT(id, unsigned ? GL11.GL_UNSIGNED_SHORT : GL11.GL_SHORT, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglSetLocalConstantEXT(int id, int type, long pAddr, long function_pointer);

	public static void glVariantEXT(int id, ByteBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantbvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglVariantbvEXT(id, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantbvEXT(int id, long pAddr, long function_pointer);

	public static void glVariantEXT(int id, ShortBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantsvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglVariantsvEXT(id, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantsvEXT(int id, long pAddr, long function_pointer);

	public static void glVariantEXT(int id, IntBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglVariantivEXT(id, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantivEXT(int id, long pAddr, long function_pointer);

	public static void glVariantEXT(int id, FloatBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantfvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglVariantfvEXT(id, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantfvEXT(int id, long pAddr, long function_pointer);

	public static void glVariantEXT(int id, DoubleBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantdvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglVariantdvEXT(id, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantdvEXT(int id, long pAddr, long function_pointer);

	public static void glVariantuEXT(int id, ByteBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantubvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglVariantubvEXT(id, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantubvEXT(int id, long pAddr, long function_pointer);

	public static void glVariantuEXT(int id, ShortBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantusvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglVariantusvEXT(id, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantusvEXT(int id, long pAddr, long function_pointer);

	public static void glVariantuEXT(int id, IntBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantuivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pAddr, 4);
		nglVariantuivEXT(id, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantuivEXT(int id, long pAddr, long function_pointer);

	public static void glVariantPointerEXT(int id, int stride, DoubleBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pAddr);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_vertex_shader_glVariantPointerEXT_pAddr = pAddr;
		nglVariantPointerEXT(id, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glVariantPointerEXT(int id, int stride, FloatBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pAddr);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_vertex_shader_glVariantPointerEXT_pAddr = pAddr;
		nglVariantPointerEXT(id, GL11.GL_FLOAT, stride, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glVariantPointerEXT(int id, boolean unsigned, int stride, ByteBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pAddr);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_vertex_shader_glVariantPointerEXT_pAddr = pAddr;
		nglVariantPointerEXT(id, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, stride, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glVariantPointerEXT(int id, boolean unsigned, int stride, IntBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pAddr);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_vertex_shader_glVariantPointerEXT_pAddr = pAddr;
		nglVariantPointerEXT(id, unsigned ? GL11.GL_UNSIGNED_INT : GL11.GL_INT, stride, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	public static void glVariantPointerEXT(int id, boolean unsigned, int stride, ShortBuffer pAddr) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pAddr);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_vertex_shader_glVariantPointerEXT_pAddr = pAddr;
		nglVariantPointerEXT(id, unsigned ? GL11.GL_UNSIGNED_SHORT : GL11.GL_SHORT, stride, MemoryUtil.getAddress(pAddr), function_pointer);
	}
	static native void nglVariantPointerEXT(int id, int type, int stride, long pAddr, long function_pointer);
	public static void glVariantPointerEXT(int id, int type, int stride, long pAddr_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVariantPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVariantPointerEXTBO(id, type, stride, pAddr_buffer_offset, function_pointer);
	}
	static native void nglVariantPointerEXTBO(int id, int type, int stride, long pAddr_buffer_offset, long function_pointer);

	public static void glEnableVariantClientStateEXT(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEnableVariantClientStateEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnableVariantClientStateEXT(id, function_pointer);
	}
	static native void nglEnableVariantClientStateEXT(int id, long function_pointer);

	public static void glDisableVariantClientStateEXT(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDisableVariantClientStateEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisableVariantClientStateEXT(id, function_pointer);
	}
	static native void nglDisableVariantClientStateEXT(int id, long function_pointer);

	public static int glBindLightParameterEXT(int light, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindLightParameterEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglBindLightParameterEXT(light, value, function_pointer);
		return __result;
	}
	static native int nglBindLightParameterEXT(int light, int value, long function_pointer);

	public static int glBindMaterialParameterEXT(int face, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindMaterialParameterEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglBindMaterialParameterEXT(face, value, function_pointer);
		return __result;
	}
	static native int nglBindMaterialParameterEXT(int face, int value, long function_pointer);

	public static int glBindTexGenParameterEXT(int unit, int coord, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindTexGenParameterEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglBindTexGenParameterEXT(unit, coord, value, function_pointer);
		return __result;
	}
	static native int nglBindTexGenParameterEXT(int unit, int coord, int value, long function_pointer);

	public static int glBindTextureUnitParameterEXT(int unit, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindTextureUnitParameterEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglBindTextureUnitParameterEXT(unit, value, function_pointer);
		return __result;
	}
	static native int nglBindTextureUnitParameterEXT(int unit, int value, long function_pointer);

	public static int glBindParameterEXT(int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindParameterEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglBindParameterEXT(value, function_pointer);
		return __result;
	}
	static native int nglBindParameterEXT(int value, long function_pointer);

	public static boolean glIsVariantEnabledEXT(int id, int cap) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsVariantEnabledEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsVariantEnabledEXT(id, cap, function_pointer);
		return __result;
	}
	static native boolean nglIsVariantEnabledEXT(int id, int cap, long function_pointer);

	public static void glGetVariantBooleanEXT(int id, int value, ByteBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVariantBooleanvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetVariantBooleanvEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetVariantBooleanvEXT(int id, int value, long pbData, long function_pointer);

	public static void glGetVariantIntegerEXT(int id, int value, IntBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVariantIntegervEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetVariantIntegervEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetVariantIntegervEXT(int id, int value, long pbData, long function_pointer);

	public static void glGetVariantFloatEXT(int id, int value, FloatBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVariantFloatvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetVariantFloatvEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetVariantFloatvEXT(int id, int value, long pbData, long function_pointer);

	public static ByteBuffer glGetVariantPointerEXT(int id, int value, long result_size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVariantPointervEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglGetVariantPointervEXT(id, value, result_size, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetVariantPointervEXT(int id, int value, long result_size, long function_pointer);

	public static void glGetInvariantBooleanEXT(int id, int value, ByteBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInvariantBooleanvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetInvariantBooleanvEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetInvariantBooleanvEXT(int id, int value, long pbData, long function_pointer);

	public static void glGetInvariantIntegerEXT(int id, int value, IntBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInvariantIntegervEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetInvariantIntegervEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetInvariantIntegervEXT(int id, int value, long pbData, long function_pointer);

	public static void glGetInvariantFloatEXT(int id, int value, FloatBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInvariantFloatvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetInvariantFloatvEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetInvariantFloatvEXT(int id, int value, long pbData, long function_pointer);

	public static void glGetLocalConstantBooleanEXT(int id, int value, ByteBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetLocalConstantBooleanvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetLocalConstantBooleanvEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetLocalConstantBooleanvEXT(int id, int value, long pbData, long function_pointer);

	public static void glGetLocalConstantIntegerEXT(int id, int value, IntBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetLocalConstantIntegervEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetLocalConstantIntegervEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetLocalConstantIntegervEXT(int id, int value, long pbData, long function_pointer);

	public static void glGetLocalConstantFloatEXT(int id, int value, FloatBuffer pbData) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetLocalConstantFloatvEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pbData, 4);
		nglGetLocalConstantFloatvEXT(id, value, MemoryUtil.getAddress(pbData), function_pointer);
	}
	static native void nglGetLocalConstantFloatvEXT(int id, int value, long pbData, long function_pointer);
}
