/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTFramebufferObject {

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindFramebufferEXT,
	 *  CheckFramebufferStatusEXT, FramebufferTexture{1D|2D|3D}EXT, and
	 *  FramebufferRenderbufferEXT:
	 */
	public static final int GL_FRAMEBUFFER_EXT = 0x8D40;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindRenderbufferEXT,
	 *  RenderbufferStorageEXT, and GetRenderbufferParameterivEXT, and
	 *  returned by GetFramebufferAttachmentParameterivEXT:
	 */
	public static final int GL_RENDERBUFFER_EXT = 0x8D41;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of
	 *  RenderbufferStorageEXT:
	 */
	public static final int GL_STENCIL_INDEX1_EXT = 0x8D46,
		GL_STENCIL_INDEX4_EXT = 0x8D47,
		GL_STENCIL_INDEX8_EXT = 0x8D48,
		GL_STENCIL_INDEX16_EXT = 0x8D49;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameterivEXT: 
	 */
	public static final int GL_RENDERBUFFER_WIDTH_EXT = 0x8D42,
		GL_RENDERBUFFER_HEIGHT_EXT = 0x8D43,
		GL_RENDERBUFFER_INTERNAL_FORMAT_EXT = 0x8D44,
		GL_RENDERBUFFER_RED_SIZE_EXT = 0x8D50,
		GL_RENDERBUFFER_GREEN_SIZE_EXT = 0x8D51,
		GL_RENDERBUFFER_BLUE_SIZE_EXT = 0x8D52,
		GL_RENDERBUFFER_ALPHA_SIZE_EXT = 0x8D53,
		GL_RENDERBUFFER_DEPTH_SIZE_EXT = 0x8D54,
		GL_RENDERBUFFER_STENCIL_SIZE_EXT = 0x8D55;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetFramebufferAttachmentParameterivEXT:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_EXT = 0x8CD0,
		GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_EXT = 0x8CD1,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_EXT = 0x8CD2,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_EXT = 0x8CD3,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_EXT = 0x8CD4;

	/**
	 *  Accepted by the &lt;attachment&gt; parameter of
	 *  FramebufferTexture{1D|2D|3D}EXT, FramebufferRenderbufferEXT, and
	 *  GetFramebufferAttachmentParameterivEXT
	 */
	public static final int GL_COLOR_ATTACHMENT0_EXT = 0x8CE0,
		GL_COLOR_ATTACHMENT1_EXT = 0x8CE1,
		GL_COLOR_ATTACHMENT2_EXT = 0x8CE2,
		GL_COLOR_ATTACHMENT3_EXT = 0x8CE3,
		GL_COLOR_ATTACHMENT4_EXT = 0x8CE4,
		GL_COLOR_ATTACHMENT5_EXT = 0x8CE5,
		GL_COLOR_ATTACHMENT6_EXT = 0x8CE6,
		GL_COLOR_ATTACHMENT7_EXT = 0x8CE7,
		GL_COLOR_ATTACHMENT8_EXT = 0x8CE8,
		GL_COLOR_ATTACHMENT9_EXT = 0x8CE9,
		GL_COLOR_ATTACHMENT10_EXT = 0x8CEA,
		GL_COLOR_ATTACHMENT11_EXT = 0x8CEB,
		GL_COLOR_ATTACHMENT12_EXT = 0x8CEC,
		GL_COLOR_ATTACHMENT13_EXT = 0x8CED,
		GL_COLOR_ATTACHMENT14_EXT = 0x8CEE,
		GL_COLOR_ATTACHMENT15_EXT = 0x8CEF,
		GL_DEPTH_ATTACHMENT_EXT = 0x8D00,
		GL_STENCIL_ATTACHMENT_EXT = 0x8D20;

	/**
	 * Returned by CheckFramebufferStatusEXT(): 
	 */
	public static final int GL_FRAMEBUFFER_COMPLETE_EXT = 0x8CD5,
		GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT = 0x8CD6,
		GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT = 0x8CD7,
		GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT = 0x8CD9,
		GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT = 0x8CDA,
		GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT = 0x8CDB,
		GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT = 0x8CDC,
		GL_FRAMEBUFFER_UNSUPPORTED_EXT = 0x8CDD;

	/**
	 * Accepted by GetIntegerv(): 
	 */
	public static final int GL_FRAMEBUFFER_BINDING_EXT = 0x8CA6,
		GL_RENDERBUFFER_BINDING_EXT = 0x8CA7,
		GL_MAX_COLOR_ATTACHMENTS_EXT = 0x8CDF,
		GL_MAX_RENDERBUFFER_SIZE_EXT = 0x84E8;

	/**
	 * Returned by GetError(): 
	 */
	public static final int GL_INVALID_FRAMEBUFFER_OPERATION_EXT = 0x506;

	private EXTFramebufferObject() {}

	public static boolean glIsRenderbufferEXT(int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsRenderbufferEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsRenderbufferEXT(renderbuffer, function_pointer);
		return __result;
	}
	static native boolean nglIsRenderbufferEXT(int renderbuffer, long function_pointer);

	public static void glBindRenderbufferEXT(int target, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindRenderbufferEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindRenderbufferEXT(target, renderbuffer, function_pointer);
	}
	static native void nglBindRenderbufferEXT(int target, int renderbuffer, long function_pointer);

	public static void glDeleteRenderbuffersEXT(IntBuffer renderbuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteRenderbuffersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(renderbuffers);
		nglDeleteRenderbuffersEXT(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
	}
	static native void nglDeleteRenderbuffersEXT(int renderbuffers_n, long renderbuffers, long function_pointer);

	/** Overloads glDeleteRenderbuffersEXT. */
	public static void glDeleteRenderbuffersEXT(int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteRenderbuffersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteRenderbuffersEXT(1, APIUtil.getInt(caps, renderbuffer), function_pointer);
	}

	public static void glGenRenderbuffersEXT(IntBuffer renderbuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenRenderbuffersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(renderbuffers);
		nglGenRenderbuffersEXT(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers), function_pointer);
	}
	static native void nglGenRenderbuffersEXT(int renderbuffers_n, long renderbuffers, long function_pointer);

	/** Overloads glGenRenderbuffersEXT. */
	public static int glGenRenderbuffersEXT() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenRenderbuffersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer renderbuffers = APIUtil.getBufferInt(caps);
		nglGenRenderbuffersEXT(1, MemoryUtil.getAddress(renderbuffers), function_pointer);
		return renderbuffers.get(0);
	}

	public static void glRenderbufferStorageEXT(int target, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glRenderbufferStorageEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRenderbufferStorageEXT(target, internalformat, width, height, function_pointer);
	}
	static native void nglRenderbufferStorageEXT(int target, int internalformat, int width, int height, long function_pointer);

	public static void glGetRenderbufferParameterEXT(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetRenderbufferParameterivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetRenderbufferParameterivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetRenderbufferParameterivEXT(int target, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetRenderbufferParameterivEXT.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetRenderbufferParameteriEXT} instead. 
	 */
	@Deprecated
	public static int glGetRenderbufferParameterEXT(int target, int pname) {
		return EXTFramebufferObject.glGetRenderbufferParameteriEXT(target, pname);
	}

	/** Overloads glGetRenderbufferParameterivEXT. */
	public static int glGetRenderbufferParameteriEXT(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetRenderbufferParameterivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetRenderbufferParameterivEXT(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static boolean glIsFramebufferEXT(int framebuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsFramebufferEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsFramebufferEXT(framebuffer, function_pointer);
		return __result;
	}
	static native boolean nglIsFramebufferEXT(int framebuffer, long function_pointer);

	public static void glBindFramebufferEXT(int target, int framebuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFramebufferEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindFramebufferEXT(target, framebuffer, function_pointer);
	}
	static native void nglBindFramebufferEXT(int target, int framebuffer, long function_pointer);

	public static void glDeleteFramebuffersEXT(IntBuffer framebuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteFramebuffersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(framebuffers);
		nglDeleteFramebuffersEXT(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
	}
	static native void nglDeleteFramebuffersEXT(int framebuffers_n, long framebuffers, long function_pointer);

	/** Overloads glDeleteFramebuffersEXT. */
	public static void glDeleteFramebuffersEXT(int framebuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteFramebuffersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteFramebuffersEXT(1, APIUtil.getInt(caps, framebuffer), function_pointer);
	}

	public static void glGenFramebuffersEXT(IntBuffer framebuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenFramebuffersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(framebuffers);
		nglGenFramebuffersEXT(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers), function_pointer);
	}
	static native void nglGenFramebuffersEXT(int framebuffers_n, long framebuffers, long function_pointer);

	/** Overloads glGenFramebuffersEXT. */
	public static int glGenFramebuffersEXT() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenFramebuffersEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer framebuffers = APIUtil.getBufferInt(caps);
		nglGenFramebuffersEXT(1, MemoryUtil.getAddress(framebuffers), function_pointer);
		return framebuffers.get(0);
	}

	public static int glCheckFramebufferStatusEXT(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCheckFramebufferStatusEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCheckFramebufferStatusEXT(target, function_pointer);
		return __result;
	}
	static native int nglCheckFramebufferStatusEXT(int target, long function_pointer);

	public static void glFramebufferTexture1DEXT(int target, int attachment, int textarget, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTexture1DEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture1DEXT(target, attachment, textarget, texture, level, function_pointer);
	}
	static native void nglFramebufferTexture1DEXT(int target, int attachment, int textarget, int texture, int level, long function_pointer);

	public static void glFramebufferTexture2DEXT(int target, int attachment, int textarget, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTexture2DEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture2DEXT(target, attachment, textarget, texture, level, function_pointer);
	}
	static native void nglFramebufferTexture2DEXT(int target, int attachment, int textarget, int texture, int level, long function_pointer);

	public static void glFramebufferTexture3DEXT(int target, int attachment, int textarget, int texture, int level, int zoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTexture3DEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture3DEXT(target, attachment, textarget, texture, level, zoffset, function_pointer);
	}
	static native void nglFramebufferTexture3DEXT(int target, int attachment, int textarget, int texture, int level, int zoffset, long function_pointer);

	public static void glFramebufferRenderbufferEXT(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferRenderbufferEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferRenderbufferEXT(target, attachment, renderbuffertarget, renderbuffer, function_pointer);
	}
	static native void nglFramebufferRenderbufferEXT(int target, int attachment, int renderbuffertarget, int renderbuffer, long function_pointer);

	public static void glGetFramebufferAttachmentParameterEXT(int target, int attachment, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFramebufferAttachmentParameterivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetFramebufferAttachmentParameterivEXT(target, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetFramebufferAttachmentParameterivEXT(int target, int attachment, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetFramebufferAttachmentParameterivEXT.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetFramebufferAttachmentParameteriEXT} instead. 
	 */
	@Deprecated
	public static int glGetFramebufferAttachmentParameterEXT(int target, int attachment, int pname) {
		return EXTFramebufferObject.glGetFramebufferAttachmentParameteriEXT(target, attachment, pname);
	}

	/** Overloads glGetFramebufferAttachmentParameterivEXT. */
	public static int glGetFramebufferAttachmentParameteriEXT(int target, int attachment, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFramebufferAttachmentParameterivEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetFramebufferAttachmentParameterivEXT(target, attachment, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGenerateMipmapEXT(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenerateMipmapEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglGenerateMipmapEXT(target, function_pointer);
	}
	static native void nglGenerateMipmapEXT(int target, long function_pointer);
}
