package com.tungsten.fclauncher.keycodes;

import android.view.KeyEvent;

import java.util.Arrays;

public class CharKeycodeMap {

    private static final int[] FCL_KEYCODES = new int[60];
    private static final InputChar[] INPUT_CHARS = new InputChar[60];

    private static int count = 0;

    private static void add(int fclKeycode, InputChar inputChar) {
        FCL_KEYCODES[count] = fclKeycode;
        INPUT_CHARS[count] = inputChar;
        count++;
    }

    public static boolean hasChar(int fclKeycode) {
        int index = Arrays.binarySearch(FCL_KEYCODES, fclKeycode);
        return index >= 0;
    }

    public static char getInputChar(int fclKeycode, KeyEvent event) {
        int index = Arrays.binarySearch(FCL_KEYCODES, fclKeycode);
        if (index >= 0) {
            InputChar inputChar = INPUT_CHARS[index];
            if (inputChar.getCapsChar() == null && inputChar.getSpecialChar() == null) {
                return inputChar.getNormalChar();
            }
            if (inputChar.getCapsChar() != null) {
                return event.isCapsLockOn() ? (event.isShiftPressed() ? inputChar.getNormalChar() : inputChar.getCapsChar()) : (event.isShiftPressed() ? inputChar.getCapsChar() : inputChar.getNormalChar());
            }
            if (inputChar.getSpecialChar() != null) {
                return event.isShiftPressed() ? inputChar.getSpecialChar() : inputChar.getNormalChar();
            }
        }
        throw new IllegalArgumentException("FCL Keycode: " + fclKeycode + " has no paired char!");
    }

