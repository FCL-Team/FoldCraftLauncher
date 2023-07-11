/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBDrawIndirect {

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 *  GetBufferPointerv, MapBufferRange, FlushMappedBufferRange,
	 *  GetBufferParameteriv, BindBufferRange, BindBufferBase, and
	 *  CopyBufferSubData:
	 */
	public static final int GL_DRAW_INDIRECT_BUFFER = 0x8F3F;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetIntegerv, GetBooleanv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 0x8F43;

	private ARBDrawIndirect() {}

	public static void glDrawArraysIndirect(int mode, ByteBuffer indirect) {
		GL40.glDrawArraysIndirect(mode, indirect);
	}
	public static void glDrawArraysIndirect(int mode, long indirect_buffer_offset) {
		GL40.glDrawArraysIndirect(mode, indirect_buffer_offset);
	}

	/** Overloads glDrawArraysIndirect. */
	public static void glDrawArraysIndirect(int mode, IntBuffer indirect) {
		GL40.glDrawArraysIndirect(mode, indirect);
	}

	public static void glDrawElementsIndirect(int mode, int type, ByteBuffer indirect) {
		GL40.glDrawElementsIndirect(mode, type, indirect);
	}
	public static void glDrawElementsIndirect(int mode, int type, long indirect_buffer_offset) {
		GL40.glDrawElementsIndirect(mode, type, indirect_buffer_offset);
	}

	/** Overloads glDrawElementsIndirect. */
	public static void glDrawElementsIndirect(int mode, int type, IntBuffer indirect) {
		GL40.glDrawElementsIndirect(mode, type, indirect);
	}
}
