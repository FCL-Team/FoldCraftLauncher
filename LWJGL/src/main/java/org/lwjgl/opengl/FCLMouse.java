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
 * @author Tungsten
 */

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

final class FCLMouse {
	// scale the mouse wheel according to DirectInput
	private static final int WHEEL_SCALE = 120;

	private int button_count;
	
	/* FCL constants */
	private static final int Button1 = 1;
	private static final int Button2 = 2;
	private static final int Button3 = 3;
	private static final int Button4 = 4;
	private static final int Button5 = 5;
	
	private static final int Button6 = 6; // wheel tilt left *rare*
	private static final int Button7 = 7; // wheel tilt right *rare*
	private static final int Button8 = 8; // back button
	private static final int Button9 = 9; // forward button

	private static final int ButtonPress = 4;
	private static final int ButtonRelease = 5;

	private final long window;
	private final ByteBuffer event_buffer = ByteBuffer.allocate(Mouse.EVENT_SIZE);

	private int last_x;
	private int last_y;
	private int accum_dx;
	private int accum_dy;
	private int accum_dz;
	private byte[] buttons;
	private EventQueue event_queue;
	private long last_event_nanos;

	FCLMouse(long window) throws LWJGLException {
		this.window = window;
		button_count = nGetButtonCount();
		buttons = new byte[button_count];
		reset(false, false);
	}

	private void reset(boolean grab, boolean warp_pointer) {
		event_queue = new EventQueue(event_buffer.capacity());
		accum_dx = accum_dy = 0;

		// Pretend that the cursor never moved
		// last_x = win_x;
		// last_y = transformY(win_y);
		// doHandlePointerMotion(grab, warp_pointer, win_x, win_y, last_event_nanos);
	}

	public void read(ByteBuffer buffer) {
		event_queue.copyEvents(buffer);
	}

	public void poll(boolean grab, IntBuffer coord_buffer, ByteBuffer buttons_buffer) {
		if (grab) {
			coord_buffer.put(0, accum_dx);
			coord_buffer.put(1, accum_dy);
		} else {
			coord_buffer.put(0, last_x);
			coord_buffer.put(1, last_y);
		}
		coord_buffer.put(2, accum_dz);
		accum_dx = accum_dy = accum_dz = 0;
		for (int i = 0; i < buttons.length; i++)
			buttons_buffer.put(i, buttons[i]);
	}

	private void putMouseEventWithCoords(byte button, byte state, int coord1, int coord2, int dz, long nanos) {
		event_buffer.clear();
		event_buffer.put(button).put(state).putInt(coord1).putInt(coord2).putInt(dz).putLong(nanos);
		event_buffer.flip();
		event_queue.putEvent(event_buffer);
		last_event_nanos = nanos;
	}

	private void setCursorPos(boolean grab, int x, int y, long nanos) {
		y = transformY(y);
		int dx = x - last_x;
		int dy = y - last_y;
		if (dx != 0 || dy != 0) {
			accum_dx += dx;
			accum_dy += dy;
			last_x = x;
			last_y = y;
			if (grab) {
				putMouseEventWithCoords((byte)-1, (byte)0, dx, dy, 0, nanos);
			} else {
				putMouseEventWithCoords((byte)-1, (byte)0, x, y, 0, nanos);
			}
		}
	}

	private void doHandlePointerMotion(boolean grab, boolean warp_pointer, int win_x, int win_y, long nanos) {
		setCursorPos(grab, win_x, win_y, nanos);
	}

	public void changeGrabbed(boolean grab, boolean warp_pointer) {
		reset(grab, warp_pointer);
	}

	public int getButtonCount() {
		return buttons.length;
	}

	private int transformY(int y) {
		return nGetWindowHeight(window) - 1 - y;
	}
	private static native int nGetWindowHeight(long window);
	private static native int nGetWindowWidth(long window);
	
	private static native int nGetButtonCount();

	public void setCursorPosition(int x, int y) {
	}

	private void handlePointerMotion(boolean grab, boolean warp_pointer, long millis, int x, int y) {
		doHandlePointerMotion(grab, warp_pointer, x, y, millis*1000000);
	}

	private void handleButton(boolean grab, int button, byte state, long nanos) {
		byte button_num;
		switch (button) {
			case Button1:
				button_num = (byte)0;
				break;
			case Button2:
				button_num = (byte)2;
				break;
			case Button3:
				button_num = (byte)1;
				break;
			case Button6:
				button_num = (byte)5;
				break;
			case Button7:
				button_num = (byte)6;
				break;
			case Button8:
				button_num = (byte)3; // back button
				break;
			case Button9:
				button_num = (byte)4; // forward button
				break;
			default:
				if (button > Button9 && button <= button_count) {
					button_num = (byte)(button-1);
					break;
				}
				return;
		}
		buttons[button_num] = state;
		putMouseEvent(grab, button_num, state, 0, nanos);
	}

	private void putMouseEvent(boolean grab, byte button, byte state, int dz, long nanos) {
		if (grab)
			putMouseEventWithCoords(button, state, 0, 0, dz, nanos);
		else
			putMouseEventWithCoords(button, state, last_x, last_y, dz, nanos);
	}

	private void handleButtonPress(boolean grab, byte button, long nanos) {
		int delta = 0;
		switch (button) {
			case Button4:
				delta = WHEEL_SCALE;
				putMouseEvent(grab, (byte)-1, (byte)0, delta, nanos);
				accum_dz += delta;
				break;
			case Button5:
				delta = -WHEEL_SCALE;
				putMouseEvent(grab, (byte)-1, (byte)0, delta, nanos);
				accum_dz += delta;
				break;
			default:
				handleButton(grab, button, (byte)1, nanos);
				break;
		}
	}

	private void handleButtonEvent(boolean grab, long millis, int type, byte button) {
		long nanos = millis*1000000;
		switch (type) {
			case ButtonRelease:
				handleButton(grab, button, (byte)0, nanos);
				break;
			case ButtonPress:
				handleButtonPress(grab, button, nanos);
				break;
			default:
				break;
		}
	}

	public boolean filterEvent(boolean grab, boolean warp_pointer, FCLEvent event) {
		switch (event.getType()) {
			case FCLEvent.ButtonPress: /* Fall through */
			case FCLEvent.ButtonRelease:
				handleButtonEvent(grab, event.getButtonTime(), event.getButtonType(), (byte)event.getButtonButton());
				return true;
			case FCLEvent.MotionNotify:
				handlePointerMotion(grab, warp_pointer, event.getButtonTime(), event.getButtonX(), event.getButtonY());
				return true;
			default:
				break;
		}
		return false;
	}
}
