/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBDrawBuffersBlend {

	private ARBDrawBuffersBlend() {}

	public static void glBlendEquationiARB(int buf, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendEquationiARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendEquationiARB(buf, mode, function_pointer);
	}
	static native void nglBlendEquationiARB(int buf, int mode, long function_pointer);

	public static void glBlendEquationSeparateiARB(int buf, int modeRGB, int modeAlpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendEquationSeparateiARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendEquationSeparateiARB(buf, modeRGB, modeAlpha, function_pointer);
	}
	static native void nglBlendEquationSeparateiARB(int buf, int modeRGB, int modeAlpha, long function_pointer);

	public static void glBlendFunciARB(int buf, int src, int dst) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendFunciARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendFunciARB(buf, src, dst, function_pointer);
	}
	static native void nglBlendFunciARB(int buf, int src, int dst, long function_pointer);

	public static void glBlendFuncSeparateiARB(int buf, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendFuncSeparateiARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendFuncSeparateiARB(buf, srcRGB, dstRGB, srcAlpha, dstAlpha, function_pointer);
	}
	static native void nglBlendFuncSeparateiARB(int buf, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha, long function_pointer);
}
