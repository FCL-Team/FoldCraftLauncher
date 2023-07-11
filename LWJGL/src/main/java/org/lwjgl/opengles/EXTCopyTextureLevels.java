/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class EXTCopyTextureLevels {

	private EXTCopyTextureLevels() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glCopyTextureLevelsAPPLE(int destinationTexture, int sourceTexture, int sourceBaseLevel, int sourceLevelCount) {
		nglCopyTextureLevelsAPPLE(destinationTexture, sourceTexture, sourceBaseLevel, sourceLevelCount);
	}
	static native void nglCopyTextureLevelsAPPLE(int destinationTexture, int sourceTexture, int sourceBaseLevel, int sourceLevelCount);
}
