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
 * An AWT implementation of a LWJGL compatible Keyboard event queue.
 * @author elias_naur
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Component;
import java.nio.ByteBuffer;

import org.lwjgl.input.Keyboard;

final class KeyboardEventQueue extends EventQueue implements KeyListener {
	private static final int[] KEY_MAP = new int[0xffff];

	private final byte[] key_states = new byte[Keyboard.KEYBOARD_SIZE];

	/** Event scratch array */
	private final ByteBuffer event = ByteBuffer.allocate(Keyboard.EVENT_SIZE);

	private final Component component;

	private boolean has_deferred_event;
	private long deferred_nanos;
	private int deferred_key_code;
	private int deferred_key_location;
	private byte deferred_key_state;
	private int deferred_character;

	static {
		KEY_MAP[KeyEvent.VK_0] = Keyboard.KEY_0;
		KEY_MAP[KeyEvent.VK_1] = Keyboard.KEY_1;
		KEY_MAP[KeyEvent.VK_2] = Keyboard.KEY_2;
		KEY_MAP[KeyEvent.VK_3] = Keyboard.KEY_3;
		KEY_MAP[KeyEvent.VK_4] = Keyboard.KEY_4;
		KEY_MAP[KeyEvent.VK_5] = Keyboard.KEY_5;
		KEY_MAP[KeyEvent.VK_6] = Keyboard.KEY_6;
		KEY_MAP[KeyEvent.VK_7] = Keyboard.KEY_7;
		KEY_MAP[KeyEvent.VK_8] = Keyboard.KEY_8;
		KEY_MAP[KeyEvent.VK_9] = Keyboard.KEY_9;
		KEY_MAP[KeyEvent.VK_A] = Keyboard.KEY_A;
//		KEY_MAP[KeyEvent.VK_ACCEPT] = Keyboard.KEY_ACCEPT;
		KEY_MAP[KeyEvent.VK_ADD] = Keyboard.KEY_ADD;
//		KEY_MAP[KeyEvent.VK_AGAIN] = Keyboard.KEY_AGAIN;
//		KEY_MAP[KeyEvent.VK_ALL_CANDIDATES] = Keyboard.KEY_ALL_CANDIDATES;
//		KEY_MAP[KeyEvent.VK_ALPHANUMERIC] = Keyboard.KEY_ALPHANUMERIC;
//		KEY_MAP[KeyEvent.VK_ALT] = Keyboard.KEY_LMENU; manually mapped
		KEY_MAP[KeyEvent.VK_ALT_GRAPH] = Keyboard.KEY_RMENU;
//		KEY_MAP[KeyEvent.VK_AMPERSAND] = Keyboard.KEY_AMPERSAND;
//		KEY_MAP[KeyEvent.VK_ASTERISK] = Keyboard.KEY_ASTERISK;
		KEY_MAP[KeyEvent.VK_AT] = Keyboard.KEY_AT;
		KEY_MAP[KeyEvent.VK_B] = Keyboard.KEY_B;
//		KEY_MAP[KeyEvent.VK_BACK_QUOTE] = Keyboard.KEY_BACK_QUOTE;
		KEY_MAP[KeyEvent.VK_BACK_SLASH] = Keyboard.KEY_BACKSLASH;
		KEY_MAP[KeyEvent.VK_BACK_SPACE] = Keyboard.KEY_BACK;
//		KEY_MAP[KeyEvent.VK_BRACELEFT] = Keyboard.KEY_BRACELEFT;
//		KEY_MAP[KeyEvent.VK_BRACERIGHT] = Keyboard.KEY_BRACERIGHT;
		KEY_MAP[KeyEvent.VK_C] = Keyboard.KEY_C;
//		KEY_MAP[KeyEvent.VK_CANCEL] = Keyboard.KEY_CANCEL;
		KEY_MAP[KeyEvent.VK_CAPS_LOCK] = Keyboard.KEY_CAPITAL;
		KEY_MAP[KeyEvent.VK_CIRCUMFLEX] = Keyboard.KEY_CIRCUMFLEX;
//		KEY_MAP[KeyEvent.VK_CLEAR] = Keyboard.KEY_CLEAR;
		KEY_MAP[KeyEvent.VK_CLOSE_BRACKET] = Keyboard.KEY_RBRACKET;
//		KEY_MAP[KeyEvent.VK_CODE_INPUT] = Keyboard.KEY_CODE_INPUT;
		KEY_MAP[KeyEvent.VK_COLON] = Keyboard.KEY_COLON;
		KEY_MAP[KeyEvent.VK_COMMA] = Keyboard.KEY_COMMA;
//		KEY_MAP[KeyEvent.VK_COMPOSE] = Keyboard.KEY_COMPOSE;
//		KEY_MAP[KeyEvent.VK_CONTROL] = Keyboard.KEY_LCONTROL; manually mapped
		KEY_MAP[KeyEvent.VK_CONVERT] = Keyboard.KEY_CONVERT;
//		KEY_MAP[KeyEvent.VK_COPY] = Keyboard.KEY_COPY;
//		KEY_MAP[KeyEvent.VK_CUT] = Keyboard.KEY_CUT;
		KEY_MAP[KeyEvent.VK_D] = Keyboard.KEY_D;
//		KEY_MAP[KeyEvent.VK_DEAD_ABOVEDOT] = Keyboard.KEY_DEAD_ABOVEDOT;
//		KEY_MAP[KeyEvent.VK_DEAD_ABOVERING] = Keyboard.KEY_DEAD_ABOVERING;
//		KEY_MAP[KeyEvent.VK_DEAD_ACUTE] = Keyboard.KEY_DEAD_ACUTE;
//		KEY_MAP[KeyEvent.VK_DEAD_BREVE] = Keyboard.KEY_DEAD_BREVE;
//		KEY_MAP[KeyEvent.VK_DEAD_CARON] = Keyboard.KEY_DEAD_CARON;
//		KEY_MAP[KeyEvent.VK_DEAD_CEDILLA] = Keyboard.KEY_DEAD_CEDILLA;
//		KEY_MAP[KeyEvent.VK_DEAD_CIRCUMFLEX] = Keyboard.KEY_DEAD_CIRCUMFLEX;
//		KEY_MAP[KeyEvent.VK_DEAD_DIAERESIS] = Keyboard.KEY_DEAD_DIAERESIS;
//		KEY_MAP[KeyEvent.VK_DEAD_DOUBLEACUTE] = Keyboard.KEY_DEAD_DOUBLEACUTE;
//		KEY_MAP[KeyEvent.VK_DEAD_GRAVE] = Keyboard.KEY_DEAD_GRAVE;
//		KEY_MAP[KeyEvent.VK_DEAD_IOTA] = Keyboard.KEY_DEAD_IOTA;
//		KEY_MAP[KeyEvent.VK_DEAD_MACRON] = Keyboard.KEY_DEAD_MACRON;
//		KEY_MAP[KeyEvent.VK_DEAD_OGONEK] = Keyboard.KEY_DEAD_OGONEK;
//		KEY_MAP[KeyEvent.VK_DEAD_SEMIVOICED_SOUND] = Keyboard.KEY_DEAD_SEMIVOICED_SOUND;
//		KEY_MAP[KeyEvent.VK_DEAD_TILDE] = Keyboard.KEY_DEAD_TILDE;
//		KEY_MAP[KeyEvent.VK_DEAD_VOICED_SOUND] = Keyboard.KEY_DEAD_VOICED_SOUND;
		KEY_MAP[KeyEvent.VK_DECIMAL] = Keyboard.KEY_DECIMAL;
		KEY_MAP[KeyEvent.VK_DELETE] = Keyboard.KEY_DELETE;
		KEY_MAP[KeyEvent.VK_DIVIDE] = Keyboard.KEY_DIVIDE;
//		KEY_MAP[KeyEvent.VK_DOLLAR] = Keyboard.KEY_DOLLAR;
		KEY_MAP[KeyEvent.VK_DOWN] = Keyboard.KEY_DOWN;
		KEY_MAP[KeyEvent.VK_E] = Keyboard.KEY_E;
		KEY_MAP[KeyEvent.VK_END] = Keyboard.KEY_END;
		KEY_MAP[KeyEvent.VK_ENTER] = Keyboard.KEY_RETURN;
		KEY_MAP[KeyEvent.VK_EQUALS] = Keyboard.KEY_EQUALS;
		KEY_MAP[KeyEvent.VK_ESCAPE] = Keyboard.KEY_ESCAPE;
//		KEY_MAP[KeyEvent.VK_EURO_SIGN] = Keyboard.KEY_EURO_SIGN;
//		KEY_MAP[KeyEvent.VK_EXCLAMATION_MARK] = Keyboard.KEY_EXCLAMATION_MARK;
		KEY_MAP[KeyEvent.VK_F] = Keyboard.KEY_F;
		KEY_MAP[KeyEvent.VK_F1] = Keyboard.KEY_F1;
		KEY_MAP[KeyEvent.VK_F10] = Keyboard.KEY_F10;
		KEY_MAP[KeyEvent.VK_F11] = Keyboard.KEY_F11;
		KEY_MAP[KeyEvent.VK_F12] = Keyboard.KEY_F12;
		KEY_MAP[KeyEvent.VK_F13] = Keyboard.KEY_F13;
		KEY_MAP[KeyEvent.VK_F14] = Keyboard.KEY_F14;
		KEY_MAP[KeyEvent.VK_F15] = Keyboard.KEY_F15;
//		KEY_MAP[KeyEvent.VK_F16] = Keyboard.KEY_F16;
//		KEY_MAP[KeyEvent.VK_F17] = Keyboard.KEY_F17;
//		KEY_MAP[KeyEvent.VK_F18] = Keyboard.KEY_F18;
//		KEY_MAP[KeyEvent.VK_F19] = Keyboard.KEY_F19;
		KEY_MAP[KeyEvent.VK_F2] = Keyboard.KEY_F2;
//		KEY_MAP[KeyEvent.VK_F20] = Keyboard.KEY_F20;
//		KEY_MAP[KeyEvent.VK_F21] = Keyboard.KEY_F21;
//		KEY_MAP[KeyEvent.VK_F22] = Keyboard.KEY_F22;
//		KEY_MAP[KeyEvent.VK_F23] = Keyboard.KEY_F23;
//		KEY_MAP[KeyEvent.VK_F24] = Keyboard.KEY_F24;
		KEY_MAP[KeyEvent.VK_F3] = Keyboard.KEY_F3;
		KEY_MAP[KeyEvent.VK_F4] = Keyboard.KEY_F4;
		KEY_MAP[KeyEvent.VK_F5] = Keyboard.KEY_F5;
		KEY_MAP[KeyEvent.VK_F6] = Keyboard.KEY_F6;
		KEY_MAP[KeyEvent.VK_F7] = Keyboard.KEY_F7;
		KEY_MAP[KeyEvent.VK_F8] = Keyboard.KEY_F8;
		KEY_MAP[KeyEvent.VK_F9] = Keyboard.KEY_F9;
//		KEY_MAP[KeyEvent.VK_FINAL] = Keyboard.KEY_FINAL;
//		KEY_MAP[KeyEvent.VK_FIND] = Keyboard.KEY_FIND;
//		KEY_MAP[KeyEvent.VK_FULL_WIDTH] = Keyboard.KEY_FULL_WIDTH;
		KEY_MAP[KeyEvent.VK_G] = Keyboard.KEY_G;
//		KEY_MAP[KeyEvent.VK_GREATER] = Keyboard.KEY_GREATER;
		KEY_MAP[KeyEvent.VK_H] = Keyboard.KEY_H;
//		KEY_MAP[KeyEvent.VK_HALF_WIDTH] = Keyboard.KEY_HALF_WIDTH;
//		KEY_MAP[KeyEvent.VK_HELP] = Keyboard.KEY_HELP;
//		KEY_MAP[KeyEvent.VK_HIRAGANA] = Keyboard.KEY_HIRAGANA;
		KEY_MAP[KeyEvent.VK_HOME] = Keyboard.KEY_HOME;
		KEY_MAP[KeyEvent.VK_I] = Keyboard.KEY_I;
//		KEY_MAP[KeyEvent.VK_INPUT_METHOD_ON_OFF] = Keyboard.KEY_INPUT_METHOD_ON_OFF;
		KEY_MAP[KeyEvent.VK_INSERT] = Keyboard.KEY_INSERT;
//		KEY_MAP[KeyEvent.VK_INVERTED_EXCLAMATION_MARK] = Keyboard.KEY_INVERTED_EXCLAMATION_MARK;
		KEY_MAP[KeyEvent.VK_J] = Keyboard.KEY_J;
//		KEY_MAP[KeyEvent.VK_JAPANESE_HIRAGANA] = Keyboard.KEY_JAPANESE_HIRAGANA;
//		KEY_MAP[KeyEvent.VK_JAPANESE_KATAKANA] = Keyboard.KEY_JAPANESE_KATAKANA;
//		KEY_MAP[KeyEvent.VK_JAPANESE_ROMAN] = Keyboard.KEY_JAPANESE_ROMAN;
		KEY_MAP[KeyEvent.VK_K] = Keyboard.KEY_K;
		KEY_MAP[KeyEvent.VK_KANA] = Keyboard.KEY_KANA;
//		KEY_MAP[KeyEvent.VK_KANA_LOCK] = Keyboard.KEY_KANA_LOCK;
		KEY_MAP[KeyEvent.VK_KANJI] = Keyboard.KEY_KANJI;
//		KEY_MAP[KeyEvent.VK_KATAKANA] = Keyboard.KEY_KATAKANA;
//		KEY_MAP[KeyEvent.VK_KP_DOWN] = Keyboard.KEY_KP_DOWN;
//		KEY_MAP[KeyEvent.VK_KP_LEFT] = Keyboard.KEY_KP_LEFT;
//		KEY_MAP[KeyEvent.VK_KP_RIGHT] = Keyboard.KEY_KP_RIGHT;
//		KEY_MAP[KeyEvent.VK_KP_UP] = Keyboard.KEY_KP_UP;
		KEY_MAP[KeyEvent.VK_L] = Keyboard.KEY_L;
		KEY_MAP[KeyEvent.VK_LEFT] = Keyboard.KEY_LEFT;
//		KEY_MAP[KeyEvent.VK_LEFT_PARENTHESIS] = Keyboard.KEY_LEFT_PARENTHESIS;
//		KEY_MAP[KeyEvent.VK_LESS] = Keyboard.KEY_LESS;
		KEY_MAP[KeyEvent.VK_M] = Keyboard.KEY_M;
//		KEY_MAP[KeyEvent.VK_META] = Keyboard.KEY_LMENU; manually mapped
		KEY_MAP[KeyEvent.VK_MINUS] = Keyboard.KEY_MINUS;
//		KEY_MAP[KeyEvent.VK_MODECHANGE] = Keyboard.KEY_MODECHANGE;
		KEY_MAP[KeyEvent.VK_MULTIPLY] = Keyboard.KEY_MULTIPLY;
		KEY_MAP[KeyEvent.VK_N] = Keyboard.KEY_N;
//		KEY_MAP[KeyEvent.VK_NONCONVERT] = Keyboard.KEY_NONCONVERT;
		KEY_MAP[KeyEvent.VK_NUM_LOCK] = Keyboard.KEY_NUMLOCK;
//		KEY_MAP[KeyEvent.VK_NUMBER_SIGN] = Keyboard.KEY_NUMBER_SIGN;
		KEY_MAP[KeyEvent.VK_NUMPAD0] = Keyboard.KEY_NUMPAD0;
		KEY_MAP[KeyEvent.VK_NUMPAD1] = Keyboard.KEY_NUMPAD1;
		KEY_MAP[KeyEvent.VK_NUMPAD2] = Keyboard.KEY_NUMPAD2;
		KEY_MAP[KeyEvent.VK_NUMPAD3] = Keyboard.KEY_NUMPAD3;
		KEY_MAP[KeyEvent.VK_NUMPAD4] = Keyboard.KEY_NUMPAD4;
		KEY_MAP[KeyEvent.VK_NUMPAD5] = Keyboard.KEY_NUMPAD5;
		KEY_MAP[KeyEvent.VK_NUMPAD6] = Keyboard.KEY_NUMPAD6;
		KEY_MAP[KeyEvent.VK_NUMPAD7] = Keyboard.KEY_NUMPAD7;
		KEY_MAP[KeyEvent.VK_NUMPAD8] = Keyboard.KEY_NUMPAD8;
		KEY_MAP[KeyEvent.VK_NUMPAD9] = Keyboard.KEY_NUMPAD9;
		KEY_MAP[KeyEvent.VK_O] = Keyboard.KEY_O;
		KEY_MAP[KeyEvent.VK_OPEN_BRACKET] = Keyboard.KEY_LBRACKET;
		KEY_MAP[KeyEvent.VK_P] = Keyboard.KEY_P;
		KEY_MAP[KeyEvent.VK_PAGE_DOWN] = Keyboard.KEY_NEXT;
		KEY_MAP[KeyEvent.VK_PAGE_UP] = Keyboard.KEY_PRIOR;
//		KEY_MAP[KeyEvent.VK_PASTE] = Keyboard.KEY_PASTE;
		KEY_MAP[KeyEvent.VK_PAUSE] = Keyboard.KEY_PAUSE;
		KEY_MAP[KeyEvent.VK_PERIOD] = Keyboard.KEY_PERIOD;
//		KEY_MAP[KeyEvent.VK_PLUS] = Keyboard.KEY_PLUS;
//		KEY_MAP[KeyEvent.VK_PREVIOUS_CANDIDATE] = Keyboard.KEY_PREVIOUS_CANDIDATE;
//		KEY_MAP[KeyEvent.VK_PRINTSCREEN] = Keyboard.KEY_PRINTSCREEN;
//		KEY_MAP[KeyEvent.VK_PROPS] = Keyboard.KEY_PROPS;
		KEY_MAP[KeyEvent.VK_Q] = Keyboard.KEY_Q;
//		KEY_MAP[KeyEvent.VK_QUOTE] = Keyboard.KEY_QUOTE;
//		KEY_MAP[KeyEvent.VK_QUOTEDBL] = Keyboard.KEY_QUOTEDBL;
		KEY_MAP[KeyEvent.VK_R] = Keyboard.KEY_R;
		KEY_MAP[KeyEvent.VK_RIGHT] = Keyboard.KEY_RIGHT;
//		KEY_MAP[KeyEvent.VK_RIGHT_PARENTHESIS] = Keyboard.KEY_RIGHT_PARENTHESIS;
//		KEY_MAP[KeyEvent.VK_ROMAN_CHARACTERS] = Keyboard.KEY_ROMAN_CHARACTERS;
		KEY_MAP[KeyEvent.VK_S] = Keyboard.KEY_S;
		KEY_MAP[KeyEvent.VK_SCROLL_LOCK] = Keyboard.KEY_SCROLL;
		KEY_MAP[KeyEvent.VK_SEMICOLON] = Keyboard.KEY_SEMICOLON;
		KEY_MAP[KeyEvent.VK_SEPARATOR] = Keyboard.KEY_DECIMAL;
//		KEY_MAP[KeyEvent.VK_SHIFT] = Keyboard.KEY_LSHIFT; manually mapped
		KEY_MAP[KeyEvent.VK_SLASH] = Keyboard.KEY_SLASH;
		KEY_MAP[KeyEvent.VK_SPACE] = Keyboard.KEY_SPACE;
		KEY_MAP[KeyEvent.VK_STOP] = Keyboard.KEY_STOP;
		KEY_MAP[KeyEvent.VK_SUBTRACT] = Keyboard.KEY_SUBTRACT;
		KEY_MAP[KeyEvent.VK_T] = Keyboard.KEY_T;
		KEY_MAP[KeyEvent.VK_TAB] = Keyboard.KEY_TAB;
		KEY_MAP[KeyEvent.VK_U] = Keyboard.KEY_U;
//		KEY_MAP[KeyEvent.VK_UNDERSCORE] = Keyboard.KEY_UNDERSCORE;
//		KEY_MAP[KeyEvent.VK_UNDO] = Keyboard.KEY_UNDO;
		KEY_MAP[KeyEvent.VK_UP] = Keyboard.KEY_UP;
		KEY_MAP[KeyEvent.VK_V] = Keyboard.KEY_V;
		KEY_MAP[KeyEvent.VK_W] = Keyboard.KEY_W;
		KEY_MAP[KeyEvent.VK_X] = Keyboard.KEY_X;
		KEY_MAP[KeyEvent.VK_Y] = Keyboard.KEY_Y;
		KEY_MAP[KeyEvent.VK_Z] = Keyboard.KEY_Z;
	}

