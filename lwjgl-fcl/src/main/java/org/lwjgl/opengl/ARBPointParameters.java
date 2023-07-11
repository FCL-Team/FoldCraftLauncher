/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBPointParameters {

	public static final int GL_POINT_SIZE_MIN_ARB = 0x8126,
		GL_POINT_SIZE_MAX_ARB = 0x8127,
		GL_POINT_FADE_THRESHOLD_SIZE_ARB = 0x8128,
		GL_POINT_DISTANCE_ATTENUATION_ARB = 0x8129;

	private ARBPointParameters() {}

	public static void glPointParameterfARB(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPointParameterfARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPointParameterfARB(pname, param, function_pointer);
	}
	static native void nglPointParameterfARB(int pname, float param, long function_pointer);

	public static void glPointParameterARB(int pname, FloatBuffer pfParams) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPointParameterfvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pfParams, 4);
		nglPointParameterfvARB(pname, MemoryUtil.getAddress(pfParams), function_pointer);
	}
	static native void nglPointParameterfvARB(int pname, long pfParams, long function_pointer);
}
