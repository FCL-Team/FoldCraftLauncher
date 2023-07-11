/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBSeparateShaderObjects {

	/**
	 * Accepted by &lt;stages&gt; parameter to UseProgramStages: 
	 */
	public static final int GL_VERTEX_SHADER_BIT = 0x1,
		GL_FRAGMENT_SHADER_BIT = 0x2,
		GL_GEOMETRY_SHADER_BIT = 0x4,
		GL_TESS_CONTROL_SHADER_BIT = 0x8,
		GL_TESS_EVALUATION_SHADER_BIT = 0x10,
		GL_ALL_SHADER_BITS = 0xFFFFFFFF;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of ProgramParameteri and
	 *  GetProgramiv:
	 */
	public static final int GL_PROGRAM_SEPARABLE = 0x8258;

	/**
	 * Accepted by &lt;type&gt; parameter to GetProgramPipelineiv: 
	 */
	public static final int GL_ACTIVE_PROGRAM = 0x8259;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_PROGRAM_PIPELINE_BINDING = 0x825A;

	private ARBSeparateShaderObjects() {}

	public static void glUseProgramStages(int pipeline, int stages, int program) {
		GL41.glUseProgramStages(pipeline, stages, program);
	}

	public static void glActiveShaderProgram(int pipeline, int program) {
		GL41.glActiveShaderProgram(pipeline, program);
	}

	/**
	 * Single null-terminated source code string. 
	 */
	public static int glCreateShaderProgram(int type, ByteBuffer string) {
		return GL41.glCreateShaderProgram(type, string);
	}

	/**
	 * Overloads glCreateShaderProgramv.
	 * <p>
	 * Multiple null-terminated source code strings, one after the other. 
	 */
	public static int glCreateShaderProgram(int type, int count, ByteBuffer strings) {
		return GL41.glCreateShaderProgram(type, count, strings);
	}

	/** Overloads glCreateShaderProgramv. */
	public static int glCreateShaderProgram(int type, ByteBuffer[] strings) {
		return GL41.glCreateShaderProgram(type, strings);
	}

	/** Overloads glCreateShaderProgramv. */
	public static int glCreateShaderProgram(int type, CharSequence string) {
		return GL41.glCreateShaderProgram(type, string);
	}

	/** Overloads glCreateShaderProgramv. */
	public static int glCreateShaderProgram(int type, CharSequence[] strings) {
		return GL41.glCreateShaderProgram(type, strings);
	}

	public static void glBindProgramPipeline(int pipeline) {
		GL41.glBindProgramPipeline(pipeline);
	}

	public static void glDeleteProgramPipelines(IntBuffer pipelines) {
		GL41.glDeleteProgramPipelines(pipelines);
	}

	/** Overloads glDeleteProgramPipelines. */
	public static void glDeleteProgramPipelines(int pipeline) {
		GL41.glDeleteProgramPipelines(pipeline);
	}

	public static void glGenProgramPipelines(IntBuffer pipelines) {
		GL41.glGenProgramPipelines(pipelines);
	}

	/** Overloads glGenProgramPipelines. */
	public static int glGenProgramPipelines() {
		return GL41.glGenProgramPipelines();
	}

	public static boolean glIsProgramPipeline(int pipeline) {
		return GL41.glIsProgramPipeline(pipeline);
	}

	public static void glProgramParameteri(int program, int pname, int value) {
		GL41.glProgramParameteri(program, pname, value);
	}

	public static void glGetProgramPipeline(int pipeline, int pname, IntBuffer params) {
		GL41.glGetProgramPipeline(pipeline, pname, params);
	}

	/** Overloads glGetProgramPipelineiv. */
	public static int glGetProgramPipelinei(int pipeline, int pname) {
		return GL41.glGetProgramPipelinei(pipeline, pname);
	}

	public static void glProgramUniform1i(int program, int location, int v0) {
		GL41.glProgramUniform1i(program, location, v0);
	}

	public static void glProgramUniform2i(int program, int location, int v0, int v1) {
		GL41.glProgramUniform2i(program, location, v0, v1);
	}

	public static void glProgramUniform3i(int program, int location, int v0, int v1, int v2) {
		GL41.glProgramUniform3i(program, location, v0, v1, v2);
	}

	public static void glProgramUniform4i(int program, int location, int v0, int v1, int v2, int v3) {
		GL41.glProgramUniform4i(program, location, v0, v1, v2, v3);
	}

	public static void glProgramUniform1f(int program, int location, float v0) {
		GL41.glProgramUniform1f(program, location, v0);
	}

	public static void glProgramUniform2f(int program, int location, float v0, float v1) {
		GL41.glProgramUniform2f(program, location, v0, v1);
	}

	public static void glProgramUniform3f(int program, int location, float v0, float v1, float v2) {
		GL41.glProgramUniform3f(program, location, v0, v1, v2);
	}

	public static void glProgramUniform4f(int program, int location, float v0, float v1, float v2, float v3) {
		GL41.glProgramUniform4f(program, location, v0, v1, v2, v3);
	}

	public static void glProgramUniform1d(int program, int location, double v0) {
		GL41.glProgramUniform1d(program, location, v0);
	}

	public static void glProgramUniform2d(int program, int location, double v0, double v1) {
		GL41.glProgramUniform2d(program, location, v0, v1);
	}

	public static void glProgramUniform3d(int program, int location, double v0, double v1, double v2) {
		GL41.glProgramUniform3d(program, location, v0, v1, v2);
	}

	public static void glProgramUniform4d(int program, int location, double v0, double v1, double v2, double v3) {
		GL41.glProgramUniform4d(program, location, v0, v1, v2, v3);
	}

	public static void glProgramUniform1(int program, int location, IntBuffer value) {
		GL41.glProgramUniform1(program, location, value);
	}

	public static void glProgramUniform2(int program, int location, IntBuffer value) {
		GL41.glProgramUniform2(program, location, value);
	}

	public static void glProgramUniform3(int program, int location, IntBuffer value) {
		GL41.glProgramUniform3(program, location, value);
	}

	public static void glProgramUniform4(int program, int location, IntBuffer value) {
		GL41.glProgramUniform4(program, location, value);
	}

	public static void glProgramUniform1(int program, int location, FloatBuffer value) {
		GL41.glProgramUniform1(program, location, value);
	}

	public static void glProgramUniform2(int program, int location, FloatBuffer value) {
		GL41.glProgramUniform2(program, location, value);
	}

	public static void glProgramUniform3(int program, int location, FloatBuffer value) {
		GL41.glProgramUniform3(program, location, value);
	}

	public static void glProgramUniform4(int program, int location, FloatBuffer value) {
		GL41.glProgramUniform4(program, location, value);
	}

	public static void glProgramUniform1(int program, int location, DoubleBuffer value) {
		GL41.glProgramUniform1(program, location, value);
	}

	public static void glProgramUniform2(int program, int location, DoubleBuffer value) {
		GL41.glProgramUniform2(program, location, value);
	}

	public static void glProgramUniform3(int program, int location, DoubleBuffer value) {
		GL41.glProgramUniform3(program, location, value);
	}

	public static void glProgramUniform4(int program, int location, DoubleBuffer value) {
		GL41.glProgramUniform4(program, location, value);
	}

	public static void glProgramUniform1ui(int program, int location, int v0) {
		GL41.glProgramUniform1ui(program, location, v0);
	}

	public static void glProgramUniform2ui(int program, int location, int v0, int v1) {
		GL41.glProgramUniform2ui(program, location, v0, v1);
	}

	public static void glProgramUniform3ui(int program, int location, int v0, int v1, int v2) {
		GL41.glProgramUniform3ui(program, location, v0, v1, v2);
	}

	public static void glProgramUniform4ui(int program, int location, int v0, int v1, int v2, int v3) {
		GL41.glProgramUniform4ui(program, location, v0, v1, v2, v3);
	}

	public static void glProgramUniform1u(int program, int location, IntBuffer value) {
		GL41.glProgramUniform1u(program, location, value);
	}

	public static void glProgramUniform2u(int program, int location, IntBuffer value) {
		GL41.glProgramUniform2u(program, location, value);
	}

	public static void glProgramUniform3u(int program, int location, IntBuffer value) {
		GL41.glProgramUniform3u(program, location, value);
	}

	public static void glProgramUniform4u(int program, int location, IntBuffer value) {
		GL41.glProgramUniform4u(program, location, value);
	}

	public static void glProgramUniformMatrix2(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix2(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix3(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix3(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix4(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix4(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix2(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix2(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix3(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix3(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix4(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix4(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix2x3(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix3x2(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix2x4(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix4x2(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix3x4(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, FloatBuffer value) {
		GL41.glProgramUniformMatrix4x3(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix2x3(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix3x2(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix2x4(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix4x2(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix3x4(program, location, transpose, value);
	}

	public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, DoubleBuffer value) {
		GL41.glProgramUniformMatrix4x3(program, location, transpose, value);
	}

	public static void glValidateProgramPipeline(int pipeline) {
		GL41.glValidateProgramPipeline(pipeline);
	}

	public static void glGetProgramPipelineInfoLog(int pipeline, IntBuffer length, ByteBuffer infoLog) {
		GL41.glGetProgramPipelineInfoLog(pipeline, length, infoLog);
	}

	/** Overloads glGetProgramPipelineInfoLog. */
	public static String glGetProgramPipelineInfoLog(int pipeline, int bufSize) {
		return GL41.glGetProgramPipelineInfoLog(pipeline, bufSize);
	}
}
