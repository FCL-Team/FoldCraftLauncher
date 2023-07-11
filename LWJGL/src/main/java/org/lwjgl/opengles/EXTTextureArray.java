/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTTextureArray {

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexParameteri, TexParameteriv,
	 *  TexParameterf, TexParameterfv, and BindTexture:
	 */
	public static final int GL_TEXTURE_1D_ARRAY_EXT = 0x8C18,
		GL_TEXTURE_2D_ARRAY_EXT = 0x8C1A;

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexImage3D, TexSubImage3D,
	 *  CopyTexSubImage3D, CompressedTexImage3D, and CompressedTexSubImage3D:
	 */
	public static final int GL_PROXY_TEXTURE_2D_ARRAY_EXT = 0x8C1B;

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexImage2D, TexSubImage2D,
	 *  CopyTexImage2D, CopyTexSubImage2D, CompressedTexImage2D, and
	 *  CompressedTexSubImage2D:
	 */
	public static final int GL_PROXY_TEXTURE_1D_ARRAY_EXT = 0x8C19;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv
	 *  and GetFloatv:
	 */
	public static final int GL_TEXTURE_BINDING_1D_ARRAY_EXT = 0x8C1C,
		GL_TEXTURE_BINDING_2D_ARRAY_EXT = 0x8C1D,
		GL_MAX_ARRAY_TEXTURE_LAYERS_EXT = 0x88FF;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetFramebufferAttachmentParameterivEXT:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT = 0x8CD4;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_1D_ARRAY_EXT = 0x8DC0,
		GL_SAMPLER_2D_ARRAY_EXT = 0x8DC1;

	private EXTTextureArray() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glFramebufferTextureLayerEXT(int target, int attachment, int texture, int level, int layer) {
		nglFramebufferTextureLayerEXT(target, attachment, texture, level, layer);
	}
	static native void nglFramebufferTextureLayerEXT(int target, int attachment, int texture, int level, int layer);
}
