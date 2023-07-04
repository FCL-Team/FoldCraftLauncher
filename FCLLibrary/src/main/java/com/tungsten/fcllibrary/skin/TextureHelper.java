package com.tungsten.fcllibrary.skin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

public class TextureHelper {
    public static boolean isLoadingError;
    
    static {
        TextureHelper.isLoadingError = false;
    }
    
    public static int[] loadGLTextureFromBitmap(final Bitmap skin, final Bitmap cape, final GL10 gl10) {
        // Log.i("loadTex", "start");
        final int[] array = new int[2];
        gl10.glGenTextures(2, array, 0);
        gl10.glBindTexture(3553, array[0]);
        gl10.glTexParameterf(3553, 10241, 9728.0f);
        gl10.glTexParameterf(3553, 10240, 9728.0f);
        GLUtils.texImage2D(3553, 0, skin, 0);
        gl10.glBindTexture(3553, array[1]);
        gl10.glTexParameterf(3553, 10241, 9728.0f);
        gl10.glTexParameterf(3553, 10240, 9728.0f);
        if (cape != null) {
            GLUtils.texImage2D(3553, 0, cape, 0);
        }
        if (array[0] == 0 || (cape != null && array[1] == 0)) {
            throw new RuntimeException("Error loading texture.");
        } else {
            Log.i("loadTex", "success");
        }
        return array;
    }
    
    public static int[] loadTexture(final Context context, final int n) {
        TextureHelper.isLoadingError = false;
        final int[] array = new int[2];
        GLES20.glGenTextures(1, array, 0);
        if (array[0] != 0) {
            final BitmapFactory.Options bitmapFactory = new BitmapFactory.Options();
            bitmapFactory.inScaled = false;
            final Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), n, bitmapFactory);
            GLES20.glBindTexture(3553, array[0]);
            GLES20.glTexParameteri(3553, 10241, 9728);
            GLES20.glTexParameterf(3553, 10240, 9728.0f);
            GLUtils.texImage2D(3553, 0, decodeResource, 0);
            decodeResource.recycle();
        }
        if (array[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        return array;
    }
    
    public static int[] loadTexture(final Bitmap bitmap) {
        TextureHelper.isLoadingError = false;
        final int[] array = new int[2];
        GLES20.glGenTextures(1, array, 0);
        if (array[0] != 0) {
            new BitmapFactory.Options().inScaled = false;
            GLES20.glBindTexture(3553, array[0]);
            GLES20.glTexParameteri(3553, 10241, 9728);
            GLES20.glTexParameterf(3553, 10240, 9728.0f);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            bitmap.recycle();
        }
        if (array[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        return array;
    }

}
