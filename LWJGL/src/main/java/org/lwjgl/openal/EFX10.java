/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.openal;

import org.lwjgl.*;
import java.nio.*;

/**
 *  Implementation of the OpenAL extension ALC_EXT_EFX (version 1.0). Contains necessary fields,
 *  methods and a range of supplementary fields containing minimum, maximum and default values of
 *  the former fields.
 *  <p>
 *  On top of regular functions defined in the ALC_EXT_EFX, there are also several convenience
 *  functions. Namely alGen... and alDelete... which do not take a Java buffer parameter and
 *  automatically create or delete a single object, without the overhead of using a buffer.
 *  <p>
 *  For comments and specification of functions and fields, refer to the "Effects Extension Guide"
 *  which is part of the OpenAL SDK and can be downloaded from:
 *  http://connect.creativelabs.com/openal/Downloads/Forms/AllItems.aspx
 * <p>
 *  @author Ciardhubh <ciardhubh[at]ciardhubh.de>
 *  @version $Revision$
 *  $Id$
 */
public final class EFX10 {

	public static final String ALC_EXT_EFX_NAME = "ALC_EXT_EFX";

	public static final int ALC_EFX_MAJOR_VERSION = 0x20001,
		ALC_EFX_MINOR_VERSION = 0x20002,
		ALC_MAX_AUXILIARY_SENDS = 0x20003,
		AL_METERS_PER_UNIT = 0x20004,
		AL_DIRECT_FILTER = 0x20005,
		AL_AUXILIARY_SEND_FILTER = 0x20006,
		AL_AIR_ABSORPTION_FACTOR = 0x20007,
		AL_ROOM_ROLLOFF_FACTOR = 0x20008,
		AL_CONE_OUTER_GAINHF = 0x20009,
		AL_DIRECT_FILTER_GAINHF_AUTO = 0x2000A,
		AL_AUXILIARY_SEND_FILTER_GAIN_AUTO = 0x2000B,
		AL_AUXILIARY_SEND_FILTER_GAINHF_AUTO = 0x2000C,
		AL_EFFECTSLOT_EFFECT = 0x1,
		AL_EFFECTSLOT_GAIN = 0x2,
		AL_EFFECTSLOT_AUXILIARY_SEND_AUTO = 0x3,
		AL_EFFECTSLOT_NULL = 0x0,
		AL_REVERB_DENSITY = 0x1,
		AL_REVERB_DIFFUSION = 0x2,
		AL_REVERB_GAIN = 0x3,
		AL_REVERB_GAINHF = 0x4,
		AL_REVERB_DECAY_TIME = 0x5,
		AL_REVERB_DECAY_HFRATIO = 0x6,
		AL_REVERB_REFLECTIONS_GAIN = 0x7,
		AL_REVERB_REFLECTIONS_DELAY = 0x8,
		AL_REVERB_LATE_REVERB_GAIN = 0x9,
		AL_REVERB_LATE_REVERB_DELAY = 0xA,
		AL_REVERB_AIR_ABSORPTION_GAINHF = 0xB,
		AL_REVERB_ROOM_ROLLOFF_FACTOR = 0xC,
		AL_REVERB_DECAY_HFLIMIT = 0xD,
		AL_EAXREVERB_DENSITY = 0x1,
		AL_EAXREVERB_DIFFUSION = 0x2,
		AL_EAXREVERB_GAIN = 0x3,
		AL_EAXREVERB_GAINHF = 0x4,
		AL_EAXREVERB_GAINLF = 0x5,
		AL_EAXREVERB_DECAY_TIME = 0x6,
		AL_EAXREVERB_DECAY_HFRATIO = 0x7,
		AL_EAXREVERB_DECAY_LFRATIO = 0x8,
		AL_EAXREVERB_REFLECTIONS_GAIN = 0x9,
		AL_EAXREVERB_REFLECTIONS_DELAY = 0xA,
		AL_EAXREVERB_REFLECTIONS_PAN = 0xB,
		AL_EAXREVERB_LATE_REVERB_GAIN = 0xC,
		AL_EAXREVERB_LATE_REVERB_DELAY = 0xD,
		AL_EAXREVERB_LATE_REVERB_PAN = 0xE,
		AL_EAXREVERB_ECHO_TIME = 0xF,
		AL_EAXREVERB_ECHO_DEPTH = 0x10,
		AL_EAXREVERB_MODULATION_TIME = 0x11,
		AL_EAXREVERB_MODULATION_DEPTH = 0x12,
		AL_EAXREVERB_AIR_ABSORPTION_GAINHF = 0x13,
		AL_EAXREVERB_HFREFERENCE = 0x14,
		AL_EAXREVERB_LFREFERENCE = 0x15,
		AL_EAXREVERB_ROOM_ROLLOFF_FACTOR = 0x16,
		AL_EAXREVERB_DECAY_HFLIMIT = 0x17,
		AL_CHORUS_WAVEFORM = 0x1,
		AL_CHORUS_PHASE = 0x2,
		AL_CHORUS_RATE = 0x3,
		AL_CHORUS_DEPTH = 0x4,
		AL_CHORUS_FEEDBACK = 0x5,
		AL_CHORUS_DELAY = 0x6,
		AL_DISTORTION_EDGE = 0x1,
		AL_DISTORTION_GAIN = 0x2,
		AL_DISTORTION_LOWPASS_CUTOFF = 0x3,
		AL_DISTORTION_EQCENTER = 0x4,
		AL_DISTORTION_EQBANDWIDTH = 0x5,
		AL_ECHO_DELAY = 0x1,
		AL_ECHO_LRDELAY = 0x2,
		AL_ECHO_DAMPING = 0x3,
		AL_ECHO_FEEDBACK = 0x4,
		AL_ECHO_SPREAD = 0x5,
		AL_FLANGER_WAVEFORM = 0x1,
		AL_FLANGER_PHASE = 0x2,
		AL_FLANGER_RATE = 0x3,
		AL_FLANGER_DEPTH = 0x4,
		AL_FLANGER_FEEDBACK = 0x5,
		AL_FLANGER_DELAY = 0x6,
		AL_FREQUENCY_SHIFTER_FREQUENCY = 0x1,
		AL_FREQUENCY_SHIFTER_LEFT_DIRECTION = 0x2,
		AL_FREQUENCY_SHIFTER_RIGHT_DIRECTION = 0x3,
		AL_VOCAL_MORPHER_PHONEMEA = 0x1,
		AL_VOCAL_MORPHER_PHONEMEA_COARSE_TUNING = 0x2,
		AL_VOCAL_MORPHER_PHONEMEB = 0x3,
		AL_VOCAL_MORPHER_PHONEMEB_COARSE_TUNING = 0x4,
		AL_VOCAL_MORPHER_WAVEFORM = 0x5,
		AL_VOCAL_MORPHER_RATE = 0x6,
		AL_PITCH_SHIFTER_COARSE_TUNE = 0x1,
		AL_PITCH_SHIFTER_FINE_TUNE = 0x2,
		AL_RING_MODULATOR_FREQUENCY = 0x1,
		AL_RING_MODULATOR_HIGHPASS_CUTOFF = 0x2,
		AL_RING_MODULATOR_WAVEFORM = 0x3,
		AL_AUTOWAH_ATTACK_TIME = 0x1,
		AL_AUTOWAH_RELEASE_TIME = 0x2,
		AL_AUTOWAH_RESONANCE = 0x3,
		AL_AUTOWAH_PEAK_GAIN = 0x4,
		AL_COMPRESSOR_ONOFF = 0x1,
		AL_EQUALIZER_LOW_GAIN = 0x1,
		AL_EQUALIZER_LOW_CUTOFF = 0x2,
		AL_EQUALIZER_MID1_GAIN = 0x3,
		AL_EQUALIZER_MID1_CENTER = 0x4,
		AL_EQUALIZER_MID1_WIDTH = 0x5,
		AL_EQUALIZER_MID2_GAIN = 0x6,
		AL_EQUALIZER_MID2_CENTER = 0x7,
		AL_EQUALIZER_MID2_WIDTH = 0x8,
		AL_EQUALIZER_HIGH_GAIN = 0x9,
		AL_EQUALIZER_HIGH_CUTOFF = 0xA,
		AL_EFFECT_FIRST_PARAMETER = 0x0,
		AL_EFFECT_LAST_PARAMETER = 0x8000,
		AL_EFFECT_TYPE = 0x8001,
		AL_EFFECT_NULL = 0x0,
		AL_EFFECT_REVERB = 0x1,
		AL_EFFECT_CHORUS = 0x2,
		AL_EFFECT_DISTORTION = 0x3,
		AL_EFFECT_ECHO = 0x4,
		AL_EFFECT_FLANGER = 0x5,
		AL_EFFECT_FREQUENCY_SHIFTER = 0x6,
		AL_EFFECT_VOCAL_MORPHER = 0x7,
		AL_EFFECT_PITCH_SHIFTER = 0x8,
		AL_EFFECT_RING_MODULATOR = 0x9,
		AL_EFFECT_AUTOWAH = 0xA,
		AL_EFFECT_COMPRESSOR = 0xB,
		AL_EFFECT_EQUALIZER = 0xC,
		AL_EFFECT_EAXREVERB = 0x8000,
		AL_LOWPASS_GAIN = 0x1,
		AL_LOWPASS_GAINHF = 0x2,
		AL_HIGHPASS_GAIN = 0x1,
		AL_HIGHPASS_GAINLF = 0x2,
		AL_BANDPASS_GAIN = 0x1,
		AL_BANDPASS_GAINLF = 0x2,
		AL_BANDPASS_GAINHF = 0x3,
		AL_FILTER_FIRST_PARAMETER = 0x0,
		AL_FILTER_LAST_PARAMETER = 0x8000,
		AL_FILTER_TYPE = 0x8001,
		AL_FILTER_NULL = 0x0,
		AL_FILTER_LOWPASS = 0x1,
		AL_FILTER_HIGHPASS = 0x2,
		AL_FILTER_BANDPASS = 0x3;

