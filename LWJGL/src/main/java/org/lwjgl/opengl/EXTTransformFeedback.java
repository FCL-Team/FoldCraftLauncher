/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTTransformFeedback {

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 *  BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 *  GetBufferPointerv, BindBufferRangeEXT, BindBufferOffsetEXT and
	 *  BindBufferBaseEXT:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_EXT = 0x8C8E;

	/**
	 *  Accepted by the &lt;param&gt; parameter of GetIntegerIndexedvEXT and
	 *  GetBooleanIndexedvEXT:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START_EXT = 0x8C84,
		GL_TRANSFORM_FEEDBACK_BUFFER_SIZE_EXT = 0x8C85;

	/**
	 *  Accepted by the &lt;param&gt; parameter of GetIntegerIndexedvEXT and
	 *  GetBooleanIndexedvEXT, and by the &lt;pname&gt; parameter of GetBooleanv,
	 *  GetDoublev, GetIntegerv, and GetFloatv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING_EXT = 0x8C8F;

	/**
	 * Accepted by the &lt;bufferMode&gt; parameter of TransformFeedbackVaryingsEXT: 
	 */
	public static final int GL_INTERLEAVED_ATTRIBS_EXT = 0x8C8C,
		GL_SEPARATE_ATTRIBS_EXT = 0x8C8D;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 *  GetQueryiv:
	 */
	public static final int GL_PRIMITIVES_GENERATED_EXT = 0x8C87,
		GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN_EXT = 0x8C88;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_RASTERIZER_DISCARD_EXT = 0x8C89;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS_EXT = 0x8C8A,
		GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS_EXT = 0x8C8B,
		GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS_EXT = 0x8C80;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_TRANSFORM_FEEDBACK_VARYINGS_EXT = 0x8C83,
		GL_TRANSFORM_FEEDBACK_BUFFER_MODE_EXT = 0x8C7F,
		GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH_EXT = 0x8C76;

	private EXTTransformFeedback() {}

	public static void glBindBufferRangeEXT(int target, int index, int buffer, long offset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBufferRangeEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferRangeEXT(target, index, buffer, offset, size, function_pointer);
	}
	static native void nglBindBufferRangeEXT(int target, int index, int buffer, long offset, long size, long function_pointer);

	public static void glBindBufferOffsetEXT(int target, int index, int buffer, long offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBufferOffsetEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferOffsetEXT(target, index, buffer, offset, function_pointer);
	}
	static native void nglBindBufferOffsetEXT(int target, int index, int buffer, long offset, long function_pointer);

	public static void glBindBufferBaseEXT(int target, int index, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindBufferBaseEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferBaseEXT(target, index, buffer, function_pointer);
	}
	static native void nglBindBufferBaseEXT(int target, int index, int buffer, long function_pointer);

	public static void glBeginTransformFeedbackEXT(int primitiveMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginTransformFeedbackEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginTransformFeedbackEXT(primitiveMode, function_pointer);
	}
	static native void nglBeginTransformFeedbackEXT(int primitiveMode, long function_pointer);

	public static void glEndTransformFeedbackEXT() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndTransformFeedbackEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndTransformFeedbackEXT(function_pointer);
	}
	static native void nglEndTransformFeedbackEXT(long function_pointer);

	public static void glTransformFeedbackVaryingsEXT(int program, int count, ByteBuffer varyings, int bufferMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTransformFeedbackVaryingsEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(varyings);
		BufferChecks.checkNullTerminated(varyings, count);
		nglTransformFeedbackVaryingsEXT(program, count, MemoryUtil.getAddress(varyings), bufferMode, function_pointer);
	}
	static native void nglTransformFeedbackVaryingsEXT(int program, int count, long varyings, int bufferMode, long function_pointer);

	/** Overloads glTransformFeedbackVaryingsEXT. */
	public static void glTransformFeedbackVaryingsEXT(int program, CharSequence[] varyings, int bufferMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTransformFeedbackVaryingsEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkArray(varyings);
		nglTransformFeedbackVaryingsEXT(program, varyings.length, APIUtil.getBufferNT(caps, varyings), bufferMode, function_pointer);
	}

	public static void glGetTransformFeedbackVaryingEXT(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbackVaryingEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		BufferChecks.checkDirect(name);
		nglGetTransformFeedbackVaryingEXT(program, index, name.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglGetTransformFeedbackVaryingEXT(int program, int index, int name_bufSize, long length, long size, long type, long name, long function_pointer);

	/** Overloads glGetTransformFeedbackVaryingEXT. */
	public static String glGetTransformFeedbackVaryingEXT(int program, int index, int bufSize, IntBuffer size, IntBuffer type) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetTransformFeedbackVaryingEXT;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(size, 1);
		BufferChecks.checkBuffer(type, 1);
		IntBuffer name_length = APIUtil.getLengths(caps);
		ByteBuffer name = APIUtil.getBufferByte(caps, bufSize);
		nglGetTransformFeedbackVaryingEXT(program, index, bufSize, MemoryUtil.getAddress0(name_length), MemoryUtil.getAddress(size), MemoryUtil.getAddress(type), MemoryUtil.getAddress(name), function_pointer);
		name.limit(name_length.get(0));
		return APIUtil.getString(caps, name);
	}
}
