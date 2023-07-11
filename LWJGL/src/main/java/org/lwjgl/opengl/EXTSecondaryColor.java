/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTSecondaryColor {

	public static final int GL_COLOR_SUM_EXT = 0x8458,
		GL_CURRENT_SECONDARY_COLOR_EXT = 0x8459,
		GL_SECONDARY_COLOR_ARRAY_SIZE_EXT = 0x845A,
		GL_SECONDARY_COLOR_ARRAY_TYPE_EXT = 0x845B,
		GL_SECONDARY_COLOR_ARRAY_STRIDE_EXT = 0x845C,
		GL_SECONDARY_COLOR_ARRAY_POINTER_EXT = 0x845D,
		GL_SECONDARY_COLOR_ARRAY_EXT = 0x845E;

	private EXTSecondaryColor() {}

	public static void glSecondaryColor3bEXT(byte red, byte green, byte blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColor3bEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColor3bEXT(red, green, blue, function_pointer);
	}
	static native void nglSecondaryColor3bEXT(byte red, byte green, byte blue, long function_pointer);

	public static void glSecondaryColor3fEXT(float red, float green, float blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColor3fEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColor3fEXT(red, green, blue, function_pointer);
	}
	static native void nglSecondaryColor3fEXT(float red, float green, float blue, long function_pointer);

	public static void glSecondaryColor3dEXT(double red, double green, double blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColor3dEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColor3dEXT(red, green, blue, function_pointer);
	}
	static native void nglSecondaryColor3dEXT(double red, double green, double blue, long function_pointer);

	public static void glSecondaryColor3ubEXT(byte red, byte green, byte blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColor3ubEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColor3ubEXT(red, green, blue, function_pointer);
	}
	static native void nglSecondaryColor3ubEXT(byte red, byte green, byte blue, long function_pointer);

	public static void glSecondaryColorPointerEXT(int size, int stride, DoubleBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer;
		nglSecondaryColorPointerEXT(size, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glSecondaryColorPointerEXT(int size, int stride, FloatBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer;
		nglSecondaryColorPointerEXT(size, GL11.GL_FLOAT, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glSecondaryColorPointerEXT(int size, boolean unsigned, int stride, ByteBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pPointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = pPointer;
		nglSecondaryColorPointerEXT(size, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, stride, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	static native void nglSecondaryColorPointerEXT(int size, int type, int stride, long pPointer, long function_pointer);
	public static void glSecondaryColorPointerEXT(int size, int type, int stride, long pPointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglSecondaryColorPointerEXTBO(size, type, stride, pPointer_buffer_offset, function_pointer);
	}
	static native void nglSecondaryColorPointerEXTBO(int size, int type, int stride, long pPointer_buffer_offset, long function_pointer);
}
