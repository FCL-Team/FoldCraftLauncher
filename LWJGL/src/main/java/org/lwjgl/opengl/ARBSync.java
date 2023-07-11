/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBSync {

	/**
	 * Accepted as the &lt;pname&gt; parameter of GetInteger64v: 
	 */
	public static final int GL_MAX_SERVER_WAIT_TIMEOUT = 0x9111;

	/**
	 * Accepted as the &lt;pname&gt; parameter of GetSynciv: 
	 */
	public static final int GL_OBJECT_TYPE = 0x9112,
		GL_SYNC_CONDITION = 0x9113,
		GL_SYNC_STATUS = 0x9114,
		GL_SYNC_FLAGS = 0x9115;

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; OBJECT_TYPE: 
	 */
	public static final int GL_SYNC_FENCE = 0x9116;

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; SYNC_CONDITION: 
	 */
	public static final int GL_SYNC_GPU_COMMANDS_COMPLETE = 0x9117;

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; SYNC_STATUS: 
	 */
	public static final int GL_UNSIGNALED = 0x9118,
		GL_SIGNALED = 0x9119;

	/**
	 * Accepted in the &lt;flags&gt; parameter of ClientWaitSync: 
	 */
	public static final int GL_SYNC_FLUSH_COMMANDS_BIT = 0x1;

	/**
	 * Accepted in the &lt;timeout&gt; parameter of WaitSync: 
	 */
	public static final long GL_TIMEOUT_IGNORED = 0xFFFFFFFFFFFFFFFFL;

	/**
	 * Returned by ClientWaitSync: 
	 */
	public static final int GL_ALREADY_SIGNALED = 0x911A,
		GL_TIMEOUT_EXPIRED = 0x911B,
		GL_CONDITION_SATISFIED = 0x911C,
		GL_WAIT_FAILED = 0x911D;

	private ARBSync() {}

	public static GLSync glFenceSync(int condition, int flags) {
		return GL32.glFenceSync(condition, flags);
	}

	public static boolean glIsSync(GLSync sync) {
		return GL32.glIsSync(sync);
	}

	public static void glDeleteSync(GLSync sync) {
		GL32.glDeleteSync(sync);
	}

	public static int glClientWaitSync(GLSync sync, int flags, long timeout) {
		return GL32.glClientWaitSync(sync, flags, timeout);
	}

	public static void glWaitSync(GLSync sync, int flags, long timeout) {
		GL32.glWaitSync(sync, flags, timeout);
	}

	public static void glGetInteger64(int pname, LongBuffer params) {
		GL32.glGetInteger64(pname, params);
	}

	/** Overloads glGetInteger64v. */
	public static long glGetInteger64(int pname) {
		return GL32.glGetInteger64(pname);
	}

	public static void glGetSync(GLSync sync, int pname, IntBuffer length, IntBuffer values) {
		GL32.glGetSync(sync, pname, length, values);
	}

	/**
	 * Overloads glGetSynciv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetSynci} instead. 
	 */
	@Deprecated
	public static int glGetSync(GLSync sync, int pname) {
		return GL32.glGetSynci(sync, pname);
	}

	/** Overloads glGetSynciv. */
	public static int glGetSynci(GLSync sync, int pname) {
		return GL32.glGetSynci(sync, pname);
	}
}
