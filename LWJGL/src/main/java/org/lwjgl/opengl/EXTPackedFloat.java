/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class EXTPackedFloat {

	/**
	 *  Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 *  TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 *  RenderbufferStorageEXT:
	 */
	public static final int GL_R11F_G11F_B10F_EXT = 0x8C3A;

	/**
	 *  Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 *  TexImage1D, TexImage2D, GetTexImage, TexImage3D, TexSubImage1D,
	 *  TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 *  ConvolutionFilter1D, ConvolutionFilter2D, ConvolutionFilter3D,
	 *  GetConvolutionFilter, SeparableFilter2D, GetSeparableFilter,
	 *  ColorTable, ColorSubTable, and GetColorTable:
	 */
	public static final int GL_UNSIGNED_INT_10F_11F_11F_REV_EXT = 0x8C3B;

	/**
	 *  Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_RGBA_SIGNED_COMPONENTS_EXT = 0x8C3C;

	/**
	 *  Accepted as a value in the &lt;piAttribIList&gt; and &lt;pfAttribFList&gt;
	 *  parameter arrays of wglChoosePixelFormatARB, and returned in the
	 *  &lt;piValues&gt; parameter array of wglGetPixelFormatAttribivARB, and the
	 *  &lt;pfValues&gt; parameter array of wglGetPixelFormatAttribfvARB:
	 */
	public static final int WGL_TYPE_RGBA_UNSIGNED_FLOAT_EXT = 0x20A8;

	/**
	 *  Accepted as values of the &lt;render_type&gt; arguments in the
	 *  glXCreateNewContext and glXCreateContext functions
	 */
	public static final int GLX_RGBA_UNSIGNED_FLOAT_TYPE_EXT = 0x20B1;

	/**
	 *  Returned by glXGetFBConfigAttrib (when &lt;attribute&gt; is set to
	 *  GLX_RENDER_TYPE) and accepted by the &lt;attrib_list&gt; parameter of
	 *  glXChooseFBConfig (following the GLX_RENDER_TYPE token):
	 */
	public static final int GLX_RGBA_UNSIGNED_FLOAT_BIT_EXT = 0x8;

	private EXTPackedFloat() {}
}
