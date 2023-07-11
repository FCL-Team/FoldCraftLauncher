/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTextureView {

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetTexParameterfv and
	 *  GetTexParameteriv:
	 */
	public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 0x82DB,
		GL_TEXTURE_VIEW_NUM_LEVELS = 0x82DC,
		GL_TEXTURE_VIEW_MIN_LAYER = 0x82DD,
		GL_TEXTURE_VIEW_NUM_LAYERS = 0x82DE,
		GL_TEXTURE_IMMUTABLE_LEVELS = 0x82DF;

	private ARBTextureView() {}

	public static void glTextureView(int texture, int target, int origtexture, int internalformat, int minlevel, int numlevels, int minlayer, int numlayers) {
		GL43.glTextureView(texture, target, origtexture, internalformat, minlevel, numlevels, minlayer, numlayers);
	}
}
