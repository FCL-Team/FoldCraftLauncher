/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBProgramInterfaceQuery {

	/**
	 *  Accepted by the &lt;programInterface&gt; parameter of GetProgramInterfaceiv,
	 *  GetProgramResourceIndex, GetProgramResourceName, GetProgramResourceiv,
	 *  GetProgramResourceLocation, and GetProgramResourceLocationIndex:
	 */
	public static final int GL_UNIFORM = 0x92E1,
		GL_UNIFORM_BLOCK = 0x92E2,
		GL_PROGRAM_INPUT = 0x92E3,
		GL_PROGRAM_OUTPUT = 0x92E4,
		GL_BUFFER_VARIABLE = 0x92E5,
		GL_SHADER_STORAGE_BLOCK = 0x92E6,
		GL_VERTEX_SUBROUTINE = 0x92E8,
		GL_TESS_CONTROL_SUBROUTINE = 0x92E9,
		GL_TESS_EVALUATION_SUBROUTINE = 0x92EA,
		GL_GEOMETRY_SUBROUTINE = 0x92EB,
		GL_FRAGMENT_SUBROUTINE = 0x92EC,
		GL_COMPUTE_SUBROUTINE = 0x92ED,
		GL_VERTEX_SUBROUTINE_UNIFORM = 0x92EE,
		GL_TESS_CONTROL_SUBROUTINE_UNIFORM = 0x92EF,
		GL_TESS_EVALUATION_SUBROUTINE_UNIFORM = 0x92F0,
		GL_GEOMETRY_SUBROUTINE_UNIFORM = 0x92F1,
		GL_FRAGMENT_SUBROUTINE_UNIFORM = 0x92F2,
		GL_COMPUTE_SUBROUTINE_UNIFORM = 0x92F3,
		GL_TRANSFORM_FEEDBACK_VARYING = 0x92F4;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramInterfaceiv: 
	 */
	public static final int GL_ACTIVE_RESOURCES = 0x92F5,
		GL_MAX_NAME_LENGTH = 0x92F6,
		GL_MAX_NUM_ACTIVE_VARIABLES = 0x92F7,
		GL_MAX_NUM_COMPATIBLE_SUBROUTINES = 0x92F8;

	/**
	 * Accepted in the &lt;props&gt; array of GetProgramResourceiv: 
	 */
	public static final int GL_NAME_LENGTH = 0x92F9,
		GL_TYPE = 0x92FA,
		GL_ARRAY_SIZE = 0x92FB,
		GL_OFFSET = 0x92FC,
		GL_BLOCK_INDEX = 0x92FD,
		GL_ARRAY_STRIDE = 0x92FE,
		GL_MATRIX_STRIDE = 0x92FF,
		GL_IS_ROW_MAJOR = 0x9300,
		GL_ATOMIC_COUNTER_BUFFER_INDEX = 0x9301,
		GL_BUFFER_BINDING = 0x9302,
		GL_BUFFER_DATA_SIZE = 0x9303,
		GL_NUM_ACTIVE_VARIABLES = 0x9304,
		GL_ACTIVE_VARIABLES = 0x9305,
		GL_REFERENCED_BY_VERTEX_SHADER = 0x9306,
		GL_REFERENCED_BY_TESS_CONTROL_SHADER = 0x9307,
		GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x9308,
		GL_REFERENCED_BY_GEOMETRY_SHADER = 0x9309,
		GL_REFERENCED_BY_FRAGMENT_SHADER = 0x930A,
		GL_REFERENCED_BY_COMPUTE_SHADER = 0x930B,
		GL_TOP_LEVEL_ARRAY_SIZE = 0x930C,
		GL_TOP_LEVEL_ARRAY_STRIDE = 0x930D,
		GL_LOCATION = 0x930E,
		GL_LOCATION_INDEX = 0x930F,
		GL_IS_PER_PATCH = 0x92E7;

	private ARBProgramInterfaceQuery() {}

	public static void glGetProgramInterface(int program, int programInterface, int pname, IntBuffer params) {
		GL43.glGetProgramInterface(program, programInterface, pname, params);
	}

	/** Overloads glGetProgramInterfaceiv. */
	public static int glGetProgramInterfacei(int program, int programInterface, int pname) {
		return GL43.glGetProgramInterfacei(program, programInterface, pname);
	}

	public static int glGetProgramResourceIndex(int program, int programInterface, ByteBuffer name) {
		return GL43.glGetProgramResourceIndex(program, programInterface, name);
	}

	/** Overloads glGetProgramResourceIndex. */
	public static int glGetProgramResourceIndex(int program, int programInterface, CharSequence name) {
		return GL43.glGetProgramResourceIndex(program, programInterface, name);
	}

	public static void glGetProgramResourceName(int program, int programInterface, int index, IntBuffer length, ByteBuffer name) {
		GL43.glGetProgramResourceName(program, programInterface, index, length, name);
	}

	/** Overloads glGetProgramResourceName. */
	public static String glGetProgramResourceName(int program, int programInterface, int index, int bufSize) {
		return GL43.glGetProgramResourceName(program, programInterface, index, bufSize);
	}

	public static void glGetProgramResource(int program, int programInterface, int index, IntBuffer props, IntBuffer length, IntBuffer params) {
		GL43.glGetProgramResource(program, programInterface, index, props, length, params);
	}

	public static int glGetProgramResourceLocation(int program, int programInterface, ByteBuffer name) {
		return GL43.glGetProgramResourceLocation(program, programInterface, name);
	}

	/** Overloads glGetProgramResourceLocation. */
	public static int glGetProgramResourceLocation(int program, int programInterface, CharSequence name) {
		return GL43.glGetProgramResourceLocation(program, programInterface, name);
	}

	public static int glGetProgramResourceLocationIndex(int program, int programInterface, ByteBuffer name) {
		return GL43.glGetProgramResourceLocationIndex(program, programInterface, name);
	}

	/** Overloads glGetProgramResourceLocationIndex. */
	public static int glGetProgramResourceLocationIndex(int program, int programInterface, CharSequence name) {
		return GL43.glGetProgramResourceLocationIndex(program, programInterface, name);
	}
}
