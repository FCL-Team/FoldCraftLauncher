/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

/**
 *  <p/>
 *  The GL12 imaging subset extension.
 * <p>
 *  @author cix_foo <cix_foo@users.sourceforge.net>
 *  @version $Revision$
 *  $Id$
 */
public final class ARBImaging {

	public static final int GL_BLEND_COLOR = 0x8005,
		GL_FUNC_ADD = 0x8006,
		GL_MIN = 0x8007,
		GL_MAX = 0x8008,
		GL_BLEND_EQUATION = 0x8009,
		GL_FUNC_SUBTRACT = 0x800A,
		GL_FUNC_REVERSE_SUBTRACT = 0x800B,
		GL_COLOR_MATRIX = 0x80B1,
		GL_COLOR_MATRIX_STACK_DEPTH = 0x80B2,
		GL_MAX_COLOR_MATRIX_STACK_DEPTH = 0x80B3,
		GL_POST_COLOR_MATRIX_RED_SCALE = 0x80B4,
		GL_POST_COLOR_MATRIX_GREEN_SCALE = 0x80B5,
		GL_POST_COLOR_MATRIX_BLUE_SCALE = 0x80B6,
		GL_POST_COLOR_MATRIX_ALPHA_SCALE = 0x80B7,
		GL_POST_COLOR_MATRIX_RED_BIAS = 0x80B8,
		GL_POST_COLOR_MATRIX_GREEN_BIAS = 0x80B9,
		GL_POST_COLOR_MATRIX_BLUE_BIAS = 0x80BA,
		GL_POST_COLOR_MATRIX_ALPHA_BIAS = 0x80BB,
		GL_COLOR_TABLE = 0x80D0,
		GL_POST_CONVOLUTION_COLOR_TABLE = 0x80D1,
		GL_POST_COLOR_MATRIX_COLOR_TABLE = 0x80D2,
		GL_PROXY_COLOR_TABLE = 0x80D3,
		GL_PROXY_POST_CONVOLUTION_COLOR_TABLE = 0x80D4,
		GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE = 0x80D5,
		GL_COLOR_TABLE_SCALE = 0x80D6,
		GL_COLOR_TABLE_BIAS = 0x80D7,
		GL_COLOR_TABLE_FORMAT = 0x80D8,
		GL_COLOR_TABLE_WIDTH = 0x80D9,
		GL_COLOR_TABLE_RED_SIZE = 0x80DA,
		GL_COLOR_TABLE_GREEN_SIZE = 0x80DB,
		GL_COLOR_TABLE_BLUE_SIZE = 0x80DC,
		GL_COLOR_TABLE_ALPHA_SIZE = 0x80DD,
		GL_COLOR_TABLE_LUMINANCE_SIZE = 0x80DE,
		GL_COLOR_TABLE_INTENSITY_SIZE = 0x80DF,
		GL_CONVOLUTION_1D = 0x8010,
		GL_CONVOLUTION_2D = 0x8011,
		GL_SEPARABLE_2D = 0x8012,
		GL_CONVOLUTION_BORDER_MODE = 0x8013,
		GL_CONVOLUTION_FILTER_SCALE = 0x8014,
		GL_CONVOLUTION_FILTER_BIAS = 0x8015,
		GL_REDUCE = 0x8016,
		GL_CONVOLUTION_FORMAT = 0x8017,
		GL_CONVOLUTION_WIDTH = 0x8018,
		GL_CONVOLUTION_HEIGHT = 0x8019,
		GL_MAX_CONVOLUTION_WIDTH = 0x801A,
		GL_MAX_CONVOLUTION_HEIGHT = 0x801B,
		GL_POST_CONVOLUTION_RED_SCALE = 0x801C,
		GL_POST_CONVOLUTION_GREEN_SCALE = 0x801D,
		GL_POST_CONVOLUTION_BLUE_SCALE = 0x801E,
		GL_POST_CONVOLUTION_ALPHA_SCALE = 0x801F,
		GL_POST_CONVOLUTION_RED_BIAS = 0x8020,
		GL_POST_CONVOLUTION_GREEN_BIAS = 0x8021,
		GL_POST_CONVOLUTION_BLUE_BIAS = 0x8022,
		GL_POST_CONVOLUTION_ALPHA_BIAS = 0x8023,
		GL_IGNORE_BORDER = 0x8150,
		GL_CONSTANT_BORDER = 0x8151,
		GL_REPLICATE_BORDER = 0x8153,
		GL_CONVOLUTION_BORDER_COLOR = 0x8154,
		GL_HISTOGRAM = 0x8024,
		GL_PROXY_HISTOGRAM = 0x8025,
		GL_HISTOGRAM_WIDTH = 0x8026,
		GL_HISTOGRAM_FORMAT = 0x8027,
		GL_HISTOGRAM_RED_SIZE = 0x8028,
		GL_HISTOGRAM_GREEN_SIZE = 0x8029,
		GL_HISTOGRAM_BLUE_SIZE = 0x802A,
		GL_HISTOGRAM_ALPHA_SIZE = 0x802B,
		GL_HISTOGRAM_LUMINANCE_SIZE = 0x802C,
		GL_HISTOGRAM_SINK = 0x802D,
		GL_MINMAX = 0x802E,
		GL_MINMAX_FORMAT = 0x802F,
		GL_MINMAX_SINK = 0x8030,
		GL_TABLE_TOO_LARGE = 0x8031;

