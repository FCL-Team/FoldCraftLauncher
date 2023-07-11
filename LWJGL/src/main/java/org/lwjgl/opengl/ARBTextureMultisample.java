/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.*;
import java.nio.*;

public final class ARBTextureMultisample {

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetMultisamplefv: 
	 */
	public static final int GL_SAMPLE_POSITION = 0x8E50;

	/**
	 *  Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 *  the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 *  GetDoublev:
	 */
	public static final int GL_SAMPLE_MASK = 0x8E51;

	/**
	 *  Accepted by the &lt;target&gt; parameter of GetBooleani_v and
	 *  GetIntegeri_v:
	 */
	public static final int GL_SAMPLE_MASK_VALUE = 0x8E52;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindTexture and
	 *  TexImage2DMultisample:
	 */
	public static final int GL_TEXTURE_2D_MULTISAMPLE = 0x9100;

	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage2DMultisample: 
	 */
	public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 0x9101;

	/**
	 *  Accepted by the &lt;target&gt; parameter of BindTexture and
	 *  TexImage3DMultisample:
	 */
	public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9102;

	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage3DMultisample: 
	 */
	public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9103;

	/**
	 *  Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 *  and GetFloatv:
	 */
	public static final int GL_MAX_SAMPLE_MASK_WORDS = 0x8E59,
		GL_MAX_COLOR_TEXTURE_SAMPLES = 0x910E,
		GL_MAX_DEPTH_TEXTURE_SAMPLES = 0x910F,
		GL_MAX_INTEGER_SAMPLES = 0x9110,
		GL_TEXTURE_BINDING_2D_MULTISAMPLE = 0x9104,
		GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 0x9105;

	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameter 
	 */
	public static final int GL_TEXTURE_SAMPLES = 0x9106,
		GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 0x9107;

	/**
	 * Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_2D_MULTISAMPLE = 0x9108,
		GL_INT_SAMPLER_2D_MULTISAMPLE = 0x9109,
		GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 0x910A,
		GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910B,
		GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910C,
		GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910D;

	private ARBTextureMultisample() {}

	public static void glTexImage2DMultisample(int target, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
		GL32.glTexImage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations);
	}

	public static void glTexImage3DMultisample(int target, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
		GL32.glTexImage3DMultisample(target, samples, internalformat, width, height, depth, fixedsamplelocations);
	}

	public static void glGetMultisample(int pname, int index, FloatBuffer val) {
		GL32.glGetMultisample(pname, index, val);
	}

	public static void glSampleMaski(int index, int mask) {
		GL32.glSampleMaski(index, mask);
	}
}
