/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVBindlessMultiDrawIndirect {

	private NVBindlessMultiDrawIndirect() {}

	public static void glMultiDrawArraysIndirectBindlessNV(int mode, ByteBuffer indirect, int drawCount, int stride, int vertexBufferCount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirectBindlessNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 20 + 24 * vertexBufferCount : stride) * drawCount);
		nglMultiDrawArraysIndirectBindlessNV(mode, MemoryUtil.getAddress(indirect), drawCount, stride, vertexBufferCount, function_pointer);
	}
	static native void nglMultiDrawArraysIndirectBindlessNV(int mode, long indirect, int drawCount, int stride, int vertexBufferCount, long function_pointer);
	public static void glMultiDrawArraysIndirectBindlessNV(int mode, long indirect_buffer_offset, int drawCount, int stride, int vertexBufferCount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirectBindlessNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglMultiDrawArraysIndirectBindlessNVBO(mode, indirect_buffer_offset, drawCount, stride, vertexBufferCount, function_pointer);
	}
	static native void nglMultiDrawArraysIndirectBindlessNVBO(int mode, long indirect_buffer_offset, int drawCount, int stride, int vertexBufferCount, long function_pointer);

	public static void glMultiDrawElementsIndirectBindlessNV(int mode, int type, ByteBuffer indirect, int drawCount, int stride, int vertexBufferCount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirectBindlessNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 48 + 24 * vertexBufferCount : stride) * drawCount);
		nglMultiDrawElementsIndirectBindlessNV(mode, type, MemoryUtil.getAddress(indirect), drawCount, stride, vertexBufferCount, function_pointer);
	}
	static native void nglMultiDrawElementsIndirectBindlessNV(int mode, int type, long indirect, int drawCount, int stride, int vertexBufferCount, long function_pointer);
	public static void glMultiDrawElementsIndirectBindlessNV(int mode, int type, long indirect_buffer_offset, int drawCount, int stride, int vertexBufferCount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirectBindlessNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglMultiDrawElementsIndirectBindlessNVBO(mode, type, indirect_buffer_offset, drawCount, stride, vertexBufferCount, function_pointer);
	}
	static native void nglMultiDrawElementsIndirectBindlessNVBO(int mode, int type, long indirect_buffer_offset, int drawCount, int stride, int vertexBufferCount, long function_pointer);
}
