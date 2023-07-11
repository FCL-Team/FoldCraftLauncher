/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBGetTextureSubImage {

	private ARBGetTextureSubImage() {}

	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
		GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, DoubleBuffer pixels) {
		GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer pixels) {
		GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer pixels) {
		GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer pixels) {
		GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
	}
	public static void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
		GL45.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels_bufSize, pixels_buffer_offset);
	}

	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, ByteBuffer pixels) {
		GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, DoubleBuffer pixels) {
		GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, FloatBuffer pixels) {
		GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, IntBuffer pixels) {
		GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, ShortBuffer pixels) {
		GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels);
	}
	public static void glGetCompressedTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int pixels_bufSize, long pixels_buffer_offset) {
		GL45.glGetCompressedTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, pixels_bufSize, pixels_buffer_offset);
	}
}
