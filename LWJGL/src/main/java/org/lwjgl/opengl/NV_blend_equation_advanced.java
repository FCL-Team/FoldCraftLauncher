/*
 * Copyright (c) 2002-2013 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.opengl;

import org.lwjgl.util.generator.opengl.GLenum;

public interface NV_blend_equation_advanced {

	/**
	 * Accepted by the &lt;cap&gt; parameter of Disable, Enable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv, GetFloatv, GetDoublev
	 * and GetInteger64v:
	 */
	int GL_BLEND_ADVANCED_COHERENT_NV = 0x9285;

	/**
	 * Accepted by the &lt;pname&gt; parameter of BlendParameteriNV, GetBooleanv,
	 * GetIntegerv, GetInteger64v, GetFloatv, and GetDoublev:
	 */
	int GL_BLEND_PREMULTIPLIED_SRC_NV = 0x9280,
		GL_BLEND_OVERLAP_NV           = 0x9281;

	/**
	 * Accepted by the &lt;value&gt; parameter of BlendParameteriNV when &lt;pname&gt; is
	 * BLEND_OVERLAP_NV:
	 */
	int GL_UNCORRELATED_NV = 0x9282,
		GL_DISJOINT_NV     = 0x9283,
		GL_CONJOINT_NV     = 0x9284;

	/** Accepted by the &lt;mode&gt; parameter of BlendEquation and BlendEquationi: */
	int GL_SRC_NV                = 0x9286,
		GL_DST_NV                = 0x9287,
		GL_SRC_OVER_NV           = 0x9288,
		GL_DST_OVER_NV           = 0x9289,
		GL_SRC_IN_NV             = 0x928A,
		GL_DST_IN_NV             = 0x928B,
		GL_SRC_OUT_NV            = 0x928C,
		GL_DST_OUT_NV            = 0x928D,
		GL_SRC_ATOP_NV           = 0x928E,
		GL_DST_ATOP_NV           = 0x928F,
		GL_MULTIPLY_NV           = 0x9294,
		GL_SCREEN_NV             = 0x9295,
		GL_OVERLAY_NV            = 0x9296,
		GL_DARKEN_NV             = 0x9297,
		GL_LIGHTEN_NV            = 0x9298,
		GL_COLORDODGE_NV         = 0x9299,
		GL_COLORBURN_NV          = 0x929A,
		GL_HARDLIGHT_NV          = 0x929B,
		GL_SOFTLIGHT_NV          = 0x929C,
		GL_DIFFERENCE_NV         = 0x929E,
		GL_EXCLUSION_NV          = 0x92A0,
		GL_INVERT_RGB_NV         = 0x92A3,
		GL_LINEARDODGE_NV        = 0x92A4,
		GL_LINEARBURN_NV         = 0x92A5,
		GL_VIVIDLIGHT_NV         = 0x92A6,
		GL_LINEARLIGHT_NV        = 0x92A7,
		GL_PINLIGHT_NV           = 0x92A8,
		GL_HARDMIX_NV            = 0x92A9,
		GL_HSL_HUE_NV            = 0x92AD,
		GL_HSL_SATURATION_NV     = 0x92AE,
		GL_HSL_COLOR_NV          = 0x92AF,
		GL_HSL_LUMINOSITY_NV     = 0x92B0,
		GL_PLUS_NV               = 0x9291,
		GL_PLUS_CLAMPED_NV       = 0x92B1,
		GL_PLUS_CLAMPED_ALPHA_NV = 0x92B2,
		GL_PLUS_DARKER_NV        = 0x9292,
		GL_MINUS_NV              = 0x929F,
		GL_MINUS_CLAMPED_NV      = 0x92B3,
		GL_CONTRAST_NV           = 0x92A1,
		GL_INVERT_OVG_NV         = 0x92B4;

	void glBlendParameteriNV(@GLenum int pname, int value);

	void glBlendBarrierNV();
}
