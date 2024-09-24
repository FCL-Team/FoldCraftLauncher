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
package org.lwjgl.util.jinput;

import net.java.games.input.Component;

import org.lwjgl.input.Keyboard;

/**
 * @author elias
 */
final class KeyMap {
	public static Component.Identifier.Key map(int lwjgl_key_code) {
		switch (lwjgl_key_code) {
			case Keyboard.KEY_ESCAPE:
				return Component.Identifier.Key.ESCAPE;
			case Keyboard.KEY_1:
				return Component.Identifier.Key._1;
			case Keyboard.KEY_2:
				return Component.Identifier.Key._2;
			case Keyboard.KEY_3:
				return Component.Identifier.Key._3;
			case Keyboard.KEY_4:
				return Component.Identifier.Key._4;
			case Keyboard.KEY_5:
				return Component.Identifier.Key._5;
			case Keyboard.KEY_6:
				return Component.Identifier.Key._6;
			case Keyboard.KEY_7:
				return Component.Identifier.Key._7;
			case Keyboard.KEY_8:
				return Component.Identifier.Key._8;
			case Keyboard.KEY_9:
				return Component.Identifier.Key._9;
			case Keyboard.KEY_0:
				return Component.Identifier.Key._0;
			case Keyboard.KEY_MINUS:
				return Component.Identifier.Key.MINUS;
			case Keyboard.KEY_EQUALS:
				return Component.Identifier.Key.EQUALS;
			case Keyboard.KEY_BACK:
				return Component.Identifier.Key.BACK;
			case Keyboard.KEY_TAB:
				return Component.Identifier.Key.TAB;
			case Keyboard.KEY_Q:
				return Component.Identifier.Key.Q;
			case Keyboard.KEY_W:
				return Component.Identifier.Key.W;
			case Keyboard.KEY_E:
				return Component.Identifier.Key.E;
			case Keyboard.KEY_R:
				return Component.Identifier.Key.R;
			case Keyboard.KEY_T:
				return Component.Identifier.Key.T;
			case Keyboard.KEY_Y:
				return Component.Identifier.Key.Y;
			case Keyboard.KEY_U:
				return Component.Identifier.Key.U;
			case Keyboard.KEY_I:
				return Component.Identifier.Key.I;
			case Keyboard.KEY_O:
				return Component.Identifier.Key.O;
			case Keyboard.KEY_P:
				return Component.Identifier.Key.P;
			case Keyboard.KEY_LBRACKET:
				return Component.Identifier.Key.LBRACKET;
			case Keyboard.KEY_RBRACKET:
				return Component.Identifier.Key.RBRACKET;
			case Keyboard.KEY_RETURN:
				return Component.Identifier.Key.RETURN;
			case Keyboard.KEY_LCONTROL:
				return Component.Identifier.Key.LCONTROL;
			case Keyboard.KEY_A:
				return Component.Identifier.Key.A;
			case Keyboard.KEY_S:
				return Component.Identifier.Key.S;
			case Keyboard.KEY_D:
				return Component.Identifier.Key.D;
			case Keyboard.KEY_F:
				return Component.Identifier.Key.F;
			case Keyboard.KEY_G:
				return Component.Identifier.Key.G;
			case Keyboard.KEY_H:
				return Component.Identifier.Key.H;
			case Keyboard.KEY_J:
				return Component.Identifier.Key.J;
			case Keyboard.KEY_K:
				return Component.Identifier.Key.K;
			case Keyboard.KEY_L:
				return Component.Identifier.Key.L;
			case Keyboard.KEY_SEMICOLON:
				return Component.Identifier.Key.SEMICOLON;
			case Keyboard.KEY_APOSTROPHE:
				return Component.Identifier.Key.APOSTROPHE;
			case Keyboard.KEY_GRAVE:
				return Component.Identifier.Key.GRAVE;
			case Keyboard.KEY_LSHIFT:
				return Component.Identifier.Key.LSHIFT;
			case Keyboard.KEY_BACKSLASH:
				return Component.Identifier.Key.BACKSLASH;
			case Keyboard.KEY_Z:
				return Component.Identifier.Key.Z;
			case Keyboard.KEY_X:
				return Component.Identifier.Key.X;
			case Keyboard.KEY_C:
				return Component.Identifier.Key.C;
			case Keyboard.KEY_V:
				return Component.Identifier.Key.V;
			case Keyboard.KEY_B:
				return Component.Identifier.Key.B;
			case Keyboard.KEY_N:
				return Component.Identifier.Key.N;
			case Keyboard.KEY_M:
				return Component.Identifier.Key.M;
			case Keyboard.KEY_COMMA:
				return Component.Identifier.Key.COMMA;
			case Keyboard.KEY_PERIOD:
				return Component.Identifier.Key.PERIOD;
			case Keyboard.KEY_SLASH:
				return Component.Identifier.Key.SLASH;
			case Keyboard.KEY_RSHIFT:
				return Component.Identifier.Key.RSHIFT;
			case Keyboard.KEY_MULTIPLY:
				return Component.Identifier.Key.MULTIPLY;
			case Keyboard.KEY_LMENU:
				return Component.Identifier.Key.LALT;
			case Keyboard.KEY_SPACE:
				return Component.Identifier.Key.SPACE;
			case Keyboard.KEY_CAPITAL:
				return Component.Identifier.Key.CAPITAL;
			case Keyboard.KEY_F1:
				return Component.Identifier.Key.F1;
			case Keyboard.KEY_F2:
				return Component.Identifier.Key.F2;
			case Keyboard.KEY_F3:
				return Component.Identifier.Key.F3;
			case Keyboard.KEY_F4:
				return Component.Identifier.Key.F4;
			case Keyboard.KEY_F5:
				return Component.Identifier.Key.F5;
			case Keyboard.KEY_F6:
				return Component.Identifier.Key.F6;
			case Keyboard.KEY_F7:
				return Component.Identifier.Key.F7;
			case Keyboard.KEY_F8:
				return Component.Identifier.Key.F8;
			case Keyboard.KEY_F9:
				return Component.Identifier.Key.F9;
			case Keyboard.KEY_F10:
				return Component.Identifier.Key.F10;
			case Keyboard.KEY_NUMLOCK:
				return Component.Identifier.Key.NUMLOCK;
			case Keyboard.KEY_SCROLL:
				return Component.Identifier.Key.SCROLL;
			case Keyboard.KEY_NUMPAD7:
				return Component.Identifier.Key.NUMPAD7;
			case Keyboard.KEY_NUMPAD8:
				return Component.Identifier.Key.NUMPAD8;
			case Keyboard.KEY_NUMPAD9:
				return Component.Identifier.Key.NUMPAD9;
			case Keyboard.KEY_SUBTRACT:
				return Component.Identifier.Key.SUBTRACT;
			case Keyboard.KEY_NUMPAD4:
				return Component.Identifier.Key.NUMPAD4;
			case Keyboard.KEY_NUMPAD5:
				return Component.Identifier.Key.NUMPAD5;
			case Keyboard.KEY_NUMPAD6:
				return Component.Identifier.Key.NUMPAD6;
			case Keyboard.KEY_ADD:
				return Component.Identifier.Key.ADD;
			case Keyboard.KEY_NUMPAD1:
				return Component.Identifier.Key.NUMPAD1;
			case Keyboard.KEY_NUMPAD2:
				return Component.Identifier.Key.NUMPAD2;
			case Keyboard.KEY_NUMPAD3:
				return Component.Identifier.Key.NUMPAD3;
			case Keyboard.KEY_NUMPAD0:
				return Component.Identifier.Key.NUMPAD0;
			case Keyboard.KEY_DECIMAL:
				return Component.Identifier.Key.DECIMAL;
			case Keyboard.KEY_F11:
				return Component.Identifier.Key.F11;
			case Keyboard.KEY_F12:
				return Component.Identifier.Key.F12;
			case Keyboard.KEY_F13:
				return Component.Identifier.Key.F13;
			case Keyboard.KEY_F14:
				return Component.Identifier.Key.F14;
			case Keyboard.KEY_F15:
				return Component.Identifier.Key.F15;
			case Keyboard.KEY_KANA:
				return Component.Identifier.Key.KANA;
			case Keyboard.KEY_CONVERT:
				return Component.Identifier.Key.CONVERT;
			case Keyboard.KEY_NOCONVERT:
				return Component.Identifier.Key.NOCONVERT;
			case Keyboard.KEY_YEN:
				return Component.Identifier.Key.YEN;
			case Keyboard.KEY_NUMPADEQUALS:
				return Component.Identifier.Key.NUMPADEQUAL;
			case Keyboard.KEY_CIRCUMFLEX:
				return Component.Identifier.Key.CIRCUMFLEX;
			case Keyboard.KEY_AT:
				return Component.Identifier.Key.AT;
			case Keyboard.KEY_COLON:
				return Component.Identifier.Key.COLON;
			case Keyboard.KEY_UNDERLINE:
				return Component.Identifier.Key.UNDERLINE;
			case Keyboard.KEY_KANJI:
				return Component.Identifier.Key.KANJI;
			case Keyboard.KEY_STOP:
				return Component.Identifier.Key.STOP;
			case Keyboard.KEY_AX:
				return Component.Identifier.Key.AX;
			case Keyboard.KEY_UNLABELED:
				return Component.Identifier.Key.UNLABELED;
			case Keyboard.KEY_NUMPADENTER:
				return Component.Identifier.Key.NUMPADENTER;
			case Keyboard.KEY_RCONTROL:
				return Component.Identifier.Key.RCONTROL;
			case Keyboard.KEY_NUMPADCOMMA:
				return Component.Identifier.Key.NUMPADCOMMA;
			case Keyboard.KEY_DIVIDE:
				return Component.Identifier.Key.DIVIDE;
			case Keyboard.KEY_SYSRQ:
				return Component.Identifier.Key.SYSRQ;
			case Keyboard.KEY_RMENU:
				return Component.Identifier.Key.RALT;
			case Keyboard.KEY_PAUSE:
				return Component.Identifier.Key.PAUSE;
			case Keyboard.KEY_HOME:
				return Component.Identifier.Key.HOME;
			case Keyboard.KEY_UP:
				return Component.Identifier.Key.UP;
			case Keyboard.KEY_PRIOR:
				return Component.Identifier.Key.PAGEUP;
			case Keyboard.KEY_LEFT:
				return Component.Identifier.Key.LEFT;
			case Keyboard.KEY_RIGHT:
				return Component.Identifier.Key.RIGHT;
			case Keyboard.KEY_END:
				return Component.Identifier.Key.END;
			case Keyboard.KEY_DOWN:
				return Component.Identifier.Key.DOWN;
			case Keyboard.KEY_NEXT:
				return Component.Identifier.Key.PAGEDOWN;
			case Keyboard.KEY_INSERT:
				return Component.Identifier.Key.INSERT;
			case Keyboard.KEY_DELETE:
				return Component.Identifier.Key.DELETE;
			case Keyboard.KEY_LMETA:
				return Component.Identifier.Key.LWIN;
			case Keyboard.KEY_RMETA:
				return Component.Identifier.Key.RWIN;
			case Keyboard.KEY_APPS:
				return Component.Identifier.Key.APPS;
			case Keyboard.KEY_POWER:
				return Component.Identifier.Key.POWER;
			case Keyboard.KEY_SLEEP:
				return Component.Identifier.Key.SLEEP;
			default:
				return Component.Identifier.Key.UNKNOWN;
		}
	}
}
