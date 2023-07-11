/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVCopyImage {

	private NVCopyImage() {}

	public static void glCopyImageSubDataNV(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int width, int height, int depth) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyImageSubDataNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyImageSubDataNV(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, width, height, depth, function_pointer);
	}
	static native void nglCopyImageSubDataNV(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int width, int height, int depth, long function_pointer);
}
