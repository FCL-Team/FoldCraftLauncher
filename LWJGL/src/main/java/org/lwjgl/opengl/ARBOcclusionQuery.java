/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBOcclusionQuery {

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQueryARB, EndQueryARB,
	 *  and GetQueryivARB:
	 */
	public static final int GL_SAMPLES_PASSED_ARB = 0x8914;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetQueryivARB: 
	 */
	public static final int GL_QUERY_COUNTER_BITS_ARB = 0x8864,
		GL_CURRENT_QUERY_ARB = 0x8865;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetQueryObjectivARB and
	 *  GetQueryObjectuivARB:
	 */
	public static final int GL_QUERY_RESULT_ARB = 0x8866,
		GL_QUERY_RESULT_AVAILABLE_ARB = 0x8867;

	private ARBOcclusionQuery() {}

	public static void glGenQueriesARB(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenQueriesARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglGenQueriesARB(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglGenQueriesARB(int ids_n, long ids, long function_pointer);

	/** Overloads glGenQueriesARB. */
	public static int glGenQueriesARB() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenQueriesARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer ids = APIUtil.getBufferInt(caps);
		nglGenQueriesARB(1, MemoryUtil.getAddress(ids), function_pointer);
		return ids.get(0);
	}

	public static void glDeleteQueriesARB(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteQueriesARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglDeleteQueriesARB(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglDeleteQueriesARB(int ids_n, long ids, long function_pointer);

	/** Overloads glDeleteQueriesARB. */
	public static void glDeleteQueriesARB(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteQueriesARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteQueriesARB(1, APIUtil.getInt(caps, id), function_pointer);
	}

	public static boolean glIsQueryARB(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsQueryARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsQueryARB(id, function_pointer);
		return __result;
	}
	static native boolean nglIsQueryARB(int id, long function_pointer);

	public static void glBeginQueryARB(int target, int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginQueryARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginQueryARB(target, id, function_pointer);
	}
	static native void nglBeginQueryARB(int target, int id, long function_pointer);

	public static void glEndQueryARB(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndQueryARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndQueryARB(target, function_pointer);
	}
	static native void nglEndQueryARB(int target, long function_pointer);

	public static void glGetQueryARB(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryivARB(int target, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetQueryivARB.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetQueryiARB} instead. 
	 */
	@Deprecated
	public static int glGetQueryARB(int target, int pname) {
		return ARBOcclusionQuery.glGetQueryiARB(target, pname);
	}

	/** Overloads glGetQueryivARB. */
	public static int glGetQueryiARB(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetQueryivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetQueryObjectARB(int id, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryObjectivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryObjectivARB(int id, int pname, long params, long function_pointer);

	/** Overloads glGetQueryObjectivARB. */
	public static int glGetQueryObjectiARB(int id, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetQueryObjectivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetQueryObjectuARB(int id, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectuivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryObjectuivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryObjectuivARB(int id, int pname, long params, long function_pointer);

	/** Overloads glGetQueryObjectuivARB. */
	public static int glGetQueryObjectuiARB(int id, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectuivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetQueryObjectuivARB(id, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
