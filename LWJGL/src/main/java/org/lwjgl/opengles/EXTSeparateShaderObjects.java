/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTSeparateShaderObjects {

	/**
	 * Accepted by &lt;stages&gt; parameter to UseProgramStagesEXT: 
	 */
	public static final int GL_VERTEX_SHADER_BIT_EXT = 0x1,
		GL_FRAGMENT_SHADER_BIT_EXT = 0x2,
		GL_ALL_SHADER_BITS_EXT = 0xFFFFFFFF;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of ProgramParameteriEXT and
	 *  GetProgramiv:
	 */
	public static final int GL_PROGRAM_SEPARABLE_EXT = 0x8258;

	/**
	 * Accepted by &lt;type&gt; parameter to GetProgramPipelineivEXT: 
	 */
	public static final int GL_ACTIVE_PROGRAM_EXT = 0x8259;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, and
	 *  GetFloatv:
	 */
	public static final int GL_PROGRAM_PIPELINE_BINDING_EXT = 0x825A;

	private EXTSeparateShaderObjects() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glUseProgramStagesEXT(int pipeline, int stages, int program) {
		nglUseProgramStagesEXT(pipeline, stages, program);
	}
	static native void nglUseProgramStagesEXT(int pipeline, int stages, int program);

	public static void glActiveShaderProgramEXT(int pipeline, int program) {
		nglActiveShaderProgramEXT(pipeline, program);
	}
	static native void nglActiveShaderProgramEXT(int pipeline, int program);

	/**
	 * Single null-terminated source code string. 
	 */
	public static int glCreateShaderProgramEXT(int type, ByteBuffer string) {
		BufferChecks.checkDirect(string);
		BufferChecks.checkNullTerminated(string);
		int __result = nglCreateShaderProgramvEXT(type, 1, MemoryUtil.getAddress(string));
		return __result;
	}
	static native int nglCreateShaderProgramvEXT(int type, int count, long string);

	/**
	 * Overloads glCreateShaderProgramvEXT.
	 * <p>
	 * Multiple null-terminated source code strings, one after the other. 
	 */
	public static int glCreateShaderProgramEXT(int type, int count, ByteBuffer strings) {
		BufferChecks.checkDirect(strings);
		BufferChecks.checkNullTerminated(strings, count);
		int __result = nglCreateShaderProgramvEXT2(type, count, MemoryUtil.getAddress(strings));
		return __result;
	}
	static native int nglCreateShaderProgramvEXT2(int type, int count, long strings);

	/** Overloads glCreateShaderProgramvEXT. */
	public static int glCreateShaderProgramEXT(int type, ByteBuffer[] strings) {
		BufferChecks.checkArray(strings, 1);
		int __result = nglCreateShaderProgramvEXT3(type, strings.length, strings);
		return __result;
	}
	static native int nglCreateShaderProgramvEXT3(int type, int count, ByteBuffer[] strings);

	/** Overloads glCreateShaderProgramvEXT. */
	public static int glCreateShaderProgramEXT(int type, CharSequence string) {
		int __result = nglCreateShaderProgramvEXT(type, 1, APIUtil.getBufferNT(string));
		return __result;
	}

	/** Overloads glCreateShaderProgramvEXT. */
	public static int glCreateShaderProgramEXT(int type, CharSequence[] strings) {
		BufferChecks.checkArray(strings);
		int __result = nglCreateShaderProgramvEXT2(type, strings.length, APIUtil.getBufferNT(strings));
		return __result;
	}

	public static void glBindProgramPipelineEXT(int pipeline) {
		nglBindProgramPipelineEXT(pipeline);
	}
	static native void nglBindProgramPipelineEXT(int pipeline);

	public static void glDeleteProgramPipelinesEXT(IntBuffer pipelines) {
		BufferChecks.checkDirect(pipelines);
		nglDeleteProgramPipelinesEXT(pipelines.remaining(), MemoryUtil.getAddress(pipelines));
	}
	static native void nglDeleteProgramPipelinesEXT(int pipelines_n, long pipelines);

	/** Overloads glDeleteProgramPipelinesEXT. */
	public static void glDeleteProgramPipelinesEXT(int pipeline) {
		nglDeleteProgramPipelinesEXT(1, APIUtil.getInt(pipeline));
	}

	public static void glGenProgramPipelinesEXT(IntBuffer pipelines) {
		BufferChecks.checkDirect(pipelines);
		nglGenProgramPipelinesEXT(pipelines.remaining(), MemoryUtil.getAddress(pipelines));
	}
	static native void nglGenProgramPipelinesEXT(int pipelines_n, long pipelines);

	/** Overloads glGenProgramPipelinesEXT. */
	public static int glGenProgramPipelinesEXT() {
		IntBuffer pipelines = APIUtil.getBufferInt();
		nglGenProgramPipelinesEXT(1, MemoryUtil.getAddress(pipelines));
		return pipelines.get(0);
	}

	public static boolean glIsProgramPipelineEXT(int pipeline) {
		boolean __result = nglIsProgramPipelineEXT(pipeline);
		return __result;
	}
	static native boolean nglIsProgramPipelineEXT(int pipeline);

	public static void glProgramParameteriEXT(int program, int pname, int value) {
		nglProgramParameteriEXT(program, pname, value);
	}
	static native void nglProgramParameteriEXT(int program, int pname, int value);

	public static void glGetProgramPipelineEXT(int pipeline, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglGetProgramPipelineivEXT(pipeline, pname, MemoryUtil.getAddress(params));
	}
	static native void nglGetProgramPipelineivEXT(int pipeline, int pname, long params);

	/** Overloads glGetProgramPipelineivEXT. */
	public static int glGetProgramPipelineiEXT(int pipeline, int pname) {
		IntBuffer params = APIUtil.getBufferInt();
		nglGetProgramPipelineivEXT(pipeline, pname, MemoryUtil.getAddress(params));
		return params.get(0);
	}

	public static void glProgramUniform1iEXT(int program, int location, int v0) {
		nglProgramUniform1iEXT(program, location, v0);
	}
	static native void nglProgramUniform1iEXT(int program, int location, int v0);

	public static void glProgramUniform2iEXT(int program, int location, int v0, int v1) {
		nglProgramUniform2iEXT(program, location, v0, v1);
	}
	static native void nglProgramUniform2iEXT(int program, int location, int v0, int v1);

	public static void glProgramUniform3iEXT(int program, int location, int v0, int v1, int v2) {
		nglProgramUniform3iEXT(program, location, v0, v1, v2);
	}
	static native void nglProgramUniform3iEXT(int program, int location, int v0, int v1, int v2);

	public static void glProgramUniform4iEXT(int program, int location, int v0, int v1, int v2, int v3) {
		nglProgramUniform4iEXT(program, location, v0, v1, v2, v3);
	}
	static native void nglProgramUniform4iEXT(int program, int location, int v0, int v1, int v2, int v3);

	public static void glProgramUniform1fEXT(int program, int location, float v0) {
		nglProgramUniform1fEXT(program, location, v0);
	}
	static native void nglProgramUniform1fEXT(int program, int location, float v0);

	public static void glProgramUniform2fEXT(int program, int location, float v0, float v1) {
		nglProgramUniform2fEXT(program, location, v0, v1);
	}
	static native void nglProgramUniform2fEXT(int program, int location, float v0, float v1);

	public static void glProgramUniform3fEXT(int program, int location, float v0, float v1, float v2) {
		nglProgramUniform3fEXT(program, location, v0, v1, v2);
	}
	static native void nglProgramUniform3fEXT(int program, int location, float v0, float v1, float v2);

	public static void glProgramUniform4fEXT(int program, int location, float v0, float v1, float v2, float v3) {
		nglProgramUniform4fEXT(program, location, v0, v1, v2, v3);
	}
	static native void nglProgramUniform4fEXT(int program, int location, float v0, float v1, float v2, float v3);

	public static void glProgramUniform1EXT(int program, int location, IntBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniform1ivEXT(program, location, value.remaining(), MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniform1ivEXT(int program, int location, int value_count, long value);

	public static void glProgramUniform2EXT(int program, int location, IntBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniform2ivEXT(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniform2ivEXT(int program, int location, int value_count, long value);

	public static void glProgramUniform3EXT(int program, int location, IntBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniform3ivEXT(program, location, value.remaining() / 3, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniform3ivEXT(int program, int location, int value_count, long value);

	public static void glProgramUniform4EXT(int program, int location, IntBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniform4ivEXT(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniform4ivEXT(int program, int location, int value_count, long value);

	public static void glProgramUniform1EXT(int program, int location, FloatBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniform1fvEXT(program, location, value.remaining(), MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniform1fvEXT(int program, int location, int value_count, long value);

	public static void glProgramUniform2EXT(int program, int location, FloatBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniform2fvEXT(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniform2fvEXT(int program, int location, int value_count, long value);

	public static void glProgramUniform3EXT(int program, int location, FloatBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniform3fvEXT(program, location, value.remaining() / 3, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniform3fvEXT(int program, int location, int value_count, long value);

	public static void glProgramUniform4EXT(int program, int location, FloatBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniform4fvEXT(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniform4fvEXT(int program, int location, int value_count, long value);

	public static void glProgramUniformMatrix2EXT(int program, int location, boolean transpose, FloatBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix2fvEXT(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniformMatrix2fvEXT(int program, int location, int value_count, boolean transpose, long value);

	public static void glProgramUniformMatrix3EXT(int program, int location, boolean transpose, FloatBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix3fvEXT(program, location, value.remaining() / (3 * 3), transpose, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniformMatrix3fvEXT(int program, int location, int value_count, boolean transpose, long value);

	public static void glProgramUniformMatrix4EXT(int program, int location, boolean transpose, FloatBuffer value) {
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix4fvEXT(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value));
	}
	static native void nglProgramUniformMatrix4fvEXT(int program, int location, int value_count, boolean transpose, long value);

	public static void glValidateProgramPipelineEXT(int pipeline) {
		nglValidateProgramPipelineEXT(pipeline);
	}
	static native void nglValidateProgramPipelineEXT(int pipeline);

	public static void glGetProgramPipelineInfoLogEXT(int pipeline, IntBuffer length, ByteBuffer infoLog) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(infoLog);
		nglGetProgramPipelineInfoLogEXT(pipeline, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog));
	}
	static native void nglGetProgramPipelineInfoLogEXT(int pipeline, int infoLog_bufSize, long length, long infoLog);

	/** Overloads glGetProgramPipelineInfoLogEXT. */
	public static String glGetProgramPipelineInfoLogEXT(int pipeline, int bufSize) {
		IntBuffer infoLog_length = APIUtil.getLengths();
		ByteBuffer infoLog = APIUtil.getBufferByte(bufSize);
		nglGetProgramPipelineInfoLogEXT(pipeline, bufSize, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog));
		infoLog.limit(infoLog_length.get(0));
		return APIUtil.getString(infoLog);
	}
}
