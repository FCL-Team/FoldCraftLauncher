/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class GL33 {

	/**
	 *  Accepted by the &lt;src&gt; and &lt;dst&gt; parameters of BlendFunc and
	 *  BlendFunci, and by the &lt;srcRGB&gt;, &lt;dstRGB&gt;, &lt;srcAlpha&gt; and &lt;dstAlpha&gt;
	 *  parameters of BlendFuncSeparate and BlendFuncSeparatei:
	 */
	public static final int GL_SRC1_COLOR = 0x88F9,
		GL_ONE_MINUS_SRC1_COLOR = 0x88FA,
		GL_ONE_MINUS_SRC1_ALPHA = 0x88FB;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv
	 *  and GetDoublev:
	 */
	public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 0x88FC;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery,
	 *  and GetQueryiv:
	 */
	public static final int GL_ANY_SAMPLES_PASSED = 0x8C2F;

	/**
	 *  Accepted by the &lt;value&gt; parameter of the GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv and GetDoublev functions:
	 */
	public static final int GL_SAMPLER_BINDING = 0x8919;

	/**
	 *  Accepted by the &lt;internalFormat&gt; parameter of TexImage1D, TexImage2D,
	 *  TexImage3D, CopyTexImage1D, CopyTexImage2D, RenderbufferStorage and
	 *  RenderbufferStorageMultisample:
	 */
	public static final int GL_RGB10_A2UI = 0x906F;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of TexParameteri,
	 *  TexParameterf, TexParameteriv, TexParameterfv,
	 *  GetTexParameterfv, and GetTexParameteriv:
	 */
	public static final int GL_TEXTURE_SWIZZLE_R = 0x8E42,
		GL_TEXTURE_SWIZZLE_G = 0x8E43,
		GL_TEXTURE_SWIZZLE_B = 0x8E44,
		GL_TEXTURE_SWIZZLE_A = 0x8E45;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of TexParameteriv,
	 *  TexParameterfv, GetTexParameterfv, and GetTexParameteriv:
	 */
	public static final int GL_TEXTURE_SWIZZLE_RGBA = 0x8E46;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 *  GetQueryiv:
	 */
	public static final int GL_TIME_ELAPSED = 0x88BF;

	/**
	 *  Accepted by the &lt;target&gt; parameter of GetQueryiv and QueryCounter.
	 *  Accepted by the &lt;value&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetInteger64v, GetFloatv, and GetDoublev:
	 */
	public static final int GL_TIMESTAMP = 0x8E28;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetVertexAttribdv,
	 *  GetVertexAttribfv, and GetVertexAttribiv:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 0x88FE;

	/**
	 *  Accepted by the &lt;type&gt; parameter of VertexAttribPointer, VertexPointer,
	 *  NormalPointer, ColorPointer, SecondaryColorPointer, TexCoordPointer,
	 *  VertexAttribP{1234}ui, VertexP*, TexCoordP*, MultiTexCoordP*, NormalP3ui,
	 *  ColorP*, SecondaryColorP* and VertexAttribP*
	 */
	public static final int GL_INT_2_10_10_10_REV = 0x8D9F;

	private GL33() {}

	public static void glBindFragDataLocationIndexed(int program, int colorNumber, int index, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFragDataLocationIndexed;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		nglBindFragDataLocationIndexed(program, colorNumber, index, MemoryUtil.getAddress(name), function_pointer);
	}
	static native void nglBindFragDataLocationIndexed(int program, int colorNumber, int index, long name, long function_pointer);

	/** Overloads glBindFragDataLocationIndexed. */
	public static void glBindFragDataLocationIndexed(int program, int colorNumber, int index, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindFragDataLocationIndexed;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindFragDataLocationIndexed(program, colorNumber, index, APIUtil.getBufferNT(caps, name), function_pointer);
	}

	public static int glGetFragDataIndex(int program, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFragDataIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetFragDataIndex(program, MemoryUtil.getAddress(name), function_pointer);
		return __result;
	}
	static native int nglGetFragDataIndex(int program, long name, long function_pointer);

	/** Overloads glGetFragDataIndex. */
	public static int glGetFragDataIndex(int program, CharSequence name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetFragDataIndex;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglGetFragDataIndex(program, APIUtil.getBufferNT(caps, name), function_pointer);
		return __result;
	}

	public static void glGenSamplers(IntBuffer samplers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenSamplers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(samplers);
		nglGenSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
	}
	static native void nglGenSamplers(int samplers_count, long samplers, long function_pointer);

	/** Overloads glGenSamplers. */
	public static int glGenSamplers() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGenSamplers;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer samplers = APIUtil.getBufferInt(caps);
		nglGenSamplers(1, MemoryUtil.getAddress(samplers), function_pointer);
		return samplers.get(0);
	}

	public static void glDeleteSamplers(IntBuffer samplers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteSamplers;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkDirect(samplers);
		nglDeleteSamplers(samplers.remaining(), MemoryUtil.getAddress(samplers), function_pointer);
	}
	static native void nglDeleteSamplers(int samplers_count, long samplers, long function_pointer);

	/** Overloads glDeleteSamplers. */
	public static void glDeleteSamplers(int sampler) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glDeleteSamplers;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDeleteSamplers(1, APIUtil.getInt(caps, sampler), function_pointer);
	}

	public static boolean glIsSampler(int sampler) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glIsSampler;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsSampler(sampler, function_pointer);
		return __result;
	}
	static native boolean nglIsSampler(int sampler, long function_pointer);

	public static void glBindSampler(int unit, int sampler) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindSampler;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindSampler(unit, sampler, function_pointer);
	}
	static native void nglBindSampler(int unit, int sampler, long function_pointer);

	public static void glSamplerParameteri(int sampler, int pname, int param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSamplerParameteri;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSamplerParameteri(sampler, pname, param, function_pointer);
	}
	static native void nglSamplerParameteri(int sampler, int pname, int param, long function_pointer);

	public static void glSamplerParameterf(int sampler, int pname, float param) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSamplerParameterf;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSamplerParameterf(sampler, pname, param, function_pointer);
	}
	static native void nglSamplerParameterf(int sampler, int pname, float param, long function_pointer);

	public static void glSamplerParameter(int sampler, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSamplerParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglSamplerParameteriv(int sampler, int pname, long params, long function_pointer);

	public static void glSamplerParameter(int sampler, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSamplerParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglSamplerParameterfv(int sampler, int pname, long params, long function_pointer);

	public static void glSamplerParameterI(int sampler, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSamplerParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglSamplerParameterIiv(int sampler, int pname, long params, long function_pointer);

	public static void glSamplerParameterIu(int sampler, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSamplerParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglSamplerParameterIuiv(int sampler, int pname, long params, long function_pointer);

	public static void glGetSamplerParameter(int sampler, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSamplerParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetSamplerParameteriv(int sampler, int pname, long params, long function_pointer);

	/** Overloads glGetSamplerParameteriv. */
	public static int glGetSamplerParameteri(int sampler, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSamplerParameteriv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetSamplerParameteriv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetSamplerParameter(int sampler, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSamplerParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetSamplerParameterfv(int sampler, int pname, long params, long function_pointer);

	/** Overloads glGetSamplerParameterfv. */
	public static float glGetSamplerParameterf(int sampler, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSamplerParameterfv;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetSamplerParameterfv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetSamplerParameterI(int sampler, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSamplerParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetSamplerParameterIiv(int sampler, int pname, long params, long function_pointer);

	/** Overloads glGetSamplerParameterIiv. */
	public static int glGetSamplerParameterIi(int sampler, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSamplerParameterIiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetSamplerParameterIiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetSamplerParameterIu(int sampler, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSamplerParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 4);
		nglGetSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetSamplerParameterIuiv(int sampler, int pname, long params, long function_pointer);

	/** Overloads glGetSamplerParameterIuiv. */
	public static int glGetSamplerParameterIui(int sampler, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetSamplerParameterIuiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetSamplerParameterIuiv(sampler, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glQueryCounter(int id, int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glQueryCounter;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglQueryCounter(id, target, function_pointer);
	}
	static native void nglQueryCounter(int id, int target, long function_pointer);

	public static void glGetQueryObject(int id, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjecti64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryObjecti64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryObjecti64v(int id, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetQueryObjecti64v.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetQueryObjecti64} instead. 
	 */
	@Deprecated
	public static long glGetQueryObject(int id, int pname) {
		return GL33.glGetQueryObjecti64(id, pname);
	}

	/** Overloads glGetQueryObjecti64v. */
	public static long glGetQueryObjecti64(int id, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjecti64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params = APIUtil.getBufferLong(caps);
		nglGetQueryObjecti64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetQueryObjectu(int id, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectui64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetQueryObjectui64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetQueryObjectui64v(int id, int pname, long params, long function_pointer);

	/**
	 * Overloads glGetQueryObjectui64v.
	 * <p>
	 * @deprecated Will be removed in 3.0. Use {@link #glGetQueryObjectui64} instead. 
	 */
	@Deprecated
	public static long glGetQueryObjectu(int id, int pname) {
		return GL33.glGetQueryObjectui64(id, pname);
	}

	/** Overloads glGetQueryObjectui64v. */
	public static long glGetQueryObjectui64(int id, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetQueryObjectui64v;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params = APIUtil.getBufferLong(caps);
		nglGetQueryObjectui64v(id, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glVertexAttribDivisor(int index, int divisor) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribDivisor;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribDivisor(index, divisor, function_pointer);
	}
	static native void nglVertexAttribDivisor(int index, int divisor, long function_pointer);

	public static void glVertexP2ui(int type, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexP2ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexP2ui(type, value, function_pointer);
	}
	static native void nglVertexP2ui(int type, int value, long function_pointer);

	public static void glVertexP3ui(int type, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexP3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexP3ui(type, value, function_pointer);
	}
	static native void nglVertexP3ui(int type, int value, long function_pointer);

	public static void glVertexP4ui(int type, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexP4ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexP4ui(type, value, function_pointer);
	}
	static native void nglVertexP4ui(int type, int value, long function_pointer);

	public static void glVertexP2u(int type, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexP2uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 2);
		nglVertexP2uiv(type, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglVertexP2uiv(int type, long value, long function_pointer);

	public static void glVertexP3u(int type, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexP3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 3);
		nglVertexP3uiv(type, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglVertexP3uiv(int type, long value, long function_pointer);

	public static void glVertexP4u(int type, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexP4uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglVertexP4uiv(type, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglVertexP4uiv(int type, long value, long function_pointer);

	public static void glTexCoordP1ui(int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordP1ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoordP1ui(type, coords, function_pointer);
	}
	static native void nglTexCoordP1ui(int type, int coords, long function_pointer);

	public static void glTexCoordP2ui(int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordP2ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoordP2ui(type, coords, function_pointer);
	}
	static native void nglTexCoordP2ui(int type, int coords, long function_pointer);

	public static void glTexCoordP3ui(int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordP3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoordP3ui(type, coords, function_pointer);
	}
	static native void nglTexCoordP3ui(int type, int coords, long function_pointer);

	public static void glTexCoordP4ui(int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordP4ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexCoordP4ui(type, coords, function_pointer);
	}
	static native void nglTexCoordP4ui(int type, int coords, long function_pointer);

	public static void glTexCoordP1u(int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordP1uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 1);
		nglTexCoordP1uiv(type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglTexCoordP1uiv(int type, long coords, long function_pointer);

	public static void glTexCoordP2u(int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordP2uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 2);
		nglTexCoordP2uiv(type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglTexCoordP2uiv(int type, long coords, long function_pointer);

	public static void glTexCoordP3u(int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordP3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 3);
		nglTexCoordP3uiv(type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglTexCoordP3uiv(int type, long coords, long function_pointer);

	public static void glTexCoordP4u(int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexCoordP4uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 4);
		nglTexCoordP4uiv(type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglTexCoordP4uiv(int type, long coords, long function_pointer);

	public static void glMultiTexCoordP1ui(int texture, int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoordP1ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoordP1ui(texture, type, coords, function_pointer);
	}
	static native void nglMultiTexCoordP1ui(int texture, int type, int coords, long function_pointer);

	public static void glMultiTexCoordP2ui(int texture, int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoordP2ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoordP2ui(texture, type, coords, function_pointer);
	}
	static native void nglMultiTexCoordP2ui(int texture, int type, int coords, long function_pointer);

	public static void glMultiTexCoordP3ui(int texture, int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoordP3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoordP3ui(texture, type, coords, function_pointer);
	}
	static native void nglMultiTexCoordP3ui(int texture, int type, int coords, long function_pointer);

	public static void glMultiTexCoordP4ui(int texture, int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoordP4ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglMultiTexCoordP4ui(texture, type, coords, function_pointer);
	}
	static native void nglMultiTexCoordP4ui(int texture, int type, int coords, long function_pointer);

	public static void glMultiTexCoordP1u(int texture, int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoordP1uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 1);
		nglMultiTexCoordP1uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglMultiTexCoordP1uiv(int texture, int type, long coords, long function_pointer);

	public static void glMultiTexCoordP2u(int texture, int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoordP2uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 2);
		nglMultiTexCoordP2uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglMultiTexCoordP2uiv(int texture, int type, long coords, long function_pointer);

	public static void glMultiTexCoordP3u(int texture, int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoordP3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 3);
		nglMultiTexCoordP3uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglMultiTexCoordP3uiv(int texture, int type, long coords, long function_pointer);

	public static void glMultiTexCoordP4u(int texture, int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glMultiTexCoordP4uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 4);
		nglMultiTexCoordP4uiv(texture, type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglMultiTexCoordP4uiv(int texture, int type, long coords, long function_pointer);

	public static void glNormalP3ui(int type, int coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalP3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglNormalP3ui(type, coords, function_pointer);
	}
	static native void nglNormalP3ui(int type, int coords, long function_pointer);

	public static void glNormalP3u(int type, IntBuffer coords) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glNormalP3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(coords, 3);
		nglNormalP3uiv(type, MemoryUtil.getAddress(coords), function_pointer);
	}
	static native void nglNormalP3uiv(int type, long coords, long function_pointer);

	public static void glColorP3ui(int type, int color) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorP3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorP3ui(type, color, function_pointer);
	}
	static native void nglColorP3ui(int type, int color, long function_pointer);

	public static void glColorP4ui(int type, int color) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorP4ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorP4ui(type, color, function_pointer);
	}
	static native void nglColorP4ui(int type, int color, long function_pointer);

	public static void glColorP3u(int type, IntBuffer color) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorP3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(color, 3);
		nglColorP3uiv(type, MemoryUtil.getAddress(color), function_pointer);
	}
	static native void nglColorP3uiv(int type, long color, long function_pointer);

	public static void glColorP4u(int type, IntBuffer color) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glColorP4uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(color, 4);
		nglColorP4uiv(type, MemoryUtil.getAddress(color), function_pointer);
	}
	static native void nglColorP4uiv(int type, long color, long function_pointer);

	public static void glSecondaryColorP3ui(int type, int color) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorP3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSecondaryColorP3ui(type, color, function_pointer);
	}
	static native void nglSecondaryColorP3ui(int type, int color, long function_pointer);

	public static void glSecondaryColorP3u(int type, IntBuffer color) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSecondaryColorP3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(color, 3);
		nglSecondaryColorP3uiv(type, MemoryUtil.getAddress(color), function_pointer);
	}
	static native void nglSecondaryColorP3uiv(int type, long color, long function_pointer);

	public static void glVertexAttribP1ui(int index, int type, boolean normalized, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribP1ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribP1ui(index, type, normalized, value, function_pointer);
	}
	static native void nglVertexAttribP1ui(int index, int type, boolean normalized, int value, long function_pointer);

	public static void glVertexAttribP2ui(int index, int type, boolean normalized, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribP2ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribP2ui(index, type, normalized, value, function_pointer);
	}
	static native void nglVertexAttribP2ui(int index, int type, boolean normalized, int value, long function_pointer);

	public static void glVertexAttribP3ui(int index, int type, boolean normalized, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribP3ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribP3ui(index, type, normalized, value, function_pointer);
	}
	static native void nglVertexAttribP3ui(int index, int type, boolean normalized, int value, long function_pointer);

	public static void glVertexAttribP4ui(int index, int type, boolean normalized, int value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribP4ui;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribP4ui(index, type, normalized, value, function_pointer);
	}
	static native void nglVertexAttribP4ui(int index, int type, boolean normalized, int value, long function_pointer);

	public static void glVertexAttribP1u(int index, int type, boolean normalized, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribP1uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 1);
		nglVertexAttribP1uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglVertexAttribP1uiv(int index, int type, boolean normalized, long value, long function_pointer);

	public static void glVertexAttribP2u(int index, int type, boolean normalized, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribP2uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 2);
		nglVertexAttribP2uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglVertexAttribP2uiv(int index, int type, boolean normalized, long value, long function_pointer);

	public static void glVertexAttribP3u(int index, int type, boolean normalized, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribP3uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 3);
		nglVertexAttribP3uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglVertexAttribP3uiv(int index, int type, boolean normalized, long value, long function_pointer);

	public static void glVertexAttribP4u(int index, int type, boolean normalized, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVertexAttribP4uiv;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(value, 4);
		nglVertexAttribP4uiv(index, type, normalized, MemoryUtil.getAddress(value), function_pointer);
	}
	static native void nglVertexAttribP4uiv(int index, int type, boolean normalized, long value, long function_pointer);
}
