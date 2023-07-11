/*
 * Copyright (c) 2002-2011 LWJGL Project
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
package org.lwjgl.opengles;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.PixelFormatLWJGL;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.opengles.EGL.*;
import static org.lwjgl.opengles.NVCoverageSample.*;
import static org.lwjgl.opengles.NVDepthNonlinear.*;
import static org.lwjgl.opengles.PixelFormat.Attrib.*;

/**
 * This class describes the configuration settings for an EGL surface. Instances
 * of this class are used as arguments to Display.create(). The attributes specified
 * in this class will be used to get EGLConfigs from an EGLDisplay. PixelFormat
 * is not the best name for this class, but it matches the corresponding class
 * in the official desktop LWJGL.
 * <p/>
 * Instances of this class are immutable. An example of the expected way to set
 * the PixelFormat property values is the following:
 * <code>PixelFormat pf = new PixelFormat().withDepth(24).withSamples(4);</code>
 * <p/>
 * Attributes that correspond to EGL extensions will be silently ignored if those
 * extensions are not supported by the EGLDisplay.
 */
public final class PixelFormat implements PixelFormatLWJGL {

	public static enum Attrib {
		// CORE ATTRIBUTES

		RED_SIZE(EGL_RED_SIZE, 0),
		GREEN_SIZE(EGL_GREEN_SIZE, 0),
		BLUE_SIZE(EGL_BLUE_SIZE, 0),
		ALPHA_SIZE(EGL_ALPHA_SIZE, 0),

		LUMINANCE_SIZE(EGL_LUMINANCE_SIZE, 0),

		DEPTH_SIZE(EGL_DEPTH_SIZE, 0),
		STENCIL_SIZE(EGL_STENCIL_SIZE, 0),

		MIN_SWAP_INTERVAL(EGL_MIN_SWAP_INTERVAL, EGL_DONT_CARE),
		MAX_SWAP_INTERVAL(EGL_MAX_SWAP_INTERVAL, EGL_DONT_CARE),

		SAMPLES(EGL_SAMPLES, 0),
		SAMPLE_BUFFERS(EGL_SAMPLE_BUFFERS, 0),

		TRANSPARENT_TYPE(EGL_TRANSPARENT_TYPE, EGL_NONE),
		TRANSPARENT_RED_VALUE(EGL_TRANSPARENT_RED_VALUE, EGL_DONT_CARE),
		TRANSPARENT_GREEN_VALUE(EGL_TRANSPARENT_GREEN_VALUE, EGL_DONT_CARE),
		TRANSPARENT_BLUE_VALUE(EGL_TRANSPARENT_BLUE_VALUE, EGL_DONT_CARE),

		// SURFACE ATTRIBUTES

		MULTISAMPLE_RESOLVE(EGL_MULTISAMPLE_RESOLVE, EGL_MULTISAMPLE_RESOLVE_DEFAULT, true),
		SWAP_BEHAVIOR(EGL_SWAP_BEHAVIOR, EGL_DONT_CARE, true),

		// EXTENSION ATTRIBUTES

		COVERAGE_SAMPLES_NV(EGL_COVERAGE_SAMPLES_NV, 0),
		COVERAGE_BUFFERS_NV(EGL_COVERAGE_BUFFERS_NV, 0),

		DEPTH_ENCODING_NONLINEAR_NV(EGL_DEPTH_ENCODING_NONLINEAR_NV, EGL_DONT_CARE);

		private final int eglAttrib;
		private final int defaultValue;

		private final boolean surfaceAttrib;

		Attrib(final int eglAttrib, final int defaultValue) {
			this(eglAttrib, defaultValue, false);
		}

		Attrib(final int eglAttrib, final int defaultValue, final boolean surfaceAttrib) {
			this.eglAttrib = eglAttrib;
			this.defaultValue = defaultValue;

			this.surfaceAttrib = surfaceAttrib;
		}

		/**
		 * Returns the EGL token that corresponds to this attribute.
		 *
		 * @return the EGL attribute token
		 */
		public int getEGLAttrib() {
			return eglAttrib;
		}

