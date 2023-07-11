/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVDrawBuffers {

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_MAX_DRAW_BUFFERS_NV = 0x8824,
		GL_DRAW_BUFFER0_NV = 0x8825,
		GL_DRAW_BUFFER1_NV = 0x8826,
		GL_DRAW_BUFFER2_NV = 0x8827,
		GL_DRAW_BUFFER3_NV = 0x8828,
		GL_DRAW_BUFFER4_NV = 0x8829,
		GL_DRAW_BUFFER5_NV = 0x882A,
		GL_DRAW_BUFFER6_NV = 0x882B,
		GL_DRAW_BUFFER7_NV = 0x882C,
		GL_DRAW_BUFFER8_NV = 0x882D,
		GL_DRAW_BUFFER9_NV = 0x882E,
		GL_DRAW_BUFFER10_NV = 0x882F,
		GL_DRAW_BUFFER11_NV = 0x8830,
		GL_DRAW_BUFFER12_NV = 0x8831,
		GL_DRAW_BUFFER13_NV = 0x8832,
		GL_DRAW_BUFFER14_NV = 0x8833,
		GL_DRAW_BUFFER15_NV = 0x8834;

	/**
	 * Accepted by the &lt;bufs&gt; parameter of DrawBuffersNV: 
	 */
	public static final int GL_COLOR_ATTACHMENT0_NV = 0x8CE0,
		GL_COLOR_ATTACHMENT1_NV = 0x8CE1,
		GL_COLOR_ATTACHMENT2_NV = 0x8CE2,
		GL_COLOR_ATTACHMENT3_NV = 0x8CE3,
		GL_COLOR_ATTACHMENT4_NV = 0x8CE4,
		GL_COLOR_ATTACHMENT5_NV = 0x8CE5,
		GL_COLOR_ATTACHMENT6_NV = 0x8CE6,
		GL_COLOR_ATTACHMENT7_NV = 0x8CE7,
		GL_COLOR_ATTACHMENT8_NV = 0x8CE8,
		GL_COLOR_ATTACHMENT9_NV = 0x8CE9,
		GL_COLOR_ATTACHMENT10_NV = 0x8CEA,
		GL_COLOR_ATTACHMENT11_NV = 0x8CEB,
		GL_COLOR_ATTACHMENT12_NV = 0x8CEC,
		GL_COLOR_ATTACHMENT13_NV = 0x8CED,
		GL_COLOR_ATTACHMENT14_NV = 0x8CEE,
		GL_COLOR_ATTACHMENT15_NV = 0x8CEF;

	private NVDrawBuffers() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glDrawBuffersNV(IntBuffer bufs) {
		BufferChecks.checkDirect(bufs);
		nglDrawBuffersNV(bufs.remaining(), MemoryUtil.getAddress(bufs));
	}
	static native void nglDrawBuffersNV(int bufs_n, long bufs);

	/** Overloads glDrawBuffersNV. */
	public static void glDrawBuffersNV(int buf) {
		nglDrawBuffersNV(1, APIUtil.getInt(buf));
	}
}