	public static final float AL_MIN_AIR_ABSORPTION_FACTOR = 0.0f,
		AL_MAX_AIR_ABSORPTION_FACTOR = 10.0f,
		AL_DEFAULT_AIR_ABSORPTION_FACTOR = 0.0f,
		AL_MIN_ROOM_ROLLOFF_FACTOR = 0.0f,
		AL_MAX_ROOM_ROLLOFF_FACTOR = 10.0f,
		AL_DEFAULT_ROOM_ROLLOFF_FACTOR = 0.0f,
		AL_MIN_CONE_OUTER_GAINHF = 0.0f,
		AL_MAX_CONE_OUTER_GAINHF = 1.0f,
		AL_DEFAULT_CONE_OUTER_GAINHF = 1.0f;

	public static final int AL_MIN_DIRECT_FILTER_GAINHF_AUTO = 0x0,
		AL_MAX_DIRECT_FILTER_GAINHF_AUTO = 0x1,
		AL_DEFAULT_DIRECT_FILTER_GAINHF_AUTO = 0x1,
		AL_MIN_AUXILIARY_SEND_FILTER_GAIN_AUTO = 0x0,
		AL_MAX_AUXILIARY_SEND_FILTER_GAIN_AUTO = 0x1,
		AL_DEFAULT_AUXILIARY_SEND_FILTER_GAIN_AUTO = 0x1,
		AL_MIN_AUXILIARY_SEND_FILTER_GAINHF_AUTO = 0x0,
		AL_MAX_AUXILIARY_SEND_FILTER_GAINHF_AUTO = 0x1,
		AL_DEFAULT_AUXILIARY_SEND_FILTER_GAINHF_AUTO = 0x1;

