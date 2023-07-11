/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

/**
 *  <p/>
 *  The core OpenGL1.4 API.
 * <p>
 *  @author cix_foo <cix_foo@users.sourceforge.net>
 *  @version $Revision$
 *  $Id$
 */
public final class GL14 {

	public static final int GL_GENERATE_MIPMAP = 0x8191,
		GL_GENERATE_MIPMAP_HINT = 0x8192,
		GL_DEPTH_COMPONENT16 = 0x81A5,
		GL_DEPTH_COMPONENT24 = 0x81A6,
		GL_DEPTH_COMPONENT32 = 0x81A7,
		GL_TEXTURE_DEPTH_SIZE = 0x884A,
		GL_DEPTH_TEXTURE_MODE = 0x884B,
		GL_TEXTURE_COMPARE_MODE = 0x884C,
		GL_TEXTURE_COMPARE_FUNC = 0x884D,
		GL_COMPARE_R_TO_TEXTURE = 0x884E,
		GL_FOG_COORDINATE_SOURCE = 0x8450,
		GL_FOG_COORDINATE = 0x8451,
		GL_FRAGMENT_DEPTH = 0x8452,
		GL_CURRENT_FOG_COORDINATE = 0x8453,
		GL_FOG_COORDINATE_ARRAY_TYPE = 0x8454,
		GL_FOG_COORDINATE_ARRAY_STRIDE = 0x8455,
		GL_FOG_COORDINATE_ARRAY_POINTER = 0x8456,
		GL_FOG_COORDINATE_ARRAY = 0x8457,
		GL_POINT_SIZE_MIN = 0x8126,
		GL_POINT_SIZE_MAX = 0x8127,
		GL_POINT_FADE_THRESHOLD_SIZE = 0x8128,
		GL_POINT_DISTANCE_ATTENUATION = 0x8129,
		GL_COLOR_SUM = 0x8458,
		GL_CURRENT_SECONDARY_COLOR = 0x8459,
		GL_SECONDARY_COLOR_ARRAY_SIZE = 0x845A,
		GL_SECONDARY_COLOR_ARRAY_TYPE = 0x845B,
		GL_SECONDARY_COLOR_ARRAY_STRIDE = 0x845C,
		GL_SECONDARY_COLOR_ARRAY_POINTER = 0x845D,
		GL_SECONDARY_COLOR_ARRAY = 0x845E,
		GL_BLEND_DST_RGB = 0x80C8,
		GL_BLEND_SRC_RGB = 0x80C9,
		GL_BLEND_DST_ALPHA = 0x80CA,
		GL_BLEND_SRC_ALPHA = 0x80CB,
		GL_INCR_WRAP = 0x8507,
		GL_DECR_WRAP = 0x8508,
		GL_TEXTURE_FILTER_CONTROL = 0x8500,
		GL_TEXTURE_LOD_BIAS = 0x8501,
		GL_MAX_TEXTURE_LOD_BIAS = 0x84FD,
		GL_MIRRORED_REPEAT = 0x8370,
		GL_BLEND_COLOR = 0x8005,
		GL_BLEND_EQUATION = 0x8009,
		GL_FUNC_ADD = 0x8006,
		GL_FUNC_SUBTRACT = 0x800A,
		GL_FUNC_REVERSE_SUBTRACT = 0x800B,
		GL_MIN = 0x8007,
		GL_MAX = 0x8008;

	private GL14() {}

	public static void glBlendEquation(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendEquation;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendEquation(mode, function_pointer);
	}
	static native void nglBlendEquation(int mode, long function_pointer);

	public static void glBlendColor(float red, float green, float blue, float alpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendColor;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendColor(red, green, blue, alpha, function_pointer);
	}
	static native void nglBlendColor(float red, float green, float blue, float alpha, long function_pointer);

