/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTDrawRangeElements {

	public static final int GL_MAX_ELEMENTS_VERTICES_EXT = 0x80E8,
		GL_MAX_ELEMENTS_INDICES_EXT = 0x80E9;

	private EXTDrawRangeElements() {}

	public static void glDrawRangeElementsEXT(int mode, int start, int end, ByteBuffer pIndices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementsEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(pIndices);
		nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(pIndices), function_pointer);
	}
	public static void glDrawRangeElementsEXT(int mode, int start, int end, IntBuffer pIndices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementsEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(pIndices);
		nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(pIndices), function_pointer);
	}
	public static void glDrawRangeElementsEXT(int mode, int start, int end, ShortBuffer pIndices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementsEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(pIndices);
		nglDrawRangeElementsEXT(mode, start, end, pIndices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(pIndices), function_pointer);
	}
	static native void nglDrawRangeElementsEXT(int mode, int start, int end, int pIndices_count, int type, long pIndices, long function_pointer);
	public static void glDrawRangeElementsEXT(int mode, int start, int end, int pIndices_count, int type, long pIndices_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementsEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawRangeElementsEXTBO(mode, start, end, pIndices_count, type, pIndices_buffer_offset, function_pointer);
	}
	static native void nglDrawRangeElementsEXTBO(int mode, int start, int end, int pIndices_count, int type, long pIndices_buffer_offset, long function_pointer);
}
