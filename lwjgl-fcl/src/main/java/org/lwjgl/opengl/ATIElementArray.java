/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ATIElementArray {

	public static final int GL_ELEMENT_ARRAY_ATI = 0x8768,
		GL_ELEMENT_ARRAY_TYPE_ATI = 0x8769,
		GL_ELEMENT_ARRAY_POINTER_ATI = 0x876A;

	private ATIElementArray() {}

	public static void glElementPointerATI(ByteBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glElementPointerATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPointer);
		nglElementPointerATI(GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glElementPointerATI(IntBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glElementPointerATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPointer);
		nglElementPointerATI(GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	public static void glElementPointerATI(ShortBuffer pPointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glElementPointerATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pPointer);
		nglElementPointerATI(GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(pPointer), function_pointer);
	}
	static native void nglElementPointerATI(int type, long pPointer, long function_pointer);

	public static void glDrawElementArrayATI(int mode, int count) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementArrayATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawElementArrayATI(mode, count, function_pointer);
	}
	static native void nglDrawElementArrayATI(int mode, int count, long function_pointer);

	public static void glDrawRangeElementArrayATI(int mode, int start, int end, int count) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementArrayATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawRangeElementArrayATI(mode, start, end, count, function_pointer);
	}
	static native void nglDrawRangeElementArrayATI(int mode, int start, int end, int count, long function_pointer);
}
