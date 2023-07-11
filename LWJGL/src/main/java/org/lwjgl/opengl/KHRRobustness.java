/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class KHRRobustness {

	/**
	 * Returned by GetGraphicsResetStatus: 
	 */
	public static final int GL_GUILTY_CONTEXT_RESET = 0x8253,
		GL_INNOCENT_CONTEXT_RESET = 0x8254,
		GL_UNKNOWN_CONTEXT_RESET = 0x8255;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_CONTEXT_ROBUST_ACCESS = 0x90F3,
		GL_RESET_NOTIFICATION_STRATEGY = 0x8256;

	/**
	 *  Returned by GetIntegerv and related simple queries when &lt;value&gt; is
	 *  RESET_NOTIFICATION_STRATEGY:
	 */
	public static final int GL_LOSE_CONTEXT_ON_RESET = 0x8252,
		GL_NO_RESET_NOTIFICATION = 0x8261;

	/**
	 * Returned by GetError: 
	 */
	public static final int GL_CONTEXT_LOST = 0x507;

	private KHRRobustness() {}

	public static int glGetGraphicsResetStatus() {
		return GL45.glGetGraphicsResetStatus();
	}

	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
		GL45.glReadnPixels(x, y, width, height, format, type, pixels);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, DoubleBuffer pixels) {
		GL45.glReadnPixels(x, y, width, height, format, type, pixels);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, FloatBuffer pixels) {
		GL45.glReadnPixels(x, y, width, height, format, type, pixels);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, IntBuffer pixels) {
		GL45.glReadnPixels(x, y, width, height, format, type, pixels);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, ShortBuffer pixels) {
		GL45.glReadnPixels(x, y, width, height, format, type, pixels);
	}
	public static void glReadnPixels(int x, int y, int width, int height, int format, int type, int pixels_bufSize, long pixels_buffer_offset) {
		GL45.glReadnPixels(x, y, width, height, format, type, pixels_bufSize, pixels_buffer_offset);
	}

	public static void glGetnUniform(int program, int location, FloatBuffer params) {
		GL45.glGetnUniform(program, location, params);
	}

	public static void glGetnUniform(int program, int location, IntBuffer params) {
		GL45.glGetnUniform(program, location, params);
	}

	public static void glGetnUniformu(int program, int location, IntBuffer params) {
		GL45.glGetnUniformu(program, location, params);
	}
}
