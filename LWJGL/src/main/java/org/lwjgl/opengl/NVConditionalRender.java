/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVConditionalRender {

	/**
	 *   Accepted by the &lt;mode&gt; parameter of BeginConditionalRenderNV:
	 */
	public static final int GL_QUERY_WAIT_NV = 0x8E13,
		GL_QUERY_NO_WAIT_NV = 0x8E14,
		GL_QUERY_BY_REGION_WAIT_NV = 0x8E15,
		GL_QUERY_BY_REGION_NO_WAIT_NV = 0x8E16;

	private NVConditionalRender() {}

	public static void glBeginConditionalRenderNV(int id, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginConditionalRenderNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginConditionalRenderNV(id, mode, function_pointer);
	}
	static native void nglBeginConditionalRenderNV(int id, int mode, long function_pointer);

	public static void glEndConditionalRenderNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndConditionalRenderNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndConditionalRenderNV(function_pointer);
	}
	static native void nglEndConditionalRenderNV(long function_pointer);
}
