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

import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;

/**
 *
 * @author Tungsten
 * @version $Revision$
 * $Id$
 */
final class FCLDisplayPeerInfo extends FCLPeerInfo {

	final boolean gles;

	FCLDisplayPeerInfo() throws LWJGLException {
		gles = true;
		org.lwjgl.opengles.GLContext.loadOpenGLLibrary();
	}

	FCLDisplayPeerInfo(PixelFormat pixel_format) throws LWJGLException {
		gles = false;
		GLContext.loadOpenGLLibrary();
		try {
			FCLDisplay.incDisplay();
			try {
				initDefaultPeerInfo(FCLDisplay.getDisplay(), getHandle(), pixel_format);
			} catch (LWJGLException e) {
				FCLDisplay.decDisplay();
				throw e;
			}
		} catch (LWJGLException e) {
			GLContext.unloadOpenGLLibrary();
			throw e;
		}
	}
	private static native void initDefaultPeerInfo(long display, ByteBuffer peer_info_handle, PixelFormat pixel_format) throws LWJGLException;

	protected void doLockAndInitHandle() throws LWJGLException {
		initDrawable(getHandle());
	}
	private static native void initDrawable(ByteBuffer peer_info_handle);

	protected void doUnlock() throws LWJGLException {
		// NO-OP
	}

	public void destroy() {
		super.destroy();

		if ( gles )
			org.lwjgl.opengles.GLContext.unloadOpenGLLibrary();
		else {
			FCLDisplay.decDisplay();
			GLContext.unloadOpenGLLibrary();
		}
	}
}