	public static final float AL_MIN_METERS_PER_UNIT = 1.4E-45f,
		AL_MAX_METERS_PER_UNIT = 3.4028235E38f,
		AL_DEFAULT_METERS_PER_UNIT = 1.0f,
		AL_REVERB_MIN_DENSITY = 0.0f,
		AL_REVERB_MAX_DENSITY = 1.0f,
		AL_REVERB_DEFAULT_DENSITY = 1.0f,
		AL_REVERB_MIN_DIFFUSION = 0.0f,
		AL_REVERB_MAX_DIFFUSION = 1.0f,
		AL_REVERB_DEFAULT_DIFFUSION = 1.0f,
		AL_REVERB_MIN_GAIN = 0.0f,
		AL_REVERB_MAX_GAIN = 1.0f,
		AL_REVERB_DEFAULT_GAIN = 0.32f,
		AL_REVERB_MIN_GAINHF = 0.0f,
		AL_REVERB_MAX_GAINHF = 1.0f,
		AL_REVERB_DEFAULT_GAINHF = 0.89f,
		AL_REVERB_MIN_DECAY_TIME = 0.1f,
		AL_REVERB_MAX_DECAY_TIME = 20.0f,
		AL_REVERB_DEFAULT_DECAY_TIME = 1.49f,
		AL_REVERB_MIN_DECAY_HFRATIO = 0.1f,
		AL_REVERB_MAX_DECAY_HFRATIO = 2.0f,
		AL_REVERB_DEFAULT_DECAY_HFRATIO = 0.83f,
		AL_REVERB_MIN_REFLECTIONS_GAIN = 0.0f,
		AL_REVERB_MAX_REFLECTIONS_GAIN = 3.16f,
		AL_REVERB_DEFAULT_REFLECTIONS_GAIN = 0.05f,
		AL_REVERB_MIN_REFLECTIONS_DELAY = 0.0f,
		AL_REVERB_MAX_REFLECTIONS_DELAY = 0.3f,
		AL_REVERB_DEFAULT_REFLECTIONS_DELAY = 0.007f,
		AL_REVERB_MIN_LATE_REVERB_GAIN = 0.0f,
		AL_REVERB_MAX_LATE_REVERB_GAIN = 10.0f,
		AL_REVERB_DEFAULT_LATE_REVERB_GAIN = 1.26f,
		AL_REVERB_MIN_LATE_REVERB_DELAY = 0.0f,
		AL_REVERB_MAX_LATE_REVERB_DELAY = 0.1f,
		AL_REVERB_DEFAULT_LATE_REVERB_DELAY = 0.011f,
		AL_REVERB_MIN_AIR_ABSORPTION_GAINHF = 0.892f,
		AL_REVERB_MAX_AIR_ABSORPTION_GAINHF = 1.0f,
		AL_REVERB_DEFAULT_AIR_ABSORPTION_GAINHF = 0.994f,
		AL_REVERB_MIN_ROOM_ROLLOFF_FACTOR = 0.0f,
		AL_REVERB_MAX_ROOM_ROLLOFF_FACTOR = 10.0f,
		AL_REVERB_DEFAULT_ROOM_ROLLOFF_FACTOR = 0.0f;

	public static final int AL_REVERB_MIN_DECAY_HFLIMIT = 0x0,
		AL_REVERB_MAX_DECAY_HFLIMIT = 0x1,
		AL_REVERB_DEFAULT_DECAY_HFLIMIT = 0x1;

	public static final float AL_EAXREVERB_MIN_DENSITY = 0.0f,
		AL_EAXREVERB_MAX_DENSITY = 1.0f,
		AL_EAXREVERB_DEFAULT_DENSITY = 1.0f,
		AL_EAXREVERB_MIN_DIFFUSION = 0.0f,
		AL_EAXREVERB_MAX_DIFFUSION = 1.0f,
		AL_EAXREVERB_DEFAULT_DIFFUSION = 1.0f,
		AL_EAXREVERB_MIN_GAIN = 0.0f,
		AL_EAXREVERB_MAX_GAIN = 1.0f,
		AL_EAXREVERB_DEFAULT_GAIN = 0.32f,
		AL_EAXREVERB_MIN_GAINHF = 0.0f,
		AL_EAXREVERB_MAX_GAINHF = 1.0f,
		AL_EAXREVERB_DEFAULT_GAINHF = 0.89f,
		AL_EAXREVERB_MIN_GAINLF = 0.0f,
		AL_EAXREVERB_MAX_GAINLF = 1.0f,
		AL_EAXREVERB_DEFAULT_GAINLF = 1.0f,
		AL_EAXREVERB_MIN_DECAY_TIME = 0.1f,
		AL_EAXREVERB_MAX_DECAY_TIME = 20.0f,
		AL_EAXREVERB_DEFAULT_DECAY_TIME = 1.49f,
		AL_EAXREVERB_MIN_DECAY_HFRATIO = 0.1f,
		AL_EAXREVERB_MAX_DECAY_HFRATIO = 2.0f,
		AL_EAXREVERB_DEFAULT_DECAY_HFRATIO = 0.83f,
		AL_EAXREVERB_MIN_DECAY_LFRATIO = 0.1f,
		AL_EAXREVERB_MAX_DECAY_LFRATIO = 2.0f,
		AL_EAXREVERB_DEFAULT_DECAY_LFRATIO = 1.0f,
		AL_EAXREVERB_MIN_REFLECTIONS_GAIN = 0.0f,
		AL_EAXREVERB_MAX_REFLECTIONS_GAIN = 3.16f,
		AL_EAXREVERB_DEFAULT_REFLECTIONS_GAIN = 0.05f,
		AL_EAXREVERB_MIN_REFLECTIONS_DELAY = 0.0f,
		AL_EAXREVERB_MAX_REFLECTIONS_DELAY = 0.3f,
		AL_EAXREVERB_DEFAULT_REFLECTIONS_DELAY = 0.007f,
		AL_EAXREVERB_DEFAULT_REFLECTIONS_PAN_XYZ = 0.0f,
		AL_EAXREVERB_MIN_LATE_REVERB_GAIN = 0.0f,
		AL_EAXREVERB_MAX_LATE_REVERB_GAIN = 10.0f,
		AL_EAXREVERB_DEFAULT_LATE_REVERB_GAIN = 1.26f,
		AL_EAXREVERB_MIN_LATE_REVERB_DELAY = 0.0f,
		AL_EAXREVERB_MAX_LATE_REVERB_DELAY = 0.1f,
		AL_EAXREVERB_DEFAULT_LATE_REVERB_DELAY = 0.011f,
		AL_EAXREVERB_DEFAULT_LATE_REVERB_PAN_XYZ = 0.0f,
		AL_EAXREVERB_MIN_ECHO_TIME = 0.075f,
		AL_EAXREVERB_MAX_ECHO_TIME = 0.25f,
		AL_EAXREVERB_DEFAULT_ECHO_TIME = 0.25f,
		AL_EAXREVERB_MIN_ECHO_DEPTH = 0.0f,
		AL_EAXREVERB_MAX_ECHO_DEPTH = 1.0f,
		AL_EAXREVERB_DEFAULT_ECHO_DEPTH = 0.0f,
		AL_EAXREVERB_MIN_MODULATION_TIME = 0.04f,
		AL_EAXREVERB_MAX_MODULATION_TIME = 4.0f,
		AL_EAXREVERB_DEFAULT_MODULATION_TIME = 0.25f,
		AL_EAXREVERB_MIN_MODULATION_DEPTH = 0.0f,
		AL_EAXREVERB_MAX_MODULATION_DEPTH = 1.0f,
		AL_EAXREVERB_DEFAULT_MODULATION_DEPTH = 0.0f,
		AL_EAXREVERB_MIN_AIR_ABSORPTION_GAINHF = 0.892f,
		AL_EAXREVERB_MAX_AIR_ABSORPTION_GAINHF = 1.0f,
		AL_EAXREVERB_DEFAULT_AIR_ABSORPTION_GAINHF = 0.994f,
		AL_EAXREVERB_MIN_HFREFERENCE = 1000.0f,
		AL_EAXREVERB_MAX_HFREFERENCE = 20000.0f,
		AL_EAXREVERB_DEFAULT_HFREFERENCE = 5000.0f,
		AL_EAXREVERB_MIN_LFREFERENCE = 20.0f,
		AL_EAXREVERB_MAX_LFREFERENCE = 1000.0f,
		AL_EAXREVERB_DEFAULT_LFREFERENCE = 250.0f,
		AL_EAXREVERB_MIN_ROOM_ROLLOFF_FACTOR = 0.0f,
		AL_EAXREVERB_MAX_ROOM_ROLLOFF_FACTOR = 10.0f,
		AL_EAXREVERB_DEFAULT_ROOM_ROLLOFF_FACTOR = 0.0f;

