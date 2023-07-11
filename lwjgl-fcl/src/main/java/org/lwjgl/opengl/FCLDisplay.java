/*
 * Copyright (c) 2002-2010 LWJGL Project
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
 * This is the Display implementation interface. Display delegates
 * to implementors of this interface. There is one DisplayImplementation
 * for each supported platform.
 * @author Tungsten
 */

import java.awt.Canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.lang.reflect.InvocationTargetException;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengles.EGL;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

final class FCLDisplay implements DisplayImplementation {
	/* FCL constants */
	public static final int GrabSuccess = 0;

	/** Window mode enum */
	private static final int FULLSCREEN_LEGACY = 1;
	private static final int WINDOWED = 3;

	/** Current window mode */
	private static int current_window_mode = WINDOWED;

	/** Current EGL Display pointer */
	private static long display;
	private static long current_window;

	private static int display_connection_usage_count;

	/** Event buffer */
	private final FCLEvent event_buffer = new FCLEvent();

	private PeerInfo peer_info;

	/** Saved mode to restore with */
	private DisplayMode saved_mode;
	private DisplayMode current_mode;


	private boolean pointer_grabbed;
	private boolean input_released;
	private boolean grab;
	private boolean minimized;
	private boolean dirty;
	private boolean close_requested;
	private boolean mouseInside = true;
	private boolean resizable;
	private boolean resized;
	
	private int window_x;
	private int window_y;
	private int window_width;
	private int window_height;

	private FCLKeyboard keyboard;
	private FCLMouse mouse;

	/**
	 * increment and decrement display usage.
	 */
	static void incDisplay() throws LWJGLException {
		if (display_connection_usage_count == 0) {
			try {
				// TODO: Can we know if we're on desktop or ES?
				GLContext.loadOpenGLLibrary();
				org.lwjgl.opengles.GLContext.loadOpenGLLibrary();
			} catch (Throwable t) {
			}
			display = openDisplay();
		}
		display_connection_usage_count++;
	}

	static void decDisplay() {
		/*
		 * Some drivers (at least some versions of the radeon dri driver)
		 * don't like it when the display is closed and later re-opened,
		 * so we'll just let the singleton display connection leak.
		 */
/*		display_connection_usage_count--;
		if (display_connection_usage_count < 0)
			throw new InternalError("display_connection_usage_count < 0: " + display_connection_usage_count);
		if (display_connection_usage_count == 0) {
			closeDisplay(display);
			resetErrorHandler(saved_error_handler);
			display = 0;
			GLContext.unloadOpenGLLibrary();
		}*/
	}

	static native long openDisplay() throws LWJGLException;
	static native void closeDisplay(long display);

	private int getWindowMode(boolean fullscreen) throws LWJGLException {
		if (fullscreen) {
			LWJGLUtil.log("Using legacy mode for fullscreen window");
			return FULLSCREEN_LEGACY;
		} else
			return WINDOWED;
	}

	static long getDisplay() {
		if (display_connection_usage_count <= 0)
			throw new InternalError("display_connection_usage_count = " + display_connection_usage_count);
		return display;
	}

	static long getWindow() {
		return current_window;
	}

	private void ungrabKeyboard() {
	}

	private void grabKeyboard() {
	}

	private void grabPointer() {
		if (!pointer_grabbed) {
			int result = nGrabPointer();
			if (result == GrabSuccess) {
				pointer_grabbed = true;
			}
		}
	}
	static native int nGrabPointer();

	private void ungrabPointer() {
		if (pointer_grabbed) {
			pointer_grabbed = false;
			nUngrabPointer();
		}
	}
	static native int nUngrabPointer();

	private static boolean isFullscreen() {
		return current_window_mode == FULLSCREEN_LEGACY;
	}

	private boolean shouldGrab() {
		return !input_released && grab && mouse != null;
	}

	private void updatePointerGrab() {
		if (isFullscreen() || shouldGrab()) {
			grabPointer();
		} else {
			ungrabPointer();
		}
		FCLInjector.setClassLoader(Thread.currentThread().getContextClassLoader());
	}

	private static boolean isLegacyFullscreen() {
		return current_window_mode == FULLSCREEN_LEGACY;
	}

