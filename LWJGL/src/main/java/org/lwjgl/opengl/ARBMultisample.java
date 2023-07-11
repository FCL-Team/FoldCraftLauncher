/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBMultisample {

	public static final int GL_MULTISAMPLE_ARB = 0x809D,
		GL_SAMPLE_ALPHA_TO_COVERAGE_ARB = 0x809E,
		GL_SAMPLE_ALPHA_TO_ONE_ARB = 0x809F,
		GL_SAMPLE_COVERAGE_ARB = 0x80A0,
		GL_SAMPLE_BUFFERS_ARB = 0x80A8,
		GL_SAMPLES_ARB = 0x80A9,
		GL_SAMPLE_COVERAGE_VALUE_ARB = 0x80AA,
		GL_SAMPLE_COVERAGE_INVERT_ARB = 0x80AB,
		GL_MULTISAMPLE_BIT_ARB = 0x20000000;

	private ARBMultisample() {}

	public static void glSampleCoverageARB(float value, boolean invert) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSampleCoverageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSampleCoverageARB(value, invert, function_pointer);
	}
	static native void nglSampleCoverageARB(float value, boolean invert, long function_pointer);
}
