/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBUniformBufferObject {

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
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformsivARB: 
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
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformBlockivARB: 
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
	 * Returned by GetActiveUniformsivARB and GetUniformBlockIndexARB 
	 */
	public static final int GL_INVALID_INDEX = 0xFFFFFFFF;

	private ARBUniformBufferObject() {}

	public static void glGetUniformIndices(int program, ByteBuffer uniformNames, IntBuffer uniformIndices) {
		GL31.glGetUniformIndices(program, uniformNames, uniformIndices);
	}

	/** Overloads glGetUniformIndices. */
	public static void glGetUniformIndices(int program, CharSequence[] uniformNames, IntBuffer uniformIndices) {
		GL31.glGetUniformIndices(program, uniformNames, uniformIndices);
	}

	public static void glGetActiveUniforms(int program, IntBuffer uniformIndices, int pname, IntBuffer params) {
		GL31.glGetActiveUniforms(program, uniformIndices, pname, params);
	}

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
		return GL31.glGetActiveUniformsi(program, uniformIndex, pname);
	}

	public static void glGetActiveUniformName(int program, int uniformIndex, IntBuffer length, ByteBuffer uniformName) {
		GL31.glGetActiveUniformName(program, uniformIndex, length, uniformName);
	}

	/** Overloads glGetActiveUniformName. */
	public static String glGetActiveUniformName(int program, int uniformIndex, int bufSize) {
		return GL31.glGetActiveUniformName(program, uniformIndex, bufSize);
	}

	public static int glGetUniformBlockIndex(int program, ByteBuffer uniformBlockName) {
		return GL31.glGetUniformBlockIndex(program, uniformBlockName);
	}

	/** Overloads glGetUniformBlockIndex. */
	public static int glGetUniformBlockIndex(int program, CharSequence uniformBlockName) {
		return GL31.glGetUniformBlockIndex(program, uniformBlockName);
	}

	public static void glGetActiveUniformBlock(int program, int uniformBlockIndex, int pname, IntBuffer params) {
		GL31.glGetActiveUniformBlock(program, uniformBlockIndex, pname, params);
	}

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
		return GL31.glGetActiveUniformBlocki(program, uniformBlockIndex, pname);
	}

	public static void glGetActiveUniformBlockName(int program, int uniformBlockIndex, IntBuffer length, ByteBuffer uniformBlockName) {
		GL31.glGetActiveUniformBlockName(program, uniformBlockIndex, length, uniformBlockName);
	}

	/** Overloads glGetActiveUniformBlockName. */
	public static String glGetActiveUniformBlockName(int program, int uniformBlockIndex, int bufSize) {
		return GL31.glGetActiveUniformBlockName(program, uniformBlockIndex, bufSize);
	}

	public static void glBindBufferRange(int target, int index, int buffer, long offset, long size) {
		GL30.glBindBufferRange(target, index, buffer, offset, size);
	}

	public static void glBindBufferBase(int target, int index, int buffer) {
		GL30.glBindBufferBase(target, index, buffer);
	}

	public static void glGetInteger(int value, int index, IntBuffer data) {
		GL30.glGetInteger(value, index, data);
	}

	/** Overloads glGetIntegeri_v. */
	public static int glGetInteger(int value, int index) {
		return GL30.glGetInteger(value, index);
	}

	public static void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding) {
		GL31.glUniformBlockBinding(program, uniformBlockIndex, uniformBlockBinding);
	}
}
