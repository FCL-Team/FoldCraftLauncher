/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESGetProgramBinary {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_PROGRAM_BINARY_LENGTH_OES = 0x8741;

	/**
	 *  Accepted by the &lt;pname&lt; parameter of GetBooleanv, GetIntegerv, and
	 *  GetFloatv:
	 */
	public static final int GL_NUM_PROGRAM_BINARY_FORMATS_OES = 0x87FE,
		GL_PROGRAM_BINARY_FORMATS_OES = 0x87FF;

	private OESGetProgramBinary() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glGetProgramBinaryOES(int program, IntBuffer length, IntBuffer binaryFormat, ByteBuffer binary) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(binaryFormat, 1);
		BufferChecks.checkDirect(binary);
		nglGetProgramBinaryOES(program, binary.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(binaryFormat), MemoryUtil.getAddress(binary));
	}
	static native void nglGetProgramBinaryOES(int program, int binary_bufSize, long length, long binaryFormat, long binary);

	public static void glProgramBinaryOES(int program, int binaryFormat, ByteBuffer binary) {
		BufferChecks.checkDirect(binary);
		nglProgramBinaryOES(program, binaryFormat, MemoryUtil.getAddress(binary), binary.remaining());
	}
	static native void nglProgramBinaryOES(int program, int binaryFormat, long binary, int binary_length);
}
