/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GREMEDYFrameTerminator {

	private GREMEDYFrameTerminator() {}

	public static void glFrameTerminatorGREMEDY() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFrameTerminatorGREMEDY;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFrameTerminatorGREMEDY(function_pointer);
	}
	static native void nglFrameTerminatorGREMEDY(long function_pointer);
}
