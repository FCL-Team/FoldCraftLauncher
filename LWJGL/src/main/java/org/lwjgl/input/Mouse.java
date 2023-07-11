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
package org.lwjgl.input;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.InputImplementation;


/**
 * <br>
 * A raw Mouse interface. This can be used to poll the current state of the
 * mouse buttons, and determine the mouse movement delta since the last poll.
 *
 * n buttons supported, n being a native limit. A scrolly wheel is also
 * supported, if one such is available. Movement is reported as delta from
 * last position or as an absolute position. If the window has been created
 * the absolute position will be clamped to 0 - width | height.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 * $Id$
 */
public class Mouse {
	/** Internal use - event size in bytes */
	public static final int	EVENT_SIZE									= 1 + 1 + 4 + 4 + 4 + 8;

	/** Has the mouse been created? */
	private static boolean		created;

	/** The mouse buttons status from the last poll */
	private static ByteBuffer	buttons;

	/** Mouse absolute X position in pixels */
	private static int				x;

	/** Mouse absolute Y position in pixels */
	private static int				y;
	
	/** Mouse absolute X position in pixels without any clipping */
	private static int				absolute_x;
	
	/** Mouse absolute Y position in pixels without any clipping */
	private static int				absolute_y;

	/** Buffer to hold the deltas dx, dy and dwheel */
	private static IntBuffer	coord_buffer;

	/** Delta X */
	private static int				dx;

	/** Delta Y */
	private static int				dy;

	/** Delta Z */
	private static int				dwheel;

	/** Number of buttons supported by the mouse */
	private static int			buttonCount									= -1;

	/** Does this mouse support a scroll wheel */
	private static boolean		hasWheel;

	/** The current native cursor, if any */
	private static Cursor		currentCursor;

	/** Button names. These are set upon create(), to names like BUTTON0, BUTTON1, etc. */
	private static String[]		buttonName;

	/** hashmap of button names, for fast lookup */
	private static final Map<String, Integer>	buttonMap									= new HashMap<String, Integer>(16);

	/** Lazy initialization */
	private static boolean		initialized;

	/** The mouse button events from the last read */
	private static ByteBuffer	readBuffer;

	/** The current mouse event button being examined */
	private static int				eventButton;

	/** The current state of the button being examined in the event queue */
	private static boolean		eventState;

	/** The current delta of the mouse in the event queue */
	private static int			event_dx;
	private static int			event_dy;
	private static int			event_dwheel;
	/** The current absolute position of the mouse in the event queue */
	private static int			event_x;
	private static int			event_y;
	private static long			event_nanos;
	/** The position of the mouse it was grabbed at */
	private static int			grab_x;
	private static int			grab_y;
	/** The last absolute mouse event position (before clipping) for delta computation */
	private static int			last_event_raw_x;
	private static int			last_event_raw_y;

	/** Buffer size in events */
	private static final int	BUFFER_SIZE									= 50;

	private static boolean		isGrabbed;

	private static InputImplementation implementation;

	/** Whether we need cursor animation emulation */
	private static final boolean emulateCursorAnimation = 	LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_WINDOWS ||
								LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_MACOSX;

	private static  boolean clipMouseCoordinatesToWindow = !getPrivilegedBoolean("org.lwjgl.input.Mouse.allowNegativeMouseCoords");

	/**
	 * Mouse cannot be constructed.
	 */
	private Mouse() {
	}

