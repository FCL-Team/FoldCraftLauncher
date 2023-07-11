/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTransformFeedback3 {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 0x8E70,
		GL_MAX_VERTEX_STREAMS = 0x8E71;

	private ARBTransformFeedback3() {}

	public static void glDrawTransformFeedbackStream(int mode, int id, int stream) {
		GL40.glDrawTransformFeedbackStream(mode, id, stream);
	}

	public static void glBeginQueryIndexed(int target, int index, int id) {
		GL40.glBeginQueryIndexed(target, index, id);
	}

	public static void glEndQueryIndexed(int target, int index) {
		GL40.glEndQueryIndexed(target, index);
	}

	public static void glGetQueryIndexed(int target, int index, int pname, IntBuffer params) {
		GL40.glGetQueryIndexed(target, index, pname, params);
	}

	/** Overloads glGetQueryIndexediv. */
	public static int glGetQueryIndexedi(int target, int index, int pname) {
		return GL40.glGetQueryIndexedi(target, index, pname);
	}
}
