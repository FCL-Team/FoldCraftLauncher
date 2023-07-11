/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBFramebufferObject {

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindFramebuffer,
	 *  CheckFramebufferStatus, FramebufferTexture{1D|2D|3D},
	 *  FramebufferRenderbuffer, and
	 *  GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_FRAMEBUFFER = 0x8D40,
		GL_READ_FRAMEBUFFER = 0x8CA8,
		GL_DRAW_FRAMEBUFFER = 0x8CA9;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindRenderbuffer,
	 *  RenderbufferStorage, and GetRenderbufferParameteriv, and
	 *  returned by GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_RENDERBUFFER = 0x8D41;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of
	 *  RenderbufferStorage:
	 */
	public static final int GL_STENCIL_INDEX1 = 0x8D46,
		GL_STENCIL_INDEX4 = 0x8D47,
		GL_STENCIL_INDEX8 = 0x8D48,
		GL_STENCIL_INDEX16 = 0x8D49;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv: 
	 */
	public static final int GL_RENDERBUFFER_WIDTH = 0x8D42,
		GL_RENDERBUFFER_HEIGHT = 0x8D43,
		GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8D44,
		GL_RENDERBUFFER_RED_SIZE = 0x8D50,
		GL_RENDERBUFFER_GREEN_SIZE = 0x8D51,
		GL_RENDERBUFFER_BLUE_SIZE = 0x8D52,
		GL_RENDERBUFFER_ALPHA_SIZE = 0x8D53,
		GL_RENDERBUFFER_DEPTH_SIZE = 0x8D54,
		GL_RENDERBUFFER_STENCIL_SIZE = 0x8D55,
		GL_RENDERBUFFER_SAMPLES = 0x8CAB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0,
		GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3,
		GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8CD4,
		GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 0x8210,
		GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 0x8211,
		GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 0x8212,
		GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 0x8213,
		GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 0x8214,
		GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 0x8215,
		GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 0x8216,
		GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 0x8217;

	/**
	 * Returned in &lt;params&gt; by GetFramebufferAttachmentParameteriv: 
	 */
	public static final int GL_SRGB = 0x8C40,
		GL_UNSIGNED_NORMALIZED = 0x8C17,
		GL_FRAMEBUFFER_DEFAULT = 0x8218,
		GL_INDEX = 0x8222;

	/**
	 *  Accepted by the &lt;attachment&gt; parameter of
	 *  FramebufferTexture{1D|2D|3D}, FramebufferRenderbuffer, and
	 *  GetFramebufferAttachmentParameteriv
	 */
	public static final int GL_COLOR_ATTACHMENT0 = 0x8CE0,
		GL_COLOR_ATTACHMENT1 = 0x8CE1,
		GL_COLOR_ATTACHMENT2 = 0x8CE2,
		GL_COLOR_ATTACHMENT3 = 0x8CE3,
		GL_COLOR_ATTACHMENT4 = 0x8CE4,
		GL_COLOR_ATTACHMENT5 = 0x8CE5,
		GL_COLOR_ATTACHMENT6 = 0x8CE6,
		GL_COLOR_ATTACHMENT7 = 0x8CE7,
		GL_COLOR_ATTACHMENT8 = 0x8CE8,
		GL_COLOR_ATTACHMENT9 = 0x8CE9,
		GL_COLOR_ATTACHMENT10 = 0x8CEA,
		GL_COLOR_ATTACHMENT11 = 0x8CEB,
		GL_COLOR_ATTACHMENT12 = 0x8CEC,
		GL_COLOR_ATTACHMENT13 = 0x8CED,
		GL_COLOR_ATTACHMENT14 = 0x8CEE,
		GL_COLOR_ATTACHMENT15 = 0x8CEF,
		GL_DEPTH_ATTACHMENT = 0x8D00,
		GL_STENCIL_ATTACHMENT = 0x8D20,
		GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_SAMPLES = 0x8D57;

	/**
	 * Returned by CheckFramebufferStatus(): 
	 */
	public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5,
		GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6,
		GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7,
		GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0x8CDB,
		GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0x8CDC,
		GL_FRAMEBUFFER_UNSUPPORTED = 0x8CDD,
		GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8D56,
		GL_FRAMEBUFFER_UNDEFINED = 0x8219;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_FRAMEBUFFER_BINDING = 0x8CA6,
		GL_DRAW_FRAMEBUFFER_BINDING = 0x8CA6,
		GL_READ_FRAMEBUFFER_BINDING = 0x8CAA,
		GL_RENDERBUFFER_BINDING = 0x8CA7,
		GL_MAX_COLOR_ATTACHMENTS = 0x8CDF,
		GL_MAX_RENDERBUFFER_SIZE = 0x84E8;

	/**
	 * Returned by GetError(): 
	 */
	public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 0x506;

	/**
	 *  Accepted by the &lt;format&gt; parameter of DrawPixels, ReadPixels,
	 *  TexImage1D, TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D,
	 *  TexSubImage3D, and GetTexImage, by the &lt;type&gt; parameter of
	 *  CopyPixels, by the &lt;internalformat&gt; parameter of TexImage1D,
	 *  TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 *  RenderbufferStorage, and returned in the &lt;data&gt; parameter of
	 *  GetTexLevelParameter and GetRenderbufferParameteriv:
	 */
	public static final int GL_DEPTH_STENCIL = 0x84F9;

	/**
	 *  Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 *  TexImage1D, TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D,
	 *  TexSubImage3D, and GetTexImage:
	 */
	public static final int GL_UNSIGNED_INT_24_8 = 0x84FA;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 *  TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 *  RenderbufferStorage, and returned in the &lt;data&gt; parameter of
	 *  GetTexLevelParameter and GetRenderbufferParameteriv:
	 */
	public static final int GL_DEPTH24_STENCIL8 = 0x88F0;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetTexLevelParameter: 
	 */
	public static final int GL_TEXTURE_STENCIL_SIZE = 0x88F1;

	private ARBFramebufferObject() {}

	public static boolean glIsRenderbuffer(int renderbuffer) {
		return GL30.glIsRenderbuffer(renderbuffer);
	}

	public static void glBindRenderbuffer(int target, int renderbuffer) {
		GL30.glBindRenderbuffer(target, renderbuffer);
	}

	public static void glDeleteRenderbuffers(IntBuffer renderbuffers) {
		GL30.glDeleteRenderbuffers(renderbuffers);
	}

	/** Overloads glDeleteRenderbuffers. */
	public static void glDeleteRenderbuffers(int renderbuffer) {
		GL30.glDeleteRenderbuffers(renderbuffer);
	}

	public static void glGenRenderbuffers(IntBuffer renderbuffers) {
		GL30.glGenRenderbuffers(renderbuffers);
	}

	/** Overloads glGenRenderbuffers. */
	public static int glGenRenderbuffers() {
		return GL30.glGenRenderbuffers();
	}

	public static void glRenderbufferStorage(int target, int internalformat, int width, int height) {
		GL30.glRenderbufferStorage(target, internalformat, width, height);
	}

	public static void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
		GL30.glRenderbufferStorageMultisample(target, samples, internalformat, width, height);
	}

	public static void glGetRenderbufferParameter(int target, int pname, IntBuffer params) {
		GL30.glGetRenderbufferParameter(target, pname, params);
	}

	/**
	 * Overloads glGetRenderbufferParameteriv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetRenderbufferParameteri} instead. 
	 */
	@Deprecated
	public static int glGetRenderbufferParameter(int target, int pname) {
		return ARBFramebufferObject.glGetRenderbufferParameteri(target, pname);
	}

	/** Overloads glGetRenderbufferParameteriv. */
	public static int glGetRenderbufferParameteri(int target, int pname) {
		return GL30.glGetRenderbufferParameteri(target, pname);
	}

	public static boolean glIsFramebuffer(int framebuffer) {
		return GL30.glIsFramebuffer(framebuffer);
	}

	public static void glBindFramebuffer(int target, int framebuffer) {
		GL30.glBindFramebuffer(target, framebuffer);
	}

	public static void glDeleteFramebuffers(IntBuffer framebuffers) {
		GL30.glDeleteFramebuffers(framebuffers);
	}

	/** Overloads glDeleteFramebuffers. */
	public static void glDeleteFramebuffers(int framebuffer) {
		GL30.glDeleteFramebuffers(framebuffer);
	}

	public static void glGenFramebuffers(IntBuffer framebuffers) {
		GL30.glGenFramebuffers(framebuffers);
	}

	/** Overloads glGenFramebuffers. */
	public static int glGenFramebuffers() {
		return GL30.glGenFramebuffers();
	}

	public static int glCheckFramebufferStatus(int target) {
		return GL30.glCheckFramebufferStatus(target);
	}

	public static void glFramebufferTexture1D(int target, int attachment, int textarget, int texture, int level) {
		GL30.glFramebufferTexture1D(target, attachment, textarget, texture, level);
	}

	public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
	}

	public static void glFramebufferTexture3D(int target, int attachment, int textarget, int texture, int level, int layer) {
		GL30.glFramebufferTexture3D(target, attachment, textarget, texture, level, layer);
	}

	public static void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
		GL30.glFramebufferTextureLayer(target, attachment, texture, level, layer);
	}

	public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		GL30.glFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer);
	}

	public static void glGetFramebufferAttachmentParameter(int target, int attachment, int pname, IntBuffer params) {
		GL30.glGetFramebufferAttachmentParameter(target, attachment, pname, params);
	}

	/**
	 * Overloads glGetFramebufferAttachmentParameteriv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetFramebufferAttachmentParameteri} instead. 
	 */
	@Deprecated
	public static int glGetFramebufferAttachmentParameter(int target, int attachment, int pname) {
		return GL30.glGetFramebufferAttachmentParameteri(target, attachment, pname);
	}

	/** Overloads glGetFramebufferAttachmentParameteriv. */
	public static int glGetFramebufferAttachmentParameteri(int target, int attachment, int pname) {
		return GL30.glGetFramebufferAttachmentParameteri(target, attachment, pname);
	}

	public static void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
		GL30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
	}

	public static void glGenerateMipmap(int target) {
		GL30.glGenerateMipmap(target);
	}
}
