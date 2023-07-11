/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBViewportArray {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  GetDoublev and GetInteger64v:
	 */
	public static final int GL_MAX_VIEWPORTS = 0x825B,
		GL_VIEWPORT_SUBPIXEL_BITS = 0x825C,
		GL_VIEWPORT_BOUNDS_RANGE = 0x825D,
		GL_LAYER_PROVOKING_VERTEX = 0x825E,
		GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 0x825F;

	/**
	 *  Returned in the &lt;data&gt; parameter from a Get query with a &lt;pname&gt; of
	 *  LAYER_PROVOKING_VERTEX or VIEWPORT_INDEX_PROVOKING_VERTEX:
	 */
	public static final int GL_FIRST_VERTEX_CONVENTION = 0x8E4D,
		GL_LAST_VERTEX_CONVENTION = 0x8E4E,
		GL_PROVOKING_VERTEX = 0x8E4F,
		GL_UNDEFINED_VERTEX = 0x8260;

	private ARBViewportArray() {}

	public static void glViewportArray(int first, FloatBuffer v) {
		GL41.glViewportArray(first, v);
	}

	public static void glViewportIndexedf(int index, float x, float y, float w, float h) {
		GL41.glViewportIndexedf(index, x, y, w, h);
	}

	public static void glViewportIndexed(int index, FloatBuffer v) {
		GL41.glViewportIndexed(index, v);
	}

	public static void glScissorArray(int first, IntBuffer v) {
		GL41.glScissorArray(first, v);
	}

	public static void glScissorIndexed(int index, int left, int bottom, int width, int height) {
		GL41.glScissorIndexed(index, left, bottom, width, height);
	}

	public static void glScissorIndexed(int index, IntBuffer v) {
		GL41.glScissorIndexed(index, v);
	}

	public static void glDepthRangeArray(int first, DoubleBuffer v) {
		GL41.glDepthRangeArray(first, v);
	}

	public static void glDepthRangeIndexed(int index, double n, double f) {
		GL41.glDepthRangeIndexed(index, n, f);
	}

	public static void glGetFloat(int target, int index, FloatBuffer data) {
		GL41.glGetFloat(target, index, data);
	}

	/** Overloads glGetFloati_v. */
	public static float glGetFloat(int target, int index) {
		return GL41.glGetFloat(target, index);
	}

	public static void glGetDouble(int target, int index, DoubleBuffer data) {
		GL41.glGetDouble(target, index, data);
	}

	/** Overloads glGetDoublei_v. */
	public static double glGetDouble(int target, int index) {
		return GL41.glGetDouble(target, index);
	}

	public static void glGetIntegerIndexedEXT(int target, int index, IntBuffer v) {
		EXTDrawBuffers2.glGetIntegerIndexedEXT(target, index, v);
	}

	/** Overloads glGetIntegerIndexedivEXT. */
	public static int glGetIntegerIndexedEXT(int target, int index) {
		return EXTDrawBuffers2.glGetIntegerIndexedEXT(target, index);
	}

	public static void glEnableIndexedEXT(int target, int index) {
		EXTDrawBuffers2.glEnableIndexedEXT(target, index);
	}

	public static void glDisableIndexedEXT(int target, int index) {
		EXTDrawBuffers2.glDisableIndexedEXT(target, index);
	}

	public static boolean glIsEnabledIndexedEXT(int target, int index) {
		return EXTDrawBuffers2.glIsEnabledIndexedEXT(target, index);
	}
}
