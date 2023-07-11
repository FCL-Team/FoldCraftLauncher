/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class OESEGLImage {

	private OESEGLImage() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glEGLImageTargetTexture2DOES(int target, EGLImageOES image) {
		nglEGLImageTargetTexture2DOES(target, image.getPointer());
	}
	static native void nglEGLImageTargetTexture2DOES(int target, long image);

	public static void glEGLImageTargetRenderbufferStorageOES(int target, EGLImageOES image) {
		nglEGLImageTargetRenderbufferStorageOES(target, image.getPointer());
	}
	static native void nglEGLImageTargetRenderbufferStorageOES(int target, long image);
}
