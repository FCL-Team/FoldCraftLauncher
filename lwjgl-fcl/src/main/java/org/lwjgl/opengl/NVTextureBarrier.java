/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVTextureBarrier {

	private NVTextureBarrier() {}

	public static void glTextureBarrierNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureBarrierNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureBarrierNV(function_pointer);
	}
	static native void nglTextureBarrierNV(long function_pointer);
}
