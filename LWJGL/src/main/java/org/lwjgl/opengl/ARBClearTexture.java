/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBClearTexture {

	/**
	 *  Accepted by the &lt;pname&gt; parameter for GetInternalformativ and
	 *  GetInternalformati64v:
	 */
	public static final int GL_CLEAR_TEXTURE = 0x9365;

	private ARBClearTexture() {}

	public static void glClearTexImage(int texture, int level, int format, int type, ByteBuffer data) {
		GL44.glClearTexImage(texture, level, format, type, data);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, DoubleBuffer data) {
		GL44.glClearTexImage(texture, level, format, type, data);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, FloatBuffer data) {
		GL44.glClearTexImage(texture, level, format, type, data);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, IntBuffer data) {
		GL44.glClearTexImage(texture, level, format, type, data);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, ShortBuffer data) {
		GL44.glClearTexImage(texture, level, format, type, data);
	}
	public static void glClearTexImage(int texture, int level, int format, int type, LongBuffer data) {
		GL44.glClearTexImage(texture, level, format, type, data);
	}

	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data) {
		GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer data) {
		GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer data) {
		GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer data) {
		GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer data) {
		GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
	}
	public static void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, LongBuffer data) {
		GL44.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
	}
}
