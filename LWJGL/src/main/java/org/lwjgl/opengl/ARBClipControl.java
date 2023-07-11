/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBClipControl {

	/**
	 * Accepted by the &lt;origin&gt; parameter of ClipControl: 
	 */
	public static final int GL_LOWER_LEFT = 0x8CA1,
		GL_UPPER_LEFT = 0x8CA2;

	/**
	 * Accepted by the &lt;depth&gt; parameter of ClipControl: 
	 */
	public static final int GL_NEGATIVE_ONE_TO_ONE = 0x935E,
		GL_ZERO_TO_ONE = 0x935F;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_CLIP_ORIGIN = 0x935C,
		GL_CLIP_DEPTH_MODE = 0x935D;

	private ARBClipControl() {}

	public static void glClipControl(int origin, int depth) {
		GL45.glClipControl(origin, depth);
	}
}
