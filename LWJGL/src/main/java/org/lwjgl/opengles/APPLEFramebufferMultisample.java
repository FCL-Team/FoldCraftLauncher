/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class APPLEFramebufferMultisample {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv: 
	 */
	public static final int GL_RENDERBUFFER_SAMPLES_APPLE = 0x8CAB;

	/**
	 * Returned by CheckFramebufferStatus: 
	 */
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_APPLE = 0x8D56;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, and
	 *  GetFloatv:
	 */
	public static final int GL_MAX_SAMPLES_APPLE = 0x8D57;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindFramebuffer,
	 *  CheckFramebufferStatus, FramebufferTexture2D, FramebufferRenderbuffer, and
	 *  GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_READ_FRAMEBUFFER_APPLE = 0x8CA8,
		GL_DRAW_FRAMEBUFFER_APPLE = 0x8CA9;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, and
	 *  GetFloatv:
	 */
	public static final int GL_DRAW_FRAMEBUFFER_BINDING_APPLE = 0x8CA6,
		GL_READ_FRAMEBUFFER_BINDING_APPLE = 0x8CAA;

	private APPLEFramebufferMultisample() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glRenderbufferStorageMultisampleAPPLE(int target, int samples, int internalformat, int width, int height) {
		nglRenderbufferStorageMultisampleAPPLE(target, samples, internalformat, width, height);
	}
	static native void nglRenderbufferStorageMultisampleAPPLE(int target, int samples, int internalformat, int width, int height);

	public static void glResolveMultisampleFramebufferAPPLE() {
		nglResolveMultisampleFramebufferAPPLE();
	}
	static native void nglResolveMultisampleFramebufferAPPLE();
}