	public static final int AL_EAXREVERB_MIN_DECAY_HFLIMIT = 0x0,
		AL_EAXREVERB_MAX_DECAY_HFLIMIT = 0x1,
		AL_EAXREVERB_DEFAULT_DECAY_HFLIMIT = 0x1,
		AL_CHORUS_WAVEFORM_SINUSOID = 0x0,
		AL_CHORUS_WAVEFORM_TRIANGLE = 0x1,
		AL_CHORUS_MIN_WAVEFORM = 0x0,
		AL_CHORUS_MAX_WAVEFORM = 0x1,
		AL_CHORUS_DEFAULT_WAVEFORM = 0x1,
		AL_CHORUS_MIN_PHASE = 0xFFFFFF4C,
		AL_CHORUS_MAX_PHASE = 0xB4,
		AL_CHORUS_DEFAULT_PHASE = 0x5A;

	public static final float AL_CHORUS_MIN_RATE = 0.0f,
		AL_CHORUS_MAX_RATE = 10.0f,
		AL_CHORUS_DEFAULT_RATE = 1.1f,
		AL_CHORUS_MIN_DEPTH = 0.0f,
		AL_CHORUS_MAX_DEPTH = 1.0f,
		AL_CHORUS_DEFAULT_DEPTH = 0.1f,
		AL_CHORUS_MIN_FEEDBACK = -1.0f,
		AL_CHORUS_MAX_FEEDBACK = 1.0f,
		AL_CHORUS_DEFAULT_FEEDBACK = 0.25f,
		AL_CHORUS_MIN_DELAY = 0.0f,
		AL_CHORUS_MAX_DELAY = 0.016f,
		AL_CHORUS_DEFAULT_DELAY = 0.016f,
		AL_DISTORTION_MIN_EDGE = 0.0f,
		AL_DISTORTION_MAX_EDGE = 1.0f,
		AL_DISTORTION_DEFAULT_EDGE = 0.2f,
		AL_DISTORTION_MIN_GAIN = 0.01f,
		AL_DISTORTION_MAX_GAIN = 1.0f,
		AL_DISTORTION_DEFAULT_GAIN = 0.05f,
		AL_DISTORTION_MIN_LOWPASS_CUTOFF = 80.0f,
		AL_DISTORTION_MAX_LOWPASS_CUTOFF = 24000.0f,
		AL_DISTORTION_DEFAULT_LOWPASS_CUTOFF = 8000.0f,
		AL_DISTORTION_MIN_EQCENTER = 80.0f,
		AL_DISTORTION_MAX_EQCENTER = 24000.0f,
		AL_DISTORTION_DEFAULT_EQCENTER = 3600.0f,
		AL_DISTORTION_MIN_EQBANDWIDTH = 80.0f,
		AL_DISTORTION_MAX_EQBANDWIDTH = 24000.0f,
		AL_DISTORTION_DEFAULT_EQBANDWIDTH = 3600.0f,
		AL_ECHO_MIN_DELAY = 0.0f,
		AL_ECHO_MAX_DELAY = 0.207f,
		AL_ECHO_DEFAULT_DELAY = 0.1f,
		AL_ECHO_MIN_LRDELAY = 0.0f,
		AL_ECHO_MAX_LRDELAY = 0.404f,
		AL_ECHO_DEFAULT_LRDELAY = 0.1f,
		AL_ECHO_MIN_DAMPING = 0.0f,
		AL_ECHO_MAX_DAMPING = 0.99f,
		AL_ECHO_DEFAULT_DAMPING = 0.5f,
		AL_ECHO_MIN_FEEDBACK = 0.0f,
		AL_ECHO_MAX_FEEDBACK = 1.0f,
		AL_ECHO_DEFAULT_FEEDBACK = 0.5f,
		AL_ECHO_MIN_SPREAD = -1.0f,
		AL_ECHO_MAX_SPREAD = 1.0f,
		AL_ECHO_DEFAULT_SPREAD = -1.0f;

