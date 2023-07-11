/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTRobustness {

	/**
	 * Returned by GetGraphicsResetStatusEXT: 
	 */
	public static final int GL_NO_ERROR = 0x0,
		GL_GUILTY_CONTEXT_RESET_EXT = 0x8253,
		GL_INNOCENT_CONTEXT_RESET_EXT = 0x8254,
		GL_UNKNOWN_CONTEXT_RESET_EXT = 0x8255;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_CONTEXT_ROBUST_ACCESS_EXT = 0x90F3,
		GL_RESET_NOTIFICATION_STRATEGY_EXT = 0x8256;

	/**
	 *  Returned by GetIntegerv and related simple queries when &lt;value&gt; is
	 *  RESET_NOTIFICATION_STRATEGY_EXT :
	 */
	public static final int GL_LOSE_CONTEXT_ON_RESET_EXT = 0x8252,
		GL_NO_RESET_NOTIFICATION_EXT = 0x8261;

	private EXTRobustness() {}

	static native void initNativeStubs() throws LWJGLException;

	public static int glGetGraphicsResetStatusEXT() {
		int __result = nglGetGraphicsResetStatusEXT();
		return __result;
	}
	static native int nglGetGraphicsResetStatusEXT();

	public static void glReadnPixelsEXT(int x, int y, int width, int height, int format, int type, ByteBuffer data) {
		BufferChecks.checkDirect(data);
		nglReadnPixelsEXT(x, y, width, height, format, type, data.remaining(), MemoryUtil.getAddress(data));
	}
	public static void glReadnPixelsEXT(int x, int y, int width, int height, int format, int type, FloatBuffer data) {
		BufferChecks.checkDirect(data);
		nglReadnPixelsEXT(x, y, width, height, format, type, (data.remaining() << 2), MemoryUtil.getAddress(data));
	}
	public static void glReadnPixelsEXT(int x, int y, int width, int height, int format, int type, IntBuffer data) {
		BufferChecks.checkDirect(data);
		nglReadnPixelsEXT(x, y, width, height, format, type, (data.remaining() << 2), MemoryUtil.getAddress(data));
	}
	public static void glReadnPixelsEXT(int x, int y, int width, int height, int format, int type, ShortBuffer data) {
		BufferChecks.checkDirect(data);
		nglReadnPixelsEXT(x, y, width, height, format, type, (data.remaining() << 1), MemoryUtil.getAddress(data));
	}
	static native void nglReadnPixelsEXT(int x, int y, int width, int height, int format, int type, int data_bufSize, long data);

	public static void glGetnUniformEXT(int program, int location, FloatBuffer params) {
		BufferChecks.checkDirect(params);
		nglGetnUniformfvEXT(program, location, params.remaining(), MemoryUtil.getAddress(params));
	}
	static native void nglGetnUniformfvEXT(int program, int location, int params_bufSize, long params);

	public static void glGetnUniformEXT(int program, int location, IntBuffer params) {
		BufferChecks.checkDirect(params);
		nglGetnUniformivEXT(program, location, params.remaining(), MemoryUtil.getAddress(params));
	}
	static native void nglGetnUniformivEXT(int program, int location, int params_bufSize, long params);
}
