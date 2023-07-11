/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL40 {

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

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_GEOMETRY_SHADER_INVOCATIONS = 0x887F;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  GetDoublev, and GetInteger64v:
	 */
	public static final int GL_MAX_GEOMETRY_SHADER_INVOCATIONS = 0x8E5A,
		GL_MIN_FRAGMENT_INTERPOLATION_OFFSET = 0x8E5B,
		GL_MAX_FRAGMENT_INTERPOLATION_OFFSET = 0x8E5C,
		GL_FRAGMENT_INTERPOLATION_OFFSET_BITS = 0x8E5D,
		GL_MAX_VERTEX_STREAMS = 0x8E71;

	/**
	 *  Returned in the &lt;type&gt; parameter of GetActiveUniform, and
	 *  GetTransformFeedbackVarying:
	 */
	public static final int GL_DOUBLE_VEC2 = 0x8FFC,
		GL_DOUBLE_VEC3 = 0x8FFD,
		GL_DOUBLE_VEC4 = 0x8FFE,
		GL_DOUBLE_MAT2 = 0x8F46,
		GL_DOUBLE_MAT3 = 0x8F47,
		GL_DOUBLE_MAT4 = 0x8F48,
		GL_DOUBLE_MAT2x3 = 0x8F49,
		GL_DOUBLE_MAT2x4 = 0x8F4A,
		GL_DOUBLE_MAT3x2 = 0x8F4B,
		GL_DOUBLE_MAT3x4 = 0x8F4C,
		GL_DOUBLE_MAT4x2 = 0x8F4D,
		GL_DOUBLE_MAT4x3 = 0x8F4E;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 *  and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 *  and GetDoublev:
	 */
	public static final int GL_SAMPLE_SHADING = 0x8C36;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev,
	 *  GetIntegerv, and GetFloatv:
	 */
	public static final int GL_MIN_SAMPLE_SHADING_VALUE = 0x8C37;

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

	/**
	 *  Accepted by the &lt;mode&gt; parameter of Begin and all vertex array functions
	 *  that implicitly call Begin:
	 */
	public static final int GL_PATCHES = 0xE;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of PatchParameteri, GetBooleanv,
	 *  GetDoublev, GetFloatv, GetIntegerv, and GetInteger64v:
	 */
	public static final int GL_PATCH_VERTICES = 0x8E72;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of PatchParameterfv, GetBooleanv,
	 *  GetDoublev, GetFloatv, and GetIntegerv, and GetInteger64v:
	 */
	public static final int GL_PATCH_DEFAULT_INNER_LEVEL = 0x8E73,
		GL_PATCH_DEFAULT_OUTER_LEVEL = 0x8E74;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_TESS_CONTROL_OUTPUT_VERTICES = 0x8E75,
		GL_TESS_GEN_MODE = 0x8E76,
		GL_TESS_GEN_SPACING = 0x8E77,
		GL_TESS_GEN_VERTEX_ORDER = 0x8E78,
		GL_TESS_GEN_POINT_MODE = 0x8E79;

	/**
	 * Returned by GetProgramiv when &lt;pname&gt; is TESS_GEN_MODE: 
	 */
	public static final int GL_ISOLINES = 0x8E7A;

	/**
	 * Returned by GetProgramiv when &lt;pname&gt; is TESS_GEN_SPACING: 
	 */
	public static final int GL_FRACTIONAL_ODD = 0x8E7B,
		GL_FRACTIONAL_EVEN = 0x8E7C;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetFloatv,
	 *  GetIntegerv, and GetInteger64v:
	 */
	public static final int GL_MAX_PATCH_VERTICES = 0x8E7D,
		GL_MAX_TESS_GEN_LEVEL = 0x8E7E,
		GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E7F,
		GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E80,
		GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 0x8E81,
		GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 0x8E82,
		GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 0x8E83,
		GL_MAX_TESS_PATCH_COMPONENTS = 0x8E84,
		GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 0x8E85,
		GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 0x8E86,
		GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 0x8E89,
		GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 0x8E8A,
		GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 0x886C,
		GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 0x886D,
		GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E1E,
		GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E1F;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformBlockiv: 
	 */
	public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 0x84F0,
		GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x84F1;

	/**
	 *  Accepted by the &lt;type&gt; parameter of CreateShader and returned by the
	 *  &lt;params&gt; parameter of GetShaderiv:
	 */
	public static final int GL_TESS_EVALUATION_SHADER = 0x8E87,
		GL_TESS_CONTROL_SHADER = 0x8E88;

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexParameteri, TexParameteriv,
	 *  TexParameterf, TexParameterfv, BindTexture, and GenerateMipmap:
	 *  <p/>
	 *  Accepted by the &lt;target&gt; parameter of TexImage3D, TexSubImage3D,
	 *  CompressedTeximage3D, CompressedTexSubImage3D and CopyTexSubImage3D:
	 *  <p/>
	 *  Accepted by the &lt;tex&gt; parameter of GetTexImage:
	 */
	public static final int GL_TEXTURE_CUBE_MAP_ARRAY = 0x9009;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev,
	 *  GetIntegerv and GetFloatv:
	 */
	public static final int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = 0x900A;

	/**
	 *  Accepted by the &lt;target&gt; parameter of TexImage3D, TexSubImage3D,
	 *  CompressedTeximage3D, CompressedTexSubImage3D and CopyTexSubImage3D:
	 */
	public static final int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = 0x900B;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_CUBE_MAP_ARRAY = 0x900C,
		GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW = 0x900D,
		GL_INT_SAMPLER_CUBE_MAP_ARRAY = 0x900E,
		GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 0x900F;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET_ARB = 0x8E5E,
		GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET_ARB = 0x8E5F,
		GL_MAX_PROGRAM_TEXTURE_GATHER_COMPONENTS_ARB = 0x8F9F;

	/**
	 * Accepted by the &lt;target&gt; parameter of BindTransformFeedback: 
	 */
	public static final int GL_TRANSFORM_FEEDBACK = 0x8E22;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_PAUSED = 0x8E23,
		GL_TRANSFORM_FEEDBACK_ACTIVE = 0x8E24,
		GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 0x8E23,
		GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 0x8E24,
		GL_TRANSFORM_FEEDBACK_BINDING = 0x8E25;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 0x8E70;

	private GL40() {}

	public static void glBlendEquationi(int buf, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendEquationi;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendEquationi(buf, mode, function_pointer);
	}
	static native void nglBlendEquationi(int buf, int mode, long function_pointer);

	public static void glBlendEquationSeparatei(int buf, int modeRGB, int modeAlpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendEquationSeparatei;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendEquationSeparatei(buf, modeRGB, modeAlpha, function_pointer);
	}
	static native void nglBlendEquationSeparatei(int buf, int modeRGB, int modeAlpha, long function_pointer);

	public static void glBlendFunci(int buf, int src, int dst) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendFunci;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendFunci(buf, src, dst, function_pointer);
	}
	static native void nglBlendFunci(int buf, int src, int dst, long function_pointer);

	public static void glBlendFuncSeparatei(int buf, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBlendFuncSeparatei;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlendFuncSeparatei(buf, srcRGB, dstRGB, srcAlpha, dstAlpha, function_pointer);
	}
	static native void nglBlendFuncSeparatei(int buf, int srcRGB, int dstRGB, int srcAlpha, int dstAlpha, long function_pointer);

	public static void glDrawArraysIndirect(int mode, ByteBuffer indirect) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawArraysIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, 4 * 4);
		nglDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), function_pointer);
	}
	static native void nglDrawArraysIndirect(int mode, long indirect, long function_pointer);
	public static void glDrawArraysIndirect(int mode, long indirect_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawArraysIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglDrawArraysIndirectBO(mode, indirect_buffer_offset, function_pointer);
	}
	static native void nglDrawArraysIndirectBO(int mode, long indirect_buffer_offset, long function_pointer);

	/** Overloads glDrawArraysIndirect. */
	public static void glDrawArraysIndirect(int mode, IntBuffer indirect) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawArraysIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, 4);
		nglDrawArraysIndirect(mode, MemoryUtil.getAddress(indirect), function_pointer);
	}

	public static void glDrawElementsIndirect(int mode, int type, ByteBuffer indirect) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, 5 * 4);
		nglDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), function_pointer);
	}
	static native void nglDrawElementsIndirect(int mode, int type, long indirect, long function_pointer);
	public static void glDrawElementsIndirect(int mode, int type, long indirect_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOenabled(caps);
		nglDrawElementsIndirectBO(mode, type, indirect_buffer_offset, function_pointer);
	}
	static native void nglDrawElementsIndirectBO(int mode, int type, long indirect_buffer_offset, long function_pointer);

	/** Overloads glDrawElementsIndirect. */
	public static void glDrawElementsIndirect(int mode, int type, IntBuffer indirect) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsIndirect;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureIndirectBOdisabled(caps);
		BufferChecks.checkBuffer(indirect, 5);
		nglDrawElementsIndirect(mode, type, MemoryUtil.getAddress(indirect), function_pointer);
	}

	public static void glUniform1d(int location, double x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform1d(location, x, function_pointer);
	}
	static native void nglUniform1d(int location, double x, long function_pointer);

	public static void glUniform2d(int location, double x, double y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform2d(location, x, y, function_pointer);
	}
	static native void nglUniform2d(int location, double x, double y, long function_pointer);

	public static void glUniform3d(int location, double x, double y, double z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform3d(location, x, y, z, function_pointer);
	}
	static native void nglUniform3d(int location, double x, double y, double z, long function_pointer);

	public static void glUniform4d(int location, double x, double y, double z, double w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4d;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform4d(location, x, y, z, w, function_pointer);
	}
	static native void nglUniform4d(int location, double x, double y, double z, double w, long function_pointer);

	public static void glUniform1(int location, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform1dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform1dv(location, value.remaining(), MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform1dv(int location, int value_count, long value, long function_pointer);

	public static void glUniform2(int location, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform2dv(location, value.remaining() >> 1, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform2dv(int location, int value_count, long value, long function_pointer);

	public static void glUniform3(int location, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform3dv(location, value.remaining() / 3, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform3dv(int location, int value_count, long value, long function_pointer);

	public static void glUniform4(int location, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniform4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniform4dv(location, value.remaining() >> 2, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniform4dv(int location, int value_count, long value, long function_pointer);

	public static void glUniformMatrix2(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix2dv(location, value.remaining() >> 2, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix2dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glUniformMatrix3(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix3dv(location, value.remaining() / (3 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix3dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glUniformMatrix4(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix4dv(location, value.remaining() >> 4, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix4dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glUniformMatrix2x3(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix2x3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix2x3dv(location, value.remaining() / (2 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix2x3dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glUniformMatrix2x4(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix2x4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix2x4dv(location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix2x4dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glUniformMatrix3x2(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix3x2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix3x2dv(location, value.remaining() / (3 * 2), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix3x2dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glUniformMatrix3x4(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix3x4dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix3x4dv(location, value.remaining() / (3 * 4), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix3x4dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glUniformMatrix4x2(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix4x2dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix4x2dv(location, value.remaining() >> 3, transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix4x2dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glUniformMatrix4x3(int location, boolean transpose, DoubleBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformMatrix4x3dv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(value);
		nglUniformMatrix4x3dv(location, value.remaining() / (4 * 3), transpose, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglUniformMatrix4x3dv(int location, int value_count, boolean transpose, long value, long function_pointer);

	public static void glGetUniform(int program, int location, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformdv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetUniformdv(program, location, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetUniformdv(int program, int location, long params, long function_pointer);

	public static void glMinSampleShading(float value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMinSampleShading;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMinSampleShading(value, function_pointer);
	}
	static native void nglMinSampleShading(float value, long function_pointer);

	public static int glGetSubroutineUniformLocation(int program, int shadertype, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSubroutineUniformLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetSubroutineUniformLocation(program, shadertype, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetSubroutineUniformLocation(int program, int shadertype, long name, long function_pointer);

	/** Overloads glGetSubroutineUniformLocation. */
	public static int glGetSubroutineUniformLocation(int program, int shadertype, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSubroutineUniformLocation;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetSubroutineUniformLocation(program, shadertype, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static int glGetSubroutineIndex(int program, int shadertype, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSubroutineIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetSubroutineIndex(program, shadertype, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetSubroutineIndex(int program, int shadertype, long name, long function_pointer);

	/** Overloads glGetSubroutineIndex. */
	public static int glGetSubroutineIndex(int program, int shadertype, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSubroutineIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetSubroutineIndex(program, shadertype, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static void glGetActiveSubroutineUniform(int program, int shadertype, int index, int pname, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveSubroutineUniformiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(values, 1);
		nglGetActiveSubroutineUniformiv(program, shadertype, index, pname, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetActiveSubroutineUniformiv(int program, int shadertype, int index, int pname, long values, long function_pointer);

	/**
	 * Overloads glGetActiveSubroutineUniformiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetActiveSubroutineUniformi} instead. 
	 */
	@Deprecated
	public static int glGetActiveSubroutineUniform(int program, int shadertype, int index, int pname) {
		return GL40.glGetActiveSubroutineUniformi(program, shadertype, index, pname);
	}

	/** Overloads glGetActiveSubroutineUniformiv. */
	public static int glGetActiveSubroutineUniformi(int program, int shadertype, int index, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveSubroutineUniformiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer values = APIUtil.getBufferInt(caps);
		nglGetActiveSubroutineUniformiv(program, shadertype, index, pname, MemoryUtil.getAddress(values), function_pointer);
		return values.get(0);
	}

	public static void glGetActiveSubroutineUniformName(int program, int shadertype, int index, IntBuffer length, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveSubroutineUniformName;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(name);
		nglGetActiveSubroutineUniformName(program, shadertype, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglGetActiveSubroutineUniformName(int program, int shadertype, int index, int name_bufsize, long length, long name, long function_pointer);

	/** Overloads glGetActiveSubroutineUniformName. */
	public static String glGetActiveSubroutineUniformName(int program, int shadertype, int index, int bufsize) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveSubroutineUniformName;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, bufsize);
		nglGetActiveSubroutineUniformName(program, shadertype, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	public static void glGetActiveSubroutineName(int program, int shadertype, int index, IntBuffer length, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveSubroutineName;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(name);
		nglGetActiveSubroutineName(program, shadertype, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglGetActiveSubroutineName(int program, int shadertype, int index, int name_bufsize, long length, long name, long function_pointer);

	/** Overloads glGetActiveSubroutineName. */
	public static String glGetActiveSubroutineName(int program, int shadertype, int index, int bufsize) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveSubroutineName;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, bufsize);
		nglGetActiveSubroutineName(program, shadertype, index, bufsize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}

	public static void glUniformSubroutinesu(int shadertype, IntBuffer indices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformSubroutinesuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(indices);
		nglUniformSubroutinesuiv(shadertype, indices.remaining(), MemoryUtil.getAddress(indices), function_pointer);
	}
	static native void nglUniformSubroutinesuiv(int shadertype, int indices_count, long indices, long function_pointer);

	public static void glGetUniformSubroutineu(int shadertype, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformSubroutineuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetUniformSubroutineuiv(shadertype, location, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetUniformSubroutineuiv(int shadertype, int location, long params, long function_pointer);

	/**
	 * Overloads glGetUniformSubroutineuiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetUniformSubroutineui} instead. 
	 */
	@Deprecated
	public static int glGetUniformSubroutineu(int shadertype, int location) {
		return GL40.glGetUniformSubroutineui(shadertype, location);
	}

	/** Overloads glGetUniformSubroutineuiv. */
	public static int glGetUniformSubroutineui(int shadertype, int location) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformSubroutineuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetUniformSubroutineuiv(shadertype, location, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetProgramStage(int program, int shadertype, int pname, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramStageiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(values, 1);
		nglGetProgramStageiv(program, shadertype, pname, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetProgramStageiv(int program, int shadertype, int pname, long values, long function_pointer);

	/**
	 * Overloads glGetProgramStageiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetProgramStagei} instead. 
	 */
	@Deprecated
	public static int glGetProgramStage(int program, int shadertype, int pname) {
		return GL40.glGetProgramStagei(program, shadertype, pname);
	}

	/** Overloads glGetProgramStageiv. */
	public static int glGetProgramStagei(int program, int shadertype, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetProgramStageiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer values = APIUtil.getBufferInt(caps);
		nglGetProgramStageiv(program, shadertype, pname, MemoryUtil.getAddress(values), function_pointer);
		return values.get(0);
	}

	public static void glPatchParameteri(int pname, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPatchParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPatchParameteri(pname, value, function_pointer);
	}
	static native void nglPatchParameteri(int pname, int value, long function_pointer);

	public static void glPatchParameter(int pname, FloatBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPatchParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(values, 4);
		nglPatchParameterfv(pname, MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglPatchParameterfv(int pname, long values, long function_pointer);

	public static void glBindTransformFeedback(int target, int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindTransformFeedback;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindTransformFeedback(target, id, function_pointer);
	}
	static native void nglBindTransformFeedback(int target, int id, long function_pointer);

	public static void glDeleteTransformFeedbacks(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteTransformFeedbacks;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglDeleteTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglDeleteTransformFeedbacks(int ids_n, long ids, long function_pointer);

	/** Overloads glDeleteTransformFeedbacks. */
	public static void glDeleteTransformFeedbacks(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteTransformFeedbacks;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteTransformFeedbacks(1, APIUtil.getInt(caps, id), function_pointer);
	}

	public static void glGenTransformFeedbacks(IntBuffer ids) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenTransformFeedbacks;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(ids);
		nglGenTransformFeedbacks(ids.remaining(), MemoryUtil.getAddress(ids), function_pointer);
	}
	static native void nglGenTransformFeedbacks(int ids_n, long ids, long function_pointer);

	/** Overloads glGenTransformFeedbacks. */
	public static int glGenTransformFeedbacks() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenTransformFeedbacks;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer ids = APIUtil.getBufferInt(caps);
		nglGenTransformFeedbacks(1, MemoryUtil.getAddress(ids), function_pointer);
		return ids.get(0);
	}

	public static boolean glIsTransformFeedback(int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsTransformFeedback;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsTransformFeedback(id, function_pointer);
		return __result;
	}
	static native boolean nglIsTransformFeedback(int id, long function_pointer);

	public static void glPauseTransformFeedback() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPauseTransformFeedback;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPauseTransformFeedback(function_pointer);
	}
	static native void nglPauseTransformFeedback(long function_pointer);

	public static void glResumeTransformFeedback() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glResumeTransformFeedback;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglResumeTransformFeedback(function_pointer);
	}
	static native void nglResumeTransformFeedback(long function_pointer);

	public static void glDrawTransformFeedback(int mode, int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawTransformFeedback;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawTransformFeedback(mode, id, function_pointer);
	}
	static native void nglDrawTransformFeedback(int mode, int id, long function_pointer);

	public static void glDrawTransformFeedbackStream(int mode, int id, int stream) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawTransformFeedbackStream;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawTransformFeedbackStream(mode, id, stream, function_pointer);
	}
	static native void nglDrawTransformFeedbackStream(int mode, int id, int stream, long function_pointer);

	public static void glBeginQueryIndexed(int target, int index, int id) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginQueryIndexed;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginQueryIndexed(target, index, id, function_pointer);
	}
	static native void nglBeginQueryIndexed(int target, int index, int id, long function_pointer);

	public static void glEndQueryIndexed(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndQueryIndexed;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndQueryIndexed(target, index, function_pointer);
	}
	static native void nglEndQueryIndexed(int target, int index, long function_pointer);

	public static void glGetQueryIndexed(int target, int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryIndexediv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryIndexediv(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryIndexediv(int target, int index, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetQueryIndexediv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetQueryIndexedi} instead. 
	 */
	@Deprecated
	public static int glGetQueryIndexed(int target, int index, int pname) {
		return GL40.glGetQueryIndexedi(target, index, pname);
	}

	/** Overloads glGetQueryIndexediv. */
	public static int glGetQueryIndexedi(int target, int index, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryIndexediv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetQueryIndexediv(target, index, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
