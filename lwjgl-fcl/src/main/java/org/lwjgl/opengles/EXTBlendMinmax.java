/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTBlendMinmax {

	/**
	 * Accepted by the &lt;mode&gt; parameter of BlendEquationEXT: 
	 */
	public static final int GL_FUNC_ADD_EXT = 0x8006,
		GL_MIN_EXT = 0x8007,
		GL_MAX_EXT = 0x8008;

	private EXTBlendMinmax() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glBlendEquationEXT(int mode) {
		nglBlendEquationEXT(mode);
	}
	static native void nglBlendEquationEXT(int mode);
}
