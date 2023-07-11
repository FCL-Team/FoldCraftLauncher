/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVTextureMultisample {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameter: 
	 */
	public static final int GL_TEXTURE_COVERAGE_SAMPLES_NV = 0x9045,
		GL_TEXTURE_COLOR_SAMPLES_NV = 0x9046;

	private NVTextureMultisample() {}

	public static void glTexImage2DMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, boolean fixedSampleLocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage2DMultisampleCoverageNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexImage2DMultisampleCoverageNV(target, coverageSamples, colorSamples, internalFormat, width, height, fixedSampleLocations, function_pointer);
	}
	static native void nglTexImage2DMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, boolean fixedSampleLocations, long function_pointer);

	public static void glTexImage3DMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage3DMultisampleCoverageNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexImage3DMultisampleCoverageNV(target, coverageSamples, colorSamples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
	}
	static native void nglTexImage3DMultisampleCoverageNV(int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations, long function_pointer);

	public static void glTextureImage2DMultisampleNV(int texture, int target, int samples, int internalFormat, int width, int height, boolean fixedSampleLocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureImage2DMultisampleNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureImage2DMultisampleNV(texture, target, samples, internalFormat, width, height, fixedSampleLocations, function_pointer);
	}
	static native void nglTextureImage2DMultisampleNV(int texture, int target, int samples, int internalFormat, int width, int height, boolean fixedSampleLocations, long function_pointer);

	public static void glTextureImage3DMultisampleNV(int texture, int target, int samples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureImage3DMultisampleNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureImage3DMultisampleNV(texture, target, samples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
	}
	static native void nglTextureImage3DMultisampleNV(int texture, int target, int samples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations, long function_pointer);

	public static void glTextureImage2DMultisampleCoverageNV(int texture, int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, boolean fixedSampleLocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureImage2DMultisampleCoverageNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureImage2DMultisampleCoverageNV(texture, target, coverageSamples, colorSamples, internalFormat, width, height, fixedSampleLocations, function_pointer);
	}
	static native void nglTextureImage2DMultisampleCoverageNV(int texture, int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, boolean fixedSampleLocations, long function_pointer);

	public static void glTextureImage3DMultisampleCoverageNV(int texture, int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureImage3DMultisampleCoverageNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureImage3DMultisampleCoverageNV(texture, target, coverageSamples, colorSamples, internalFormat, width, height, depth, fixedSampleLocations, function_pointer);
	}
	static native void nglTextureImage3DMultisampleCoverageNV(int texture, int target, int coverageSamples, int colorSamples, int internalFormat, int width, int height, int depth, boolean fixedSampleLocations, long function_pointer);
}
