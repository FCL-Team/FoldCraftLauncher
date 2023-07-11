/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBInternalformatQuery {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetInternalformativ: 
	 */
	public static final int GL_NUM_SAMPLE_COUNTS = 0x9380;

	private ARBInternalformatQuery() {}

	public static void glGetInternalformat(int target, int internalformat, int pname, IntBuffer params) {
		GL42.glGetInternalformat(target, internalformat, pname, params);
	}

	/** Overloads glGetInternalformativ. */
	public static int glGetInternalformat(int target, int internalformat, int pname) {
		return GL42.glGetInternalformat(target, internalformat, pname);
	}
}
