package com.tungsten.fclcore.util.gson;

public @interface JsonSubtype {
    Class<?> clazz();

    String name();
}
