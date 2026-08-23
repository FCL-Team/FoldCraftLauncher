package com.mio.util;
/*
 * Copyright (c) 1998, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import android.util.Log;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class ParseUtil {
    public static boolean isValidCharacters(String s) {
        try {
            File file = new File(s);
            String s1 = normalizeString(file.toURI().toASCIIString());
            String s2 = normalizeString(fileToEncodedURL(file).toString());
            return s1.equals(s2);
        } catch (MalformedURLException ignore) {
        }
        return false;
    }

    private static String normalizeString(String s) {
        String result = s.toLowerCase(Locale.ROOT);
        if (result.startsWith("file://")) {
            result = result.replace("file://", "");
        } else if (result.startsWith("file:")) {
            result = result.replace("file:", "");
        }
        return result;
    }

    public static URL fileToEncodedURL(File file)
            throws MalformedURLException {
        String path = file.getAbsolutePath();
        path = encodePath(path);
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/") && file.isDirectory()) {
            path = path + "/";
        }
        return new URL("file", "", path);
    }

    public static String encodePath(String path) {
        return encodePath(path, true);
    }

    public static String encodePath(String path, boolean flag) {
        if (flag && File.separatorChar != '/') {
            return encodePath(path, 0, File.separatorChar);
        } else {
            int index = firstEncodeIndex(path);
            if (index > -1) {
                return encodePath(path, index, '/');
            } else {
                return path;
            }
        }
    }

    private static int firstEncodeIndex(String path) {
        int len = path.length();
        for (int i = 0; i < len; i++) {
            char c = path.charAt(i);
            // Ordering in the following test is performance sensitive,
            // and typically paths have most chars in the a-z range, then
            // in the symbol range '&'-':' (includes '.', '/' and '0'-'9')
            // and more rarely in the A-Z range.
            if (c >= 'a' && c <= 'z' ||
                    c >= '&' && c <= ':' ||
                    c >= 'A' && c <= 'Z') {
                continue;
            } else if (c > 0x007F || match(c, L_ENCODED, H_ENCODED)) {
                return i;
            }
        }
        return -1;
    }

    private static String encodePath(String path, int index, char sep) {
        char[] pathCC = path.toCharArray();
        char[] retCC = new char[pathCC.length * 2 + 16 - index];
        if (index > 0) {
            System.arraycopy(pathCC, 0, retCC, 0, index);
        }
        int retLen = index;

        for (int i = index; i < pathCC.length; i++) {
            char c = pathCC[i];
            if (c == sep)
                retCC[retLen++] = '/';
            else {
                if (c <= 0x007F) {
                    if (c >= 'a' && c <= 'z' ||
                            c >= 'A' && c <= 'Z' ||
                            c >= '0' && c <= '9') {
                        retCC[retLen++] = c;
                    } else if (match(c, L_ENCODED, H_ENCODED)) {
                        retLen = escape(retCC, c, retLen);
                    } else {
                        retCC[retLen++] = c;
                    }
                } else if (c > 0x07FF) {
                    retLen = escape(retCC, (char) (0xE0 | ((c >> 12) & 0x0F)), retLen);
                    retLen = escape(retCC, (char) (0x80 | ((c >> 6) & 0x3F)), retLen);
                    retLen = escape(retCC, (char) (0x80 | ((c >> 0) & 0x3F)), retLen);
                } else {
                    retLen = escape(retCC, (char) (0xC0 | ((c >> 6) & 0x1F)), retLen);
                    retLen = escape(retCC, (char) (0x80 | ((c >> 0) & 0x3F)), retLen);
                }
            }
            //worst case scenario for character [0x7ff-] every single
            //character will be encoded into 9 characters.
            if (retLen + 9 > retCC.length) {
                int newLen = retCC.length * 2 + 16;
                if (newLen < 0) {
                    newLen = Integer.MAX_VALUE;
                }
                char[] buf = new char[newLen];
                System.arraycopy(retCC, 0, buf, 0, retLen);
                retCC = buf;
            }
        }
        return new String(retCC, 0, retLen);
    }

    private static int escape(char[] cc, char c, int index) {
        cc[index++] = '%';
        cc[index++] = Character.forDigit((c >> 4) & 0xF, 16);
        cc[index++] = Character.forDigit(c & 0xF, 16);
        return index;
    }

    private static boolean match(char c, long lowMask, long highMask) {
        if (c < 64)
            return ((1L << c) & lowMask) != 0;
        if (c < 128)
            return ((1L << (c - 64)) & highMask) != 0;
        return false;
    }

    // lowMask((char)0, (char)31) | lowMask("=;?/# <>%\"{}|\\^[]`");
    private static final long L_ENCODED = 0xF800802DFFFFFFFFL;

    // highMask((char)0x7F, (char)0x7F) | highMask("=;?/# <>%\"{}|\\^[]`");
    private static final long H_ENCODED = 0xB800000178000000L;
}