	KeyboardEventQueue(Component component) {
		super(Keyboard.EVENT_SIZE);
		this.component = component;
	}

	public void register() {
		component.addKeyListener(this);
	}

	public void unregister() {
		/*
		 * This line is commented out to work around AWT bug 4867453:
		 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4867453
		 */
		//component.removeKeyListener(this);
	}

	private void putKeyboardEvent(int key_code, byte state, int character, long nanos, boolean repeat) {
		event.clear();
		event.putInt(key_code).put(state).putInt(character).putLong(nanos).put(repeat ? (byte)1 : (byte)0);
		event.flip();
		putEvent(event);
	}

	public synchronized void poll(ByteBuffer key_down_buffer) {
		flushDeferredEvent();
		int old_position = key_down_buffer.position();
		key_down_buffer.put(key_states);
		key_down_buffer.position(old_position);
	}

	public synchronized void copyEvents(ByteBuffer dest) {
		flushDeferredEvent();
		super.copyEvents(dest);
	}

	private synchronized void handleKey(int key_code, int key_location, byte state, int character, long nanos) {
		if (character == KeyEvent.CHAR_UNDEFINED)
			character = Keyboard.CHAR_NONE;
		if (state == 1) {
			boolean repeat = false;
			if (has_deferred_event) {
				if ((nanos == deferred_nanos && deferred_key_code == key_code &&
						deferred_key_location == key_location)) {
					has_deferred_event = false;
					repeat = true; // Repeat event
				} else
					flushDeferredEvent();
			}
			putKeyEvent(key_code, key_location, state, character, nanos, repeat);
		} else {
			flushDeferredEvent();
			has_deferred_event = true;
			deferred_nanos = nanos;
			deferred_key_code = key_code;
			deferred_key_location = key_location;
			deferred_key_state = state;
			deferred_character = character;
		}
	}

