package com.tungsten.fclcore.mod;

public class ModpackCompletionException extends Exception {
    public ModpackCompletionException() {
    }

    public ModpackCompletionException(String message) {
        super(message);
    }

    public ModpackCompletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModpackCompletionException(Throwable cause) {
        super(cause);
    }
}
