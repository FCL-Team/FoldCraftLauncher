/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESBlendFuncSeparate {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, and
	 *  GetFloatv:
	 */
	public static final int GL_BLEND_DST_RGB_OES = 0x80C8,
		BLEND_SRC_RGB_OES = 0x80C9,
		BLEND_DST_ALPHA_OES = 0x80CA,
		BLEND_SRC_ALPHA_OES = 0x80CB;

	private OESBlendFuncSeparate() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glBlendFuncSeparateOES(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
		nglBlendFuncSeparateOES(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
	}
	static native void nglBlendFuncSeparateOES(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha);
}
