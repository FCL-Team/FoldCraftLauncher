/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class ANGLEFramebufferBlit {

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindFramebuffer,
	 *  CheckFramebufferStatus, FramebufferTexture2D, FramebufferTexture3DOES,
	 *  FramebufferRenderbuffer, and
	 *  GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_READ_FRAMEBUFFER_ANGLE = 0x8CA8,
		GL_DRAW_FRAMEBUFFER_ANGLE = 0x8CA9;

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetIntegerv and GetFloatv: 
	 */
	public static final int GL_DRAW_FRAMEBUFFER_BINDING_ANGLE = 0x8CA6,
		GL_READ_FRAMEBUFFER_BINDING_ANGLE = 0x8CAA;

	private ANGLEFramebufferBlit() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glBlitFramebufferANGLE(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
		nglBlitFramebufferANGLE(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
	}
	static native void nglBlitFramebufferANGLE(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter);
}
