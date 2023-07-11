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

import org.lwjgl.input.Keyboard;

final class FCLKeycodes {
	public static final int  KEY_RESERVED        = 0;

	public static final int  KEY_ESC             = 1;
	public static final int  KEY_1               = 2;
	public static final int  KEY_2               = 3;
	public static final int  KEY_3               = 4;
	public static final int  KEY_4               = 5;
	public static final int  KEY_5               = 6;
	public static final int  KEY_6               = 7;
	public static final int  KEY_7               = 8;
	public static final int  KEY_8               = 9;
	public static final int  KEY_9               = 10;
	public static final int  KEY_0               = 11;
	public static final int  KEY_MINUS           = 12;
	public static final int  KEY_EQUAL           = 13;
	public static final int  KEY_BACKSPACE       = 14;
	public static final int  KEY_TAB             = 15;
	public static final int  KEY_Q               = 16;
	public static final int  KEY_W               = 17;
	public static final int  KEY_E               = 18;
	public static final int  KEY_R               = 19;
	public static final int  KEY_T               = 20;
	public static final int  KEY_Y               = 21;
	public static final int  KEY_U               = 22;
	public static final int  KEY_I               = 23;
	public static final int  KEY_O               = 24;
	public static final int  KEY_P               = 25;
	public static final int  KEY_LEFTBRACE       = 26;
	public static final int  KEY_RIGHTBRACE      = 27;
	public static final int  KEY_ENTER           = 28;
	public static final int  KEY_LEFTCTRL        = 29;
	public static final int  KEY_A               = 30;
	public static final int  KEY_S               = 31;
	public static final int  KEY_D               = 32;
	public static final int  KEY_F               = 33;
	public static final int  KEY_G               = 34;
	public static final int  KEY_H               = 35;
	public static final int  KEY_J               = 36;
	public static final int  KEY_K               = 37;
	public static final int  KEY_L               = 38;
	public static final int  KEY_SEMICOLON       = 39;
	public static final int  KEY_APOSTROPHE      = 40;
	public static final int  KEY_GRAVE           = 41;
	public static final int  KEY_LEFTSHIFT       = 42;
	public static final int  KEY_BACKSLASH       = 43;
	public static final int  KEY_Z               = 44;
	public static final int  KEY_X               = 45;
	public static final int  KEY_C               = 46;
	public static final int  KEY_V               = 47;
	public static final int  KEY_B               = 48;
	public static final int  KEY_N               = 49;
	public static final int  KEY_M               = 50;
	public static final int  KEY_COMMA           = 51;
	public static final int  KEY_DOT             = 52;
	public static final int  KEY_SLASH           = 53;
	public static final int  KEY_RIGHTSHIFT      = 54;
	public static final int  KEY_KPASTERISK      = 55;
	public static final int  KEY_LEFTALT         = 56;
	public static final int  KEY_SPACE           = 57;
	public static final int  KEY_CAPSLOCK        = 58;
	public static final int  KEY_F1              = 59;
	public static final int  KEY_F2              = 60;
	public static final int  KEY_F3              = 61;
	public static final int  KEY_F4              = 62;
	public static final int  KEY_F5              = 63;
	public static final int  KEY_F6              = 64;
	public static final int  KEY_F7              = 65;
	public static final int  KEY_F8              = 66;
	public static final int  KEY_F9              = 67;
	public static final int  KEY_F10             = 68;
	public static final int  KEY_NUMLOCK         = 69;
	public static final int  KEY_SCROLLLOCK      = 70;
	public static final int  KEY_KP7             = 71;
	public static final int  KEY_KP8             = 72;
	public static final int  KEY_KP9             = 73;
	public static final int  KEY_KPMINUS         = 74;
	public static final int  KEY_KP4             = 75;
	public static final int  KEY_KP5             = 76;
	public static final int  KEY_KP6             = 77;
	public static final int  KEY_KPPLUS          = 78;
	public static final int  KEY_KP1             = 79;
	public static final int  KEY_KP2             = 80;
	public static final int  KEY_KP3             = 81;
	public static final int  KEY_KP0             = 82;
	public static final int  KEY_KPDOT           = 83;

	public static final int  KEY_F11             = 87;
	public static final int  KEY_F12             = 88;

	public static final int  KEY_KPENTER         = 96;
	public static final int  KEY_RIGHTCTRL       = 97;
	public static final int  KEY_KPSLASH         = 98;
	public static final int  KEY_SYSRQ           = 99;
	public static final int  KEY_RIGHTALT        = 100;

	public static final int  KEY_HOME            = 102;
	public static final int  KEY_UP              = 103;
	public static final int  KEY_PAGEUP          = 104;
	public static final int  KEY_LEFT            = 105;
	public static final int  KEY_RIGHT           = 106;
	public static final int  KEY_END             = 107;
	public static final int  KEY_DOWN            = 108;
	public static final int  KEY_PAGEDOWN        = 109;
	public static final int  KEY_INSERT          = 110;
	public static final int  KEY_DELETE          = 111;

