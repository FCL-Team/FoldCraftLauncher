package com.tungsten.fclcore.download.game;

import com.tungsten.fclcore.game.Library;

import org.jetbrains.annotations.NotNull;

public class LibraryDownloadException extends Exception {
    private final Library library;

    public LibraryDownloadException(Library library, @NotNull Throwable cause) {
        super("Unable to download library " + library, cause);

        this.library = library;
    }

    public Library getLibrary() {
        return library;
    }
}
