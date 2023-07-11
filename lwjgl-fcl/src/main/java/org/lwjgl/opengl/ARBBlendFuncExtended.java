/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBBlendFuncExtended {

	/**
	 *  Accepted by the &lt;src&gt; and &lt;dst&gt; parameters of BlendFunc and
	 *  BlendFunci, and by the &lt;srcRGB&gt;, &lt;dstRGB&gt;, &lt;srcAlpha&gt; and &lt;dstAlpha&gt;
	 *  parameters of BlendFuncSeparate and BlendFuncSeparatei:
	 */
	public static final int GL_SRC1_COLOR = 0x88F9,
		GL_SRC1_ALPHA = 0x8589,
		GL_ONE_MINUS_SRC1_COLOR = 0x88FA,
		GL_ONE_MINUS_SRC1_ALPHA = 0x88FB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv
	 *  and GetDoublev:
	 */
	public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 0x88FC;

	private ARBBlendFuncExtended() {}

	public static void glBindFragDataLocationIndexed(int program, int colorNumber, int index, ByteBuffer name) {
		GL33.glBindFragDataLocationIndexed(program, colorNumber, index, name);
	}

	/** Overloads glBindFragDataLocationIndexed. */
	public static void glBindFragDataLocationIndexed(int program, int colorNumber, int index, CharSequence name) {
		GL33.glBindFragDataLocationIndexed(program, colorNumber, index, name);
	}

	public static int glGetFragDataIndex(int program, ByteBuffer name) {
		return GL33.glGetFragDataIndex(program, name);
	}

	/** Overloads glGetFragDataIndex. */
	public static int glGetFragDataIndex(int program, CharSequence name) {
		return GL33.glGetFragDataIndex(program, name);
	}
}
