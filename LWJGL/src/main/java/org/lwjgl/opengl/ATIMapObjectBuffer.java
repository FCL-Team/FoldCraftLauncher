/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ATIMapObjectBuffer {

	private ATIMapObjectBuffer() {}

	/**
	 *  glMapObjectBufferATI maps an ATI vertex array object to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the vertex array object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapObjectBufferATI like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapObjectBufferATI(..., null); ... // Another map on the same buffer mapped_buffer = glMapObjectBufferATI(..., mapped_buffer);
	 *  <p/>
	 *  Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 *  <p/>
	 *  The version of this method without an explicit length argument calls glGetObjectBufferATI internally to
	 *  retrieve the current vertex array object size, which may cause a pipeline flush and reduce application performance.
	 *  <p/>
	 *  The version of this method with an explicit length argument is a fast alternative to the one without. No GL call
	 *  is made to retrieve the vertex array object size, so the user is responsible for tracking and using the appropriate length.<br>
	 *  Security warning: The length argument should match the vertex array object size. Reading from or writing to outside
	 *  the memory region that corresponds to the mapped vertex array object will cause native crashes.
	 * <p>
	 *  @param length        the length of the mapped memory in bytes.
	 *  @param old_buffer    A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 * <p>
	 *  @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static ByteBuffer glMapObjectBufferATI(int buffer, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapObjectBufferATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapObjectBufferATI(buffer, ATIVertexArrayObject.glGetObjectBufferiATI(buffer, ATIVertexArrayObject.GL_OBJECT_BUFFER_SIZE_ATI), old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	/**
	 *  glMapObjectBufferATI maps an ATI vertex array object to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the vertex array object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapObjectBufferATI like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapObjectBufferATI(..., null); ... // Another map on the same buffer mapped_buffer = glMapObjectBufferATI(..., mapped_buffer);
	 *  <p/>
	 *  Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 *  <p/>
	 *  The version of this method without an explicit length argument calls glGetObjectBufferATI internally to
	 *  retrieve the current vertex array object size, which may cause a pipeline flush and reduce application performance.
	 *  <p/>
	 *  The version of this method with an explicit length argument is a fast alternative to the one without. No GL call
	 *  is made to retrieve the vertex array object size, so the user is responsible for tracking and using the appropriate length.<br>
	 *  Security warning: The length argument should match the vertex array object size. Reading from or writing to outside
	 *  the memory region that corresponds to the mapped vertex array object will cause native crashes.
	 * <p>
	 *  @param length        the length of the mapped memory in bytes.
	 *  @param old_buffer    A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 * <p>
	 *  @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static ByteBuffer glMapObjectBufferATI(int buffer, long length, ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMapObjectBufferATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapObjectBufferATI(buffer, length, old_buffer, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapObjectBufferATI(int buffer, long result_size, ByteBuffer old_buffer, long function_pointer);

	public static void glUnmapObjectBufferATI(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUnmapObjectBufferATI;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUnmapObjectBufferATI(buffer, function_pointer);
	}
	static native void nglUnmapObjectBufferATI(int buffer, long function_pointer);
}
