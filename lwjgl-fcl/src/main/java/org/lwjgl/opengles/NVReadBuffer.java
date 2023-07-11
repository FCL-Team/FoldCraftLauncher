/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVReadBuffer {

	public static final int GL_READ_BUFFER_NV = 0xC02,
		GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_NV = 0x8CDC;

	private NVReadBuffer() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glReadBufferNV(int mode) {
		nglReadBufferNV(mode);
	}
	static native void nglReadBufferNV(int mode);
}