    static {
        add(KeyEvent.KEYCODE_0,                              InputChar.KEY_0);
        add(KeyEvent.KEYCODE_1,                              InputChar.KEY_1);
        add(KeyEvent.KEYCODE_2,                              InputChar.KEY_2);
        add(KeyEvent.KEYCODE_3,                              InputChar.KEY_3);
        add(KeyEvent.KEYCODE_4,                              InputChar.KEY_4);
        add(KeyEvent.KEYCODE_5,                              InputChar.KEY_5);
        add(KeyEvent.KEYCODE_6,                              InputChar.KEY_6);
        add(KeyEvent.KEYCODE_7,                              InputChar.KEY_7);
        add(KeyEvent.KEYCODE_8,                              InputChar.KEY_8);
        add(KeyEvent.KEYCODE_9,                              InputChar.KEY_9);

        add(KeyEvent.KEYCODE_NUMPAD_DOT,                     InputChar.KEY_KP_DOT);
        add(KeyEvent.KEYCODE_NUMPAD_COMMA,                   InputChar.KEY_KP_COMMA);

        add(KeyEvent.KEYCODE_NUMPAD_0,                       InputChar.KEY_KP_0);
        add(KeyEvent.KEYCODE_NUMPAD_1,                       InputChar.KEY_KP_1);
        add(KeyEvent.KEYCODE_NUMPAD_2,                       InputChar.KEY_KP_2);
        add(KeyEvent.KEYCODE_NUMPAD_3,                       InputChar.KEY_KP_3);
        add(KeyEvent.KEYCODE_NUMPAD_4,                       InputChar.KEY_KP_4);
        add(KeyEvent.KEYCODE_NUMPAD_5,                       InputChar.KEY_KP_5);
        add(KeyEvent.KEYCODE_NUMPAD_6,                       InputChar.KEY_KP_6);
        add(KeyEvent.KEYCODE_NUMPAD_7,                       InputChar.KEY_KP_7);
        add(KeyEvent.KEYCODE_NUMPAD_8,                       InputChar.KEY_KP_8);
        add(KeyEvent.KEYCODE_NUMPAD_9,                       InputChar.KEY_KP_9);

        add(KeyEvent.KEYCODE_COMMA,                          InputChar.KEY_COMMA);
        add(KeyEvent.KEYCODE_PERIOD,                         InputChar.KEY_PERIOD);
        add(KeyEvent.KEYCODE_GRAVE,                          InputChar.KEY_GRAVE);
        add(KeyEvent.KEYCODE_MINUS,                          InputChar.KEY_MINUS);
        add(KeyEvent.KEYCODE_EQUALS,                         InputChar.KEY_EQUAL);
        add(KeyEvent.KEYCODE_LEFT_BRACKET,                   InputChar.KEY_LEFT_BRACKET);
        add(KeyEvent.KEYCODE_RIGHT_BRACKET,                  InputChar.KEY_RIGHT_BRACKET);
        add(KeyEvent.KEYCODE_BACKSLASH,                      InputChar.KEY_BACK_SLASH);
        add(KeyEvent.KEYCODE_SEMICOLON,                      InputChar.KEY_SEMICOLON);
        add(KeyEvent.KEYCODE_APOSTROPHE,                     InputChar.KEY_APOSTROPHE);
        add(KeyEvent.KEYCODE_SLASH,                          InputChar.KEY_SLASH);

        add(KeyEvent.KEYCODE_SPACE,                          InputChar.KEY_SPACE);

        add(KeyEvent.KEYCODE_A,                              InputChar.KEY_A);
        add(KeyEvent.KEYCODE_B,                              InputChar.KEY_B);
        add(KeyEvent.KEYCODE_C,                              InputChar.KEY_C);
        add(KeyEvent.KEYCODE_D,                              InputChar.KEY_D);
        add(KeyEvent.KEYCODE_E,                              InputChar.KEY_E);
        add(KeyEvent.KEYCODE_F,                              InputChar.KEY_F);
        add(KeyEvent.KEYCODE_G,                              InputChar.KEY_G);
        add(KeyEvent.KEYCODE_H,                              InputChar.KEY_H);
        add(KeyEvent.KEYCODE_I,                              InputChar.KEY_I);
        add(KeyEvent.KEYCODE_J,                              InputChar.KEY_J);
        add(KeyEvent.KEYCODE_K,                              InputChar.KEY_K);
        add(KeyEvent.KEYCODE_L,                              InputChar.KEY_L);
        add(KeyEvent.KEYCODE_M,                              InputChar.KEY_M);
        add(KeyEvent.KEYCODE_N,                              InputChar.KEY_N);
        add(KeyEvent.KEYCODE_O,                              InputChar.KEY_O);
        add(KeyEvent.KEYCODE_P,                              InputChar.KEY_P);
        add(KeyEvent.KEYCODE_Q,                              InputChar.KEY_Q);
        add(KeyEvent.KEYCODE_R,                              InputChar.KEY_R);
        add(KeyEvent.KEYCODE_S,                              InputChar.KEY_S);
        add(KeyEvent.KEYCODE_T,                              InputChar.KEY_T);
        add(KeyEvent.KEYCODE_U,                              InputChar.KEY_U);
        add(KeyEvent.KEYCODE_V,                              InputChar.KEY_V);
        add(KeyEvent.KEYCODE_W,                              InputChar.KEY_W);
        add(KeyEvent.KEYCODE_X,                              InputChar.KEY_X);
        add(KeyEvent.KEYCODE_Y,                              InputChar.KEY_Y);
        add(KeyEvent.KEYCODE_Z,                              InputChar.KEY_Z);
    }

    public static class InputChar {

        public static final InputChar KEY_0                  = new InputChar('0', null, ')');
        public static final InputChar KEY_1                  = new InputChar('1', null, '!');
        public static final InputChar KEY_2                  = new InputChar('2', null, '@');
        public static final InputChar KEY_3                  = new InputChar('3', null, '#');
        public static final InputChar KEY_4                  = new InputChar('4', null, '$');
        public static final InputChar KEY_5                  = new InputChar('5', null, '%');
        public static final InputChar KEY_6                  = new InputChar('6', null, '^');
        public static final InputChar KEY_7                  = new InputChar('7', null, '&');
        public static final InputChar KEY_8                  = new InputChar('8', null, '*');
        public static final InputChar KEY_9                  = new InputChar('9', null, '(');

        public static final InputChar KEY_KP_DOT             = new InputChar('.', null, null);
        public static final InputChar KEY_KP_COMMA           = new InputChar(',', null, null);

