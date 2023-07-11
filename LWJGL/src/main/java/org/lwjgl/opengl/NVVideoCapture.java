/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class NVVideoCapture {

	/**
	 *  Accepted by the &lt;target&gt; parameters of BindBufferARB, BufferDataARB,
	 *  BufferSubDataARB, MapBufferARB, UnmapBufferARB, GetBufferSubDataARB,
	 *  GetBufferParameterivARB, and GetBufferPointervARB:
	 */
	public static final int GL_VIDEO_BUFFER_NV = 0x9020;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 *  GetFloatv, and GetDoublev:
	 */
	public static final int GL_VIDEO_BUFFER_BINDING_NV = 0x9021;

	/**
	 *  Accepted by the &lt;frame_region&gt; parameter of
	 *  BindVideoCaptureStreamBufferNV, and BindVideoCaptureStreamTextureNV:
	 */
	public static final int GL_FIELD_UPPER_NV = 0x9022,
		GL_FIELD_LOWER_NV = 0x9023;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetVideoCaptureivNV: 
	 */
	public static final int GL_NUM_VIDEO_CAPTURE_STREAMS_NV = 0x9024,
		GL_NEXT_VIDEO_CAPTURE_BUFFER_STATUS_NV = 0x9025;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetVideoCaptureStream{i,f,d}vNV:
	 */
	public static final int GL_LAST_VIDEO_CAPTURE_STATUS_NV = 0x9027,
		GL_VIDEO_BUFFER_PITCH_NV = 0x9028,
		GL_VIDEO_CAPTURE_FRAME_WIDTH_NV = 0x9038,
		GL_VIDEO_CAPTURE_FRAME_HEIGHT_NV = 0x9039,
		GL_VIDEO_CAPTURE_FIELD_UPPER_HEIGHT_NV = 0x903A,
		GL_VIDEO_CAPTURE_FIELD_LOWER_HEIGHT_NV = 0x903B,
		GL_VIDEO_CAPTURE_TO_422_SUPPORTED_NV = 0x9026;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of
	 *  GetVideoCaptureStream{i,f,d}vNV and as the &lt;pname&gt; parameter of
	 *  VideoCaptureStreamParameter{i,f,d}vNV:
	 */
	public static final int GL_VIDEO_COLOR_CONVERSION_MATRIX_NV = 0x9029,
		GL_VIDEO_COLOR_CONVERSION_MAX_NV = 0x902A,
		GL_VIDEO_COLOR_CONVERSION_MIN_NV = 0x902B,
		GL_VIDEO_COLOR_CONVERSION_OFFSET_NV = 0x902C,
		GL_VIDEO_BUFFER_INTERNAL_FORMAT_NV = 0x902D,
		GL_VIDEO_CAPTURE_SURFACE_ORIGIN_NV = 0x903C;

	/**
	 * Returned by VideoCaptureNV: 
	 */
	public static final int GL_PARTIAL_SUCCESS_NV = 0x902E;

	/**
	 *  Returned by VideoCaptureNV and GetVideoCaptureStream{i,f,d}vNV
	 *  when &lt;pname&gt; is LAST_VIDEO_CAPTURE_STATUS_NV:
	 */
	public static final int GL_SUCCESS_NV = 0x902F,
		GL_FAILURE_NV = 0x9030;

	/**
	 *  Accepted in the &lt;params&gt; parameter of
	 *  VideoCaptureStreamParameter{i,f,d}vNV when &lt;pname&gt; is
	 *  VIDEO_BUFFER_INTERNAL_FORMAT_NV and returned by
	 *  GetVideoCaptureStream{i,f,d}vNV when &lt;pname&gt; is
	 *  VIDEO_BUFFER_INTERNAL_FORMAT_NV:
	 */
	public static final int GL_YCBYCR8_422_NV = 0x9031,
		GL_YCBAYCR8A_4224_NV = 0x9032,
		GL_Z6Y10Z6CB10Z6Y10Z6CR10_422_NV = 0x9033,
		GL_Z6Y10Z6CB10Z6A10Z6Y10Z6CR10Z6A10_4224_NV = 0x9034,
		GL_Z4Y12Z4CB12Z4Y12Z4CR12_422_NV = 0x9035,
		GL_Z4Y12Z4CB12Z4A12Z4Y12Z4CR12Z4A12_4224_NV = 0x9036,
		GL_Z4Y12Z4CB12Z4CR12_444_NV = 0x9037;

	/**
	 * Accepted by the &lt;attribute&gt; parameter of NVPresentVideoUtil.glQueryContextNV: 
	 */
	public static final int GL_NUM_VIDEO_CAPTURE_SLOTS_NV = 0x20CF;

	/**
	 *  Accepted by the &lt;attribute&gt; parameter of
	 *  glQueryVideoCaptureDeviceNV:
	 */
	public static final int GL_UNIQUE_ID_NV = 0x20CE;

	private NVVideoCapture() {}

	public static void glBeginVideoCaptureNV(int video_capture_slot) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBeginVideoCaptureNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginVideoCaptureNV(video_capture_slot, function_pointer);
	}
	static native void nglBeginVideoCaptureNV(int video_capture_slot, long function_pointer);

	public static void glBindVideoCaptureStreamBufferNV(int video_capture_slot, int stream, int frame_region, long offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindVideoCaptureStreamBufferNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindVideoCaptureStreamBufferNV(video_capture_slot, stream, frame_region, offset, function_pointer);
	}
	static native void nglBindVideoCaptureStreamBufferNV(int video_capture_slot, int stream, int frame_region, long offset, long function_pointer);

	public static void glBindVideoCaptureStreamTextureNV(int video_capture_slot, int stream, int frame_region, int target, int texture) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glBindVideoCaptureStreamTextureNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindVideoCaptureStreamTextureNV(video_capture_slot, stream, frame_region, target, texture, function_pointer);
	}
	static native void nglBindVideoCaptureStreamTextureNV(int video_capture_slot, int stream, int frame_region, int target, int texture, long function_pointer);

	public static void glEndVideoCaptureNV(int video_capture_slot) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glEndVideoCaptureNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndVideoCaptureNV(video_capture_slot, function_pointer);
	}
	static native void nglEndVideoCaptureNV(int video_capture_slot, long function_pointer);

	public static void glGetVideoCaptureNV(int video_capture_slot, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoCaptureivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetVideoCaptureivNV(video_capture_slot, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVideoCaptureivNV(int video_capture_slot, int pname, long params, long function_pointer);

	/** Overloads glGetVideoCaptureivNV. */
	public static int glGetVideoCaptureiNV(int video_capture_slot, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoCaptureivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetVideoCaptureivNV(video_capture_slot, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetVideoCaptureStreamNV(int video_capture_slot, int stream, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoCaptureStreamivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetVideoCaptureStreamivNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVideoCaptureStreamivNV(int video_capture_slot, int stream, int pname, long params, long function_pointer);

	/** Overloads glGetVideoCaptureStreamivNV. */
	public static int glGetVideoCaptureStreamiNV(int video_capture_slot, int stream, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoCaptureStreamivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params = APIUtil.getBufferInt(caps);
		nglGetVideoCaptureStreamivNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetVideoCaptureStreamNV(int video_capture_slot, int stream, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoCaptureStreamfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetVideoCaptureStreamfvNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVideoCaptureStreamfvNV(int video_capture_slot, int stream, int pname, long params, long function_pointer);

	/** Overloads glGetVideoCaptureStreamfvNV. */
	public static float glGetVideoCaptureStreamfNV(int video_capture_slot, int stream, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoCaptureStreamfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		FloatBuffer params = APIUtil.getBufferFloat(caps);
		nglGetVideoCaptureStreamfvNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static void glGetVideoCaptureStreamNV(int video_capture_slot, int stream, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoCaptureStreamdvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 1);
		nglGetVideoCaptureStreamdvNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglGetVideoCaptureStreamdvNV(int video_capture_slot, int stream, int pname, long params, long function_pointer);

	/** Overloads glGetVideoCaptureStreamdvNV. */
	public static double glGetVideoCaptureStreamdNV(int video_capture_slot, int stream, int pname) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glGetVideoCaptureStreamdvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		DoubleBuffer params = APIUtil.getBufferDouble(caps);
		nglGetVideoCaptureStreamdvNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
		return params.get(0);
	}

	public static int glVideoCaptureNV(int video_capture_slot, IntBuffer sequence_num, LongBuffer capture_time) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVideoCaptureNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(sequence_num, 1);
		BufferChecks.checkBuffer(capture_time, 1);
		int __result = nglVideoCaptureNV(video_capture_slot, MemoryUtil.getAddress(sequence_num), MemoryUtil.getAddress(capture_time), function_pointer);
		return __result;
	}
	static native int nglVideoCaptureNV(int video_capture_slot, long sequence_num, long capture_time, long function_pointer);

	public static void glVideoCaptureStreamParameterNV(int video_capture_slot, int stream, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVideoCaptureStreamParameterivNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 16);
		nglVideoCaptureStreamParameterivNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglVideoCaptureStreamParameterivNV(int video_capture_slot, int stream, int pname, long params, long function_pointer);

	public static void glVideoCaptureStreamParameterNV(int video_capture_slot, int stream, int pname, FloatBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVideoCaptureStreamParameterfvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 16);
		nglVideoCaptureStreamParameterfvNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglVideoCaptureStreamParameterfvNV(int video_capture_slot, int stream, int pname, long params, long function_pointer);

	public static void glVideoCaptureStreamParameterNV(int video_capture_slot, int stream, int pname, DoubleBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.glVideoCaptureStreamParameterdvNV;
		BufferChecks.checkFunctionAddress(function_pointer);
		BufferChecks.checkBuffer(params, 16);
		nglVideoCaptureStreamParameterdvNV(video_capture_slot, stream, pname, MemoryUtil.getAddress(params), function_pointer);
	}
	static native void nglVideoCaptureStreamParameterdvNV(int video_capture_slot, int stream, int pname, long params, long function_pointer);
}