	public void createWindow(final DrawableLWJGL drawable, DisplayMode mode, Canvas parent, int x, int y) throws LWJGLException {
		incDisplay();
		try {
			if ( drawable instanceof DrawableGLES )
				peer_info = new FCLDisplayPeerInfo();

			ByteBuffer handle = peer_info.lockAndGetHandle();
			try {
				current_window_mode = getWindowMode(Display.isFullscreen());

				resizable = Display.isResizable();
				resized = false;
				window_x = x;
				window_y = y;
				window_width = mode.getWidth();
				window_height = mode.getHeight();

				current_window = nCreateWindow(getDisplay(), handle, mode, current_window_mode, x, y, resizable);

				input_released = false;
				pointer_grabbed = false;
				close_requested = false;
				grab = false;
				minimized = false;
				dirty = true;

				if ( drawable instanceof DrawableGLES )
					((DrawableGLES)drawable).initialize(current_window, getDisplay(), EGL.EGL_WINDOW_BIT, (org.lwjgl.opengles.PixelFormat)drawable.getPixelFormat());
			} finally {
				peer_info.unlock();
			}
		} catch (LWJGLException e) {
			decDisplay();
			throw e;
		}
	}
	private static native long nCreateWindow(long display, ByteBuffer peer_info_handle, DisplayMode mode, int window_mode, int x, int y, boolean resizable) throws LWJGLException;
	private static native int nGetX(long window);
	private static native int nGetY(long window);
	private static native int nGetWidth(long window);
	private static native int nGetHeight(long window);

	private void updateInputGrab() {
		updatePointerGrab();
	}

	public void destroyWindow() {
		nDestroyWindow(getDisplay(), getWindow());
		decDisplay();
	}
	static native void nDestroyWindow(long display, long window);

	public void switchDisplayMode(DisplayMode mode) throws LWJGLException {
		current_mode = mode;
		LWJGLUtil.log("Ignore video mode setting on FCL");
	}

	public void resetDisplayMode() {
		try {
			switchDisplayMode(saved_mode);
		}
		catch (LWJGLException e) {
		}
	}

	public int getGammaRampLength() {
		return 0;
	}

	public void setGammaRamp(FloatBuffer gammaRamp) throws LWJGLException {
		throw new LWJGLException("No gamma ramp support on FCL");
	}

	public String getAdapter() {
		return null;
	}

	public String getVersion() {
		return null;
	}

	public DisplayMode init() throws LWJGLException {
		DisplayMode[] modes = getAvailableDisplayModes();
		if (modes == null || modes.length == 0)
			throw new LWJGLException("No modes available");
		saved_mode = modes[0];
		current_mode = saved_mode;
		return saved_mode;
	}

	public void setTitle(String title) {
	}

	public boolean isCloseRequested() {
		boolean result = close_requested;
		close_requested = false;
		return result;
	}

	public boolean isVisible() {
		return !minimized;
	}

	public boolean isActive() {
		return true;
	}

	public boolean isDirty() {
		boolean result = dirty;
		dirty = false;
		return result;
	}

	public PeerInfo createPeerInfo(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
		peer_info = new FCLDisplayPeerInfo(pixel_format);
		return peer_info;
	}

	private void processEvents() {
		while (FCLEvent.getPending() > 0) {
			if (event_buffer.nextEvent() == 0) {
				break;
			}
			if ((mouse != null && mouse.filterEvent(grab, shouldWarpPointer(), event_buffer)) ||
			    (keyboard != null && keyboard.filterEvent(event_buffer)))
				continue;
			switch (event_buffer.getType()) {
				case FCLEvent.FCLMessage:
					if (event_buffer.getFCLMessageMessage() == FCLEvent.CloseRequest)
						close_requested = true;
					break;
				case FCLEvent.ConfigureNotify:
					int x = nGetX(getWindow());
					int y = nGetY(getWindow());
					
					int width = nGetWidth(getWindow());
					int height = nGetHeight(getWindow());
					
					window_x = x;
					window_y = y;
					
					if (window_width != width || window_height != height) {
						resized = true;
						window_width = width;
						window_height = height;
					}
					
					break;
				default:
					break;
			}
		}
	}

	public void update() {
		processEvents();
	}

	public void reshape(int x, int y, int width, int height) {
	}

