/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTMultisampledRenderToTexture {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv: 
	 */
	public static final int GL_RENDERBUFFER_SAMPLES_EXT = 0x9133;

	/**
	 * Returned by CheckFramebufferStatus: 
	 */
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_EXT = 0x9134;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_MAX_SAMPLES_EXT = 0x9135;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetFramebufferAttachmentParameteriv: 
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_SAMPLES_EXT = 0x8D6C;

	private EXTMultisampledRenderToTexture() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glRenderbufferStorageMultisampleEXT(int target, int samples, int internalformat, int width, int height) {
		nglRenderbufferStorageMultisampleEXT(target, samples, internalformat, width, height);
	}
	static native void nglRenderbufferStorageMultisampleEXT(int target, int samples, int internalformat, int width, int height);

	public static void glFramebufferTexture2DMultisampleEXT(int target, int attachment, int textarget, int texture, int level, int samples) {
		nglFramebufferTexture2DMultisampleEXT(target, attachment, textarget, texture, level, samples);
	}
	static native void nglFramebufferTexture2DMultisampleEXT(int target, int attachment, int textarget, int texture, int level, int samples);
}
