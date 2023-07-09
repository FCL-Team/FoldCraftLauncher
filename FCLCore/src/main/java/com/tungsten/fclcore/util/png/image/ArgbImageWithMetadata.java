package com.tungsten.fclcore.util.png.image;

import com.tungsten.fclcore.util.png.PNGMetadata;

final class ArgbImageWithMetadata implements ArgbImage {
    private final ArgbImage source;
    private final PNGMetadata metadata;

    ArgbImageWithMetadata(ArgbImage source, PNGMetadata metadata) {
        this.source = source;
        this.metadata = metadata;
    }

    @Override
    public int getWidth() {
        return source.getWidth();
    }

    @Override
    public int getHeight() {
        return source.getHeight();
    }

    @Override
    public int getArgb(int x, int y) {
        return source.getArgb(x, y);
    }

    @Override
    public PNGMetadata getMetadata() {
        return metadata;
    }

    @Override
    public ArgbImage withMetadata(PNGMetadata metadata) {
        return new ArgbImageWithMetadata(source, metadata);
    }
}