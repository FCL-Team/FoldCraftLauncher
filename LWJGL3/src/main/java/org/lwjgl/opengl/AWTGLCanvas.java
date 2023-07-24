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

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Sys;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * <p/>
 * An AWT rendering context.
 * <p/>
 *
 * @author $Author$
 *         $Id$
 * @version $Revision$
 */
public class AWTGLCanvas extends Canvas implements DrawableLWJGL, ComponentListener, HierarchyListener {

	private static final long serialVersionUID = 1L;

	private static final AWTCanvasImplementation implementation;
	private              boolean                 update_context;
	private Object SYNC_LOCK = new Object();

	/** The requested pixel format */
	private final PixelFormat pixel_format;

	/** The drawable to share context with */
	private final Drawable drawable;

	/** The ContextAttribs to use when creating the context */
	private final ContextAttribs attribs;

	/** Context handle */
	private PeerInfo  peer_info;
	private ContextGL context;

	/**
	 * re-entry counter for support for re-entrant
	 * redrawing in paint(). It happens when using dialog boxes.
	 */
	private int reentry_count;

	/** Tracks whether initGL() needs to be called */
	private boolean first_run;

	static {
		Sys.initialize();
		implementation = createImplementation();
	}

	static AWTCanvasImplementation createImplementation() {
		switch ( LWJGLUtil.getPlatform() ) {

			default:
				throw new IllegalStateException("Unsupported platform");
		}
	}

	private void setUpdate() {
		synchronized ( SYNC_LOCK ) {
			update_context = true;
		}
	}

	public void setPixelFormat(final PixelFormatLWJGL pf) throws LWJGLException {
		throw new UnsupportedOperationException();
	}

	public void setPixelFormat(final PixelFormatLWJGL pf, final ContextAttribs attribs) throws LWJGLException {
		throw new UnsupportedOperationException();
	}

	public PixelFormatLWJGL getPixelFormat() {
		return pixel_format;
	}

	/** This method should only be called internally. */
	public ContextGL getContext() {
		return context;
	}

	/** This method should only be called internally. */
	public ContextGL createSharedContext() throws LWJGLException {
		synchronized ( SYNC_LOCK ) {
			if ( context == null ) throw new IllegalStateException("Canvas not yet displayable");

			return new ContextGL(peer_info, context.getContextAttribs(), context);
		}
	}

	public void checkGLError() {
		Util.checkGLError();
	}

	public void initContext(final float r, final float g, final float b) {
		// set background clear color
		glClearColor(r, g, b, 0.0f);
		// Clear window to avoid the desktop "showing through"
		glClear(GL_COLOR_BUFFER_BIT);
	}

	/** Constructor using the default PixelFormat. */
	public AWTGLCanvas() throws LWJGLException {
		this(new PixelFormat());
	}

	/**
	 * Create an AWTGLCanvas with the requested PixelFormat on the default GraphicsDevice.
	 *
	 * @param pixel_format The desired pixel format. May not be null
	 */
	public AWTGLCanvas(PixelFormat pixel_format) throws LWJGLException {
		this(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(), pixel_format);
	}

	/**
	 * Create an AWTGLCanvas with the requested PixelFormat on the default GraphicsDevice.
	 *
	 * @param device       the device to create the canvas on.
	 * @param pixel_format The desired pixel format. May not be null
	 */
	public AWTGLCanvas(GraphicsDevice device, PixelFormat pixel_format) throws LWJGLException {
		this(device, pixel_format, null);
	}

	/**
	 * Create an AWTGLCanvas with the requested PixelFormat on the specified GraphicsDevice.
	 *
	 * @param device       the device to create the canvas on.
	 * @param pixel_format The desired pixel format. May not be null
	 * @param drawable     The Drawable to share context with
	 */
	public AWTGLCanvas(GraphicsDevice device, PixelFormat pixel_format, Drawable drawable) throws LWJGLException {
		this(device, pixel_format, drawable, null);
	}

	/**
	 * Create an AWTGLCanvas with the requested PixelFormat on the specified GraphicsDevice.
	 *
	 * @param device       the device to create the canvas on.
	 * @param pixel_format The desired pixel format. May not be null
	 * @param drawable     The Drawable to share context with
	 * @param attribs      The ContextAttribs to use when creating the context. (optional, may be null)
	 */
	public AWTGLCanvas(GraphicsDevice device, PixelFormat pixel_format, Drawable drawable, ContextAttribs attribs) throws LWJGLException {
		super(implementation.findConfiguration(device, pixel_format));
		if ( pixel_format == null )
			throw new NullPointerException("Pixel format must be non-null");
		addHierarchyListener(this);
		addComponentListener(this);
		this.drawable = drawable;
		this.pixel_format = pixel_format;
		this.attribs = attribs;
	}

	/* (non-Javadoc)
		 * @see java.awt.Canvas#addNotify()
		 */
	public void addNotify() {
		super.addNotify();
	}

