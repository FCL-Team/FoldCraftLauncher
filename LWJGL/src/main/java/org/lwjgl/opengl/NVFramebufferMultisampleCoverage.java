/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVFramebufferMultisampleCoverage {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameterivEXT. 
	 */
	public static final int GL_RENDERBUFFER_COVERAGE_SAMPLES_NV = 0x8CAB,
		GL_RENDERBUFFER_COLOR_SAMPLES_NV = 0x8E10;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv. 
	 */
	public static final int GL_MAX_MULTISAMPLE_COVERAGE_MODES_NV = 0x8E11,
		GL_MULTISAMPLE_COVERAGE_MODES_NV = 0x8E12;

	private NVFramebufferMultisampleCoverage() {}

	public static void glRenderbufferStorageMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRenderbufferStorageMultisampleCoverageNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRenderbufferStorageMultisampleCoverageNV(target, coverageSamples, colorSamples, internalformat, width, height, function_pointer);
	}
	static native void nglRenderbufferStorageMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalformat, int width, int height, long function_pointer);
}
