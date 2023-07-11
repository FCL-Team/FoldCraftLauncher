/*
 * Copyright (c) 2002-2008 LWJGL Project
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
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.Component;
import java.applet.Applet;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

/**
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$
 * $Id$
 */
final class AWTSurfaceLock {

	private static final int	WAIT_DELAY_MILLIS	= 100;

	private final ByteBuffer	lock_buffer;

	private boolean				firstLockSucceeded;

	AWTSurfaceLock() {
		lock_buffer = createHandle();
	}

	private static native ByteBuffer createHandle();

	public ByteBuffer lockAndGetHandle(Canvas component) throws LWJGLException {
		while (!privilegedLockAndInitHandle(component)) {
			LWJGLUtil.log("Could not get drawing surface info, retrying...");
			try {
				Thread.sleep(WAIT_DELAY_MILLIS);
			} catch (InterruptedException e) {
				LWJGLUtil.log("Interrupted while retrying: " + e);
			}
		}

		return lock_buffer;
	}

	private boolean privilegedLockAndInitHandle(final Canvas component) throws LWJGLException {
		// Workaround for Sun JDK bug 4796548 which still exists in java for OS X
		// We need to elevate privileges because of an AWT bug. Please see
		// http://192.18.37.44/forums/index.php?topic=10572 for a discussion.
		// It is only needed on first call, so we avoid it on all subsequent calls
		// due to performance..
        
		if (firstLockSucceeded)
			return lockAndInitHandle(lock_buffer, component);
		else
			try {
				firstLockSucceeded = AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() {
					public Boolean run() throws LWJGLException {
						return lockAndInitHandle(lock_buffer, component);
					}
				});
				return firstLockSucceeded;
			} catch (PrivilegedActionException e) {
				throw (LWJGLException) e.getException();
			}
	}

	private static native boolean lockAndInitHandle(ByteBuffer lock_buffer, Canvas component) throws LWJGLException;

	void unlock() throws LWJGLException {
		nUnlock(lock_buffer);
	}

	private static native void nUnlock(ByteBuffer lock_buffer) throws LWJGLException;
}
