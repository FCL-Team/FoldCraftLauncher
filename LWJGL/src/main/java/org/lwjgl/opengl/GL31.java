/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL31 {

	public static final int GL_RED_SNORM = 0x8F90,
		GL_RG_SNORM = 0x8F91,
		GL_RGB_SNORM = 0x8F92,
		GL_RGBA_SNORM = 0x8F93,
		GL_R8_SNORM = 0x8F94,
		GL_RG8_SNORM = 0x8F95,
		GL_RGB8_SNORM = 0x8F96,
		GL_RGBA8_SNORM = 0x8F97,
		GL_R16_SNORM = 0x8F98,
		GL_RG16_SNORM = 0x8F99,
		GL_RGB16_SNORM = 0x8F9A,
		GL_RGBA16_SNORM = 0x8F9B,
		GL_SIGNED_NORMALIZED = 0x8F9C,
		GL_COPY_READ_BUFFER_BINDING = 0x8F36,
		GL_COPY_WRITE_BUFFER_BINDING = 0x8F37,
		GL_COPY_READ_BUFFER = 0x8F36,
		GL_COPY_WRITE_BUFFER = 0x8F37;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of IsEnabled, and by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_PRIMITIVE_RESTART = 0x8F9D;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_PRIMITIVE_RESTART_INDEX = 0x8F9E;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, MapBufferRange, BindTexture, UnmapBuffer,
	 *  GetBufferSubData, GetBufferParameteriv, GetBufferPointerv, and TexBuffer,
	 *  and the <pname> parameter of GetBooleanv, GetDoublev, GetFloatv, and
	 *  GetIntegerv:
	 */
	public static final int GL_TEXTURE_BUFFER = 0x8C2A;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetDoublev,
	 *  GetFloatv, and GetIntegerv:
	 */
	public static final int GL_MAX_TEXTURE_BUFFER_SIZE = 0x8C2B,
		GL_TEXTURE_BINDING_BUFFER = 0x8C2C,
		GL_TEXTURE_BUFFER_DATA_STORE_BINDING = 0x8C2D,
		GL_TEXTURE_BUFFER_FORMAT = 0x8C2E;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable and IsEnabled;
	 *  by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv
	 *  and GetDoublev; and by the &lt;target&gt; parameter of BindTexture,
	 *  GetTexParameterfv, GetTexParameteriv, TexParameterf, TexParameteri,
	 *  TexParameterfv and TexParameteriv:
	 *  Accepted by the &lt;target&gt; parameter of GetTexImage,
	 *  GetTexLevelParameteriv, GetTexLevelParameterfv, TexImage2D,
	 *  CopyTexImage2D, TexSubImage2D and CopySubTexImage2D:
	 */
	public static final int GL_TEXTURE_RECTANGLE = 0x84F5;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv and GetDoublev:
	 */
	public static final int GL_TEXTURE_BINDING_RECTANGLE = 0x84F6;

	/**
	 *  Accepted by the &lt;target&gt; parameter of GetTexLevelParameteriv,
	 *  GetTexLevelParameterfv, GetTexParameteriv and TexImage2D:
	 */
	public static final int GL_PROXY_TEXTURE_RECTANGLE = 0x84F7;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev,
	 *  GetIntegerv and GetFloatv:
	 */
	public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE = 0x84F8;

	/**
	 *  Returned by &lt;type&gt; parameter of GetActiveUniform when the location
	 *  &lt;index&gt; for program object &lt;program&gt; is of type sampler2DRect:
	 */
	public static final int GL_SAMPLER_2D_RECT = 0x8B63;

	/**
	 *  Returned by &lt;type&gt; parameter of GetActiveUniform when the location
	 *  &lt;index&gt; for program object &lt;program&gt; is of type sampler2DRectShadow:
	 */
	public static final int GL_SAMPLER_2D_RECT_SHADOW = 0x8B64;

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and
	 *  GetBufferPointerv:
	 */
	public static final int GL_UNIFORM_BUFFER = 0x8A11;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetIntegeri_v, GetBooleanv,
	 *  GetIntegerv, GetFloatv, and GetDoublev:
	 */
	public static final int GL_UNIFORM_BUFFER_BINDING = 0x8A28;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegeri_v: 
	 */
	public static final int GL_UNIFORM_BUFFER_START = 0x8A29,
		GL_UNIFORM_BUFFER_SIZE = 0x8A2A;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 0x8A2B,
		GL_MAX_GEOMETRY_UNIFORM_BLOCKS = 0x8A2C,
		GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 0x8A2D,
		GL_MAX_COMBINED_UNIFORM_BLOCKS = 0x8A2E,
		GL_MAX_UNIFORM_BUFFER_BINDINGS = 0x8A2F,
		GL_MAX_UNIFORM_BLOCK_SIZE = 0x8A30,
		GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 0x8A31,
		GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS = 0x8A32,
		GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 0x8A33,
		GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 0x8A34;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 0x8A35,
		GL_ACTIVE_UNIFORM_BLOCKS = 0x8A36;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformsiv: 
	 */
	public static final int GL_UNIFORM_TYPE = 0x8A37,
		GL_UNIFORM_SIZE = 0x8A38,
		GL_UNIFORM_NAME_LENGTH = 0x8A39,
		GL_UNIFORM_BLOCK_INDEX = 0x8A3A,
		GL_UNIFORM_OFFSET = 0x8A3B,
		GL_UNIFORM_ARRAY_STRIDE = 0x8A3C,
		GL_UNIFORM_MATRIX_STRIDE = 0x8A3D,
		GL_UNIFORM_IS_ROW_MAJOR = 0x8A3E;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformBlockiv: 
	 */
	public static final int GL_UNIFORM_BLOCK_BINDING = 0x8A3F,
		GL_UNIFORM_BLOCK_DATA_SIZE = 0x8A40,
		GL_UNIFORM_BLOCK_NAME_LENGTH = 0x8A41,
		GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 0x8A42,
		GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 0x8A43,
		GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 0x8A44,
		GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER = 0x8A45,
		GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 0x8A46;

	/**
	 * Returned by GetActiveUniformsiv and GetUniformBlockIndex 
	 */
	public static final int GL_INVALID_INDEX = 0xFFFFFFFF;

	private GL31() {}

	public static void glDrawArraysInstanced(int mode, int first, int count, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawArraysInstanced;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawArraysInstanced(mode, first, count, primcount, function_pointer);
	}
	static native void nglDrawArraysInstanced(int mode, int first, int count, int primcount, long function_pointer);

	public static void glDrawElementsInstanced(int mode, ByteBuffer indices, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstanced;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstanced(mode, indices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), primcount, function_pointer);
	}
	public static void glDrawElementsInstanced(int mode, IntBuffer indices, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstanced;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstanced(mode, indices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), primcount, function_pointer);
	}
	public static void glDrawElementsInstanced(int mode, ShortBuffer indices, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstanced;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstanced(mode, indices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), primcount, function_pointer);
	}
	static native void nglDrawElementsInstanced(int mode, int indices_count, int type, long indices, int primcount, long function_pointer);
	public static void glDrawElementsInstanced(int mode, int indices_count, int type, long indices_buffer_offset, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstanced;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawElementsInstancedBO(mode, indices_count, type, indices_buffer_offset, primcount, function_pointer);
	}
	static native void nglDrawElementsInstancedBO(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, long function_pointer);

	public static void glCopyBufferSubData(int readtarget, int writetarget, long readoffset, long writeoffset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glCopyBufferSubData;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglCopyBufferSubData(readtarget, writetarget, readoffset, writeoffset, size, function_pointer);
	}
	static native void nglCopyBufferSubData(int readtarget, int writetarget, long readoffset, long writeoffset, long size, long function_pointer);

	public static void glPrimitiveRestartIndex(int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPrimitiveRestartIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPrimitiveRestartIndex(index, function_pointer);
	}
	static native void nglPrimitiveRestartIndex(int index, long function_pointer);

	public static void glTexBuffer(int target, int internalformat, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexBuffer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexBuffer(target, internalformat, buffer, function_pointer);
	}
	static native void nglTexBuffer(int target, int internalformat, int buffer, long function_pointer);

	public static void glGetUniformIndices(int program, ByteBuffer uniformNames, IntBuffer uniformIndices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformIndices;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(uniformNames);
		BufferChecks.checkNullTerminated(uniformNames, uniformIndices.remaining());
		BufferChecks.checkDirect(uniformIndices);
		nglGetUniformIndices(program, uniformIndices.remaining(), MemoryUtil.getAddress(uniformNames), MemoryUtil.getAddress(uniformIndices), function_pointer);
	}
	static native void nglGetUniformIndices(int program, int uniformIndices_uniformCount, long uniformNames, long uniformIndices, long function_pointer);

	/** Overloads glGetUniformIndices. */
	public static void glGetUniformIndices(int program, CharSequence[] uniformNames, IntBuffer uniformIndices) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformIndices;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(uniformNames);
		BufferChecks.checkBuffer(uniformIndices, uniformNames.length);
		nglGetUniformIndices(program, uniformNames.length, APIUtil.getBufferNT(caps, uniformNames), MemoryUtil.getAddress(uniformIndices), function_pointer);
	}

	public static void glGetActiveUniforms(int program, IntBuffer uniformIndices, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniformsiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(uniformIndices);
		BufferChecks.checkBuffer(params, uniformIndices.remaining());
		nglGetActiveUniformsiv(program, uniformIndices.remaining(), MemoryUtil.getAddress(uniformIndices), pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetActiveUniformsiv(int program, int uniformIndices_uniformCount, long uniformIndices, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetActiveUniformsiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetActiveUniformsi} instead. 
	 */
	@Deprecated
	public static int glGetActiveUniforms(int program, int uniformIndex, int pname) {
		return GL31.glGetActiveUniformsi(program, uniformIndex, pname);
	}

	/** Overloads glGetActiveUniformsiv. */
	public static int glGetActiveUniformsi(int program, int uniformIndex, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniformsiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetActiveUniformsiv(program, 1, MemoryUtil.getAddress(params.put(1, uniformIndex), 1), pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetActiveUniformName(int program, int uniformIndex, IntBuffer length, ByteBuffer uniformName) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniformName;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(uniformName);
		nglGetActiveUniformName(program, uniformIndex, uniformName.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(uniformName), function_pointer);
	}
	static native void nglGetActiveUniformName(int program, int uniformIndex, int uniformName_bufSize, long length, long uniformName, long function_pointer);

	/** Overloads glGetActiveUniformName. */
	public static String glGetActiveUniformName(int program, int uniformIndex, int bufSize) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniformName;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer uniformName_length = APIUtil.getLengths(caps);
		ByteBuffer uniformName = APIUtil.getBufferByte(caps, bufSize);
		nglGetActiveUniformName(program, uniformIndex, bufSize, MemoryUtil.getAddress0(uniformName_length), MemoryUtil.getAddress(uniformName), function_pointer);
		uniformName.limit(uniformName_length.get(0));
		return APIUtil.getString(caps, uniformName);
	}

	public static int glGetUniformBlockIndex(int program, ByteBuffer uniformBlockName) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformBlockIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(uniformBlockName);
		BufferChecks.checkNullTerminated(uniformBlockName);
		int __result = nglGetUniformBlockIndex(program, MemoryUtil.getAddress(uniformBlockName), function_pointer);
		return __result;
	}
	static native int nglGetUniformBlockIndex(int program, long uniformBlockName, long function_pointer);

	/** Overloads glGetUniformBlockIndex. */
	public static int glGetUniformBlockIndex(int program, CharSequence uniformBlockName) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetUniformBlockIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetUniformBlockIndex(program, APIUtil.getBufferNT(caps, uniformBlockName), function_pointer);
		return __result;
	}

	public static void glGetActiveUniformBlock(int program, int uniformBlockIndex, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniformBlockiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 16);
		nglGetActiveUniformBlockiv(program, uniformBlockIndex, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetActiveUniformBlockiv(int program, int uniformBlockIndex, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetActiveUniformBlockiv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetActiveUniformBlocki} instead. 
	 */
	@Deprecated
	public static int glGetActiveUniformBlock(int program, int uniformBlockIndex, int pname) {
		return GL31.glGetActiveUniformBlocki(program, uniformBlockIndex, pname);
	}

	/** Overloads glGetActiveUniformBlockiv. */
	public static int glGetActiveUniformBlocki(int program, int uniformBlockIndex, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniformBlockiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetActiveUniformBlockiv(program, uniformBlockIndex, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetActiveUniformBlockName(int program, int uniformBlockIndex, IntBuffer length, ByteBuffer uniformBlockName) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniformBlockName;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(uniformBlockName);
		nglGetActiveUniformBlockName(program, uniformBlockIndex, uniformBlockName.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(uniformBlockName), function_pointer);
	}
	static native void nglGetActiveUniformBlockName(int program, int uniformBlockIndex, int uniformBlockName_bufSize, long length, long uniformBlockName, long function_pointer);

	/** Overloads glGetActiveUniformBlockName. */
	public static String glGetActiveUniformBlockName(int program, int uniformBlockIndex, int bufSize) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveUniformBlockName;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer uniformBlockName_length = APIUtil.getLengths(caps);
		ByteBuffer uniformBlockName = APIUtil.getBufferByte(caps, bufSize);
		nglGetActiveUniformBlockName(program, uniformBlockIndex, bufSize, MemoryUtil.getAddress0(uniformBlockName_length), MemoryUtil.getAddress(uniformBlockName), function_pointer);
		uniformBlockName.limit(uniformBlockName_length.get(0));
		return APIUtil.getString(caps, uniformBlockName);
	}

	public static void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glUniformBlockBinding;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniformBlockBinding(program, uniformBlockIndex, uniformBlockBinding, function_pointer);
	}
	static native void nglUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding, long function_pointer);
}
