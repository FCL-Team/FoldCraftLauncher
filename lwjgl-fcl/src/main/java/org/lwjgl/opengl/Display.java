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

/**
 * This is the abstract class for a Display in LWJGL. LWJGL displays have some
 * peculiar characteristics:
 *
 * - the display may be closeable by the user or operating system, and may be minimized
 * by the user or operating system
 * - only one display may ever be open at once
 * - the operating system may or may not be able to do fullscreen or windowed displays.
 *
 * @author foo
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;

public final class Display {

	private static final Thread shutdown_hook = new Thread() {
		public void run() {
			reset();
		}
	};

	/** The display implementor */
	private static final DisplayImplementation display_impl;

	/** The initial display mode */
	private static final DisplayMode initial_mode;

	/** The parent, if any */
	private static Canvas parent;

	/** The current display mode, if created */
	private static DisplayMode current_mode;

	/** X coordinate of the window */
	private static int x = -1;

	/** Cached window icons, for when Display is recreated */
	private static ByteBuffer[] cached_icons;

	/**
	 * Y coordinate of the window. Y in window coordinates is from the top of the display down,
	 * unlike GL, where it is typically at the bottom of the display.
	 */
	private static int y = -1;

	/** the width of the Display window */
	private static int width = 0;

	/** the height of the Display window */
	private static int height = 0;

	/** Title of the window (never null) */
	private static String title = "Game";

	/** Fullscreen */
	private static boolean fullscreen;

	/** Swap interval */
	private static int swap_interval;

	/** The Drawable instance that tracks the current Display context */
	private static DrawableLWJGL drawable;

	private static boolean window_created;

	private static boolean parent_resized;

	private static boolean window_resized;

	private static boolean window_resizable;

	/** Initial Background Color of Display */
	private static float r, g, b;

	private static final ComponentListener component_listener = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			synchronized ( GlobalLock.lock ) {
				parent_resized = true;
			}
		}
	};

	static {
		Sys.initialize();
		display_impl = createDisplayImplementation();
		try {
			current_mode = initial_mode = display_impl.init();
			LWJGLUtil.log("Initial mode: " + initial_mode);
		} catch (LWJGLException e) {
			LWJGLUtil.log(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * Fetch the Drawable from the Display.
	 *
	 * @return the Drawable corresponding to the Display context
	 */
	public static Drawable getDrawable() {
		return drawable;
	}

	private static DisplayImplementation createDisplayImplementation() {
		switch ( LWJGLUtil.getPlatform() ) {
			case LWJGLUtil.PLATFORM_FCL:
				return new FCLDisplay();
			default:
				throw new IllegalStateException("Unsupported platform");
		}
	}

	/** Only constructed by ourselves */
	private Display() {
	}

	/**
	 * Returns the entire list of possible fullscreen display modes as an array, in no
	 * particular order. Although best attempts to filter out invalid modes are done, any
	 * given mode is not guaranteed to be available nor is it guaranteed to be within the
	 * current monitor specs (this is especially a problem with the frequency parameter).
	 * Furthermore, it is not guaranteed that create() will detect an illegal display mode.
	 * <p/>
	 * The only certain way to check
	 * is to call create() and make sure it works.
	 * Only non-palette-indexed modes are returned (ie. bpp will be 16, 24, or 32).
	 * Only DisplayModes from this call can be used when the Display is in fullscreen
	 * mode.
	 *
	 * @return an array of all display modes the system reckons it can handle.
	 */
	public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			DisplayMode[] unfilteredModes = display_impl.getAvailableDisplayModes();

			if ( unfilteredModes == null ) {
				return new DisplayMode[0];
			}

			// We'll use a HashSet to filter out the duplicated modes
			HashSet<DisplayMode> modes = new HashSet<DisplayMode>(unfilteredModes.length);

			modes.addAll(Arrays.asList(unfilteredModes));
			DisplayMode[] filteredModes = new DisplayMode[modes.size()];
			modes.toArray(filteredModes);

			LWJGLUtil.log("Removed " + (unfilteredModes.length - filteredModes.length) + " duplicate displaymodes");

			return filteredModes;
		}
	}

	/**
	 * Return the initial desktop display mode.
	 *
	 * @return The desktop display mode
	 */
	public static DisplayMode getDesktopDisplayMode() {
		return initial_mode;
	}

	/**
	 * Return the current display mode, as set by setDisplayMode().
	 *
	 * @return The current display mode
	 */
	public static DisplayMode getDisplayMode() {
		return current_mode;
	}

	/**
	 * Set the current display mode. If no OpenGL context has been created, the given mode will apply to
	 * the context when create() is called, and no immediate mode switching will happen. If there is a
	 * context already, it will be resized according to the given mode. If the context is also a
	 * fullscreen context, the mode will also be switched immediately. The native cursor position
	 * is also reset.
	 *
	 * @param mode The new display mode to set
	 *
	 * @throws LWJGLException if the display mode could not be set
	 */
	public static void setDisplayMode(DisplayMode mode) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			if ( mode == null )
				throw new NullPointerException("mode must be non-null");
			boolean was_fullscreen = isFullscreen();
			current_mode = mode;
			if ( isCreated() ) {
				destroyWindow();
				// If mode is not fullscreen capable, make sure we are in windowed mode
				try {
					if ( was_fullscreen && !isFullscreen() )
						display_impl.resetDisplayMode();
                    else if ( isFullscreen() )
						switchDisplayMode();
					createWindow();
					makeCurrentAndSetSwapInterval();
				} catch (LWJGLException e) {
					drawable.destroy();
					display_impl.resetDisplayMode();
					throw e;
				}
			}
		}
	}

	private static DisplayMode getEffectiveMode() {
		return !isFullscreen() && parent != null ? new DisplayMode(parent.getWidth(), parent.getHeight()) : current_mode;
	}

	private static int getWindowX() {
		if ( !isFullscreen() && parent == null ) {
			// if no display location set, center window
			if ( x == -1 ) {
				return Math.max(0, (initial_mode.getWidth() - current_mode.getWidth()) / 2);
			} else {
				return x;
			}
		} else {
			return 0;
		}
	}

	private static int getWindowY() {
		if ( !isFullscreen() && parent == null ) {
			// if no display location set, center window
			if ( y == -1 ) {
				return Math.max(0, (initial_mode.getHeight() - current_mode.getHeight()) / 2);
			} else {
				return y;
			}
		} else {
			return 0;
		}
	}

	/**
	 * Create the native window peer from the given mode and fullscreen flag.
	 * A native context must exist, and it will be attached to the window.
	 */
	private static void createWindow() throws LWJGLException {
		if ( window_created ) {
			return;
		}
		Canvas tmp_parent = isFullscreen() ? null : parent;
		if ( tmp_parent != null && !tmp_parent.isDisplayable() ) // Only a best effort check, since the parent can turn undisplayable hereafter
			throw new LWJGLException("Parent.isDisplayable() must be true");
		if ( tmp_parent != null ) {
			tmp_parent.addComponentListener(component_listener);
		}
		DisplayMode mode = getEffectiveMode();
		display_impl.createWindow(drawable, mode, tmp_parent, getWindowX(), getWindowY());
		window_created = true;

		width = Display.getDisplayMode().getWidth();
		height = Display.getDisplayMode().getHeight();

		setTitle(title);
		initControls();

		// set cached window icon if exists
		if ( cached_icons != null ) {
			setIcon(cached_icons);
		} else {
			setIcon(new ByteBuffer[] { LWJGLUtil.LWJGLIcon32x32, LWJGLUtil.LWJGLIcon16x16 });
		}
	}

	private static void releaseDrawable() {
		try {
			Context context = drawable.getContext();
			if ( context != null && context.isCurrent() ) {
				context.releaseCurrent();
				context.releaseDrawable();
			}
		} catch (LWJGLException e) {
			LWJGLUtil.log("Exception occurred while trying to release context: " + e);
		}
	}

	private static void destroyWindow() {
		if ( !window_created ) {
			return;
		}
		if ( parent != null ) {
			parent.removeComponentListener(component_listener);
		}
		releaseDrawable();

		// Automatically destroy keyboard & mouse
		if ( Mouse.isCreated() ) {
			Mouse.destroy();
		}
		if ( Keyboard.isCreated() ) {
			Keyboard.destroy();
		}
		display_impl.destroyWindow();
		window_created = false;
	}

	private static void switchDisplayMode() throws LWJGLException {
		if ( !current_mode.isFullscreenCapable() ) {
			throw new IllegalStateException("Only modes acquired from getAvailableDisplayModes() can be used for fullscreen display");
		}
		display_impl.switchDisplayMode(current_mode);
	}

	/**
	 * Set the display configuration to the specified gamma, brightness and contrast.
	 * The configuration changes will be reset when destroy() is called.
	 *
	 * @param gamma      The gamma value
	 * @param brightness The brightness value between -1.0 and 1.0, inclusive
	 * @param contrast   The contrast, larger than 0.0.
	 */
	public static void setDisplayConfiguration(float gamma, float brightness, float contrast) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			if ( !isCreated() ) {
				throw new LWJGLException("Display not yet created.");
			}
			if ( brightness < -1.0f || brightness > 1.0f )
				throw new IllegalArgumentException("Invalid brightness value");
			if ( contrast < 0.0f )
				throw new IllegalArgumentException("Invalid contrast value");
			int rampSize = display_impl.getGammaRampLength();
			if ( rampSize == 0 ) {
				throw new LWJGLException("Display configuration not supported");
			}
			FloatBuffer gammaRamp = BufferUtils.createFloatBuffer(rampSize);
			for ( int i = 0; i < rampSize; i++ ) {
				float intensity = (float)i / (rampSize - 1);
				// apply gamma
				float rampEntry = (float) Math.pow(intensity, gamma);
				// apply brightness
				rampEntry += brightness;
				// apply contrast
				rampEntry = (rampEntry - 0.5f) * contrast + 0.5f;
				// Clamp entry to [0, 1]
				if ( rampEntry > 1.0f )
					rampEntry = 1.0f;
				else if ( rampEntry < 0.0f )
					rampEntry = 0.0f;
				gammaRamp.put(i, rampEntry);
			}
			display_impl.setGammaRamp(gammaRamp);
			LWJGLUtil.log("Gamma set, gamma = " + gamma + ", brightness = " + brightness + ", contrast = " + contrast);
		}
	}

	/**
	 * An accurate sync method that will attempt to run at a constant frame rate.
	 * It should be called once every frame.
	 *
	 * @param fps - the desired frame rate, in frames per second
	 */
	public static void sync(int fps) {
		Sync.sync(fps);
	}

	/** @return the title of the window */
	public static String getTitle() {
		synchronized ( GlobalLock.lock ) {
			return title;
		}
	}

	/** Return the last parent set with setParent(). */
	public static Canvas getParent() {
		synchronized ( GlobalLock.lock ) {
			return parent;
		}
	}

	/**
	 * Set the parent of the Display. If parent is null, the Display will appear as a top level window.
	 * If parent is not null, the Display is made a child of the parent. A parent's isDisplayable() must be true when
	 * setParent() is called and remain true until setParent() is called again with
	 * null or a different parent. This generally means that the parent component must remain added to it's parent container.<p>
	 * It is not advisable to call this method from an AWT thread, since the context will be made current on the thread
	 * and it is difficult to predict which AWT thread will process any given AWT event.<p>
	 * While the Display is in fullscreen mode, the current parent will be ignored. Additionally, when a non null parent is specified,
	 * the Dispaly will inherit the size of the parent, disregarding the currently set display mode.<p>
	 */
	public static void setParent(Canvas parent) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			if ( Display.parent != parent ) {
				Display.parent = parent;
				int width = Integer.parseInt(System.getProperty("window.width") == null ? "-1" : System.getProperty("window.width"));
				int height = Integer.parseInt(System.getProperty("window.height") == null ? "-1" : System.getProperty("window.height"));
				if (width != -1 && height != -1) {
					Display.parent.setSize(width, height);
				}
				if ( !isCreated() )
					return;
				destroyWindow();
				try {
					if ( isFullscreen() ) {
						switchDisplayMode();
					} else {
						display_impl.resetDisplayMode();
					}
					createWindow();
					makeCurrentAndSetSwapInterval();
				} catch (LWJGLException e) {
					drawable.destroy();
					display_impl.resetDisplayMode();
					throw e;
				}
			}
		}
	}

	/**
	 * Set the fullscreen mode of the context. If no context has been created through create(),
	 * the mode will apply when create() is called. If fullscreen is true, the context will become
	 * a fullscreen context and the display mode is switched to the mode given by getDisplayMode(). If
	 * fullscreen is false, the context will become a windowed context with the dimensions given in the
	 * mode returned by getDisplayMode(). The native cursor position is also reset.
	 *
	 * @param fullscreen Specify the fullscreen mode of the context.
	 *
	 * @throws LWJGLException If fullscreen is true, and the current DisplayMode instance is not
	 *                        from getAvailableDisplayModes() or if the mode switch fails.
	 */
	public static void setFullscreen(boolean fullscreen) throws LWJGLException {
		setDisplayModeAndFullscreenInternal(fullscreen, current_mode);
	}

	/**
	 * Set the mode of the context. If no context has been created through create(),
	 * the mode will apply when create() is called. If mode.isFullscreenCapable() is true, the context will become
	 * a fullscreen context and the display mode is switched to the mode given by getDisplayMode(). If
	 * mode.isFullscreenCapable() is false, the context will become a windowed context with the dimensions given in the
	 * mode returned by getDisplayMode(). The native cursor position is also reset.
	 *
	 * @param mode The new display mode to set. Must be non-null.
	 *
	 * @throws LWJGLException If the mode switch fails.
	 */
	public static void setDisplayModeAndFullscreen(DisplayMode mode) throws LWJGLException {
		setDisplayModeAndFullscreenInternal(mode.isFullscreenCapable(), mode);
	}

	private static void setDisplayModeAndFullscreenInternal(boolean fullscreen, DisplayMode mode) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			if ( mode == null )
				throw new NullPointerException("mode must be non-null");
			DisplayMode old_mode = current_mode;
			current_mode = mode;
			boolean was_fullscreen = isFullscreen();
			Display.fullscreen = fullscreen;
			if ( was_fullscreen != isFullscreen() || !mode.equals(old_mode) ) {
				if ( !isCreated() )
					return;
				destroyWindow();
				try {
					if ( isFullscreen() ) {
						switchDisplayMode();
					} else {
						display_impl.resetDisplayMode();
					}
					createWindow();
					makeCurrentAndSetSwapInterval();
				} catch (LWJGLException e) {
					drawable.destroy();
					display_impl.resetDisplayMode();
					throw e;
				}
			}
		}
	}

	/** @return whether the Display is in fullscreen mode */
	public static boolean isFullscreen() {
		synchronized ( GlobalLock.lock ) {
			return fullscreen && current_mode.isFullscreenCapable();
		}
	}

	/**
	 * Set the title of the window. This may be ignored by the underlying OS.
	 *
	 * @param newTitle The new window title
	 */
	public static void setTitle(String newTitle) {
		synchronized ( GlobalLock.lock ) {
			if ( newTitle == null ) {
				newTitle = "";
			}
			title = newTitle;
			if ( isCreated() )
				display_impl.setTitle(title);
		}
	}

	/** @return true if the user or operating system has asked the window to close */
	public static boolean isCloseRequested() {
		synchronized ( GlobalLock.lock ) {
			if ( !isCreated() )
				throw new IllegalStateException("Cannot determine close requested state of uncreated window");
			return display_impl.isCloseRequested();
		}
	}

	/** @return true if the window is visible, false if not */
	public static boolean isVisible() {
		synchronized ( GlobalLock.lock ) {
			if ( !isCreated() )
				throw new IllegalStateException("Cannot determine minimized state of uncreated window");
			return display_impl.isVisible();
		}
	}

	/** @return true if window is active, that is, the foreground display of the operating system. */
	public static boolean isActive() {
		synchronized ( GlobalLock.lock ) {
			if ( !isCreated() )
				throw new IllegalStateException("Cannot determine focused state of uncreated window");
			return display_impl.isActive();
		}
	}

	/**
	 * Determine if the window's contents have been damaged by external events.
	 * If you are writing a straightforward game rendering loop and simply paint
	 * every frame regardless, you can ignore this flag altogether. If you are
	 * trying to be kind to other processes you can check this flag and only
	 * redraw when it returns true. The flag is cleared when update() or isDirty() is called.
	 *
	 * @return true if the window has been damaged by external changes
	 *         and needs to repaint itself
	 */
	public static boolean isDirty() {
		synchronized ( GlobalLock.lock ) {
			if ( !isCreated() )
				throw new IllegalStateException("Cannot determine dirty state of uncreated window");
			return display_impl.isDirty();
		}
	}

	/**
	 * Process operating system events. Call this to update the Display's state and to receive new
	 * input device events. This method is called from update(), so it is not necessary to call
	 * this method if update() is called periodically.
	 */
	public static void processMessages() {
		synchronized ( GlobalLock.lock ) {
			if ( !isCreated() )
				throw new IllegalStateException("Display not created");

			display_impl.update();
		}
		pollDevices();
	}

	/**
	 * Swap the display buffers. This method is called from update(), and should normally not be called by
	 * the application.
	 *
	 * @throws OpenGLException if an OpenGL error has occured since the last call to glGetError()
	 */
	public static void swapBuffers() throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			if ( !isCreated() )
				throw new IllegalStateException("Display not created");

			if ( LWJGLUtil.DEBUG )
				drawable.checkGLError();
			drawable.swapBuffers();
		}
	}

	/**
	 * Update the window. If the window is visible clears
	 * the dirty flag and calls swapBuffers() and finally
	 * polls the input devices.
	 */
	public static void update() {
		update(true);
	}

	/**
	 * Update the window. If the window is visible clears
	 * the dirty flag and calls swapBuffers() and finally
	 * polls the input devices if processMessages is true.
	 *
	 * @param processMessages Poll input devices if true
	 */
	public static void update(boolean processMessages) {
		synchronized ( GlobalLock.lock ) {
			if ( !isCreated() )
				throw new IllegalStateException("Display not created");

			// We paint only when the window is visible or dirty
			if ( display_impl.isVisible() || display_impl.isDirty() ) {
				try {
					swapBuffers();
				} catch (LWJGLException e) {
					throw new RuntimeException(e);
				}
			}

			window_resized = !isFullscreen() && parent == null && display_impl.wasResized();

			if ( window_resized ) {
				width = display_impl.getWidth();
				height = display_impl.getHeight();
			}

			if ( parent_resized ) {
				reshape();
				parent_resized = false;
				window_resized = true;
			}

			if ( processMessages )
				processMessages();
		}
	}

	static void pollDevices() {
		// Poll the input devices while we're here
		if ( Mouse.isCreated() ) {
			Mouse.poll();
			Mouse.updateCursor();
		}

		if ( Keyboard.isCreated() ) {
			Keyboard.poll();
		}

		if ( Controllers.isCreated() ) {
			Controllers.poll();
		}
	}

	/**
	 * Release the Display context.
	 *
	 * @throws LWJGLException If the context could not be released
	 */
	public static void releaseContext() throws LWJGLException {
		drawable.releaseContext();
	}

	/** Returns true if the Display's context is current in the current thread. */
	public static boolean isCurrent() throws LWJGLException {
		return drawable.isCurrent();
	}

	/**
	 * Make the Display the current rendering context for GL calls.
	 *
	 * @throws LWJGLException If the context could not be made current
	 */
	public static void makeCurrent() throws LWJGLException {
		drawable.makeCurrent();
	}

	private static void removeShutdownHook() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				Runtime.getRuntime().removeShutdownHook(shutdown_hook);
				return null;
			}
		});
	}

	private static void registerShutdownHook() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				Runtime.getRuntime().addShutdownHook(shutdown_hook);
				return null;
			}
		});
	}

	/**
	 * Create the OpenGL context. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @throws LWJGLException
	 */
	public static void create() throws LWJGLException {
		create(new PixelFormat());
	}

	/**
	 * Create the OpenGL context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format Describes the minimum specifications the context must fulfill.
	 *
	 * @throws LWJGLException
	 */
	public static void create(PixelFormat pixel_format) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			create(pixel_format, null, (ContextAttribs)null);
		}
	}

	/**
	 * Create the OpenGL context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format    Describes the minimum specifications the context must fulfill.
	 * @param shared_drawable The Drawable to share context with. (optional, may be null)
	 *
	 * @throws LWJGLException
	 */
	public static void create(PixelFormat pixel_format, Drawable shared_drawable) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			create(pixel_format, shared_drawable, (ContextAttribs)null);
		}
	}

	/**
	 * Create the OpenGL context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format Describes the minimum specifications the context must fulfill.
	 * @param attribs      The ContextAttribs to use when creating the context. (optional, may be null)
	 *
	 * @throws LWJGLException
	 */
	public static void create(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			create(pixel_format, null, attribs);
		}
	}

	/**
	 * Create the OpenGL context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format    Describes the minimum specifications the context must fulfill.
	 * @param shared_drawable The Drawable to share context with. (optional, may be null)
	 * @param attribs         The ContextAttribs to use when creating the context. (optional, may be null)
	 *
	 * @throws LWJGLException
	 */
	public static void create(PixelFormat pixel_format, Drawable shared_drawable, ContextAttribs attribs) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			if ( isCreated() )
				throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time.");
			if ( pixel_format == null )
				throw new NullPointerException("pixel_format cannot be null");
			removeShutdownHook();
			registerShutdownHook();
			if ( isFullscreen() )
				switchDisplayMode();

			final DrawableGL drawable = new DrawableGL() {
				public void destroy() {
					synchronized ( GlobalLock.lock ) {
						if ( !isCreated() )
							return;

						releaseDrawable();
						super.destroy();
						destroyWindow();
						x = y = -1;
						cached_icons = null;
						reset();
						removeShutdownHook();
					}
				}
			};
			Display.drawable = drawable;

			try {
				drawable.setPixelFormat(pixel_format, attribs);
				try {
					createWindow();
					try {
						drawable.context = new ContextGL(drawable.peer_info, attribs, shared_drawable != null ? ((DrawableGL)shared_drawable).getContext() : null);
						try {
							makeCurrentAndSetSwapInterval();
							initContext();
						} catch (LWJGLException e) {
							drawable.destroy();
							throw e;
						}
					} catch (LWJGLException e) {
						destroyWindow();
						throw e;
					}
				} catch (LWJGLException e) {
					drawable.destroy();
					throw e;
				}
			} catch (LWJGLException e) {
				display_impl.resetDisplayMode();
				throw e;
			}
		}
	}

	/**
	 * Create the OpenGL ES context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format Describes the minimum specifications the context must fulfill. Must be an instance of org.lwjgl.opengles.PixelFormat.
	 *
	 * @throws LWJGLException
	 */

	public static void create(PixelFormatLWJGL pixel_format) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			create(pixel_format, null, null);
		}
	}

	/**
	 * Create the OpenGL ES context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format    Describes the minimum specifications the context must fulfill. Must be an instance of org.lwjgl.opengles.PixelFormat.
	 * @param shared_drawable The Drawable to share context with. (optional, may be null)
	 *
	 * @throws LWJGLException
	 */
	public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			create(pixel_format, shared_drawable, null);
		}
	}

	/**
	 * Create the OpenGL ES context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format Describes the minimum specifications the context must fulfill. Must be an instance of org.lwjgl.opengles.PixelFormat.
	 * @param attribs      The ContextAttribs to use when creating the context. (optional, may be null)
	 *
	 * @throws LWJGLException
	 */
	public static void create(PixelFormatLWJGL pixel_format, org.lwjgl.opengles.ContextAttribs attribs) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			create(pixel_format, null, attribs);
		}
	}

	/**
	 * Create the OpenGL ES context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 * <p/>
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format    Describes the minimum specifications the context must fulfill. Must be an instance of org.lwjgl.opengles.PixelFormat.
	 * @param shared_drawable The Drawable to share context with. (optional, may be null)
	 * @param attribs         The ContextAttribs to use when creating the context. (optional, may be null)
	 *
	 * @throws LWJGLException
	 */
	public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable, org.lwjgl.opengles.ContextAttribs attribs) throws LWJGLException {
		synchronized ( GlobalLock.lock ) {
			if ( isCreated() )
				throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time.");
			if ( pixel_format == null )
				throw new NullPointerException("pixel_format cannot be null");
			removeShutdownHook();
			registerShutdownHook();
			if ( isFullscreen() )
				switchDisplayMode();

			final DrawableGLES drawable = new DrawableGLES() {

				public void setPixelFormat(final PixelFormatLWJGL pf, final ContextAttribs attribs) throws LWJGLException {
					throw new UnsupportedOperationException();
				}

				public void destroy() {
					synchronized ( GlobalLock.lock ) {
						if ( !isCreated() )
							return;

						releaseDrawable();
						super.destroy();
						destroyWindow();
						x = y = -1;
						cached_icons = null;
						reset();
						removeShutdownHook();
					}
				}
			};
			Display.drawable = drawable;

			try {
				drawable.setPixelFormat(pixel_format);
				try {
					createWindow();
					try {
						drawable.createContext(attribs, shared_drawable);
						try {
							makeCurrentAndSetSwapInterval();
							initContext();
						} catch (LWJGLException e) {
							drawable.destroy();
							throw e;
						}
					} catch (LWJGLException e) {
						destroyWindow();
						throw e;
					}
				} catch (LWJGLException e) {
					drawable.destroy();
					throw e;
				}
			} catch (LWJGLException e) {
				display_impl.resetDisplayMode();
				throw e;
			}
		}
	}

	/**
	 * Set the initial color of the Display. This method is called before the Display is created and will set the
	 * background color to the one specified in this method.
	 *
	 * @param red   - color value between 0 - 1
	 * @param green - color value between 0 - 1
	 * @param blue  - color value between 0 - 1
	 */
	public static void setInitialBackground(float red, float green, float blue) {
		r = red;
		g = green;
		b = blue;
	}

	private static void makeCurrentAndSetSwapInterval() throws LWJGLException {
		makeCurrent();
		try {
			drawable.checkGLError();
		} catch (OpenGLException e) {
			LWJGLUtil.log("OpenGL error during context creation: " + e.getMessage());
		}
		setSwapInterval(swap_interval);
	}

	private static void initContext() {
		drawable.initContext(r, g, b);
		update();
	}

	static DisplayImplementation getImplementation() {
		return display_impl;
	}

	/** Gets a boolean property as a privileged action. */
	static boolean getPrivilegedBoolean(final String property_name) {
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
			public Boolean run() {
				return Boolean.getBoolean(property_name);
			}
		});
	}
	
	/** Gets a string property as a privileged action. */
	static String getPrivilegedString(final String property_name) {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				return System.getProperty(property_name);
			}
		});
	}

	private static void initControls() {
		// Automatically create mouse, keyboard and controller
		if ( !getPrivilegedBoolean("org.lwjgl.opengl.Display.noinput") ) {
			if ( !Mouse.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nomouse") ) {
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					if ( LWJGLUtil.DEBUG ) {
						e.printStackTrace(System.err);
					} else {
						LWJGLUtil.log("Failed to create Mouse: " + e);
					}
				}
			}
			if ( !Keyboard.isCreated() && !getPrivilegedBoolean("org.lwjgl.opengl.Display.nokeyboard") ) {
				try {
					Keyboard.create();
				} catch (LWJGLException e) {
					if ( LWJGLUtil.DEBUG ) {
						e.printStackTrace(System.err);
					} else {
						LWJGLUtil.log("Failed to create Keyboard: " + e);
					}
				}
			}
		}
	}

	/**
	 * Destroy the Display. After this call, there will be no current GL rendering context,
	 * regardless of whether the Display was the current rendering context.
	 */
	public static void destroy() {
		if(isCreated()) {
			drawable.destroy();
		}
	}

	/*
	 * Reset display mode if fullscreen. This method is also called from the shutdown hook added
	 * in the static constructor
	 */

	private static void reset() {
		display_impl.resetDisplayMode();
		current_mode = initial_mode;
	}

	/** @return true if the window's native peer has been created */
	public static boolean isCreated() {
		synchronized ( GlobalLock.lock ) {
			return window_created;
		}
	}

	/**
	 * Set the buffer swap interval. This call is a best-attempt at changing
	 * the monitor swap interval, which is the minimum periodicity of color buffer swaps,
	 * measured in video frame periods, and is not guaranteed to be successful.
	 * <p/>
	 * A video frame period is the time required to display a full frame of video data.
	 *
	 * @param value The swap interval in frames, 0 to disable
	 */
	public static void setSwapInterval(int value) {
		synchronized ( GlobalLock.lock ) {
			swap_interval = value;
			if ( isCreated() )
				drawable.setSwapInterval(swap_interval);

		}
	}

	/**
	 * Enable or disable vertical monitor synchronization. This call is a best-attempt at changing
	 * the vertical refresh synchronization of the monitor, and is not guaranteed to be successful.
	 *
	 * @param sync true to synchronize; false to ignore synchronization
	 */
	public static void setVSyncEnabled(boolean sync) {
		synchronized ( GlobalLock.lock ) {
			setSwapInterval(sync ? 1 : 0);
		}
	}

	/**
	 * Set the window's location. This is a no-op on fullscreen windows or when getParent() != null.
	 * The window is clamped to remain entirely on the screen. If you attempt
	 * to position the window such that it would extend off the screen, the window
	 * is simply placed as close to the edge as possible.
	 * <br><b>note</b>If no location has been specified (or x == y == -1) the window will be centered
	 *
	 * @param new_x The new window location on the x axis
	 * @param new_y The new window location on the y axis
	 */
	public static void setLocation(int new_x, int new_y) {
		synchronized ( GlobalLock.lock ) {
			// cache position
			x = new_x;
			y = new_y;

			// offset if already created
			if ( isCreated() && !isFullscreen() ) {
				reshape();
			}
		}
	}

	private static void reshape() {
		DisplayMode mode = getEffectiveMode();
		display_impl.reshape(getWindowX(), getWindowY(), mode.getWidth(), mode.getHeight());
	}

	/**
	 * Get the driver adapter string. This is a unique string describing the actual card's hardware, eg. "Geforce2", "PS2",
	 * "Radeon9700". If the adapter cannot be determined, this function returns null.
	 *
	 * @return a String
	 */
	public static String getAdapter() {
		synchronized ( GlobalLock.lock ) {
			return display_impl.getAdapter();
		}
	}

	/**
	 * Get the driver version. This is a vendor/adapter specific version string. If the version cannot be determined,
	 * this function returns null.
	 *
	 * @return a String
	 */
	public static String getVersion() {
		synchronized ( GlobalLock.lock ) {
			return display_impl.getVersion();
		}
	}

	/**
	 * Sets one or more icons for the Display.
	 * <ul>
	 * <li>On Windows you should supply at least one 16x16 icon and one 32x32.</li>
	 * <li>Linux (and similar platforms) expect one 32x32 icon.</li>
	 * <li>Mac OS X should be supplied one 128x128 icon</li>
	 * </ul>
	 * The implementation will use the supplied ByteBuffers with image data in RGBA (size must be a power of two) and perform any conversions nescesarry for the specific platform.
	 * <p/>
	 * <b>NOTE:</b> The display will make a deep copy of the supplied byte buffer array, for the purpose
	 * of recreating the icons when you go back and forth fullscreen mode. You therefore only need to
	 * set the icon once per instance.
	 *
	 * @param icons Array of icons in RGBA mode. Pass the icons in order of preference.
	 *
	 * @return number of icons used, or 0 if display hasn't been created
	 */
	public static int setIcon(ByteBuffer[] icons) {
		synchronized ( GlobalLock.lock ) {
			// make deep copy so we dont rely on the supplied buffers later on
			// don't recache!
			if ( cached_icons != icons ) {
				cached_icons = new ByteBuffer[icons.length];
				for ( int i = 0; i < icons.length; i++ ) {
					cached_icons[i] = BufferUtils.createByteBuffer(icons[i].capacity());
					int old_position = icons[i].position();
					cached_icons[i].put(icons[i]);
					icons[i].position(old_position);
					cached_icons[i].flip();
				}
			}

			if ( Display.isCreated() && parent == null ) {
				return display_impl.setIcon(cached_icons);
			} else {
				return 0;
			}
		}
	}

	/**
	 * Enable or disable the Display window to be resized.
	 *
	 * @param resizable set to true to make the Display window resizable;
	 * false to disable resizing on the Display window.
	 */
	public static void setResizable(boolean resizable) {
		window_resizable = resizable;
		if ( isCreated() ) {
			display_impl.setResizable(resizable);
		}
	}

	/**
	 * @return true if the Display window is resizable.
	 */
	public static boolean isResizable() {
		return window_resizable;
	}

	/**
	 * @return true if the Display window has been resized.
	 * This value will be updated after a call to Display.update().
	 *
	 * This will return false if running in fullscreen or with Display.setParent(Canvas parent)
	 */
	public static boolean wasResized() {
		return window_resized;
	}

	/**
	 * @return this method will return the x position (top-left) of the Display window.
	 *
	 * If running in fullscreen mode it will return 0.
	 * If Display.setParent(Canvas parent) is being used, the x position of
	 * the parent will be returned.
	 */
	public static int getX() {

		if (Display.isFullscreen()) {
			return 0;
		}

		if (parent != null) {
			return parent.getX();
		}

		return display_impl.getX();
	}

	/**
	 * @return this method will return the y position (top-left) of the Display window.
	 *
	 * If running in fullscreen mode it will return 0.
	 * If Display.setParent(Canvas parent) is being used, the y position of
	 * the parent will be returned.
	 */
	public static int getY() {

		if (Display.isFullscreen()) {
			return 0;
		}

		if (parent != null) {
			return parent.getY();
		}

		return display_impl.getY();
	}

	/**
	 * @return this method will return the width of the Display window.
	 *
	 * If running in fullscreen mode it will return the width of the current set DisplayMode.
	 * If Display.setParent(Canvas parent) is being used, the width of the parent
	 * will be returned.
	 *
	 * This value will be updated after a call to Display.update().
	 */
	public static int getWidth() {

		if (Display.isFullscreen()) {
			return Display.getDisplayMode().getWidth();
		}

		if (parent != null) {
			return parent.getWidth();
		}

		return width;
	}

	/**
	 * @return this method will return the height of the Display window.
	 *
	 * If running in fullscreen mode it will return the height of the current set DisplayMode.
	 * If Display.setParent(Canvas parent) is being used, the height of the parent
	 * will be returned.
	 *
	 * This value will be updated after a call to Display.update().
	 */
	public static int getHeight() {

		if (Display.isFullscreen()) {
			return Display.getDisplayMode().getHeight();
		}

		if (parent != null) {
			return parent.getHeight();
		}

		return height;
	}
	
	/**
	 * @return this method will return the pixel scale factor of the Display window.
	 *
	 * This method should be used when running in high DPI mode. In such modes Operating
	 * Systems will scale the Display window to avoid the window shrinking due to high
	 * resolutions. The OpenGL frame buffer will however use the higher resolution and
	 * not be scaled to match the Display window size.
	 * 
	 * OpenGL methods that require pixel dependent values e.g. glViewport, glTexImage2D,
	 * glReadPixels, glScissor, glLineWidth, glRenderbufferStorage, etc can convert the 
	 * scaled Display and Mouse coordinates to the correct high resolution value by 
	 * multiplying them by the pixel scale factor.
	 * 
	 * e.g. Display.getWidth() * Display.getPixelScaleFactor() will return the high DPI
	 * width of the OpenGL frame buffer. Whereas Display.getWidth() will be the same as
	 * the OpenGL frame buffer in non high DPI mode.
	 * 
	 * Where high DPI mode is not available this method will just return 1.0f therefore
	 * not have any effect on values that are multiplied by it.
	 */
	public static float getPixelScaleFactor() {
		return display_impl.getPixelScaleFactor();
	}
}
