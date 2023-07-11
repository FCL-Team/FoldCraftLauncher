/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTOcclusionQueryBoolean {

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQueryEXT, EndQueryEXT,
	 *  and GetQueryivEXT:
	 */
	public static final int GL_ANY_SAMPLES_PASSED_EXT = 0x8C2F,
		GL_ANY_SAMPLES_PASSED_CONSERVATIVE_EXT = 0x8D6A;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetQueryivEXT: 
	 */
	public static final int GL_CURRENT_QUERY_EXT = 0x8865;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetQueryObjectivEXT and
	 *  GetQueryObjectuivEXT:
	 */
	public static final int GL_QUERY_RESULT_EXT = 0x8866,
		GL_QUERY_RESULT_AVAILABLE_EXT = 0x8867;

	private EXTOcclusionQueryBoolean() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glGenQueriesEXT(IntBuffer ids) {
		BufferChecks.checkDirect(ids);
		nglGenQueriesEXT(ids.remaining(), MemoryUtil.getAddress(ids));
	}
	static native void nglGenQueriesEXT(int ids_n, long ids);

	/** Overloads glGenQueriesEXT. */
	public static int glGenQueriesEXT() {
		IntBuffer ids = APIUtil.getBufferInt();
		nglGenQueriesEXT(1, MemoryUtil.getAddress(ids));
		return ids.get(0);
	}

	public static void glDeleteQueriesEXT(IntBuffer ids) {
		BufferChecks.checkDirect(ids);
		nglDeleteQueriesEXT(ids.remaining(), MemoryUtil.getAddress(ids));
	}
	static native void nglDeleteQueriesEXT(int ids_n, long ids);

	/** Overloads glDeleteQueriesEXT. */
	public static void glDeleteQueriesEXT(int id) {
		nglDeleteQueriesEXT(1, APIUtil.getInt(id));
	}

	public static boolean glIsQueryEXT(int id) {
		boolean __result = nglIsQueryEXT(id);
		return __result;
	}
	static native boolean nglIsQueryEXT(int id);

	public static void glBeginQueryEXT(int target, int id) {
		nglBeginQueryEXT(target, id);
	}
	static native void nglBeginQueryEXT(int target, int id);

	public static void glEndQueryEXT(int target) {
		nglEndQueryEXT(target);
	}
	static native void nglEndQueryEXT(int target);

	public static void glGetQueryEXT(int target, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryivEXT(target, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetQueryivEXT(int target, int pname, long params);

	/** Overloads glGetQueryivEXT. */
	public static int glGetQueryiEXT(int target, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetQueryivEXT(target, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetQueryObjectuEXT(int id, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryObjectuivEXT(id, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetQueryObjectuivEXT(int id, int pname, long params);

	/** Overloads glGetQueryObjectuivEXT. */
	public static int glGetQueryObjectuiEXT(int id, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetQueryObjectuivEXT(id, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}
}
