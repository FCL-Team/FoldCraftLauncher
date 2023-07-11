/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTDebugLabel {

	/**
	 *  Accepted by the &lt;type&gt; parameter of LabelObjectEXT and
	 *  GetObjectLabelEXT:
	 */
	public static final int GL_BUFFER_OBJECT_EXT = 0x9151,
		GL_SHADER_OBJECT_EXT = 0x8B48,
		GL_PROGRAM_OBJECT_EXT = 0x8B40,
		GL_VERTEX_ARRAY_OBJECT_EXT = 0x9154,
		GL_QUERY_OBJECT_EXT = 0x9153,
		GL_PROGRAM_PIPELINE_OBJECT_EXT = 0x8A4F;

	private EXTDebugLabel() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glLabelObjectEXT(int type, int object, ByteBuffer label) {
		BufferChecks.checkDirect(label);
		nglLabelObjectEXT(type, object, label.remaining(), MemoryUtil.getAddress(label));
	}
	static native void nglLabelObjectEXT(int type, int object, int label_length, long label);

	/** Overloads glLabelObjectEXT. */
	public static void glLabelObjectEXT(int type, int object, CharSequence label) {
		nglLabelObjectEXT(type, object, label.length(), APIUtil.getBuffer(label));
	}

	public static void glGetObjectLabelEXT(int type, int object, IntBuffer length, ByteBuffer label) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		if (label != null)
			BufferChecks.checkDirect(label);
		nglGetObjectLabelEXT(type, object, (label == null ? 0 : label.remaining()), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddressSafe(label));
	}
	static native void nglGetObjectLabelEXT(int type, int object, int label_bufSize, long length, long label);

	/** Overloads glGetObjectLabelEXT. */
	public static String glGetObjectLabelEXT(int type, int object, int bufSize) {
		IntBuffer label_length = APIUtil.getLengths();
		ByteBuffer label = APIUtil.getBufferByte(bufSize);
		nglGetObjectLabelEXT(type, object, bufSize, MemoryUtil.getAddress0(label_length), MemoryUtil.getAddress(label));
		label.limit(label_length.get(0));
		return APIUtil.getString(label);
	}
}
