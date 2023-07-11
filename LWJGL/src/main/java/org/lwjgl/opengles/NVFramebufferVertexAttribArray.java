/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVFramebufferVertexAttribArray {

	public static final int GL_FRAMEBUFFER_ATTACHABLE_NV = 0x852A,
		GL_VERTEX_ATTRIB_ARRAY_NV = 0x852B,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_SIZE_NV = 0x852C,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_TYPE_NV = 0x852D,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_NORMALIZED_NV = 0x852E,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_OFFSET_NV = 0x852F,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_WIDTH_NV = 0x8530,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_STRIDE_NV = 0x8531,
		GL_FRAMEBUFFER_ATTACHMENT_VERTEX_ATTRIB_ARRAY_HEIGHT_NV = 0x8532;

	private NVFramebufferVertexAttribArray() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glFramebufferVertexAttribArrayNV(int target, int attachment, int buffertarget, int bufferobject, int size, int type, boolean normalized, long offset, long width, long height, int stride) {
		nglFramebufferVertexAttribArrayNV(target, attachment, buffertarget, bufferobject, size, type, normalized, offset, width, height, stride);
	}
	static native void nglFramebufferVertexAttribArrayNV(int target, int attachment, int buffertarget, int bufferobject, int size, int type, boolean normalized, long offset, long width, long height, int stride);
}
