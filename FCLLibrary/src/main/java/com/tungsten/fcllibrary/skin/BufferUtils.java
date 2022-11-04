package com.tungsten.fcllibrary.skin;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class BufferUtils {
    public static float d2r(final float n) {
        return 3.1415927f * n / 180.0f;
    }
    
    static FloatBuffer toFloatBuffer(final float[] array) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(array.length * 4);
        allocateDirect.order(ByteOrder.nativeOrder());
        final FloatBuffer floatBuffer = allocateDirect.asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        return floatBuffer;
    }
    
    static ShortBuffer toShortBuffer(final short[] array) {
        final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(array.length * 2);
        allocateDirect.order(ByteOrder.nativeOrder());
        final ShortBuffer shortBuffer = allocateDirect.asShortBuffer();
        shortBuffer.put(array);
        shortBuffer.position(0);
        return shortBuffer;
    }
}
