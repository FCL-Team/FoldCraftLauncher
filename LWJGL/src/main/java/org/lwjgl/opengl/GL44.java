/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL44 {

	/**
	 * Implementation-dependent state which constrains the maximum value of stride parameters to vertex array pointer-setting commands. 
	 */
	public static final int GL_MAX_VERTEX_ATTRIB_STRIDE = 0x82E5;

	/**
	 *  Accepted in the &lt;flags&gt; parameter of BufferStorage and
	 *  NamedBufferStorageEXT:
	 */
	public static final int GL_MAP_PERSISTENT_BIT = 0x40,
		GL_MAP_COHERENT_BIT = 0x80,
		GL_DYNAMIC_STORAGE_BIT = 0x100,
		GL_CLIENT_STORAGE_BIT = 0x200;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBufferParameter{i|i64}v:\ 
	 */
	public static final int GL_BUFFER_IMMUTABLE_STORAGE = 0x821F,
		GL_BUFFER_STORAGE_FLAGS = 0x8220;

	/**
	 * Accepted by the &lt;barriers&gt; parameter of MemoryBarrier: 
	 */
	public static final int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 0x4000;

	/**
	 *  Accepted by the &lt;pname&gt; parameter for GetInternalformativ and
	 *  GetInternalformati64v:
	 */
	public static final int GL_CLEAR_TEXTURE = 0x9365;

	/**
	 * Accepted in the &lt;props&gt; array of GetProgramResourceiv: 
	 */
	public static final int GL_LOCATION_COMPONENT = 0x934A,
		GL_TRANSFORM_FEEDBACK_BUFFER_INDEX = 0x934B,
		GL_TRANSFORM_FEEDBACK_BUFFER_STRIDE = 0x934C;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetQueryObjectiv, GetQueryObjectuiv,
	 *  GetQueryObjecti64v and GetQueryObjectui64v:
	 */
	public static final int GL_QUERY_RESULT_NO_WAIT = 0x9194;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, MapBufferRange, GetBufferSubData,
	 *  GetBufferParameteriv, GetBufferParameteri64v, GetBufferPointerv,
	 *  ClearBufferSubData, and the &lt;readtarget&gt; and &lt;writetarget&gt; parameters of
	 *  CopyBufferSubData:
	 */
	public static final int GL_QUERY_BUFFER = 0x9192;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_QUERY_BUFFER_BINDING = 0x9193;

	/**
	 * Accepted in the &lt;barriers&gt; bitfield in MemoryBarrier: 
	 */
	public static final int GL_QUERY_BUFFER_BARRIER_BIT = 0x8000;

	/**
	 *  Accepted by the &lt;param&gt; parameter of TexParameter{if}, SamplerParameter{if}
	 *  and SamplerParameter{if}v, and by the &lt;params&gt; parameter of
	 *  TexParameter{if}v, TexParameterI{i ui}v and SamplerParameterI{i ui}v when
	 *  their &lt;pname&gt; parameter is TEXTURE_WRAP_S, TEXTURE_WRAP_T, or
	 *  TEXTURE_WRAP_R:
	 */
	public static final int GL_MIRROR_CLAMP_TO_EDGE = 0x8743;

	private GL44() {}

	public static void glBufferStorage(int target, ByteBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferStorage(target, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glBufferStorage(int target, DoubleBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferStorage(target, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glBufferStorage(int target, FloatBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferStorage(target, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glBufferStorage(int target, IntBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferStorage(target, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glBufferStorage(int target, ShortBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferStorage(target, (data.remaining() << 1), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glBufferStorage(int target, LongBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglBufferStorage(target, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	static native void nglBufferStorage(int target, long data_size, long data, int flags, long function_pointer);

	/** Overloads glBufferStorage. */
	public static void glBufferStorage(int target, long size, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBufferStorage;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBufferStorage(target, size, 0L, flags, function_pointer);
	}

	public static void glClearTexImage(int texture, int level, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, LongBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexImage(texture, level, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	static native void nglClearTexImage(int texture, int level, int format, int type, long data, long function_pointer);

	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, LongBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearTexSubImage;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (data != null)
			BufferChecks.checkBuffer(data, 1);
		nglClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddressSafe(data), function_pointer);
	}
	static native void nglClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long data, long function_pointer);

	public static void glBindBuffersBase(int target, int first, int count, IntBuffer buffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBuffersBase;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (buffers != null)
			BufferChecks.checkBuffer(buffers, count);
		nglBindBuffersBase(target, first, count, MemoryUtil.getAddressSafe(buffers), function_pointer);
	}
	static native void nglBindBuffersBase(int target, int first, int count, long buffers, long function_pointer);

	public static void glBindBuffersRange(int target, int first, int count, IntBuffer buffers, PointerBuffer offsets, PointerBuffer sizes) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBuffersRange;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (buffers != null)
			BufferChecks.checkBuffer(buffers, count);
		if (offsets != null)
			BufferChecks.checkBuffer(offsets, count);
		if (sizes != null)
			BufferChecks.checkBuffer(sizes, count);
		nglBindBuffersRange(target, first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(sizes), function_pointer);
	}
	static native void nglBindBuffersRange(int target, int first, int count, long buffers, long offsets, long sizes, long function_pointer);

	public static void glBindTextures(int first, int count, IntBuffer textures) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (textures != null)
			BufferChecks.checkBuffer(textures, count);
		nglBindTextures(first, count, MemoryUtil.getAddressSafe(textures), function_pointer);
	}
	static native void nglBindTextures(int first, int count, long textures, long function_pointer);

	public static void glBindSamplers(int first, int count, IntBuffer samplers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindSamplers;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (samplers != null)
			BufferChecks.checkBuffer(samplers, count);
		nglBindSamplers(first, count, MemoryUtil.getAddressSafe(samplers), function_pointer);
	}
	static native void nglBindSamplers(int first, int count, long samplers, long function_pointer);

	public static void glBindImageTextures(int first, int count, IntBuffer textures) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindImageTextures;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (textures != null)
			BufferChecks.checkBuffer(textures, count);
		nglBindImageTextures(first, count, MemoryUtil.getAddressSafe(textures), function_pointer);
	}
	static native void nglBindImageTextures(int first, int count, long textures, long function_pointer);

	public static void glBindVertexBuffers(int first, int count, IntBuffer buffers, PointerBuffer offsets, IntBuffer strides) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindVertexBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (buffers != null)
			BufferChecks.checkBuffer(buffers, count);
		if (offsets != null)
			BufferChecks.checkBuffer(offsets, count);
		if (strides != null)
			BufferChecks.checkBuffer(strides, count);
		nglBindVertexBuffers(first, count, MemoryUtil.getAddressSafe(buffers), MemoryUtil.getAddressSafe(offsets), MemoryUtil.getAddressSafe(strides), function_pointer);
	}
	static native void nglBindVertexBuffers(int first, int count, long buffers, long offsets, long strides, long function_pointer);
}
