/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL45 {

	/**
	 * Accepted by the &lt;depth&gt; parameter of ClipControl: 
	 */
	public static final int GL_NEGATIVE_ONE_TO_ONE = 0x935E,
		GL_ZERO_TO_ONE = 0x935F;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_CLIP_ORIGIN = 0x935C,
		GL_CLIP_DEPTH_MODE = 0x935D;

	/**
	 * Accepted by the &lt;mode&gt; parameter of BeginConditionalRender: 
	 */
	public static final int GL_QUERY_WAIT_INVERTED = 0x8E17,
		GL_QUERY_NO_WAIT_INVERTED = 0x8E18,
		GL_QUERY_BY_REGION_WAIT_INVERTED = 0x8E19,
		GL_QUERY_BY_REGION_NO_WAIT_INVERTED = 0x8E1A;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooeleanv, GetDoublev, GetFloatv GetIntegerv, and GetInteger64v: 
	 */
	public static final int GL_MAX_CULL_DISTANCES = 0x82F9,
		GL_MAX_COMBINED_CLIP_AND_CULL_DISTANCES = 0x82FA;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetTextureParameter{if}v and
	 *  GetTextureParameterI{i ui}v:
	 */
	public static final int GL_TEXTURE_TARGET = 0x1006;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetQueryObjectiv: 
	 */
	public static final int GL_QUERY_TARGET = 0x82EA;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegeri_v: 
	 */
	public static final int GL_TEXTURE_BINDING = 0x82EB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetFloatv, GetBooleanv
	 *  GetDoublev and GetInteger64v:
	 */
	public static final int GL_CONTEXT_RELEASE_BEHAVIOR = 0x82FB;

	/**
	 *  Returned in &lt;data&gt; by GetIntegerv, GetFloatv, GetBooleanv
	 *  GetDoublev and GetInteger64v when &lt;pname&gt; is
	 *  GL_CONTEXT_RELEASE_BEHAVIOR:
	 */
	public static final int GL_CONTEXT_RELEASE_BEHAVIOR_FLUSH = 0x82FC;

	/**
	 * Returned by GetGraphicsResetStatus: 
	 */
	public static final int GL_GUILTY_CONTEXT_RESET = 0x8253,
		GL_INNOCENT_CONTEXT_RESET = 0x8254,
		GL_UNKNOWN_CONTEXT_RESET = 0x8255;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_CONTEXT_ROBUST_ACCESS = 0x90F3,
		GL_RESET_NOTIFICATION_STRATEGY = 0x8256;

	/**
	 *  Returned by GetIntegerv and related simple queries when &lt;value&gt; is
	 *  RESET_NOTIFICATION_STRATEGY:
	 */
	public static final int GL_LOSE_CONTEXT_ON_RESET = 0x8252,
		GL_NO_RESET_NOTIFICATION = 0x8261;

	/**
	 * Returned by GetError: 
	 */
	public static final int GL_CONTEXT_LOST = 0x507;

	private GL45() {}

	public static void glClipControl(int origin, int depth) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClipControl;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClipControl(origin, depth, function_pointer);
	}
	static native void nglClipControl(int origin, int depth, long function_pointer);

	public static void glCreateTransformFeedbacks(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateTransformFeedbacks;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglCreateTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglCreateTransformFeedbacks(int ids_n, long ids, long function_pointer);

	/** Overloads glCreateTransformFeedbacks. */
	public static int glCreateTransformFeedbacks() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateTransformFeedbacks;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer ids = APIUtil.getBufferInt(caps);
		nglCreateTransformFeedbacks(1, MemoryUtil.getAddress(ids), function_pointer);
		return ids.get(0);
	}

	public static void glTransformFeedbackBufferBase(int xfb, int index, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTransformFeedbackBufferBase;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTransformFeedbackBufferBase(xfb, index, buffer, function_pointer);
	}
	static native void nglTransformFeedbackBufferBase(int xfb, int index, int buffer, long function_pointer);

	public static void glTransformFeedbackBufferRange(int xfb, int index, int buffer, long offset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTransformFeedbackBufferRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTransformFeedbackBufferRange(xfb, index, buffer, offset, size, function_pointer);
	}
	static native void nglTransformFeedbackBufferRange(int xfb, int index, int buffer, long offset, long size, long function_pointer);

	public static void glGetTransformFeedback(int xfb, int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbackiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 1);
		nglGetTransformFeedbackiv(xfb, pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglGetTransformFeedbackiv(int xfb, int pname, long param, long function_pointer);

	/** Overloads glGetTransformFeedbackiv. */
	public static int glGetTransformFeedbacki(int xfb, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbackiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer param = APIUtil.getBufferInt(caps);
		nglGetTransformFeedbackiv(xfb, pname, MemoryUtil.getAddress(param), function_pointer);
		return param.get(0);
	}

	public static void glGetTransformFeedback(int xfb, int pname, int index, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbacki_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 1);
		nglGetTransformFeedbacki_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglGetTransformFeedbacki_v(int xfb, int pname, int index, long param, long function_pointer);

	/** Overloads glGetTransformFeedbacki_v. */
	public static int glGetTransformFeedbacki(int xfb, int pname, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbacki_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer param = APIUtil.getBufferInt(caps);
		nglGetTransformFeedbacki_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
		return param.get(0);
	}

	public static void glGetTransformFeedback(int xfb, int pname, int index, LongBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbacki64_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 1);
		nglGetTransformFeedbacki64_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglGetTransformFeedbacki64_v(int xfb, int pname, int index, long param, long function_pointer);

	/** Overloads glGetTransformFeedbacki64_v. */
	public static long glGetTransformFeedbacki64(int xfb, int pname, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbacki64_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer param = APIUtil.getBufferLong(caps);
		nglGetTransformFeedbacki64_v(xfb, pname, index, MemoryUtil.getAddress(param), function_pointer);
		return param.get(0);
	}

	public static void glCreateBuffers(IntBuffer buffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglCreateBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
	}
	static native void nglCreateBuffers(int buffers_n, long buffers, long function_pointer);

	/** Overloads glCreateBuffers. */
	public static int glCreateBuffers() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer buffers = APIUtil.getBufferInt(caps);
		nglCreateBuffers(1, MemoryUtil.getAddress(buffers), function_pointer);
		return buffers.get(0);
	}

	public static void glNamedBufferStorage(int buffer, ByteBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorage(buffer, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorage(int buffer, DoubleBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorage(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorage(int buffer, FloatBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorage(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorage(int buffer, IntBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorage(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorage(int buffer, ShortBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorage(buffer, (data.remaining() << 1), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorage(int buffer, LongBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorage(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	static native void nglNamedBufferStorage(int buffer, long data_size, long data, int flags, long function_pointer);

	/** Overloads glNamedBufferStorage. */
	public static void glNamedBufferStorage(int buffer, long size, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedBufferStorage(buffer, size, 0L, flags, function_pointer);
	}

	public static void glNamedBufferData(int buffer, long data_size, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedBufferData(buffer, data_size, 0L, usage, function_pointer);
	}
	public static void glNamedBufferData(int buffer, ByteBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferData(buffer, data.remaining(), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glNamedBufferData(int buffer, DoubleBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferData(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glNamedBufferData(int buffer, FloatBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferData(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glNamedBufferData(int buffer, IntBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferData(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	public static void glNamedBufferData(int buffer, ShortBuffer data, int usage) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferData(buffer, (data.remaining() << 1), MemoryUtil.getAddress(data), usage, function_pointer);
	}
	static native void nglNamedBufferData(int buffer, long data_size, long data, int usage, long function_pointer);

	public static void glNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferSubData(buffer, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glNamedBufferSubData(int buffer, long offset, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferSubData(buffer, offset, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glNamedBufferSubData(int buffer, long offset, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferSubData(buffer, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glNamedBufferSubData(int buffer, long offset, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferSubData(buffer, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glNamedBufferSubData(int buffer, long offset, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferSubData(buffer, offset, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglNamedBufferSubData(int buffer, long offset, long data_size, long data, long function_pointer);

	public static void glCopyNamedBufferSubData(int readBuffer, int writeBuffer, long readOffset, long writeOffset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyNamedBufferSubData(readBuffer, writeBuffer, readOffset, writeOffset, size, function_pointer);
	}
	static native void nglCopyNamedBufferSubData(int readBuffer, int writeBuffer, long readOffset, long writeOffset, long size, long function_pointer);

	public static void glClearNamedBufferData(int buffer, int internalformat, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearNamedBufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 1);
		nglClearNamedBufferData(buffer, internalformat, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglClearNamedBufferData(int buffer, int internalformat, int format, int type, long data, long function_pointer);

	public static void glClearNamedBufferSubData(int buffer, int internalformat, long offset, long size, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 1);
		nglClearNamedBufferSubData(buffer, internalformat, offset, size, format, type, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglClearNamedBufferSubData(int buffer, int internalformat, long offset, long size, int format, int type, long data, long function_pointer);

	/**
	 *  Maps a buffer object's data store.
	 *  <p/>
	 *  <b>LWJGL note</b>: This method comes in 2 flavors:
	 *  <ol>
	 *  <li>{@link #glMapNamedBuffer(int, int, ByteBuffer)} - Calls {@link #glGetNamedBufferParameteri} to retrieve the buffer size and the {@code old_buffer} parameter is reused if the returned size and pointer match the buffer capacity and address, respectively.</li>
	 *  <li>{@link #glMapNamedBuffer(int, int, int, ByteBuffer)} - The buffer size is explicitly specified and the {@code old_buffer} parameter is reused if {@code size} and the returned pointer match the buffer capacity and address, respectively. This is the most efficient method.</li>
	 *  </ol>
	 * <p>
	 *  @param buffer the buffer object being mapped
	 *  @param access the access policy, indicating whether it will be possible to read from, write to, or both read from and write to the buffer object's mapped data store
	 */
	public static ByteBuffer glMapNamedBuffer(int buffer, int access, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapNamedBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapNamedBuffer(buffer, access, glGetNamedBufferParameteri(buffer, GL15.GL_BUFFER_SIZE), old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	/**
	 *  Maps a buffer object's data store.
	 *  <p/>
	 *  <b>LWJGL note</b>: This method comes in 2 flavors:
	 *  <ol>
	 *  <li>{@link #glMapNamedBuffer(int, int, ByteBuffer)} - Calls {@link #glGetNamedBufferParameteri} to retrieve the buffer size and the {@code old_buffer} parameter is reused if the returned size and pointer match the buffer capacity and address, respectively.</li>
	 *  <li>{@link #glMapNamedBuffer(int, int, int, ByteBuffer)} - The buffer size is explicitly specified and the {@code old_buffer} parameter is reused if {@code size} and the returned pointer match the buffer capacity and address, respectively. This is the most efficient method.</li>
	 *  </ol>
	 * <p>
	 *  @param buffer the buffer object being mapped
	 *  @param access the access policy, indicating whether it will be possible to read from, write to, or both read from and write to the buffer object's mapped data store
	 */
	public static ByteBuffer glMapNamedBuffer(int buffer, int access, long length, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapNamedBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapNamedBuffer(buffer, access, length, old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapNamedBuffer(int buffer, int access, long result_size, ByteBuffer old_buffer, long function_pointer);

	public static ByteBuffer glMapNamedBufferRange(int buffer, long offset, long length, int access, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapNamedBufferRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapNamedBufferRange(buffer, offset, length, access, old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapNamedBufferRange(int buffer, long offset, long length, int access, ByteBuffer old_buffer, long function_pointer);

	public static boolean glUnmapNamedBuffer(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUnmapNamedBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglUnmapNamedBuffer(buffer, function_pointer);
		return __result;
	}
	static native boolean nglUnmapNamedBuffer(int buffer, long function_pointer);

	public static void glFlushMappedNamedBufferRange(int buffer, long offset, long length) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFlushMappedNamedBufferRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFlushMappedNamedBufferRange(buffer, offset, length, function_pointer);
	}
	static native void nglFlushMappedNamedBufferRange(int buffer, long offset, long length, long function_pointer);

	public static void glGetNamedBufferParameter(int buffer, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetNamedBufferParameteriv(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetNamedBufferParameteriv(int buffer, int pname, long params, long function_pointer);

	/** Overloads glGetNamedBufferParameteriv. */
	public static int glGetNamedBufferParameteri(int buffer, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetNamedBufferParameteriv(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetNamedBufferParameter(int buffer, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferParameteri64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetNamedBufferParameteri64v(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetNamedBufferParameteri64v(int buffer, int pname, long params, long function_pointer);

	/** Overloads glGetNamedBufferParameteri64v. */
	public static long glGetNamedBufferParameteri64(int buffer, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferParameteri64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params = APIUtil.getBufferLong(caps);
		nglGetNamedBufferParameteri64v(buffer, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static ByteBuffer glGetNamedBufferPointer(int buffer, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferPointerv;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglGetNamedBufferPointerv(buffer, pname, glGetNamedBufferParameteri(buffer, GL15.GL_BUFFER_SIZE), function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetNamedBufferPointerv(int buffer, int pname, long result_size, long function_pointer);

	public static void glGetNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetNamedBufferSubData(buffer, offset, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetNamedBufferSubData(int buffer, long offset, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetNamedBufferSubData(buffer, offset, (data.remaining() << 3), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetNamedBufferSubData(int buffer, long offset, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetNamedBufferSubData(buffer, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetNamedBufferSubData(int buffer, long offset, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetNamedBufferSubData(buffer, offset, (data.remaining() << 2), MemoryUtil.getAddress(data), function_pointer);
	}
	public static void glGetNamedBufferSubData(int buffer, long offset, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetNamedBufferSubData(buffer, offset, (data.remaining() << 1), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetNamedBufferSubData(int buffer, long offset, long data_size, long data, long function_pointer);

	public static void glCreateFramebuffers(IntBuffer framebuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateFramebuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(framebuffers);
		nglCreateFramebuffers(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
	}
	static native void nglCreateFramebuffers(int framebuffers_n, long framebuffers, long function_pointer);

	/** Overloads glCreateFramebuffers. */
	public static int glCreateFramebuffers() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateFramebuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer framebuffers = APIUtil.getBufferInt(caps);
		nglCreateFramebuffers(1, MemoryUtil.getAddress(framebuffers), function_pointer);
		return framebuffers.get(0);
	}

	public static void glNamedFramebufferRenderbuffer(int framebuffer, int attachment, int renderbuffertarget, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedFramebufferRenderbuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferRenderbuffer(framebuffer, attachment, renderbuffertarget, renderbuffer, function_pointer);
	}
	static native void nglNamedFramebufferRenderbuffer(int framebuffer, int attachment, int renderbuffertarget, int renderbuffer, long function_pointer);

	public static void glNamedFramebufferParameteri(int framebuffer, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedFramebufferParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferParameteri(framebuffer, pname, param, function_pointer);
	}
	static native void nglNamedFramebufferParameteri(int framebuffer, int pname, int param, long function_pointer);

	public static void glNamedFramebufferTexture(int framebuffer, int attachment, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedFramebufferTexture;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferTexture(framebuffer, attachment, texture, level, function_pointer);
	}
	static native void nglNamedFramebufferTexture(int framebuffer, int attachment, int texture, int level, long function_pointer);

	public static void glNamedFramebufferTextureLayer(int framebuffer, int attachment, int texture, int level, int layer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedFramebufferTextureLayer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferTextureLayer(framebuffer, attachment, texture, level, layer, function_pointer);
	}
	static native void nglNamedFramebufferTextureLayer(int framebuffer, int attachment, int texture, int level, int layer, long function_pointer);

	public static void glNamedFramebufferDrawBuffer(int framebuffer, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedFramebufferDrawBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferDrawBuffer(framebuffer, mode, function_pointer);
	}
	static native void nglNamedFramebufferDrawBuffer(int framebuffer, int mode, long function_pointer);

	public static void glNamedFramebufferDrawBuffers(int framebuffer, IntBuffer bufs) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedFramebufferDrawBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(bufs);
		nglNamedFramebufferDrawBuffers(framebuffer, bufs.remaining(), MemoryUtil.getAddress(bufs), function_pointer);
	}
	static native void nglNamedFramebufferDrawBuffers(int framebuffer, int bufs_n, long bufs, long function_pointer);

	public static void glNamedFramebufferReadBuffer(int framebuffer, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedFramebufferReadBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferReadBuffer(framebuffer, mode, function_pointer);
	}
	static native void nglNamedFramebufferReadBuffer(int framebuffer, int mode, long function_pointer);

	public static void glInvalidateNamedFramebufferData(int framebuffer, IntBuffer attachments) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInvalidateNamedFramebufferData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(attachments);
		nglInvalidateNamedFramebufferData(framebuffer, attachments.remaining(), MemoryUtil.getAddress(attachments), function_pointer);
	}
	static native void nglInvalidateNamedFramebufferData(int framebuffer, int attachments_numAttachments, long attachments, long function_pointer);

	public static void glInvalidateNamedFramebufferSubData(int framebuffer, IntBuffer attachments, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glInvalidateNamedFramebufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(attachments);
		nglInvalidateNamedFramebufferSubData(framebuffer, attachments.remaining(), MemoryUtil.getAddress(attachments), x, y, width, height, function_pointer);
	}
	static native void nglInvalidateNamedFramebufferSubData(int framebuffer, int attachments_numAttachments, long attachments, int x, int y, int width, int height, long function_pointer);

	public static void glClearNamedFramebuffer(int framebuffer, int buffer, int drawbuffer, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearNamedFramebufferiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 1);
		nglClearNamedFramebufferiv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglClearNamedFramebufferiv(int framebuffer, int buffer, int drawbuffer, long value, long function_pointer);

	public static void glClearNamedFramebufferu(int framebuffer, int buffer, int drawbuffer, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearNamedFramebufferuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglClearNamedFramebufferuiv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglClearNamedFramebufferuiv(int framebuffer, int buffer, int drawbuffer, long value, long function_pointer);

	public static void glClearNamedFramebuffer(int framebuffer, int buffer, int drawbuffer, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearNamedFramebufferfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 1);
		nglClearNamedFramebufferfv(framebuffer, buffer, drawbuffer, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglClearNamedFramebufferfv(int framebuffer, int buffer, int drawbuffer, long value, long function_pointer);

	public static void glClearNamedFramebufferfi(int framebuffer, int buffer, float depth, int stencil) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearNamedFramebufferfi;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearNamedFramebufferfi(framebuffer, buffer, depth, stencil, function_pointer);
	}
	static native void nglClearNamedFramebufferfi(int framebuffer, int buffer, float depth, int stencil, long function_pointer);

	public static void glBlitNamedFramebuffer(int readFramebuffer, int drawFramebuffer, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlitNamedFramebuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlitNamedFramebuffer(readFramebuffer, drawFramebuffer, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter, function_pointer);
	}
	static native void nglBlitNamedFramebuffer(int readFramebuffer, int drawFramebuffer, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter, long function_pointer);

	public static int glCheckNamedFramebufferStatus(int framebuffer, int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCheckNamedFramebufferStatus;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCheckNamedFramebufferStatus(framebuffer, target, function_pointer);
		return __result;
	}
	static native int nglCheckNamedFramebufferStatus(int framebuffer, int target, long function_pointer);

	public static void glGetNamedFramebufferParameter(int framebuffer, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedFramebufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetNamedFramebufferParameteriv(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetNamedFramebufferParameteriv(int framebuffer, int pname, long params, long function_pointer);

	/** Overloads glGetNamedFramebufferParameteriv. */
	public static int glGetNamedFramebufferParameter(int framebuffer, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedFramebufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetNamedFramebufferParameteriv(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetNamedFramebufferAttachmentParameter(int framebuffer, int attachment, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedFramebufferAttachmentParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetNamedFramebufferAttachmentParameteriv(framebuffer, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetNamedFramebufferAttachmentParameteriv(int framebuffer, int attachment, int pname, long params, long function_pointer);

	/** Overloads glGetNamedFramebufferAttachmentParameteriv. */
	public static int glGetNamedFramebufferAttachmentParameter(int framebuffer, int attachment, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedFramebufferAttachmentParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetNamedFramebufferAttachmentParameteriv(framebuffer, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glCreateRenderbuffers(IntBuffer renderbuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateRenderbuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(renderbuffers);
		nglCreateRenderbuffers(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
	}
	static native void nglCreateRenderbuffers(int renderbuffers_n, long renderbuffers, long function_pointer);

	/** Overloads glCreateRenderbuffers. */
	public static int glCreateRenderbuffers() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateRenderbuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer renderbuffers = APIUtil.getBufferInt(caps);
		nglCreateRenderbuffers(1, MemoryUtil.getAddress(renderbuffers), function_pointer);
		return renderbuffers.get(0);
	}

	public static void glNamedRenderbufferStorage(int renderbuffer, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedRenderbufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedRenderbufferStorage(renderbuffer, internalformat, width, height, function_pointer);
	}
	static native void nglNamedRenderbufferStorage(int renderbuffer, int internalformat, int width, int height, long function_pointer);

	public static void glNamedRenderbufferStorageMultisample(int renderbuffer, int samples, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedRenderbufferStorageMultisample;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedRenderbufferStorageMultisample(renderbuffer, samples, internalformat, width, height, function_pointer);
	}
	static native void nglNamedRenderbufferStorageMultisample(int renderbuffer, int samples, int internalformat, int width, int height, long function_pointer);

	public static void glGetNamedRenderbufferParameter(int renderbuffer, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedRenderbufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetNamedRenderbufferParameteriv(renderbuffer, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetNamedRenderbufferParameteriv(int renderbuffer, int pname, long params, long function_pointer);

	/** Overloads glGetNamedRenderbufferParameteriv. */
	public static int glGetNamedRenderbufferParameter(int renderbuffer, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedRenderbufferParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetNamedRenderbufferParameteriv(renderbuffer, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glCreateTextures(int target, IntBuffer textures) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(textures);
		nglCreateTextures(target, textures.remaining(), MemoryUtil.getAddress(textures), function_pointer);
	}
	static native void nglCreateTextures(int target, int textures_n, long textures, long function_pointer);

	/** Overloads glCreateTextures. */
	public static int glCreateTextures(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer textures = APIUtil.getBufferInt(caps);
		nglCreateTextures(target, 1, MemoryUtil.getAddress(textures), function_pointer);
		return textures.get(0);
	}

	public static void glTextureBuffer(int texture, int internalformat, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureBuffer(texture, internalformat, buffer, function_pointer);
	}
	static native void nglTextureBuffer(int texture, int internalformat, int buffer, long function_pointer);

	public static void glTextureBufferRange(int texture, int internalformat, int buffer, long offset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureBufferRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureBufferRange(texture, internalformat, buffer, offset, size, function_pointer);
	}
	static native void nglTextureBufferRange(int texture, int internalformat, int buffer, long offset, long size, long function_pointer);

	public static void glTextureStorage1D(int texture, int levels, int internalformat, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorage1D(texture, levels, internalformat, width, function_pointer);
	}
	static native void nglTextureStorage1D(int texture, int levels, int internalformat, int width, long function_pointer);

	public static void glTextureStorage2D(int texture, int levels, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorage2D(texture, levels, internalformat, width, height, function_pointer);
	}
	static native void nglTextureStorage2D(int texture, int levels, int internalformat, int width, int height, long function_pointer);

	public static void glTextureStorage3D(int texture, int levels, int internalformat, int width, int height, int depth) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorage3D(texture, levels, internalformat, width, height, depth, function_pointer);
	}
	static native void nglTextureStorage3D(int texture, int levels, int internalformat, int width, int height, int depth, long function_pointer);

	public static void glTextureStorage2DMultisample(int texture, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorage2DMultisample;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorage2DMultisample(texture, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
	}
	static native void nglTextureStorage2DMultisample(int texture, int samples, int internalformat, int width, int height, boolean fixedsamplelocations, long function_pointer);

	public static void glTextureStorage3DMultisample(int texture, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureStorage3DMultisample;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureStorage3DMultisample(texture, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
	}
	static native void nglTextureStorage3DMultisample(int texture, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations, long function_pointer);

	public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, 1, 1));
		nglTextureSubImage1D(texture, level, xoffset, width, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, long pixels, long function_pointer);
	public static void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureSubImage1DBO(texture, level, xoffset, width, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTextureSubImage1DBO(int texture, int level, int xoffset, int width, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, 1));
		nglTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels, long function_pointer);
	public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureSubImage2DBO(texture, level, xoffset, yoffset, width, height, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTextureSubImage2DBO(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkBuffer(pixels, GLChecks.calculateImageStorage(pixels, format, type, width, height, depth));
		nglTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels, long function_pointer);
	public static void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglTextureSubImage3DBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_buffer_offset, function_pointer);
	}
	static native void nglTextureSubImage3DBO(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long pixels_buffer_offset, long function_pointer);

	public static void glCompressedTextureSubImage1D(int texture, int level, int xoffset, int width, int format, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCompressedTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglCompressedTextureSubImage1D(texture, level, xoffset, width, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglCompressedTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int data_imageSize, long data, long function_pointer);
	public static void glCompressedTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int data_imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCompressedTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureSubImage1DBO(texture, level, xoffset, width, format, data_imageSize, data_buffer_offset, function_pointer);
	}
	static native void nglCompressedTextureSubImage1DBO(int texture, int level, int xoffset, int width, int format, int data_imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCompressedTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglCompressedTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, data.remaining(), MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglCompressedTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int data_imageSize, long data, long function_pointer);
	public static void glCompressedTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int data_imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCompressedTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureSubImage2DBO(texture, level, xoffset, yoffset, width, height, format, data_imageSize, data_buffer_offset, function_pointer);
	}
	static native void nglCompressedTextureSubImage2DBO(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int data_imageSize, long data_buffer_offset, long function_pointer);

	public static void glCompressedTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCompressedTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOdisabled(caps);
		BufferChecks.checkDirect(data);
		nglCompressedTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglCompressedTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data, long function_pointer);
	public static void glCompressedTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCompressedTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureUnpackPBOenabled(caps);
		nglCompressedTextureSubImage3DBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, imageSize, data_buffer_offset, function_pointer);
	}
	static native void nglCompressedTextureSubImage3DBO(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int imageSize, long data_buffer_offset, long function_pointer);

	public static void glCopyTextureSubImage1D(int texture, int level, int xoffset, int x, int y, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyTextureSubImage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTextureSubImage1D(texture, level, xoffset, x, y, width, function_pointer);
	}
	static native void nglCopyTextureSubImage1D(int texture, int level, int xoffset, int x, int y, int width, long function_pointer);

	public static void glCopyTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyTextureSubImage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTextureSubImage2D(texture, level, xoffset, yoffset, x, y, width, height, function_pointer);
	}
	static native void nglCopyTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int x, int y, int width, int height, long function_pointer);

	public static void glCopyTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyTextureSubImage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, x, y, width, height, function_pointer);
	}
	static native void nglCopyTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int x, int y, int width, int height, long function_pointer);

	public static void glTextureParameterf(int texture, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureParameterf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureParameterf(texture, pname, param, function_pointer);
	}
	static native void nglTextureParameterf(int texture, int pname, float param, long function_pointer);

	public static void glTextureParameter(int texture, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTextureParameterfv(int texture, int pname, long params, long function_pointer);

	public static void glTextureParameteri(int texture, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureParameteri(texture, pname, param, function_pointer);
	}
	static native void nglTextureParameteri(int texture, int pname, int param, long function_pointer);

	public static void glTextureParameterI(int texture, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTextureParameterIiv(int texture, int pname, long params, long function_pointer);

	public static void glTextureParameterIu(int texture, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTextureParameterIuiv(int texture, int pname, long params, long function_pointer);

	public static void glTextureParameter(int texture, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTextureParameteriv(int texture, int pname, long params, long function_pointer);

	public static void glGenerateTextureMipmap(int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenerateTextureMipmap;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglGenerateTextureMipmap(texture, function_pointer);
	}
	static native void nglGenerateTextureMipmap(int texture, long function_pointer);

	public static void glBindTextureUnit(int unit, int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindTextureUnit;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindTextureUnit(unit, texture, function_pointer);
	}
	static native void nglBindTextureUnit(int unit, int texture, long function_pointer);

	public static void glGetTextureImage(int texture, int level, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureImage(texture, level, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTextureImage(int texture, int level, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureImage(texture, level, format, type, (pixels.remaining() << 3), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTextureImage(int texture, int level, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureImage(texture, level, format, type, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTextureImage(int texture, int level, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureImage(texture, level, format, type, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTextureImage(int texture, int level, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureImage(texture, level, format, type, (pixels.remaining() << 1), MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglGetTextureImage(int texture, int level, int format, int type, int pixels_bufSize, long pixels, long function_pointer);
	public static void glGetTextureImage(int texture, int level, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetTextureImageBO(texture, level, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
	}
	static native void nglGetTextureImageBO(int texture, int level, int format, int type, int pixels_bufSize, long pixels_buffer_offset, long function_pointer);

	public static void glGetCompressedTextureImage(int texture, int level, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetCompressedTextureImage(texture, level, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetCompressedTextureImage(int texture, int level, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetCompressedTextureImage(texture, level, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetCompressedTextureImage(int texture, int level, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetCompressedTextureImage(texture, level, (pixels.remaining() << 1), MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglGetCompressedTextureImage(int texture, int level, int pixels_bufSize, long pixels, long function_pointer);
	public static void glGetCompressedTextureImage(int texture, int level, int pixels_bufSize, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetCompressedTextureImageBO(texture, level, pixels_bufSize, pixels_buffer_offset, function_pointer);
	}
	static native void nglGetCompressedTextureImageBO(int texture, int level, int pixels_bufSize, long pixels_buffer_offset, long function_pointer);

	public static void glGetTextureLevelParameter(int texture, int level, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureLevelParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetTextureLevelParameterfv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTextureLevelParameterfv(int texture, int level, int pname, long params, long function_pointer);

	/** Overloads glGetTextureLevelParameterfv. */
	public static float glGetTextureLevelParameterf(int texture, int level, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureLevelParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetTextureLevelParameterfv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTextureLevelParameter(int texture, int level, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureLevelParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetTextureLevelParameteriv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTextureLevelParameteriv(int texture, int level, int pname, long params, long function_pointer);

	/** Overloads glGetTextureLevelParameteriv. */
	public static int glGetTextureLevelParameteri(int texture, int level, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureLevelParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTextureLevelParameteriv(texture, level, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTextureParameter(int texture, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTextureParameterfv(int texture, int pname, long params, long function_pointer);

	/** Overloads glGetTextureParameterfv. */
	public static float glGetTextureParameterf(int texture, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetTextureParameterfv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTextureParameterI(int texture, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTextureParameterIiv(int texture, int pname, long params, long function_pointer);

	/** Overloads glGetTextureParameterIiv. */
	public static int glGetTextureParameterIi(int texture, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTextureParameterIiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTextureParameterIu(int texture, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTextureParameterIuiv(int texture, int pname, long params, long function_pointer);

	/** Overloads glGetTextureParameterIuiv. */
	public static int glGetTextureParameterIui(int texture, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTextureParameterIuiv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTextureParameter(int texture, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTextureParameteriv(int texture, int pname, long params, long function_pointer);

	/** Overloads glGetTextureParameteriv. */
	public static int glGetTextureParameteri(int texture, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTextureParameteriv(texture, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glCreateVertexArrays(IntBuffer arrays) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateVertexArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(arrays);
		nglCreateVertexArrays(arrays.remaining(), MemoryUtil.getAddress(arrays), function_pointer);
	}
	static native void nglCreateVertexArrays(int arrays_n, long arrays, long function_pointer);

	/** Overloads glCreateVertexArrays. */
	public static int glCreateVertexArrays() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateVertexArrays;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer arrays = APIUtil.getBufferInt(caps);
		nglCreateVertexArrays(1, MemoryUtil.getAddress(arrays), function_pointer);
		return arrays.get(0);
	}

	public static void glDisableVertexArrayAttrib(int vaobj, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDisableVertexArrayAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisableVertexArrayAttrib(vaobj, index, function_pointer);
	}
	static native void nglDisableVertexArrayAttrib(int vaobj, int index, long function_pointer);

	public static void glEnableVertexArrayAttrib(int vaobj, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEnableVertexArrayAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnableVertexArrayAttrib(vaobj, index, function_pointer);
	}
	static native void nglEnableVertexArrayAttrib(int vaobj, int index, long function_pointer);

	public static void glVertexArrayElementBuffer(int vaobj, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayElementBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexArrayElementBuffer(vaobj, buffer, function_pointer);
	}
	static native void nglVertexArrayElementBuffer(int vaobj, int buffer, long function_pointer);

	public static void glVertexArrayVertexBuffer(int vaobj, int bindingindex, int buffer, long offset, int stride) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayVertexBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexArrayVertexBuffer(vaobj, bindingindex, buffer, offset, stride, function_pointer);
	}
	static native void nglVertexArrayVertexBuffer(int vaobj, int bindingindex, int buffer, long offset, int stride, long function_pointer);

	public static void glVertexArrayVertexBuffers(int vaobj, int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayVertexBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (buffers != null)
			BufferChecks.checkBuffer(buffers, count);
		if (offsets != null)
			BufferChecks.checkBuffer(offsets, count);
		if (strides != null)
			BufferChecks.checkBuffer(strides, count);
		nglVertexArrayVertexBuffers(vaobj, first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(strides), function_pointer);
	}
	static native void nglVertexArrayVertexBuffers(int vaobj, int first, int count, long buffers, long offsets, long strides, long function_pointer);

	public static void glVertexArrayAttribFormat(int vaobj, int attribindex, int size, int type, boolean normalized, int relativeoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayAttribFormat;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexArrayAttribFormat(vaobj, attribindex, size, type, normalized, relativeoffset, function_pointer);
	}
	static native void nglVertexArrayAttribFormat(int vaobj, int attribindex, int size, int type, boolean normalized, int relativeoffset, long function_pointer);

	public static void glVertexArrayAttribIFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayAttribIFormat;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexArrayAttribIFormat(vaobj, attribindex, size, type, relativeoffset, function_pointer);
	}
	static native void nglVertexArrayAttribIFormat(int vaobj, int attribindex, int size, int type, int relativeoffset, long function_pointer);

	public static void glVertexArrayAttribLFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayAttribLFormat;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexArrayAttribLFormat(vaobj, attribindex, size, type, relativeoffset, function_pointer);
	}
	static native void nglVertexArrayAttribLFormat(int vaobj, int attribindex, int size, int type, int relativeoffset, long function_pointer);

	public static void glVertexArrayAttribBinding(int vaobj, int attribindex, int bindingindex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayAttribBinding;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexArrayAttribBinding(vaobj, attribindex, bindingindex, function_pointer);
	}
	static native void nglVertexArrayAttribBinding(int vaobj, int attribindex, int bindingindex, long function_pointer);

	public static void glVertexArrayBindingDivisor(int vaobj, int bindingindex, int divisor) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexArrayBindingDivisor;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexArrayBindingDivisor(vaobj, bindingindex, divisor, function_pointer);
	}
	static native void nglVertexArrayBindingDivisor(int vaobj, int bindingindex, int divisor, long function_pointer);

	public static void glGetVertexArray(int vaobj, int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexArrayiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 1);
		nglGetVertexArrayiv(vaobj, pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglGetVertexArrayiv(int vaobj, int pname, long param, long function_pointer);

	/** Overloads glGetVertexArrayiv. */
	public static int glGetVertexArray(int vaobj, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexArrayiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer param = APIUtil.getBufferInt(caps);
		nglGetVertexArrayiv(vaobj, pname, MemoryUtil.getAddress(param), function_pointer);
		return param.get(0);
	}

	public static void glGetVertexArrayIndexed(int vaobj, int index, int pname, IntBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexArrayIndexediv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 1);
		nglGetVertexArrayIndexediv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglGetVertexArrayIndexediv(int vaobj, int index, int pname, long param, long function_pointer);

	/** Overloads glGetVertexArrayIndexediv. */
	public static int glGetVertexArrayIndexed(int vaobj, int index, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexArrayIndexediv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer param = APIUtil.getBufferInt(caps);
		nglGetVertexArrayIndexediv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
		return param.get(0);
	}

	public static void glGetVertexArrayIndexed64i(int vaobj, int index, int pname, LongBuffer param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexArrayIndexed64iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(param, 1);
		nglGetVertexArrayIndexed64iv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
	}
	static native void nglGetVertexArrayIndexed64iv(int vaobj, int index, int pname, long param, long function_pointer);

	/** Overloads glGetVertexArrayIndexed64iv. */
	public static long glGetVertexArrayIndexed64i(int vaobj, int index, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexArrayIndexed64iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer param = APIUtil.getBufferLong(caps);
		nglGetVertexArrayIndexed64iv(vaobj, index, pname, MemoryUtil.getAddress(param), function_pointer);
		return param.get(0);
	}

	public static void glCreateSamplers(IntBuffer samplers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateSamplers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(samplers);
		nglCreateSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
	}
	static native void nglCreateSamplers(int samplers_n, long samplers, long function_pointer);

	/** Overloads glCreateSamplers. */
	public static int glCreateSamplers() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateSamplers;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer samplers = APIUtil.getBufferInt(caps);
		nglCreateSamplers(1, MemoryUtil.getAddress(samplers), function_pointer);
		return samplers.get(0);
	}

	public static void glCreateProgramPipelines(IntBuffer pipelines) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateProgramPipelines;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pipelines);
		nglCreateProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
	}
	static native void nglCreateProgramPipelines(int pipelines_n, long pipelines, long function_pointer);

	/** Overloads glCreateProgramPipelines. */
	public static int glCreateProgramPipelines() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateProgramPipelines;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer pipelines = APIUtil.getBufferInt(caps);
		nglCreateProgramPipelines(1, MemoryUtil.getAddress(pipelines), function_pointer);
		return pipelines.get(0);
	}

	public static void glCreateQueries(int target, IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateQueries;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglCreateQueries(target, ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglCreateQueries(int target, int ids_n, long ids, long function_pointer);

	/** Overloads glCreateQueries. */
	public static int glCreateQueries(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateQueries;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer ids = APIUtil.getBufferInt(caps);
		nglCreateQueries(target, 1, MemoryUtil.getAddress(ids), function_pointer);
		return ids.get(0);
	}

	public static void glMemoryBarrierByRegion(int barriers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMemoryBarrierByRegion;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMemoryBarrierByRegion(barriers, function_pointer);
	}
	static native void nglMemoryBarrierByRegion(int barriers, long function_pointer);

	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (pixels.remaining() << 3), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, (pixels.remaining() << 1), MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, int pixels_bufSize, long pixels, long function_pointer);
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetTextureSubImageBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
	}
	static native void nglGetTextureSubImageBO(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, int pixels_bufSize, long pixels_buffer_offset, long function_pointer);

	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, (pixels.remaining() << 3), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, (pixels.remaining() << 1), MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int pixels_bufSize, long pixels, long function_pointer);
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int pixels_bufSize, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetCompressedTextureSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglGetCompressedTextureSubImageBO(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels_bufSize, pixels_buffer_offset, function_pointer);
	}
	static native void nglGetCompressedTextureSubImageBO(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int pixels_bufSize, long pixels_buffer_offset, long function_pointer);

	public static void glTextureBarrier() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTextureBarrier;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTextureBarrier(function_pointer);
	}
	static native void nglTextureBarrier(long function_pointer);

	public static int glGetGraphicsResetStatus() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetGraphicsResetStatus;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetGraphicsResetStatus(function_pointer);
		return __result;
	}
	static native int nglGetGraphicsResetStatus(long function_pointer);

	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglReadnPixels(x, y, width, height, format, type, pixels.remaining(), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, DoubleBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglReadnPixels(x, y, width, height, format, type, (pixels.remaining() << 3), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, FloatBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglReadnPixels(x, y, width, height, format, type, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglReadnPixels(x, y, width, height, format, type, (pixels.remaining() << 2), MemoryUtil.getAddress(pixels), function_pointer);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, ShortBuffer pixels) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOdisabled(caps);
		BufferChecks.checkDirect(pixels);
		nglReadnPixels(x, y, width, height, format, type, (pixels.remaining() << 1), MemoryUtil.getAddress(pixels), function_pointer);
	}
	static native void nglReadnPixels(int x, int y, int width, int height, int format, int type, int pixels_bufSize, long pixels, long function_pointer);
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReadnPixels;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensurePackPBOenabled(caps);
		nglReadnPixelsBO(x, y, width, height, format, type, pixels_bufSize, pixels_buffer_offset, function_pointer);
	}
	static native void nglReadnPixelsBO(int x, int y, int width, int height, int format, int type, int pixels_bufSize, long pixels_buffer_offset, long function_pointer);

	public static void glGetnUniform(int program, int location, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnUniformfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetnUniformfv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetnUniformfv(int program, int location, int params_bufSize, long params, long function_pointer);

	public static void glGetnUniform(int program, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnUniformiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetnUniformiv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetnUniformiv(int program, int location, int params_bufSize, long params, long function_pointer);

	public static void glGetnUniformu(int program, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetnUniformuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetnUniformuiv(program, location, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetnUniformuiv(int program, int location, int params_bufSize, long params, long function_pointer);
}
