package com.tungsten.fclcore.util.gson;

import com.google.gson.JsonParseException;

public interface Validation {

    /**
     * 1. Check some non-null fields and;
     * 2. Check strings and;
     * 3. Check generic type of lists &lt;T&gt; and maps &lt;K, V&gt; are correct.
     *
     * Will be called immediately after initialization.
     * Throw an exception when values are malformed.
     *
     * @throws JsonParseException if fields are filled in wrong format or wrong type.
     * @throws TolerableValidationException if we want to replace this object with null (i.e. the object does not fulfill the constraints).
     */
    void validate() throws JsonParseException, TolerableValidationException;

    static void requireNonNull(Object object, String message) throws JsonParseException {
        if (object == null)
            throw new JsonParseException(message);
    }
}
