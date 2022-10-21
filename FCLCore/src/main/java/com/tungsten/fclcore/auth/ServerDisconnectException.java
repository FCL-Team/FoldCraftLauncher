package com.tungsten.fclcore.auth;

public class ServerDisconnectException extends AuthenticationException {
    public ServerDisconnectException() {
    }

    public ServerDisconnectException(String message) {
        super(message);
    }

    public ServerDisconnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerDisconnectException(Throwable cause) {
        super(cause);
    }
}
