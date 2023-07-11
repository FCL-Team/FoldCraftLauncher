/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVExplicitMultisample {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetMultisamplefvNV: 
	 */
	public static final int GL_SAMPLE_POSITION_NV = 0x8E50;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_SAMPLE_MASK_NV = 0x8E51;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanIndexedvEXT and
	 *  GetIntegerIndexedvEXT:
	 */
	public static final int GL_SAMPLE_MASK_VALUE_NV = 0x8E52;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_TEXTURE_BINDING_RENDERBUFFER_NV = 0x8E53,
		GL_TEXTURE_RENDERBUFFER_DATA_STORE_BINDING_NV = 0x8E54,
		GL_MAX_SAMPLE_MASK_WORDS_NV = 0x8E59;

	/**
	 * Accepted by the &lt;target&gt; parameter of BindTexture, and TexRenderbufferNV: 
	 */
	public static final int GL_TEXTURE_RENDERBUFFER_NV = 0x8E55;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_RENDERBUFFER_NV = 0x8E56,
		GL_INT_SAMPLER_RENDERBUFFER_NV = 0x8E57,
		GL_UNSIGNED_INT_SAMPLER_RENDERBUFFER_NV = 0x8E58;

	private NVExplicitMultisample() {}

	public static void glGetBooleanIndexedEXT(int pname, int index, ByteBuffer data) {
		EXTDrawBuffers2.glGetBooleanIndexedEXT(pname, index, data);
	}

	/** Overloads glGetBooleanIndexedvEXT. */
	public static boolean glGetBooleanIndexedEXT(int pname, int index) {
		return EXTDrawBuffers2.glGetBooleanIndexedEXT(pname, index);
	}

	public static void glGetIntegerIndexedEXT(int pname, int index, IntBuffer data) {
		EXTDrawBuffers2.glGetIntegerIndexedEXT(pname, index, data);
	}

	/** Overloads glGetIntegerIndexedvEXT. */
	public static int glGetIntegerIndexedEXT(int pname, int index) {
		return EXTDrawBuffers2.glGetIntegerIndexedEXT(pname, index);
	}

	public static void glGetMultisampleNV(int pname, int index, FloatBuffer val) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetMultisamplefvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(val, 2);
		nglGetMultisamplefvNV(pname, index, MemoryUtil.getAddress(val), function_pointer);
	}
	static native void nglGetMultisamplefvNV(int pname, int index, long val, long function_pointer);

	public static void glSampleMaskIndexedNV(int index, int mask) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glSampleMaskIndexedNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglSampleMaskIndexedNV(index, mask, function_pointer);
	}
	static native void nglSampleMaskIndexedNV(int index, int mask, long function_pointer);

	public static void glTexRenderbufferNV(int target, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glTexRenderbufferNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglTexRenderbufferNV(target, renderbuffer, function_pointer);
	}
	static native void nglTexRenderbufferNV(int target, int renderbuffer, long function_pointer);
}
