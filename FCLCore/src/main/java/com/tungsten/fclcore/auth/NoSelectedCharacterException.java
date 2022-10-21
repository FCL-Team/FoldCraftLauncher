package com.tungsten.fclcore.auth;

/**
 * This exception gets threw when a monitor of {@link CharacterSelector} cannot select a
 * valid character.
 *
 * @see CharacterSelector
 */
public final class NoSelectedCharacterException extends AuthenticationException {
    public NoSelectedCharacterException() {
    }
}
