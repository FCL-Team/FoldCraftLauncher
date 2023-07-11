/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTBlendEquationSeparate {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_BLEND_EQUATION_RGB_EXT = 0x8009,
		GL_BLEND_EQUATION_ALPHA_EXT = 0x883D;

	private EXTBlendEquationSeparate() {}

	public static void glBlendEquationSeparateEXT(int modeRGB, int modeAlpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendEquationSeparateEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendEquationSeparateEXT(modeRGB, modeAlpha, function_pointer);
	}
	static native void nglBlendEquationSeparateEXT(int modeRGB, int modeAlpha, long function_pointer);
}
