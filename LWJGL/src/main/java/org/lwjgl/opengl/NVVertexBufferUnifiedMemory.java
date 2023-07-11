/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVVertexBufferUnifiedMemory {

	/**
	 *  Accepted by the &lt;cap&gt; parameter of DisableClientState,
	 *  EnableClientState, IsEnabled:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_UNIFIED_NV = 0x8F1E,
		GL_ELEMENT_ARRAY_UNIFIED_NV = 0x8F1F;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of BufferAddressRangeNV
	 *  and the &lt;value&gt; parameter of GetIntegerui64i_vNV:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_ADDRESS_NV = 0x8F20,
		GL_TEXTURE_COORD_ARRAY_ADDRESS_NV = 0x8F25;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of BufferAddressRangeNV
	 *  and the &lt;value&gt; parameter of GetIntegerui64vNV:
	 */
	public static final int GL_VERTEX_ARRAY_ADDRESS_NV = 0x8F21,
		GL_NORMAL_ARRAY_ADDRESS_NV = 0x8F22,
		GL_COLOR_ARRAY_ADDRESS_NV = 0x8F23,
		GL_INDEX_ARRAY_ADDRESS_NV = 0x8F24,
		GL_EDGE_FLAG_ARRAY_ADDRESS_NV = 0x8F26,
		GL_SECONDARY_COLOR_ARRAY_ADDRESS_NV = 0x8F27,
		GL_FOG_COORD_ARRAY_ADDRESS_NV = 0x8F28,
		GL_ELEMENT_ARRAY_ADDRESS_NV = 0x8F29;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetIntegeri_vNV: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_LENGTH_NV = 0x8F2A,
		GL_TEXTURE_COORD_ARRAY_LENGTH_NV = 0x8F2F;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetIntegerv: 
	 */
	public static final int GL_VERTEX_ARRAY_LENGTH_NV = 0x8F2B,
		GL_NORMAL_ARRAY_LENGTH_NV = 0x8F2C,
		GL_COLOR_ARRAY_LENGTH_NV = 0x8F2D,
		GL_INDEX_ARRAY_LENGTH_NV = 0x8F2E,
		GL_EDGE_FLAG_ARRAY_LENGTH_NV = 0x8F30,
		GL_SECONDARY_COLOR_ARRAY_LENGTH_NV = 0x8F31,
		GL_FOG_COORD_ARRAY_LENGTH_NV = 0x8F32,
		GL_ELEMENT_ARRAY_LENGTH_NV = 0x8F33;

	private NVVertexBufferUnifiedMemory() {}

	public static void glBufferAddressRangeNV(int pname, int index, long address, long length) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferAddressRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBufferAddressRangeNV(pname, index, address, length, function_pointer);
	}
	static native void nglBufferAddressRangeNV(int pname, int index, long address, long length, long function_pointer);

	public static void glVertexFormatNV(int size, int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexFormatNV(size, type, stride, function_pointer);
	}
	static native void nglVertexFormatNV(int size, int type, int stride, long function_pointer);

	public static void glNormalFormatNV(int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormalFormatNV(type, stride, function_pointer);
	}
	static native void nglNormalFormatNV(int type, int stride, long function_pointer);

	public static void glColorFormatNV(int size, int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorFormatNV(size, type, stride, function_pointer);
	}
	static native void nglColorFormatNV(int size, int type, int stride, long function_pointer);

	public static void glIndexFormatNV(int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIndexFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglIndexFormatNV(type, stride, function_pointer);
	}
	static native void nglIndexFormatNV(int type, int stride, long function_pointer);

	public static void glTexCoordFormatNV(int size, int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoordFormatNV(size, type, stride, function_pointer);
	}
	static native void nglTexCoordFormatNV(int size, int type, int stride, long function_pointer);

	public static void glEdgeFlagFormatNV(int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEdgeFlagFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEdgeFlagFormatNV(stride, function_pointer);
	}
	static native void nglEdgeFlagFormatNV(int stride, long function_pointer);

	public static void glSecondaryColorFormatNV(int size, int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColorFormatNV(size, type, stride, function_pointer);
	}
	static native void nglSecondaryColorFormatNV(int size, int type, int stride, long function_pointer);

	public static void glFogCoordFormatNV(int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFogCoordFormatNV(type, stride, function_pointer);
	}
	static native void nglFogCoordFormatNV(int type, int stride, long function_pointer);

	public static void glVertexAttribFormatNV(int index, int size, int type, boolean normalized, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribFormatNV(index, size, type, normalized, stride, function_pointer);
	}
	static native void nglVertexAttribFormatNV(int index, int size, int type, boolean normalized, int stride, long function_pointer);

	public static void glVertexAttribIFormatNV(int index, int size, int type, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribIFormatNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribIFormatNV(index, size, type, stride, function_pointer);
	}
	static native void nglVertexAttribIFormatNV(int index, int size, int type, int stride, long function_pointer);

	public static void glGetIntegeruNV(int value, int index, LongBuffer result) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetIntegerui64i_vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(result, 1);
		nglGetIntegerui64i_vNV(value, index, MemoryUtil.getAddress(result), function_pointer);
	}
	static native void nglGetIntegerui64i_vNV(int value, int index, long result, long function_pointer);

	/** Overloads glGetIntegerui64i_vNV. */
	public static long glGetIntegerui64NV(int value, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetIntegerui64i_vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer result = APIUtil.getBufferLong(caps);
		nglGetIntegerui64i_vNV(value, index, MemoryUtil.getAddress(result), function_pointer);
		return result.get(0);
	}
}