	public static final int AL_FLANGER_WAVEFORM_SINUSOID = 0x0,
		AL_FLANGER_WAVEFORM_TRIANGLE = 0x1,
		AL_FLANGER_MIN_WAVEFORM = 0x0,
		AL_FLANGER_MAX_WAVEFORM = 0x1,
		AL_FLANGER_DEFAULT_WAVEFORM = 0x1,
		AL_FLANGER_MIN_PHASE = 0xFFFFFF4C,
		AL_FLANGER_MAX_PHASE = 0xB4,
		AL_FLANGER_DEFAULT_PHASE = 0x0;

	public static final float AL_FLANGER_MIN_RATE = 0.0f,
		AL_FLANGER_MAX_RATE = 10.0f,
		AL_FLANGER_DEFAULT_RATE = 0.27f,
		AL_FLANGER_MIN_DEPTH = 0.0f,
		AL_FLANGER_MAX_DEPTH = 1.0f,
		AL_FLANGER_DEFAULT_DEPTH = 1.0f,
		AL_FLANGER_MIN_FEEDBACK = -1.0f,
		AL_FLANGER_MAX_FEEDBACK = 1.0f,
		AL_FLANGER_DEFAULT_FEEDBACK = -0.5f,
		AL_FLANGER_MIN_DELAY = 0.0f,
		AL_FLANGER_MAX_DELAY = 0.004f,
		AL_FLANGER_DEFAULT_DELAY = 0.002f,
		AL_FREQUENCY_SHIFTER_MIN_FREQUENCY = 0.0f,
		AL_FREQUENCY_SHIFTER_MAX_FREQUENCY = 24000.0f,
		AL_FREQUENCY_SHIFTER_DEFAULT_FREQUENCY = 0.0f;

	public static final int AL_FREQUENCY_SHIFTER_MIN_LEFT_DIRECTION = 0x0,
		AL_FREQUENCY_SHIFTER_MAX_LEFT_DIRECTION = 0x2,
		AL_FREQUENCY_SHIFTER_DEFAULT_LEFT_DIRECTION = 0x0,
		AL_FREQUENCY_SHIFTER_DIRECTION_DOWN = 0x0,
		AL_FREQUENCY_SHIFTER_DIRECTION_UP = 0x1,
		AL_FREQUENCY_SHIFTER_DIRECTION_OFF = 0x2,
		AL_FREQUENCY_SHIFTER_MIN_RIGHT_DIRECTION = 0x0,
		AL_FREQUENCY_SHIFTER_MAX_RIGHT_DIRECTION = 0x2,
		AL_FREQUENCY_SHIFTER_DEFAULT_RIGHT_DIRECTION = 0x0,
		AL_VOCAL_MORPHER_MIN_PHONEMEA = 0x0,
		AL_VOCAL_MORPHER_MAX_PHONEMEA = 0x1D,
		AL_VOCAL_MORPHER_DEFAULT_PHONEMEA = 0x0,
		AL_VOCAL_MORPHER_MIN_PHONEMEA_COARSE_TUNING = 0xFFFFFFE8,
		AL_VOCAL_MORPHER_MAX_PHONEMEA_COARSE_TUNING = 0x18,
		AL_VOCAL_MORPHER_DEFAULT_PHONEMEA_COARSE_TUNING = 0x0,
		AL_VOCAL_MORPHER_MIN_PHONEMEB = 0x0,
		AL_VOCAL_MORPHER_MAX_PHONEMEB = 0x1D,
		AL_VOCAL_MORPHER_DEFAULT_PHONEMEB = 0xA,
		AL_VOCAL_MORPHER_MIN_PHONEMEB_COARSE_TUNING = 0xFFFFFFE8,
		AL_VOCAL_MORPHER_MAX_PHONEMEB_COARSE_TUNING = 0x18,
		AL_VOCAL_MORPHER_DEFAULT_PHONEMEB_COARSE_TUNING = 0x0,
		AL_VOCAL_MORPHER_PHONEME_A = 0x0,
		AL_VOCAL_MORPHER_PHONEME_E = 0x1,
		AL_VOCAL_MORPHER_PHONEME_I = 0x2,
		AL_VOCAL_MORPHER_PHONEME_O = 0x3,
		AL_VOCAL_MORPHER_PHONEME_U = 0x4,
		AL_VOCAL_MORPHER_PHONEME_AA = 0x5,
		AL_VOCAL_MORPHER_PHONEME_AE = 0x6,
		AL_VOCAL_MORPHER_PHONEME_AH = 0x7,
		AL_VOCAL_MORPHER_PHONEME_AO = 0x8,
		AL_VOCAL_MORPHER_PHONEME_EH = 0x9,
		AL_VOCAL_MORPHER_PHONEME_ER = 0xA,
		AL_VOCAL_MORPHER_PHONEME_IH = 0xB,
		AL_VOCAL_MORPHER_PHONEME_IY = 0xC,
		AL_VOCAL_MORPHER_PHONEME_UH = 0xD,
		AL_VOCAL_MORPHER_PHONEME_UW = 0xE,
		AL_VOCAL_MORPHER_PHONEME_B = 0xF,
		AL_VOCAL_MORPHER_PHONEME_D = 0x10,
		AL_VOCAL_MORPHER_PHONEME_F = 0x11,
		AL_VOCAL_MORPHER_PHONEME_G = 0x12,
		AL_VOCAL_MORPHER_PHONEME_J = 0x13,
		AL_VOCAL_MORPHER_PHONEME_K = 0x14,
		AL_VOCAL_MORPHER_PHONEME_L = 0x15,
		AL_VOCAL_MORPHER_PHONEME_M = 0x16,
		AL_VOCAL_MORPHER_PHONEME_N = 0x17,
		AL_VOCAL_MORPHER_PHONEME_P = 0x18,
		AL_VOCAL_MORPHER_PHONEME_R = 0x19,
		AL_VOCAL_MORPHER_PHONEME_S = 0x1A,
		AL_VOCAL_MORPHER_PHONEME_T = 0x1B,
		AL_VOCAL_MORPHER_PHONEME_V = 0x1C,
		AL_VOCAL_MORPHER_PHONEME_Z = 0x1D,
		AL_VOCAL_MORPHER_WAVEFORM_SINUSOID = 0x0,
		AL_VOCAL_MORPHER_WAVEFORM_TRIANGLE = 0x1,
		AL_VOCAL_MORPHER_WAVEFORM_SAWTOOTH = 0x2,
		AL_VOCAL_MORPHER_MIN_WAVEFORM = 0x0,
		AL_VOCAL_MORPHER_MAX_WAVEFORM = 0x2,
		AL_VOCAL_MORPHER_DEFAULT_WAVEFORM = 0x0;

