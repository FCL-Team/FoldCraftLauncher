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

/**
 * Wrapper class for FCL events.
 *
 * @author Tungsten
 */
final class FCLEvent {
	public static final int KeyPress        = 2;
	public static final int KeyRelease      = 3;
	public static final int ButtonPress     = 4;
	public static final int ButtonRelease	= 5;
	public static final int MotionNotify	= 6;
	public static final int ConfigureNotify = 22;
	public static final int FCLMessage     = 37;

	public static final int CloseRequest    = 0;

	private final ByteBuffer event_buffer;

	FCLEvent() {
		this.event_buffer = createEventBuffer();
	}
	private static native ByteBuffer createEventBuffer();

	public static native int getPending();

	public int nextEvent() {
		return nNextEvent(event_buffer);
	}
	private static native int nNextEvent(ByteBuffer event_buffer);

	public int getType() {
		return nGetType(event_buffer);
	}
	private static native int nGetType(ByteBuffer event_buffer);

	/* FCLMessage methods */

	public int getFCLMessageMessage() {
		return nGetFCLMessageMessage(event_buffer);
	}
	private static native int nGetFCLMessageMessage(ByteBuffer event_buffer);

	/* Button methods */

	public long getButtonTime() {
		return nGetButtonTime(event_buffer);
	}
	private static native long nGetButtonTime(ByteBuffer event_buffer);

	public int getButtonState() {
		return nGetButtonState(event_buffer);
	}
	private static native int nGetButtonState(ByteBuffer event_buffer);

	public int getButtonType() {
		return nGetButtonType(event_buffer);
	}
	private static native int nGetButtonType(ByteBuffer event_buffer);

	public int getButtonButton() {
		return nGetButtonButton(event_buffer);
	}
	private static native int nGetButtonButton(ByteBuffer event_buffer);

	public int getButtonX() {
		return nGetButtonX(event_buffer);
	}
	private static native int nGetButtonX(ByteBuffer event_buffer);

	public int getButtonY() {
		return nGetButtonY(event_buffer);
	}
	private static native int nGetButtonY(ByteBuffer event_buffer);

	/* Key methods */

	public long getKeyTime() {
		return nGetKeyTime(event_buffer);
	}
	private static native int nGetKeyTime(ByteBuffer event_buffer);

	public int getKeyType() {
		return nGetKeyType(event_buffer);
	}
	private static native int nGetKeyType(ByteBuffer event_buffer);

	public int getKeyKeyCode() {
		return nGetKeyKeyCode(event_buffer);
	}
	private static native int nGetKeyKeyCode(ByteBuffer event_buffer);

	public int getKeyState() {
		return nGetKeyState(event_buffer);
	}
	private static native int nGetKeyState(ByteBuffer event_buffer);

	public int getKeyKeyChar() {
		return nGetKeyKeyChar(event_buffer);
	}
	private static native int nGetKeyKeyChar(ByteBuffer event_buffer);
}
