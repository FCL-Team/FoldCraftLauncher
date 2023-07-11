/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESBlendEquationSeparate {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, and
	 *  GetFloatv:
	 */
	public static final int GL_BLEND_EQUATION_RGB_OES = 0x8009,
		GL_BLEND_EQUATION_ALPHA_OES = 0x883D;

	private OESBlendEquationSeparate() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glBlendEquationSeparateOES(int modeRGB, int modeAlpha) {
		nglBlendEquationSeparateOES(modeRGB, modeAlpha);
	}
	static native void nglBlendEquationSeparateOES(int modeRGB, int modeAlpha);
}
