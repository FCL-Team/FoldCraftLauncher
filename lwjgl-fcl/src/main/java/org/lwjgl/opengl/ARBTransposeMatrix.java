/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTransposeMatrix {

	public static final int GL_TRANSPOSE_MODELVIEW_MATRIX_ARB = 0x84E3,
		GL_TRANSPOSE_PROJECTION_MATRIX_ARB = 0x84E4,
		GL_TRANSPOSE_TEXTURE_MATRIX_ARB = 0x84E5,
		GL_TRANSPOSE_COLOR_MATRIX_ARB = 0x84E6;

	private ARBTransposeMatrix() {}

	public static void glLoadTransposeMatrixARB(FloatBuffer pfMtx) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLoadTransposeMatrixfARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pfMtx, 16);
		nglLoadTransposeMatrixfARB(MemoryUtil.getAddress(pfMtx), function_pointer);
	}
	static native void nglLoadTransposeMatrixfARB(long pfMtx, long function_pointer);

	public static void glMultTransposeMatrixARB(FloatBuffer pfMtx) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultTransposeMatrixfARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pfMtx, 16);
		nglMultTransposeMatrixfARB(MemoryUtil.getAddress(pfMtx), function_pointer);
	}
	static native void nglMultTransposeMatrixfARB(long pfMtx, long function_pointer);
}
