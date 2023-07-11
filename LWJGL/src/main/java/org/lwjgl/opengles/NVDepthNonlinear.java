/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengles;

import org.lwjgl.*;
import java.nio.*;

public final class NVDepthNonlinear {

	/**
	 *  Accepted as a valid sized internal format by all functions accepting
	 *  sized internal formats with a base format of DEPTH_COMPONENT:
	 */
	public static final int GL_DEPTH_COMPONENT16_NONLINEAR_NV = 0x8553;

	/**
	 *  Accepted by the &lt;attrib_list&gt; parameter of eglChooseConfig,
	 *  and by the &lt;attribute&gt; parameter of eglGetConfigAttrib:
	 */
	public static final int EGL_DEPTH_ENCODING_NV = 0x30E2;

	/**
	 *  Accepted as a value in the &lt;attrib_list&gt; parameter of eglChooseConfig
	 *  and eglCreatePbufferSurface, and returned in the &lt;value&gt; parameter
	 *  of eglGetConfigAttrib:
	 */
	public static final int EGL_DEPTH_ENCODING_NONE_NV = 0x0,
		EGL_DEPTH_ENCODING_NONLINEAR_NV = 0x30E3;

	private NVDepthNonlinear() {}
}