	private void flushDeferredEvent() {
		if (has_deferred_event) {
			putKeyEvent(deferred_key_code, deferred_key_location, deferred_key_state, deferred_character, deferred_nanos, false);
			has_deferred_event = false;
		}
	}

	private void putKeyEvent(int key_code, int key_location, byte state, int character, long nanos, boolean repeat) {
		int key_code_mapped = getMappedKeyCode(key_code, key_location);
		/* Ignore repeating presses */
		if ( key_states[key_code_mapped] == state )
			repeat = true;
		key_states[key_code_mapped] = state;
		int key_int_char = character & 0xffff;
		putKeyboardEvent(key_code_mapped, state, key_int_char, nanos, repeat);
	}

	private int getMappedKeyCode(int key_code, int position) {
		// manually map positioned keys
		switch (key_code) {
			case KeyEvent.VK_ALT: // fall through
				if (position == KeyEvent.KEY_LOCATION_RIGHT)
					return Keyboard.KEY_RMENU;
				else
					return Keyboard.KEY_LMENU;
			case KeyEvent.VK_META:
				if (position == KeyEvent.KEY_LOCATION_RIGHT)
					return Keyboard.KEY_RMETA;
				else
					return Keyboard.KEY_LMETA;
			case KeyEvent.VK_SHIFT:
				if (position == KeyEvent.KEY_LOCATION_RIGHT)
					return Keyboard.KEY_RSHIFT;
				else
					return Keyboard.KEY_LSHIFT;
			case KeyEvent.VK_CONTROL:
				if (position == KeyEvent.KEY_LOCATION_RIGHT)
					return Keyboard.KEY_RCONTROL;
				else
					return Keyboard.KEY_LCONTROL;
			default:
				return KEY_MAP[key_code];
		}
	}

	public void keyPressed(KeyEvent e) {
		handleKey(e.getKeyCode(), e.getKeyLocation(), (byte)1, e.getKeyChar(), e.getWhen()*1000000);
	}

	public void keyReleased(KeyEvent e) {
		handleKey(e.getKeyCode(), e.getKeyLocation(), (byte)0, Keyboard.CHAR_NONE, e.getWhen()*1000000);
	}

	public void keyTyped(KeyEvent e) {
	}
}
