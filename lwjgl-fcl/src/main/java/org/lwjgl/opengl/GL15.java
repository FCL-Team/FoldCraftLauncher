/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL15 {

	public static final int GL_ARRAY_BUFFER = 0x8892,
		GL_ELEMENT_ARRAY_BUFFER = 0x8893,
		GL_ARRAY_BUFFER_BINDING = 0x8894,
		GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895,
		GL_VERTEX_ARRAY_BUFFER_BINDING = 0x8896,
		GL_NORMAL_ARRAY_BUFFER_BINDING = 0x8897,
		GL_COLOR_ARRAY_BUFFER_BINDING = 0x8898,
		GL_INDEX_ARRAY_BUFFER_BINDING = 0x8899,
		GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING = 0x889A,
		GL_EDGE_FLAG_ARRAY_BUFFER_BINDING = 0x889B,
		GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING = 0x889C,
		GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING = 0x889D,
		GL_WEIGHT_ARRAY_BUFFER_BINDING = 0x889E,
		GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F,
		GL_STREAM_DRAW = 0x88E0,
		GL_STREAM_READ = 0x88E1,
		GL_STREAM_COPY = 0x88E2,
		GL_STATIC_DRAW = 0x88E4,
		GL_STATIC_READ = 0x88E5,
		GL_STATIC_COPY = 0x88E6,
		GL_DYNAMIC_DRAW = 0x88E8,
		GL_DYNAMIC_READ = 0x88E9,
		GL_DYNAMIC_COPY = 0x88EA,
		GL_READ_ONLY = 0x88B8,
		GL_WRITE_ONLY = 0x88B9,
		GL_READ_WRITE = 0x88BA,
		GL_BUFFER_SIZE = 0x8764,
		GL_BUFFER_USAGE = 0x8765,
		GL_BUFFER_ACCESS = 0x88BB,
		GL_BUFFER_MAPPED = 0x88BC,
		GL_BUFFER_MAP_POINTER = 0x88BD,
		GL_FOG_COORD_SRC = 0x8450,
		GL_FOG_COORD = 0x8451,
		GL_CURRENT_FOG_COORD = 0x8453,
		GL_FOG_COORD_ARRAY_TYPE = 0x8454,
		GL_FOG_COORD_ARRAY_STRIDE = 0x8455,
		GL_FOG_COORD_ARRAY_POINTER = 0x8456,
		GL_FOG_COORD_ARRAY = 0x8457,
		GL_FOG_COORD_ARRAY_BUFFER_BINDING = 0x889D,
		GL_SRC0_RGB = 0x8580,
		GL_SRC1_RGB = 0x8581,
		GL_SRC2_RGB = 0x8582,
		GL_SRC0_ALPHA = 0x8588,
		GL_SRC1_ALPHA = 0x8589,
		GL_SRC2_ALPHA = 0x858A;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery,
	 *  and GetQueryiv:
	 */
	public static final int GL_SAMPLES_PASSED = 0x8914;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetQueryiv: 
	 */
	public static final int GL_QUERY_COUNTER_BITS = 0x8864,
		GL_CURRENT_QUERY = 0x8865;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetQueryObjectiv and
	 *  GetQueryObjectuiv:
	 */
	public static final int GL_QUERY_RESULT = 0x8866,
		GL_QUERY_RESULT_AVAILABLE = 0x8867;

	private GL15() {}

	public static void glBindBuffer(int target, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		StateTracker.bindBuffer(caps, target, buffer);
		nglBindBuffer(target, buffer, function_pointer);
	}
	static native void nglBindBuffer(int target, int buffer, long function_pointer);

	public static void glDeleteBuffers(IntBuffer buffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglDeleteBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
	}
	static native void nglDeleteBuffers(int buffers_n, long buffers, long function_pointer);

	/** Overloads glDeleteBuffers. */
	public static void glDeleteBuffers(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteBuffers(1, APIUtil.getInt(caps, buffer), function_pointer);
	}

	public static void glGenBuffers(IntBuffer buffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglGenBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
	}
	static native void nglGenBuffers(int buffers_n, long buffers, long function_pointer);

	/** Overloads glGenBuffers. */
	public static int glGenBuffers() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer buffers = APIUtil.getBufferInt(caps);
		nglGenBuffers(1, MemoryUtil.getAddress(buffers), function_pointer);
		return buffers.get(0);
	}

	public static boolean glIsBuffer(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsBuffer(buffer, function_pointer);
		return __result;
	}
	static native boolean nglIsBuffer(int buffer, long function_pointer);

	public static void glBufferData(int target, long data_size, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBufferData(target, data_size, 0L, usage, function_pointer);
	}
	public static void glBufferData(int target, ByteBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, data.remaining(), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glBufferData(int target, DoubleBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 3), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glBufferData(int target, FloatBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 2), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glBufferData(int target, IntBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 2), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glBufferData(int target, ShortBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferData(target, (data.remaining() << 1), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	static native void nglBufferData(int target, long data_size, long data, int usage, long function_pointer);

	public static void glBufferSubData(int target, long offset, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glBufferSubData(int target, long offset, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glBufferSubData(int target, long offset, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glBufferSubData(int target, long offset, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glBufferSubData(int target, long offset, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferSubData(target, offset, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglBufferSubData(int target, long offset, long data_size, long data, long function_pointer);

	public static void glGetBufferSubData(int target, long offset, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetBufferSubData(int target, long offset, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetBufferSubData(int target, long offset, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetBufferSubData(int target, long offset, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetBufferSubData(int target, long offset, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetBufferSubData(target, offset, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetBufferSubData(int target, long offset, long data_size, long data, long function_pointer);

	/**
	 *  glMapBuffer maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapBuffer like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapBuffer(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBuffer(..., ..., mapped_buffer);
	 *  <p/>
	 *  Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 *  <p/>
	 *  The version of this method without an explicit length argument calls glGetBufferParameter internally to
	 *  retrieve the current buffer object size, which may cause a pipeline flush and reduce application performance.
	 *  <p/>
	 *  The version of this method with an explicit length argument is a fast alternative to the one without. No GL call
	 *  is made to retrieve the buffer object size, so the user is responsible for tracking and using the appropriate length.<br>
	 *  Security warning: The length argument should match the buffer object size. Reading from or writing to outside
	 *  the memory region that corresponds to the mapped buffer object will cause native crashes.
	 * <p>
	 *  @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 * <p>
	 *  @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static ByteBuffer glMapBuffer(int target, int access, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBuffer(target, access, glGetBufferParameteri(target, GL_BUFFER_SIZE), old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	/**
	 *  glMapBuffer maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapBuffer like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapBuffer(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBuffer(..., ..., mapped_buffer);
	 *  <p/>
	 *  Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 *  <p/>
	 *  The version of this method without an explicit length argument calls glGetBufferParameter internally to
	 *  retrieve the current buffer object size, which may cause a pipeline flush and reduce application performance.
	 *  <p/>
	 *  The version of this method with an explicit length argument is a fast alternative to the one without. No GL call
	 *  is made to retrieve the buffer object size, so the user is responsible for tracking and using the appropriate length.<br>
	 *  Security warning: The length argument should match the buffer object size. Reading from or writing to outside
	 *  the memory region that corresponds to the mapped buffer object will cause native crashes.
	 * <p>
	 *  @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 * <p>
	 *  @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static ByteBuffer glMapBuffer(int target, int access, long length, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBuffer(target, access, length, old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapBuffer(int target, int access, long result_size, ByteBuffer old_buffer, long function_pointer);

	public static boolean glUnmapBuffer(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUnmapBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglUnmapBuffer(target, function_pointer);
		return __result;
	}
	static native boolean nglUnmapBuffer(int target, long function_pointer);

	public static void glGetBufferParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetBufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetBufferParameteriv(int target, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetBufferParameteriv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetBufferParameteri} instead. 
	 */
	@Deprecated
	public static int glGetBufferParameter(int target, int pname) {
		return GL15.glGetBufferParameteri(target, pname);
	}

	/** Overloads glGetBufferParameteriv. */
	public static int glGetBufferParameteri(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetBufferParameteriv(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static ByteBuffer glGetBufferPointer(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferPointerv;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglGetBufferPointerv(target, pname, glGetBufferParameteri(target, GL_BUFFER_SIZE), function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetBufferPointerv(int target, int pname, long result_size, long function_pointer);

	public static void glGenQueries(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenQueries;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglGenQueries(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglGenQueries(int ids_n, long ids, long function_pointer);

	/** Overloads glGenQueries. */
	public static int glGenQueries() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenQueries;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer ids = APIUtil.getBufferInt(caps);
		nglGenQueries(1, MemoryUtil.getAddress(ids), function_pointer);
		return ids.get(0);
	}

	public static void glDeleteQueries(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteQueries;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglDeleteQueries(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglDeleteQueries(int ids_n, long ids, long function_pointer);

	/** Overloads glDeleteQueries. */
	public static void glDeleteQueries(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteQueries;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteQueries(1, APIUtil.getInt(caps, id), function_pointer);
	}

	public static boolean glIsQuery(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsQuery;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsQuery(id, function_pointer);
		return __result;
	}
	static native boolean nglIsQuery(int id, long function_pointer);

	public static void glBeginQuery(int target, int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginQuery;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginQuery(target, id, function_pointer);
	}
	static native void nglBeginQuery(int target, int id, long function_pointer);

	public static void glEndQuery(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndQuery;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndQuery(target, function_pointer);
	}
	static native void nglEndQuery(int target, long function_pointer);

	public static void glGetQuery(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryiv(int target, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetQueryiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetQueryi} instead. 
	 */
	@Deprecated
	public static int glGetQuery(int target, int pname) {
		return GL15.glGetQueryi(target, pname);
	}

	/** Overloads glGetQueryiv. */
	public static int glGetQueryi(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetQueryiv(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetQueryObject(int id, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryObjectiv(id, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryObjectiv(int id, int pname, long params, long function_pointer);

	/** Overloads glGetQueryObjectiv. */
	public static int glGetQueryObjecti(int id, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetQueryObjectiv(id, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetQueryObjectu(int id, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryObjectuiv(id, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryObjectuiv(int id, int pname, long params, long function_pointer);

	/** Overloads glGetQueryObjectuiv. */
	public static int glGetQueryObjectui(int id, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetQueryObjectuiv(id, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
