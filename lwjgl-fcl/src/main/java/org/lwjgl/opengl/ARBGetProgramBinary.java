/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBGetProgramBinary {

	/**
	 *  Accepted by the &lt;pname&gt; parameter of ProgramParameteri and
	 *  GetProgramiv:
	 */
	public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 0x8257;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_PROGRAM_BINARY_LENGTH = 0x8741;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv and GetDoublev:
	 */
	public static final int GL_NUM_PROGRAM_BINARY_FORMATS = 0x87FE,
		GL_PROGRAM_BINARY_FORMATS = 0x87FF;

	private ARBGetProgramBinary() {}

	public static void glGetProgramBinary(int program, IntBuffer length, IntBuffer binaryFormat, ByteBuffer binary) {
		GL41.glGetProgramBinary(program, length, binaryFormat, binary);
	}

	public static void glProgramBinary(int program, int binaryFormat, ByteBuffer binary) {
		GL41.glProgramBinary(program, binaryFormat, binary);
	}

	public static void glProgramParameteri(int program, int pname, int value) {
		GL41.glProgramParameteri(program, pname, value);
	}
}
