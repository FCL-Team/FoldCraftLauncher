/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTFramebufferBlit {

	/**
	 * 	 Accepted by the &lt;target&gt; parameter of BindFramebufferEXT,
	 * 	 CheckFramebufferStatusEXT, FramebufferTexture{1D|2D|3D}EXT,
	 * 	 FramebufferRenderbufferEXT, and
	 * 	 GetFramebufferAttachmentParameterivEXT.
	 */
	public static final int GL_READ_FRAMEBUFFER_EXT = 0x8CA8,
		GL_DRAW_FRAMEBUFFER_EXT = 0x8CA9;

	/**
	 * 	 Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv, and GetDoublev.
	 */
	public static final int GL_DRAW_FRAMEBUFFER_BINDING_EXT = 0x8CA6,
		GL_READ_FRAMEBUFFER_BINDING_EXT = 0x8CAA;

	private EXTFramebufferBlit() {}

	/**
	 * 	 Transfers a rectangle of pixel values from one
	 * 	 region of the read framebuffer to another in the draw framebuffer.
	 * 	 &lt;mask&gt; is the bitwise OR of a number of values indicating which
	 * 	 buffers are to be copied. The values are COLOR_BUFFER_BIT,
	 * 	 DEPTH_BUFFER_BIT, and STENCIL_BUFFER_BIT.
	 * 	 The pixels corresponding to these buffers are
	 * 	 copied from the source rectangle, bound by the locations (srcX0,
	 * 	 srcY0) and (srcX1, srcY1) inclusive, to the destination rectangle,
	 * 	 bound by the locations (dstX0, dstY0) and (dstX1, dstY1)
	 * 	 inclusive.
	 * 	 If the source and destination rectangle dimensions do not match,
	 * 	 the source image is stretched to fit the destination
	 * 	 rectangle. &lt;filter&gt; must be LINEAR or NEAREST and specifies the
	 * 	 method of interpolation to be applied if the image is
	 * 	 stretched.
	 */
	public static void glBlitFramebufferEXT(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlitFramebufferEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlitFramebufferEXT(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter, function_pointer);
	}
	static native void nglBlitFramebufferEXT(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter, long function_pointer);
}