	/**
	 * Gets the currently bound native cursor, if any.
	 *
	 * @return the currently bound native cursor, if any.
	 */
	public static Cursor getNativeCursor() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return currentCursor;
		}
	}

	/**
	 * Binds a native cursor. If the cursor argument is null, any
	 * currently bound native cursor is disabled, and the cursor reverts
	 * to the default operating system supplied cursor.
	 *
	 * NOTE: The native cursor is not constrained to the window, but
	 * relative events will not be generated if the cursor is outside.
	 *
	 * @param cursor the native cursor object to bind. May be null.
	 * @return The previous Cursor object set, or null.
	 * @throws LWJGLException if the cursor could not be set for any reason
	 */
	public static Cursor setNativeCursor(Cursor cursor) throws LWJGLException {
		synchronized (OpenGLPackageAccess.global_lock) {
			if ((Cursor.getCapabilities() & Cursor.CURSOR_ONE_BIT_TRANSPARENCY) == 0)
				throw new IllegalStateException("Mouse doesn't support native cursors");
			Cursor oldCursor = currentCursor;
			currentCursor = cursor;
			if (isCreated()) {
				if (currentCursor != null) {
					implementation.setNativeCursor(currentCursor.getHandle());
					currentCursor.setTimeout();
				} else {
					implementation.setNativeCursor(null);
				}
			}
			return oldCursor;
		}
	}

	public static boolean isClipMouseCoordinatesToWindow() {
		return clipMouseCoordinatesToWindow;
	}

	public static void setClipMouseCoordinatesToWindow(boolean clip) {
		clipMouseCoordinatesToWindow = clip;
	}

	/**
	 * Set the position of the cursor. If the cursor is not grabbed,
	 * the native cursor is moved to the new position.
	 *
	 * @param new_x The x coordinate of the new cursor position in OpenGL coordinates relative
	 *			to the window origin.
	 * @param new_y The y coordinate of the new cursor position in OpenGL coordinates relative
	 *			to the window origin.
	 */
	public static void setCursorPosition(int new_x, int new_y) {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (!isCreated())
				throw new IllegalStateException("Mouse is not created");
			x = event_x = new_x;
			y = event_y = new_y;
			if (!isGrabbed() && (Cursor.getCapabilities() & Cursor.CURSOR_ONE_BIT_TRANSPARENCY) != 0) {
				implementation.setCursorPosition(x, y);
			}
			else {
				grab_x = new_x;
				grab_y = new_y;
			}
		}
	}

	/**
	 * Static initialization
	 */
	private static void initialize() {
		Sys.initialize();

		// Assign names to all the buttons
		buttonName = new String[16];
		for (int i = 0; i < 16; i++) {
			buttonName[i] = "BUTTON" + i;
			buttonMap.put(buttonName[i], i);
		}

		initialized = true;
	}

	private static void resetMouse() {
		dx = dy = dwheel = 0;
		readBuffer.position(readBuffer.limit());
	}

	static InputImplementation getImplementation() {
		return implementation;
	}

	/**
	 * "Create" the mouse with the given custom implementation.	This is used
	 * reflectively by AWTInputAdapter.
	 *
	 * @throws LWJGLException if the mouse could not be created for any reason
	 */
	private static void create(InputImplementation impl) throws LWJGLException {
		if (created)
			return;
		if (!initialized)
			initialize();
		implementation = impl;
		implementation.createMouse();
		hasWheel = implementation.hasWheel();
		created = true;

		// set mouse buttons
		buttonCount = implementation.getButtonCount();
		buttons = BufferUtils.createByteBuffer(buttonCount);
		coord_buffer = BufferUtils.createIntBuffer(3);
		if (currentCursor != null && implementation.getNativeCursorCapabilities() != 0)
			setNativeCursor(currentCursor);
		readBuffer = ByteBuffer.allocate(EVENT_SIZE * BUFFER_SIZE);
		readBuffer.limit(0);
		setGrabbed(isGrabbed);
	}

	/**
	 * "Create" the mouse. The display must first have been created.
	 * Initially, the mouse is not grabbed and the delta values are reported
	 * with respect to the center of the display.
	 *
	 * @throws LWJGLException if the mouse could not be created for any reason
	 */
	public static void create() throws LWJGLException {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (!Display.isCreated()) throw new IllegalStateException("Display must be created.");

			create(OpenGLPackageAccess.createImplementation());
		}
	}

	/**
	 * @return true if the mouse has been created
	 */
	public static boolean isCreated() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return created;
		}
	}

	/**
	 * "Destroy" the mouse.
	 */
	public static void destroy() {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (!created) return;
			created = false;
			buttons = null;
			coord_buffer = null;

			implementation.destroyMouse();
		}
	}

	/**
	 * Polls the mouse for its current state. Access the polled values using the
	 * get<value> methods.
	 * By using this method, it is possible to "miss" mouse click events if you don't
	 * poll fast enough.
	 *
	 * To use buffered values, you have to call <code>next</code> for each event you
	 * want to read. You can query which button caused the event by using
	 * <code>getEventButton</code>. To get the state of that button, for that event, use
	 * <code>getEventButtonState</code>.
	 *
	 * NOTE: This method does not query the operating system for new events. To do that,
	 * Display.processMessages() (or Display.update()) must be called first.
	 *
	 * @see Mouse#next()
	 * @see Mouse#getEventButton()
	 * @see Mouse#getEventButtonState()
	 * @see Mouse#isButtonDown(int button)
	 * @see Mouse#getX()
	 * @see Mouse#getY()
	 * @see Mouse#getDX()
	 * @see Mouse#getDY()
	 * @see Mouse#getDWheel()
	 */
	public static void poll() {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (!created) throw new IllegalStateException("Mouse must be created before you can poll it");
			implementation.pollMouse(coord_buffer, buttons);

			/* If we're grabbed, poll returns mouse deltas, if not it returns absolute coordinates */
			int poll_coord1 = coord_buffer.get(0);
			int poll_coord2 = coord_buffer.get(1);
			/* The wheel is always relative */
			int poll_dwheel = coord_buffer.get(2);

			if (isGrabbed()) {
				dx += poll_coord1;
				dy += poll_coord2;
				x += poll_coord1;
				y += poll_coord2;
				absolute_x += poll_coord1;
				absolute_y += poll_coord2;
			} else {
				dx = poll_coord1 - absolute_x;
				dy = poll_coord2 - absolute_y;
				absolute_x = x = poll_coord1;
				absolute_y = y = poll_coord2;
			}
                        
			if(clipMouseCoordinatesToWindow) {
				x = Math.min(Display.getWidth() - 1, Math.max(0, x));
				y = Math.min(Display.getHeight() - 1, Math.max(0, y));
			}
			
			dwheel += poll_dwheel;
			read();
		}
	}

	private static void read() {
		readBuffer.compact();
		implementation.readMouse(readBuffer);
		readBuffer.flip();
	}

	/**
	 * See if a particular mouse button is down.
	 *
	 * @param button The index of the button you wish to test (0..getButtonCount-1)
	 * @return true if the specified button is down
	 */
	public static boolean isButtonDown(int button) {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (!created) throw new IllegalStateException("Mouse must be created before you can poll the button state");
			if (button >= buttonCount || button < 0)
				return false;
			else
				return buttons.get(button) == 1;
		}
	}

	/**
	 * Gets a button's name
	 * @param button The button
	 * @return a String with the button's human readable name in it or null if the button is unnamed
	 */
	public static String getButtonName(int button) {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (button >= buttonName.length || button < 0)
				return null;
			else
				return buttonName[button];
		}
	}

	/**
	 * Get's a button's index. If the button is unrecognised then -1 is returned.
	 * @param buttonName The button name
	 */
	public static int getButtonIndex(String buttonName) {
		synchronized (OpenGLPackageAccess.global_lock) {
			Integer ret = buttonMap.get(buttonName);
			if (ret == null)
				return -1;
			else
				return ret;
		}
	}

	/**
	 * Gets the next mouse event. You can query which button caused the event by using
	 * <code>getEventButton()</code> (if any). To get the state of that key, for that event, use
	 * <code>getEventButtonState</code>. To get the current mouse delta values use <code>getEventDX()</code>
	 * and <code>getEventDY()</code>.
	 * @see Mouse#getEventButton()
	 * @see Mouse#getEventButtonState()
	 * @return true if a mouse event was read, false otherwise
	 */
	public static boolean next() {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (!created) throw new IllegalStateException("Mouse must be created before you can read events");
			if (readBuffer.hasRemaining()) {
				eventButton = readBuffer.get();
				eventState = readBuffer.get() != 0;
				if (isGrabbed()) {
					event_dx = readBuffer.getInt();
					event_dy = readBuffer.getInt();
					event_x += event_dx;
					event_y += event_dy;
					last_event_raw_x = event_x;
					last_event_raw_y = event_y;
				} else {
					int new_event_x = readBuffer.getInt();
					int new_event_y = readBuffer.getInt();
					event_dx = new_event_x - last_event_raw_x;
					event_dy = new_event_y - last_event_raw_y;
					event_x = new_event_x;
					event_y = new_event_y;
					last_event_raw_x = new_event_x;
					last_event_raw_y = new_event_y;
				}
				if(clipMouseCoordinatesToWindow) {
					event_x = Math.min(Display.getWidth() - 1, Math.max(0, event_x));
					event_y = Math.min(Display.getHeight() - 1, Math.max(0, event_y));
				}
				event_dwheel = readBuffer.getInt();
				event_nanos = readBuffer.getLong();
				return true;
			} else
				return false;
		}
	}

	/**
	 * @return Current events button. Returns -1 if no button state was changed
	 */
	public static int getEventButton() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return eventButton;
		}
	}

	/**
	 * Get the current events button state.
	 * @return Current events button state.
	 */
	public static boolean getEventButtonState() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return eventState;
		}
	}

	/**
	 * @return Current events delta x.
	 */
	public static int getEventDX() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return event_dx;
		}
	}

	/**
	 * @return Current events delta y.
	 */
	public static int getEventDY() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return event_dy;
		}
	}

	/**
	 * @return Current events absolute x.
	 */
	public static int getEventX() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return event_x;
		}
	}

	/**
	 * @return Current events absolute y.
	 */
	public static int getEventY() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return event_y;
		}
	}

	/**
	 * @return Current events delta z
	 */
	public static int getEventDWheel() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return event_dwheel;
		}
	}

	/**
	 * Gets the time in nanoseconds of the current event.
	 * Only useful for relative comparisons with other
	 * Mouse events, as the absolute time has no defined
	 * origin.
	 *
	 * @return The time in nanoseconds of the current event
	 */
	public static long getEventNanoseconds() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return event_nanos;
		}
	}

	/**
	 * Retrieves the absolute position. It will be clamped to
	 * 0...width-1.
	 *
	 * @return Absolute x axis position of mouse
	 */
	public static int getX() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return x;
		}
	}

	/**
	 * Retrieves the absolute position. It will be clamped to
	 * 0...height-1.
	 *
	 * @return Absolute y axis position of mouse
	 */
	public static int getY() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return y;
		}
	}

	/**
	 * @return Movement on the x axis since last time getDX() was called.
	 */
	public static int getDX() {
		synchronized (OpenGLPackageAccess.global_lock) {
			int result = dx;
			dx = 0;
			return result;
		}
	}

	/**
	 * @return Movement on the y axis since last time getDY() was called.
	 */
	public static int getDY() {
		synchronized (OpenGLPackageAccess.global_lock) {
			int result = dy;
			dy = 0;
			return result;
		}
	}

	/**
	 * @return Movement of the wheel since last time getDWheel() was called
	 */
	public static int getDWheel() {
		synchronized (OpenGLPackageAccess.global_lock) {
			int result = dwheel;
			dwheel = 0;
			return result;
		}
	}

	/**
	 * @return Number of buttons on this mouse
	 */
	public static int getButtonCount() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return buttonCount;
		}
	}

	/**
	 * @return Whether or not this mouse has wheel support
	 */
	public static boolean hasWheel() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return hasWheel;
		}
	}

	/**
	 * @return whether or not the mouse has grabbed the cursor
	 */
	public static boolean isGrabbed() {
		synchronized (OpenGLPackageAccess.global_lock) {
			return isGrabbed;
		}
	}

	/**
	 * Sets whether or not the mouse has grabbed the cursor
	 * (and thus hidden). If grab is false, the getX() and getY()
	 * will return delta movement in pixels clamped to the display
	 * dimensions, from the center of the display.
	 *
	 * @param grab whether the mouse should be grabbed
	 */
	public static void setGrabbed(boolean grab) {
		synchronized (OpenGLPackageAccess.global_lock) {
			boolean grabbed = isGrabbed;
			isGrabbed = grab;
			if (isCreated()) {
				if (grab && !grabbed) {
					// store location mouse was grabbed
					grab_x = x;
					grab_y = y;
				}
				else if (!grab && grabbed) {
					// move mouse back to location it was grabbed before ungrabbing
					if ((Cursor.getCapabilities() & Cursor.CURSOR_ONE_BIT_TRANSPARENCY) != 0)
						implementation.setCursorPosition(grab_x, grab_y);
				}

				implementation.grabMouse(grab);
				// Get latest values from native side
				poll();
				event_x = x;
				event_y = y;
				last_event_raw_x = x;
				last_event_raw_y = y;
				resetMouse();
			}
		}
	}

	/**
	 * Updates the cursor, so that animation can be changed if needed.
	 * This method is called automatically by the window on its update, and
	 * shouldn't be called otherwise
	 */
	public static void updateCursor() {
		synchronized (OpenGLPackageAccess.global_lock) {
			if (emulateCursorAnimation && currentCursor != null && currentCursor.hasTimedOut() && Mouse.isInsideWindow()) {
				currentCursor.nextCursor();
				try {
					setNativeCursor(currentCursor);
				} catch (LWJGLException e) {
					if (LWJGLUtil.DEBUG) e.printStackTrace();
				}
			}
		}
	}

        /** Gets a boolean property as a privileged action. */
	static boolean getPrivilegedBoolean(final String property_name) {
		Boolean value = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
			public Boolean run() {
				return Boolean.getBoolean(property_name);
			}
		});
		return value;
	}

        /**
         * Retrieves whether or not the mouse cursor is within the bounds of the window.
         * If the mouse cursor was moved outside the display during a drag, then the result of calling
         * this method will be true until the button is released.
         * @return true if mouse is inside display, false otherwise.
         */
        public static boolean isInsideWindow() {
            return implementation.isInsideWindow();
        }
}
