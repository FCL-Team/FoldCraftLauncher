/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBVertexShader {

	/**
	 *  Accepted by the &lt;shaderType&gt; argument of CreateShaderObjectARB and
	 *  returned by the &lt;params&gt; parameter of GetObjectParameter{if}vARB:
	 */
	public static final int GL_VERTEX_SHADER_ARB = 0x8B31;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS_ARB = 0x8B4A,
		GL_MAX_VARYING_FLOATS_ARB = 0x8B4B,
		GL_MAX_VERTEX_ATTRIBS_ARB = 0x8869,
		GL_MAX_TEXTURE_IMAGE_UNITS_ARB = 0x8872,
		GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS_ARB = 0x8B4C,
		GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS_ARB = 0x8B4D,
		GL_MAX_TEXTURE_COORDS_ARB = 0x8871;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and
	 *  by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 0x8642,
		GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 0x8643;

	/**
	 * Accepted by the &lt;pname&gt; parameter GetObjectParameter{if}vARB: 
	 */
	public static final int GL_OBJECT_ACTIVE_ATTRIBUTES_ARB = 0x8B89,
		GL_OBJECT_ACTIVE_ATTRIBUTE_MAX_LENGTH_ARB = 0x8B8A;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttrib{dfi}vARB: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 0x8622,
		GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 0x8623,
		GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 0x8624,
		GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 0x8625,
		GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 0x886A,
		GL_CURRENT_VERTEX_ATTRIB_ARB = 0x8626;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttribPointervARB: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 0x8645;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveAttribARB: 
	 */
	public static final int GL_FLOAT_VEC2_ARB = 0x8B50,
		GL_FLOAT_VEC3_ARB = 0x8B51,
		GL_FLOAT_VEC4_ARB = 0x8B52,
		GL_FLOAT_MAT2_ARB = 0x8B5A,
		GL_FLOAT_MAT3_ARB = 0x8B5B,
		GL_FLOAT_MAT4_ARB = 0x8B5C;

	private ARBVertexShader() {}

	public static void glVertexAttrib1sARB(int index, short v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1sARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1sARB(index, v0, function_pointer);
	}
	static native void nglVertexAttrib1sARB(int index, short v0, long function_pointer);

	public static void glVertexAttrib1fARB(int index, float v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1fARB(index, v0, function_pointer);
	}
	static native void nglVertexAttrib1fARB(int index, float v0, long function_pointer);

	public static void glVertexAttrib1dARB(int index, double v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1dARB(index, v0, function_pointer);
	}
	static native void nglVertexAttrib1dARB(int index, double v0, long function_pointer);

	public static void glVertexAttrib2sARB(int index, short v0, short v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2sARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2sARB(index, v0, v1, function_pointer);
	}
	static native void nglVertexAttrib2sARB(int index, short v0, short v1, long function_pointer);

	public static void glVertexAttrib2fARB(int index, float v0, float v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2fARB(index, v0, v1, function_pointer);
	}
	static native void nglVertexAttrib2fARB(int index, float v0, float v1, long function_pointer);

	public static void glVertexAttrib2dARB(int index, double v0, double v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2dARB(index, v0, v1, function_pointer);
	}
	static native void nglVertexAttrib2dARB(int index, double v0, double v1, long function_pointer);

	public static void glVertexAttrib3sARB(int index, short v0, short v1, short v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3sARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3sARB(index, v0, v1, v2, function_pointer);
	}
	static native void nglVertexAttrib3sARB(int index, short v0, short v1, short v2, long function_pointer);

	public static void glVertexAttrib3fARB(int index, float v0, float v1, float v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3fARB(index, v0, v1, v2, function_pointer);
	}
	static native void nglVertexAttrib3fARB(int index, float v0, float v1, float v2, long function_pointer);

	public static void glVertexAttrib3dARB(int index, double v0, double v1, double v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3dARB(index, v0, v1, v2, function_pointer);
	}
	static native void nglVertexAttrib3dARB(int index, double v0, double v1, double v2, long function_pointer);

	public static void glVertexAttrib4sARB(int index, short v0, short v1, short v2, short v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4sARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4sARB(index, v0, v1, v2, v3, function_pointer);
	}
	static native void nglVertexAttrib4sARB(int index, short v0, short v1, short v2, short v3, long function_pointer);

	public static void glVertexAttrib4fARB(int index, float v0, float v1, float v2, float v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4fARB(index, v0, v1, v2, v3, function_pointer);
	}
	static native void nglVertexAttrib4fARB(int index, float v0, float v1, float v2, float v3, long function_pointer);

	public static void glVertexAttrib4dARB(int index, double v0, double v1, double v2, double v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4dARB(index, v0, v1, v2, v3, function_pointer);
	}
	static native void nglVertexAttrib4dARB(int index, double v0, double v1, double v2, double v3, long function_pointer);

	public static void glVertexAttrib4NubARB(int index, byte x, byte y, byte z, byte w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4NubARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4NubARB(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4NubARB(int index, byte x, byte y, byte z, byte w, long function_pointer);

	public static void glVertexAttribPointerARB(int index, int size, boolean normalized, int stride, DoubleBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerARB(index, size, GL11.GL_DOUBLE, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointerARB(int index, int size, boolean normalized, int stride, FloatBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerARB(index, size, GL11.GL_FLOAT, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, ByteBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerARB(index, size, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, IntBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerARB(index, size, unsigned ? GL11.GL_UNSIGNED_INT : GL11.GL_INT, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointerARB(int index, int size, boolean unsigned, boolean normalized, int stride, ShortBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerARB(index, size, unsigned ? GL11.GL_UNSIGNED_SHORT : GL11.GL_SHORT, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	static native void nglVertexAttribPointerARB(int index, int size, int type, boolean normalized, int stride, long buffer, long function_pointer);
	public static void glVertexAttribPointerARB(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexAttribPointerARBBO(index, size, type, normalized, stride, buffer_buffer_offset, function_pointer);
	}
	static native void nglVertexAttribPointerARBBO(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset, long function_pointer);

	/** Overloads glVertexAttribPointerARB. */
	public static void glVertexAttribPointerARB(int index, int size, int type, boolean normalized, int stride, ByteBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointerARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointerARB(index, size, type, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}

	public static void glEnableVertexAttribArrayARB(int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEnableVertexAttribArrayARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnableVertexAttribArrayARB(index, function_pointer);
	}
	static native void nglEnableVertexAttribArrayARB(int index, long function_pointer);

	public static void glDisableVertexAttribArrayARB(int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDisableVertexAttribArrayARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisableVertexAttribArrayARB(index, function_pointer);
	}
	static native void nglDisableVertexAttribArrayARB(int index, long function_pointer);

	public static void glBindAttribLocationARB(int programObj, int index, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindAttribLocationARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		nglBindAttribLocationARB(programObj, index, MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglBindAttribLocationARB(int programObj, int index, long name, long function_pointer);

	/** Overloads glBindAttribLocationARB. */
	public static void glBindAttribLocationARB(int programObj, int index, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindAttribLocationARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindAttribLocationARB(programObj, index, APIUtil.getBufferNT(caps, name), function_pointer);
	}

	public static void glGetActiveAttribARB(int programObj, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttribARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		BufferChecks.checkDirect(name);
		nglGetActiveAttribARB(programObj, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglGetActiveAttribARB(int programObj, int index, int name_maxLength, long length, long size, long type, long name, long function_pointer);

	/**
	 * Overloads glGetActiveAttribARB.
	 * <p>
	 * Overloads glGetActiveAttribARB. This version returns both size and type in the sizeType buffer (at .position() and .position() + 1). 
	 */
	public static String glGetActiveAttribARB(int programObj, int index, int maxLength, IntBuffer sizeType) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttribARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(sizeType, 2);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
		nglGetActiveAttribARB(programObj, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	/**
	 * Overloads glGetActiveAttribARB.
	 * <p>
	 * Overloads glGetActiveAttribARB. This version returns only the attrib name. 
	 */
	public static String glGetActiveAttribARB(int programObj, int index, int maxLength) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttribARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
		nglGetActiveAttribARB(programObj, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	/**
	 * Overloads glGetActiveAttribARB.
	 * <p>
	 * Overloads glGetActiveAttribARB. This version returns only the attrib size. 
	 */
	public static int glGetActiveAttribSizeARB(int programObj, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttribARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer size = APIUtil.getBufferInt(caps);
		nglGetActiveAttribARB(programObj, index, 0, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0(caps), function_pointer);
		return size.get(0);
	}

	/**
	 * Overloads glGetActiveAttribARB.
	 * <p>
	 * Overloads glGetActiveAttribARB. This version returns only the attrib type. 
	 */
	public static int glGetActiveAttribTypeARB(int programObj, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttribARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer type = APIUtil.getBufferInt(caps);
		nglGetActiveAttribARB(programObj, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0(caps), function_pointer);
		return type.get(0);
	}

	public static int glGetAttribLocationARB(int programObj, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetAttribLocationARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetAttribLocationARB(programObj, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetAttribLocationARB(int programObj, long name, long function_pointer);

	/** Overloads glGetAttribLocationARB. */
	public static int glGetAttribLocationARB(int programObj, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetAttribLocationARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetAttribLocationARB(programObj, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static void glGetVertexAttribARB(int index, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribfvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribfvARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribfvARB(int index, int pname, long params, long function_pointer);

	public static void glGetVertexAttribARB(int index, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribdvARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribdvARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribdvARB(int index, int pname, long params, long function_pointer);

	public static void glGetVertexAttribARB(int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribivARB(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribivARB(int index, int pname, long params, long function_pointer);

	public static ByteBuffer glGetVertexAttribPointerARB(int index, int pname, long result_size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribPointervARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglGetVertexAttribPointervARB(index, pname, result_size, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetVertexAttribPointervARB(int index, int pname, long result_size, long function_pointer);
}
