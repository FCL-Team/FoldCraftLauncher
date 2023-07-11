/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVCoverageSample {

	/**
	 *  Accepted by the &lt;attrib_list&gt; parameter of eglChooseConfig
	 *  and eglCreatePbufferSurface, and by the &lt;attribute&gt;
	 *  parameter of eglGetConfigAttrib:
	 */
	public static final int EGL_COVERAGE_BUFFERS_NV = 0x30E0,
		EGL_COVERAGE_SAMPLES_NV = 0x30E1;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of
	 *  RenderbufferStorageEXT and the &lt;format&gt; parameter of ReadPixels:
	 */
	public static final int GL_COVERAGE_COMPONENT_NV = 0x8522;

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of
	 *  RenderbufferStorageEXT:
	 */
	public static final int GL_COVERAGE_COMPONENT4_NV = 0x8523;

	/**
	 * Accepted by the &lt;operation&gt; parameter of CoverageOperationNV: 
	 */
	public static final int GL_COVERAGE_ALL_FRAGMENTS_NV = 0x8524,
		GL_COVERAGE_EDGE_FRAGMENTS_NV = 0x8525,
		GL_COVERAGE_AUTOMATIC_NV = 0x8526;

	/**
	 *  Accepted by the &lt;attachment&gt; parameter of
	 *  FramebufferRenderbuffer, and GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_COVERAGE_ATTACHMENT_NV = 0x8527;

	/**
	 * Accepted by the &lt;buf&gt; parameter of Clear: 
	 */
	public static final int GL_COVERAGE_BUFFER_BIT_NV = 0x8000;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetIntegerv: 
	 */
	public static final int GL_COVERAGE_BUFFERS_NV = 0x8528,
		GL_COVERAGE_SAMPLES_NV = 0x8529;

	private NVCoverageSample() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glCoverageMaskNV(boolean mask) {
		nglCoverageMaskNV(mask);
	}
	static native void nglCoverageMaskNV(boolean mask);

	public static void glCoverageOperationNV(int operation) {
		nglCoverageOperationNV(operation);
	}
	static native void nglCoverageOperationNV(int operation);
}
