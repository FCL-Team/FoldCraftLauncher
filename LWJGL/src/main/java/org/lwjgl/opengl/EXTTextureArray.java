/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

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
	 *  Accepted by the &lt;param&gt; parameter of TexParameterf, TexParameteri,
	 *  TexParameterfv, and TexParameteriv when the &lt;pname&gt; parameter is
	 *  TEXTURE_COMPARE_MODE_ARB:
	 */
	public static final int GL_COMPARE_REF_DEPTH_TO_TEXTURE_EXT = 0x884E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetFramebufferAttachmentParameterivEXT:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER_EXT = 0x8CD4;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_1D_ARRAY_EXT = 0x8DC0,
		GL_SAMPLER_2D_ARRAY_EXT = 0x8DC1,
		GL_SAMPLER_1D_ARRAY_SHADOW_EXT = 0x8DC3,
		GL_SAMPLER_2D_ARRAY_SHADOW_EXT = 0x8DC4;

	private EXTTextureArray() {}

	public static void glFramebufferTextureLayerEXT(int target, int attachment, int texture, int level, int layer) {
		EXTGeometryShader4.glFramebufferTextureLayerEXT(target, attachment, texture, level, layer);
	}
}