	private ARBImaging() {}

	public static void glColorTable(int target, int internalFormat, int width, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(data, 256);
		nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorTable(int target, int internalFormat, int width, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(data, 256);
		nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorTable(int target, int internalFormat, int width, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(data, 256);
		nglColorTable(target, internalFormat, width, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglColorTable(int target, int internalFormat, int width, int format, int type, long data, long function_pointer);
	public static void glColorTable(int target, int internalFormat, int width, int format, int type, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglColorTableBO(target, internalFormat, width, format, type, data_buffer_offset, function_pointer);
	}
	static native void nglColorTableBO(int target, int internalFormat, int width, int format, int type, long data_buffer_offset, long function_pointer);

	public static void glColorSubTable(int target, int start, int count, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(data, 256);
		nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorSubTable(int target, int start, int count, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(data, 256);
		nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glColorSubTable(int target, int start, int count, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(data, 256);
		nglColorSubTable(target, start, count, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglColorSubTable(int target, int start, int count, int format, int type, long data, long function_pointer);
	public static void glColorSubTable(int target, int start, int count, int format, int type, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorSubTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglColorSubTableBO(target, start, count, format, type, data_buffer_offset, function_pointer);
	}
	static native void nglColorSubTableBO(int target, int start, int count, int format, int type, long data_buffer_offset, long function_pointer);

	public static void glColorTableParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTableParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglColorTableParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglColorTableParameteriv(int target, int pname, long params, long function_pointer);

	public static void glColorTableParameter(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorTableParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglColorTableParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglColorTableParameterfv(int target, int pname, long params, long function_pointer);

	public static void glCopyColorSubTable(int target, int start, int x, int y, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyColorSubTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyColorSubTable(target, start, x, y, width, function_pointer);
	}
	static native void nglCopyColorSubTable(int target, int start, int x, int y, int width, long function_pointer);

	public static void glCopyColorTable(int target, int internalformat, int x, int y, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyColorTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyColorTable(target, internalformat, x, y, width, function_pointer);
	}
	static native void nglCopyColorTable(int target, int internalformat, int x, int y, int width, long function_pointer);

	public static void glGetColorTable(int target, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 256);
		nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetColorTable(int target, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 256);
		nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetColorTable(int target, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTable;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 256);
		nglGetColorTable(target, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetColorTable(int target, int format, int type, long data, long function_pointer);

	public static void glGetColorTableParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetColorTableParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetColorTableParameteriv(int target, int pname, long params, long function_pointer);

	public static void glGetColorTableParameter(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetColorTableParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetColorTableParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetColorTableParameterfv(int target, int pname, long params, long function_pointer);

	public static void glBlendEquation(int mode) {
		GL14.glBlendEquation(mode);
	}

	public static void glBlendColor(float red, float green, float blue, float alpha) {
		GL14.glBlendColor(red, green, blue, alpha);
	}

	public static void glHistogram(int target, int width, int internalformat, boolean sink) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glHistogram;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglHistogram(target, width, internalformat, sink, function_pointer);
	}
	static native void nglHistogram(int target, int width, int internalformat, boolean sink, long function_pointer);

	public static void glResetHistogram(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glResetHistogram;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglResetHistogram(target, function_pointer);
	}
	static native void nglResetHistogram(int target, long function_pointer);

	public static void glGetHistogram(int target, boolean reset, int format, int type, ByteBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetHistogram;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 256);
		nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetHistogram(int target, boolean reset, int format, int type, DoubleBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetHistogram;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 256);
		nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetHistogram(int target, boolean reset, int format, int type, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetHistogram;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 256);
		nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetHistogram(int target, boolean reset, int format, int type, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetHistogram;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 256);
		nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetHistogram(int target, boolean reset, int format, int type, ShortBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetHistogram;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 256);
		nglGetHistogram(target, reset, format, type, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetHistogram(int target, boolean reset, int format, int type, long values, long function_pointer);
	public static void glGetHistogram(int target, boolean reset, int format, int type, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetHistogram;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetHistogramBO(target, reset, format, type, values_buffer_offset, function_pointer);
	}
	static native void nglGetHistogramBO(int target, boolean reset, int format, int type, long values_buffer_offset, long function_pointer);

	public static void glGetHistogramParameter(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetHistogramParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 256);
		nglGetHistogramParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetHistogramParameterfv(int target, int pname, long params, long function_pointer);

	public static void glGetHistogramParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetHistogramParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 256);
		nglGetHistogramParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetHistogramParameteriv(int target, int pname, long params, long function_pointer);

	public static void glMinmax(int target, int internalformat, boolean sink) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMinmax;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMinmax(target, internalformat, sink, function_pointer);
	}
	static native void nglMinmax(int target, int internalformat, boolean sink, long function_pointer);

	public static void glResetMinmax(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glResetMinmax;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglResetMinmax(target, function_pointer);
	}
	static native void nglResetMinmax(int target, long function_pointer);

	public static void glGetMinmax(int target, boolean reset, int format, int types, ByteBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMinmax;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 4);
		nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetMinmax(int target, boolean reset, int format, int types, DoubleBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMinmax;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 4);
		nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetMinmax(int target, boolean reset, int format, int types, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMinmax;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 4);
		nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetMinmax(int target, boolean reset, int format, int types, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMinmax;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 4);
		nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
	}
	public static void glGetMinmax(int target, boolean reset, int format, int types, ShortBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMinmax;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkBuffer(values, 4);
		nglGetMinmax(target, reset, format, types, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetMinmax(int target, boolean reset, int format, int types, long values, long function_pointer);
	public static void glGetMinmax(int target, boolean reset, int format, int types, long values_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMinmax;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetMinmaxBO(target, reset, format, types, values_buffer_offset, function_pointer);
	}
	static native void nglGetMinmaxBO(int target, boolean reset, int format, int types, long values_buffer_offset, long function_pointer);

	public static void glGetMinmaxParameter(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMinmaxParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetMinmaxParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetMinmaxParameterfv(int target, int pname, long params, long function_pointer);

	public static void glGetMinmaxParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMinmaxParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetMinmaxParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetMinmaxParameteriv(int target, int pname, long params, long function_pointer);

	public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, ByteBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
		nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, DoubleBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
		nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, FloatBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
		nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, IntBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
		nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, ShortBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, 1, 1));
		nglConvolutionFilter1D(target, internalformat, width, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	static native void nglConvolutionFilter1D(int target, int internalformat, int width, int format, int type, long image, long function_pointer);
	public static void glConvolutionFilter1D(int target, int internalformat, int width, int format, int type, long image_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglConvolutionFilter1DBO(target, internalformat, width, format, type, image_buffer_offset, function_pointer);
	}
	static native void nglConvolutionFilter1DBO(int target, int internalformat, int width, int format, int type, long image_buffer_offset, long function_pointer);

	public static void glConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
		nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
		nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(image, GLChecks.calculateImageStorage(image, format, type, width, height, 1));
		nglConvolutionFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	static native void nglConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, long image, long function_pointer);
	public static void glConvolutionFilter2D(int target, int internalformat, int width, int height, int format, int type, long image_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglConvolutionFilter2DBO(target, internalformat, width, height, format, type, image_buffer_offset, function_pointer);
	}
	static native void nglConvolutionFilter2DBO(int target, int internalformat, int width, int height, int format, int type, long image_buffer_offset, long function_pointer);

	public static void glConvolutionParameterf(int target, int pname, float params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionParameterf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglConvolutionParameterf(target, pname, params, function_pointer);
	}
	static native void nglConvolutionParameterf(int target, int pname, float params, long function_pointer);

	public static void glConvolutionParameter(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglConvolutionParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglConvolutionParameterfv(int target, int pname, long params, long function_pointer);

	public static void glConvolutionParameteri(int target, int pname, int params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglConvolutionParameteri(target, pname, params, function_pointer);
	}
	static native void nglConvolutionParameteri(int target, int pname, int params, long function_pointer);

	public static void glConvolutionParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glConvolutionParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglConvolutionParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglConvolutionParameteriv(int target, int pname, long params, long function_pointer);

	public static void glCopyConvolutionFilter1D(int target, int internalformat, int x, int y, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyConvolutionFilter1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyConvolutionFilter1D(target, internalformat, x, y, width, function_pointer);
	}
	static native void nglCopyConvolutionFilter1D(int target, int internalformat, int x, int y, int width, long function_pointer);

	public static void glCopyConvolutionFilter2D(int target, int internalformat, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyConvolutionFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyConvolutionFilter2D(target, internalformat, x, y, width, height, function_pointer);
	}
	static native void nglCopyConvolutionFilter2D(int target, int internalformat, int x, int y, int width, int height, long function_pointer);

	public static void glGetConvolutionFilter(int target, int format, int type, ByteBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetConvolutionFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glGetConvolutionFilter(int target, int format, int type, DoubleBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetConvolutionFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glGetConvolutionFilter(int target, int format, int type, FloatBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetConvolutionFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glGetConvolutionFilter(int target, int format, int type, IntBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetConvolutionFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	public static void glGetConvolutionFilter(int target, int format, int type, ShortBuffer image) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetConvolutionFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(image);
		nglGetConvolutionFilter(target, format, type, MemoryUtil.getAddress(image), function_pointer);
	}
	static native void nglGetConvolutionFilter(int target, int format, int type, long image, long function_pointer);
	public static void glGetConvolutionFilter(int target, int format, int type, long image_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetConvolutionFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetConvolutionFilterBO(target, format, type, image_buffer_offset, function_pointer);
	}
	static native void nglGetConvolutionFilterBO(int target, int format, int type, long image_buffer_offset, long function_pointer);

	public static void glGetConvolutionParameter(int target, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetConvolutionParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetConvolutionParameterfv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetConvolutionParameterfv(int target, int pname, long params, long function_pointer);

	public static void glGetConvolutionParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetConvolutionParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetConvolutionParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetConvolutionParameteriv(int target, int pname, long params, long function_pointer);

	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, ByteBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, DoubleBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, FloatBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, IntBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ByteBuffer row, ShortBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, ByteBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, DoubleBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, FloatBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, IntBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, DoubleBuffer row, ShortBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, ByteBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, DoubleBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, FloatBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, IntBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, FloatBuffer row, ShortBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, ByteBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, DoubleBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, FloatBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, IntBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, IntBuffer row, ShortBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, ByteBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, DoubleBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, FloatBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, IntBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, ShortBuffer row, ShortBuffer column) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		nglSeparableFilter2D(target, internalformat, width, height, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), function_pointer);
	}
	static native void nglSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, long row, long column, long function_pointer);
	public static void glSeparableFilter2D(int target, int internalformat, int width, int height, int format, int type, long row_buffer_offset, long column_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSeparableFilter2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglSeparableFilter2DBO(target, internalformat, width, height, format, type, row_buffer_offset, column_buffer_offset, function_pointer);
	}
	static native void nglSeparableFilter2DBO(int target, int internalformat, int width, int height, int format, int type, long row_buffer_offset, long column_buffer_offset, long function_pointer);

	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ByteBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, DoubleBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, FloatBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, IntBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ByteBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ByteBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ByteBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ByteBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, DoubleBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, DoubleBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, DoubleBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, DoubleBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, IntBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, IntBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, IntBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, IntBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ShortBuffer column, ByteBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ShortBuffer column, DoubleBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ShortBuffer column, IntBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	public static void glGetSeparableFilter(int target, int format, int type, ShortBuffer row, ShortBuffer column, ShortBuffer span) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(row);
		BufferChecks.checkDirect(column);
		BufferChecks.checkDirect(span);
		nglGetSeparableFilter(target, format, type, MemoryUtil.getAddress(row), MemoryUtil.getAddress(column), MemoryUtil.getAddress(span), function_pointer);
	}
	static native void nglGetSeparableFilter(int target, int format, int type, long row, long column, long span, long function_pointer);
	public static void glGetSeparableFilter(int target, int format, int type, long row_buffer_offset, long column_buffer_offset, long span_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSeparableFilter;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetSeparableFilterBO(target, format, type, row_buffer_offset, column_buffer_offset, span_buffer_offset, function_pointer);
	}
	static native void nglGetSeparableFilterBO(int target, int format, int type, long row_buffer_offset, long column_buffer_offset, long span_buffer_offset, long function_pointer);
}
