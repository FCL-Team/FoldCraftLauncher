package com.tungsten.fclcore.mod;

public class MismatchedModpackTypeException extends Exception {
    private final String required;
    private final String found;

    public MismatchedModpackTypeException(String required, String found) {
        super("Required " + required + ", but found " + found);

        this.required = required;
        this.found = found;
    }

    public String getRequired() {
        return required;
    }

    public String getFound() {
        return found;
    }
}
