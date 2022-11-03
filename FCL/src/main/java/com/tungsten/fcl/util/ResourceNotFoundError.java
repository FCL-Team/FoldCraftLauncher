package com.tungsten.fcl.util;

import java.io.InputStream;

public class ResourceNotFoundError extends Error {
    public ResourceNotFoundError(String message) {
        super(message);
    }

    public ResourceNotFoundError(String message, Throwable cause) {
        super(message, cause);
    }

    public static InputStream getResourceAsStream(String url) {
        InputStream stream = ResourceNotFoundError.class.getResourceAsStream(url);
        if (stream == null)
            throw new ResourceNotFoundError("Resource not found: " + url);
        return stream;
    }
}
