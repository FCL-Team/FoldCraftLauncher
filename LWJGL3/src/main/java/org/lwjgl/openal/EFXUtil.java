/*
 * Copyright (c) 2002-2010 LWJGL Project
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
package org.lwjgl.openal;

import static org.lwjgl.openal.AL10.AL_INVALID_OPERATION;
import static org.lwjgl.openal.AL10.AL_INVALID_VALUE;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.AL_OUT_OF_MEMORY;
import static org.lwjgl.openal.AL10.alGetError;
import static org.lwjgl.openal.EFX10.AL_EFFECT_AUTOWAH;
import static org.lwjgl.openal.EFX10.AL_EFFECT_CHORUS;
import static org.lwjgl.openal.EFX10.AL_EFFECT_COMPRESSOR;
import static org.lwjgl.openal.EFX10.AL_EFFECT_DISTORTION;
import static org.lwjgl.openal.EFX10.AL_EFFECT_EAXREVERB;
import static org.lwjgl.openal.EFX10.AL_EFFECT_ECHO;
import static org.lwjgl.openal.EFX10.AL_EFFECT_EQUALIZER;
import static org.lwjgl.openal.EFX10.AL_EFFECT_FLANGER;
import static org.lwjgl.openal.EFX10.AL_EFFECT_FREQUENCY_SHIFTER;
import static org.lwjgl.openal.EFX10.AL_EFFECT_NULL;
import static org.lwjgl.openal.EFX10.AL_EFFECT_PITCH_SHIFTER;
import static org.lwjgl.openal.EFX10.AL_EFFECT_REVERB;
import static org.lwjgl.openal.EFX10.AL_EFFECT_RING_MODULATOR;
import static org.lwjgl.openal.EFX10.AL_EFFECT_TYPE;
import static org.lwjgl.openal.EFX10.AL_EFFECT_VOCAL_MORPHER;
import static org.lwjgl.openal.EFX10.AL_FILTER_BANDPASS;
import static org.lwjgl.openal.EFX10.AL_FILTER_HIGHPASS;
import static org.lwjgl.openal.EFX10.AL_FILTER_LOWPASS;
import static org.lwjgl.openal.EFX10.AL_FILTER_NULL;
import static org.lwjgl.openal.EFX10.AL_FILTER_TYPE;
import static org.lwjgl.openal.EFX10.alDeleteEffects;
import static org.lwjgl.openal.EFX10.alDeleteFilters;
import static org.lwjgl.openal.EFX10.alEffecti;
import static org.lwjgl.openal.EFX10.alFilteri;
import static org.lwjgl.openal.EFX10.alGenEffects;
import static org.lwjgl.openal.EFX10.alGenFilters;

/**
 * Utility class for the OpenAL extension ALC_EXT_EFX. Provides functions to check for the extension
 * and support of various effects and filters.
 * <p>
 * Currently supports ALC_EXT_EFX version 1.0 effects and filters.
 *
 * @author Ciardhubh <ciardhubh[at]ciardhubh.de>
 * @version $Revision$
 * $Id$
 */
public final class EFXUtil {

    /** Constant for testSupportGeneric to check an effect. */
    private static final int EFFECT = 1111;
    /** Constant for testSupportGeneric to check a filter. */
    private static final int FILTER = 2222;

    /** Utility class, hidden contructor. */
    private EFXUtil() {
    }

    /**
     * Checks if OpenAL implementation is loaded and supports ALC_EXT_EFX.
     *
     * @return True if ALC_EXT_EFX is supported, false if not.
     * @throws OpenALException If OpenAL has not been created yet.
     */
    public static boolean isEfxSupported() {
        return ALC.createCapabilities(AL.alcDevice.device).ALC_EXT_EFX;
    }

