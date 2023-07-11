/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVFence {

	/**
	 * Accepted by the &lt;condition&gt; parameter of SetFenceNV: 
	 */
	public static final int GL_ALL_COMPLETED_NV = 0x84F2;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetFenceivNV: 
	 */
	public static final int GL_FENCE_STATUS_NV = 0x84F3,
		GL_FENCE_CONDITION_NV = 0x84F4;

	private NVFence() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glGenFencesNV(IntBuffer fences) {
		BufferChecks.checkDirect(fences);
		nglGenFencesNV(fences.remaining(), MemoryUtil.getAddress(fences));
	}
	static native void nglGenFencesNV(int fences_n, long fences);

	/** Overloads glGenFencesNV. */
	public static int glGenFencesNV() {
		IntBuffer fences = APIUtil.getBufferInt();
		nglGenFencesNV(1, MemoryUtil.getAddress(fences));
		return fences.get(0);
	}

	public static void glDeleteFencesNV(IntBuffer fences) {
		BufferChecks.checkDirect(fences);
		nglDeleteFencesNV(fences.remaining(), MemoryUtil.getAddress(fences));
	}
	static native void nglDeleteFencesNV(int fences_n, long fences);

	/** Overloads glDeleteFencesNV. */
	public static void glDeleteFencesNV(int fence) {
		nglDeleteFencesNV(1, APIUtil.getInt(fence));
	}

	public static void glSetFenceNV(int fence, int condition) {
		nglSetFenceNV(fence, condition);
	}
	static native void nglSetFenceNV(int fence, int condition);

	public static boolean glTestFenceNV(int fence) {
		boolean __result = nglTestFenceNV(fence);
		return __result;
	}
	static native boolean nglTestFenceNV(int fence);

	public static void glFinishFenceNV(int fence) {
		nglFinishFenceNV(fence);
	}
	static native void nglFinishFenceNV(int fence);

	public static boolean glIsFenceNV(int fence) {
		boolean __result = nglIsFenceNV(fence);
		return __result;
	}
	static native boolean nglIsFenceNV(int fence);

	public static void glGetFenceivNV(int fence, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetFenceivNV(fence, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetFenceivNV(int fence, int pname, long params);
}
