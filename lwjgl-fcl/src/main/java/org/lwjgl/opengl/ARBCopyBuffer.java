/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBCopyBuffer {

	/**
	 *  Accepted by the target parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 *  GetBufferPointerv, MapBufferRange, FlushMappedBufferRange,
	 *  GetBufferParameteriv, BindBufferRange, BindBufferBase,
	 *  and CopyBufferSubData:
	 */
	public static final int GL_COPY_READ_BUFFER = 0x8F36,
		GL_COPY_WRITE_BUFFER = 0x8F37;

	private ARBCopyBuffer() {}

	public static void glCopyBufferSubData(int readTarget, int writeTarget, long readOffset, long writeOffset, long size) {
		GL31.glCopyBufferSubData(readTarget, writeTarget, readOffset, writeOffset, size);
	}
}
