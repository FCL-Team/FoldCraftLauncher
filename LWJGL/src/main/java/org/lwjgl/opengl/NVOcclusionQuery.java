/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVOcclusionQuery {

	public static final int GL_OCCLUSION_TEST_HP = 0x8165,
		GL_OCCLUSION_TEST_RESULT_HP = 0x8166,
		GL_PIXEL_COUNTER_BITS_NV = 0x8864,
		GL_CURRENT_OCCLUSION_QUERY_ID_NV = 0x8865,
		GL_PIXEL_COUNT_NV = 0x8866,
		GL_PIXEL_COUNT_AVAILABLE_NV = 0x8867;

	private NVOcclusionQuery() {}

	public static void glGenOcclusionQueriesNV(IntBuffer piIDs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenOcclusionQueriesNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(piIDs);
		nglGenOcclusionQueriesNV(piIDs.remaining(), MemoryUtil.getAddress(piIDs), function_pointer);
	}
	static native void nglGenOcclusionQueriesNV(int piIDs_n, long piIDs, long function_pointer);

	/** Overloads glGenOcclusionQueriesNV. */
	public static int glGenOcclusionQueriesNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenOcclusionQueriesNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer piIDs = APIUtil.getBufferInt(caps);
		nglGenOcclusionQueriesNV(1, MemoryUtil.getAddress(piIDs), function_pointer);
		return piIDs.get(0);
	}

	public static void glDeleteOcclusionQueriesNV(IntBuffer piIDs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteOcclusionQueriesNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(piIDs);
		nglDeleteOcclusionQueriesNV(piIDs.remaining(), MemoryUtil.getAddress(piIDs), function_pointer);
	}
	static native void nglDeleteOcclusionQueriesNV(int piIDs_n, long piIDs, long function_pointer);

	/** Overloads glDeleteOcclusionQueriesNV. */
	public static void glDeleteOcclusionQueriesNV(int piID) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteOcclusionQueriesNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteOcclusionQueriesNV(1, APIUtil.getInt(caps, piID), function_pointer);
	}

	public static boolean glIsOcclusionQueryNV(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsOcclusionQueryNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsOcclusionQueryNV(id, function_pointer);
		return __result;
	}
	static native boolean nglIsOcclusionQueryNV(int id, long function_pointer);

	public static void glBeginOcclusionQueryNV(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginOcclusionQueryNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginOcclusionQueryNV(id, function_pointer);
	}
	static native void nglBeginOcclusionQueryNV(int id, long function_pointer);

	public static void glEndOcclusionQueryNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndOcclusionQueryNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndOcclusionQueryNV(function_pointer);
	}
	static native void nglEndOcclusionQueryNV(long function_pointer);

	public static void glGetOcclusionQueryuNV(int id, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetOcclusionQueryuivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetOcclusionQueryuivNV(id, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetOcclusionQueryuivNV(int id, int pname, long params, long function_pointer);

	/** Overloads glGetOcclusionQueryuivNV. */
	public static int glGetOcclusionQueryuiNV(int id, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetOcclusionQueryuivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetOcclusionQueryuivNV(id, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetOcclusionQueryNV(int id, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetOcclusionQueryivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetOcclusionQueryivNV(id, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetOcclusionQueryivNV(int id, int pname, long params, long function_pointer);

	/** Overloads glGetOcclusionQueryivNV. */
	public static int glGetOcclusionQueryiNV(int id, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetOcclusionQueryivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetOcclusionQueryivNV(id, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
