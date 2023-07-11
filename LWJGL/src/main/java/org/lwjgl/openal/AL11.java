/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.openal;

import org.lwjgl.*;
import java.nio.*;

/**
 *  <br>
 *  This is the core OpenAL class. This class implements
 *  AL.h version 1.1
 * <p>
 *  @author Brian Matzon <brian@matzon.dk>
 *  @version $Revision: 2286 $
 *  $Id: AL10.java 2286 2006-03-23 19:32:21Z matzon $
 */
public final class AL11 {

	/**
	 * Source buffer position information in seconds 
	 */
	public static final int AL_SEC_OFFSET = 0x1024;

	/**
	 * Source buffer position information in samples 
	 */
	public static final int AL_SAMPLE_OFFSET = 0x1025;

	/**
	 * Source buffer position information in bytes 
	 */
	public static final int AL_BYTE_OFFSET = 0x1026;

	/**
	 * Type of source: Buffer has been attached using AL_BUFFER 
	 */
	public static final int AL_STATIC = 0x1028;

	/**
	 * Type of source: if one or more Buffers have been attached using alSourceQueueBuffers 
	 */
	public static final int AL_STREAMING = 0x1029;

	/**
	 * Type of source: when it has the NULL buffer attached 
	 */
	public static final int AL_UNDETERMINED = 0x1030;

	/**
	 * @see AL10#AL_INVALID_OPERATION 
	 */
	public static final int AL_ILLEGAL_COMMAND = 0xA004;

	/**
	 * Speed of Sound in units per second 
	 */
	public static final int AL_SPEED_OF_SOUND = 0xC003,
		AL_LINEAR_DISTANCE = 0xD003,
		AL_LINEAR_DISTANCE_CLAMPED = 0xD004,
		AL_EXPONENT_DISTANCE = 0xD005,
		AL_EXPONENT_DISTANCE_CLAMPED = 0xD006;

	private AL11() {}

	static native void initNativeStubs() throws LWJGLException;

	/**
	 *  Listener attributes are changed using the Listener group of commands.
	 * <p>
	 *  @param pname name of the attribute to be set
	 *  @param v1 value value 1
	 *  @param v2 value value 2
	 *  @param v3 value value 3
	 */
	public static void alListener3i(int pname, int v1, int v2, int v3) {
		nalListener3i(pname, v1, v2, v3);
	}
	static native void nalListener3i(int pname, int v1, int v2, int v3);

	/**
	 *  Listener state is maintained inside the AL implementation and can be queried in
	 *  full.
	 * <p>
	 *  @param pname name of the attribute to be retrieved
	 *  @param intdata Buffer to write ints to
	 */
	public static void alGetListeneri(int pname, FloatBuffer intdata) {
		BufferChecks.checkBuffer(intdata, 1);
		nalGetListeneriv(pname, MemoryUtil.getAddress(intdata));
	}
	static native void nalGetListeneriv(int pname, long intdata);

	/**
	 *  Specifies the position and other properties as taken into account during
	 *  sound processing.
	 * <p>
	 *  @param source Source to set property on
	 *  @param pname property to set
	 *  @param v1 value 1 of property
	 *  @param v2 value 2 of property
	 *  @param v3 value 3 of property
	 */
	public static void alSource3i(int source, int pname, int v1, int v2, int v3) {
		nalSource3i(source, pname, v1, v2, v3);
	}
	static native void nalSource3i(int source, int pname, int v1, int v2, int v3);

	/**
	 *  Specifies the position and other properties as taken into account during
	 *  sound processing.
	 * <p>
	 *  @param source Source to set property on
	 *  @param pname property to set
	 *  @param value IntBuffer containing value of property
	 */
	public static void alSource(int source, int pname, IntBuffer value) {
		BufferChecks.checkBuffer(value, 1);
		nalSourceiv(source, pname, MemoryUtil.getAddress(value));
	}
	static native void nalSourceiv(int source, int pname, long value);

	/**
	 *  This function sets a floating point property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to set property on
	 *  @param pname property to set
	 *  @param value value of property
	 */
	public static void alBufferf(int buffer, int pname, float value) {
		nalBufferf(buffer, pname, value);
	}
	static native void nalBufferf(int buffer, int pname, float value);

