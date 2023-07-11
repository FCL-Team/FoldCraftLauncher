/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public class ARBBufferObject {

	/**
	 * Accepted by the &lt;usage&gt; parameter of BufferDataARB: 
	 */
	public static final int GL_STREAM_DRAW_ARB = 0x88E0,
		GL_STREAM_READ_ARB = 0x88E1,
		GL_STREAM_COPY_ARB = 0x88E2,
		GL_STATIC_DRAW_ARB = 0x88E4,
		GL_STATIC_READ_ARB = 0x88E5,
		GL_STATIC_COPY_ARB = 0x88E6,
		GL_DYNAMIC_DRAW_ARB = 0x88E8,
		GL_DYNAMIC_READ_ARB = 0x88E9,
		GL_DYNAMIC_COPY_ARB = 0x88EA;

	/**
	 * Accepted by the &lt;access&gt; parameter of MapBufferARB: 
	 */
	public static final int GL_READ_ONLY_ARB = 0x88B8,
		GL_WRITE_ONLY_ARB = 0x88B9,
		GL_READ_WRITE_ARB = 0x88BA;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBufferParameterivARB: 
	 */
	public static final int GL_BUFFER_SIZE_ARB = 0x8764,
		GL_BUFFER_USAGE_ARB = 0x8765,
		GL_BUFFER_ACCESS_ARB = 0x88BB,
		GL_BUFFER_MAPPED_ARB = 0x88BC,
		GL_BUFFER_MAP_POINTER_ARB = 0x88BD;


	public static void glBindBufferARB(int target, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBufferARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		StateTracker.bindBuffer(caps, target, buffer);
		nglBindBufferARB(target, buffer, function_pointer);
	}
	static native void nglBindBufferARB(int target, int buffer, long function_pointer);

	public static void glDeleteBuffersARB(IntBuffer buffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteBuffersARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglDeleteBuffersARB(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
	}
	static native void nglDeleteBuffersARB(int buffers_n, long buffers, long function_pointer);

	/** Overloads glDeleteBuffersARB. */
	public static void glDeleteBuffersARB(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteBuffersARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteBuffersARB(1, APIUtil.getInt(caps, buffer), function_pointer);
	}

	public static void glGenBuffersARB(IntBuffer buffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenBuffersARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglGenBuffersARB(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
	}
	static native void nglGenBuffersARB(int buffers_n, long buffers, long function_pointer);

	/** Overloads glGenBuffersARB. */
	public static int glGenBuffersARB() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenBuffersARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer buffers = APIUtil.getBufferInt(caps);
		nglGenBuffersARB(1, MemoryUtil.getAddress(buffers), function_pointer);
		return buffers.get(0);
	}

	public static boolean glIsBufferARB(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsBufferARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsBufferARB(buffer, function_pointer);
		return __result;
	}
	static native boolean nglIsBufferARB(int buffer, long function_pointer);

	public static void glBufferDataARB(int target, long data_size, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBufferDataARB(target, data_size, 0L, usage, function_pointer);
	}
	public static void glBufferDataARB(int target, ByteBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferDataARB(target, data.remaining(), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glBufferDataARB(int target, DoubleBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferDataARB(target, (data.remaining() << 3), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glBufferDataARB(int target, FloatBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferDataARB(target, (data.remaining() << 2), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glBufferDataARB(int target, IntBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferDataARB(target, (data.remaining() << 2), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glBufferDataARB(int target, ShortBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferDataARB(target, (data.remaining() << 1), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	static native void nglBufferDataARB(int target, long data_size, long data, int usage, long function_pointer);

	public static void glBufferSubDataARB(int target, long offset, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubDataARB(target, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glBufferSubDataARB(int target, long offset, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubDataARB(target, offset, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glBufferSubDataARB(int target, long offset, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubDataARB(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glBufferSubDataARB(int target, long offset, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubDataARB(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glBufferSubDataARB(int target, long offset, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubDataARB(target, offset, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglBufferSubDataARB(int target, long offset, long data_size, long data, long function_pointer);

	public static void glGetBufferSubDataARB(int target, long offset, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubDataARB(target, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetBufferSubDataARB(int target, long offset, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubDataARB(target, offset, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetBufferSubDataARB(int target, long offset, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubDataARB(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetBufferSubDataARB(int target, long offset, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubDataARB(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetBufferSubDataARB(int target, long offset, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubDataARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubDataARB(target, offset, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetBufferSubDataARB(int target, long offset, long data_size, long data, long function_pointer);

	/**
	 *  glMapBufferARB maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapBufferARB like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapBufferARB(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferARB(..., ..., mapped_buffer);
	 *  <p/>
	 *  Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 *  <p/>
	 *  The version of this method without an explicit length argument calls glGetBufferParameterARB internally to
	 *  retrieve the current buffer object size, which may cause a pipeline flush and reduce application performance.
	 *  <p/>
	 *  The version of this method with an explicit length argument is a fast alternative to the one without. No GL call
	 *  is made to retrieve the buffer object size, so the user is responsible for tracking and using the appropriate length.<br>
	 *  Security warning: The length argument should match the buffer object size. Reading from or writing to outside
	 *  the memory region that corresponds to the mapped buffer object will cause native crashes.
	 * <p>
	 *  @param length     the length of the mapped memory in bytes.
	 *  @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 * <p>
	 *  @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static ByteBuffer glMapBufferARB(int target, int access, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapBufferARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBufferARB(target, access, glGetBufferParameteriARB(target, GL_BUFFER_SIZE_ARB), old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	/**
	 *  glMapBufferARB maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapBufferARB like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapBufferARB(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferARB(..., ..., mapped_buffer);
	 *  <p/>
	 *  Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 *  <p/>
	 *  The version of this method without an explicit length argument calls glGetBufferParameterARB internally to
	 *  retrieve the current buffer object size, which may cause a pipeline flush and reduce application performance.
	 *  <p/>
	 *  The version of this method with an explicit length argument is a fast alternative to the one without. No GL call
	 *  is made to retrieve the buffer object size, so the user is responsible for tracking and using the appropriate length.<br>
	 *  Security warning: The length argument should match the buffer object size. Reading from or writing to outside
	 *  the memory region that corresponds to the mapped buffer object will cause native crashes.
	 * <p>
	 *  @param length     the length of the mapped memory in bytes.
	 *  @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 * <p>
	 *  @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static ByteBuffer glMapBufferARB(int target, int access, long length, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapBufferARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBufferARB(target, access, length, old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapBufferARB(int target, int access, long result_size, ByteBuffer old_buffer, long function_pointer);

	public static boolean glUnmapBufferARB(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUnmapBufferARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglUnmapBufferARB(target, function_pointer);
		return __result;
	}
	static native boolean nglUnmapBufferARB(int target, long function_pointer);

	public static void glGetBufferParameterARB(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferParameterivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetBufferParameterivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetBufferParameterivARB(int target, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetBufferParameterivARB.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetBufferParameteriARB} instead. 
	 */
	@Deprecated
	public static int glGetBufferParameterARB(int target, int pname) {
		return ARBBufferObject.glGetBufferParameteriARB(target, pname);
	}

	/** Overloads glGetBufferParameterivARB. */
	public static int glGetBufferParameteriARB(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferParameterivARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetBufferParameterivARB(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static ByteBuffer glGetBufferPointerARB(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferPointervARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglGetBufferPointervARB(target, pname, glGetBufferParameteriARB(target, GL_BUFFER_SIZE_ARB), function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetBufferPointervARB(int target, int pname, long result_size, long function_pointer);
}