		/**
		 * Returns the default value of this attribute. Attributes
		 * with default values will be ignored when choosing the EGLConfig.
		 *
		 * @return the default value
		 */
		public int getDefaultValue() {
			return defaultValue;
		}

		public boolean isSurfaceAttrib() {
			return surfaceAttrib;
		}

	}

	private final Map<Attrib, Integer> config = new HashMap<Attrib, Integer>(16);

	/**
	 * Creates a new PixelFormat with rgbSize = 8, alphaSize = 8 and depthSize = 16.
	 *
	 * @see #PixelFormat(int, int, int, int, int, int)
	 */
	public PixelFormat() {
		this(8, 16, 0);
	}

	/**
	 * Creates a new PixelFormat with rgbSize = 8 and the specified
	 * alphaSize, depthSize and stencilSize.
	 *
	 * @param alphaSize   the EGL_ALPHA_SIZE value
	 * @param depthSize   the EGL_DEPTH_SIZE value
	 * @param stencilSize the EGL_STENCIL_SIZE value
	 *
	 * @see #PixelFormat(int, int, int, int, int, int)
	 */
	public PixelFormat(int alphaSize, int depthSize, int stencilSize) {
		this(alphaSize, depthSize, stencilSize, 0);
	}

	/**
	 * Creates a new PixelFormat with rgbSize = 8 and the specified
	 * alphaSize, depthSize, stencilSize and samples.
	 *
	 * @param alphaSize   the EGL_ALPHA_SIZE value
	 * @param depthSize   the EGL_DEPTH_SIZE value
	 * @param stencilSize the EGL_STENCIL_SIZE value
	 * @param samples     the EGL_SAMPLE_SIZE value
	 *
	 * @see #PixelFormat(int, int, int, int, int, int)
	 */
	public PixelFormat(int alphaSize, int depthSize, int stencilSize, int samples) {
		this(8, alphaSize, 0, depthSize, stencilSize, samples);
	}

	/**
	 * Creates a new PixelFormat with the specified RGB sizes, EGL_ALPHA_SIZE,
	 * EGL_LUMINANCE_SIZE, EGL_DEPTH_SIZE, EGL_STENCIL_SIZE, EGL_SAMPLES.
	 * All values must be greater than or equal to 0. rgbSize and luminanceSize
	 * cannot both be greater than 0. depthSize greater than 24 and stencilSize
	 * greater than 8 are not recommended.
	 * The corresponding EGL_SAMPLE_BUFFERS value will become 0 if samples is 0,
	 * or 1 if samples is greater than 0.
	 *
	 * @param rgbSize       the RGB sizes
	 * @param alphaSize     the EGL_ALPHA_SIZE value
	 * @param luminanceSize the EGL_LUMINANCE_SIZE value
	 * @param depthSize     the EGL_DEPTH_SIZE value
	 * @param stencilSize   the EGL_STENCIL_SIZE value
	 * @param samples       the EGL_SAMPLE_SIZE value
	 */
	public PixelFormat(int rgbSize, int alphaSize, int luminanceSize, int depthSize, int stencilSize, int samples) {
		if ( rgbSize < 0 )
			throw new IllegalArgumentException("Invalid RGB size specified: " + rgbSize);

		if ( alphaSize < 0 )
			throw new IllegalArgumentException("Invalid EGL_ALPHA_SIZE specified: " + alphaSize);

		if ( luminanceSize < 0 || (0 < luminanceSize && 0 < rgbSize) )
			throw new IllegalArgumentException("Invalid EGL_LUMINANCE_SIZE specified: " + luminanceSize);

		if ( depthSize < 0 )
			throw new IllegalArgumentException("Invalid EGL_DEPTH_SIZE specified: " + depthSize);

		if ( stencilSize < 0 )
			throw new IllegalArgumentException("Invalid EGL_STENCIL_SIZE specified: " + stencilSize);

		if ( samples < 0 )
			throw new IllegalArgumentException("Invalid EGL_SAMPLES specified: " + samples);

		if ( 0 < rgbSize ) {
			setAttrib(RED_SIZE, rgbSize);
			setAttrib(GREEN_SIZE, rgbSize);
			setAttrib(BLUE_SIZE, rgbSize);
		}
		setAttrib(ALPHA_SIZE, alphaSize);

		setAttrib(LUMINANCE_SIZE, luminanceSize);

		setAttrib(DEPTH_SIZE, depthSize);
		setAttrib(STENCIL_SIZE, stencilSize);

		setAttrib(SAMPLES, samples);
		setAttrib(SAMPLE_BUFFERS, samples == 0 ? 0 : 1);
	}

