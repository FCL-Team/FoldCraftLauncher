package com.tungsten.fclcore.auth;

import java.util.UUID;

public abstract class OAuthAccount extends Account {

    /**
     * Fully login.
     *
     * OAuth server may ask us to do fully login because too frequent action to log in, IP changed,
     * or some other vulnerabilities detected.
     *
     * Difference between logIn & logInWhenCredentialsExpired.
     * logIn only update access token by refresh token, and will not ask user to login by opening the authorization
     * page in web browser.
     * logInWhenCredentialsExpired will open the authorization page in web browser, asking user to select an account
     * (and enter password or PIN if necessary).
     */
    public abstract AuthInfo logInWhenCredentialsExpired() throws AuthenticationException;

    public static class WrongAccountException extends AuthenticationException {
        private final UUID expected;
        private final UUID actual;

        public WrongAccountException(UUID expected, UUID actual) {
            super("Expected account " + expected + ", but found " + actual);
            this.expected = expected;
            this.actual = actual;
        }

        public UUID getExpected() {
            return expected;
        }

        public UUID getActual() {
            return actual;
        }
    }
}
