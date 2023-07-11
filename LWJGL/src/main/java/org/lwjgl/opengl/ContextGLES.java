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

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengles.EGLContext;
import org.lwjgl.opengles.GLContext;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengles.PowerManagementEventException;

import static org.lwjgl.opengles.EGL.*;

/**
 * <p/>
 * Context encapsulates an OpenGL ES context.
 * <p/>
 * <p/>
 * This class is thread-safe.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3332 $
 *          $Id: Context.java 3332 2010-04-20 18:21:05Z spasi $
 */
final class ContextGLES implements Context {

	/** The current Context */
	private static final ThreadLocal<ContextGLES> current_context_local = new ThreadLocal<ContextGLES>();

	/** Handle to the native GL rendering context */
	private final DrawableGLES drawable;
	private final EGLContext  eglContext;

	private final org.lwjgl.opengles.ContextAttribs contextAttribs;

	/** Whether the context has been destroyed */
	private boolean destroyed;

	private boolean destroy_requested;

	/** The thread that has this context current, or null. */
	private Thread thread;

	static {
		Sys.initialize();
	}

	public EGLContext getEGLContext() {
		return eglContext;
	}

	org.lwjgl.opengles.ContextAttribs getContextAttribs() {
		return contextAttribs;
	}

	static ContextGLES getCurrentContext() {
		return current_context_local.get();
	}

	/** Create a context with the specified peer info and shared context */
	ContextGLES(DrawableGLES drawable, org.lwjgl.opengles.ContextAttribs attribs, ContextGLES shared_context) throws LWJGLException {
		if ( drawable == null )
			throw new IllegalArgumentException();

		ContextGLES context_lock = shared_context != null ? shared_context : this;
		// If shared_context is not null, synchronize on it to make sure it is not deleted
		// while this context is created. Otherwise, simply synchronize on ourself to avoid NPE
		synchronized ( context_lock ) {
			if ( shared_context != null && shared_context.destroyed )
				throw new IllegalArgumentException("Shared context is destroyed");

			this.drawable = drawable;
			this.contextAttribs = attribs;
			this.eglContext = drawable.getEGLDisplay().createContext(drawable.getEGLConfig(),
			                                                         shared_context == null ? null : shared_context.eglContext,
			                                                         attribs == null ? new org.lwjgl.opengles.ContextAttribs(2).getAttribList() : attribs.getAttribList());
		}
	}

	/** Release the current context (if any). After this call, no context is current. */
	public void releaseCurrent() throws LWJGLException, PowerManagementEventException {
		eglReleaseCurrent(drawable.getEGLDisplay());
		GLContext.useContext(null);
		current_context_local.set(null);

		synchronized ( this ) {
			thread = null;
			checkDestroy();
		}
	}

	/** Swap the buffers on the current context. Only valid for double-buffered contexts */
	public static void swapBuffers() throws LWJGLException, PowerManagementEventException {
		ContextGLES current_context = getCurrentContext();
		if ( current_context != null )
			current_context.drawable.getEGLSurface().swapBuffers();
	}

	private boolean canAccess() {
		return thread == null || Thread.currentThread() == thread;
	}

	private void checkAccess() {
		if ( !canAccess() )
			throw new IllegalStateException("From thread " + Thread.currentThread() + ": " + thread + " already has the context current");
	}

	/** Make the context current */
	public synchronized void makeCurrent() throws LWJGLException, PowerManagementEventException {
		checkAccess();
		if ( destroyed )
			throw new IllegalStateException("Context is destroyed");
		thread = Thread.currentThread();
		current_context_local.set(this);
		eglContext.makeCurrent(drawable.getEGLSurface());
		GLContext.useContext(this);
	}

	/** Query whether the context is current */
	public synchronized boolean isCurrent() throws LWJGLException {
		if ( destroyed )
			throw new IllegalStateException("Context is destroyed");
		return eglIsCurrentContext(eglContext);
	}

	private void checkDestroy() {
		if ( !destroyed && destroy_requested ) {
			try {
				eglContext.destroy();
				destroyed = true;
				thread = null;
			} catch (LWJGLException e) {
				LWJGLUtil.log("Exception occurred while destroying context: " + e);
			}
		}
	}

	/**
	 * Set the buffer swap interval. This call is a best-attempt at changing
	 * the monitor swap interval, which is the minimum periodicity of color buffer swaps,
	 * measured in video frame periods, and is not guaranteed to be successful.
	 * <p/>
	 * A video frame period is the time required to display a full frame of video data.
	 */
	public static void setSwapInterval(int value) {
		ContextGLES current_context = getCurrentContext();
		if ( current_context != null ) {
			try {
				current_context.drawable.getEGLDisplay().setSwapInterval(value);
			} catch (LWJGLException e) {
				LWJGLUtil.log("Failed to set swap interval. Reason: " + e.getMessage());
			}
		}
	}

	/**
	 * Destroy the context. This method behaves the same as destroy() with the extra
	 * requirement that the context must be either current to the current thread or not
	 * current at all.
	 */
	public synchronized void forceDestroy() throws LWJGLException {
		checkAccess();
		destroy();
	}

	/**
	 * Request destruction of the Context. If the context is current, no context will be current after this call.
	 * The context is destroyed when no thread has it current.
	 */
	public synchronized void destroy() throws LWJGLException {
		if ( destroyed )
			return;
		destroy_requested = true;
		boolean was_current = isCurrent();
		int error = GLES20.GL_NO_ERROR;
		if ( was_current ) {
			if ( GLContext.getCapabilities() != null && GLContext.getCapabilities().OpenGLES20 )
				error = GLES20.glGetError();

			try {
				releaseCurrent();
			} catch (PowerManagementEventException e) {
				// Ignore
			}
		}
		checkDestroy();
		if ( was_current && error != GLES20.GL_NO_ERROR )
			throw new OpenGLException(error);
	}

	public void releaseDrawable() throws LWJGLException {
	}

}
