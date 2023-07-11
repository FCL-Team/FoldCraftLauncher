/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBMatrixPalette {

	public static final int GL_MATRIX_PALETTE_ARB = 0x8840,
		GL_MAX_MATRIX_PALETTE_STACK_DEPTH_ARB = 0x8841,
		GL_MAX_PALETTE_MATRICES_ARB = 0x8842,
		GL_CURRENT_PALETTE_MATRIX_ARB = 0x8843,
		GL_MATRIX_INDEX_ARRAY_ARB = 0x8844,
		GL_CURRENT_MATRIX_INDEX_ARB = 0x8845,
		GL_MATRIX_INDEX_ARRAY_SIZE_ARB = 0x8846,
		GL_MATRIX_INDEX_ARRAY_TYPE_ARB = 0x8847,
		GL_MATRIX_INDEX_ARRAY_STRIDE_ARB = 0x8848,
		GL_MATRIX_INDEX_ARRAY_POINTER_ARB = 0x8849;

	private ARBMatrixPalette() {}

	public static void glCurrentPaletteMatrixARB(int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCurrentPaletteMatrixARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCurrentPaletteMatrixARB(index, function_pointer);
	}
	static native void nglCurrentPaletteMatrixARB(int index, long function_pointer);

	public static void glMatrixIndexPointerARB(int size, int stride, ByteBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMatrixIndexPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = pPointer;
		nglMatrixIndexPointerARB(size, GL11.GL_UNSIGNED_BYTE, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glMatrixIndexPointerARB(int size, int stride, IntBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMatrixIndexPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = pPointer;
		nglMatrixIndexPointerARB(size, GL11.GL_UNSIGNED_INT, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glMatrixIndexPointerARB(int size, int stride, ShortBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMatrixIndexPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = pPointer;
		nglMatrixIndexPointerARB(size, GL11.GL_UNSIGNED_SHORT, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	static native void nglMatrixIndexPointerARB(int size, int type, int stride, long pPointer, long function_pointer);
	public static void glMatrixIndexPointerARB(int size, int type, int stride, long pPointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMatrixIndexPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglMatrixIndexPointerARBBO(size, type, stride, pPointer_buffer_offset, function_pointer);
	}
	static native void nglMatrixIndexPointerARBBO(int size, int type, int stride, long pPointer_buffer_offset, long function_pointer);

	public static void glMatrixIndexuARB(ByteBuffer pIndices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMatrixIndexubvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pIndices);
		nglMatrixIndexubvARB(pIndices.remaining(), MemoryUtil.getAddress(pIndices), function_pointer);
	}
	static native void nglMatrixIndexubvARB(int pIndices_size, long pIndices, long function_pointer);

	public static void glMatrixIndexuARB(ShortBuffer pIndices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMatrixIndexusvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pIndices);
		nglMatrixIndexusvARB(pIndices.remaining(), MemoryUtil.getAddress(pIndices), function_pointer);
	}
	static native void nglMatrixIndexusvARB(int pIndices_size, long pIndices, long function_pointer);

	public static void glMatrixIndexuARB(IntBuffer pIndices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMatrixIndexuivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pIndices);
		nglMatrixIndexuivARB(pIndices.remaining(), MemoryUtil.getAddress(pIndices), function_pointer);
	}
	static native void nglMatrixIndexuivARB(int pIndices_size, long pIndices, long function_pointer);
}
