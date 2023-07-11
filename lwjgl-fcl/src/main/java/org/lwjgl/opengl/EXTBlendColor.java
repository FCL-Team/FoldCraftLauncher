/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTBlendColor {

	/**
	 * Accepted by the &lt;sfactor&gt; and &lt;dfactor&gt; parameters of BlendFunc. 
	 */
	public static final int GL_CONSTANT_COLOR_EXT = 0x8001,
		GL_ONE_MINUS_CONSTANT_COLOR_EXT = 0x8002,
		GL_CONSTANT_ALPHA_EXT = 0x8003,
		GL_ONE_MINUS_CONSTANT_ALPHA_EXT = 0x8004;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev.
	 */
	public static final int GL_BLEND_COLOR_EXT = 0x8005;

	private EXTBlendColor() {}

	public static void glBlendColorEXT(float red, float green, float blue, float alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendColorEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendColorEXT(red, green, blue, alpha, function_pointer);
	}
	static native void nglBlendColorEXT(float red, float green, float blue, float alpha, long function_pointer);
}
