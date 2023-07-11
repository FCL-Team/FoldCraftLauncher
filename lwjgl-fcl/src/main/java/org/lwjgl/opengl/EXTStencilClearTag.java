/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTStencilClearTag {

	/**
	 * 	 Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * 	 GetFloatv, and GetDoublev.
	 */
	public static final int GL_STENCIL_TAG_BITS_EXT = 0x88F2,
		GL_STENCIL_CLEAR_TAG_VALUE_EXT = 0x88F3;

	private EXTStencilClearTag() {}

	/**
	 * 	 Controls the stencil clear tag state. stencilTagBits is a count of
	 * 	 the number of most-significant stencil buffer bits involved in the
	 * 	 stencil clear tag update.
	 */
	public static void glStencilClearTagEXT(int stencilTagBits, int stencilClearTag) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilClearTagEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilClearTagEXT(stencilTagBits, stencilClearTag, function_pointer);
	}
	static native void nglStencilClearTagEXT(int stencilTagBits, int stencilClearTag, long function_pointer);
}