	/**
	 * Creates a new PixelFormat that is a copy of the specified PixelFormat.
	 *
	 * @param pf the source PixelFormat
	 */
	private PixelFormat(final PixelFormat pf) {
		config.clear();
		config.putAll(pf.config);
	}

	/**
	 * Sets the value of an attribute to the current configuration.
	 * If the value matches the default attribute value, the
	 * attribute will be removed from the configuration.
	 *
	 * @param attrib the attribute
	 * @param value  the new value
	 */
	private void setAttrib(final Attrib attrib, final int value) {
		if ( attrib.defaultValue == value )
			config.remove(attrib);
		else
			config.put(attrib, value);
	}

	/**
	 * Returns an IntBuffer that can be used to get/choose EGLConfigs.
	 * The contents of the IntBuffer will be the sum of the source
	 * LWJGL attributes and the user-defined attributes from this
	 * PixelFormat's configuration.
	 * <p/>
	 * The source LWJGL attributes should not contain the EGL_SURFACE_TYPE
	 * attirube, or any attributes that are handled by PixelFormat.
	 * <p/>
	 * Attributes that correspond to EGL extensions will be checked
	 * against the extensions supported in the specified EGLDisplay.
	 * Attributes that correspond to unsupported extensions will not
	 * be included in the final EGLConfig query.
	 *
	 * @param display      the EGL display from which the EGLConfig is going to be retrieved
	 * @param lwjglAttribs the LWJGL attributes
	 *
	 * @return the IntBuffer
	 */
	public IntBuffer getAttribBuffer(final EGLDisplay display, int surfaceType, final int[] lwjglAttribs) {
		// Create a copy of the configuration attributes
		Set<Attrib> keys = new HashSet<Attrib>(config.keySet());

		// Handle surface type bits
		if ( keys.contains(MULTISAMPLE_RESOLVE) ) {
			if ( display.getMajorVersion() == 1 && display.getMinorVersion() < 4 )
				keys.remove(MULTISAMPLE_RESOLVE);
			else if ( getAttrib(MULTISAMPLE_RESOLVE) == EGL_MULTISAMPLE_RESOLVE_BOX )
				surfaceType |= EGL_MULTISAMPLE_RESOLVE_BOX_BIT;
		}

		if ( keys.contains(SWAP_BEHAVIOR) ) {
			if ( display.getMajorVersion() == 1 && display.getMinorVersion() < 4 )
				keys.remove(SWAP_BEHAVIOR);
			else if ( getAttrib(SWAP_BEHAVIOR) == EGL_BUFFER_PRESERVED )
				surfaceType |= EGL_SWAP_BEHAVIOR_PRESERVED_BIT;
		}

		// Check NV_coverage_sample
		if ( keys.contains(COVERAGE_BUFFERS_NV) && !display.isExtensionSupported("EGL_NV_coverage_sample") ) {
			keys.remove(COVERAGE_BUFFERS_NV);
			keys.remove(COVERAGE_SAMPLES_NV);
		}

		// Check NV_depth_nonlinear
		if ( keys.contains(DEPTH_ENCODING_NONLINEAR_NV) && !display.isExtensionSupported("EGL_NV_depth_nonlinear") )
			keys.remove(DEPTH_ENCODING_NONLINEAR_NV);

		// Create IntBuffer and insert the attributes
		final IntBuffer attribs = BufferUtils.createIntBuffer(
			2                       // SURFACE_TYPE
			+ lwjglAttribs.length   // Source LWJGL attributes
			+ (keys.size() * 2)     // PixelFormat attributes
			+ 1                     // Termination
		);

		attribs.put(EGL_SURFACE_TYPE).put(surfaceType);
		attribs.put(lwjglAttribs);

		for ( Attrib key : keys ) {
			if ( !key.isSurfaceAttrib() )
				attribs.put(key.eglAttrib).put(config.get(key));
		}

		// Finish the attribute list
		attribs.put(EGL_NONE);
		attribs.flip();

		return attribs;
	}

