package com.tungsten.fclcore.auth.authlibinjector;

import com.tungsten.fclcore.auth.AuthenticationException;

public class AuthlibInjectorDownloadException extends AuthenticationException {

    public AuthlibInjectorDownloadException() {
    }

    public AuthlibInjectorDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthlibInjectorDownloadException(String message) {
        super(message);
    }

    public AuthlibInjectorDownloadException(Throwable cause) {
        super(cause);
    }
}
