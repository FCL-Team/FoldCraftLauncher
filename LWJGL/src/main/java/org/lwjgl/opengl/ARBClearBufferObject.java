/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBClearBufferObject {

	private ARBClearBufferObject() {}

	public static void glClearBufferData(int target, int internalformat, int format, int type, ByteBuffer data) {
		GL43.glClearBufferData(target, internalformat, format, type, data);
	}

	public static void glClearBufferSubData(int target, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
		GL43.glClearBufferSubData(target, internalformat, offset, size, format, type, data);
	}

	public static void glClearNamedBufferDataEXT(int buffer, int internalformat, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearNamedBufferDataEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 1);
		nglClearNamedBufferDataEXT(buffer, internalformat, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglClearNamedBufferDataEXT(int buffer, int internalformat, int format, int type, long data, long function_pointer);

	public static void glClearNamedBufferSubDataEXT(int buffer, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearNamedBufferSubDataEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 1);
		nglClearNamedBufferSubDataEXT(buffer, internalformat, offset, size, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglClearNamedBufferSubDataEXT(int buffer, int internalformat, long offset, long size, int format, int type, long data, long function_pointer);
}
