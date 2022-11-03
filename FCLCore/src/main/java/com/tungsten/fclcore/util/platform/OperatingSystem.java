package com.tungsten.fclcore.util.platform;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public enum OperatingSystem {
    /**
     * Microsoft Windows.
     */
    WINDOWS("windows"),
    /**
     * Linux and Unix like OS, including Solaris.
     */
    LINUX("linux"),
    /**
     * Mac OS X.
     */
    OSX("osx"),
    /**
     * Unknown operating system.
     */
    UNKNOWN("universal");

    private final String checkedName;

    OperatingSystem(String checkedName) {
        this.checkedName = checkedName;
    }

    public String getCheckedName() {
        return checkedName;
    }

    public static final Charset NATIVE_CHARSET;

    static {
        String nativeEncoding = System.getProperty("native.encoding");
        String hmclNativeEncoding = System.getProperty("fcl.native.encoding");
        Charset nativeCharset = Charset.defaultCharset();

        try {
            if (hmclNativeEncoding != null) {
                nativeCharset = Charset.forName(hmclNativeEncoding);
            } else {
                if (nativeEncoding != null && !nativeEncoding.equalsIgnoreCase(nativeCharset.name())) {
                    nativeCharset = Charset.forName(nativeEncoding);
                }

                if (nativeCharset == StandardCharsets.UTF_8 || nativeCharset == StandardCharsets.US_ASCII) {
                    nativeCharset = StandardCharsets.UTF_8;
                } else if ("GBK".equalsIgnoreCase(nativeCharset.name()) || "GB2312".equalsIgnoreCase(nativeCharset.name())) {
                    nativeCharset = Charset.forName("GB18030");
                }
            }
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        }
        NATIVE_CHARSET = nativeCharset;
    }

    public static boolean isNameValid(String name) {
        // empty filename is not allowed
        if (name.isEmpty())
            return false;
        // . and .. have special meaning on all platforms
        if (name.equals("."))
            return false;
        // \0 and / are forbidden on all platforms
        if (name.indexOf('/') != -1 || name.indexOf('\0') != -1)
            return false;

        return true;
    }
}