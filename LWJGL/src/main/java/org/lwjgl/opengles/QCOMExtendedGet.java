/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class QCOMExtendedGet {

	/**
	 * Accepted by the &lt;pname&gt; parameter of ExtGetTexLevelParameterivQCOM 
	 */
	public static final int GL_TEXTURE_WIDTH_QCOM = 0x8BD2,
		GL_TEXTURE_HEIGHT_QCOM = 0x8BD3,
		GL_TEXTURE_DEPTH_QCOM = 0x8BD4,
		GL_TEXTURE_INTERNAL_FORMAT_QCOM = 0x8BD5,
		GL_TEXTURE_FORMAT_QCOM = 0x8BD6,
		GL_TEXTURE_TYPE_QCOM = 0x8BD7,
		GL_TEXTURE_IMAGE_VALID_QCOM = 0x8BD8,
		GL_TEXTURE_NUM_LEVELS_QCOM = 0x8BD9,
		GL_TEXTURE_TARGET_QCOM = 0x8BDA,
		GL_TEXTURE_OBJECT_VALID_QCOM = 0x8BDB;

	/**
	 * Accepted by the &lt;pname&gt; parameter of ExtTexObjectStateOverrideiQCOM 
	 */
	public static final int GL_STATE_RESTORE = 0x8BDC;

	private QCOMExtendedGet() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glExtGetTexturesQCOM(IntBuffer textures, IntBuffer numTextures) {
		BufferChecks.checkBuffer(textures, 1);
		BufferChecks.checkBuffer(numTextures, 1);
		nglExtGetTexturesQCOM(MemoryUtil.getAddress(textures), textures.remaining(), MemoryUtil.getAddress(numTextures));
	}
	static native void nglExtGetTexturesQCOM(long textures, int textures_maxTextures, long numTextures);

	public static void glExtGetBuffersQCOM(IntBuffer buffers, IntBuffer numBuffers) {
		BufferChecks.checkBuffer(buffers, 1);
		BufferChecks.checkBuffer(numBuffers, 1);
		nglExtGetBuffersQCOM(MemoryUtil.getAddress(buffers), buffers.remaining(), MemoryUtil.getAddress(numBuffers));
	}
	static native void nglExtGetBuffersQCOM(long buffers, int buffers_maxBuffers, long numBuffers);

	public static void glExtGetRenderbuffersQCOM(IntBuffer renderbuffers, IntBuffer numRenderbuffers) {
		BufferChecks.checkBuffer(renderbuffers, 1);
		BufferChecks.checkBuffer(numRenderbuffers, 1);
		nglExtGetRenderbuffersQCOM(MemoryUtil.getAddress(renderbuffers), renderbuffers.remaining(), MemoryUtil.getAddress(numRenderbuffers));
	}
	static native void nglExtGetRenderbuffersQCOM(long renderbuffers, int renderbuffers_maxRenderbuffers, long numRenderbuffers);

	public static void glExtGetFramebuffersQCOM(IntBuffer framebuffers, IntBuffer numFramebuffers) {
		BufferChecks.checkBuffer(framebuffers, 1);
		BufferChecks.checkBuffer(numFramebuffers, 1);
		nglExtGetFramebuffersQCOM(MemoryUtil.getAddress(framebuffers), framebuffers.remaining(), MemoryUtil.getAddress(numFramebuffers));
	}
	static native void nglExtGetFramebuffersQCOM(long framebuffers, int framebuffers_maxFramebuffers, long numFramebuffers);

	public static void glExtGetTexLevelParameterivQCOM(int texture, int face, int level, int pname, IntBuffer params) {
		BufferChecks.checkBuffer(params, 1);
		nglExtGetTexLevelParameterivQCOM(texture, face, level, pname, MemoryUtil.getAddress(params));
	}
	static native void nglExtGetTexLevelParameterivQCOM(int texture, int face, int level, int pname, long params);

	public static void glExtTexObjectStateOverrideiQCOM(int target, int pname, int param) {
		nglExtTexObjectStateOverrideiQCOM(target, pname, param);
	}
	static native void nglExtTexObjectStateOverrideiQCOM(int target, int pname, int param);

	public static void glExtGetTexSubImageQCOM(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer texels) {
		BufferChecks.checkBuffer(texels, GLChecks.calculateImageStorage(texels, format, type, width, height, depth));
		nglExtGetTexSubImageQCOM(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(texels));
	}
	public static void glExtGetTexSubImageQCOM(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, FloatBuffer texels) {
		BufferChecks.checkBuffer(texels, GLChecks.calculateImageStorage(texels, format, type, width, height, depth));
		nglExtGetTexSubImageQCOM(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(texels));
	}
	public static void glExtGetTexSubImageQCOM(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, IntBuffer texels) {
		BufferChecks.checkBuffer(texels, GLChecks.calculateImageStorage(texels, format, type, width, height, depth));
		nglExtGetTexSubImageQCOM(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(texels));
	}
	public static void glExtGetTexSubImageQCOM(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ShortBuffer texels) {
		BufferChecks.checkBuffer(texels, GLChecks.calculateImageStorage(texels, format, type, width, height, depth));
		nglExtGetTexSubImageQCOM(target, level, xoffset, yoffset, zoffset, width, height, depth, format, type, MemoryUtil.getAddress(texels));
	}
	static native void nglExtGetTexSubImageQCOM(int target, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, long texels);

	public static ByteBuffer glExtGetBufferPointervQCOM(int target, long result_size) {
		ByteBuffer __result = nglExtGetBufferPointervQCOM(target, result_size);
		return LWJGLUtil.CHECKS && __result == null ? null : __result.order(ByteOrder.nativeOrder());
	}
	static native ByteBuffer nglExtGetBufferPointervQCOM(int target, long result_size);
}
