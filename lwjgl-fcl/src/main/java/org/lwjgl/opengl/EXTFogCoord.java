/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTFogCoord {

	public static final int GL_FOG_COORDINATE_SOURCE_EXT = 0x8450,
		GL_FOG_COORDINATE_EXT = 0x8451,
		GL_FRAGMENT_DEPTH_EXT = 0x8452,
		GL_CURRENT_FOG_COORDINATE_EXT = 0x8453,
		GL_FOG_COORDINATE_ARRAY_TYPE_EXT = 0x8454,
		GL_FOG_COORDINATE_ARRAY_STRIDE_EXT = 0x8455,
		GL_FOG_COORDINATE_ARRAY_POINTER_EXT = 0x8456,
		GL_FOG_COORDINATE_ARRAY_EXT = 0x8457;

	private EXTFogCoord() {}

	public static void glFogCoordfEXT(float coord) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordfEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFogCoordfEXT(coord, function_pointer);
	}
	static native void nglFogCoordfEXT(float coord, long function_pointer);

	public static void glFogCoorddEXT(double coord) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoorddEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFogCoorddEXT(coord, function_pointer);
	}
	static native void nglFogCoorddEXT(double coord, long function_pointer);

	public static void glFogCoordPointerEXT(int stride, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(data);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_fog_coord_glFogCoordPointerEXT_data = data;
		nglFogCoordPointerEXT(GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glFogCoordPointerEXT(int stride, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(data);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).EXT_fog_coord_glFogCoordPointerEXT_data = data;
		nglFogCoordPointerEXT(GL11.GL_FLOAT, stride, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglFogCoordPointerEXT(int type, int stride, long data, long function_pointer);
	public static void glFogCoordPointerEXT(int type, int stride, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFogCoordPointerEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglFogCoordPointerEXTBO(type, stride, data_buffer_offset, function_pointer);
	}
	static native void nglFogCoordPointerEXTBO(int type, int stride, long data_buffer_offset, long function_pointer);
}
