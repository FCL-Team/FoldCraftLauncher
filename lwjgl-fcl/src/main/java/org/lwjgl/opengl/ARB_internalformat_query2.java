/*
 * Copyright (c) 2002-2012 LWJGL Project
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

import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLenum;
import org.lwjgl.util.generator.opengl.GLint64;
import org.lwjgl.util.generator.opengl.GLreturn;
import org.lwjgl.util.generator.opengl.GLsizei;

import java.nio.LongBuffer;

@Extension(postfix = "")
public interface ARB_internalformat_query2 {

	/**
	 * Accepted by the &lt;target&gt; parameter of GetInternalformativ
	 * and GetInternalformati64v:
	 */
	int GL_TEXTURE_1D_ARRAY             = 0x8C18,
		GL_TEXTURE_2D_ARRAY             = 0x8C1A,
		GL_TEXTURE_CUBE_MAP_ARRAY       = 0x9009,
		GL_TEXTURE_RECTANGLE            = 0x84F5,
		GL_TEXTURE_BUFFER               = 0x8C2A,
		GL_RENDERBUFFER                 = 0x8D41,
		GL_TEXTURE_2D_MULTISAMPLE       = 0x9100,
		GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9102;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetInternalformativ
	 * and GetInternalformati64v:
	 */
	int GL_NUM_SAMPLE_COUNTS                      = 0x9380,
		GL_INTERNALFORMAT_SUPPORTED               = 0x826F,
		GL_INTERNALFORMAT_PREFERRED               = 0x8270,
		GL_INTERNALFORMAT_RED_SIZE                = 0x8271,
		GL_INTERNALFORMAT_GREEN_SIZE              = 0x8272,
		GL_INTERNALFORMAT_BLUE_SIZE               = 0x8273,
		GL_INTERNALFORMAT_ALPHA_SIZE              = 0x8274,
		GL_INTERNALFORMAT_DEPTH_SIZE              = 0x8275,
		GL_INTERNALFORMAT_STENCIL_SIZE            = 0x8276,
		GL_INTERNALFORMAT_SHARED_SIZE             = 0x8277,
		GL_INTERNALFORMAT_RED_TYPE                = 0x8278,
		GL_INTERNALFORMAT_GREEN_TYPE              = 0x8279,
		GL_INTERNALFORMAT_BLUE_TYPE               = 0x827A,
		GL_INTERNALFORMAT_ALPHA_TYPE              = 0x827B,
		GL_INTERNALFORMAT_DEPTH_TYPE              = 0x827C,
		GL_INTERNALFORMAT_STENCIL_TYPE            = 0x827D,
		GL_MAX_WIDTH                              = 0x827E,
		GL_MAX_HEIGHT                             = 0x827F,
		GL_MAX_DEPTH                              = 0x8280,
		GL_MAX_LAYERS                             = 0x8281,
		GL_MAX_COMBINED_DIMENSIONS                = 0x8282,
		GL_COLOR_COMPONENTS                       = 0x8283,
		GL_DEPTH_COMPONENTS                       = 0x8284,
		GL_STENCIL_COMPONENTS                     = 0x8285,
		GL_COLOR_RENDERABLE                       = 0x8286,
		GL_DEPTH_RENDERABLE                       = 0x8287,
		GL_STENCIL_RENDERABLE                     = 0x8288,
		GL_FRAMEBUFFER_RENDERABLE                 = 0x8289,
		GL_FRAMEBUFFER_RENDERABLE_LAYERED         = 0x828A,
		GL_FRAMEBUFFER_BLEND                      = 0x828B,
		GL_READ_PIXELS                            = 0x828C,
		GL_READ_PIXELS_FORMAT                     = 0x828D,
		GL_READ_PIXELS_TYPE                       = 0x828E,
		GL_TEXTURE_IMAGE_FORMAT                   = 0x828F,
		GL_TEXTURE_IMAGE_TYPE                     = 0x8290,
		GL_GET_TEXTURE_IMAGE_FORMAT               = 0x8291,
		GL_GET_TEXTURE_IMAGE_TYPE                 = 0x8292,
		GL_MIPMAP                                 = 0x8293,
		GL_MANUAL_GENERATE_MIPMAP                 = 0x8294,
		GL_AUTO_GENERATE_MIPMAP                   = 0x8295,
		GL_COLOR_ENCODING                         = 0x8296,
		GL_SRGB_READ                              = 0x8297,
		GL_SRGB_WRITE                             = 0x8298,
		GL_SRGB_DECODE_ARB                        = 0x8299,
		GL_FILTER                                 = 0x829A,
		GL_VERTEX_TEXTURE                         = 0x829B,
		GL_TESS_CONTROL_TEXTURE                   = 0x829C,
		GL_TESS_EVALUATION_TEXTURE                = 0x829D,
		GL_GEOMETRY_TEXTURE                       = 0x829E,
		GL_FRAGMENT_TEXTURE                       = 0x829F,
		GL_COMPUTE_TEXTURE                        = 0x82A0,
		GL_TEXTURE_SHADOW                         = 0x82A1,
		GL_TEXTURE_GATHER                         = 0x82A2,
		GL_TEXTURE_GATHER_SHADOW                  = 0x82A3,
		GL_SHADER_IMAGE_LOAD                      = 0x82A4,
		GL_SHADER_IMAGE_STORE                     = 0x82A5,
		GL_SHADER_IMAGE_ATOMIC                    = 0x82A6,
		GL_IMAGE_TEXEL_SIZE                       = 0x82A7,
		GL_IMAGE_COMPATIBILITY_CLASS              = 0x82A8,
		GL_IMAGE_PIXEL_FORMAT                     = 0x82A9,
		GL_IMAGE_PIXEL_TYPE                       = 0x82AA,
		GL_IMAGE_FORMAT_COMPATIBILITY_TYPE        = 0x90C7,
		GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST    = 0x82AC,
		GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST  = 0x82AD,
		GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE   = 0x82AE,
		GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE = 0x82AF,
		GL_TEXTURE_COMPRESSED_BLOCK_WIDTH         = 0x82B1,
		GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT        = 0x82B2,
		GL_TEXTURE_COMPRESSED_BLOCK_SIZE          = 0x82B3,
		GL_CLEAR_BUFFER                           = 0x82B4,
		GL_TEXTURE_VIEW                           = 0x82B5,
		GL_VIEW_COMPATIBILITY_CLASS               = 0x82B6;

	/**
	 * Returned as possible responses for various &lt;pname&gt; queries
	 * to GetInternalformativ and GetInternalformati64v
	 */
	int GL_FULL_SUPPORT              = 0x82B7,
		GL_CAVEAT_SUPPORT            = 0x82B8,
		GL_IMAGE_CLASS_4_X_32        = 0x82B9,
		GL_IMAGE_CLASS_2_X_32        = 0x82BA,
		GL_IMAGE_CLASS_1_X_32        = 0x82BB,
		GL_IMAGE_CLASS_4_X_16        = 0x82BC,
		GL_IMAGE_CLASS_2_X_16        = 0x82BD,
		GL_IMAGE_CLASS_1_X_16        = 0x82BE,
		GL_IMAGE_CLASS_4_X_8         = 0x82BF,
		GL_IMAGE_CLASS_2_X_8         = 0x82C0,
		GL_IMAGE_CLASS_1_X_8         = 0x82C1,
		GL_IMAGE_CLASS_11_11_10      = 0x82C2,
		GL_IMAGE_CLASS_10_10_10_2    = 0x82C3,
		GL_VIEW_CLASS_128_BITS       = 0x82C4,
		GL_VIEW_CLASS_96_BITS        = 0x82C5,
		GL_VIEW_CLASS_64_BITS        = 0x82C6,
		GL_VIEW_CLASS_48_BITS        = 0x82C7,
		GL_VIEW_CLASS_32_BITS        = 0x82C8,
		GL_VIEW_CLASS_24_BITS        = 0x82C9,
		GL_VIEW_CLASS_16_BITS        = 0x82CA,
		GL_VIEW_CLASS_8_BITS         = 0x82CB,
		GL_VIEW_CLASS_S3TC_DXT1_RGB  = 0x82CC,
		GL_VIEW_CLASS_S3TC_DXT1_RGBA = 0x82CD,
		GL_VIEW_CLASS_S3TC_DXT3_RGBA = 0x82CE,
		GL_VIEW_CLASS_S3TC_DXT5_RGBA = 0x82CF,
		GL_VIEW_CLASS_RGTC1_RED      = 0x82D0,
		GL_VIEW_CLASS_RGTC2_RG       = 0x82D1,
		GL_VIEW_CLASS_BPTC_UNORM     = 0x82D2,
		GL_VIEW_CLASS_BPTC_FLOAT     = 0x82D3;

	@Reuse("GL43")
	@StripPostfix("params")
	void glGetInternalformati64v(@GLenum int target, @GLenum int internalformat,
	                             @GLenum int pname, @AutoSize("params") @GLsizei int bufSize, @OutParameter @GLint64 LongBuffer params);

	@Reuse("GL43")
	@Alternate("glGetInternalformati64v")
	@GLreturn("params")
	@StripPostfix(value = "params", hasPostfix = false)
	void glGetInternalformati64v2(@GLenum int target, @GLenum int internalformat,
	                              @GLenum int pname, @Constant("1") @GLsizei int bufSize, @OutParameter @GLint64 LongBuffer params);

}