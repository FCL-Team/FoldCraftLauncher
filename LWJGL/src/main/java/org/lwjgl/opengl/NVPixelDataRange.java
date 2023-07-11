/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVPixelDataRange {

	/**
	 *  Accepted by the &lt;target&gt; parameter of PixelDataRangeNV and
	 *  FlushPixelDataRangeNV, and by the &lt;cap&gt; parameter of
	 *  EnableClientState, DisableClientState, and IsEnabled:
	 */
	public static final int GL_WRITE_PIXEL_DATA_RANGE_NV = 0x8878,
		GL_READ_PIXEL_DATA_RANGE_NV = 0x8879;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_WRITE_PIXEL_DATA_RANGE_LENGTH_NV = 0x887A,
		GL_READ_PIXEL_DATA_RANGE_LENGTH_NV = 0x887B;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetPointerv:
	 */
	public static final int GL_WRITE_PIXEL_DATA_RANGE_POINTER_NV = 0x887C,
		GL_READ_PIXEL_DATA_RANGE_POINTER_NV = 0x887D;

	private NVPixelDataRange() {}

	public static void glPixelDataRangeNV(int target, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelDataRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglPixelDataRangeNV(target, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glPixelDataRangeNV(int target, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelDataRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglPixelDataRangeNV(target, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glPixelDataRangeNV(int target, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelDataRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglPixelDataRangeNV(target, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glPixelDataRangeNV(int target, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelDataRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglPixelDataRangeNV(target, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glPixelDataRangeNV(int target, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPixelDataRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglPixelDataRangeNV(target, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglPixelDataRangeNV(int target, int data_length, long data, long function_pointer);

	public static void glFlushPixelDataRangeNV(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFlushPixelDataRangeNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFlushPixelDataRangeNV(target, function_pointer);
	}
	static native void nglFlushPixelDataRangeNV(int target, long function_pointer);
}
