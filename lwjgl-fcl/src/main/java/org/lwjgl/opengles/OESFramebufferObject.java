/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESFramebufferObject {

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindFramebufferOES,
	 *  CheckFramebufferStatusOES, FramebufferTexture{2D|3D}OES,
	 *  FramebufferRenderbufferOES, and
	 *  GetFramebufferAttachmentParameterivOES:
	 */
	public static final int GL_FRAMEBUFFER_OES = 0x8D40;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindRenderbufferOES,
	 *  RenderbufferStorageOES, and GetRenderbufferParameterivOES, and
	 *  returned by GetFramebufferAttachmentParameterivOES:
	 */
	public static final int GL_RENDERBUFFER_OES = 0x8D41;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of
	 *  RenderbufferStorageOES:
	 */
	public static final int GL_DEPTH_COMPONENT16_OES = 0x81A5,
		GL_RGBA4_OES = 0x8056,
		GL_RGB5_A1_OES = 0x8057,
		GL_RGB565_OES = 0x8D62,
		GL_STENCIL_INDEX1_OES = 0x8D46,
		GL_STENCIL_INDEX4_OES = 0x8D47,
		GL_STENCIL_INDEX8_OES = 0x8D48;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameterivOES: 
	 */
	public static final int GL_RENDERBUFFER_WIDTH_OES = 0x8D42,
		GL_RENDERBUFFER_HEIGHT_OES = 0x8D43,
		GL_RENDERBUFFER_INTERNAL_FORMAT_OES = 0x8D44,
		GL_RENDERBUFFER_RED_SIZE_OES = 0x8D50,
		GL_RENDERBUFFER_GREEN_SIZE_OES = 0x8D51,
		GL_RENDERBUFFER_BLUE_SIZE_OES = 0x8D52,
		GL_RENDERBUFFER_ALPHA_SIZE_OES = 0x8D53,
		GL_RENDERBUFFER_DEPTH_SIZE_OES = 0x8D54,
		GL_RENDERBUFFER_STENCIL_SIZE_OES = 0x8D55;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetFramebufferAttachmentParameterivOES:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_OES = 0x8CD0,
		GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME_OES = 0x8CD1,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL_OES = 0x8CD2,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE_OES = 0x8CD3,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET_OES = 0x8CD4;

	/**
	 *  Accepted by the &lt;attachment&gt; parameter of
	 *  FramebufferTexture{2D|3D}OES, FramebufferRenderbufferOES, and
	 *  GetFramebufferAttachmentParameterivOES
	 */
	public static final int GL_COLOR_ATTACHMENT0_OES = 0x8CE0,
		GL_DEPTH_ATTACHMENT_OES = 0x8D00,
		GL_STENCIL_ATTACHMENT_OES = 0x8D20;

	/**
	 *  Returned by GetFramebufferAttachmentParameterivOES when the
	 *  &lt;pname&gt; parameter is FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE_OES:
	 */
	public static final int GL_NONE_OES = 0x0;

	/**
	 * Returned by CheckFramebufferStatusOES(): 
	 */
	public static final int GL_FRAMEBUFFER_COMPLETE_OES = 0x8CD5,
		GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_OES = 0x8CD6,
		GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_OES = 0x8CD7,
		GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_OES = 0x8CD9,
		GL_FRAMEBUFFER_INCOMPLETE_FORMATS_OES = 0x8CDA,
		GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_OES = 0x8CDB,
		GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_OES = 0x8CDC,
		GL_FRAMEBUFFER_UNSUPPORTED_OES = 0x8CDD;

	/**
	 * Accepted by GetIntegerv(): 
	 */
	public static final int GL_FRAMEBUFFER_BINDING_OES = 0x8CA6,
		GL_RENDERBUFFER_BINDING_OES = 0x8CA7,
		GL_MAX_RENDERBUFFER_SIZE_OES = 0x84E8;

	/**
	 * Returned by GetError(): 
	 */
	public static final int GL_INVALID_FRAMEBUFFER_OPERATION_OES = 0x506;

	private OESFramebufferObject() {}

	static native void initNativeStubs() throws LWJGLException;

	public static boolean glIsRenderbufferOES(int renderbuffer) {
		boolean __result = nglIsRenderbufferOES(renderbuffer);
		return __result;
	}
	static native boolean nglIsRenderbufferOES(int renderbuffer);

	public static void glBindRenderbufferOES(int target, int renderbuffer) {
		nglBindRenderbufferOES(target, renderbuffer);
	}
	static native void nglBindRenderbufferOES(int target, int renderbuffer);

	public static void glDeleteRenderbuffersOES(IntBuffer renderbuffers) {
		BufferChecks.checkDirect(renderbuffers);
		nglDeleteRenderbuffersOES(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers));
	}
	static native void nglDeleteRenderbuffersOES(int renderbuffers_n, long renderbuffers);

	/** Overloads glDeleteRenderbuffersOES. */
	public static void glDeleteRenderbuffersOES(int renderbuffer) {
		nglDeleteRenderbuffersOES(1, APIUtil.getInt(renderbuffer));
	}

	public static void glGenRenderbuffersOES(IntBuffer renderbuffers) {
		BufferChecks.checkDirect(renderbuffers);
		nglGenRenderbuffersOES(renderbuffers.remaining(), MemoryUtil.getAddress(renderbuffers));
	}
	static native void nglGenRenderbuffersOES(int renderbuffers_n, long renderbuffers);

	/** Overloads glGenRenderbuffersOES. */
	public static int glGenRenderbuffersOES() {
		IntBuffer renderbuffers = APIUtil.getBufferInt();
		nglGenRenderbuffersOES(1, MemoryUtil.getAddress(renderbuffers));
		return renderbuffers.get(0);
	}

	public static void glRenderbufferStorageOES(int target, int internalformat, int width, int height) {
		nglRenderbufferStorageOES(target, internalformat, width, height);
	}
	static native void nglRenderbufferStorageOES(int target, int internalformat, int width, int height);

	public static void glGetRenderbufferParameterOES(int target, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetRenderbufferParameterivOES(target, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetRenderbufferParameterivOES(int target, int pname, long params);

	/**
	 * Overloads glGetRenderbufferParameterivOES.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetRenderbufferParameteriOES} instead. 
	 */
	@Deprecated
	public static int glGetRenderbufferParameterOES(int target, int pname) {
		return OESFramebufferObject.glGetRenderbufferParameteriOES(target, pname);
	}

	/** Overloads glGetRenderbufferParameterivOES. */
	public static int glGetRenderbufferParameteriOES(int target, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetRenderbufferParameterivOES(target, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static boolean glIsFramebufferOES(int framebuffer) {
		boolean __result = nglIsFramebufferOES(framebuffer);
		return __result;
	}
	static native boolean nglIsFramebufferOES(int framebuffer);

	public static void glBindFramebufferOES(int target, int framebuffer) {
		nglBindFramebufferOES(target, framebuffer);
	}
	static native void nglBindFramebufferOES(int target, int framebuffer);

	public static void glDeleteFramebuffersOES(IntBuffer framebuffers) {
		BufferChecks.checkDirect(framebuffers);
		nglDeleteFramebuffersOES(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers));
	}
	static native void nglDeleteFramebuffersOES(int framebuffers_n, long framebuffers);

	/** Overloads glDeleteFramebuffersOES. */
	public static void glDeleteFramebuffersOES(int framebuffer) {
		nglDeleteFramebuffersOES(1, APIUtil.getInt(framebuffer));
	}

	public static void glGenFramebuffersOES(IntBuffer framebuffers) {
		BufferChecks.checkDirect(framebuffers);
		nglGenFramebuffersOES(framebuffers.remaining(), MemoryUtil.getAddress(framebuffers));
	}
	static native void nglGenFramebuffersOES(int framebuffers_n, long framebuffers);

	/** Overloads glGenFramebuffersOES. */
	public static int glGenFramebuffersOES() {
		IntBuffer framebuffers = APIUtil.getBufferInt();
		nglGenFramebuffersOES(1, MemoryUtil.getAddress(framebuffers));
		return framebuffers.get(0);
	}

	public static int glCheckFramebufferStatusOES(int target) {
		int __result = nglCheckFramebufferStatusOES(target);
		return __result;
	}
	static native int nglCheckFramebufferStatusOES(int target);

	public static void glFramebufferTexture2DOES(int target, int attachment, int textarget, int texture, int level) {
		nglFramebufferTexture2DOES(target, attachment, textarget, texture, level);
	}
	static native void nglFramebufferTexture2DOES(int target, int attachment, int textarget, int texture, int level);

	public static void glFramebufferRenderbufferOES(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		nglFramebufferRenderbufferOES(target, attachment, renderbuffertarget, renderbuffer);
	}
	static native void nglFramebufferRenderbufferOES(int target, int attachment, int renderbuffertarget, int renderbuffer);

	public static void glGetFramebufferAttachmentParameterOES(int target, int attachment, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetFramebufferAttachmentParameterivOES(target, attachment, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetFramebufferAttachmentParameterivOES(int target, int attachment, int pname, long params);

	/**
	 * Overloads glGetFramebufferAttachmentParameterivOES.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetFramebufferAttachmentParameteriOES} instead. 
	 */
	@Deprecated
	public static int glGetFramebufferAttachmentParameterOES(int target, int attachment, int pname) {
		return OESFramebufferObject.glGetFramebufferAttachmentParameteriOES(target, attachment, pname);
	}

	/** Overloads glGetFramebufferAttachmentParameterivOES. */
	public static int glGetFramebufferAttachmentParameteriOES(int target, int attachment, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetFramebufferAttachmentParameterivOES(target, attachment, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glGenerateMipmapOES(int target) {
		nglGenerateMipmapOES(target);
	}
	static native void nglGenerateMipmapOES(int target);
}