	public static void glFogCoordf(float coord) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFogCoordf(coord, function_pointer);
	}
	static native void nglFogCoordf(float coord, long function_pointer);

	public static void glFogCoordd(double coord) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordd;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFogCoordd(coord, function_pointer);
	}
	static native void nglFogCoordd(double coord, long function_pointer);

	public static void glFogCoordPointer(int stride, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(data);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL14_glFogCoordPointer_data = data;
		nglFogCoordPointer(GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glFogCoordPointer(int stride, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(data);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).GL14_glFogCoordPointer_data = data;
		nglFogCoordPointer(GL11.GL_FLOAT, stride, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglFogCoordPointer(int type, int stride, long data, long function_pointer);
	public static void glFogCoordPointer(int type, int stride, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglFogCoordPointerBO(type, stride, data_buffer_offset, function_pointer);
	}
	static native void nglFogCoordPointerBO(int type, int stride, long data_buffer_offset, long function_pointer);

	public static void glMultiDrawArrays(int mode, IntBuffer piFirst, IntBuffer piCount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiDrawArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(piFirst);
		BufferChecks.checkBuffer(piCount, piFirst.remaining());
		nglMultiDrawArrays(mode, MemoryUtil.getAddress(piFirst), MemoryUtil.getAddress(piCount), piFirst.remaining(), function_pointer);
	}
	static native void nglMultiDrawArrays(int mode, long piFirst, long piCount, int piFirst_primcount, long function_pointer);

	public static void glPointParameteri(int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPointParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPointParameteri(pname, param, function_pointer);
	}
	static native void nglPointParameteri(int pname, int param, long function_pointer);

	public static void glPointParameterf(int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPointParameterf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPointParameterf(pname, param, function_pointer);
	}
	static native void nglPointParameterf(int pname, float param, long function_pointer);

	public static void glPointParameter(int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPointParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglPointParameteriv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglPointParameteriv(int pname, long params, long function_pointer);

	public static void glPointParameter(int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPointParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglPointParameterfv(pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglPointParameterfv(int pname, long params, long function_pointer);

	public static void glSecondaryColor3b(byte red, byte green, byte blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColor3b;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColor3b(red, green, blue, function_pointer);
	}
	static native void nglSecondaryColor3b(byte red, byte green, byte blue, long function_pointer);

	public static void glSecondaryColor3f(float red, float green, float blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColor3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColor3f(red, green, blue, function_pointer);
	}
	static native void nglSecondaryColor3f(float red, float green, float blue, long function_pointer);

	public static void glSecondaryColor3d(double red, double green, double blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColor3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColor3d(red, green, blue, function_pointer);
	}
	static native void nglSecondaryColor3d(double red, double green, double blue, long function_pointer);

	public static void glSecondaryColor3ub(byte red, byte green, byte blue) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColor3ub;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColor3ub(red, green, blue, function_pointer);
	}
	static native void nglSecondaryColor3ub(byte red, byte green, byte blue, long function_pointer);

	public static void glSecondaryColorPointer(int size, int stride, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglSecondaryColorPointer(size, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glSecondaryColorPointer(int size, int stride, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglSecondaryColorPointer(size, GL11.GL_FLOAT, stride, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glSecondaryColorPointer(int size, boolean unsigned, int stride, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglSecondaryColorPointer(size, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, stride, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglSecondaryColorPointer(int size, int type, int stride, long data, long function_pointer);
	public static void glSecondaryColorPointer(int size, int type, int stride, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglSecondaryColorPointerBO(size, type, stride, data_buffer_offset, function_pointer);
	}
	static native void nglSecondaryColorPointerBO(int size, int type, int stride, long data_buffer_offset, long function_pointer);

	public static void glBlendFuncSeparate(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendFuncSeparate;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha, function_pointer);
	}
	static native void nglBlendFuncSeparate(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha, long function_pointer);

	public static void glWindowPos2f(float x, float y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWindowPos2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglWindowPos2f(x, y, function_pointer);
	}
	static native void nglWindowPos2f(float x, float y, long function_pointer);

	public static void glWindowPos2d(double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWindowPos2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglWindowPos2d(x, y, function_pointer);
	}
	static native void nglWindowPos2d(double x, double y, long function_pointer);

	public static void glWindowPos2i(int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWindowPos2i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglWindowPos2i(x, y, function_pointer);
	}
	static native void nglWindowPos2i(int x, int y, long function_pointer);

	public static void glWindowPos3f(float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWindowPos3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglWindowPos3f(x, y, z, function_pointer);
	}
	static native void nglWindowPos3f(float x, float y, float z, long function_pointer);

	public static void glWindowPos3d(double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWindowPos3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglWindowPos3d(x, y, z, function_pointer);
	}
	static native void nglWindowPos3d(double x, double y, double z, long function_pointer);

	public static void glWindowPos3i(int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWindowPos3i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglWindowPos3i(x, y, z, function_pointer);
	}
	static native void nglWindowPos3i(int x, int y, int z, long function_pointer);
}
