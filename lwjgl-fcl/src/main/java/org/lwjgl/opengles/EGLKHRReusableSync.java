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

import java.nio.IntBuffer;

import static org.lwjgl.opengles.EGL.*;

/** EGL_KHR_reusable_sync wrapper class. */
public final class EGLKHRReusableSync {

	/**
	 * Accepted by the &lt;type&gt; parameter of eglCreateSyncKHR, and returned
	 * in &lt;value&gt; when eglGetSyncAttribKHR is called with &lt;attribute&gt;
	 * EGL_SYNC_TYPE_KHR:
	 */
	public static final int EGL_SYNC_REUSABLE_KHR = 0x30FA;

	/** Accepted by the &lt;attribute&gt; parameter of eglGetSyncAttribKHR: */
	public static final int EGL_SYNC_TYPE_KHR = 0x30F7,
		EGL_SYNC_STATUS_KHR                   = 0x30F1;

	/**
	 * Returned in &lt;value&gt; when eglGetSyncAttribKHR is called with
	 * &lt;attribute&gt; EGL_SYNC_STATUS_KHR:
	 */
	public static final int EGL_SIGNALED_KHR = 0x30F2,
		EGL_UNSIGNALED_KHR                   = 0x30F3;

	/** Accepted in the &lt;flags&gt; parameter of eglClientWaitSyncKHR: */
	public static final int EGL_SYNC_FLUSH_COMMANDS_BIT_KHR = 0x0001;

	/** Accepted in the &lt;timeout&gt; parameter of eglClientWaitSyncKHR: */
	public static final long EGL_FOREVER_KHR = 0xFFFFFFFFFFFFFFFFl;

	/** Returned by eglClientWaitSyncKHR: */
	public static final int EGL_TIMEOUT_EXPIRED_KHR = 0x30F5,
		EGL_CONDITION_SATISFIED_KHR                 = 0x30F6;

	/** Returned by eglCreateSyncKHR in the event of an error: */
	public static final long EGL_NO_SYNC_KHR = 0;

	static {
		initNativeStubs();
	}

	private EGLKHRReusableSync() {
	}

	private static native void initNativeStubs();

	/**
	 * Creates a fence sync object for the specified EGL display and returns
	 * a handle to the new object.
	 *
	 * @param dpy         the EGL display
	 * @param type        the sync type
	 * @param attrib_list an attribute list (may be null)
	 *
	 * @return the created fence sync object
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static EGLSyncKHR eglCreateSyncKHR(EGLDisplay dpy, int type, IntBuffer attrib_list) throws LWJGLException {
		return EGLKHRFenceSync.eglCreateSyncKHR(dpy, type, attrib_list);
	}

	/**
	 * Destroys an existing sync object.
	 *
	 * @param sync the sync object
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static void eglDestroySyncKHR(EGLDisplay dpy, EGLSyncKHR sync) throws LWJGLException {
		EGLKHRFenceSync.eglDestroySyncKHR(dpy, sync);
	}

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
	public static int eglClientWaitSyncKHR(EGLDisplay dpy, EGLSyncKHR sync, int flags, long timeout) throws LWJGLException {
		return EGLKHRFenceSync.eglClientWaitSyncKHR(dpy, sync, flags, timeout);
	}

	/**
	 * Signals or unsignals the sync object by changing its status to
	 * the specified mode.
	 *
	 * @param sync the sync object
	 * @param mode the mode
	 *
	 * @throws LWJGLException if an EGL error occurs.
	 */
	public static void eglSignalSyncKHR(EGLDisplay dpy, EGLSyncKHR sync, int mode) throws LWJGLException {
		if ( !neglSignalSyncKHR(dpy.getPointer(), sync.getPointer(), mode) )
			throwEGLError("Failed to signal the KHR fence sync object.");
	}

	private static native boolean neglSignalSyncKHR(long dpy_ptr, long sync_ptr, int mode);

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
	public static int eglGetSyncAttribKHR(EGLDisplay dpy, EGLSyncKHR sync, int attribute) throws LWJGLException {
		return EGLKHRFenceSync.eglGetSyncAttribKHR(dpy, sync, attribute);
	}

}