/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class ARBDrawBuffers {

	/**
	 * Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv: 
	 */
	public static final int GL_MAX_DRAW_BUFFERS_ARB = 0x8824,
		GL_DRAW_BUFFER0_ARB = 0x8825,
		GL_DRAW_BUFFER1_ARB = 0x8826,
		GL_DRAW_BUFFER2_ARB = 0x8827,
		GL_DRAW_BUFFER3_ARB = 0x8828,
		GL_DRAW_BUFFER4_ARB = 0x8829,
		GL_DRAW_BUFFER5_ARB = 0x882A,
		GL_DRAW_BUFFER6_ARB = 0x882B,
		GL_DRAW_BUFFER7_ARB = 0x882C,
		GL_DRAW_BUFFER8_ARB = 0x882D,
		GL_DRAW_BUFFER9_ARB = 0x882E,
		GL_DRAW_BUFFER10_ARB = 0x882F,
		GL_DRAW_BUFFER11_ARB = 0x8830,
		GL_DRAW_BUFFER12_ARB = 0x8831,
		GL_DRAW_BUFFER13_ARB = 0x8832,
		GL_DRAW_BUFFER14_ARB = 0x8833,
		GL_DRAW_BUFFER15_ARB = 0x8834;

	private ARBDrawBuffers() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glDrawBuffersARB(IntBuffer buffers) {
		BufferChecks.checkDirect(buffers);
		nglDrawBuffersARB(buffers.remaining(), MemoryUtil.getAddress(buffers));
	}
	static native void nglDrawBuffersARB(int buffers_size, long buffers);

	/** Overloads glDrawBuffersARB. */
	public static void glDrawBuffersARB(int buffer) {
		nglDrawBuffersARB(1, APIUtil.getInt(buffer));
	}
}