	public static final float AL_VOCAL_MORPHER_MIN_RATE = 0.0f,
		AL_VOCAL_MORPHER_MAX_RATE = 10.0f,
		AL_VOCAL_MORPHER_DEFAULT_RATE = 1.41f;

	public static final int AL_PITCH_SHIFTER_MIN_COARSE_TUNE = 0xFFFFFFF4,
		AL_PITCH_SHIFTER_MAX_COARSE_TUNE = 0xC,
		AL_PITCH_SHIFTER_DEFAULT_COARSE_TUNE = 0xC,
		AL_PITCH_SHIFTER_MIN_FINE_TUNE = 0xFFFFFFCE,
		AL_PITCH_SHIFTER_MAX_FINE_TUNE = 0x32,
		AL_PITCH_SHIFTER_DEFAULT_FINE_TUNE = 0x0;

	public static final float AL_RING_MODULATOR_MIN_FREQUENCY = 0.0f,
		AL_RING_MODULATOR_MAX_FREQUENCY = 8000.0f,
		AL_RING_MODULATOR_DEFAULT_FREQUENCY = 440.0f,
		AL_RING_MODULATOR_MIN_HIGHPASS_CUTOFF = 0.0f,
		AL_RING_MODULATOR_MAX_HIGHPASS_CUTOFF = 24000.0f,
		AL_RING_MODULATOR_DEFAULT_HIGHPASS_CUTOFF = 800.0f;

	public static final int AL_RING_MODULATOR_SINUSOID = 0x0,
		AL_RING_MODULATOR_SAWTOOTH = 0x1,
		AL_RING_MODULATOR_SQUARE = 0x2,
		AL_RING_MODULATOR_MIN_WAVEFORM = 0x0,
		AL_RING_MODULATOR_MAX_WAVEFORM = 0x2,
		AL_RING_MODULATOR_DEFAULT_WAVEFORM = 0x0;

	public static final float AL_AUTOWAH_MIN_ATTACK_TIME = 1.0E-4f,
		AL_AUTOWAH_MAX_ATTACK_TIME = 1.0f,
		AL_AUTOWAH_DEFAULT_ATTACK_TIME = 0.06f,
		AL_AUTOWAH_MIN_RELEASE_TIME = 1.0E-4f,
		AL_AUTOWAH_MAX_RELEASE_TIME = 1.0f,
		AL_AUTOWAH_DEFAULT_RELEASE_TIME = 0.06f,
		AL_AUTOWAH_MIN_RESONANCE = 2.0f,
		AL_AUTOWAH_MAX_RESONANCE = 1000.0f,
		AL_AUTOWAH_DEFAULT_RESONANCE = 1000.0f,
		AL_AUTOWAH_MIN_PEAK_GAIN = 3.0E-5f,
		AL_AUTOWAH_MAX_PEAK_GAIN = 31621.0f,
		AL_AUTOWAH_DEFAULT_PEAK_GAIN = 11.22f;

	public static final int AL_COMPRESSOR_MIN_ONOFF = 0x0,
		AL_COMPRESSOR_MAX_ONOFF = 0x1,
		AL_COMPRESSOR_DEFAULT_ONOFF = 0x1;

