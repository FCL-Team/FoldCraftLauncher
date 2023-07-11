/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESEGLImageExternal {

	/**
	 *  Accepted as a target in the &lt;target&gt; parameter of BindTexture and
	 *  EGLImageTargetTexture2DOES:
	 */
	public static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

	/**
	 * Returned in the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_EXTERNAL_OES = 0x8D66;

	/**
	 * Accepted as &lt;value&gt; in GetIntegerv() and GetFloatv() queries: 
	 */
	public static final int GL_TEXTURE_BINDING_EXTERNAL_OES = 0x8D67;

	/**
	 * Accepted as &lt;value&gt; in GetTexParameter*() queries: 
	 */
	public static final int GL_REQUIRED_TEXTURE_IMAGE_UNITS_OES = 0x8D68;

	private OESEGLImageExternal() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glEGLImageTargetTexture2DOES(int target, EGLImageOES image) {
		nglEGLImageTargetTexture2DOES(target, image.getPointer());
	}
	static native void nglEGLImageTargetTexture2DOES(int target, long image);
}
