/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class INTELMapTexture {

	/**
	 * Accepted by the &lt;pname&gt; parameter of TexParameteri, for target TEXTURE_2D 
	 */
	public static final int GL_TEXTURE_MEMORY_LAYOUT_INTEL = 0x83FF;

	/**
	 *  Accepted by the &lt;params&gt; when &lt;pname&gt; is set to
	 *  &lt;TEXTURE_MEMORY_LAYOUT_INTEL&gt;:
	 */
	public static final int GL_LAYOUT_DEFAULT_INTEL = 0x0,
		GL_LAYOUT_LINEAR_INTEL = 0x1,
		GL_LAYOUT_LINEAR_CPU_CACHED_INTEL = 0x2;

	private INTELMapTexture() {}

	/**
	 *  The length parameter does not exist in the native API. It used by LWJGL to return a ByteBuffer
	 *  with a proper capacity.
	 */
	public static ByteBuffer glMapTexture2DINTEL(int texture, int level, long length, int access, IntBuffer stride, IntBuffer layout, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapTexture2DINTEL;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(stride, 1);
		BufferChecks.checkBuffer(layout, 1);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapTexture2DINTEL(texture, level, length, access, MemoryUtil.getAddress(stride), MemoryUtil.getAddress(layout), old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapTexture2DINTEL(int texture, int level, long length, int access, long stride, long layout, ByteBuffer old_buffer, long function_pointer);

	public static void glUnmapTexture2DINTEL(int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUnmapTexture2DINTEL;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUnmapTexture2DINTEL(texture, level, function_pointer);
	}
	static native void nglUnmapTexture2DINTEL(int texture, int level, long function_pointer);

	public static void glSyncTextureINTEL(int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSyncTextureINTEL;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSyncTextureINTEL(texture, function_pointer);
	}
	static native void nglSyncTextureINTEL(int texture, long function_pointer);
}
