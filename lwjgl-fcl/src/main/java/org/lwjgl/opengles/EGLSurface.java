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

/** EGLSurface wrapper class. */
public final class EGLSurface extends PointerWrapperAbstract {

	private final EGLDisplay display;
	private final EGLConfig  config;

	private boolean destroyed;

	EGLSurface(final EGLDisplay display, final EGLConfig config, final long pointer) {
		super(pointer);

		if ( !display.isInitialized() )
			throw new IllegalStateException("Invalid EGL display specified.");

		this.display = display;
		this.config = config;
	}

	/**
	 * Returns the EGL display from which this EGL surface was created.
	 *
	 * @return the EGL display
	 */
	public EGLDisplay getDisplay() {
		return display;
	}

	/**
	 * Returns the EGL config associated with this EGL surface.
	 *
	 * @return the EGL config
	 */
	public EGLConfig getConfig() {
		return config;
	}

	private void checkDestroyed() {
		if ( destroyed )
			throw new IllegalStateException("The EGL surface has been destroyed.");
	}

	/** Destroys this EGL surface. */
	public void destroy() throws LWJGLException {
		eglDestroySurface(display, this);
		destroyed = true;
	}

	void setAttribute(int attribute, int value) throws LWJGLException {
		checkDestroyed();
		eglSurfaceAttrib(display, this, attribute, value);
	}

	/**
	 * Returns the value of the specified EGL surface attribute.
	 *
	 * @param attribute the surface attribute
	 *
	 * @return the attribute value
	 */
	public int getAttribute(int attribute) throws LWJGLException {
		checkDestroyed();

		IntBuffer value = APIUtil.getBufferInt();
		eglQuerySurface(display, this, attribute, value);
		return value.get(0);
	}

	public void swapBuffers() throws LWJGLException, PowerManagementEventException {
		checkDestroyed();
		eglSwapBuffers(display, this);
	}

	public boolean equals(final Object obj) {
		if ( obj == null || !(obj instanceof EGLSurface) )
			return false;

		return getPointer() == ((EGLSurface)obj).getPointer();
	}

}