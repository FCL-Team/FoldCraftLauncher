/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class QCOMDriverControl {

	private QCOMDriverControl() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glGetDriverControlsQCOM(IntBuffer num, IntBuffer driverControls) {
		if (num != null)
			BufferChecks.checkBuffer(num, 1);
		if (driverControls != null)
			BufferChecks.checkDirect(driverControls);
		nglGetDriverControlsQCOM(MemoryUtil.getAddressSafe(num), (driverControls == null ? 0 : driverControls.remaining()), MemoryUtil.getAddressSafe(driverControls));
	}
	static native void nglGetDriverControlsQCOM(long num, int driverControls_size, long driverControls);

	public static void glGetDriverControlStringQCOM(int driverControl, IntBuffer length, ByteBuffer driverControlString) {
		if (length != null)
			BufferChecks.checkBuffer(length, 1);
		if (driverControlString != null)
			BufferChecks.checkDirect(driverControlString);
		nglGetDriverControlStringQCOM(driverControl, (driverControlString == null ? 0 : driverControlString.remaining()), MemoryUtil.getAddressSafe(length), MemoryUtil.getAddressSafe(driverControlString));
	}
	static native void nglGetDriverControlStringQCOM(int driverControl, int driverControlString_bufSize, long length, long driverControlString);

	/** Overloads glGetDriverControlStringQCOM. */
	public static String glGetDriverControlStringQCOM(int driverControl, int bufSize) {
		IntBuffer driverControlString_length = APIUtil.getLengths();
		ByteBuffer driverControlString = APIUtil.getBufferByte(bufSize);
		nglGetDriverControlStringQCOM(driverControl, bufSize, MemoryUtil.getAddress0(driverControlString_length), MemoryUtil.getAddress(driverControlString));
		driverControlString.limit(driverControlString_length.get(0));
		return APIUtil.getString(driverControlString);
	}

	public static void glEnableDriverControlQCOM(int driverControl) {
		nglEnableDriverControlQCOM(driverControl);
	}
	static native void nglEnableDriverControlQCOM(int driverControl);

	public static void glDisableDriverControlQCOM(int driverControl) {
		nglDisableDriverControlQCOM(driverControl);
	}
	static native void nglDisableDriverControlQCOM(int driverControl);
}
