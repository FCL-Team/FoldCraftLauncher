/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class IMGMultisampledRenderToTexture {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv: 
	 */
	public static final int GL_RENDERBUFFER_SAMPLES_IMG = 0x9133;

	/**
	 * Returned by CheckFramebufferStatus: 
	 */
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE_IMG = 0x9134;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_SAMPLES_IMG = 0x9135;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetFramebufferAttachmentParameteriv: 
	 */
	public static final int GL_TEXTURE_SAMPLES_IMG = 0x9136;

	private IMGMultisampledRenderToTexture() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glRenderbufferStorageMultisampleIMG(int target, int samples, int internalformat, int width, int height) {
		nglRenderbufferStorageMultisampleIMG(target, samples, internalformat, width, height);
	}
	static native void nglRenderbufferStorageMultisampleIMG(int target, int samples, int internalformat, int width, int height);

	public static void glFramebufferTexture2DMultisampleIMG(int target, int attachment, int textarget, int texture, int level, int samples) {
		nglFramebufferTexture2DMultisampleIMG(target, attachment, textarget, texture, level, samples);
	}
	static native void nglFramebufferTexture2DMultisampleIMG(int target, int attachment, int textarget, int texture, int level, int samples);
}