        public static final InputChar KEY_KP_0               = new InputChar('0', null, null);
        public static final InputChar KEY_KP_1               = new InputChar('1', null, null);
        public static final InputChar KEY_KP_2               = new InputChar('2', null, null);
        public static final InputChar KEY_KP_3               = new InputChar('3', null, null);
        public static final InputChar KEY_KP_4               = new InputChar('4', null, null);
        public static final InputChar KEY_KP_5               = new InputChar('5', null, null);
        public static final InputChar KEY_KP_6               = new InputChar('6', null, null);
        public static final InputChar KEY_KP_7               = new InputChar('7', null, null);
        public static final InputChar KEY_KP_8               = new InputChar('8', null, null);
        public static final InputChar KEY_KP_9               = new InputChar('9', null, null);

        public static final InputChar KEY_GRAVE              = new InputChar('`', null, '~');
        public static final InputChar KEY_MINUS              = new InputChar('-', null, '_');
        public static final InputChar KEY_EQUAL              = new InputChar('=', null, '+');
        public static final InputChar KEY_LEFT_BRACKET       = new InputChar('[', null, '{');
        public static final InputChar KEY_RIGHT_BRACKET      = new InputChar(']', null, '}');
        public static final InputChar KEY_BACK_SLASH         = new InputChar('\\', null, '|');
        public static final InputChar KEY_SEMICOLON          = new InputChar(';', null, ':');
        public static final InputChar KEY_APOSTROPHE         = new InputChar('\'', null, '"');
        public static final InputChar KEY_COMMA              = new InputChar(',', null, '<');
        public static final InputChar KEY_PERIOD             = new InputChar('.', null, '>');
        public static final InputChar KEY_SLASH              = new InputChar('/', null, '?');

        public static final InputChar KEY_SPACE              = new InputChar(' ', null, null);

        public static final InputChar KEY_A                  = new InputChar('a', 'A', null);
        public static final InputChar KEY_B                  = new InputChar('b', 'B', null);
        public static final InputChar KEY_C                  = new InputChar('c', 'C', null);
        public static final InputChar KEY_D                  = new InputChar('d', 'D', null);
        public static final InputChar KEY_E                  = new InputChar('e', 'E', null);
        public static final InputChar KEY_F                  = new InputChar('f', 'F', null);
        public static final InputChar KEY_G                  = new InputChar('g', 'G', null);
        public static final InputChar KEY_H                  = new InputChar('h', 'H', null);
        public static final InputChar KEY_I                  = new InputChar('i', 'I', null);
        public static final InputChar KEY_J                  = new InputChar('j', 'J', null);
        public static final InputChar KEY_K                  = new InputChar('k', 'K', null);
        public static final InputChar KEY_L                  = new InputChar('l', 'L', null);
        public static final InputChar KEY_M                  = new InputChar('m', 'M', null);
        public static final InputChar KEY_N                  = new InputChar('n', 'N', null);
        public static final InputChar KEY_O                  = new InputChar('o', 'O', null);
        public static final InputChar KEY_P                  = new InputChar('p', 'P', null);
        public static final InputChar KEY_Q                  = new InputChar('q', 'Q', null);
        public static final InputChar KEY_R                  = new InputChar('r', 'R', null);
        public static final InputChar KEY_S                  = new InputChar('s', 'S', null);
        public static final InputChar KEY_T                  = new InputChar('t', 'T', null);
        public static final InputChar KEY_U                  = new InputChar('u', 'U', null);
        public static final InputChar KEY_V                  = new InputChar('v', 'V', null);
        public static final InputChar KEY_W                  = new InputChar('w', 'W', null);
        public static final InputChar KEY_X                  = new InputChar('x', 'X', null);
        public static final InputChar KEY_Y                  = new InputChar('y', 'Y', null);
        public static final InputChar KEY_Z                  = new InputChar('z', 'Z', null);

        private final char normalChar;
        private final Character capsChar;
        private final Character specialChar;

        public InputChar(char normalChar, Character capsChar, Character specialChar) {
            this.normalChar = normalChar;
            this.capsChar = capsChar;
            this.specialChar = specialChar;
        }

        public char getNormalChar() {
            return normalChar;
        }

        public Character getCapsChar() {
            return capsChar;
        }

        public Character getSpecialChar() {
            return specialChar;
        }
    }

}
