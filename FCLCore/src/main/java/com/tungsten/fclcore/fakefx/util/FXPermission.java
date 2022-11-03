package com.tungsten.fclcore.fakefx.util;

import java.security.BasicPermission;

public final class FXPermission extends BasicPermission {

    private static final long serialVersionUID = 2890556410764946054L;

    /**
     * Creates a new {@code FXPermission} with the specified name.
     * The name is the symbolic name of the {@code FXPermission},
     * such as "accessClipboard", "createTransparentWindow ", etc. An asterisk
     * may be used to indicate all JavaFX permissions.
     *
     * @param name the name of the FXPermission
     *
     * @throws NullPointerException if {@code name} is {@code null}.
     * @throws IllegalArgumentException if {@code name} is empty.
     */
    public FXPermission(String name) {
        super(name);
    }

}