	/**
	 * Returns true if the requested attribute matches the attribute in the specified EGL config.
	 *
	 * @param attrib the requested attribute
	 * @param config the EGL config
	 *
	 * @return true if the two attributes match
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	private boolean matches(final Attrib attrib, final EGLConfig config) throws LWJGLException {
		return getAttrib(attrib) == config.getAttribute(attrib.getEGLAttrib());
	}

	/**
	 * Returns true if the requested attribute matches the attribute in the specified EGL config.
	 * If the requested attribute is equal to 1, then it will match with any EGL config attribute
	 * that is greater than 0.
	 *
	 * @param attrib the requested attribute
	 * @param config the EGL config
	 *
	 * @return true if the two attributes match
	 *
	 * @throws LWJGLException if an EGL error occurs
	 */
	private boolean matchesNonZero(final Attrib attrib, final EGLConfig config) throws LWJGLException {
		final int reqValue = getAttrib(attrib);
		final int cfgValue = config.getAttribute(attrib.getEGLAttrib());

		return reqValue == cfgValue || (reqValue == 1 && cfgValue > 0);
	}

	/**
	 * Returns the EGL config from the specified array that best matches this PixelFormat.
	 *
	 * @param configs the EGL configs
	 *
	 * @return the best match
	 */
	public EGLConfig getBestMatch(final EGLConfig[] configs) throws LWJGLException {
		if ( configs == null || configs.length == 0 )
			throw new IllegalArgumentException("No EGLConfigs specified.");

		if ( configs.length == 1 )
			return configs[0];

		/*System.out.println("\nASKED FOR:");
		for ( Attrib attrib : config.keySet() ) {
			System.out.println("EGL_" + attrib.name() + ": " + getAttrib(attrib));
		}

		for ( EGLConfig config : configs ) {
			if ( config == null )
				continue;

			System.out.println("\n----");
			System.out.println(config);
		}*/

		for ( EGLConfig config : configs ) {
			if ( config == null )
				continue;

			if ( !(matches(ALPHA_SIZE, config) && matchesNonZero(DEPTH_SIZE, config) && matchesNonZero(STENCIL_SIZE, config)) )
				continue;

			final int luminance = getAttrib(LUMINANCE_SIZE);
			if ( 0 < luminance && !matches(LUMINANCE_SIZE, config) )
				continue;

			if ( luminance == 0 && !(matches(RED_SIZE, config) && matches(GREEN_SIZE, config) && matches(BLUE_SIZE, config)) )
				continue;

			if ( !(matches(SAMPLE_BUFFERS, config) && matches(SAMPLES, config)) )
				continue;

			// TODO: Add more? NV's Tegra SDK checks a hardcoded 5 value for COVERAGE_SAMPLES_NV (nv_main.c, line: 1823)

			return config;
		}

		// No match found, use the one recommended by the EGL implementation.
		LWJGLUtil.log("Could not find an exact EGLConfig match for the PixelFormat requested, using first returned.");
		return configs[0];
	}

	/**
	 * Applies this PixelFormat's surface attributes to the specified EGL surface.
	 *
	 * @param surface the EGL surface
	 */
	public void setSurfaceAttribs(final EGLSurface surface) throws LWJGLException {
		setSurfaceAttrib(surface, SWAP_BEHAVIOR);
		setSurfaceAttrib(surface, MULTISAMPLE_RESOLVE);
	}

	private void setSurfaceAttrib(final EGLSurface surface, final Attrib attrib) throws LWJGLException {
		final int value = getAttrib(attrib);
		if ( value != attrib.getDefaultValue() )
			surface.setAttribute(attrib.getEGLAttrib(), value);
	}

