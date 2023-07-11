/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBVertexBlend {

	public static final int GL_MAX_VERTEX_UNITS_ARB = 0x86A4,
		GL_ACTIVE_VERTEX_UNITS_ARB = 0x86A5,
		GL_WEIGHT_SUM_UNITY_ARB = 0x86A6,
		GL_VERTEX_BLEND_ARB = 0x86A7,
		GL_CURRENT_WEIGHT_ARB = 0x86A8,
		GL_WEIGHT_ARRAY_TYPE_ARB = 0x86A9,
		GL_WEIGHT_ARRAY_STRIDE_ARB = 0x86AA,
		GL_WEIGHT_ARRAY_SIZE_ARB = 0x86AB,
		GL_WEIGHT_ARRAY_POINTER_ARB = 0x86AC,
		GL_WEIGHT_ARRAY_ARB = 0x86AD,
		GL_MODELVIEW0_ARB = 0x1700,
		GL_MODELVIEW1_ARB = 0x850A,
		GL_MODELVIEW2_ARB = 0x8722,
		GL_MODELVIEW3_ARB = 0x8723,
		GL_MODELVIEW4_ARB = 0x8724,
		GL_MODELVIEW5_ARB = 0x8725,
		GL_MODELVIEW6_ARB = 0x8726,
		GL_MODELVIEW7_ARB = 0x8727,
		GL_MODELVIEW8_ARB = 0x8728,
		GL_MODELVIEW9_ARB = 0x8729,
		GL_MODELVIEW10_ARB = 0x872A,
		GL_MODELVIEW11_ARB = 0x872B,
		GL_MODELVIEW12_ARB = 0x872C,
		GL_MODELVIEW13_ARB = 0x872D,
		GL_MODELVIEW14_ARB = 0x872E,
		GL_MODELVIEW15_ARB = 0x872F,
		GL_MODELVIEW16_ARB = 0x8730,
		GL_MODELVIEW17_ARB = 0x8731,
		GL_MODELVIEW18_ARB = 0x8732,
		GL_MODELVIEW19_ARB = 0x8733,
		GL_MODELVIEW20_ARB = 0x8734,
		GL_MODELVIEW21_ARB = 0x8735,
		GL_MODELVIEW22_ARB = 0x8736,
		GL_MODELVIEW23_ARB = 0x8737,
		GL_MODELVIEW24_ARB = 0x8738,
		GL_MODELVIEW25_ARB = 0x8739,
		GL_MODELVIEW26_ARB = 0x873A,
		GL_MODELVIEW27_ARB = 0x873B,
		GL_MODELVIEW28_ARB = 0x873C,
		GL_MODELVIEW29_ARB = 0x873D,
		GL_MODELVIEW30_ARB = 0x873E,
		GL_MODELVIEW31_ARB = 0x873F;

	private ARBVertexBlend() {}

	public static void glWeightARB(ByteBuffer pWeights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightbvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pWeights);
		nglWeightbvARB(pWeights.remaining(), MemoryUtil.getAddress(pWeights), function_pointer);
	}
	static native void nglWeightbvARB(int pWeights_size, long pWeights, long function_pointer);

	public static void glWeightARB(ShortBuffer pWeights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightsvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pWeights);
		nglWeightsvARB(pWeights.remaining(), MemoryUtil.getAddress(pWeights), function_pointer);
	}
	static native void nglWeightsvARB(int pWeights_size, long pWeights, long function_pointer);

	public static void glWeightARB(IntBuffer pWeights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pWeights);
		nglWeightivARB(pWeights.remaining(), MemoryUtil.getAddress(pWeights), function_pointer);
	}
	static native void nglWeightivARB(int pWeights_size, long pWeights, long function_pointer);

	public static void glWeightARB(FloatBuffer pWeights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightfvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pWeights);
		nglWeightfvARB(pWeights.remaining(), MemoryUtil.getAddress(pWeights), function_pointer);
	}
	static native void nglWeightfvARB(int pWeights_size, long pWeights, long function_pointer);

	public static void glWeightARB(DoubleBuffer pWeights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightdvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pWeights);
		nglWeightdvARB(pWeights.remaining(), MemoryUtil.getAddress(pWeights), function_pointer);
	}
	static native void nglWeightdvARB(int pWeights_size, long pWeights, long function_pointer);

	public static void glWeightuARB(ByteBuffer pWeights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightubvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pWeights);
		nglWeightubvARB(pWeights.remaining(), MemoryUtil.getAddress(pWeights), function_pointer);
	}
	static native void nglWeightubvARB(int pWeights_size, long pWeights, long function_pointer);

	public static void glWeightuARB(ShortBuffer pWeights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightusvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pWeights);
		nglWeightusvARB(pWeights.remaining(), MemoryUtil.getAddress(pWeights), function_pointer);
	}
	static native void nglWeightusvARB(int pWeights_size, long pWeights, long function_pointer);

	public static void glWeightuARB(IntBuffer pWeights) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightuivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pWeights);
		nglWeightuivARB(pWeights.remaining(), MemoryUtil.getAddress(pWeights), function_pointer);
	}
	static native void nglWeightuivARB(int pWeights_size, long pWeights, long function_pointer);

	public static void glWeightPointerARB(int size, int stride, DoubleBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).ARB_vertex_blend_glWeightPointerARB_pPointer = pPointer;
		nglWeightPointerARB(size, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glWeightPointerARB(int size, int stride, FloatBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).ARB_vertex_blend_glWeightPointerARB_pPointer = pPointer;
		nglWeightPointerARB(size, GL11.GL_FLOAT, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glWeightPointerARB(int size, boolean unsigned, int stride, ByteBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).ARB_vertex_blend_glWeightPointerARB_pPointer = pPointer;
		nglWeightPointerARB(size, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glWeightPointerARB(int size, boolean unsigned, int stride, IntBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).ARB_vertex_blend_glWeightPointerARB_pPointer = pPointer;
		nglWeightPointerARB(size, unsigned ? GL11.GL_UNSIGNED_INT : GL11.GL_INT, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glWeightPointerARB(int size, boolean unsigned, int stride, ShortBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).ARB_vertex_blend_glWeightPointerARB_pPointer = pPointer;
		nglWeightPointerARB(size, unsigned ? GL11.GL_UNSIGNED_SHORT : GL11.GL_SHORT, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	static native void nglWeightPointerARB(int size, int type, int stride, long pPointer, long function_pointer);
	public static void glWeightPointerARB(int size, int type, int stride, long pPointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWeightPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglWeightPointerARBBO(size, type, stride, pPointer_buffer_offset, function_pointer);
	}
	static native void nglWeightPointerARBBO(int size, int type, int stride, long pPointer_buffer_offset, long function_pointer);

	public static void glVertexBlendARB(int count) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexBlendARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexBlendARB(count, function_pointer);
	}
	static native void nglVertexBlendARB(int count, long function_pointer);
}
