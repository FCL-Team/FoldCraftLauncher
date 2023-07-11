/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBBufferStorage {

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

	private ARBBufferStorage() {}

	public static void glBufferStorage(int target, ByteBuffer data, int flags) {
		GL44.glBufferStorage(target, data, flags);
	}
	public static void glBufferStorage(int target, DoubleBuffer data, int flags) {
		GL44.glBufferStorage(target, data, flags);
	}
	public static void glBufferStorage(int target, FloatBuffer data, int flags) {
		GL44.glBufferStorage(target, data, flags);
	}
	public static void glBufferStorage(int target, IntBuffer data, int flags) {
		GL44.glBufferStorage(target, data, flags);
	}
	public static void glBufferStorage(int target, ShortBuffer data, int flags) {
		GL44.glBufferStorage(target, data, flags);
	}
	public static void glBufferStorage(int target, LongBuffer data, int flags) {
		GL44.glBufferStorage(target, data, flags);
	}

	/** Overloads glBufferStorage. */
	public static void glBufferStorage(int target, long size, int flags) {
		GL44.glBufferStorage(target, size, flags);
	}

	public static void glNamedBufferStorageEXT(int buffer, ByteBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorageEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorageEXT(buffer, data.remaining(), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorageEXT(int buffer, DoubleBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorageEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorageEXT(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorageEXT(int buffer, FloatBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorageEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorageEXT(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorageEXT(int buffer, IntBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorageEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorageEXT(buffer, (data.remaining() << 2), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorageEXT(int buffer, ShortBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorageEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorageEXT(buffer, (data.remaining() << 1), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	public static void glNamedBufferStorageEXT(int buffer, LongBuffer data, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorageEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglNamedBufferStorageEXT(buffer, (data.remaining() << 3), MemoryUtil.getAddress(data), flags, function_pointer);
	}
	static native void nglNamedBufferStorageEXT(int buffer, long data_size, long data, int flags, long function_pointer);

	/** Overloads glNamedBufferStorageEXT. */
	public static void glNamedBufferStorageEXT(int buffer, long size, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedBufferStorageEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedBufferStorageEXT(buffer, size, 0L, flags, function_pointer);
	}
}
