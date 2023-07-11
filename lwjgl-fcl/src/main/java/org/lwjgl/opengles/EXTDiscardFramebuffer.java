/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTDiscardFramebuffer {

	/**
	 *  Accepted in the &lt;attachments&gt; parameter of DiscardFramebufferEXT when the
	 *  default framebuffer is bound to &lt;target&gt;:
	 */
	public static final int GL_COLOR_EXT = 0x1800,
		GL_DEPTH_EXT = 0x1801,
		GL_STENCIL_EXT = 0x1802;

	private EXTDiscardFramebuffer() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glDiscardFramebufferEXT(int target, IntBuffer attachments) {
		BufferChecks.checkDirect(attachments);
		nglDiscardFramebufferEXT(target, attachments.remaining(), MemoryUtil.getAddress(attachments));
	}
	static native void nglDiscardFramebufferEXT(int target, int attachments_numAttachments, long attachments);
}