	public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
                incDisplay();
                try {
                        DisplayMode[] modes = nGetAvailableDisplayModes();
                        return modes;
                } finally {
                        decDisplay();
                }
	}
	private static native DisplayMode[] nGetAvailableDisplayModes() throws LWJGLException;

	/* Mouse */
	public boolean hasWheel() {
		return true;
	}

	public int getButtonCount() {
		return mouse.getButtonCount();
	}

	public void createMouse() throws LWJGLException {
		mouse = new FCLMouse(getWindow());
	}

	public void destroyMouse() {
		mouse = null;
		updateInputGrab();
	}

	public void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons) {
		mouse.poll(grab, coord_buffer, buttons);
	}

	public void readMouse(ByteBuffer buffer) {
		mouse.read(buffer);
	}

	public void setCursorPosition(int x, int y) {
		mouse.setCursorPosition(x, y);
	}

	public void grabMouse(boolean new_grab) {
		if (new_grab != grab) {
			grab = new_grab;
			updateInputGrab();
			mouse.changeGrabbed(grab, shouldWarpPointer());
		}
	}

	private boolean shouldWarpPointer() {
		return pointer_grabbed && shouldGrab();
	}

	public int getNativeCursorCapabilities() {
		return nGetNativeCursorCapabilities();
	}
	private static native int nGetNativeCursorCapabilities();

	public void setNativeCursor(Object handle) throws LWJGLException {
	}

	public int getMinCursorSize() {
		return 32;
	}

	public int getMaxCursorSize() {
		return 32;
	}

	/* Keyboard */
	public void createKeyboard() throws LWJGLException {
		keyboard = new FCLKeyboard();
	}

	public void destroyKeyboard() {
		keyboard.destroy();
		keyboard = null;
	}

	public void pollKeyboard(ByteBuffer keyDownBuffer) {
		keyboard.poll(keyDownBuffer);
	}

	public void readKeyboard(ByteBuffer buffer) {
		keyboard.read(buffer);
	}

	public Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
		return null;
	}

	public void destroyCursor(Object cursorHandle) {
	}

	public int getPbufferCapabilities() {
		try {
			incDisplay();
			try {
				return nGetPbufferCapabilities(getDisplay());
			} finally {
				decDisplay();
			}
		} catch (LWJGLException e) {
			LWJGLUtil.log("Exception occurred in getPbufferCapabilities: " + e);
			return 0;
		}
	}
	private static native int nGetPbufferCapabilities(long display);

	public boolean isBufferLost(PeerInfo handle) {
		return false;
	}

	public PeerInfo createPbuffer(int width, int height, PixelFormat pixel_format, ContextAttribs attribs,
			IntBuffer pixelFormatCaps,
			IntBuffer pBufferAttribs) throws LWJGLException {
		return new FCLPbufferPeerInfo(width, height, pixel_format);
	}

	public void setPbufferAttrib(PeerInfo handle, int attrib, int value) {
		throw new UnsupportedOperationException();
	}

	public void bindTexImageToPbuffer(PeerInfo handle, int buffer) {
		throw new UnsupportedOperationException();
	}

	public void releaseTexImageFromPbuffer(PeerInfo handle, int buffer) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets one or more icons for the Display.
	 * <ul>
	 * <li>On Windows you should supply at least one 16x16 icon and one 32x32.</li>
	 * <li>Linux (and similar platforms) expect one 32x32 icon.</li>
	 * <li>Mac OS X should be supplied one 128x128 icon</li>
	 * </ul>
	 * The implementation will use the supplied ByteBuffers with image data in RGBA and perform any conversions necessary for the specific platform.
	 *
	 * @param icons Array of icons in RGBA mode
	 * @return number of icons used.
	 */
	public int setIcon(ByteBuffer[] icons) {
		LWJGLUtil.log("Failed to set display icon: no support on FCL.");
		return 0;
	}

	public int getX() {
		return window_x;
	}

	public int getY() {
		return window_y;
	}
	
	public int getWidth() {
		return window_width;
	}

	public int getHeight() {
		return window_height;
	}

	public boolean isInsideWindow() {
		return mouseInside;
	}

	public void setResizable(boolean resizable) {
		if (this.resizable == resizable) {
			return;
		}
		
		this.resizable = resizable;
	}

	public boolean wasResized() {
		if (resized) {
			resized = false;
			return true;
		}
		
		return false;
	}

	public float getPixelScaleFactor() {
		return 1f;
	}
}
