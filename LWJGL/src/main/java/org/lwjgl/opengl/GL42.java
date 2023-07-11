/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL42 {

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage2D, TexImage3D,
	 *  CopyTexImage2D, CopyTexImage3D, CompressedTexImage2DARB, and
	 *  CompressedTexImage3DARB and the &lt;format&gt; parameter of
	 *  CompressedTexSubImage2DARB and CompressedTexSubImage3DARB:
	 */
	public static final int GL_COMPRESSED_RGBA_BPTC_UNORM = 0x8E8C,
		GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM = 0x8E8D,
		GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT = 0x8E8E,
		GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT = 0x8E8F;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of PixelStore[fi], GetBooleanv,
	 *  GetIntegerv, GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_UNPACK_COMPRESSED_BLOCK_WIDTH = 0x9127,
		GL_UNPACK_COMPRESSED_BLOCK_HEIGHT = 0x9128,
		GL_UNPACK_COMPRESSED_BLOCK_DEPTH = 0x9129,
		GL_UNPACK_COMPRESSED_BLOCK_SIZE = 0x912A,
		GL_PACK_COMPRESSED_BLOCK_WIDTH = 0x912B,
		GL_PACK_COMPRESSED_BLOCK_HEIGHT = 0x912C,
		GL_PACK_COMPRESSED_BLOCK_DEPTH = 0x912D,
		GL_PACK_COMPRESSED_BLOCK_SIZE = 0x912E;

	/**
	 * Accepted by the &lt;target&gt; parameter of BindBufferBase and BindBufferRange: 
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER = 0x92C0;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleani_v, GetIntegeri_v,
	 *  GetFloati_v, GetDoublei_v, GetInteger64i_v, GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, GetDoublev, and GetActiveAtomicCounterBufferiv:
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER_BINDING = 0x92C1;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegeri_64v: 
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER_START = 0x92C2,
		GL_ATOMIC_COUNTER_BUFFER_SIZE = 0x92C3;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveAtomicCounterBufferiv: 
	 */
	public static final int GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE = 0x92C4,
		GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS = 0x92C5,
		GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES = 0x92C6,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER = 0x92C7,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER = 0x92C8,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x92C9,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER = 0x92CA,
		GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER = 0x92CB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS = 0x92CC,
		GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS = 0x92CD,
		GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 0x92CE,
		GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS = 0x92CF,
		GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS = 0x92D0,
		GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS = 0x92D1,
		GL_MAX_VERTEX_ATOMIC_COUNTERS = 0x92D2,
		GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS = 0x92D3,
		GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS = 0x92D4,
		GL_MAX_GEOMETRY_ATOMIC_COUNTERS = 0x92D5,
		GL_MAX_FRAGMENT_ATOMIC_COUNTERS = 0x92D6,
		GL_MAX_COMBINED_ATOMIC_COUNTERS = 0x92D7,
		GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE = 0x92D8,
		GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS = 0x92DC;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_ACTIVE_ATOMIC_COUNTER_BUFFERS = 0x92D9;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetActiveUniformsiv: 
	 */
	public static final int GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX = 0x92DA;

	/**
	 * Returned in &lt;params&gt; by GetActiveUniform and GetActiveUniformsiv: 
	 */
	public static final int GL_UNSIGNED_INT_ATOMIC_COUNTER = 0x92DB;

	/**
	 * Accepted by the &lt;value&gt; parameter of GetTexParameter{if}v: 
	 */
	public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 0x912F;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, GetDoublev, and GetInteger64v:
	 */
	public static final int GL_MAX_IMAGE_UNITS = 0x8F38,
		GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS = 0x8F39,
		GL_MAX_IMAGE_SAMPLES = 0x906D,
		GL_MAX_VERTEX_IMAGE_UNIFORMS = 0x90CA,
		GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS = 0x90CB,
		GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS = 0x90CC,
		GL_MAX_GEOMETRY_IMAGE_UNIFORMS = 0x90CD,
		GL_MAX_FRAGMENT_IMAGE_UNIFORMS = 0x90CE,
		GL_MAX_COMBINED_IMAGE_UNIFORMS = 0x90CF;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetIntegeri_v and GetBooleani_v: 
	 */
	public static final int GL_IMAGE_BINDING_NAME = 0x8F3A,
		GL_IMAGE_BINDING_LEVEL = 0x8F3B,
		GL_IMAGE_BINDING_LAYERED = 0x8F3C,
		GL_IMAGE_BINDING_LAYER = 0x8F3D,
		GL_IMAGE_BINDING_ACCESS = 0x8F3E,
		GL_IMAGE_BINDING_FORMAT = 0x906E;

	/**
	 * Accepted by the &lt;barriers&gt; parameter of MemoryBarrier: 
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = 0x1,
		GL_ELEMENT_ARRAY_BARRIER_BIT = 0x2,
		GL_UNIFORM_BARRIER_BIT = 0x4,
		GL_TEXTURE_FETCH_BARRIER_BIT = 0x8,
		GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = 0x20,
		GL_COMMAND_BARRIER_BIT = 0x40,
		GL_PIXEL_BUFFER_BARRIER_BIT = 0x80,
		GL_TEXTURE_UPDATE_BARRIER_BIT = 0x100,
		GL_BUFFER_UPDATE_BARRIER_BIT = 0x200,
		GL_FRAMEBUFFER_BARRIER_BIT = 0x400,
		GL_TRANSFORM_FEEDBACK_BARRIER_BIT = 0x800,
		GL_ATOMIC_COUNTER_BARRIER_BIT = 0x1000,
		GL_ALL_BARRIER_BITS = 0xFFFFFFFF;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_IMAGE_1D = 0x904C,
		GL_IMAGE_2D = 0x904D,
		GL_IMAGE_3D = 0x904E,
		GL_IMAGE_2D_RECT = 0x904F,
		GL_IMAGE_CUBE = 0x9050,
		GL_IMAGE_BUFFER = 0x9051,
		GL_IMAGE_1D_ARRAY = 0x9052,
		GL_IMAGE_2D_ARRAY = 0x9053,
		GL_IMAGE_CUBE_MAP_ARRAY = 0x9054,
		GL_IMAGE_2D_MULTISAMPLE = 0x9055,
		GL_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9056,
		GL_INT_IMAGE_1D = 0x9057,
		GL_INT_IMAGE_2D = 0x9058,
		GL_INT_IMAGE_3D = 0x9059,
		GL_INT_IMAGE_2D_RECT = 0x905A,
		GL_INT_IMAGE_CUBE = 0x905B,
		GL_INT_IMAGE_BUFFER = 0x905C,
		GL_INT_IMAGE_1D_ARRAY = 0x905D,
		GL_INT_IMAGE_2D_ARRAY = 0x905E,
		GL_INT_IMAGE_CUBE_MAP_ARRAY = 0x905F,
		GL_INT_IMAGE_2D_MULTISAMPLE = 0x9060,
		GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9061,
		GL_UNSIGNED_INT_IMAGE_1D = 0x9062,
		GL_UNSIGNED_INT_IMAGE_2D = 0x9063,
		GL_UNSIGNED_INT_IMAGE_3D = 0x9064,
		GL_UNSIGNED_INT_IMAGE_2D_RECT = 0x9065,
		GL_UNSIGNED_INT_IMAGE_CUBE = 0x9066,
		GL_UNSIGNED_INT_IMAGE_BUFFER = 0x9067,
		GL_UNSIGNED_INT_IMAGE_1D_ARRAY = 0x9068,
		GL_UNSIGNED_INT_IMAGE_2D_ARRAY = 0x9069,
		GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY = 0x906A,
		GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE = 0x906B,
		GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x906C;

	/**
	 *  Accepted by the &lt;value&gt; parameter of GetTexParameteriv, GetTexParameterfv,
	 *  GetTexParameterIiv, and GetTexParameterIuiv:
	 */
	public static final int GL_IMAGE_FORMAT_COMPATIBILITY_TYPE = 0x90C7;

	/**
	 *  Returned in the &lt;data&gt; parameter of GetTexParameteriv, GetTexParameterfv,
	 *  GetTexParameterIiv, and GetTexParameterIuiv when &lt;value&gt; is
	 *  IMAGE_FORMAT_COMPATIBILITY_TYPE:
	 */
	public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE = 0x90C8,
		IMAGE_FORMAT_COMPATIBILITY_BY_CLASS = 0x90C9;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetInternalformativ: 
	 */
	public static final int GL_NUM_SAMPLE_COUNTS = 0x9380;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_MIN_MAP_BUFFER_ALIGNMENT = 0x90BC;

	private GL42() {}

	public static void glGetActiveAtomicCounterBuffer(int program, int bufferIndex, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAtomicCounterBufferiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetActiveAtomicCounterBufferiv(program, bufferIndex, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetActiveAtomicCounterBufferiv(int program, int bufferIndex, int pname, long params, long function_pointer);

	/** Overloads glGetActiveAtomicCounterBufferiv. */
	public static int glGetActiveAtomicCounterBuffer(int program, int bufferIndex, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetActiveAtomicCounterBufferiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetActiveAtomicCounterBufferiv(program, bufferIndex, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glTexStorage1D(int target, int levels, int internalformat, int width) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexStorage1D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexStorage1D(target, levels, internalformat, width, function_pointer);
	}
	static native void nglTexStorage1D(int target, int levels, int internalformat, int width, long function_pointer);

	public static void glTexStorage2D(int target, int levels, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexStorage2D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexStorage2D(target, levels, internalformat, width, height, function_pointer);
	}
	static native void nglTexStorage2D(int target, int levels, int internalformat, int width, int height, long function_pointer);

	public static void glTexStorage3D(int target, int levels, int internalformat, int width, int height, int depth) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexStorage3D;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexStorage3D(target, levels, internalformat, width, height, depth, function_pointer);
	}
	static native void nglTexStorage3D(int target, int levels, int internalformat, int width, int height, int depth, long function_pointer);

	public static void glDrawTransformFeedbackInstanced(int mode, int id, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawTransformFeedbackInstanced;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawTransformFeedbackInstanced(mode, id, primcount, function_pointer);
	}
	static native void nglDrawTransformFeedbackInstanced(int mode, int id, int primcount, long function_pointer);

	public static void glDrawTransformFeedbackStreamInstanced(int mode, int id, int stream, int primcount) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawTransformFeedbackStreamInstanced;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawTransformFeedbackStreamInstanced(mode, id, stream, primcount, function_pointer);
	}
	static native void nglDrawTransformFeedbackStreamInstanced(int mode, int id, int stream, int primcount, long function_pointer);

	public static void glDrawArraysInstancedBaseInstance(int mode, int first, int count, int primcount, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawArraysInstancedBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDrawArraysInstancedBaseInstance(mode, first, count, primcount, baseinstance, function_pointer);
	}
	static native void nglDrawArraysInstancedBaseInstance(int mode, int first, int count, int primcount, int baseinstance, long function_pointer);

	public static void glDrawElementsInstancedBaseInstance(int mode, ByteBuffer indices, int primcount, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseInstance(mode, indices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), primcount, baseinstance, function_pointer);
	}
	public static void glDrawElementsInstancedBaseInstance(int mode, IntBuffer indices, int primcount, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseInstance(mode, indices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), primcount, baseinstance, function_pointer);
	}
	public static void glDrawElementsInstancedBaseInstance(int mode, ShortBuffer indices, int primcount, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseInstance(mode, indices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), primcount, baseinstance, function_pointer);
	}
	static native void nglDrawElementsInstancedBaseInstance(int mode, int indices_count, int type, long indices, int primcount, int baseinstance, long function_pointer);
	public static void glDrawElementsInstancedBaseInstance(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawElementsInstancedBaseInstanceBO(mode, indices_count, type, indices_buffer_offset, primcount, baseinstance, function_pointer);
	}
	static native void nglDrawElementsInstancedBaseInstanceBO(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int baseinstance, long function_pointer);

	public static void glDrawElementsInstancedBaseVertexBaseInstance(int mode, ByteBuffer indices, int primcount, int basevertex, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseVertexBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseVertexBaseInstance(mode, indices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), primcount, basevertex, baseinstance, function_pointer);
	}
	public static void glDrawElementsInstancedBaseVertexBaseInstance(int mode, IntBuffer indices, int primcount, int basevertex, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseVertexBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseVertexBaseInstance(mode, indices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), primcount, basevertex, baseinstance, function_pointer);
	}
	public static void glDrawElementsInstancedBaseVertexBaseInstance(int mode, ShortBuffer indices, int primcount, int basevertex, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseVertexBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseVertexBaseInstance(mode, indices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), primcount, basevertex, baseinstance, function_pointer);
	}
	static native void nglDrawElementsInstancedBaseVertexBaseInstance(int mode, int indices_count, int type, long indices, int primcount, int basevertex, int baseinstance, long function_pointer);
	public static void glDrawElementsInstancedBaseVertexBaseInstance(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int basevertex, int baseinstance) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseVertexBaseInstance;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawElementsInstancedBaseVertexBaseInstanceBO(mode, indices_count, type, indices_buffer_offset, primcount, basevertex, baseinstance, function_pointer);
	}
	static native void nglDrawElementsInstancedBaseVertexBaseInstanceBO(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int basevertex, int baseinstance, long function_pointer);

	public static void glBindImageTexture(int unit, int texture, int level, boolean layered, int layer, int access, int format) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindImageTexture;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindImageTexture(unit, texture, level, layered, layer, access, format, function_pointer);
	}
	static native void nglBindImageTexture(int unit, int texture, int level, boolean layered, int layer, int access, int format, long function_pointer);

	public static void glMemoryBarrier(int barriers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMemoryBarrier;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMemoryBarrier(barriers, function_pointer);
	}
	static native void nglMemoryBarrier(int barriers, long function_pointer);

	public static void glGetInternalformat(int target, int internalformat, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInternalformativ;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(params);
		nglGetInternalformativ(target, internalformat, pname, params.remaining(), MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetInternalformativ(int target, int internalformat, int pname, int params_bufSize, long params, long function_pointer);

	/** Overloads glGetInternalformativ. */
	public static int glGetInternalformat(int target, int internalformat, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInternalformativ;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetInternalformativ(target, internalformat, pname, 1, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
