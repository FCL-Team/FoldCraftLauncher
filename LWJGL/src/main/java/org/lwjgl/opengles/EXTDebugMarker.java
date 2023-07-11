/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTDebugMarker {

	private EXTDebugMarker() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glInsertEventMarkerEXT(ByteBuffer marker) {
		BufferChecks.checkDirect(marker);
		nglInsertEventMarkerEXT(marker.remaining(), MemoryUtil.getAddress(marker));
	}
	static native void nglInsertEventMarkerEXT(int marker_length, long marker);

	/** Overloads glInsertEventMarkerEXT. */
	public static void glInsertEventMarkerEXT(CharSequence marker) {
		nglInsertEventMarkerEXT(marker.length(), APIUtil.getBuffer(marker));
	}

	public static void glPushGroupMarkerEXT(ByteBuffer marker) {
		BufferChecks.checkDirect(marker);
		nglPushGroupMarkerEXT(marker.remaining(), MemoryUtil.getAddress(marker));
	}
	static native void nglPushGroupMarkerEXT(int marker_length, long marker);

	/** Overloads glPushGroupMarkerEXT. */
	public static void glPushGroupMarkerEXT(CharSequence marker) {
		nglPushGroupMarkerEXT(marker.length(), APIUtil.getBuffer(marker));
	}

	public static void glPopGroupMarkerEXT() {
		nglPopGroupMarkerEXT();
	}
	static native void nglPopGroupMarkerEXT();
}
