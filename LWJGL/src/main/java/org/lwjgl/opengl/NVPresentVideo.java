/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVPresentVideo {

	/**
	 *  Accepted by the &lt;type&gt; parameter of PresentFrameKeyedNV and
	 *  PresentFrameDualFillNV:
	 */
	public static final int GL_FRAME_NV = 0x8E26,
		FIELDS_NV = 0x8E27;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetVideoivNV, GetVideouivNV,
	 *  GetVideoi64vNV, GetVideoui64vNV:
	 */
	public static final int GL_CURRENT_TIME_NV = 0x8E28,
		GL_NUM_FILL_STREAMS_NV = 0x8E29;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetQueryiv: 
	 */
	public static final int GL_PRESENT_TIME_NV = 0x8E2A,
		GL_PRESENT_DURATION_NV = 0x8E2B;

	/**
	 * Accepted by the &lt;attribute&gt; parameter of NVPresentVideoUtil.glQueryContextNV: 
	 */
	public static final int GL_NUM_VIDEO_SLOTS_NV = 0x20F0;

	private NVPresentVideo() {}

	public static void glPresentFrameKeyedNV(int video_slot, long minPresentTime, int beginPresentTimeId, int presentDurationId, int type, int target0, int fill0, int key0, int target1, int fill1, int key1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPresentFrameKeyedNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPresentFrameKeyedNV(video_slot, minPresentTime, beginPresentTimeId, presentDurationId, type, target0, fill0, key0, target1, fill1, key1, function_pointer);
	}
	static native void nglPresentFrameKeyedNV(int video_slot, long minPresentTime, int beginPresentTimeId, int presentDurationId, int type, int target0, int fill0, int key0, int target1, int fill1, int key1, long function_pointer);

	public static void glPresentFrameDualFillNV(int video_slot, long minPresentTime, int beginPresentTimeId, int presentDurationId, int type, int target0, int fill0, int target1, int fill1, int target2, int fill2, int target3, int fill3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glPresentFrameDualFillNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglPresentFrameDualFillNV(video_slot, minPresentTime, beginPresentTimeId, presentDurationId, type, target0, fill0, target1, fill1, target2, fill2, target3, fill3, function_pointer);
	}
	static native void nglPresentFrameDualFillNV(int video_slot, long minPresentTime, int beginPresentTimeId, int presentDurationId, int type, int target0, int fill0, int target1, int fill1, int target2, int fill2, int target3, int fill3, long function_pointer);

	public static void glGetVideoNV(int video_slot, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetVideoivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVideoivNV(int video_slot, int pname, long params, long function_pointer);

	/** Overloads glGetVideoivNV. */
	public static int glGetVideoiNV(int video_slot, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetVideoivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetVideouNV(int video_slot, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideouivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetVideouivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVideouivNV(int video_slot, int pname, long params, long function_pointer);

	/** Overloads glGetVideouivNV. */
	public static int glGetVideouiNV(int video_slot, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideouivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetVideouivNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetVideoNV(int video_slot, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoi64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetVideoi64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVideoi64vNV(int video_slot, int pname, long params, long function_pointer);

	/** Overloads glGetVideoi64vNV. */
	public static long glGetVideoi64NV(int video_slot, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoi64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params = APIUtil.getBufferLong(caps);
		nglGetVideoi64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetVideouNV(int video_slot, int pname, LongBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetVideoui64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVideoui64vNV(int video_slot, int pname, long params, long function_pointer);

	/** Overloads glGetVideoui64vNV. */
	public static long glGetVideoui64NV(int video_slot, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoui64vNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		LongBuffer params = APIUtil.getBufferLong(caps);
		nglGetVideoui64vNV(video_slot, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}
}
