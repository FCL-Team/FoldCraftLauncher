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
package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.api.Filter;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static java.lang.Math.*;

/**
 * This class is a wrapper around a cl_platform_id pointer.
 *
 * @author Spasi
 */
public final class CLPlatform extends CLObject {

	private static final CLPlatformUtil util = (CLPlatformUtil)getInfoUtilInstance(CLPlatform.class, "CL_PLATFORM_UTIL");

	private static final FastLongMap<CLPlatform> clPlatforms = new FastLongMap<CLPlatform>();

	private final CLObjectRegistry<CLDevice> clDevices;

	private Object caps;

	CLPlatform(final long pointer) {
		super(pointer);

		if ( isValid() ) {
			clPlatforms.put(pointer, this);
			clDevices = new CLObjectRegistry<CLDevice>();
		} else
			clDevices = null;
	}

	/**
	 * Returns a CLPlatform with the specified id.
	 *
	 * @param id the platform object id
	 *
	 * @return the CLPlatform object
	 */
	public static CLPlatform getCLPlatform(final long id) { return clPlatforms.get(id); }

	/**
	 * Returns a CLDevice that is available on this platform.
	 *
	 * @param id the device object id
	 *
	 * @return the CLDevice object
	 */
	public CLDevice getCLDevice(final long id) { return clDevices.getObject(id); }

	// ---------------[ UTILITY METHODS ]---------------

	@SuppressWarnings("unchecked")
	static <T extends CLObject> InfoUtil<T> getInfoUtilInstance(final Class<T> clazz, final String fieldName) {
		InfoUtil<T> instance = null;
		try {
			final Class<?> infoUtil = Class.forName("org.lwjgl.opencl.InfoUtilFactory");
			instance = (InfoUtil<T>)infoUtil.getDeclaredField(fieldName).get(null);
		} catch (Exception e) {
			// Ignore
		}
		return instance;
	}

	/**
	 * Returns a list of all the available platforms.
	 *
	 * @return the available platforms
	 */
	public static List<CLPlatform> getPlatforms() {
		return getPlatforms(null);
	}

	/**
	 * Returns a list of the available platforms, filtered by the specified filter.
	 *
	 * @param filter the platform filter
	 *
	 * @return the available platforms
	 */
	public static List<CLPlatform> getPlatforms(final Filter<CLPlatform> filter) {
		return util.getPlatforms(filter);
	}

	/**
	 * Returns the String value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public String getInfoString(int param_name) {
		return util.getInfoString(this, param_name);
	}

	/**
	 * Returns a list of the available devices on this platform that
	 * match the specified type.
	 *
	 * @param device_type the device type
	 *
	 * @return the available devices
	 */
	public List<CLDevice> getDevices(final int device_type) {
		return getDevices(device_type, null);
	}

	/**
	 * Returns a list of the available devices on this platform that
	 * match the specified type, filtered by the specified filter.
	 *
	 * @param device_type the device type
	 * @param filter      the device filter
	 *
	 * @return the available devices
	 */
	public List<CLDevice> getDevices(final int device_type, final Filter<CLDevice> filter) {
		return util.getDevices(this, device_type, filter);
	}

	/** CLPlatform utility methods interface. */
	interface CLPlatformUtil extends InfoUtil<CLPlatform> {

		List<CLPlatform> getPlatforms(Filter<CLPlatform> filter);

		List<CLDevice> getDevices(CLPlatform platform, int device_type, final Filter<CLDevice> filter);

	}

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	void setCapabilities(final Object caps) {
		this.caps = caps;
	}

	Object getCapabilities() {
		return caps;
	}

	/**
	 * Called from clGetPlatformIDs to register new platforms.
	 *
	 * @param platforms a buffer containing CLPlatform pointers.
	 */
	static void registerCLPlatforms(final PointerBuffer platforms, final IntBuffer num_platforms) {
		if ( platforms == null )
			return;

		final int pos = platforms.position();
		final int count = min(num_platforms.get(0), platforms.remaining()); // We can't depend on .remaining()
		for ( int i = 0; i < count; i++ ) {
			final long id = platforms.get(pos + i);
			if ( !clPlatforms.containsKey(id) )
				new CLPlatform(id);
		}
	}

	CLObjectRegistry<CLDevice> getCLDeviceRegistry() { return clDevices; }

	/**
	 * Called from <code>clGetDeviceIDs</code> to register new devices.
	 *
	 * @param devices a buffer containing CLDevice pointers.
	 */
	void registerCLDevices(final PointerBuffer devices, final IntBuffer num_devices) {
		final int pos = devices.position();
		final int count = min(num_devices.get(num_devices.position()), devices.remaining()); // We can't depend on .remaining()
		for ( int i = 0; i < count; i++ ) {
			final long id = devices.get(pos + i);
			if ( !clDevices.hasObject(id) )
				new CLDevice(id, this);
		}
	}

	/**
	 * Called from <code>clGetContextInfo</code> to register new devices.
	 *
	 * @param devices a buffer containing CLDevice pointers.
	 */
	void registerCLDevices(final ByteBuffer devices, final PointerBuffer num_devices) {
		final int pos = devices.position();
		final int count = min((int)num_devices.get(num_devices.position()), devices.remaining()) / PointerBuffer.getPointerSize(); // We can't depend on .remaining()
		for ( int i = 0; i < count; i++ ) {
			final int offset = pos + (i * PointerBuffer.getPointerSize());
			final long id = PointerBuffer.is64Bit() ? devices.getLong(offset) : devices.getInt(offset);
			if ( !clDevices.hasObject(id) )
				new CLDevice(id, this);
		}
	}

}