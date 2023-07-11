/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBFramebufferNoAttachments {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of FramebufferParameteri,
	 *  GetFramebufferParameteriv, NamedFramebufferParameteriEXT, and
	 *  GetNamedFramebufferParameterivEXT:
	 */
	public static final int GL_FRAMEBUFFER_DEFAULT_WIDTH = 0x9310,
		GL_FRAMEBUFFER_DEFAULT_HEIGHT = 0x9311,
		GL_FRAMEBUFFER_DEFAULT_LAYERS = 0x9312,
		GL_FRAMEBUFFER_DEFAULT_SAMPLES = 0x9313,
		GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 0x9314;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_FRAMEBUFFER_WIDTH = 0x9315,
		GL_MAX_FRAMEBUFFER_HEIGHT = 0x9316,
		GL_MAX_FRAMEBUFFER_LAYERS = 0x9317,
		GL_MAX_FRAMEBUFFER_SAMPLES = 0x9318;

	private ARBFramebufferNoAttachments() {}

	public static void glFramebufferParameteri(int target, int pname, int param) {
		GL43.glFramebufferParameteri(target, pname, param);
	}

	public static void glGetFramebufferParameter(int target, int pname, IntBuffer params) {
		GL43.glGetFramebufferParameter(target, pname, params);
	}

	/** Overloads glGetFramebufferParameteriv. */
	public static int glGetFramebufferParameteri(int target, int pname) {
		return GL43.glGetFramebufferParameteri(target, pname);
	}

	public static void glNamedFramebufferParameteriEXT(int framebuffer, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNamedFramebufferParameteriEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNamedFramebufferParameteriEXT(framebuffer, pname, param, function_pointer);
	}
	static native void nglNamedFramebufferParameteriEXT(int framebuffer, int pname, int param, long function_pointer);

	public static void glGetNamedFramebufferParameterEXT(int framebuffer, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedFramebufferParameterivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetNamedFramebufferParameterivEXT(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetNamedFramebufferParameterivEXT(int framebuffer, int pname, long params, long function_pointer);

	/** Overloads glGetNamedFramebufferParameterivEXT. */
	public static int glGetNamedFramebufferParameterEXT(int framebuffer, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetNamedFramebufferParameterivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetNamedFramebufferParameterivEXT(framebuffer, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
