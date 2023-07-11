/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBDrawElementsBaseVertex {

	private ARBDrawElementsBaseVertex() {}

	public static void glDrawElementsBaseVertex(int mode, ByteBuffer indices, int basevertex) {
		GL32.glDrawElementsBaseVertex(mode, indices, basevertex);
	}
	public static void glDrawElementsBaseVertex(int mode, IntBuffer indices, int basevertex) {
		GL32.glDrawElementsBaseVertex(mode, indices, basevertex);
	}
	public static void glDrawElementsBaseVertex(int mode, ShortBuffer indices, int basevertex) {
		GL32.glDrawElementsBaseVertex(mode, indices, basevertex);
	}
	public static void glDrawElementsBaseVertex(int mode, int indices_count, int type, long indices_buffer_offset, int basevertex) {
		GL32.glDrawElementsBaseVertex(mode, indices_count, type, indices_buffer_offset, basevertex);
	}

	public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, ByteBuffer indices, int basevertex) {
		GL32.glDrawRangeElementsBaseVertex(mode, start, end, indices, basevertex);
	}
	public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, IntBuffer indices, int basevertex) {
		GL32.glDrawRangeElementsBaseVertex(mode, start, end, indices, basevertex);
	}
	public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, ShortBuffer indices, int basevertex) {
		GL32.glDrawRangeElementsBaseVertex(mode, start, end, indices, basevertex);
	}
	public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset, int basevertex) {
		GL32.glDrawRangeElementsBaseVertex(mode, start, end, indices_count, type, indices_buffer_offset, basevertex);
	}

	public static void glDrawElementsInstancedBaseVertex(int mode, ByteBuffer indices, int primcount, int basevertex) {
		GL32.glDrawElementsInstancedBaseVertex(mode, indices, primcount, basevertex);
	}
	public static void glDrawElementsInstancedBaseVertex(int mode, IntBuffer indices, int primcount, int basevertex) {
		GL32.glDrawElementsInstancedBaseVertex(mode, indices, primcount, basevertex);
	}
	public static void glDrawElementsInstancedBaseVertex(int mode, ShortBuffer indices, int primcount, int basevertex) {
		GL32.glDrawElementsInstancedBaseVertex(mode, indices, primcount, basevertex);
	}
	public static void glDrawElementsInstancedBaseVertex(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int basevertex) {
		GL32.glDrawElementsInstancedBaseVertex(mode, indices_count, type, indices_buffer_offset, primcount, basevertex);
	}
}
