package com.tungsten.fclcore.mod;

public class UnsupportedModpackException extends Exception {
    public UnsupportedModpackException() {
    }

    public UnsupportedModpackException(String message) {
        super(message);
    }

    public UnsupportedModpackException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedModpackException(Throwable cause) {
        super(cause);
    }
}
