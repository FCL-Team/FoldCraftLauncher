/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESBlendSubtract {

	/**
	 * Accepted by the &lt;mode&gt; parameter of BlendEquationOES: 
	 */
	public static final int GL_FUNC_ADD_OES = 0x8006,
		GL_FUNC_SUBTRACT_OES = 0x800A,
		GL_FUNC_REVERSE_SUBTRACT_OES = 0x800B;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_BLEND_EQUATION_OES = 0x8009;

	private OESBlendSubtract() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glBlendEquationOES(int mode) {
		nglBlendEquationOES(mode);
	}
	static native void nglBlendEquationOES(int mode);
}
