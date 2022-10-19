package com.tungsten.fclcore.util;

import java.io.IOException;

public final class Hex {

    private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static byte[] decodeHex(String str) throws IOException {
        char[] data = str.toCharArray();
        int len = data.length;

        if ((len & 0x1) != 0)
            throw new IOException("Odd number of characters.");

        byte[] out = new byte[len >> 1];

        int i = 0;
        for (int j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    public static String encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];

        int i = 0;
        for (int j = 0; i < l; i++) {
            out[(j++)] = DIGITS_LOWER[((0xF0 & data[i]) >>> 4)];
            out[(j++)] = DIGITS_LOWER[(0xF & data[i])];
        }
        return new String(out);
    }

    private static int toDigit(char ch, int index) throws IOException {
        int digit = Character.digit(ch, 16);
        if (digit == -1)
            throw new IOException("Illegal hexadecimal character " + ch + " at index " + index);
        return digit;
    }

    private Hex() {}
}
