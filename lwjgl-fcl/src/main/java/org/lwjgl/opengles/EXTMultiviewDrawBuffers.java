/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTMultiviewDrawBuffers {

	/**
	 * Accepted by the &lt;location&gt; parameter of DrawBuffersIndexedEXT: 
	 */
	public static final int GL_COLOR_ATTACHMENT_EXT = 0x90F0,
		GL_MULTIVIEW_EXT = 0x90F1;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetIntegeri_EXT: 
	 */
	public static final int GL_DRAW_BUFFER_EXT = 0xC01,
		GL_READ_BUFFER_EXT = 0xC02;

	/**
	 * Accepted by the &lt;target&gt; parameter of GetInteger: 
	 */
	public static final int GL_MAX_MULTIVIEW_BUFFERS_EXT = 0x90F2;

	private EXTMultiviewDrawBuffers() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glReadBufferIndexedEXT(int src, int index) {
		nglReadBufferIndexedEXT(src, index);
	}
	static native void nglReadBufferIndexedEXT(int src, int index);

	public static void glDrawBuffersIndexedEXT(IntBuffer location, IntBuffer indices) {
		BufferChecks.checkBuffer(location, indices.remaining());
		BufferChecks.checkDirect(indices);
		nglDrawBuffersIndexedEXT(indices.remaining(), MemoryUtil.getAddress(location), MemoryUtil.getAddress(indices));
	}
	static native void nglDrawBuffersIndexedEXT(int indices_n, long location, long indices);

	public static void glGetIntegerEXT(int target, int index, IntBuffer data) {
		BufferChecks.checkBuffer(data, 4);
		nglGetIntegeri_vEXT(target, index, MemoryUtil.getAddress(data));
	}
	static native void nglGetIntegeri_vEXT(int target, int index, long data);

	/** Overloads glGetIntegeri_vEXT. */
	public static int glGetIntegerEXT(int value, int index) {
		IntBuffer data = APIUtil.getBufferInt();
		nglGetIntegeri_vEXT(value, index, MemoryUtil.getAddress(data));
		return data.get(0);
	}
}
