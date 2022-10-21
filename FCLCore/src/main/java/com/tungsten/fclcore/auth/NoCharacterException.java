package com.tungsten.fclcore.auth;

/**
 * This exception gets threw when authenticating a yggdrasil account and there is no valid character.
 * (A account may hold more than one characters.)
 */
public final class NoCharacterException extends AuthenticationException {
    public NoCharacterException() {
    }
}
