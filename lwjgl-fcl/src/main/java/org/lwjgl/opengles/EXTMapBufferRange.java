/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTMapBufferRange {

	/**
	 * Accepted by the &lt;access&gt; parameter of MapBufferRangeEXT: 
	 */
	public static final int GL_MAP_READ_BIT_EXT = 0x1,
		GL_MAP_WRITE_BIT_EXT = 0x2,
		GL_MAP_INVALIDATE_RANGE_BIT_EXT = 0x4,
		GL_MAP_INVALIDATE_BUFFER_BIT_EXT = 0x8,
		GL_MAP_FLUSH_EXPLICIT_BIT_EXT = 0x10,
		GL_MAP_UNSYNCHRONIZED_BIT_EXT = 0x20;

	private EXTMapBufferRange() {}

	static native void initNativeStubs() throws LWJGLException;

	/**
	 *  glMapBufferRange maps a GL buffer object range to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapBufferRange like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapBufferRange(..., ..., ..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferRange(..., ..., ..., ..., mapped_buffer);
	 *  <p/>
	 *  Only ByteBuffers returned from this method are to be passed as the old_buffer argument. User-created ByteBuffers cannot be reused.
	 * <p>
	 *  @param old_buffer A ByteBuffer. If this argument points to the same address and has the same capacity as the new mapping, it will be returned and no new buffer will be created.
	 * <p>
	 *  @return A ByteBuffer representing the mapped buffer memory.
	 */
	public static ByteBuffer glMapBufferRangeEXT(int target, long offset, long length, int access, ByteBuffer old_buffer) {
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBufferRangeEXT(target, offset, length, access, old_buffer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapBufferRangeEXT(int target, long offset, long length, int access, ByteBuffer old_buffer);

	public static void glFlushMappedBufferRangeEXT(int target, long offset, long length) {
		nglFlushMappedBufferRangeEXT(target, offset, length);
	}
	static native void nglFlushMappedBufferRangeEXT(int target, long offset, long length);
}
