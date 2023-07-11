/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTBlendFuncSeparate {

	public static final int GL_BLEND_DST_RGB_EXT = 0x80C8,
		GL_BLEND_SRC_RGB_EXT = 0x80C9,
		GL_BLEND_DST_ALPHA_EXT = 0x80CA,
		GL_BLEND_SRC_ALPHA_EXT = 0x80CB;

	private EXTBlendFuncSeparate() {}

	public static void glBlendFuncSeparateEXT(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendFuncSeparateEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendFuncSeparateEXT(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha, function_pointer);
	}
	static native void nglBlendFuncSeparateEXT(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha, long function_pointer);
}