	/**
	 *  This function sets a floating point property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to set property on
	 *  @param pname property to set
	 *  @param v1 value of property
	 *  @param v2 value of property
	 *  @param v3 value of property
	 */
	public static void alBuffer3f(int buffer, int pname, float v1, float v2, float v3) {
		nalBuffer3f(buffer, pname, v1, v2, v3);
	}
	static native void nalBuffer3f(int buffer, int pname, float v1, float v2, float v3);

	/**
	 *  This function sets a floating point property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to set property on
	 *  @param pname property to set
	 *  @param value FloatBuffer containing value of property
	 */
	public static void alBuffer(int buffer, int pname, FloatBuffer value) {
		BufferChecks.checkBuffer(value, 1);
		nalBufferfv(buffer, pname, MemoryUtil.getAddress(value));
	}
	static native void nalBufferfv(int buffer, int pname, long value);

	/**
	 *  This function sets an integer property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to set property on
	 *  @param pname property to set
	 *  @param value value of property
	 */
	public static void alBufferi(int buffer, int pname, int value) {
		nalBufferi(buffer, pname, value);
	}
	static native void nalBufferi(int buffer, int pname, int value);

	/**
	 *  This function sets an integer property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to set property on
	 *  @param pname property to set
	 *  @param v1 value of property
	 *  @param v2 value of property
	 *  @param v3 value of property
	 */
	public static void alBuffer3i(int buffer, int pname, int v1, int v2, int v3) {
		nalBuffer3i(buffer, pname, v1, v2, v3);
	}
	static native void nalBuffer3i(int buffer, int pname, int v1, int v2, int v3);

	/**
	 *  This function sets an integer property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to set property on
	 *  @param pname property to set
	 *  @param value IntBuffer containing value of property
	 */
	public static void alBuffer(int buffer, int pname, IntBuffer value) {
		BufferChecks.checkBuffer(value, 1);
		nalBufferiv(buffer, pname, MemoryUtil.getAddress(value));
	}
	static native void nalBufferiv(int buffer, int pname, long value);

	/**
	 *  This function retrieves an integer property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to get property from
	 *  @param pname name of property
	 *  @return int
	 */
	public static int alGetBufferi(int buffer, int pname) {
		int __result = nalGetBufferi(buffer, pname);
		return __result;
	}
	static native int nalGetBufferi(int buffer, int pname);

	/**
	 *  This function retrieves an integer property of a buffer.
	 * <p>
	 *  @param buffer Buffer to get property from
	 *  @param pname name of property
	 */
	public static void alGetBuffer(int buffer, int pname, IntBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nalGetBufferiv(buffer, pname, MemoryUtil.getAddress(values));
	}
	static native void nalGetBufferiv(int buffer, int pname, long values);

	/**
	 *  This function retrieves a floating point property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to get property from
	 *  @param pname name of property
	 *  @return floating point property
	 */
	public static float alGetBufferf(int buffer, int pname) {
		float __result = nalGetBufferf(buffer, pname);
		return __result;
	}
	static native float nalGetBufferf(int buffer, int pname);

	/**
	 *  This function retrieves a floating point property of a buffer.
	 *  <i>note: There are no relevant buffer properties defined in OpenAL 1.1 which can be affected by
	 *  this call, but this function may be used by OpenAL extensions.</i>
	 * <p>
	 *  @param buffer Buffer to get property from
	 *  @param pname name of property
	 */
	public static void alGetBuffer(int buffer, int pname, FloatBuffer values) {
		BufferChecks.checkBuffer(values, 1);
		nalGetBufferfv(buffer, pname, MemoryUtil.getAddress(values));
	}
	static native void nalGetBufferfv(int buffer, int pname, long values);

	/**
	 *  <p>
	 *  AL_SPEED_OF_SOUND allows the application to change the reference (propagation)
	 *  speed used in the Doppler calculation. The source and listener velocities should be
	 *  expressed in the same units as the speed of sound.
	 *  </p>
	 *  <p>
	 *  A negative or zero value will result in an AL_INVALID_VALUE error, and the
	 *  command is ignored. The default value is 343.3 (appropriate for velocity units of meters
	 *  and air as the propagation medium). The current setting can be queried using
	 *  alGetFloat{v} and AL_SPEED_OF_SOUND.
	 *  Distance and velocity units are completely independent of one another (so you could use
	 *  different units for each if desired).
	 *  </p>
	 * <p>
	 *  @param value distance model to be set
	 */
	public static void alSpeedOfSound(float value) {
		nalSpeedOfSound(value);
	}
	static native void nalSpeedOfSound(float value);
}
