/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVTransformFeedback2 {

	/**
	 * Accepted by the &lt;target&gt; parameter of BindTransformFeedbackNV: 
	 */
	public static final int GL_TRANSFORM_FEEDBACK_NV = 0x8E22;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED_NV = 0x8E23,
		GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE_NV = 0x8E24,
		GL_TRANSFORM_FEEDBACK_BINDING_NV = 0x8E25;

	private NVTransformFeedback2() {}

	public static void glBindTransformFeedbackNV(int target, int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindTransformFeedbackNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindTransformFeedbackNV(target, id, function_pointer);
	}
	static native void nglBindTransformFeedbackNV(int target, int id, long function_pointer);

	public static void glDeleteTransformFeedbacksNV(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteTransformFeedbacksNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglDeleteTransformFeedbacksNV(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglDeleteTransformFeedbacksNV(int ids_n, long ids, long function_pointer);

	/** Overloads glDeleteTransformFeedbacksNV. */
	public static void glDeleteTransformFeedbacksNV(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteTransformFeedbacksNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteTransformFeedbacksNV(1, APIUtil.getInt(caps, id), function_pointer);
	}

	public static void glGenTransformFeedbacksNV(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenTransformFeedbacksNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglGenTransformFeedbacksNV(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglGenTransformFeedbacksNV(int ids_n, long ids, long function_pointer);

	/** Overloads glGenTransformFeedbacksNV. */
	public static int glGenTransformFeedbacksNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenTransformFeedbacksNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer ids = APIUtil.getBufferInt(caps);
		nglGenTransformFeedbacksNV(1, MemoryUtil.getAddress(ids), function_pointer);
		return ids.get(0);
	}

	public static boolean glIsTransformFeedbackNV(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsTransformFeedbackNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsTransformFeedbackNV(id, function_pointer);
		return __result;
	}
	static native boolean nglIsTransformFeedbackNV(int id, long function_pointer);

	public static void glPauseTransformFeedbackNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPauseTransformFeedbackNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPauseTransformFeedbackNV(function_pointer);
	}
	static native void nglPauseTransformFeedbackNV(long function_pointer);

	public static void glResumeTransformFeedbackNV() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glResumeTransformFeedbackNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglResumeTransformFeedbackNV(function_pointer);
	}
	static native void nglResumeTransformFeedbackNV(long function_pointer);

	public static void glDrawTransformFeedbackNV(int mode, int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawTransformFeedbackNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawTransformFeedbackNV(mode, id, function_pointer);
	}
	static native void nglDrawTransformFeedbackNV(int mode, int id, long function_pointer);
}
