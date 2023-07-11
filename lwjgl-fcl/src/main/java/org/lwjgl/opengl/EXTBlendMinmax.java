/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTBlendMinmax {

	/**
	 * Accepted by the &lt;mode&gt; parameter of BlendEquationEXT. 
	 */
	public static final int GL_FUNC_ADD_EXT = 0x8006,
		GL_MIN_EXT = 0x8007,
		GL_MAX_EXT = 0x8008;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev.
	 */
	public static final int GL_BLEND_EQUATION_EXT = 0x8009;

	private EXTBlendMinmax() {}

	public static void glBlendEquationEXT(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendEquationEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendEquationEXT(mode, function_pointer);
	}
	static native void nglBlendEquationEXT(int mode, long function_pointer);
}
