/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTTextureInteger {

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_RGBA_INTEGER_MODE_EXT = 0x8D9E;

	/**
	 *  Accepted by the &lt;internalFormat&gt; parameter of TexImage1D,
	 *  TexImage2D, and TexImage3D:
	 */
	public static final int GL_RGBA32UI_EXT = 0x8D70,
		GL_RGB32UI_EXT = 0x8D71,
		GL_ALPHA32UI_EXT = 0x8D72,
		GL_INTENSITY32UI_EXT = 0x8D73,
		GL_LUMINANCE32UI_EXT = 0x8D74,
		GL_LUMINANCE_ALPHA32UI_EXT = 0x8D75,
		GL_RGBA16UI_EXT = 0x8D76,
		GL_RGB16UI_EXT = 0x8D77,
		GL_ALPHA16UI_EXT = 0x8D78,
		GL_INTENSITY16UI_EXT = 0x8D79,
		GL_LUMINANCE16UI_EXT = 0x8D7A,
		GL_LUMINANCE_ALPHA16UI_EXT = 0x8D7B,
		GL_RGBA8UI_EXT = 0x8D7C,
		GL_RGB8UI_EXT = 0x8D7D,
		GL_ALPHA8UI_EXT = 0x8D7E,
		GL_INTENSITY8UI_EXT = 0x8D7F,
		GL_LUMINANCE8UI_EXT = 0x8D80,
		GL_LUMINANCE_ALPHA8UI_EXT = 0x8D81,
		GL_RGBA32I_EXT = 0x8D82,
		GL_RGB32I_EXT = 0x8D83,
		GL_ALPHA32I_EXT = 0x8D84,
		GL_INTENSITY32I_EXT = 0x8D85,
		GL_LUMINANCE32I_EXT = 0x8D86,
		GL_LUMINANCE_ALPHA32I_EXT = 0x8D87,
		GL_RGBA16I_EXT = 0x8D88,
		GL_RGB16I_EXT = 0x8D89,
		GL_ALPHA16I_EXT = 0x8D8A,
		GL_INTENSITY16I_EXT = 0x8D8B,
		GL_LUMINANCE16I_EXT = 0x8D8C,
		GL_LUMINANCE_ALPHA16I_EXT = 0x8D8D,
		GL_RGBA8I_EXT = 0x8D8E,
		GL_RGB8I_EXT = 0x8D8F,
		GL_ALPHA8I_EXT = 0x8D90,
		GL_INTENSITY8I_EXT = 0x8D91,
		GL_LUMINANCE8I_EXT = 0x8D92,
		GL_LUMINANCE_ALPHA8I_EXT = 0x8D93;

	/**
	 *  Accepted by the &lt;format&gt; parameter of TexImage1D, TexImage2D,
	 *  TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D,
	 *  DrawPixels and ReadPixels:
	 */
	public static final int GL_RED_INTEGER_EXT = 0x8D94,
		GL_GREEN_INTEGER_EXT = 0x8D95,
		GL_BLUE_INTEGER_EXT = 0x8D96,
		GL_ALPHA_INTEGER_EXT = 0x8D97,
		GL_RGB_INTEGER_EXT = 0x8D98,
		GL_RGBA_INTEGER_EXT = 0x8D99,
		GL_BGR_INTEGER_EXT = 0x8D9A,
		GL_BGRA_INTEGER_EXT = 0x8D9B,
		GL_LUMINANCE_INTEGER_EXT = 0x8D9C,
		GL_LUMINANCE_ALPHA_INTEGER_EXT = 0x8D9D;

	private EXTTextureInteger() {}

	public static void glClearColorIiEXT(int r, int g, int b, int a) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearColorIiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearColorIiEXT(r, g, b, a, function_pointer);
	}
	static native void nglClearColorIiEXT(int r, int g, int b, int a, long function_pointer);

	public static void glClearColorIuiEXT(int r, int g, int b, int a) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearColorIuiEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearColorIuiEXT(r, g, b, a, function_pointer);
	}
	static native void nglClearColorIuiEXT(int r, int g, int b, int a, long function_pointer);

	public static void glTexParameterIEXT(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterIivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexParameterIivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexParameterIivEXT(int target, int pname, long params, long function_pointer);

	/** Overloads glTexParameterIivEXT. */
	public static void glTexParameterIiEXT(int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterIivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexParameterIivEXT(target, pname, APIUtil.getInt(caps, param), function_pointer);
	}

	public static void glTexParameterIuEXT(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterIuivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglTexParameterIuivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglTexParameterIuivEXT(int target, int pname, long params, long function_pointer);

	/** Overloads glTexParameterIuivEXT. */
	public static void glTexParameterIuiEXT(int target, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexParameterIuivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexParameterIuivEXT(target, pname, APIUtil.getInt(caps, param), function_pointer);
	}

	public static void glGetTexParameterIEXT(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterIivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexParameterIivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexParameterIivEXT(int target, int pname, long params, long function_pointer);

	/** Overloads glGetTexParameterIivEXT. */
	public static int glGetTexParameterIiEXT(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterIivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTexParameterIivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetTexParameterIuEXT(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterIuivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetTexParameterIuivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetTexParameterIuivEXT(int target, int pname, long params, long function_pointer);

	/** Overloads glGetTexParameterIuivEXT. */
	public static int glGetTexParameterIuiEXT(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTexParameterIuivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetTexParameterIuivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
