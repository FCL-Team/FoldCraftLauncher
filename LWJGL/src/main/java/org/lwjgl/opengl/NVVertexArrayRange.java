/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVVertexArrayRange {

	public static final int GL_VERTEX_ARRAY_RANGE_NV = 0x851D,
		GL_VERTEX_ARRAY_RANGE_LENGTH_NV = 0x851E,
		GL_VERTEX_ARRAY_RANGE_VALID_NV = 0x851F,
		GL_MAX_VERTEX_ARRAY_RANGE_ELEMENT_NV = 0x8520,
		GL_VERTEX_ARRAY_RANGE_POINTER_NV = 0x8521;

	private NVVertexArrayRange() {}

	public static void glVertexArrayRangeNV(ByteBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPointer);
		nglVertexArrayRangeNV(pPointer.remaining(), MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glVertexArrayRangeNV(DoubleBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPointer);
		nglVertexArrayRangeNV((pPointer.remaining() << 3), MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glVertexArrayRangeNV(FloatBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPointer);
		nglVertexArrayRangeNV((pPointer.remaining() << 2), MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glVertexArrayRangeNV(IntBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPointer);
		nglVertexArrayRangeNV((pPointer.remaining() << 2), MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glVertexArrayRangeNV(ShortBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPointer);
		nglVertexArrayRangeNV((pPointer.remaining() << 1), MemoryUtil.getAddress(pPointer), function_pointer);
	}
	static native void nglVertexArrayRangeNV(int pPointer_size, long pPointer, long function_pointer);

	public static void glFlushVertexArrayRangeNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFlushVertexArrayRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFlushVertexArrayRangeNV(function_pointer);
	}
	static native void nglFlushVertexArrayRangeNV(long function_pointer);

	public static ByteBuffer glAllocateMemoryNV(int size, float readFrequency, float writeFrequency, float priority) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAllocateMemoryNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglAllocateMemoryNV(size, readFrequency, writeFrequency, priority, size, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglAllocateMemoryNV(int size, float readFrequency, float writeFrequency, float priority, long result_size, long function_pointer);

	public static void glFreeMemoryNV(ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFreeMemoryNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pointer);
		nglFreeMemoryNV(MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglFreeMemoryNV(long pointer, long function_pointer);
}