	public static final float AL_EQUALIZER_MIN_LOW_GAIN = 0.126f,
		AL_EQUALIZER_MAX_LOW_GAIN = 7.943f,
		AL_EQUALIZER_DEFAULT_LOW_GAIN = 1.0f,
		AL_EQUALIZER_MIN_LOW_CUTOFF = 50.0f,
		AL_EQUALIZER_MAX_LOW_CUTOFF = 800.0f,
		AL_EQUALIZER_DEFAULT_LOW_CUTOFF = 200.0f,
		AL_EQUALIZER_MIN_MID1_GAIN = 0.126f,
		AL_EQUALIZER_MAX_MID1_GAIN = 7.943f,
		AL_EQUALIZER_DEFAULT_MID1_GAIN = 1.0f,
		AL_EQUALIZER_MIN_MID1_CENTER = 200.0f,
		AL_EQUALIZER_MAX_MID1_CENTER = 3000.0f,
		AL_EQUALIZER_DEFAULT_MID1_CENTER = 500.0f,
		AL_EQUALIZER_MIN_MID1_WIDTH = 0.01f,
		AL_EQUALIZER_MAX_MID1_WIDTH = 1.0f,
		AL_EQUALIZER_DEFAULT_MID1_WIDTH = 1.0f,
		AL_EQUALIZER_MIN_MID2_GAIN = 0.126f,
		AL_EQUALIZER_MAX_MID2_GAIN = 7.943f,
		AL_EQUALIZER_DEFAULT_MID2_GAIN = 1.0f,
		AL_EQUALIZER_MIN_MID2_CENTER = 1000.0f,
		AL_EQUALIZER_MAX_MID2_CENTER = 8000.0f,
		AL_EQUALIZER_DEFAULT_MID2_CENTER = 3000.0f,
		AL_EQUALIZER_MIN_MID2_WIDTH = 0.01f,
		AL_EQUALIZER_MAX_MID2_WIDTH = 1.0f,
		AL_EQUALIZER_DEFAULT_MID2_WIDTH = 1.0f,
		AL_EQUALIZER_MIN_HIGH_GAIN = 0.126f,
		AL_EQUALIZER_MAX_HIGH_GAIN = 7.943f,
		AL_EQUALIZER_DEFAULT_HIGH_GAIN = 1.0f,
		AL_EQUALIZER_MIN_HIGH_CUTOFF = 4000.0f,
		AL_EQUALIZER_MAX_HIGH_CUTOFF = 16000.0f,
		AL_EQUALIZER_DEFAULT_HIGH_CUTOFF = 6000.0f,
		LOWPASS_MIN_GAIN = 0.0f,
		LOWPASS_MAX_GAIN = 1.0f,
		LOWPASS_DEFAULT_GAIN = 1.0f,
		LOWPASS_MIN_GAINHF = 0.0f,
		LOWPASS_MAX_GAINHF = 1.0f,
		LOWPASS_DEFAULT_GAINHF = 1.0f,
		HIGHPASS_MIN_GAIN = 0.0f,
		HIGHPASS_MAX_GAIN = 1.0f,
		HIGHPASS_DEFAULT_GAIN = 1.0f,
		HIGHPASS_MIN_GAINLF = 0.0f,
		HIGHPASS_MAX_GAINLF = 1.0f,
		HIGHPASS_DEFAULT_GAINLF = 1.0f,
		BANDPASS_MIN_GAIN = 0.0f,
		BANDPASS_MAX_GAIN = 1.0f,
		BANDPASS_DEFAULT_GAIN = 1.0f,
		BANDPASS_MIN_GAINHF = 0.0f,
		BANDPASS_MAX_GAINHF = 1.0f,
		BANDPASS_DEFAULT_GAINHF = 1.0f,
		BANDPASS_MIN_GAINLF = 0.0f,
		BANDPASS_MAX_GAINLF = 1.0f,
		BANDPASS_DEFAULT_GAINLF = 1.0f;

	private EFX10() {}

	static native void initNativeStubs() throws LWJGLException;

	public static void alGenAuxiliaryEffectSlots(IntBuffer auxiliaryeffectslots) {
		BufferChecks.checkDirect(auxiliaryeffectslots);
		nalGenAuxiliaryEffectSlots(auxiliaryeffectslots.remaining(), MemoryUtil.getAddress(auxiliaryeffectslots));
	}
	static native void nalGenAuxiliaryEffectSlots(int auxiliaryeffectslots_n, long auxiliaryeffectslots);

	/** Overloads alGenAuxiliaryEffectSlots. */
	public static int alGenAuxiliaryEffectSlots() {
		int __result = nalGenAuxiliaryEffectSlots2(1);
		return __result;
	}
	static native int nalGenAuxiliaryEffectSlots2(int n);

	public static void alDeleteAuxiliaryEffectSlots(IntBuffer auxiliaryeffectslots) {
		BufferChecks.checkDirect(auxiliaryeffectslots);
		nalDeleteAuxiliaryEffectSlots(auxiliaryeffectslots.remaining(), MemoryUtil.getAddress(auxiliaryeffectslots));
	}
	static native void nalDeleteAuxiliaryEffectSlots(int auxiliaryeffectslots_n, long auxiliaryeffectslots);

	/** Overloads alDeleteAuxiliaryEffectSlots. */
	public static void alDeleteAuxiliaryEffectSlots(int auxiliaryeffectslot) {
		nalDeleteAuxiliaryEffectSlots2(1, auxiliaryeffectslot);
	}
	static native void nalDeleteAuxiliaryEffectSlots2(int n, int auxiliaryeffectslot);

	public static boolean alIsAuxiliaryEffectSlot(int auxiliaryeffectslot) {
		boolean __result = nalIsAuxiliaryEffectSlot(auxiliaryeffectslot);
		return __result;
	}
	static native boolean nalIsAuxiliaryEffectSlot(int auxiliaryeffectslot);

	public static void alAuxiliaryEffectSloti(int auxiliaryeffectslot, int param, int value) {
		nalAuxiliaryEffectSloti(auxiliaryeffectslot, param, value);
	}
	static native void nalAuxiliaryEffectSloti(int auxiliaryeffectslot, int param, int value);

	public static void alAuxiliaryEffectSlot(int auxiliaryeffectslot, int param, IntBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nalAuxiliaryEffectSlotiv(auxiliaryeffectslot, param, MemoryUtil.getAddress(values));
	}
	static native void nalAuxiliaryEffectSlotiv(int auxiliaryeffectslot, int param, long values);

	public static void alAuxiliaryEffectSlotf(int auxiliaryeffectslot, int param, float value) {
		nalAuxiliaryEffectSlotf(auxiliaryeffectslot, param, value);
	}
	static native void nalAuxiliaryEffectSlotf(int auxiliaryeffectslot, int param, float value);

	public static void alAuxiliaryEffectSlot(int auxiliaryeffectslot, int param, FloatBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nalAuxiliaryEffectSlotfv(auxiliaryeffectslot, param, MemoryUtil.getAddress(values));
	}
	static native void nalAuxiliaryEffectSlotfv(int auxiliaryeffectslot, int param, long values);

	public static int alGetAuxiliaryEffectSloti(int auxiliaryeffectslot, int param) {
		int __result = nalGetAuxiliaryEffectSloti(auxiliaryeffectslot, param);
		return __result;
	}
	static native int nalGetAuxiliaryEffectSloti(int auxiliaryeffectslot, int param);

	public static void alGetAuxiliaryEffectSlot(int auxiliaryeffectslot, int param, IntBuffer intdata) {
		BufferChecks.checkBuffer(intdata, 1);
		nalGetAuxiliaryEffectSlotiv(auxiliaryeffectslot, param, MemoryUtil.getAddress(intdata));
	}
	static native void nalGetAuxiliaryEffectSlotiv(int auxiliaryeffectslot, int param, long intdata);

