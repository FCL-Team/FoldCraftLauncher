/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBMultiDrawIndirect {

	private ARBMultiDrawIndirect() {}

	public static void glMultiDrawArraysIndirect(int mode, ByteBuffer indirect, int primcount, int stride) {
		GL43.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
	}
	public static void glMultiDrawArraysIndirect(int mode, long indirect_buffer_offset, int primcount, int stride) {
		GL43.glMultiDrawArraysIndirect(mode, indirect_buffer_offset, primcount, stride);
	}

	/** Overloads glMultiDrawArraysIndirect. */
	public static void glMultiDrawArraysIndirect(int mode, IntBuffer indirect, int primcount, int stride) {
		GL43.glMultiDrawArraysIndirect(mode, indirect, primcount, stride);
	}

	public static void glMultiDrawElementsIndirect(int mode, int type, ByteBuffer indirect, int primcount, int stride) {
		GL43.glMultiDrawElementsIndirect(mode, type, indirect, primcount, stride);
	}
	public static void glMultiDrawElementsIndirect(int mode, int type, long indirect_buffer_offset, int primcount, int stride) {
		GL43.glMultiDrawElementsIndirect(mode, type, indirect_buffer_offset, primcount, stride);
	}

	/** Overloads glMultiDrawElementsIndirect. */
	public static void glMultiDrawElementsIndirect(int mode, int type, IntBuffer indirect, int primcount, int stride) {
		GL43.glMultiDrawElementsIndirect(mode, type, indirect, primcount, stride);
	}
}
