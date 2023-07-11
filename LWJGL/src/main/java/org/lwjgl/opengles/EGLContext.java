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
import org.lwjgl.PointerWrapperAbstract;

import java.nio.IntBuffer;

import static org.lwjgl.opengles.EGL.*;

/** EGLContext wrapper class. */
public final class EGLContext extends PointerWrapperAbstract {

	private       EGLDisplay display;
	private final EGLConfig  config;

	private boolean destroyed;

	EGLContext(final EGLDisplay display, final EGLConfig config, final long pointer) {
		super(pointer);

		if ( !display.isInitialized() )
			throw new IllegalStateException("Invalid EGL display specified.");

		if ( config.getDisplay() != display )
			throw new IllegalStateException("Invalid EGL config specified.");

		this.display = display;
		this.config = config;
	}

	public void setDisplay(EGLDisplay display) {
		this.display = display;
	}

	EGLDisplay getDisplay() {
		return display;
	}

	EGLConfig getConfig() {
		return config;
	}

	private void checkDestroyed() {
		if ( destroyed )
			throw new IllegalStateException("The EGL surface has been destroyed.");
	}

	public void destroy() throws LWJGLException {
		eglDestroyContext(display, this);
		destroyed = true;
	}

	/**
	 * Returns the value of the specified EGL context attribute.
	 *
	 * @param attribute the context attribute
	 *
	 * @return the attribute value
	 */
	int getAttribute(final int attribute) throws LWJGLException {
			checkDestroyed();

			IntBuffer value = APIUtil.getBufferInt();
			eglQueryContext(display, this, attribute, value);
			return value.get(0);
	}

	public void makeCurrent(final EGLSurface surface) throws LWJGLException, PowerManagementEventException {
		makeCurrent(surface, surface);
	}

	public void makeCurrent(final EGLSurface draw, final EGLSurface read) throws LWJGLException, PowerManagementEventException {
		eglMakeCurrent(display, draw, read, this);
	}

	public boolean equals(final Object obj) {
		if ( obj == null || !(obj instanceof EGLContext) )
			return false;

		return getPointer() == ((EGLContext)obj).getPointer();
	}

}