    /**
     * Tests OpenAL to see whether the given effect type is supported. This is done by creating an
     * effect of the given type. If creation succeeds the effect is supported.
     *
     * @param effectType Type of effect whose support is to be tested, e.g. AL_EFFECT_REVERB.
     * @return True if it is supported, false if not.
     * @throws OpenALException If the request fails due to an AL_OUT_OF_MEMORY error or OpenAL has
     * not been created yet.
     * @throws IllegalArgumentException effectType is not a valid effect type.
     */
    public static boolean isEffectSupported(final int effectType) {
        // Make sure type is a real effect.
        switch (effectType) {
            case AL_EFFECT_NULL:
            case AL_EFFECT_EAXREVERB:
            case AL_EFFECT_REVERB:
            case AL_EFFECT_CHORUS:
            case AL_EFFECT_DISTORTION:
            case AL_EFFECT_ECHO:
            case AL_EFFECT_FLANGER:
            case AL_EFFECT_FREQUENCY_SHIFTER:
            case AL_EFFECT_VOCAL_MORPHER:
            case AL_EFFECT_PITCH_SHIFTER:
            case AL_EFFECT_RING_MODULATOR:
            case AL_EFFECT_AUTOWAH:
            case AL_EFFECT_COMPRESSOR:
            case AL_EFFECT_EQUALIZER:
                break;
            default:
                throw new IllegalArgumentException("Unknown or invalid effect type: " + effectType);
        }

        return testSupportGeneric(EFFECT, effectType);
    }

    /**
     * Tests OpenAL to see whether the given filter type is supported. This is done by creating a
     * filter of the given type. If creation succeeds the filter is supported.
     *
     * @param filterType Type of filter whose support is to be tested, e.g. AL_FILTER_LOWPASS.
     * @return True if it is supported, false if not.
     * @throws OpenALException If the request fails due to an AL_OUT_OF_MEMORY error or OpenAL has
     * not been created yet.
     * @throws IllegalArgumentException filterType is not a valid filter type.
     */
    public static boolean isFilterSupported(final int filterType) {
        // Make sure type is a real filter.
        switch (filterType) {
            case AL_FILTER_NULL:
            case AL_FILTER_LOWPASS:
            case AL_FILTER_HIGHPASS:
            case AL_FILTER_BANDPASS:
                break;
            default:
                throw new IllegalArgumentException("Unknown or invalid filter type: " + filterType);
        }

        return testSupportGeneric(FILTER, filterType);
    }

    /**
     * Generic test function to see if an EFX object supports a given kind of type. Works for
     * effects and filters.
     *
     * @param objectType Type of object to test. Must be either EFXUtil.EFFECT or EFXUtil.FILTER.
     * @param typeValue OpenAL type the object should be tested for support, e.g. AL_FILTER_LOWPASS
     * or AL_EFFECT_REVERB.
     * @return True if object supports typeValue, false else.
     */
    private static boolean testSupportGeneric(final int objectType, final int typeValue) {
        // Check for supported objectType.
        switch (objectType) {
            case EFFECT:
            case FILTER:
                break;
            default:
                throw new IllegalArgumentException("Invalid objectType: " + objectType);
        }

        boolean supported = false;
        if (isEfxSupported()) {

            // Try to create object in order to check AL's response.
            alGetError();
            int genError;
            int testObject = 0;
            try {
                switch (objectType) { // Create object based on type
                    case EFFECT:
                        testObject = alGenEffects();
                        break;
                    case FILTER:
                        testObject = alGenFilters();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid objectType: " + objectType);
                }
                genError = alGetError();
            } catch (final OpenALException debugBuildException) {
                // Hack because OpenALException hides the original error code (short of parsing the
                // error message String which would break if it gets changed).
                if (debugBuildException.getMessage().contains("AL_OUT_OF_MEMORY")) {
                    genError = AL_OUT_OF_MEMORY;
                } else {
                    genError = AL_INVALID_OPERATION;
                }
            }

            if (genError == AL_NO_ERROR) {
                // Successfully created, now try to set type.
                alGetError();
                int setError;
                try {
                    switch (objectType) { // Set based on object type
                        case EFFECT:
                            alEffecti(testObject, AL_EFFECT_TYPE, typeValue);
                            break;
                        case FILTER:
                            alFilteri(testObject, AL_FILTER_TYPE, typeValue);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid objectType: " + objectType);
                    }
                    setError = alGetError();
                } catch (final OpenALException debugBuildException) {
                    // Hack because OpenALException hides the original error code (short of parsing
                    // the error message String which would break when it gets changed).
                    setError = AL_INVALID_VALUE;
                }

                if (setError == AL_NO_ERROR) {
                    supported = true;
                }

                // Cleanup
                try {
                    switch (objectType) { // Set based on object type
                        case EFFECT:
                            alDeleteEffects(testObject);
                            break;
                        case FILTER:
                            alDeleteFilters(testObject);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid objectType: " + objectType);
                    }
                } catch (final OpenALException debugBuildException) {
                    // Don't care about cleanup errors.
                }

            } else if (genError == AL_OUT_OF_MEMORY) {
                throw new OpenALException(genError);
            }
        }

        return supported;
    }
}
