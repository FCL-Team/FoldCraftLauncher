/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVDrawTexture {

	private NVDrawTexture() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glDrawTextureNV(int texture, int sampler, float x0, float y0, float x1, float y1, float z, float s0, float t0, float s1, float t1) {
		nglDrawTextureNV(texture, sampler, x0, y0, x1, y1, z, s0, t0, s1, t1);
	}
	static native void nglDrawTextureNV(int texture, int sampler, float x0, float y0, float x1, float y1, float z, float s0, float t0, float s1, float t1);
}
