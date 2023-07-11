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
package org.lwjgl.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengles.ContextAttribs;
import org.lwjgl.opengles.*;
import org.lwjgl.opengles.Util;

import static org.lwjgl.opengles.EGL.*;
import static org.lwjgl.opengles.GLES20.*;

/**
 * @author Spasi
 * @since 14/5/2011
 */
abstract class DrawableGLES implements DrawableLWJGL {

	/** The PixelFormat used to create the EGLDisplay. */
	protected org.lwjgl.opengles.PixelFormat pixel_format;

	protected EGLDisplay eglDisplay;
	protected EGLConfig  eglConfig;
	protected EGLSurface eglSurface;

	/** The OpenGL Context. */
	protected ContextGLES context;

	/** The Drawable that shares objects with this Drawable. */
	protected Drawable shared_drawable;

	protected DrawableGLES() {
	}

	public void setPixelFormat(final PixelFormatLWJGL pf) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			this.pixel_format = (org.lwjgl.opengles.PixelFormat)pf;
		}
	}

	public PixelFormatLWJGL getPixelFormat() {
		synchronized ( GlobalLock.lock ) {
			return pixel_format;
		}
	}

	public void initialize(final long window, final long display_id, final int eglSurfaceType, final org.lwjgl.opengles.PixelFormat pf) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			if ( eglSurface != null ) {
				eglSurface.destroy();
				eglSurface = null;
			}

			if ( eglDisplay != null ) {
				eglDisplay.terminate();
				eglDisplay = null;
			}

			final EGLDisplay eglDisplay = eglGetDisplay((int)display_id);

			int[] attribs = {
				EGL_LEVEL, 0,
				EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
				EGL_NATIVE_RENDERABLE, EGL_FALSE,
			};

			final EGLConfig[] configs = eglDisplay.chooseConfig(pf.getAttribBuffer(eglDisplay, eglSurfaceType, attribs), null, BufferUtils.createIntBuffer(1));
			if ( configs.length == 0 )
				throw new LWJGLException("No EGLConfigs found for the specified PixelFormat.");

			final EGLConfig eglConfig = pf.getBestMatch(configs);
			final EGLSurface eglSurface = eglDisplay.createWindowSurface(eglConfig, window, null);
			pf.setSurfaceAttribs(eglSurface);

			this.eglDisplay = eglDisplay;
			this.eglConfig = eglConfig;
			this.eglSurface = eglSurface;

			// This can happen when switching in and out of full-screen mode.
			if ( context != null )
				context.getEGLContext().setDisplay(eglDisplay);
		}
	}

	public void createContext(final ContextAttribs attribs, final Drawable shared_drawable) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			this.context = new ContextGLES(this, attribs, shared_drawable != null ? ((DrawableGLES)shared_drawable).getContext() : null);
			this.shared_drawable = shared_drawable;
		}
	}

	Drawable getSharedDrawable() {
		synchronized ( GlobalLock.lock ) {
			return shared_drawable;
		}
	}

	public EGLDisplay getEGLDisplay() {
		synchronized ( GlobalLock.lock ) {
			return eglDisplay;
		}
	}

	public EGLConfig getEGLConfig() {
		synchronized ( GlobalLock.lock ) {
			return eglConfig;
		}
	}

	public EGLSurface getEGLSurface() {
		synchronized ( GlobalLock.lock ) {
			return eglSurface;
		}
	}

	public ContextGLES getContext() {
		synchronized ( GlobalLock.lock ) {
			return context;
		}
	}

	public Context createSharedContext() throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			checkDestroyed();
			return new ContextGLES(this, context.getContextAttribs(), context);
		}
	}

	public void checkGLError() {
		Util.checkGLError();
	}

	public void setSwapInterval(final int swap_interval) {
		ContextGLES.setSwapInterval(swap_interval);
	}

	public void swapBuffers() throws LWJGLException {
		ContextGLES.swapBuffers();
	}

	public void initContext(final float r, final float g, final float b) {
		// set background clear color
		glClearColor(r, g, b, 0.0f);
		// Clear window to avoid the desktop "showing through"
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public boolean isCurrent() throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			checkDestroyed();
			return context.isCurrent();
		}
	}

	public void makeCurrent() throws LWJGLException, PowerManagementEventException {
		synchronized ( GlobalLock.lock ) {
			checkDestroyed();
			context.makeCurrent();
		}
	}

	public void releaseContext() throws LWJGLException, PowerManagementEventException {
		synchronized ( GlobalLock.lock ) {
			checkDestroyed();
			if ( context.isCurrent() )
				context.releaseCurrent();
		}
	}

	public void destroy() {
		synchronized ( GlobalLock.lock ) {
			try {
				if ( context != null ) {
					try {
						releaseContext();
					} catch (PowerManagementEventException e) {
						// Ignore
					}

					context.forceDestroy();
					context = null;
				}

				if ( eglSurface != null ) {
					eglSurface.destroy();
					eglSurface = null;
				}

				if ( eglDisplay != null ) {
					eglDisplay.terminate();
					eglDisplay = null;
				}

				pixel_format = null;
				shared_drawable = null;
			} catch (LWJGLException e) {
				LWJGLUtil.log("Exception occurred while destroying Drawable: " + e);
			}
		}
	}

	protected void checkDestroyed() {
		if ( context == null )
			throw new IllegalStateException("The Drawable has no context available.");
	}

	public void setCLSharingProperties(final PointerBuffer properties) throws LWJGLException {
		throw new UnsupportedOperationException();
	}

}