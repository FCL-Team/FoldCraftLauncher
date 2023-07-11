/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBMultitexture {

	public static final int GL_TEXTURE0_ARB = 0x84C0,
		GL_TEXTURE1_ARB = 0x84C1,
		GL_TEXTURE2_ARB = 0x84C2,
		GL_TEXTURE3_ARB = 0x84C3,
		GL_TEXTURE4_ARB = 0x84C4,
		GL_TEXTURE5_ARB = 0x84C5,
		GL_TEXTURE6_ARB = 0x84C6,
		GL_TEXTURE7_ARB = 0x84C7,
		GL_TEXTURE8_ARB = 0x84C8,
		GL_TEXTURE9_ARB = 0x84C9,
		GL_TEXTURE10_ARB = 0x84CA,
		GL_TEXTURE11_ARB = 0x84CB,
		GL_TEXTURE12_ARB = 0x84CC,
		GL_TEXTURE13_ARB = 0x84CD,
		GL_TEXTURE14_ARB = 0x84CE,
		GL_TEXTURE15_ARB = 0x84CF,
		GL_TEXTURE16_ARB = 0x84D0,
		GL_TEXTURE17_ARB = 0x84D1,
		GL_TEXTURE18_ARB = 0x84D2,
		GL_TEXTURE19_ARB = 0x84D3,
		GL_TEXTURE20_ARB = 0x84D4,
		GL_TEXTURE21_ARB = 0x84D5,
		GL_TEXTURE22_ARB = 0x84D6,
		GL_TEXTURE23_ARB = 0x84D7,
		GL_TEXTURE24_ARB = 0x84D8,
		GL_TEXTURE25_ARB = 0x84D9,
		GL_TEXTURE26_ARB = 0x84DA,
		GL_TEXTURE27_ARB = 0x84DB,
		GL_TEXTURE28_ARB = 0x84DC,
		GL_TEXTURE29_ARB = 0x84DD,
		GL_TEXTURE30_ARB = 0x84DE,
		GL_TEXTURE31_ARB = 0x84DF,
		GL_ACTIVE_TEXTURE_ARB = 0x84E0,
		GL_CLIENT_ACTIVE_TEXTURE_ARB = 0x84E1,
		GL_MAX_TEXTURE_UNITS_ARB = 0x84E2;

	private ARBMultitexture() {}

	public static void glClientActiveTextureARB(int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClientActiveTextureARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClientActiveTextureARB(texture, function_pointer);
	}
	static native void nglClientActiveTextureARB(int texture, long function_pointer);

	public static void glActiveTextureARB(int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glActiveTextureARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglActiveTextureARB(texture, function_pointer);
	}
	static native void nglActiveTextureARB(int texture, long function_pointer);

	public static void glMultiTexCoord1fARB(int target, float s) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord1fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord1fARB(target, s, function_pointer);
	}
	static native void nglMultiTexCoord1fARB(int target, float s, long function_pointer);

	public static void glMultiTexCoord1dARB(int target, double s) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord1dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord1dARB(target, s, function_pointer);
	}
	static native void nglMultiTexCoord1dARB(int target, double s, long function_pointer);

	public static void glMultiTexCoord1iARB(int target, int s) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord1iARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord1iARB(target, s, function_pointer);
	}
	static native void nglMultiTexCoord1iARB(int target, int s, long function_pointer);

	public static void glMultiTexCoord1sARB(int target, short s) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord1sARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord1sARB(target, s, function_pointer);
	}
	static native void nglMultiTexCoord1sARB(int target, short s, long function_pointer);

	public static void glMultiTexCoord2fARB(int target, float s, float t) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord2fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord2fARB(target, s, t, function_pointer);
	}
	static native void nglMultiTexCoord2fARB(int target, float s, float t, long function_pointer);

	public static void glMultiTexCoord2dARB(int target, double s, double t) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord2dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord2dARB(target, s, t, function_pointer);
	}
	static native void nglMultiTexCoord2dARB(int target, double s, double t, long function_pointer);

	public static void glMultiTexCoord2iARB(int target, int s, int t) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord2iARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord2iARB(target, s, t, function_pointer);
	}
	static native void nglMultiTexCoord2iARB(int target, int s, int t, long function_pointer);

	public static void glMultiTexCoord2sARB(int target, short s, short t) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord2sARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord2sARB(target, s, t, function_pointer);
	}
	static native void nglMultiTexCoord2sARB(int target, short s, short t, long function_pointer);

	public static void glMultiTexCoord3fARB(int target, float s, float t, float r) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord3fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord3fARB(target, s, t, r, function_pointer);
	}
	static native void nglMultiTexCoord3fARB(int target, float s, float t, float r, long function_pointer);

	public static void glMultiTexCoord3dARB(int target, double s, double t, double r) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord3dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord3dARB(target, s, t, r, function_pointer);
	}
	static native void nglMultiTexCoord3dARB(int target, double s, double t, double r, long function_pointer);

	public static void glMultiTexCoord3iARB(int target, int s, int t, int r) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord3iARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord3iARB(target, s, t, r, function_pointer);
	}
	static native void nglMultiTexCoord3iARB(int target, int s, int t, int r, long function_pointer);

	public static void glMultiTexCoord3sARB(int target, short s, short t, short r) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord3sARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord3sARB(target, s, t, r, function_pointer);
	}
	static native void nglMultiTexCoord3sARB(int target, short s, short t, short r, long function_pointer);

	public static void glMultiTexCoord4fARB(int target, float s, float t, float r, float q) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord4fARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord4fARB(target, s, t, r, q, function_pointer);
	}
	static native void nglMultiTexCoord4fARB(int target, float s, float t, float r, float q, long function_pointer);

	public static void glMultiTexCoord4dARB(int target, double s, double t, double r, double q) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord4dARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord4dARB(target, s, t, r, q, function_pointer);
	}
	static native void nglMultiTexCoord4dARB(int target, double s, double t, double r, double q, long function_pointer);

	public static void glMultiTexCoord4iARB(int target, int s, int t, int r, int q) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord4iARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord4iARB(target, s, t, r, q, function_pointer);
	}
	static native void nglMultiTexCoord4iARB(int target, int s, int t, int r, int q, long function_pointer);

	public static void glMultiTexCoord4sARB(int target, short s, short t, short r, short q) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoord4sARB;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoord4sARB(target, s, t, r, q, function_pointer);
	}
	static native void nglMultiTexCoord4sARB(int target, short s, short t, short r, short q, long function_pointer);
}
