/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTransformFeedback2 {

	/**
	 * Accepted by the &lt;target&gt; parameter of BindTransformFeedback: 
	 */
	public static final int GL_TRANSFORM_FEEDBACK = 0x8E22;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 0x8E23,
		GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 0x8E24,
		GL_TRANSFORM_FEEDBACK_BINDING = 0x8E25;

	private ARBTransformFeedback2() {}

	public static void glBindTransformFeedback(int target, int id) {
		GL40.glBindTransformFeedback(target, id);
	}

	public static void glDeleteTransformFeedbacks(IntBuffer ids) {
		GL40.glDeleteTransformFeedbacks(ids);
	}

	/** Overloads glDeleteTransformFeedbacks. */
	public static void glDeleteTransformFeedbacks(int id) {
		GL40.glDeleteTransformFeedbacks(id);
	}

	public static void glGenTransformFeedbacks(IntBuffer ids) {
		GL40.glGenTransformFeedbacks(ids);
	}

	/** Overloads glGenTransformFeedbacks. */
	public static int glGenTransformFeedbacks() {
		return GL40.glGenTransformFeedbacks();
	}

	public static boolean glIsTransformFeedback(int id) {
		return GL40.glIsTransformFeedback(id);
	}

	public static void glPauseTransformFeedback() {
		GL40.glPauseTransformFeedback();
	}

	public static void glResumeTransformFeedback() {
		GL40.glResumeTransformFeedback();
	}

	public static void glDrawTransformFeedback(int mode, int id) {
		GL40.glDrawTransformFeedback(mode, id);
	}
}
