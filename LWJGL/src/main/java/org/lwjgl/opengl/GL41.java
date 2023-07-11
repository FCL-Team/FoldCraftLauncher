/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL41 {

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_SHADER_COMPILER = 0x8DFA,
		GL_NUM_SHADER_BINARY_FORMATS = 0x8DF9,
		GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB,
		GL_MAX_VARYING_VECTORS = 0x8DFC,
		GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD,
		GL_IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A,
		GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;

	/**
	 * Accepted by the &lt;type&gt; parameter of VertexAttribPointer: 
	 */
	public static final int GL_FIXED = 0x140C;

	/**
	 *  Accepted by the &lt;precisiontype&gt; parameter of
	 *  GetShaderPrecisionFormat:
	 */
	public static final int GL_LOW_FLOAT = 0x8DF0,
		GL_MEDIUM_FLOAT = 0x8DF1,
		GL_HIGH_FLOAT = 0x8DF2,
		GL_LOW_INT = 0x8DF3,
		GL_MEDIUM_INT = 0x8DF4,
		GL_HIGH_INT = 0x8DF5;

	/**
	 * Accepted by the &lt;format&gt; parameter of most commands taking sized internal formats: 
	 */
	public static final int GL_RGB565 = 0x8D62;

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

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  GetDoublev and GetInteger64v:
	 */
	public static final int GL_MAX_VIEWPORTS = 0x825B,
		GL_VIEWPORT_SUBPIXEL_BITS = 0x825C,
		GL_VIEWPORT_BOUNDS_RANGE = 0x825D,
		GL_LAYER_PROVOKING_VERTEX = 0x825E,
		GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 0x825F;

	/**
	 *  Returned in the &lt;data&gt; parameter from a Get query with a &lt;pname&gt; of
	 *  LAYER_PROVOKING_VERTEX or VIEWPORT_INDEX_PROVOKING_VERTEX:
	 */
	public static final int GL_UNDEFINED_VERTEX = 0x8260;

	private GL41() {}

	public static void glReleaseShaderCompiler() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glReleaseShaderCompiler;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglReleaseShaderCompiler(function_pointer);
	}
	static native void nglReleaseShaderCompiler(long function_pointer);

	public static void glShaderBinary(IntBuffer shaders, int binaryformat, ByteBuffer binary) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShaderBinary;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(shaders);
		BufferChecks.checkDirect(binary);
		nglShaderBinary(shaders.remaining(), MemoryUtil.getAddress(shaders), binaryformat, MemoryUtil.getAddress(binary), binary.remaining(), function_pointer);
	}
	static native void nglShaderBinary(int shaders_count, long shaders, int binaryformat, long binary, int binary_length, long function_pointer);

	public static void glGetShaderPrecisionFormat(int shadertype, int precisiontype, IntBuffer range, IntBuffer precision) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetShaderPrecisionFormat;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(range, 2);
		BufferChecks.checkBuffer(precision, 1);
		nglGetShaderPrecisionFormat(shadertype, precisiontype, MemoryUtil.getAddress(range), MemoryUtil.getAddress(precision), function_pointer);
	}
	static native void nglGetShaderPrecisionFormat(int shadertype, int precisiontype, long range, long precision, long function_pointer);

	public static void glDepthRangef(float n, float f) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDepthRangef;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDepthRangef(n, f, function_pointer);
	}
	static native void nglDepthRangef(float n, float f, long function_pointer);

	public static void glClearDepthf(float d) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClearDepthf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearDepthf(d, function_pointer);
	}
	static native void nglClearDepthf(float d, long function_pointer);

	public static void glGetProgramBinary(int program, IntBuffer length, IntBuffer binaryFormat, ByteBuffer binary) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramBinary;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(binaryFormat, 1);
		BufferChecks.checkDirect(binary);
		nglGetProgramBinary(program, binary.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(binaryFormat), MemoryUtil.getAddress(binary), function_pointer);
	}
	static native void nglGetProgramBinary(int program, int binary_bufSize, long length, long binaryFormat, long binary, long function_pointer);

	public static void glProgramBinary(int program, int binaryFormat, ByteBuffer binary) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramBinary;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(binary);
		nglProgramBinary(program, binaryFormat, MemoryUtil.getAddress(binary), binary.remaining(), function_pointer);
	}
	static native void nglProgramBinary(int program, int binaryFormat, long binary, int binary_length, long function_pointer);

	public static void glProgramParameteri(int program, int pname, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramParameteri(program, pname, value, function_pointer);
	}
	static native void nglProgramParameteri(int program, int pname, int value, long function_pointer);

	public static void glUseProgramStages(int pipeline, int stages, int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUseProgramStages;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUseProgramStages(pipeline, stages, program, function_pointer);
	}
	static native void nglUseProgramStages(int pipeline, int stages, int program, long function_pointer);

	public static void glActiveShaderProgram(int pipeline, int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glActiveShaderProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglActiveShaderProgram(pipeline, program, function_pointer);
	}
	static native void nglActiveShaderProgram(int pipeline, int program, long function_pointer);

	/**
	 * Single null-terminated source code string. 
	 */
	public static int glCreateShaderProgram(int type, ByteBuffer string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateShaderProgramv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(string);
		BufferChecks.checkNullTerminated(string);
		int __result = nglCreateShaderProgramv(type, 1, MemoryUtil.getAddress(string), function_pointer);
		return __result;
	}
	static native int nglCreateShaderProgramv(int type, int count, long string, long function_pointer);

	/**
	 * Overloads glCreateShaderProgramv.
	 * <p>
	 * Multiple null-terminated source code strings, one after the other. 
	 */
	public static int glCreateShaderProgram(int type, int count, ByteBuffer strings) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateShaderProgramv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(strings);
		BufferChecks.checkNullTerminated(strings, count);
		int __result = nglCreateShaderProgramv2(type, count, MemoryUtil.getAddress(strings), function_pointer);
		return __result;
	}
	static native int nglCreateShaderProgramv2(int type, int count, long strings, long function_pointer);

	/** Overloads glCreateShaderProgramv. */
	public static int glCreateShaderProgram(int type, ByteBuffer[] strings) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateShaderProgramv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(strings, 1);
		int __result = nglCreateShaderProgramv3(type, strings.length, strings, function_pointer);
		return __result;
	}
	static native int nglCreateShaderProgramv3(int type, int count, ByteBuffer[] strings, long function_pointer);

	/** Overloads glCreateShaderProgramv. */
	public static int glCreateShaderProgram(int type, CharSequence string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateShaderProgramv;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCreateShaderProgramv(type, 1, APIUtil.getBufferNT(caps, string), function_pointer);
		return __result;
	}

	/** Overloads glCreateShaderProgramv. */
	public static int glCreateShaderProgram(int type, CharSequence[] strings) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateShaderProgramv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(strings);
		int __result = nglCreateShaderProgramv2(type, strings.length, APIUtil.getBufferNT(caps, strings), function_pointer);
		return __result;
	}

	public static void glBindProgramPipeline(int pipeline) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindProgramPipeline;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindProgramPipeline(pipeline, function_pointer);
	}
	static native void nglBindProgramPipeline(int pipeline, long function_pointer);

	public static void glDeleteProgramPipelines(IntBuffer pipelines) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteProgramPipelines;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pipelines);
		nglDeleteProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
	}
	static native void nglDeleteProgramPipelines(int pipelines_n, long pipelines, long function_pointer);

	/** Overloads glDeleteProgramPipelines. */
	public static void glDeleteProgramPipelines(int pipeline) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteProgramPipelines;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteProgramPipelines(1, APIUtil.getInt(caps, pipeline), function_pointer);
	}

	public static void glGenProgramPipelines(IntBuffer pipelines) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenProgramPipelines;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(pipelines);
		nglGenProgramPipelines(pipelines.remaining(), MemoryUtil.getAddress(pipelines), function_pointer);
	}
	static native void nglGenProgramPipelines(int pipelines_n, long pipelines, long function_pointer);

	/** Overloads glGenProgramPipelines. */
	public static int glGenProgramPipelines() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenProgramPipelines;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer pipelines = APIUtil.getBufferInt(caps);
		nglGenProgramPipelines(1, MemoryUtil.getAddress(pipelines), function_pointer);
		return pipelines.get(0);
	}

	public static boolean glIsProgramPipeline(int pipeline) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsProgramPipeline;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsProgramPipeline(pipeline, function_pointer);
		return __result;
	}
	static native boolean nglIsProgramPipeline(int pipeline, long function_pointer);

	public static void glGetProgramPipeline(int pipeline, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramPipelineiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetProgramPipelineiv(pipeline, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramPipelineiv(int pipeline, int pname, long params, long function_pointer);

	/** Overloads glGetProgramPipelineiv. */
	public static int glGetProgramPipelinei(int pipeline, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramPipelineiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetProgramPipelineiv(pipeline, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glProgramUniform1i(int program, int location, int v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1i(program, location, v0, function_pointer);
	}
	static native void nglProgramUniform1i(int program, int location, int v0, long function_pointer);

	public static void glProgramUniform2i(int program, int location, int v0, int v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2i(program, location, v0, v1, function_pointer);
	}
	static native void nglProgramUniform2i(int program, int location, int v0, int v1, long function_pointer);

	public static void glProgramUniform3i(int program, int location, int v0, int v1, int v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3i(program, location, v0, v1, v2, function_pointer);
	}
	static native void nglProgramUniform3i(int program, int location, int v0, int v1, int v2, long function_pointer);

	public static void glProgramUniform4i(int program, int location, int v0, int v1, int v2, int v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4i(program, location, v0, v1, v2, v3, function_pointer);
	}
	static native void nglProgramUniform4i(int program, int location, int v0, int v1, int v2, int v3, long function_pointer);

	public static void glProgramUniform1f(int program, int location, float v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1f(program, location, v0, function_pointer);
	}
	static native void nglProgramUniform1f(int program, int location, float v0, long function_pointer);

	public static void glProgramUniform2f(int program, int location, float v0, float v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2f(program, location, v0, v1, function_pointer);
	}
	static native void nglProgramUniform2f(int program, int location, float v0, float v1, long function_pointer);

	public static void glProgramUniform3f(int program, int location, float v0, float v1, float v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3f(program, location, v0, v1, v2, function_pointer);
	}
	static native void nglProgramUniform3f(int program, int location, float v0, float v1, float v2, long function_pointer);

	public static void glProgramUniform4f(int program, int location, float v0, float v1, float v2, float v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4f(program, location, v0, v1, v2, v3, function_pointer);
	}
	static native void nglProgramUniform4f(int program, int location, float v0, float v1, float v2, float v3, long function_pointer);

	public static void glProgramUniform1d(int program, int location, double v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1d(program, location, v0, function_pointer);
	}
	static native void nglProgramUniform1d(int program, int location, double v0, long function_pointer);

	public static void glProgramUniform2d(int program, int location, double v0, double v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2d(program, location, v0, v1, function_pointer);
	}
	static native void nglProgramUniform2d(int program, int location, double v0, double v1, long function_pointer);

	public static void glProgramUniform3d(int program, int location, double v0, double v1, double v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3d(program, location, v0, v1, v2, function_pointer);
	}
	static native void nglProgramUniform3d(int program, int location, double v0, double v1, double v2, long function_pointer);

	public static void glProgramUniform4d(int program, int location, double v0, double v1, double v2, double v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4d(program, location, v0, v1, v2, v3, function_pointer);
	}
	static native void nglProgramUniform4d(int program, int location, double v0, double v1, double v2, double v3, long function_pointer);

	public static void glProgramUniform1(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform1iv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform1iv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform2(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform2iv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform2iv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform3(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform3iv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform3iv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform4(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform4iv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform4iv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform1(int program, int location, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform1fv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform1fv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform2(int program, int location, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform2fv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform2fv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform3(int program, int location, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform3fv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform3fv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform4(int program, int location, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform4fv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform4fv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform1(int program, int location, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform1dv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform1dv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform2(int program, int location, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform2dv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform2dv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform3(int program, int location, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform3dv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform3dv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform4(int program, int location, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform4dv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform4dv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform1ui(int program, int location, int v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform1ui(program, location, v0, function_pointer);
	}
	static native void nglProgramUniform1ui(int program, int location, int v0, long function_pointer);

	public static void glProgramUniform2ui(int program, int location, int v0, int v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform2ui(program, location, v0, v1, function_pointer);
	}
	static native void nglProgramUniform2ui(int program, int location, int v0, int v1, long function_pointer);

	public static void glProgramUniform3ui(int program, int location, int v0, int v1, int v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform3ui(program, location, v0, v1, v2, function_pointer);
	}
	static native void nglProgramUniform3ui(int program, int location, int v0, int v1, int v2, long function_pointer);

	public static void glProgramUniform4ui(int program, int location, int v0, int v1, int v2, int v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProgramUniform4ui(program, location, v0, v1, v2, v3, function_pointer);
	}
	static native void nglProgramUniform4ui(int program, int location, int v0, int v1, int v2, int v3, long function_pointer);

	public static void glProgramUniform1u(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform1uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform1uiv(program, location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform1uiv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform2u(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform2uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform2uiv(program, location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform2uiv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform3u(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform3uiv(program, location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform3uiv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniform4u(int program, int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniform4uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniform4uiv(program, location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniform4uiv(int program, int location, int value_count, long value, long function_pointer);

	public static void glProgramUniformMatrix2(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix2fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix2fv(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix2fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix3(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix3fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix3fv(program, location, value.remaining() / (3 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix3fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix4(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix4fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix4fv(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix4fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix2(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix2dv(program, location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix2dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix3(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix3dv(program, location, value.remaining() / (3 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix3dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix4(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix4dv(program, location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix4dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix2x3fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix2x3fv(program, location, value.remaining() / (2 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix2x3fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix3x2fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix3x2fv(program, location, value.remaining() / (3 * 2), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix3x2fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix2x4fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix2x4fv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix2x4fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix4x2fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix4x2fv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix4x2fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix3x4fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix3x4fv(program, location, value.remaining() / (3 * 4), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix3x4fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix4x3fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix4x3fv(program, location, value.remaining() / (4 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix4x3fv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix2x3(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix2x3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix2x3dv(program, location, value.remaining() / (2 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix2x3dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix3x2(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix3x2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix3x2dv(program, location, value.remaining() / (3 * 2), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix3x2dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix2x4(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix2x4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix2x4dv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix2x4dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix4x2(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix4x2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix4x2dv(program, location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix4x2dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix3x4(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix3x4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix3x4dv(program, location, value.remaining() / (3 * 4), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix3x4dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glProgramUniformMatrix4x3(int program, int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProgramUniformMatrix4x3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglProgramUniformMatrix4x3dv(program, location, value.remaining() / (4 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglProgramUniformMatrix4x3dv(int program, int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glValidateProgramPipeline(int pipeline) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glValidateProgramPipeline;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglValidateProgramPipeline(pipeline, function_pointer);
	}
	static native void nglValidateProgramPipeline(int pipeline, long function_pointer);

	public static void glGetProgramPipelineInfoLog(int pipeline, IntBuffer length, ByteBuffer infoLog) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramPipelineInfoLog;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(infoLog);
		nglGetProgramPipelineInfoLog(pipeline, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog), function_pointer);
	}
	static native void nglGetProgramPipelineInfoLog(int pipeline, int infoLog_bufSize, long length, long infoLog, long function_pointer);

	/** Overloads glGetProgramPipelineInfoLog. */
	public static String glGetProgramPipelineInfoLog(int pipeline, int bufSize) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramPipelineInfoLog;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer infoLog_length = APIUtil.getLengths(caps);
		ByteBuffer infoLog = APIUtil.getBufferByte(caps, bufSize);
		nglGetProgramPipelineInfoLog(pipeline, bufSize, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog), function_pointer);
		infoLog.limit(infoLog_length.get(0));
		return APIUtil.getString(caps, infoLog);
	}

	public static void glVertexAttribL1d(int index, double x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL1d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL1d(index, x, function_pointer);
	}
	static native void nglVertexAttribL1d(int index, double x, long function_pointer);

	public static void glVertexAttribL2d(int index, double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL2d(index, x, y, function_pointer);
	}
	static native void nglVertexAttribL2d(int index, double x, double y, long function_pointer);

	public static void glVertexAttribL3d(int index, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL3d(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttribL3d(int index, double x, double y, double z, long function_pointer);

	public static void glVertexAttribL4d(int index, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL4d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribL4d(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttribL4d(int index, double x, double y, double z, double w, long function_pointer);

	public static void glVertexAttribL1(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL1dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 1);
		nglVertexAttribL1dv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL1dv(int index, long v, long function_pointer);

	public static void glVertexAttribL2(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 2);
		nglVertexAttribL2dv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL2dv(int index, long v, long function_pointer);

	public static void glVertexAttribL3(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 3);
		nglVertexAttribL3dv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL3dv(int index, long v, long function_pointer);

	public static void glVertexAttribL4(int index, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribL4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglVertexAttribL4dv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglVertexAttribL4dv(int index, long v, long function_pointer);

	public static void glVertexAttribLPointer(int index, int size, int stride, DoubleBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribLPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(pointer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = pointer;
		nglVertexAttribLPointer(index, size, GL11.GL_DOUBLE, stride, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglVertexAttribLPointer(int index, int size, int type, int stride, long pointer, long function_pointer);
	public static void glVertexAttribLPointer(int index, int size, int stride, long pointer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribLPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexAttribLPointerBO(index, size, GL11.GL_DOUBLE, stride, pointer_buffer_offset, function_pointer);
	}
	static native void nglVertexAttribLPointerBO(int index, int size, int type, int stride, long pointer_buffer_offset, long function_pointer);

	public static void glGetVertexAttribL(int index, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribLdv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribLdv(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribLdv(int index, int pname, long params, long function_pointer);

	public static void glViewportArray(int first, FloatBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glViewportArrayv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglViewportArrayv(first, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglViewportArrayv(int first, int v_count, long v, long function_pointer);

	public static void glViewportIndexedf(int index, float x, float y, float w, float h) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glViewportIndexedf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglViewportIndexedf(index, x, y, w, h, function_pointer);
	}
	static native void nglViewportIndexedf(int index, float x, float y, float w, float h, long function_pointer);

	public static void glViewportIndexed(int index, FloatBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glViewportIndexedfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglViewportIndexedfv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglViewportIndexedfv(int index, long v, long function_pointer);

	public static void glScissorArray(int first, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glScissorArrayv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglScissorArrayv(first, v.remaining() >> 2, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglScissorArrayv(int first, int v_count, long v, long function_pointer);

	public static void glScissorIndexed(int index, int left, int bottom, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glScissorIndexed;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglScissorIndexed(index, left, bottom, width, height, function_pointer);
	}
	static native void nglScissorIndexed(int index, int left, int bottom, int width, int height, long function_pointer);

	public static void glScissorIndexed(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glScissorIndexedv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(v, 4);
		nglScissorIndexedv(index, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglScissorIndexedv(int index, long v, long function_pointer);

	public static void glDepthRangeArray(int first, DoubleBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDepthRangeArrayv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(v);
		nglDepthRangeArrayv(first, v.remaining() >> 1, MemoryUtil.getAddress(v), function_pointer);
	}
	static native void nglDepthRangeArrayv(int first, int v_count, long v, long function_pointer);

	public static void glDepthRangeIndexed(int index, double n, double f) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDepthRangeIndexed;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDepthRangeIndexed(index, n, f, function_pointer);
	}
	static native void nglDepthRangeIndexed(int index, double n, double f, long function_pointer);

	public static void glGetFloat(int target, int index, FloatBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFloati_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetFloati_v(target, index, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetFloati_v(int target, int index, long data, long function_pointer);

	/** Overloads glGetFloati_v. */
	public static float glGetFloat(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFloati_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer data = APIUtil.getBufferFloat(caps);
		nglGetFloati_v(target, index, MemoryUtil.getAddress(data), function_pointer);
		return data.get(0);
	}

	public static void glGetDouble(int target, int index, DoubleBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetDoublei_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(data);
		nglGetDoublei_v(target, index, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetDoublei_v(int target, int index, long data, long function_pointer);

	/** Overloads glGetDoublei_v. */
	public static double glGetDouble(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetDoublei_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		DoubleBuffer data = APIUtil.getBufferDouble(caps);
		nglGetDoublei_v(target, index, MemoryUtil.getAddress(data), function_pointer);
		return data.get(0);
	}
}
