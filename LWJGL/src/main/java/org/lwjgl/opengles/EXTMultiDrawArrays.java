/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTMultiDrawArrays {

	private EXTMultiDrawArrays() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glMultiDrawArraysEXT(int mode, IntBuffer first, IntBuffer count) {
		BufferChecks.checkDirect(first);
		BufferChecks.checkBuffer(count, first.remaining());
		nglMultiDrawArraysEXT(mode, MemoryUtil.getAddress(first), MemoryUtil.getAddress(count), first.remaining());
	}
	static native void nglMultiDrawArraysEXT(int mode, long first, long count, int first_primcount);
}