	/* (non-Javadoc)
		 * @see java.awt.Component#removeNotify()
		 */
	public void removeNotify() {
		synchronized ( SYNC_LOCK ) {
			destroy();
			super.removeNotify();
		}
	}

	/** Set swap interval. */
	public void setSwapInterval(int swap_interval) {
		synchronized ( SYNC_LOCK ) {
			if ( context == null )
				throw new IllegalStateException("Canvas not yet displayable");
			ContextGL.setSwapInterval(swap_interval);
		}
	}

	/** Enable vsync */
	public void setVSyncEnabled(boolean enabled) {
		setSwapInterval(enabled ? 1 : 0);
	}

	/** Swap the canvas' buffer */
	public void swapBuffers() throws LWJGLException {
		synchronized ( SYNC_LOCK ) {
			if ( context == null )
				throw new IllegalStateException("Canvas not yet displayable");
			ContextGL.swapBuffers();
		}
	}

	public boolean isCurrent() throws LWJGLException {
		synchronized ( SYNC_LOCK ) {
			if ( context == null ) throw new IllegalStateException("Canvas not yet displayable");

			return context.isCurrent();
		}
	}

	/**
	 * Make the canvas' context current. It is highly recommended that the context
	 * is only made current inside the AWT thread (for example in an overridden paintGL()).
	 */
	public void makeCurrent() throws LWJGLException {
		synchronized ( SYNC_LOCK ) {
			if ( context == null )
				throw new IllegalStateException("Canvas not yet displayable");
			context.makeCurrent();
		}
	}

	public void releaseContext() throws LWJGLException {
		synchronized ( SYNC_LOCK ) {
			if ( context == null )
				throw new IllegalStateException("Canvas not yet displayable");
			if ( context.isCurrent() )
				context.releaseCurrent();
		}
	}

	/** Destroy the OpenGL context. This happens when the component becomes undisplayable */
	public final void destroy() {
		synchronized ( SYNC_LOCK ) {
			try {
				if ( context != null ) {
					context.forceDestroy();
					context = null;
					reentry_count = 0;
					peer_info.destroy();
					peer_info = null;
				}
			} catch (LWJGLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public final void setCLSharingProperties(final PointerBuffer properties) throws LWJGLException {
		synchronized ( SYNC_LOCK ) {
			if ( context == null )
				throw new IllegalStateException("Canvas not yet displayable");
			context.setCLSharingProperties(properties);
		}
	}

	/**
	 * Override this to do initialising of the context.
	 * It will be called once from paint(), immediately after
	 * the context is created and made current.
	 */
	protected void initGL() {
	}

	/** Override this to do painting */
	protected void paintGL() {
	}

	/**
	 * The default paint() operation makes the context current and calls paintGL() which should
	 * be overridden to do GL operations.
	 */
	public final void paint(Graphics g) {
		LWJGLException exception = null;
		synchronized ( SYNC_LOCK ) {
			if ( !isDisplayable() )
				return;
			try {
				if ( peer_info == null ) {
					this.peer_info = implementation.createPeerInfo(this, pixel_format, attribs);
				}
				peer_info.lockAndGetHandle();
				try {
					if ( context == null ) {
						this.context = new ContextGL(peer_info, attribs, drawable != null ? (ContextGL)((DrawableLWJGL)drawable).getContext() : null);
						first_run = true;
					}

					if ( reentry_count == 0 )
						context.makeCurrent();
					reentry_count++;
					try {
						if ( update_context ) {
							context.update();
							update_context = false;
						}
						if ( first_run ) {
							first_run = false;
							initGL();
						}
						paintGL();
					} finally {
						reentry_count--;
						if ( reentry_count == 0 )
							context.releaseCurrent();
					}
				} finally {
					peer_info.unlock();
				}
			} catch (LWJGLException e) {
				exception = e;
			}
		}
		if ( exception != null )
			exceptionOccurred(exception);
	}

	/**
	 * This method will be called if an unhandled LWJGLException occurs in paint().
	 * Override this method to be notified of this.
	 *
	 * @param exception The exception that occurred.
	 */
	protected void exceptionOccurred(LWJGLException exception) {
		LWJGLUtil.log("Unhandled exception occurred, skipping paint(): " + exception);
	}

	/** override update to avoid clearing */
	public void update(Graphics g) {
		paint(g);
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		setUpdate();
	}

	public void componentMoved(ComponentEvent e) {
		setUpdate();
	}

	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		setUpdate();
	}

	public void setLocation(Point p) {
		super.setLocation(p);
		setUpdate();
	}

	public void setSize(Dimension d) {
		super.setSize(d);
		setUpdate();
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);
		setUpdate();
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		setUpdate();
	}

	public void hierarchyChanged(HierarchyEvent e) {
		setUpdate();
	}

}