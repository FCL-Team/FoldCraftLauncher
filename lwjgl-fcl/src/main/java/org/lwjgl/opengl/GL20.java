/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL20 {

	/**
	 * Accepted by the &lt;name&gt; parameter of GetString: 
	 */
	public static final int GL_SHADING_LANGUAGE_VERSION = 0x8B8C;

	/**
	 * Accepted by the &lt;pname&gt; argument of GetInteger: 
	 */
	public static final int GL_CURRENT_PROGRAM = 0x8B8D;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetObjectParameter{fi}vARB: 
	 */
	public static final int GL_SHADER_TYPE = 0x8B4F,
		GL_DELETE_STATUS = 0x8B80,
		GL_COMPILE_STATUS = 0x8B81,
		GL_LINK_STATUS = 0x8B82,
		GL_VALIDATE_STATUS = 0x8B83,
		GL_INFO_LOG_LENGTH = 0x8B84,
		GL_ATTACHED_SHADERS = 0x8B85,
		GL_ACTIVE_UNIFORMS = 0x8B86,
		GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87,
		GL_ACTIVE_ATTRIBUTES = 0x8B89,
		GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A,
		GL_SHADER_SOURCE_LENGTH = 0x8B88;

	/**
	 * Returned by the &lt;params&gt; parameter of GetObjectParameter{fi}vARB: 
	 */
	public static final int GL_SHADER_OBJECT = 0x8B48;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniformARB: 
	 */
	public static final int GL_FLOAT_VEC2 = 0x8B50,
		GL_FLOAT_VEC3 = 0x8B51,
		GL_FLOAT_VEC4 = 0x8B52,
		GL_INT_VEC2 = 0x8B53,
		GL_INT_VEC3 = 0x8B54,
		GL_INT_VEC4 = 0x8B55,
		GL_BOOL = 0x8B56,
		GL_BOOL_VEC2 = 0x8B57,
		GL_BOOL_VEC3 = 0x8B58,
		GL_BOOL_VEC4 = 0x8B59,
		GL_FLOAT_MAT2 = 0x8B5A,
		GL_FLOAT_MAT3 = 0x8B5B,
		GL_FLOAT_MAT4 = 0x8B5C,
		GL_SAMPLER_1D = 0x8B5D,
		GL_SAMPLER_2D = 0x8B5E,
		GL_SAMPLER_3D = 0x8B5F,
		GL_SAMPLER_CUBE = 0x8B60,
		GL_SAMPLER_1D_SHADOW = 0x8B61,
		GL_SAMPLER_2D_SHADOW = 0x8B62;

	/**
	 *  Accepted by the &lt;shaderType&gt; argument of CreateShader and
	 *  returned by the &lt;params&gt; parameter of GetShader{if}v:
	 */
	public static final int GL_VERTEX_SHADER = 0x8B31;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 0x8B4A,
		GL_MAX_VARYING_FLOATS = 0x8B4B,
		GL_MAX_VERTEX_ATTRIBS = 0x8869,
		GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872,
		GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C,
		GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D,
		GL_MAX_TEXTURE_COORDS = 0x8871;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and
	 *  by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_VERTEX_PROGRAM_POINT_SIZE = 0x8642,
		GL_VERTEX_PROGRAM_TWO_SIDE = 0x8643;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttrib{dfi}vARB: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622,
		GL_VERTEX_ATTRIB_ARRAY_SIZE = 0x8623,
		GL_VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624,
		GL_VERTEX_ATTRIB_ARRAY_TYPE = 0x8625,
		GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A,
		GL_CURRENT_VERTEX_ATTRIB = 0x8626;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVertexAttribPointervARB: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;

	/**
	 *  Accepted by the &lt;shaderType&gt; argument of CreateShader and
	 *  returned by the &lt;params&gt; parameter of GetShader{fi}vARB:
	 */
	public static final int GL_FRAGMENT_SHADER = 0x8B30;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8B49;

	/**
	 *  Accepted by the &lt;target&gt; parameter of Hint and the &lt;pname&gt; parameter of
	 *  GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev:
	 */
	public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 0x8B8B;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_MAX_DRAW_BUFFERS = 0x8824,
		GL_DRAW_BUFFER0 = 0x8825,
		GL_DRAW_BUFFER1 = 0x8826,
		GL_DRAW_BUFFER2 = 0x8827,
		GL_DRAW_BUFFER3 = 0x8828,
		GL_DRAW_BUFFER4 = 0x8829,
		GL_DRAW_BUFFER5 = 0x882A,
		GL_DRAW_BUFFER6 = 0x882B,
		GL_DRAW_BUFFER7 = 0x882C,
		GL_DRAW_BUFFER8 = 0x882D,
		GL_DRAW_BUFFER9 = 0x882E,
		GL_DRAW_BUFFER10 = 0x882F,
		GL_DRAW_BUFFER11 = 0x8830,
		GL_DRAW_BUFFER12 = 0x8831,
		GL_DRAW_BUFFER13 = 0x8832,
		GL_DRAW_BUFFER14 = 0x8833,
		GL_DRAW_BUFFER15 = 0x8834;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev, and by the &lt;target&gt; parameter of TexEnvi, TexEnviv,
	 *  TexEnvf, TexEnvfv, GetTexEnviv, and GetTexEnvfv:
	 */
	public static final int GL_POINT_SPRITE = 0x8861;

	/**
	 *  When the &lt;target&gt; parameter of TexEnvf, TexEnvfv, TexEnvi, TexEnviv,
	 *  GetTexEnvfv, or GetTexEnviv is POINT_SPRITE, then the value of
	 *  &lt;pname&gt; may be:
	 */
	public static final int GL_COORD_REPLACE = 0x8862;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of PointParameter{if}vARB, and the
	 *  &lt;pname&gt; of Get:
	 */
	public static final int GL_POINT_SPRITE_COORD_ORIGIN = 0x8CA0;

	/**
	 * Accepted by the &lt;param&gt; parameter of PointParameter{if}vARB: 
	 */
	public static final int GL_LOWER_LEFT = 0x8CA1,
		GL_UPPER_LEFT = 0x8CA2,
		GL_STENCIL_BACK_FUNC = 0x8800,
		GL_STENCIL_BACK_FAIL = 0x8801,
		GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802,
		GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803,
		GL_STENCIL_BACK_REF = 0x8CA3,
		GL_STENCIL_BACK_VALUE_MASK = 0x8CA4,
		GL_STENCIL_BACK_WRITEMASK = 0x8CA5,
		GL_BLEND_EQUATION_RGB = 0x8009,
		GL_BLEND_EQUATION_ALPHA = 0x883D;

	private GL20() {}

	/**
	 *  The ARB_shader_objects extension allows multiple, optionally null-terminated, source strings to define a shader program.
	 *  <p/>
	 *  This method uses just a single string, that should NOT be null-terminated.
	 * <p>
	 *  @param shader
	 *  @param string
	 */
	public static void glShaderSource(int shader, ByteBuffer string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShaderSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(string);
		nglShaderSource(shader, 1, MemoryUtil.getAddress(string), string.remaining(), function_pointer);
	}
	static native void nglShaderSource(int shader, int count, long string, int string_length, long function_pointer);

	/** Overloads glShaderSource. */
	public static void glShaderSource(int shader, CharSequence string) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShaderSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglShaderSource(shader, 1, APIUtil.getBuffer(caps, string), string.length(), function_pointer);
	}

	/** Overloads glShaderSource. */
	public static void glShaderSource(int shader, CharSequence[] strings) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glShaderSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(strings);
		nglShaderSource3(shader, strings.length, APIUtil.getBuffer(caps, strings), APIUtil.getLengths(caps, strings), function_pointer);
	}
	static native void nglShaderSource3(int shader, int count, long strings, long length, long function_pointer);

	public static int glCreateShader(int type) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateShader;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCreateShader(type, function_pointer);
		return __result;
	}
	static native int nglCreateShader(int type, long function_pointer);

	public static boolean glIsShader(int shader) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsShader;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsShader(shader, function_pointer);
		return __result;
	}
	static native boolean nglIsShader(int shader, long function_pointer);

	public static void glCompileShader(int shader) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCompileShader;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCompileShader(shader, function_pointer);
	}
	static native void nglCompileShader(int shader, long function_pointer);

	public static void glDeleteShader(int shader) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteShader;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteShader(shader, function_pointer);
	}
	static native void nglDeleteShader(int shader, long function_pointer);

	public static int glCreateProgram() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCreateProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCreateProgram(function_pointer);
		return __result;
	}
	static native int nglCreateProgram(long function_pointer);

	public static boolean glIsProgram(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsProgram(program, function_pointer);
		return __result;
	}
	static native boolean nglIsProgram(int program, long function_pointer);

	public static void glAttachShader(int program, int shader) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glAttachShader;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglAttachShader(program, shader, function_pointer);
	}
	static native void nglAttachShader(int program, int shader, long function_pointer);

	public static void glDetachShader(int program, int shader) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDetachShader;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDetachShader(program, shader, function_pointer);
	}
	static native void nglDetachShader(int program, int shader, long function_pointer);

	public static void glLinkProgram(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glLinkProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglLinkProgram(program, function_pointer);
	}
	static native void nglLinkProgram(int program, long function_pointer);

	public static void glUseProgram(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUseProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUseProgram(program, function_pointer);
	}
	static native void nglUseProgram(int program, long function_pointer);

	public static void glValidateProgram(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glValidateProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglValidateProgram(program, function_pointer);
	}
	static native void nglValidateProgram(int program, long function_pointer);

	public static void glDeleteProgram(int program) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteProgram;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteProgram(program, function_pointer);
	}
	static native void nglDeleteProgram(int program, long function_pointer);

	public static void glUniform1f(int location, float v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform1f(location, v0, function_pointer);
	}
	static native void nglUniform1f(int location, float v0, long function_pointer);

	public static void glUniform2f(int location, float v0, float v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform2f(location, v0, v1, function_pointer);
	}
	static native void nglUniform2f(int location, float v0, float v1, long function_pointer);

	public static void glUniform3f(int location, float v0, float v1, float v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform3f(location, v0, v1, v2, function_pointer);
	}
	static native void nglUniform3f(int location, float v0, float v1, float v2, long function_pointer);

	public static void glUniform4f(int location, float v0, float v1, float v2, float v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform4f(location, v0, v1, v2, v3, function_pointer);
	}
	static native void nglUniform4f(int location, float v0, float v1, float v2, float v3, long function_pointer);

	public static void glUniform1i(int location, int v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform1i(location, v0, function_pointer);
	}
	static native void nglUniform1i(int location, int v0, long function_pointer);

	public static void glUniform2i(int location, int v0, int v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform2i(location, v0, v1, function_pointer);
	}
	static native void nglUniform2i(int location, int v0, int v1, long function_pointer);

	public static void glUniform3i(int location, int v0, int v1, int v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform3i(location, v0, v1, v2, function_pointer);
	}
	static native void nglUniform3i(int location, int v0, int v1, int v2, long function_pointer);

	public static void glUniform4i(int location, int v0, int v1, int v2, int v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4i;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform4i(location, v0, v1, v2, v3, function_pointer);
	}
	static native void nglUniform4i(int location, int v0, int v1, int v2, int v3, long function_pointer);

	public static void glUniform1(int location, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglUniform1fv(location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglUniform1fv(int location, int values_count, long values, long function_pointer);

	public static void glUniform2(int location, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglUniform2fv(location, values.remaining() >> 1, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglUniform2fv(int location, int values_count, long values, long function_pointer);

	public static void glUniform3(int location, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglUniform3fv(location, values.remaining() / 3, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglUniform3fv(int location, int values_count, long values, long function_pointer);

	public static void glUniform4(int location, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglUniform4fv(location, values.remaining() >> 2, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglUniform4fv(int location, int values_count, long values, long function_pointer);

	public static void glUniform1(int location, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglUniform1iv(location, values.remaining(), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglUniform1iv(int location, int values_count, long values, long function_pointer);

	public static void glUniform2(int location, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglUniform2iv(location, values.remaining() >> 1, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglUniform2iv(int location, int values_count, long values, long function_pointer);

	public static void glUniform3(int location, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglUniform3iv(location, values.remaining() / 3, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglUniform3iv(int location, int values_count, long values, long function_pointer);

	public static void glUniform4(int location, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4iv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(values);
		nglUniform4iv(location, values.remaining() >> 2, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglUniform4iv(int location, int values_count, long values, long function_pointer);

	public static void glUniformMatrix2(int location, boolean transpose, FloatBuffer matrices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix2fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix2fv(location, matrices.remaining() >> 2, transpose, MemoryUtil.getAddress(matrices), function_pointer);
	}
	static native void nglUniformMatrix2fv(int location, int matrices_count, boolean transpose, long matrices, long function_pointer);

	public static void glUniformMatrix3(int location, boolean transpose, FloatBuffer matrices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix3fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix3fv(location, matrices.remaining() / (3 * 3), transpose, MemoryUtil.getAddress(matrices), function_pointer);
	}
	static native void nglUniformMatrix3fv(int location, int matrices_count, boolean transpose, long matrices, long function_pointer);

	public static void glUniformMatrix4(int location, boolean transpose, FloatBuffer matrices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix4fv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(matrices);
		nglUniformMatrix4fv(location, matrices.remaining() >> 4, transpose, MemoryUtil.getAddress(matrices), function_pointer);
	}
	static native void nglUniformMatrix4fv(int location, int matrices_count, boolean transpose, long matrices, long function_pointer);

	public static void glGetShader(int shader, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetShaderiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetShaderiv(shader, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetShaderiv(int shader, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetShaderiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetShaderi} instead. 
	 */
	@Deprecated
	public static int glGetShader(int shader, int pname) {
		return GL20.glGetShaderi(shader, pname);
	}

	/** Overloads glGetShaderiv. */
	public static int glGetShaderi(int shader, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetShaderiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetShaderiv(shader, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetProgram(int program, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetProgramiv(program, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetProgramiv(int program, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetProgramiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetProgrami} instead. 
	 */
	@Deprecated
	public static int glGetProgram(int program, int pname) {
		return GL20.glGetProgrami(program, pname);
	}

	/** Overloads glGetProgramiv. */
	public static int glGetProgrami(int program, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetProgramiv(program, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetShaderInfoLog(int shader, IntBuffer length, ByteBuffer infoLog) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetShaderInfoLog;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(infoLog);
		nglGetShaderInfoLog(shader, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog), function_pointer);
	}
	static native void nglGetShaderInfoLog(int shader, int infoLog_maxLength, long length, long infoLog, long function_pointer);

	/** Overloads glGetShaderInfoLog. */
	public static String glGetShaderInfoLog(int shader, int maxLength) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetShaderInfoLog;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer infoLog_length = APIUtil.getLengths(caps);
		ByteBuffer infoLog = APIUtil.getBufferByte(caps, maxLength);
		nglGetShaderInfoLog(shader, maxLength, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog), function_pointer);
		infoLog.limit(infoLog_length.get(0));
		return APIUtil.getString(caps, infoLog);
	}

	public static void glGetProgramInfoLog(int program, IntBuffer length, ByteBuffer infoLog) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramInfoLog;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(infoLog);
		nglGetProgramInfoLog(program, infoLog.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(infoLog), function_pointer);
	}
	static native void nglGetProgramInfoLog(int program, int infoLog_maxLength, long length, long infoLog, long function_pointer);

	/** Overloads glGetProgramInfoLog. */
	public static String glGetProgramInfoLog(int program, int maxLength) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramInfoLog;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer infoLog_length = APIUtil.getLengths(caps);
		ByteBuffer infoLog = APIUtil.getBufferByte(caps, maxLength);
		nglGetProgramInfoLog(program, maxLength, MemoryUtil.getAddress0(infoLog_length), MemoryUtil.getAddress(infoLog), function_pointer);
		infoLog.limit(infoLog_length.get(0));
		return APIUtil.getString(caps, infoLog);
	}

	public static void glGetAttachedShaders(int program, IntBuffer count, IntBuffer shaders) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetAttachedShaders;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (count != null)
			BufferChecks.checkBuffer(count, 1);
		BufferChecks.checkDirect(shaders);
		nglGetAttachedShaders(program, shaders.remaining(), MemoryUtil.getAddressSafe(count), MemoryUtil.getAddress(shaders), function_pointer);
	}
	static native void nglGetAttachedShaders(int program, int shaders_maxCount, long count, long shaders, long function_pointer);

	/**
	 *  Returns the location of the uniform with the specified name. The ByteBuffer should contain the uniform name as a
	 *  <b>null-terminated</b> string.
	 * <p>
	 *  @param program
	 *  @param name
	 */
	public static int glGetUniformLocation(int program, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(name, 1);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetUniformLocation(program, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetUniformLocation(int program, long name, long function_pointer);

	/** Overloads glGetUniformLocation. */
	public static int glGetUniformLocation(int program, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetUniformLocation(program, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static void glGetActiveUniform(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniform;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		BufferChecks.checkDirect(name);
		nglGetActiveUniform(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglGetActiveUniform(int program, int index, int name_maxLength, long length, long size, long type, long name, long function_pointer);

	/**
	 * Overloads glGetActiveUniform.
	 * <p>
	 * Overloads glGetActiveUniform. This version returns both size and type in the sizeType buffer (at .position() and .position() + 1). 
	 */
	public static String glGetActiveUniform(int program, int index, int maxLength, IntBuffer sizeType) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniform;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(sizeType, 2);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
		nglGetActiveUniform(program, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	/**
	 * Overloads glGetActiveUniform.
	 * <p>
	 * Overloads glGetActiveUniformARB. This version returns only the uniform name. 
	 */
	public static String glGetActiveUniform(int program, int index, int maxLength) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniform;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
		nglGetActiveUniform(program, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	/**
	 * Overloads glGetActiveUniform.
	 * <p>
	 * Overloads glGetActiveUniform. This version returns only the uniform size. 
	 */
	public static int glGetActiveUniformSize(int program, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniform;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer size = APIUtil.getBufferInt(caps);
		nglGetActiveUniform(program, index, 1, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0(caps), function_pointer);
		return size.get(0);
	}

	/**
	 * Overloads glGetActiveUniform.
	 * <p>
	 * Overloads glGetActiveUniform. This version returns only the uniform type. 
	 */
	public static int glGetActiveUniformType(int program, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniform;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer type = APIUtil.getBufferInt(caps);
		nglGetActiveUniform(program, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0(caps), function_pointer);
		return type.get(0);
	}

	public static void glGetUniform(int program, int location, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetUniformfv(program, location, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetUniformfv(int program, int location, long params, long function_pointer);

	public static void glGetUniform(int program, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetUniformiv(program, location, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetUniformiv(int program, int location, long params, long function_pointer);

	public static void glGetShaderSource(int shader, IntBuffer length, ByteBuffer source) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetShaderSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(source);
		nglGetShaderSource(shader, source.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(source), function_pointer);
	}
	static native void nglGetShaderSource(int shader, int source_maxLength, long length, long source, long function_pointer);

	/** Overloads glGetShaderSource. */
	public static String glGetShaderSource(int shader, int maxLength) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetShaderSource;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer source_length = APIUtil.getLengths(caps);
		ByteBuffer source = APIUtil.getBufferByte(caps, maxLength);
		nglGetShaderSource(shader, maxLength, MemoryUtil.getAddress0(source_length), MemoryUtil.getAddress(source), function_pointer);
		source.limit(source_length.get(0));
		return APIUtil.getString(caps, source);
	}

	public static void glVertexAttrib1s(int index, short x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1s;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1s(index, x, function_pointer);
	}
	static native void nglVertexAttrib1s(int index, short x, long function_pointer);

	public static void glVertexAttrib1f(int index, float x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1f(index, x, function_pointer);
	}
	static native void nglVertexAttrib1f(int index, float x, long function_pointer);

	public static void glVertexAttrib1d(int index, double x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib1d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib1d(index, x, function_pointer);
	}
	static native void nglVertexAttrib1d(int index, double x, long function_pointer);

	public static void glVertexAttrib2s(int index, short x, short y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2s;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2s(index, x, y, function_pointer);
	}
	static native void nglVertexAttrib2s(int index, short x, short y, long function_pointer);

	public static void glVertexAttrib2f(int index, float x, float y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2f(index, x, y, function_pointer);
	}
	static native void nglVertexAttrib2f(int index, float x, float y, long function_pointer);

	public static void glVertexAttrib2d(int index, double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib2d(index, x, y, function_pointer);
	}
	static native void nglVertexAttrib2d(int index, double x, double y, long function_pointer);

	public static void glVertexAttrib3s(int index, short x, short y, short z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3s;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3s(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttrib3s(int index, short x, short y, short z, long function_pointer);

	public static void glVertexAttrib3f(int index, float x, float y, float z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3f(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttrib3f(int index, float x, float y, float z, long function_pointer);

	public static void glVertexAttrib3d(int index, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib3d(index, x, y, z, function_pointer);
	}
	static native void nglVertexAttrib3d(int index, double x, double y, double z, long function_pointer);

	public static void glVertexAttrib4s(int index, short x, short y, short z, short w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4s;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4s(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4s(int index, short x, short y, short z, short w, long function_pointer);

	public static void glVertexAttrib4f(int index, float x, float y, float z, float w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4f;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4f(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4f(int index, float x, float y, float z, float w, long function_pointer);

	public static void glVertexAttrib4d(int index, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4d(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4d(int index, double x, double y, double z, double w, long function_pointer);

	public static void glVertexAttrib4Nub(int index, byte x, byte y, byte z, byte w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttrib4Nub;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttrib4Nub(index, x, y, z, w, function_pointer);
	}
	static native void nglVertexAttrib4Nub(int index, byte x, byte y, byte z, byte w, long function_pointer);

	public static void glVertexAttribPointer(int index, int size, boolean normalized, int stride, DoubleBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, GL11.GL_DOUBLE, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointer(int index, int size, boolean normalized, int stride, FloatBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, GL11.GL_FLOAT, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, ByteBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, unsigned ? GL11.GL_UNSIGNED_BYTE : GL11.GL_BYTE, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, IntBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, unsigned ? GL11.GL_UNSIGNED_INT : GL11.GL_INT, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	public static void glVertexAttribPointer(int index, int size, boolean unsigned, boolean normalized, int stride, ShortBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, unsigned ? GL11.GL_UNSIGNED_SHORT : GL11.GL_SHORT, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}
	static native void nglVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long buffer, long function_pointer);
	public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexAttribPointerBO(index, size, type, normalized, stride, buffer_buffer_offset, function_pointer);
	}
	static native void nglVertexAttribPointerBO(int index, int size, int type, boolean normalized, int stride, long buffer_buffer_offset, long function_pointer);

	/** Overloads glVertexAttribPointer. */
	public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, ByteBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribPointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		if ( LWJGLUtil.CHECKS ) StateTracker.getReferences(caps).glVertexAttribPointer_buffer[index] = buffer;
		nglVertexAttribPointer(index, size, type, normalized, stride, MemoryUtil.getAddress(buffer), function_pointer);
	}

	public static void glEnableVertexAttribArray(int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEnableVertexAttribArray;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnableVertexAttribArray(index, function_pointer);
	}
	static native void nglEnableVertexAttribArray(int index, long function_pointer);

	public static void glDisableVertexAttribArray(int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDisableVertexAttribArray;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisableVertexAttribArray(index, function_pointer);
	}
	static native void nglDisableVertexAttribArray(int index, long function_pointer);

	public static void glGetVertexAttrib(int index, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribfv(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribfv(int index, int pname, long params, long function_pointer);

	public static void glGetVertexAttrib(int index, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribdv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribdv(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribdv(int index, int pname, long params, long function_pointer);

	public static void glGetVertexAttrib(int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetVertexAttribiv(index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVertexAttribiv(int index, int pname, long params, long function_pointer);

	public static ByteBuffer glGetVertexAttribPointer(int index, int pname, long result_size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribPointerv;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer __result = nglGetVertexAttribPointerv(index, pname, result_size, function_pointer);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglGetVertexAttribPointerv(int index, int pname, long result_size, long function_pointer);

	/** Overloads glGetVertexAttribPointerv. */
	public static void glGetVertexAttribPointer(int index, int pname, ByteBuffer pointer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVertexAttribPointerv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(pointer, PointerBuffer.getPointerSize());
		nglGetVertexAttribPointerv2(index, pname, MemoryUtil.getAddress(pointer), function_pointer);
	}
	static native void nglGetVertexAttribPointerv2(int index, int pname, long pointer, long function_pointer);

	public static void glBindAttribLocation(int program, int index, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindAttribLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		nglBindAttribLocation(program, index, MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglBindAttribLocation(int program, int index, long name, long function_pointer);

	/** Overloads glBindAttribLocation. */
	public static void glBindAttribLocation(int program, int index, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindAttribLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindAttribLocation(program, index, APIUtil.getBufferNT(caps, name), function_pointer);
	}

	public static void glGetActiveAttrib(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		BufferChecks.checkDirect(name);
		nglGetActiveAttrib(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglGetActiveAttrib(int program, int index, int name_maxLength, long length, long size, long type, long name, long function_pointer);

	/**
	 * Overloads glGetActiveAttrib.
	 * <p>
	 * Overloads glGetActiveAttrib. This version returns both size and type in the sizeType buffer (at .position() and .position() + 1). 
	 */
	public static String glGetActiveAttrib(int program, int index, int maxLength, IntBuffer sizeType) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(sizeType, 2);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
		nglGetActiveAttrib(program, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(sizeType), MemoryUtil.getAddress(sizeType, sizeType.position() + 1), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	/**
	 * Overloads glGetActiveAttrib.
	 * <p>
	 * Overloads glGetActiveAttrib. This version returns only the attrib name. 
	 */
	public static String glGetActiveAttrib(int program, int index, int maxLength) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, maxLength);
		nglGetActiveAttrib(program, index, maxLength, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress0(APIUtil.getBufferInt(caps)), MemoryUtil.getAddress(APIUtil.getBufferInt(caps), 1), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	/**
	 * Overloads glGetActiveAttrib.
	 * <p>
	 * Overloads glGetActiveAttribARB. This version returns only the attrib size. 
	 */
	public static int glGetActiveAttribSize(int program, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer size = APIUtil.getBufferInt(caps);
		nglGetActiveAttrib(program, index, 0, 0L, MemoryUtil.getAddress(size), MemoryUtil.getAddress(size, 1), APIUtil.getBufferByte0(caps), function_pointer);
		return size.get(0);
	}

	/**
	 * Overloads glGetActiveAttrib.
	 * <p>
	 * Overloads glGetActiveAttrib. This version returns only the attrib type. 
	 */
	public static int glGetActiveAttribType(int program, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAttrib;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer type = APIUtil.getBufferInt(caps);
		nglGetActiveAttrib(program, index, 0, 0L, MemoryUtil.getAddress(type, 1), MemoryUtil.getAddress(type), APIUtil.getBufferByte0(caps), function_pointer);
		return type.get(0);
	}

	public static int glGetAttribLocation(int program, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetAttribLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetAttribLocation(program, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetAttribLocation(int program, long name, long function_pointer);

	/** Overloads glGetAttribLocation. */
	public static int glGetAttribLocation(int program, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetAttribLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetAttribLocation(program, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static void glDrawBuffers(IntBuffer buffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(buffers);
		nglDrawBuffers(buffers.remaining(), MemoryUtil.getAddress(buffers), function_pointer);
	}
	static native void nglDrawBuffers(int buffers_size, long buffers, long function_pointer);

	/** Overloads glDrawBuffers. */
	public static void glDrawBuffers(int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawBuffers;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawBuffers(1, APIUtil.getInt(caps, buffer), function_pointer);
	}

	public static void glStencilOpSeparate(int face, int sfail, int dpfail, int dppass) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilOpSeparate;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilOpSeparate(face, sfail, dpfail, dppass, function_pointer);
	}
	static native void nglStencilOpSeparate(int face, int sfail, int dpfail, int dppass, long function_pointer);

	public static void glStencilFuncSeparate(int face, int func, int ref, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilFuncSeparate;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilFuncSeparate(face, func, ref, mask, function_pointer);
	}
	static native void nglStencilFuncSeparate(int face, int func, int ref, int mask, long function_pointer);

	public static void glStencilMaskSeparate(int face, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glStencilMaskSeparate;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglStencilMaskSeparate(face, mask, function_pointer);
	}
	static native void nglStencilMaskSeparate(int face, int mask, long function_pointer);

	public static void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendEquationSeparate;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendEquationSeparate(modeRGB, modeAlpha, function_pointer);
	}
	static native void nglBlendEquationSeparate(int modeRGB, int modeAlpha, long function_pointer);
}
