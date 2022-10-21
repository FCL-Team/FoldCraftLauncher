package com.tungsten.fclcore.auth;

public abstract class ClassicAccount extends Account {

    /**
     * Login with specified password.
     *
     * When credentials expired, the auth server will ask you to login with password to refresh
     * credentials.
     */
    public abstract AuthInfo logInWithPassword(String password) throws AuthenticationException;
}
