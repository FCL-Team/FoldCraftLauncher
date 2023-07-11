/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBRobustness {

	/**
	 * Returned by GetGraphicsResetStatusARB: 
	 */
	public static final int GL_GUILTY_CONTEXT_RESET_ARB = 0x8253,
		GL_INNOCENT_CONTEXT_RESET_ARB = 0x8254,
		GL_UNKNOWN_CONTEXT_RESET_ARB = 0x8255;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_RESET_NOTIFICATION_STRATEGY_ARB = 0x8256;

	/**
	 *  Returned by GetIntegerv and related simple queries when
	 *  &lt;value&gt; is RESET_NOTIFICATION_STRATEGY_ARB:
	 */
	public static final int GL_LOSE_CONTEXT_ON_RESET_ARB = 0x8252,
		GL_NO_RESET_NOTIFICATION_ARB = 0x8261;

	/**
	 * Returned by GetIntegerv when &lt;pname&gt; is CONTEXT_FLAGS: 
	 */
	public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT_ARB = 0x4;

	private ARBRobustness() {}

	public static int glGetGraphicsResetStatusARB() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetGraphicsResetStatusARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetGraphicsResetStatusARB(function_pointer);
		return __result;
	}
	static native int nglGetGraphicsResetStatusARB(long function_pointer);

	public static void glGetnMapdvARB(int target, int query, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMapdvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglGetnMapdvARB(target, query, v.remaining() << 3, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglGetnMapdvARB(int target, int query, int bufSize, long v, long function_pointer);

	public static void glGetnMapfvARB(int target, int query, FloatBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMapfvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglGetnMapfvARB(target, query, v.remaining() << 2, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglGetnMapfvARB(int target, int query, int bufSize, long v, long function_pointer);

	public static void glGetnMapivARB(int target, int query, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMapivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglGetnMapivARB(target, query, v.remaining() << 2, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglGetnMapivARB(int target, int query, int bufSize, long v, long function_pointer);

	public static void glGetnPixelMapfvARB(int map, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnPixelMapfvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglGetnPixelMapfvARB(map, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetnPixelMapfvARB(int map, int bufSize, long values, long function_pointer);

	public static void glGetnPixelMapuivARB(int map, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnPixelMapuivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglGetnPixelMapuivARB(map, values.remaining() << 2, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetnPixelMapuivARB(int map, int bufSize, long values, long function_pointer);

	public static void glGetnPixelMapusvARB(int map, ShortBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnPixelMapusvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglGetnPixelMapusvARB(map, values.remaining() << 1, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetnPixelMapusvARB(int map, int bufSize, long values, long function_pointer);

	public static void glGetnPolygonStippleARB(ByteBuffer pattern) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnPolygonStippleARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pattern);
		nglGetnPolygonStippleARB(pattern.remaining(), MemoryUtil.getAddress(pattern), function_pointer);
	}
	static native void nglGetnPolygonStippleARB(int pattern_bufSize, long pattern, long function_pointer);

	public static void glGetnTexImageARB(int target, int level, int format, int type, ByteBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(img);
		nglGetnTexImageARB(target, level, format, type, img.remaining(), MemoryUtil.getAddress(img), function_pointer);
	}
	public static void glGetnTexImageARB(int target, int level, int format, int type, DoubleBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(img);
		nglGetnTexImageARB(target, level, format, type, (img.remaining() << 3), MemoryUtil.getAddress(img), function_pointer);
	}
	public static void glGetnTexImageARB(int target, int level, int format, int type, FloatBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(img);
		nglGetnTexImageARB(target, level, format, type, (img.remaining() << 2), MemoryUtil.getAddress(img), function_pointer);
	}
	public static void glGetnTexImageARB(int target, int level, int format, int type, IntBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(img);
		nglGetnTexImageARB(target, level, format, type, (img.remaining() << 2), MemoryUtil.getAddress(img), function_pointer);
	}
	public static void glGetnTexImageARB(int target, int level, int format, int type, ShortBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(img);
		nglGetnTexImageARB(target, level, format, type, (img.remaining() << 1), MemoryUtil.getAddress(img), function_pointer);
	}
	static native void nglGetnTexImageARB(int target, int level, int format, int type, int img_bufSize, long img, long function_pointer);
	public static void glGetnTexImageARB(int target, int level, int format, int type, int img_bufSize, long img_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetnTexImageARBBO(target, level, format, type, img_bufSize, img_buffer_offset, function_pointer);
	}
	static native void nglGetnTexImageARBBO(int target, int level, int format, int type, int img_bufSize, long img_buffer_offset, long function_pointer);

	public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixelsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglReadnPixelsARB(x, y, width, height, format, type, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixelsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglReadnPixelsARB(x, y, width, height, format, type, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixelsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglReadnPixelsARB(x, y, width, height, format, type, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixelsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglReadnPixelsARB(x, y, width, height, format, type, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixelsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglReadnPixelsARB(x, y, width, height, format, type, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglReadnPixelsARB(int x, int y, int width, int height, int format, int type, int data_bufSize, long data, long function_pointer);
	public static void glReadnPixelsARB(int x, int y, int width, int height, int format, int type, int data_bufSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixelsARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglReadnPixelsARBBO(x, y, width, height, format, type, data_bufSize, data_buffer_offset, function_pointer);
	}
	static native void nglReadnPixelsARBBO(int x, int y, int width, int height, int format, int type, int data_bufSize, long data_buffer_offset, long function_pointer);

	public static void glGetnColorTableARB(int target, int format, int type, ByteBuffer table) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnColorTableARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(table);
		nglGetnColorTableARB(target, format, type, table.remaining(), MemoryUtil.getAddress(table), function_pointer);
	}
	public static void glGetnColorTableARB(int target, int format, int type, DoubleBuffer table) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnColorTableARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(table);
		nglGetnColorTableARB(target, format, type, (table.remaining() << 3), MemoryUtil.getAddress(table), function_pointer);
	}
	public static void glGetnColorTableARB(int target, int format, int type, FloatBuffer table) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnColorTableARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(table);
		nglGetnColorTableARB(target, format, type, (table.remaining() << 2), MemoryUtil.getAddress(table), function_pointer);
	}
	static native void nglGetnColorTableARB(int target, int format, int type, int table_bufSize, long table, long function_pointer);

	public static void glGetnConvolutionFilterARB(int target, int format, int type, ByteBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnConvolutionFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetnConvolutionFilterARB(target, format, type, image.remaining(), MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glGetnConvolutionFilterARB(int target, int format, int type, DoubleBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnConvolutionFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetnConvolutionFilterARB(target, format, type, (image.remaining() << 3), MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glGetnConvolutionFilterARB(int target, int format, int type, FloatBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnConvolutionFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetnConvolutionFilterARB(target, format, type, (image.remaining() << 2), MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glGetnConvolutionFilterARB(int target, int format, int type, IntBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnConvolutionFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetnConvolutionFilterARB(target, format, type, (image.remaining() << 2), MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glGetnConvolutionFilterARB(int target, int format, int type, ShortBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnConvolutionFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetnConvolutionFilterARB(target, format, type, (image.remaining() << 1), MemoryUtil.getAddress(image), function_pointer);
	}
	static native void nglGetnConvolutionFilterARB(int target, int format, int type, int image_bufSize, long image, long function_pointer);
	public static void glGetnConvolutionFilterARB(int target, int format, int type, int image_bufSize, long image_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnConvolutionFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetnConvolutionFilterARBBO(target, format, type, image_bufSize, image_buffer_offset, function_pointer);
	}
	static native void nglGetnConvolutionFilterARBBO(int target, int format, int type, int image_bufSize, long image_buffer_offset, long function_pointer);

	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, FloatBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ByteBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, row.remaining(), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, FloatBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, DoubleBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 3), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, FloatBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, FloatBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, FloatBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, IntBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 2), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), column.remaining(), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 3), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, FloatBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 2), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, FloatBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetnSeparableFilterARB(int target, int format, int type, ShortBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetnSeparableFilterARB(target, format, type, (row.remaining() << 1), MemoryUtil.getAddress(row), (column.remaining() << 1), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	static native void nglGetnSeparableFilterARB(int target, int format, int type, int row_rowBufSize, long row, int column_columnBufSize, long column, long span, long function_pointer);
	public static void glGetnSeparableFilterARB(int target, int format, int type, int row_rowBufSize, long row_buffer_offset, int column_columnBufSize, long column_buffer_offset, long span_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnSeparableFilterARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetnSeparableFilterARBBO(target, format, type, row_rowBufSize, row_buffer_offset, column_columnBufSize, column_buffer_offset, span_buffer_offset, function_pointer);
	}
	static native void nglGetnSeparableFilterARBBO(int target, int format, int type, int row_rowBufSize, long row_buffer_offset, int column_columnBufSize, long column_buffer_offset, long span_buffer_offset, long function_pointer);

	public static void glGetnHistogramARB(int target, boolean reset, int format, int type, ByteBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnHistogramARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnHistogramARB(target, reset, format, type, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetnHistogramARB(int target, boolean reset, int format, int type, DoubleBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnHistogramARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnHistogramARB(target, reset, format, type, (values.remaining() << 3), MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetnHistogramARB(int target, boolean reset, int format, int type, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnHistogramARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnHistogramARB(target, reset, format, type, (values.remaining() << 2), MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetnHistogramARB(int target, boolean reset, int format, int type, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnHistogramARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnHistogramARB(target, reset, format, type, (values.remaining() << 2), MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetnHistogramARB(int target, boolean reset, int format, int type, ShortBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnHistogramARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnHistogramARB(target, reset, format, type, (values.remaining() << 1), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetnHistogramARB(int target, boolean reset, int format, int type, int values_bufSize, long values, long function_pointer);
	public static void glGetnHistogramARB(int target, boolean reset, int format, int type, int values_bufSize, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnHistogramARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetnHistogramARBBO(target, reset, format, type, values_bufSize, values_buffer_offset, function_pointer);
	}
	static native void nglGetnHistogramARBBO(int target, boolean reset, int format, int type, int values_bufSize, long values_buffer_offset, long function_pointer);

	public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, ByteBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMinmaxARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnMinmaxARB(target, reset, format, type, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, DoubleBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMinmaxARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnMinmaxARB(target, reset, format, type, (values.remaining() << 3), MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMinmaxARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnMinmaxARB(target, reset, format, type, (values.remaining() << 2), MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMinmaxARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnMinmaxARB(target, reset, format, type, (values.remaining() << 2), MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, ShortBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMinmaxARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(values);
		nglGetnMinmaxARB(target, reset, format, type, (values.remaining() << 1), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetnMinmaxARB(int target, boolean reset, int format, int type, int values_bufSize, long values, long function_pointer);
	public static void glGetnMinmaxARB(int target, boolean reset, int format, int type, int values_bufSize, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnMinmaxARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetnMinmaxARBBO(target, reset, format, type, values_bufSize, values_buffer_offset, function_pointer);
	}
	static native void nglGetnMinmaxARBBO(int target, boolean reset, int format, int type, int values_bufSize, long values_buffer_offset, long function_pointer);

	public static void glGetnCompressedTexImageARB(int target, int lod, ByteBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnCompressedTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(img);
		nglGetnCompressedTexImageARB(target, lod, img.remaining(), MemoryUtil.getAddress(img), function_pointer);
	}
	public static void glGetnCompressedTexImageARB(int target, int lod, IntBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnCompressedTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(img);
		nglGetnCompressedTexImageARB(target, lod, (img.remaining() << 2), MemoryUtil.getAddress(img), function_pointer);
	}
	public static void glGetnCompressedTexImageARB(int target, int lod, ShortBuffer img) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnCompressedTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(img);
		nglGetnCompressedTexImageARB(target, lod, (img.remaining() << 1), MemoryUtil.getAddress(img), function_pointer);
	}
	static native void nglGetnCompressedTexImageARB(int target, int lod, int img_bufSize, long img, long function_pointer);
	public static void glGetnCompressedTexImageARB(int target, int lod, int img_bufSize, long img_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnCompressedTexImageARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetnCompressedTexImageARBBO(target, lod, img_bufSize, img_buffer_offset, function_pointer);
	}
	static native void nglGetnCompressedTexImageARBBO(int target, int lod, int img_bufSize, long img_buffer_offset, long function_pointer);

	public static void glGetnUniformfvARB(int program, int location, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnUniformfvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetnUniformfvARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetnUniformfvARB(int program, int location, int bufSize, long params, long function_pointer);

	public static void glGetnUniformivARB(int program, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnUniformivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetnUniformivARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetnUniformivARB(int program, int location, int bufSize, long params, long function_pointer);

	public static void glGetnUniformuivARB(int program, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnUniformuivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetnUniformuivARB(program, location, params.remaining() << 2, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetnUniformuivARB(int program, int location, int bufSize, long params, long function_pointer);

	public static void glGetnUniformdvARB(int program, int location, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnUniformdvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetnUniformdvARB(program, location, params.remaining() << 3, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetnUniformdvARB(int program, int location, int bufSize, long params, long function_pointer);
}
