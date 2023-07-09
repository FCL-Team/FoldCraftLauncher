package com.tungsten.fclcore.util.skin;

import android.graphics.Bitmap;

/**
 * Describes a Minecraft 1.8+ skin (64x64).
 * Old format skins are converted to the new format.
 */
public class NormalizedSkin {

    private static void copyImage(Bitmap src, Bitmap dst, int sx, int sy, int dx, int dy, int w, int h, boolean flipHorizontal) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = src.getPixel(sx + x, sy + y);
                dst.setPixel(dx + (flipHorizontal ? w - x - 1 : x), dy + y, pixel);
            }
        }
    }

    private final Bitmap texture;
    private final Bitmap normalizedTexture;
    private final int scale;
    private final boolean oldFormat;

    public NormalizedSkin(Bitmap texture) throws InvalidSkinException {
        this.texture = texture;

        // check format
        int w = (int) texture.getWidth();
        int h = (int) texture.getHeight();
        if (w % 64 != 0) {
            throw new InvalidSkinException("Invalid size " + w + "x" + h);
        }
        if (w == h) {
            oldFormat = false;
        } else if (w == h * 2) {
            oldFormat = true;
        } else {
            throw new InvalidSkinException("Invalid size " + w + "x" + h);
        }

        // compute scale
        scale = w / 64;

        normalizedTexture = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
        copyImage(texture, normalizedTexture, 0, 0, 0, 0, w, h, false);
        if (oldFormat) {
            convertOldSkin();
        }
    }

    private void convertOldSkin() {
        copyImageRelative(4, 16, 20, 48, 4, 4, true); // Top Leg
        copyImageRelative(8, 16, 24, 48, 4, 4, true); // Bottom Leg
        copyImageRelative(0, 20, 24, 52, 4, 12, true); // Outer Leg
        copyImageRelative(4, 20, 20, 52, 4, 12, true); // Front Leg
        copyImageRelative(8, 20, 16, 52, 4, 12, true); // Inner Leg
        copyImageRelative(12, 20, 28, 52, 4, 12, true); // Back Leg
        copyImageRelative(44, 16, 36, 48, 4, 4, true); // Top Arm
        copyImageRelative(48, 16, 40, 48, 4, 4, true); // Bottom Arm
        copyImageRelative(40, 20, 40, 52, 4, 12, true); // Outer Arm
        copyImageRelative(44, 20, 36, 52, 4, 12, true); // Front Arm
        copyImageRelative(48, 20, 32, 52, 4, 12, true); // Inner Arm
        copyImageRelative(52, 20, 44, 52, 4, 12, true); // Back Arm
    }

    private void copyImageRelative(int sx, int sy, int dx, int dy, int w, int h, boolean flipHorizontal) {
        copyImage(normalizedTexture, normalizedTexture, sx * scale, sy * scale, dx * scale, dy * scale, w * scale, h * scale, flipHorizontal);
    }

    public Bitmap getOriginalTexture() {
        return texture;
    }

    public Bitmap getNormalizedTexture() {
        return normalizedTexture;
    }

    public int getScale() {
        return scale;
    }

    public boolean isOldFormat() {
        return oldFormat;
    }

    public boolean isSlim() {
        return (hasTransparencyRelative(50, 16, 2, 4) ||
                hasTransparencyRelative(54, 20, 2, 12) ||
                hasTransparencyRelative(42, 48, 2, 4) ||
                hasTransparencyRelative(46, 52, 2, 12)) ||
                (isAreaBlackRelative(50, 16, 2, 4) &&
                        isAreaBlackRelative(54, 20, 2, 12) &&
                        isAreaBlackRelative(42, 48, 2, 4) &&
                        isAreaBlackRelative(46, 52, 2, 12));
    }

    private boolean hasTransparencyRelative(int x0, int y0, int w, int h) {
        x0 *= scale;
        y0 *= scale;
        w *= scale;
        h *= scale;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = normalizedTexture.getPixel(x0 + x, y0 + y);
                if (pixel >>> 24 != 0xff) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAreaBlackRelative(int x0, int y0, int w, int h) {
        x0 *= scale;
        y0 *= scale;
        w *= scale;
        h *= scale;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = normalizedTexture.getPixel(x0 + x, y0 + y);
                if (pixel != 0xff000000) {
                    return false;
                }
            }
        }
        return true;
    }
}
