/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class APPLESync {

	/**
	 * Accepted as the &lt;pname&gt; parameter of GetInteger64vAPPLE: 
	 */
	public static final int GL_MAX_SERVER_WAIT_TIMEOUT_APPLE = 0x9111;

	/**
	 * Accepted as the &lt;pname&gt; parameter of GetSyncivAPPLE: 
	 */
	public static final int GL_OBJECT_TYPE_APPLE = 0x9112,
		SYNC_CONDITION_APPLE = 0x9113,
		SYNC_STATUS_APPLE = 0x9114,
		SYNC_FLAGS_APPLE = 0x9115;

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; OBJECT_TYPE_APPLE: 
	 */
	public static final int GL_SYNC_FENCE_APPLE = 0x9116;

	/**
	 * Returned in &lt;values&gt; for GetSyncivAPPLE &lt;pname&gt; SYNC_CONDITION_APPLE: 
	 */
	public static final int GL_SYNC_GPU_COMMANDS_COMPLETE_APPLE = 0x9117;

	/**
	 * Returned in &lt;values&gt; for GetSyncivAPPLE &lt;pname&gt; SYNC_STATUS_APPLE: 
	 */
	public static final int GL_UNSIGNALED_APPLE = 0x9118,
		SIGNALED_APPLE = 0x9119;

	/**
	 * Accepted in the &lt;flags&gt; parameter of ClientWaitSyncAPPLE: 
	 */
	public static final int GL_SYNC_FLUSH_COMMANDS_BIT_APPLE = 0x1;

	/**
	 * Accepted in the &lt;timeout&gt; parameter of WaitSyncAPPLE: 
	 */
	public static final long GL_TIMEOUT_IGNORED_APPLE = 0xFFFFFFFFFFFFFFFFL;

	/**
	 * Returned by ClientWaitSyncAPPLE: 
	 */
	public static final int GL_ALREADY_SIGNALED_APPLE = 0x911A,
		TIMEOUT_EXPIRED_APPLE = 0x911B,
		CONDITION_SATISFIED_APPLE = 0x911C,
		WAIT_FAILED_APPLE = 0x911D;

	/**
	 *  Accepted by the &lt;type&gt; parameter of LabelObjectEXT and
	 *  GetObjectLabelEXT:
	 */
	public static final int GL_SYNC_OBJECT_APPLE = 0x8A53;

	private APPLESync() {}

	static native void initNativeStubs() throws LWJGLException;

	public static GLSync glFenceSyncAPPLE(int condition, int flags) {
		GLSync __result = new GLSync(nglFenceSyncAPPLE(condition, flags));
		return __result;
	}
	static native long nglFenceSyncAPPLE(int condition, int flags);

	public static boolean glIsSyncAPPLE(GLSync sync) {
		boolean __result = nglIsSyncAPPLE(sync.getPointer());
		return __result;
	}
	static native boolean nglIsSyncAPPLE(long sync);

	public static void glDeleteSyncAPPLE(GLSync sync) {
		nglDeleteSyncAPPLE(sync.getPointer());
	}
	static native void nglDeleteSyncAPPLE(long sync);

	public static int glClientWaitSyncAPPLE(GLSync sync, int flags, long timeout) {
		int __result = nglClientWaitSyncAPPLE(sync.getPointer(), flags, timeout);
		return __result;
	}
	static native int nglClientWaitSyncAPPLE(long sync, int flags, long timeout);

	public static void glWaitSyncAPPLE(GLSync sync, int flags, long timeout) {
		nglWaitSyncAPPLE(sync.getPointer(), flags, timeout);
	}
	static native void nglWaitSyncAPPLE(long sync, int flags, long timeout);

	public static void glGetInteger64APPLE(int pname, LongBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetInteger64vAPPLE(pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetInteger64vAPPLE(int pname, long params);

	/** Overloads glGetInteger64vAPPLE. */
	public static long glGetInteger64APPLE(int pname) {
		LongBuffer params = APIUtil.getBufferLong();
		nglGetInteger64vAPPLE(pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGetSyncAPPLE(GLSync sync, int pname, IntBuffer length, IntBuffer values) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(values);
		nglGetSyncivAPPLE(sync.getPointer(), pname, values.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(values));
	}
	static native void nglGetSyncivAPPLE(long sync, int pname, int values_bufSize, long length, long values);

	/** Overloads glGetSyncivAPPLE. */
	public static int glGetSynciAPPLE(GLSync sync, int pname) {
		IntBuffer values = APIUtil.getBufferInt();
		nglGetSyncivAPPLE(sync.getPointer(), pname, 1, 0L, MemoryUtil.getAddress(values));
		return values.get(0);
	}
}
