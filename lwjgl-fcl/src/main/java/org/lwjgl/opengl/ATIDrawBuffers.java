/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ATIDrawBuffers {

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_MAX_DRAW_BUFFERS_ATI = 0x8824,
		GL_DRAW_BUFFER0_ATI = 0x8825,
		GL_DRAW_BUFFER1_ATI = 0x8826,
		GL_DRAW_BUFFER2_ATI = 0x8827,
		GL_DRAW_BUFFER3_ATI = 0x8828,
		GL_DRAW_BUFFER4_ATI = 0x8829,
		GL_DRAW_BUFFER5_ATI = 0x882A,
		GL_DRAW_BUFFER6_ATI = 0x882B,
		GL_DRAW_BUFFER7_ATI = 0x882C,
		GL_DRAW_BUFFER8_ATI = 0x882D,
		GL_DRAW_BUFFER9_ATI = 0x882E,
		GL_DRAW_BUFFER10_ATI = 0x882F,
		GL_DRAW_BUFFER11_ATI = 0x8830,
		GL_DRAW_BUFFER12_ATI = 0x8831,
		GL_DRAW_BUFFER13_ATI = 0x8832,
		GL_DRAW_BUFFER14_ATI = 0x8833,
		GL_DRAW_BUFFER15_ATI = 0x8834;

	private ATIDrawBuffers() {}

	public static void glDrawBuffersATI(IntBuffer buffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawBuffersATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglDrawBuffersATI(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
	}
	static native void nglDrawBuffersATI(int buffers_size, long buffers, long function_pointer);

	/** Overloads glDrawBuffersATI. */
	public static void glDrawBuffersATI(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawBuffersATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawBuffersATI(1, APIUtil.getInt(caps, buffer), function_pointer);
	}
}
