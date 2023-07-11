/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVFence {

	public static final int GL_ALL_COMPLETED_NV = 0x84F2,
		GL_FENCE_STATUS_NV = 0x84F3,
		GL_FENCE_CONDITION_NV = 0x84F4;

	private NVFence() {}

	public static void glGenFencesNV(IntBuffer piFences) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenFencesNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(piFences);
		nglGenFencesNV(piFences.remaining(), MemoryUtil.getAddress(piFences), function_pointer);
	}
	static native void nglGenFencesNV(int piFences_n, long piFences, long function_pointer);

	/** Overloads glGenFencesNV. */
	public static int glGenFencesNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenFencesNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer piFences = APIUtil.getBufferInt(caps);
		nglGenFencesNV(1, MemoryUtil.getAddress(piFences), function_pointer);
		return piFences.get(0);
	}

	public static void glDeleteFencesNV(IntBuffer piFences) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteFencesNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(piFences);
		nglDeleteFencesNV(piFences.remaining(), MemoryUtil.getAddress(piFences), function_pointer);
	}
	static native void nglDeleteFencesNV(int piFences_n, long piFences, long function_pointer);

	/** Overloads glDeleteFencesNV. */
	public static void glDeleteFencesNV(int fence) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteFencesNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteFencesNV(1, APIUtil.getInt(caps, fence), function_pointer);
	}

	public static void glSetFenceNV(int fence, int condition) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSetFenceNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSetFenceNV(fence, condition, function_pointer);
	}
	static native void nglSetFenceNV(int fence, int condition, long function_pointer);

	public static boolean glTestFenceNV(int fence) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTestFenceNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglTestFenceNV(fence, function_pointer);
		return __result;
	}
	static native boolean nglTestFenceNV(int fence, long function_pointer);

	public static void glFinishFenceNV(int fence) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFinishFenceNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFinishFenceNV(fence, function_pointer);
	}
	static native void nglFinishFenceNV(int fence, long function_pointer);

	public static boolean glIsFenceNV(int fence) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsFenceNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsFenceNV(fence, function_pointer);
		return __result;
	}
	static native boolean nglIsFenceNV(int fence, long function_pointer);

	public static void glGetFenceivNV(int fence, int pname, IntBuffer piParams) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFenceivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(piParams, 4);
		nglGetFenceivNV(fence, pname, MemoryUtil.getAddress(piParams), function_pointer);
	}
	static native void nglGetFenceivNV(int fence, int pname, long piParams, long function_pointer);
}
