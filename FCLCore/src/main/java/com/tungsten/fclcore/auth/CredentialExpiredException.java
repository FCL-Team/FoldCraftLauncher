package com.tungsten.fclcore.auth;

/**
 * Thrown when the stored credentials has expired.
 * This exception indicates that a password login should be performed.
 * @see Account#logIn()
 */
public class CredentialExpiredException extends AuthenticationException {

    public CredentialExpiredException() {}

    public CredentialExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public CredentialExpiredException(String message) {
        super(message);
    }

    public CredentialExpiredException(Throwable cause) {
        super(cause);
    }
}