	public static float alGetAuxiliaryEffectSlotf(int auxiliaryeffectslot, int param) {
		float __result = nalGetAuxiliaryEffectSlotf(auxiliaryeffectslot, param);
		return __result;
	}
	static native float nalGetAuxiliaryEffectSlotf(int auxiliaryeffectslot, int param);

	public static void alGetAuxiliaryEffectSlot(int auxiliaryeffectslot, int param, FloatBuffer floatdata) {
		BufferChecks.checkBuffer(floatdata, 1);
		nalGetAuxiliaryEffectSlotfv(auxiliaryeffectslot, param, MemoryUtil.getAddress(floatdata));
	}
	static native void nalGetAuxiliaryEffectSlotfv(int auxiliaryeffectslot, int param, long floatdata);

	public static void alGenEffects(IntBuffer effects) {
		BufferChecks.checkDirect(effects);
		nalGenEffects(effects.remaining(), MemoryUtil.getAddress(effects));
	}
	static native void nalGenEffects(int effects_n, long effects);

	/** Overloads alGenEffects. */
	public static int alGenEffects() {
		int __result = nalGenEffects2(1);
		return __result;
	}
	static native int nalGenEffects2(int n);

	public static void alDeleteEffects(IntBuffer effects) {
		BufferChecks.checkDirect(effects);
		nalDeleteEffects(effects.remaining(), MemoryUtil.getAddress(effects));
	}
	static native void nalDeleteEffects(int effects_n, long effects);

	/** Overloads alDeleteEffects. */
	public static void alDeleteEffects(int effect) {
		nalDeleteEffects2(1, effect);
	}
	static native void nalDeleteEffects2(int n, int effect);

	public static boolean alIsEffect(int effect) {
		boolean __result = nalIsEffect(effect);
		return __result;
	}
	static native boolean nalIsEffect(int effect);

	public static void alEffecti(int effect, int param, int value) {
		nalEffecti(effect, param, value);
	}
	static native void nalEffecti(int effect, int param, int value);

	public static void alEffect(int effect, int param, IntBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nalEffectiv(effect, param, MemoryUtil.getAddress(values));
	}
	static native void nalEffectiv(int effect, int param, long values);

	public static void alEffectf(int effect, int param, float value) {
		nalEffectf(effect, param, value);
	}
	static native void nalEffectf(int effect, int param, float value);

	public static void alEffect(int effect, int param, FloatBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nalEffectfv(effect, param, MemoryUtil.getAddress(values));
	}
	static native void nalEffectfv(int effect, int param, long values);

	public static int alGetEffecti(int effect, int param) {
		int __result = nalGetEffecti(effect, param);
		return __result;
	}
	static native int nalGetEffecti(int effect, int param);

	public static void alGetEffect(int effect, int param, IntBuffer intdata) {
		BufferChecks.checkBuffer(intdata, 1);
		nalGetEffectiv(effect, param, MemoryUtil.getAddress(intdata));
	}
	static native void nalGetEffectiv(int effect, int param, long intdata);

	public static float alGetEffectf(int effect, int param) {
		float __result = nalGetEffectf(effect, param);
		return __result;
	}
	static native float nalGetEffectf(int effect, int param);

	public static void alGetEffect(int effect, int param, FloatBuffer floatdata) {
		BufferChecks.checkBuffer(floatdata, 1);
		nalGetEffectfv(effect, param, MemoryUtil.getAddress(floatdata));
	}
	static native void nalGetEffectfv(int effect, int param, long floatdata);

	public static void alGenFilters(IntBuffer filters) {
		BufferChecks.checkDirect(filters);
		nalGenFilters(filters.remaining(), MemoryUtil.getAddress(filters));
	}
	static native void nalGenFilters(int filters_n, long filters);

	/** Overloads alGenFilters. */
	public static int alGenFilters() {
		int __result = nalGenFilters2(1);
		return __result;
	}
	static native int nalGenFilters2(int n);

	public static void alDeleteFilters(IntBuffer filters) {
		BufferChecks.checkDirect(filters);
		nalDeleteFilters(filters.remaining(), MemoryUtil.getAddress(filters));
	}
	static native void nalDeleteFilters(int filters_n, long filters);

	/** Overloads alDeleteFilters. */
	public static void alDeleteFilters(int filter) {
		nalDeleteFilters2(1, filter);
	}
	static native void nalDeleteFilters2(int n, int filter);

	public static boolean alIsFilter(int filter) {
		boolean __result = nalIsFilter(filter);
		return __result;
	}
	static native boolean nalIsFilter(int filter);

	public static void alFilteri(int filter, int param, int value) {
		nalFilteri(filter, param, value);
	}
	static native void nalFilteri(int filter, int param, int value);

	public static void alFilter(int filter, int param, IntBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nalFilteriv(filter, param, MemoryUtil.getAddress(values));
	}
	static native void nalFilteriv(int filter, int param, long values);

	public static void alFilterf(int filter, int param, float value) {
		nalFilterf(filter, param, value);
	}
	static native void nalFilterf(int filter, int param, float value);

	public static void alFilter(int filter, int param, FloatBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nalFilterfv(filter, param, MemoryUtil.getAddress(values));
	}
	static native void nalFilterfv(int filter, int param, long values);

	public static int alGetFilteri(int filter, int param) {
		int __result = nalGetFilteri(filter, param);
		return __result;
	}
	static native int nalGetFilteri(int filter, int param);

	public static void alGetFilter(int filter, int param, IntBuffer intdata) {
		BufferChecks.checkBuffer(intdata, 1);
		nalGetFilteriv(filter, param, MemoryUtil.getAddress(intdata));
	}
	static native void nalGetFilteriv(int filter, int param, long intdata);

	public static float alGetFilterf(int filter, int param) {
		float __result = nalGetFilterf(filter, param);
		return __result;
	}
	static native float nalGetFilterf(int filter, int param);

	public static void alGetFilter(int filter, int param, FloatBuffer floatdata) {
		BufferChecks.checkBuffer(floatdata, 1);
		nalGetFilterfv(filter, param, MemoryUtil.getAddress(floatdata));
	}
	static native void nalGetFilterfv(int filter, int param, long floatdata);
}
