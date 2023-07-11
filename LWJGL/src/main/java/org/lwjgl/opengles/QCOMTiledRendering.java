/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class QCOMTiledRendering {

	/**
	 *  Accepted by the &lt;preserveMask&gt; parameter of StartTilingQCOM and
	 *  EndTilingQCOM
	 */
	public static final int GL_COLOR_BUFFER_BIT0_QCOM = 0x1,
		GL_COLOR_BUFFER_BIT1_QCOM = 0x2,
		GL_COLOR_BUFFER_BIT2_QCOM = 0x4,
		GL_COLOR_BUFFER_BIT3_QCOM = 0x8,
		GL_COLOR_BUFFER_BIT4_QCOM = 0x10,
		GL_COLOR_BUFFER_BIT5_QCOM = 0x20,
		GL_COLOR_BUFFER_BIT6_QCOM = 0x40,
		GL_COLOR_BUFFER_BIT7_QCOM = 0x80,
		GL_DEPTH_BUFFER_BIT0_QCOM = 0x100,
		GL_DEPTH_BUFFER_BIT1_QCOM = 0x200,
		GL_DEPTH_BUFFER_BIT2_QCOM = 0x400,
		GL_DEPTH_BUFFER_BIT3_QCOM = 0x800,
		GL_DEPTH_BUFFER_BIT4_QCOM = 0x1000,
		GL_DEPTH_BUFFER_BIT5_QCOM = 0x2000,
		GL_DEPTH_BUFFER_BIT6_QCOM = 0x4000,
		GL_DEPTH_BUFFER_BIT7_QCOM = 0x8000,
		GL_STENCIL_BUFFER_BIT0_QCOM = 0x10000,
		GL_STENCIL_BUFFER_BIT1_QCOM = 0x20000,
		GL_STENCIL_BUFFER_BIT2_QCOM = 0x40000,
		GL_STENCIL_BUFFER_BIT3_QCOM = 0x80000,
		GL_STENCIL_BUFFER_BIT4_QCOM = 0x100000,
		GL_STENCIL_BUFFER_BIT5_QCOM = 0x200000,
		GL_STENCIL_BUFFER_BIT6_QCOM = 0x400000,
		GL_STENCIL_BUFFER_BIT7_QCOM = 0x800000,
		GL_MULTISAMPLE_BUFFER_BIT0_QCOM = 0x1000000,
		GL_MULTISAMPLE_BUFFER_BIT1_QCOM = 0x2000000,
		GL_MULTISAMPLE_BUFFER_BIT2_QCOM = 0x4000000,
		GL_MULTISAMPLE_BUFFER_BIT3_QCOM = 0x8000000,
		GL_MULTISAMPLE_BUFFER_BIT4_QCOM = 0x10000000,
		GL_MULTISAMPLE_BUFFER_BIT5_QCOM = 0x20000000,
		GL_MULTISAMPLE_BUFFER_BIT6_QCOM = 0x40000000,
		GL_MULTISAMPLE_BUFFER_BIT7_QCOM = 0x80000000;

	private QCOMTiledRendering() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void glStartTilingQCOM(int x, int y, int width, int height, int preserveMask) {
		nglStartTilingQCOM(x, y, width, height, preserveMask);
	}
	static native void nglStartTilingQCOM(int x, int y, int width, int height, int preserveMask);

	public static void glEndTilingQCOM(int preserveMask) {
		nglEndTilingQCOM(preserveMask);
	}
	static native void nglEndTilingQCOM(int preserveMask);
}
