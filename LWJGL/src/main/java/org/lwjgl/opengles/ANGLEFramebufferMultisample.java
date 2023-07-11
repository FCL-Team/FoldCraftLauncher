/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class ANGLEFramebufferMultisample {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv: 
	 */
	public static final int GL_RENDERBUFFER_SAMPLES_ANGLE = 0x8CAB;

	/**
	 * Returned by CheckFramebufferStatus: 
	 */
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_ANGLE = 0x8D56;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_MAX_SAMPLES_ANGLE = 0x8D57;

	private ANGLEFramebufferMultisample() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glRenderbufferStorageMultisampleANGLE(int target, int samples, int internalformat, int width, int height) {
		nglRenderbufferStorageMultisampleANGLE(target, samples, internalformat, width, height);
	}
	static native void nglRenderbufferStorageMultisampleANGLE(int target, int samples, int internalformat, int width, int height);
}
