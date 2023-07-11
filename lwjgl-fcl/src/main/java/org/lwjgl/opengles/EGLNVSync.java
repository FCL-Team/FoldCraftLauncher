/*
 * Copyright (c) 2002-2011 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.opengles;

import org.lwjgl.LWJGLException;
import org.lwjgl.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengles.EGL.*;

/** EGL_NV_sync wrapper class. */
public final class EGLNVSync {

	/**
	 * Accepted in the &lt;condition&gt; parameter of eglCreateFenceSyncNV, and
	 * returned in &lt;value&gt; when eglGetSyncAttribNV is called with &lt;attribute&gt;
	 * EGL_SYNC_CONDITION_NV:
	 */
	public static final int EGL_SYNC_PRIOR_COMMANDS_COMPLETE_NV = 0x30E6;

	/**
	 * Accepted as an attribute name in the &lt;attrib_list&gt; parameter of
	 * eglCreateFenceSyncNV, and by the &lt;attribute&gt; parameter of
	 * eglGetSyncAttribNV:
	 */
	public static final int EGL_SYNC_STATUS_NV = 0x30E7;

	/**
	 * Accepted as an attribute value in the &lt;attrib_list&gt; parameter of
	 * eglCreateFenceSyncNV for the EGL_SYNC_STATUS_NV attribute, by
	 * the &lt;mode&gt; parameter of eglSignalSyncNV and returned in &lt;value&gt;
	 * when eglGetSyncAttribNV is called with &lt;attribute&gt;
	 * EGL_SYNC_STATUS_NV:
	 */
	public static final int EGL_SIGNALED_NV = 0x30E8,
		EGL_UNSIGNALED_NV                   = 0x30E9;

	/** Accepted in the &lt;flags&gt; parameter of eglClientWaitSyncNV: */
	public static final int EGL_SYNC_FLUSH_COMMANDS_BIT_NV = 0x0001;

	/** Accepted in the &lt;timeout&gt; parameter of eglClientWaitSyncNV: */
	public static final long EGL_FOREVER_NV = 0xFFFFFFFFFFFFFFFFL;

	/** Returned by eglClientWaitSyncNV: */
	public static final int EGL_ALREADY_SIGNALED_NV = 0x30EA,
		EGL_TIMEOUT_EXPIRED_NV                      = 0x30EB,
		EGL_CONDITION_SATISFIED_NV                  = 0x30EC;

	/** Accepted in the &lt;attribute&gt; parameter of eglGetSyncAttribNV: */
	public static final int EGL_SYNC_TYPE_NV = 0x30ED,
		EGL_SYNC_CONDITION_NV                = 0x30EE;

	/**
	 * Returned in &lt;value&gt; when eglGetSyncAttribNV is called with
	 * &lt;attribute&gt; EGL_SYNC_TYPE_NV:
	 */
	public static final int EGL_SYNC_FENCE_NV = 0x30EF;

	/** Returned by eglCreateFenceSyncNV in the event of an error: */
	public static final long EGL_NO_SYNC_NV = 0;

	static {
		initNativeStubs();
	}

	private EGLNVSync() {
	}

	private static native void initNativeStubs();

	/**
	 * Creates a fence sync object for the specified EGL display and returns
	 * a handle to the new object.
	 *
	 * @param dpy         the EGL display
	 * @param condition   the sync condition
	 * @param attrib_list an attribute list (may be null)
	 *
	 * @return the created fence sync object
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static EGLSyncNV eglCreateFenceSyncNV(EGLDisplay dpy, int condition, IntBuffer attrib_list) throws LWJGLException {
		checkAttribList(attrib_list);

		final long pointer = neglCreateFenceSyncNV(dpy.getPointer(), condition, MemoryUtil.getAddressSafe(attrib_list));

		if ( pointer == EGL_NO_SYNC_NV )
			throwEGLError("Failed to create NV fence sync object.");

		return new EGLSyncNV(pointer);
	}

	private static native long neglCreateFenceSyncNV(long dpy_ptr, int condition, long attrib_list);

	/**
	 * Destroys an existing sync object.
	 *
	 * @param sync the sync object
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static void eglDestroySyncNV(EGLSyncNV sync) throws LWJGLException {
		if ( !neglDestroySyncNV(sync.getPointer()) )
			throwEGLError("Failed to destroy NV fence sync object.");
	}

	private static native boolean neglDestroySyncNV(long sync_ptr);

	/**
	 * Inserts a fence command into the command stream of the bound API's current
	 * context and associates it with sync object.
	 *
	 * @param sync the sync object
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static void eglFenceNV(EGLSyncNV sync) throws LWJGLException {
		if ( !neglFenceNV(sync.getPointer()) )
			throwEGLError("Failed to insert NV fence command.");
	}

	private static native boolean neglFenceNV(long sync_ptr);

	/**
	 * Blocks the calling thread until the specified sync object is
	 * signaled, or until a specified timeout value expires.
	 *
	 * @param sync    the sync object
	 * @param flags   the block flags
	 * @param timeout the block timeout
	 *
	 * @return the sync object status
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static int eglClientWaitSyncNV(EGLSyncNV sync, int flags, long timeout) throws LWJGLException {
		final int status = neglClientWaitSyncNV(sync.getPointer(), flags, timeout);

		if ( status == EGL_FALSE )
			throwEGLError("Failed to block on NV fence sync object.");

		return status;
	}

	private static native int neglClientWaitSyncNV(long sync_ptr, int flags, long timeout);

	/**
	 * Signals or unsignals the sync object by changing its status to
	 * the specified mode.
	 *
	 * @param sync the sync object
	 * @param mode the mode
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static void eglSignalSyncNV(EGLSyncNV sync, int mode) throws LWJGLException {
		if ( !neglSignalSyncNV(sync.getPointer(), mode) )
			throwEGLError("Failed to signal the NV fence sync object.");
	}

	private static native boolean neglSignalSyncNV(long sync_ptr, int mode);

	/**
	 * Returns the value of the sync object attribute.
	 *
	 * @param sync      the sync object
	 * @param attribute the attribute to query
	 *
	 * @return the attribute value
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static int eglGetSyncAttribNV(EGLSyncNV sync, int attribute) throws LWJGLException {
		final IntBuffer value = APIUtil.getBufferInt();

		if ( !neglGetSyncAttribNV(sync.getPointer(), attribute, MemoryUtil.getAddress0(value)) )
			throwEGLError("Failed to get NV fence sync object attribute.");

		return value.get(0);
	}

	private static native boolean neglGetSyncAttribNV(long sync_ptr, int attribute, long value);

}