	public static final int  KEY_KPEQUAL         = 117;

	public static final int  KEY_PAUSE           = 119;

	public static final int  KEY_KPCOMMA         = 121;

	public static final int  KEY_LEFTMETA        = 125;
	public static final int  KEY_RIGHTMETA       = 126;

	public static final int  KEY_MENU            = 139;

	public static final int  KEY_F13             = 183;
	public static final int  KEY_F14             = 184;
	public static final int  KEY_F15             = 185;
	public static final int  KEY_F16             = 186;
	public static final int  KEY_F17             = 187;
	public static final int  KEY_F18             = 188;
	public static final int  KEY_F19             = 189;
	public static final int  KEY_F20             = 190;
	public static final int  KEY_F21             = 191;
	public static final int  KEY_F22             = 192;
	public static final int  KEY_F23             = 193;
	public static final int  KEY_F24             = 194;

	public static final int  KEY_UNKNOWN         = 240;

	public static int mapFCLKeyCodeToLWJGLKeyCode(int keycode) {
		switch (keycode) {
			case KEY_BACKSPACE:
				return Keyboard.KEY_BACK;
			case KEY_TAB:
				return Keyboard.KEY_TAB;
			case KEY_ENTER:
				return Keyboard.KEY_RETURN;
			case KEY_PAUSE:
				return Keyboard.KEY_PAUSE;
			case KEY_SCROLLLOCK:
				return Keyboard.KEY_SCROLL;
			case KEY_SYSRQ:
				return Keyboard.KEY_SYSRQ;
			case KEY_ESC:
				return Keyboard.KEY_ESCAPE;
			case KEY_DELETE:
				return Keyboard.KEY_DELETE;

				/* Cursor control & motion */

			case KEY_HOME:
				return Keyboard.KEY_HOME;
			case KEY_LEFT:
				return Keyboard.KEY_LEFT;
			case KEY_UP:
				return Keyboard.KEY_UP;
			case KEY_RIGHT:
				return Keyboard.KEY_RIGHT;
			case KEY_DOWN:
				return Keyboard.KEY_DOWN;
			case KEY_PAGEUP:
				return Keyboard.KEY_PRIOR;
			case KEY_PAGEDOWN:
				return Keyboard.KEY_NEXT;
			case KEY_END:
				return Keyboard.KEY_END;


				/* Misc Functions */

			case KEY_INSERT:
				return Keyboard.KEY_INSERT;
			case KEY_NUMLOCK:
				return Keyboard.KEY_NUMLOCK;

				/* Keypad Functions, keypad numbers cleverly chosen to map to ascii */

			case KEY_KPENTER:
				return Keyboard.KEY_NUMPADENTER;
			case KEY_KPEQUAL:
				return Keyboard.KEY_NUMPADEQUALS;
			case KEY_KPASTERISK:
				return Keyboard.KEY_MULTIPLY;
			case KEY_KPPLUS:
				return Keyboard.KEY_ADD;
			case KEY_KPMINUS:
				return Keyboard.KEY_SUBTRACT;
			case KEY_KPDOT:
				return Keyboard.KEY_DECIMAL;
			case KEY_KPSLASH:
				return Keyboard.KEY_DIVIDE;

			case KEY_KP0:
				return Keyboard.KEY_NUMPAD0;
			case KEY_KP1:
				return Keyboard.KEY_NUMPAD1;
			case KEY_KP2:
				return Keyboard.KEY_NUMPAD2;
			case KEY_KP3:
				return Keyboard.KEY_NUMPAD3;
			case KEY_KP4:
				return Keyboard.KEY_NUMPAD4;
			case KEY_KP5:
				return Keyboard.KEY_NUMPAD5;
			case KEY_KP6:
				return Keyboard.KEY_NUMPAD6;
			case KEY_KP7:
				return Keyboard.KEY_NUMPAD7;
			case KEY_KP8:
				return Keyboard.KEY_NUMPAD8;
			case KEY_KP9:
				return Keyboard.KEY_NUMPAD9;

				/*
				 * Auxilliary Functions; note the duplicate definitions for left and right
				 * function keys;  Sun keyboards and a few other manufactures have such
				 * function key groups on the left and/or right sides of the keyboard.
				 * We've not found a keyboard with more than 35 function keys total.
				 */

			case KEY_F1:
				return Keyboard.KEY_F1;
			case KEY_F2:
				return Keyboard.KEY_F2;
			case KEY_F3:
				return Keyboard.KEY_F3;
			case KEY_F4:
				return Keyboard.KEY_F4;
			case KEY_F5:
				return Keyboard.KEY_F5;
			case KEY_F6:
				return Keyboard.KEY_F6;
			case KEY_F7:
				return Keyboard.KEY_F7;
			case KEY_F8:
				return Keyboard.KEY_F8;
			case KEY_F9:
				return Keyboard.KEY_F9;
			case KEY_F10:
				return Keyboard.KEY_F10;
			case KEY_F11:
				return Keyboard.KEY_F11;
			case KEY_F12:
				return Keyboard.KEY_F12;
			case KEY_F13:
				return Keyboard.KEY_F13;
			case KEY_F14:
				return Keyboard.KEY_F14;
			case KEY_F15:
				return Keyboard.KEY_F15;

				/* Modifiers */

			case KEY_LEFTSHIFT:
				return Keyboard.KEY_LSHIFT;
			case KEY_RIGHTSHIFT:
				return Keyboard.KEY_RSHIFT;
			case KEY_LEFTCTRL:
				return Keyboard.KEY_LCONTROL;
			case KEY_RIGHTCTRL:
				return Keyboard.KEY_RCONTROL;
			case KEY_CAPSLOCK:
				return Keyboard.KEY_CAPITAL;

			case KEY_LEFTMETA:
				return Keyboard.KEY_LMENU;
			case KEY_RIGHTMETA:
				return Keyboard.KEY_RMENU;
			case KEY_LEFTALT:
				return Keyboard.KEY_LMENU;
			case KEY_RIGHTALT:
				return Keyboard.KEY_RMENU;

				/*
				 *  Latin 1
				 *  Byte 3 = 0
				 */
			case KEY_SPACE:
				return Keyboard.KEY_SPACE;
			case KEY_APOSTROPHE:
				return Keyboard.KEY_APOSTROPHE;
			case KEY_COMMA:
				return Keyboard.KEY_COMMA;
			case KEY_MINUS:
				return Keyboard.KEY_MINUS;
			case KEY_DOT:
				return Keyboard.KEY_PERIOD;
			case KEY_SLASH:
				return Keyboard.KEY_SLASH;
			case KEY_0:
				return Keyboard.KEY_0;
			case KEY_1:
				return Keyboard.KEY_1;
			case KEY_2:
				return Keyboard.KEY_2;
			case KEY_3:
				return Keyboard.KEY_3;
			case KEY_4:
				return Keyboard.KEY_4;
			case KEY_5:
				return Keyboard.KEY_5;
			case KEY_6:
				return Keyboard.KEY_6;
			case KEY_7:
				return Keyboard.KEY_7;
			case KEY_8:
				return Keyboard.KEY_8;
			case KEY_9:
				return Keyboard.KEY_9;
			case KEY_SEMICOLON:
				return Keyboard.KEY_SEMICOLON;
			case KEY_EQUAL:
				return Keyboard.KEY_EQUALS;
			case KEY_LEFTBRACE:
				return Keyboard.KEY_LBRACKET;
			case KEY_RIGHTBRACE:
				return Keyboard.KEY_RBRACKET;
			case KEY_GRAVE:
				return Keyboard.KEY_GRAVE;
			case KEY_A:
				return Keyboard.KEY_A;
			case KEY_B:
				return Keyboard.KEY_B;
			case KEY_C:
				return Keyboard.KEY_C;
			case KEY_D:
				return Keyboard.KEY_D;
			case KEY_E:
				return Keyboard.KEY_E;
			case KEY_F:
				return Keyboard.KEY_F;
			case KEY_G:
				return Keyboard.KEY_G;
			case KEY_H:
				return Keyboard.KEY_H;
			case KEY_I:
				return Keyboard.KEY_I;
			case KEY_J:
				return Keyboard.KEY_J;
			case KEY_K:
				return Keyboard.KEY_K;
			case KEY_L:
				return Keyboard.KEY_L;
			case KEY_M:
				return Keyboard.KEY_M;
			case KEY_N:
				return Keyboard.KEY_N;
			case KEY_O:
				return Keyboard.KEY_O;
			case KEY_P:
				return Keyboard.KEY_P;
			case KEY_Q:
				return Keyboard.KEY_Q;
			case KEY_R:
				return Keyboard.KEY_R;
			case KEY_S:
				return Keyboard.KEY_S;
			case KEY_T:
				return Keyboard.KEY_T;
			case KEY_U:
				return Keyboard.KEY_U;
			case KEY_V:
				return Keyboard.KEY_V;
			case KEY_W:
				return Keyboard.KEY_W;
			case KEY_X:
				return Keyboard.KEY_X;
			case KEY_Y:
				return Keyboard.KEY_Y;
			case KEY_Z:
				return Keyboard.KEY_Z;
			default:
				return Keyboard.KEY_NONE;
		}
	}

}
