/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL32 {

	public static final int GL_CONTEXT_PROFILE_MASK = 0x9126,
		GL_CONTEXT_CORE_PROFILE_BIT = 0x1,
		GL_CONTEXT_COMPATIBILITY_PROFILE_BIT = 0x2,
		GL_MAX_VERTEX_OUTPUT_COMPONENTS = 0x9122,
		GL_MAX_GEOMETRY_INPUT_COMPONENTS = 0x9123,
		GL_MAX_GEOMETRY_OUTPUT_COMPONENTS = 0x9124,
		GL_MAX_FRAGMENT_INPUT_COMPONENTS = 0x9125;

	/**
	 * Accepted by the &lt;mode&gt; parameter of ProvokingVertex: 
	 */
	public static final int GL_FIRST_VERTEX_CONVENTION = 0x8E4D,
		GL_LAST_VERTEX_CONVENTION = 0x8E4E;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_PROVOKING_VERTEX = 0x8E4F,
		GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 0x8E4C;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable and IsEnabled,
	 *  and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv
	 *  and GetDoublev:
	 */
	public static final int GL_TEXTURE_CUBE_MAP_SEAMLESS = 0x884F;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetMultisamplefv: 
	 */
	public static final int GL_SAMPLE_POSITION = 0x8E50;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_SAMPLE_MASK = 0x8E51;

	/**
	 *  Accepted by the &lt;target&gt; parameter of GetBooleani_v and
	 *  GetIntegeri_v:
	 */
	public static final int GL_SAMPLE_MASK_VALUE = 0x8E52;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindTexture and
	 *  TexImage2DMultisample:
	 */
	public static final int GL_TEXTURE_2D_MULTISAMPLE = 0x9100;

	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage2DMultisample: 
	 */
	public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 0x9101;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindTexture and
	 *  TexImage3DMultisample:
	 */
	public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9102;

	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage3DMultisample: 
	 */
	public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9103;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_MAX_SAMPLE_MASK_WORDS = 0x8E59,
		GL_MAX_COLOR_TEXTURE_SAMPLES = 0x910E,
		GL_MAX_DEPTH_TEXTURE_SAMPLES = 0x910F,
		GL_MAX_INTEGER_SAMPLES = 0x9110,
		GL_TEXTURE_BINDING_2D_MULTISAMPLE = 0x9104,
		GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 0x9105;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameter 
	 */
	public static final int GL_TEXTURE_SAMPLES = 0x9106,
		GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 0x9107;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_2D_MULTISAMPLE = 0x9108,
		GL_INT_SAMPLER_2D_MULTISAMPLE = 0x9109,
		GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 0x910A,
		GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910B,
		GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910C,
		GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910D;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 *  and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_DEPTH_CLAMP = 0x864F;

	/**
	 *  Accepted by the &lt;type&gt; parameter of CreateShader and returned by the
	 *  &lt;params&gt; parameter of GetShaderiv:
	 */
	public static final int GL_GEOMETRY_SHADER = 0x8DD9;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of ProgramParameteriEXT and
	 *  GetProgramiv:
	 */
	public static final int GL_GEOMETRY_VERTICES_OUT = 0x8DDA,
		GL_GEOMETRY_INPUT_TYPE = 0x8DDB,
		GL_GEOMETRY_OUTPUT_TYPE = 0x8DDC;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 0x8C29,
		GL_MAX_GEOMETRY_UNIFORM_COMPONENTS = 0x8DDF,
		GL_MAX_GEOMETRY_OUTPUT_VERTICES = 0x8DE0,
		GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 0x8DE1;

	/**
	 *  Accepted by the &lt;mode&gt; parameter of Begin, DrawArrays,
	 *  MultiDrawArrays, DrawElements, MultiDrawElements, and
	 *  DrawRangeElements:
	 */
	public static final int GL_LINES_ADJACENCY = 0xA,
		GL_LINE_STRIP_ADJACENCY = 0xB,
		GL_TRIANGLES_ADJACENCY = 0xC,
		GL_TRIANGLE_STRIP_ADJACENCY = 0xD;

	/**
	 * Returned by CheckFramebufferStatusEXT: 
	 */
	public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 0x8DA8;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetFramebufferAttachment-
	 *  ParameterivEXT:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED = 0x8DA7;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 *  and by the &lt;pname&gt; parameter of GetIntegerv, GetFloatv, GetDoublev,
	 *  and GetBooleanv:
	 */
	public static final int GL_PROGRAM_POINT_SIZE = 0x8642;

	/**
	 * Accepted as the &lt;pname&gt; parameter of GetInteger64v: 
	 */
	public static final int GL_MAX_SERVER_WAIT_TIMEOUT = 0x9111;

	/**
	 * Accepted as the &lt;pname&gt; parameter of GetSynciv: 
	 */
	public static final int GL_OBJECT_TYPE = 0x9112,
		GL_SYNC_CONDITION = 0x9113,
		GL_SYNC_STATUS = 0x9114,
		GL_SYNC_FLAGS = 0x9115;

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; OBJECT_TYPE: 
	 */
	public static final int GL_SYNC_FENCE = 0x9116;

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; SYNC_CONDITION: 
	 */
	public static final int GL_SYNC_GPU_COMMANDS_COMPLETE = 0x9117;

	/**
	 * Returned in &lt;values&gt; for GetSynciv &lt;pname&gt; SYNC_STATUS: 
	 */
	public static final int GL_UNSIGNALED = 0x9118,
		GL_SIGNALED = 0x9119;

	/**
	 * Accepted in the &lt;flags&gt; parameter of ClientWaitSync: 
	 */
	public static final int GL_SYNC_FLUSH_COMMANDS_BIT = 0x1;

	/**
	 * Accepted in the &lt;timeout&gt; parameter of WaitSync: 
	 */
	public static final long GL_TIMEOUT_IGNORED = 0xFFFFFFFFFFFFFFFFL;

	/**
	 * Returned by ClientWaitSync: 
	 */
	public static final int GL_ALREADY_SIGNALED = 0x911A,
		GL_TIMEOUT_EXPIRED = 0x911B,
		GL_CONDITION_SATISFIED = 0x911C,
		GL_WAIT_FAILED = 0x911D;

	private GL32() {}

	public static void glGetBufferParameter(int target, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferParameteri64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetBufferParameteri64v(target, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetBufferParameteri64v(int target, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetBufferParameteri64v.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetBufferParameteri64} instead. 
	 */
	@Deprecated
	public static long glGetBufferParameter(int target, int pname) {
		return GL32.glGetBufferParameteri64(target, pname);
	}

	/** Overloads glGetBufferParameteri64v. */
	public static long glGetBufferParameteri64(int target, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetBufferParameteri64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params = APIUtil.getBufferLong(caps);
		nglGetBufferParameteri64v(target, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glDrawElementsBaseVertex(int mode, ByteBuffer indices, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsBaseVertex(mode, indices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), basevertex, function_pointer);
	}
	public static void glDrawElementsBaseVertex(int mode, IntBuffer indices, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsBaseVertex(mode, indices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), basevertex, function_pointer);
	}
	public static void glDrawElementsBaseVertex(int mode, ShortBuffer indices, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsBaseVertex(mode, indices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), basevertex, function_pointer);
	}
	static native void nglDrawElementsBaseVertex(int mode, int indices_count, int type, long indices, int basevertex, long function_pointer);
	public static void glDrawElementsBaseVertex(int mode, int indices_count, int type, long indices_buffer_offset, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawElementsBaseVertexBO(mode, indices_count, type, indices_buffer_offset, basevertex, function_pointer);
	}
	static native void nglDrawElementsBaseVertexBO(int mode, int indices_count, int type, long indices_buffer_offset, int basevertex, long function_pointer);

	public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, ByteBuffer indices, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementsBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), basevertex, function_pointer);
	}
	public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, IntBuffer indices, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementsBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), basevertex, function_pointer);
	}
	public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, ShortBuffer indices, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementsBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawRangeElementsBaseVertex(mode, start, end, indices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), basevertex, function_pointer);
	}
	static native void nglDrawRangeElementsBaseVertex(int mode, int start, int end, int indices_count, int type, long indices, int basevertex, long function_pointer);
	public static void glDrawRangeElementsBaseVertex(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawRangeElementsBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawRangeElementsBaseVertexBO(mode, start, end, indices_count, type, indices_buffer_offset, basevertex, function_pointer);
	}
	static native void nglDrawRangeElementsBaseVertexBO(int mode, int start, int end, int indices_count, int type, long indices_buffer_offset, int basevertex, long function_pointer);

	public static void glDrawElementsInstancedBaseVertex(int mode, ByteBuffer indices, int primcount, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
	}
	public static void glDrawElementsInstancedBaseVertex(int mode, IntBuffer indices, int primcount, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), GL11.GL_UNSIGNED_INT, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
	}
	public static void glDrawElementsInstancedBaseVertex(int mode, ShortBuffer indices, int primcount, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOdisabled(caps);
		BufferChecks.checkDirect(indices);
		nglDrawElementsInstancedBaseVertex(mode, indices.remaining(), GL11.GL_UNSIGNED_SHORT, MemoryUtil.getAddress(indices), primcount, basevertex, function_pointer);
	}
	static native void nglDrawElementsInstancedBaseVertex(int mode, int indices_count, int type, long indices, int primcount, int basevertex, long function_pointer);
	public static void glDrawElementsInstancedBaseVertex(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int basevertex) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDrawElementsInstancedBaseVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureElementVBOenabled(caps);
		nglDrawElementsInstancedBaseVertexBO(mode, indices_count, type, indices_buffer_offset, primcount, basevertex, function_pointer);
	}
	static native void nglDrawElementsInstancedBaseVertexBO(int mode, int indices_count, int type, long indices_buffer_offset, int primcount, int basevertex, long function_pointer);

	public static void glProvokingVertex(int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glProvokingVertex;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglProvokingVertex(mode, function_pointer);
	}
	static native void nglProvokingVertex(int mode, long function_pointer);

	public static void glTexImage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage2DMultisample;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexImage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations, function_pointer);
	}
	static native void nglTexImage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations, long function_pointer);

	public static void glTexImage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexImage3DMultisample;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexImage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations, function_pointer);
	}
	static native void nglTexImage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations, long function_pointer);

	public static void glGetMultisample(int pname, int index, FloatBuffer val) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMultisamplefv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(val, 2);
		nglGetMultisamplefv(pname, index, MemoryUtil.getAddress(val), function_pointer);
	}
	static native void nglGetMultisamplefv(int pname, int index, long val, long function_pointer);

	public static void glSampleMaski(int index, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSampleMaski;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSampleMaski(index, mask, function_pointer);
	}
	static native void nglSampleMaski(int index, int mask, long function_pointer);

	public static void glFramebufferTexture(int target, int attachment, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFramebufferTexture;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture(target, attachment, texture, level, function_pointer);
	}
	static native void nglFramebufferTexture(int target, int attachment, int texture, int level, long function_pointer);

	public static GLSync glFenceSync(int condition, int flags) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glFenceSync;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLSync __result = new GLSync(nglFenceSync(condition, flags, function_pointer));
		return __result;
	}
	static native long nglFenceSync(int condition, int flags, long function_pointer);

	public static boolean glIsSync(GLSync sync) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsSync;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsSync(sync.getPointer(), function_pointer);
		return __result;
	}
	static native boolean nglIsSync(long sync, long function_pointer);

	public static void glDeleteSync(GLSync sync) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteSync;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteSync(sync.getPointer(), function_pointer);
	}
	static native void nglDeleteSync(long sync, long function_pointer);

	public static int glClientWaitSync(GLSync sync, int flags, long timeout) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glClientWaitSync;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglClientWaitSync(sync.getPointer(), flags, timeout, function_pointer);
		return __result;
	}
	static native int nglClientWaitSync(long sync, int flags, long timeout, long function_pointer);

	public static void glWaitSync(GLSync sync, int flags, long timeout) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glWaitSync;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglWaitSync(sync.getPointer(), flags, timeout, function_pointer);
	}
	static native void nglWaitSync(long sync, int flags, long timeout, long function_pointer);

	public static void glGetInteger64(int pname, LongBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInteger64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 1);
		nglGetInteger64v(pname, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetInteger64v(int pname, long data, long function_pointer);

	/** Overloads glGetInteger64v. */
	public static long glGetInteger64(int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInteger64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer data = APIUtil.getBufferLong(caps);
		nglGetInteger64v(pname, MemoryUtil.getAddress(data), function_pointer);
		return data.get(0);
	}

	public static void glGetInteger64(int value, int index, LongBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInteger64i_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(data, 4);
		nglGetInteger64i_v(value, index, MemoryUtil.getAddress(data), function_pointer);
	}
	static native void nglGetInteger64i_v(int value, int index, long data, long function_pointer);

	/** Overloads glGetInteger64i_v. */
	public static long glGetInteger64(int value, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetInteger64i_v;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer data = APIUtil.getBufferLong(caps);
		nglGetInteger64i_v(value, index, MemoryUtil.getAddress(data), function_pointer);
		return data.get(0);
	}

	public static void glGetSync(GLSync sync, int pname, IntBuffer length, IntBuffer values) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSynciv;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		BufferChecks.checkDirect(values);
		nglGetSynciv(sync.getPointer(), pname, values.remaining(), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddress(values), function_pointer);
	}
	static native void nglGetSynciv(long sync, int pname, int values_bufSize, long length, long values, long function_pointer);

	/**
	 * Overloads glGetSynciv.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetSynci} instead. 
	 */
	@Deprecated
	public static int glGetSync(GLSync sync, int pname) {
		return GL32.glGetSynci(sync, pname);
	}

	/** Overloads glGetSynciv. */
	public static int glGetSynci(GLSync sync, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSynciv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer values = APIUtil.getBufferInt(caps);
		nglGetSynciv(sync.getPointer(), pname, 1, 0L, MemoryUtil.getAddress(values), function_pointer);
		return values.get(0);
	}
}
