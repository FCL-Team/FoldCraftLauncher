/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESMapbuffer {

	/**
	 * Accepted by the &lt;access&gt; parameter of MapBufferOES: 
	 */
	public static final int GL_WRITE_ONLY_OES = 0x88B9;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetBufferParameteriv: 
	 */
	public static final int GL_BUFFER_ACCESS_OES = 0x88BB,
		GL_BUFFER_MAPPED_OES = 0x88BC;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBufferPointervOES: 
	 */
	public static final int GL_BUFFER_MAP_POINTER_OES = 0x88BD;

	private OESMapbuffer() {}

	static native void initNativeStubs() throws LWJGLException;

	public static ByteBuffer glGetBufferPointerOES(int target, int pname, ByteBuffer old_buffer) {
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglGetBufferPointervOES(target, pname, GLES20.glGetBufferParameteri(target, GLES20.GL_BUFFER_SIZE), old_buffer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	public static ByteBuffer glGetBufferPointervOES(int target, int pname, long length, ByteBuffer old_buffer) {
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglGetBufferPointervOES(target, pname, length, old_buffer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetBufferPointervOES(int target, int pname, long result_size, ByteBuffer old_buffer);

	/**
	 *  glMapBufferOES maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapBuffer like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapBufferOES(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferOES(..., ..., mapped_buffer);
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
	public static ByteBuffer glMapBufferOES(int target, int access, ByteBuffer old_buffer) {
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBufferOES(target, access, GLES20.glGetBufferParameteri(target, GLES20.GL_BUFFER_SIZE), old_buffer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	/**
	 *  glMapBufferOES maps a GL buffer object to a ByteBuffer. The old_buffer argument can be null,
	 *  in which case a new ByteBuffer will be created, pointing to the returned memory. If old_buffer is non-null,
	 *  it will be returned if it points to the same mapped memory and has the same capacity as the buffer object,
	 *  otherwise a new ByteBuffer is created. That way, an application will normally use glMapBuffer like this:
	 *  <p/>
	 *  ByteBuffer mapped_buffer; mapped_buffer = glMapBufferOES(..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferOES(..., ..., mapped_buffer);
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
	public static ByteBuffer glMapBufferOES(int target, int access, long length, ByteBuffer old_buffer) {
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		ByteBuffer __result = nglMapBufferOES(target, access, length, old_buffer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglMapBufferOES(int target, int access, long result_size, ByteBuffer old_buffer);

	public static boolean glUnmapBufferOES(int target) {
		boolean __result = nglUnmapBufferOES(target);
		return __result;
	}
	static native boolean nglUnmapBufferOES(int target);
}
