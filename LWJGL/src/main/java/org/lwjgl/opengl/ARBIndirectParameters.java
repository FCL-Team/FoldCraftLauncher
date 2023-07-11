/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBIndirectParameters {

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 *  GetBufferPointerv, MapBufferRange, FlushMappedBufferRange,
	 *  GetBufferParameteriv, and CopyBufferSubData:
	 */
	public static final int GL_PARAMETER_BUFFER_ARB = 0x80EE;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetIntegerv, GetBooleanv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_PARAMETER_BUFFER_BINDING_ARB = 0x80EF;

	private ARBIndirectParameters() {}

	public static void glMultiDrawArraysIndirectCountARB(int mode, ByteBuffer indirect, long drawcount, int maxdrawcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 4 * 4 : stride) * maxdrawcount);
		nglMultiDrawArraysIndirectCountARB(mode, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
	}
	static native void nglMultiDrawArraysIndirectCountARB(int mode, long indirect, long drawcount, int maxdrawcount, int stride, long function_pointer);
	public static void glMultiDrawArraysIndirectCountARB(int mode, long indirect_buffer_offset, long drawcount, int maxdrawcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglMultiDrawArraysIndirectCountARBBO(mode, indirect_buffer_offset, drawcount, maxdrawcount, stride, function_pointer);
	}
	static native void nglMultiDrawArraysIndirectCountARBBO(int mode, long indirect_buffer_offset, long drawcount, int maxdrawcount, int stride, long function_pointer);

	/** Overloads glMultiDrawArraysIndirectCountARB. */
	public static void glMultiDrawArraysIndirectCountARB(int mode, IntBuffer indirect, long drawcount, int maxdrawcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArraysIndirectCountARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 4 : stride >> 2) * maxdrawcount);
		nglMultiDrawArraysIndirectCountARB(mode, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
	}

	public static void glMultiDrawElementsIndirectCountARB(int mode, int type, ByteBuffer indirect, long drawcount, int maxdrawcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 5 * 4 : stride) * maxdrawcount);
		nglMultiDrawElementsIndirectCountARB(mode, type, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
	}
	static native void nglMultiDrawElementsIndirectCountARB(int mode, int type, long indirect, long drawcount, int maxdrawcount, int stride, long function_pointer);
	public static void glMultiDrawElementsIndirectCountARB(int mode, int type, long indirect_buffer_offset, long drawcount, int maxdrawcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglMultiDrawElementsIndirectCountARBBO(mode, type, indirect_buffer_offset, drawcount, maxdrawcount, stride, function_pointer);
	}
	static native void nglMultiDrawElementsIndirectCountARBBO(int mode, int type, long indirect_buffer_offset, long drawcount, int maxdrawcount, int stride, long function_pointer);

	/** Overloads glMultiDrawElementsIndirectCountARB. */
	public static void glMultiDrawElementsIndirectCountARB(int mode, int type, IntBuffer indirect, long drawcount, int maxdrawcount, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawElementsIndirectCountARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, (stride == 0 ? 5 : stride >> 2) * maxdrawcount);
		nglMultiDrawElementsIndirectCountARB(mode, type, MemoryUtil.getAddress(indirect), drawcount, maxdrawcount, stride, function_pointer);
	}
}
