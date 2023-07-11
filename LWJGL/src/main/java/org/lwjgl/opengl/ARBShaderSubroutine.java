/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBShaderSubroutine {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramStageiv: 
	 */
	public static final int GL_ACTIVE_SUBROUTINES = 0x8DE5,
		GL_ACTIVE_SUBROUTINE_UNIFORMS = 0x8DE6,
		GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 0x8E47,
		GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 0x8E48,
		GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 0x8E49;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, GetDoublev, and GetInteger64v:
	 */
	public static final int GL_MAX_SUBROUTINES = 0x8DE7,
		GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 0x8DE8;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveSubroutineUniformiv: 
	 */
	public static final int GL_NUM_COMPATIBLE_SUBROUTINES = 0x8E4A,
		GL_COMPATIBLE_SUBROUTINES = 0x8E4B;

	private ARBShaderSubroutine() {}

	public static int glGetSubroutineUniformLocation(int program, int shadertype, ByteBuffer name) {
		return GL40.glGetSubroutineUniformLocation(program, shadertype, name);
	}

	/** Overloads glGetSubroutineUniformLocation. */
	public static int glGetSubroutineUniformLocation(int program, int shadertype, CharSequence name) {
		return GL40.glGetSubroutineUniformLocation(program, shadertype, name);
	}

	public static int glGetSubroutineIndex(int program, int shadertype, ByteBuffer name) {
		return GL40.glGetSubroutineIndex(program, shadertype, name);
	}

	/** Overloads glGetSubroutineIndex. */
	public static int glGetSubroutineIndex(int program, int shadertype, CharSequence name) {
		return GL40.glGetSubroutineIndex(program, shadertype, name);
	}

	public static void glGetActiveSubroutineUniform(int program, int shadertype, int index, int pname, IntBuffer values) {
		GL40.glGetActiveSubroutineUniform(program, shadertype, index, pname, values);
	}

	/** Overloads glGetActiveSubroutineUniformiv. */
	public static int glGetActiveSubroutineUniformi(int program, int shadertype, int index, int pname) {
		return GL40.glGetActiveSubroutineUniformi(program, shadertype, index, pname);
	}

	public static void glGetActiveSubroutineUniformName(int program, int shadertype, int index, IntBuffer length, ByteBuffer name) {
		GL40.glGetActiveSubroutineUniformName(program, shadertype, index, length, name);
	}

	/** Overloads glGetActiveSubroutineUniformName. */
	public static String glGetActiveSubroutineUniformName(int program, int shadertype, int index, int bufsize) {
		return GL40.glGetActiveSubroutineUniformName(program, shadertype, index, bufsize);
	}

	public static void glGetActiveSubroutineName(int program, int shadertype, int index, IntBuffer length, ByteBuffer name) {
		GL40.glGetActiveSubroutineName(program, shadertype, index, length, name);
	}

	/** Overloads glGetActiveSubroutineName. */
	public static String glGetActiveSubroutineName(int program, int shadertype, int index, int bufsize) {
		return GL40.glGetActiveSubroutineName(program, shadertype, index, bufsize);
	}

	public static void glUniformSubroutinesu(int shadertype, IntBuffer indices) {
		GL40.glUniformSubroutinesu(shadertype, indices);
	}

	public static void glGetUniformSubroutineu(int shadertype, int location, IntBuffer params) {
		GL40.glGetUniformSubroutineu(shadertype, location, params);
	}

	/** Overloads glGetUniformSubroutineuiv. */
	public static int glGetUniformSubroutineui(int shadertype, int location) {
		return GL40.glGetUniformSubroutineui(shadertype, location);
	}

	public static void glGetProgramStage(int program, int shadertype, int pname, IntBuffer values) {
		GL40.glGetProgramStage(program, shadertype, pname, values);
	}

	/** Overloads glGetProgramStageiv. */
	public static int glGetProgramStagei(int program, int shadertype, int pname) {
		return GL40.glGetProgramStagei(program, shadertype, pname);
	}
}