	/**
	 * Returns the value of the specified attribute.
	 *
	 * @param attrib the attribute to retrieve
	 *
	 * @return the attribute's value
	 */
	public int getAttrib(final Attrib attrib) {
		final Integer value = config.get(attrib);
		if ( value == null )
			return attrib.defaultValue;

		return value;
	}

	/* -----------------------------------------
					CORE ATTRIBUTES
	----------------------------------------- */

	/**
	 * Returns a new PixelFormat with the specified RGB sizes.
	 *
	 * @param rgb the new EGL_RED_SIZE, EGL_GREEN_SIZE and EGL_BLUE_SIZE
	 *
	 * @return the new PixelFormat
	 *
	 * @see #withRGBSize(int, int, int)
	 */
	public PixelFormat withRGBSize(final int rgb) {
		return withRGBSize(rgb, rgb, rgb);
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_RED_SIZE, EGL_GREEN_SIZE and EGL_BLUE_SIZE.
	 * All 3 values must be greater than or equal to 0. If any of the 3 values is greater than 0,
	 * the luminanceSize will be set to 0.
	 *
	 * @param r the new EGL_RED_SIZE
	 * @param g the new EGL_GREEN_SIZE
	 * @param b the new EGL_BLUE_SIZE
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withRGBSize(final int r, final int g, final int b) {
		if ( r < 0 || g < 0 || b < 0 )
			throw new IllegalArgumentException("Invalid RGB sizes specified: " + r + ", " + g + ", " + b);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(RED_SIZE, r);
		pf.setAttrib(GREEN_SIZE, g);
		pf.setAttrib(BLUE_SIZE, b);
		if ( 0 < r || 0 < g || 0 < b )
			pf.setAttrib(LUMINANCE_SIZE, 0);
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_ALPHA_SIZE.
	 * The alphaSize value must be greater than or equal to 0.
	 *
	 * @param alphaSize the new EGL_ALPHA_SIZE
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withAlphaSize(final int alphaSize) {
		if ( alphaSize < 0 )
			throw new IllegalArgumentException("Invalid EGL_ALPHA_SIZE specified: " + alphaSize);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(ALPHA_SIZE, alphaSize);
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_LUMINANCE_SIZE.
	 * The luminanceSize value must be greater than or equal to 0. If
	 * luminanceSize is greater than 0, the RGB sizes will be set to 0.
	 *
	 * @param luminanceSize the new EGL_LUMINANCE_SIZE
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withLuminanceSize(final int luminanceSize) {
		if ( luminanceSize < 0 )
			throw new IllegalArgumentException("Invalid EGL_LUMINANCE_SIZE specified: " + luminanceSize);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(LUMINANCE_SIZE, luminanceSize);
		if ( 0 < luminanceSize ) {
			pf.setAttrib(RED_SIZE, 0);
			pf.setAttrib(GREEN_SIZE, 0);
			pf.setAttrib(BLUE_SIZE, 0);
		}
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_DEPTH_SIZE.
	 * The depthSize value must be greater than or equal to 0.
	 * Values greater than 24 are not recommended.
	 *
	 * @param depthSize the new EGL_DEPTH_SIZE
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withDepthSize(final int depthSize) {
		if ( depthSize < 0 )
			throw new IllegalArgumentException("Invalid EGL_DEPTH_SIZE specified: " + depthSize);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(DEPTH_SIZE, depthSize);
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_STENCIL_SIZE.
	 * The stencilSize value must be greater than or equal to 0.
	 * Values greater than 8 are not recommended.
	 *
	 * @param stencilSize the new EGL_STENCIL_SIZE
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withStencilSize(final int stencilSize) {
		if ( stencilSize < 0 )
			throw new IllegalArgumentException("Invalid EGL_STENCIL_SIZE specified: " + stencilSize);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(STENCIL_SIZE, stencilSize);
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_MIN_SWAP_INTERVAL.
	 * The minSwapInterval value must be between 0 and this PixelFormat's EGL_MAX_SWAP_INTERVAL.
	 *
	 * @param minSwapInterval the new EGL_MIN_SWAP_INTERVAL value
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withMinSwapInterval(final int minSwapInterval) {
		final int maxSwapInterval = getAttrib(MAX_SWAP_INTERVAL);
		if ( minSwapInterval != EGL_DONT_CARE && (minSwapInterval < 0 || (maxSwapInterval != EGL_DONT_CARE && maxSwapInterval < minSwapInterval)) )
			throw new IllegalArgumentException("Invalid EGL_MIN_SWAP_INTERVAL specified: " + minSwapInterval);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(MIN_SWAP_INTERVAL, minSwapInterval);
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_MAX_SWAP_INTERVAL.
	 * The maxSwapInterval value must be greater than or equal to this PixelFormat's EGL_MIN_SWAP_INTERVAL.
	 *
	 * @param maxSwapInterval the new EGL_MAX_SWAP_INTERVAL value
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withMaxSwapInterval(final int maxSwapInterval) {
		if ( maxSwapInterval < getAttrib(MIN_SWAP_INTERVAL) )
			throw new IllegalArgumentException("Invalid EGL_MAX_SWAP_INTERVAL specified: " + maxSwapInterval);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(MAX_SWAP_INTERVAL, maxSwapInterval);
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified number of EGL_SAMPLES.
	 * The samples value must be either 0 or greater than or equal to 2. The related
	 * EGL_SAMPLE_BUFFERS value will become 0 if samples is 0, or 1 if samples
	 * is greater than or equal to 2.
	 *
	 * @param samples the new EGL_SAMPLES value
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withSamples(final int samples) {
		if ( samples != 0 && samples < 2 )
			throw new IllegalArgumentException("Invalid number of EGL_SAMPLES specified: " + samples);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(SAMPLES, samples);
		pf.setAttrib(SAMPLE_BUFFERS, samples == 0 ? 0 : 1);
		return pf;
	}

	private static int maxValue(final int bits) {
		return (2 << bits) - 1;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_TRANSPARENT_TYPE and
	 * the specified transparent RGB values. The transparentType must be
	 * either EGL_NONE or EGL_TRANSPARENT_RGB. When it is EGL_NONE, the RGB
	 * values are set to zero and ignored. When it is EGL_TRANSPARENT_RGB,
	 * the RGB values must be between 0 and 2^rgbSize - 1.
	 *
	 * @param transparentType the new EGL_TRANSPARENT_TYPE value
	 * @param r               the new EGL_TRANSPARENT_RED_VALUE
	 * @param g               the new EGL_TRANSPARENT_GREEN_VALUE
	 * @param b               the new EGL_TRANSPARENT_BLUE_VALUE
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withTransparentType(final int transparentType, int r, int g, int b) {
		if ( transparentType == EGL_TRANSPARENT_RGB ) {
			final int redSize = getAttrib(RED_SIZE);
			final int greenSize = getAttrib(GREEN_SIZE);
			final int blueSize = getAttrib(BLUE_SIZE);
			if ( r < 0 || (0 < redSize && maxValue(redSize) < r) )
				throw new IllegalArgumentException("Invalid EGL_TRANSPARENT_RED_VALUE specified: " + r);

			if ( r < 0 || (0 < greenSize && maxValue(greenSize) < g) )
				throw new IllegalArgumentException("Invalid EGL_TRANSPARENT_GREEN_VALUE specified: " + g);

			if ( r < 0 || (0 < blueSize && maxValue(blueSize) < b) )
				throw new IllegalArgumentException("Invalid EGL_TRANSPARENT_BLUE_VALUE specified: " + b);
		} else if ( transparentType != EGL_NONE )
			throw new IllegalArgumentException("Invalid EGL_TRANSPARENT_TYPE specified: " + transparentType);
		else
			r = g = b = EGL_DONT_CARE;

		final PixelFormat pf = new PixelFormat(this);

		pf.setAttrib(TRANSPARENT_TYPE, transparentType);

		pf.setAttrib(TRANSPARENT_RED_VALUE, r);
		pf.setAttrib(TRANSPARENT_GREEN_VALUE, g);
		pf.setAttrib(TRANSPARENT_BLUE_VALUE, b);

		return pf;
	}

	/* -----------------------------------------
				SURFACE ATTRIBUTES
	----------------------------------------- */

	/**
	 * Returns a new PixelFormat with the specified EGL_MULTISAMPLE_RESOLVE value.
	 * Valid values for multisampleResolve are EGL_MULTISAMPLE_RESOLVE_DEFAULT
	 * and EGL_MULTISAMPLE_RESOLVE_BOX.
	 * <p/>
	 * An IllegalStateException will be thrown if EGL_SAMPLES has not been previously defined
	 * to be greater than or equal to 2.
	 *
	 * @param multisampleResolve the new EGL_MULTISAMPLE_RESOLVE value
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withMultisampleResolve(final int multisampleResolve) {
		if ( multisampleResolve != EGL_MULTISAMPLE_RESOLVE_DEFAULT && multisampleResolve != EGL_MULTISAMPLE_RESOLVE_BOX )
			throw new IllegalArgumentException("Invalid EGL_MULTISAMPLE_RESOLVE value specified: " + multisampleResolve);

		if ( getAttrib(SAMPLE_BUFFERS) == 0 )
			throw new IllegalStateException("An EGL_MULTISAMPLE_RESOLVE value cannot be specified unless EGL_SAMPLE_BUFFERS is 1.");

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(MULTISAMPLE_RESOLVE, multisampleResolve);
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_SWAP_BEHAVIOR value.
	 * Valid values for swapBehavior are EGL_DONT_CARE, EGL_BUFFER_PRESERVED
	 * and EGL_BUFFER_DESTROYED.
	 *
	 * @param swapBehavior the new EGL_SWAP_BEHAVIOR value
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withSwapBehavior(final int swapBehavior) {
		switch ( swapBehavior ) {
			case EGL_DONT_CARE:
			case EGL_BUFFER_PRESERVED:
			case EGL_BUFFER_DESTROYED:
				break;
			default:
				throw new IllegalArgumentException("Invalid EGL_SWAP_BEHAVIOR value specified: " + swapBehavior);
		}

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(SWAP_BEHAVIOR, swapBehavior);
		return pf;
	}

	/* -----------------------------------------
				EXTENSION ATTRIBUTES
	----------------------------------------- */

	/**
	 * Returns a new PixelFormat with the specified number of EGL_COVERAGE_SAMPLES_NV.
	 * The samples value must be greater than or equal to 0. The related
	 * EGL_COVERAGE_BUFFERS_NV value will become 0 if samples is 0, or 1 if samples
	 * is greater than 0.
	 *
	 * @param samples the new EGL_SAMPLES value
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withCoverageSamplesNV(final int samples) {
		if ( samples < 0 )
			throw new IllegalArgumentException("Invalid number of EGL_COVERAGE_SAMPLES_NV specified: " + samples);

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(COVERAGE_SAMPLES_NV, samples);
		pf.setAttrib(COVERAGE_BUFFERS_NV, samples == 0 ? 0 : 1);
		return pf;
	}

	/**
	 * Returns a new PixelFormat with the specified EGL_DEPTH_ENCODING_NONLINEAR_NV.
	 * Valid values for depthEncoding are EGL_DONT_CARE, EGL_DEPTH_ENCODING_NONE_NV
	 * and EGL_DEPTH_ENCODING_NONLINEAR_NV.
	 *
	 * @param depthEncoding the new EGL_DEPTH_ENCODING_NONLINEAR_NV value
	 *
	 * @return the new PixelFormat
	 */
	public PixelFormat withDepthEncodingNonlinearNV(final int depthEncoding) {
		switch ( depthEncoding ) {
			case EGL_DONT_CARE:
			case EGL_DEPTH_ENCODING_NONE_NV:
			case EGL_DEPTH_ENCODING_NONLINEAR_NV:
				break;
			default:
				throw new IllegalArgumentException("Invalid EGL_DEPTH_ENCODING_NONLINEAR_NV value specified: " + depthEncoding);
		}

		final PixelFormat pf = new PixelFormat(this);
		pf.setAttrib(DEPTH_ENCODING_NONLINEAR_NV, depthEncoding);
		return pf;
	}

}