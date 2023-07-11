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

/**
 * This class is a wrapper around a cl_device_id pointer.
 *
 * @author Spasi
 */
public final class CLDevice extends CLObjectChild<CLDevice> {

	private static final InfoUtil<CLDevice> util = CLPlatform.getInfoUtilInstance(CLDevice.class, "CL_DEVICE_UTIL");

	private final CLPlatform platform;
	private final CLObjectRegistry<CLDevice> subCLDevices;

	private Object caps;

	CLDevice(final long pointer, final CLPlatform platform) {
		this(pointer, null, platform);
	}

	/**
	 * EXT_device_fission constructor.
	 *
	 * @param pointer the sub-device pointer
	 * @param parent  the parent CLDevice
	 */
	CLDevice(final long pointer, final CLDevice parent) {
		this(pointer, parent, parent.getPlatform());
	}

	CLDevice(final long pointer, final CLDevice parent, final CLPlatform platform) {
		super(pointer, parent);

		if ( isValid() ) {
			this.platform = platform;
			platform.getCLDeviceRegistry().registerObject(this);

			this.subCLDevices = new CLObjectRegistry<CLDevice>();
			if ( parent != null )
				parent.subCLDevices.registerObject(this);
		} else {
			this.platform = null;
			this.subCLDevices = null;
		}
	}

	public CLPlatform getPlatform() {
		return platform;
	}

	/**
	 * Returns a sub-device of this device.
	 *
	 * @param id the sub-device object id
	 *
	 * @return the CLDevice object
	 */
	public CLDevice getSubCLDevice(final long id) { return subCLDevices.getObject(id); }

	// ---------------[ UTILITY METHODS ]---------------

	/**
	 * Returns the value of the specified String parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public String getInfoString(int param_name) {
		return util.getInfoString(this, param_name);
	}

	/**
	 * Returns the integer value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public int getInfoInt(int param_name) {
		return util.getInfoInt(this, param_name);
	}

	/**
	 * Returns the boolean value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public boolean getInfoBoolean(int param_name) {
		return util.getInfoInt(this, param_name) != 0;
	}

	/**
	 * Returns the size_t value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public long getInfoSize(int param_name) {
		return util.getInfoSize(this, param_name);
	}

	/**
	 * Returns an array of size_t values of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter values
	 */
	public long[] getInfoSizeArray(int param_name) {
		return util.getInfoSizeArray(this, param_name);
	}

	/**
	 * Returns the long value of the specified parameter. Can be used
	 * for both cl_ulong and cl_bitfield parameters.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public long getInfoLong(int param_name) {
		return util.getInfoLong(this, param_name);
	}

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	void setCapabilities(final Object caps) {
		this.caps = caps;
	}

	Object getCapabilities() {
		return caps;
	}

	int retain() {
		if ( getParent() == null )
			return getReferenceCount(); // NO-OP, root devices cannot be retained

		return super.retain();
	}

	int release() {
		if ( getParent() == null )
			return getReferenceCount(); // NO-OP, root devices cannot be released

		try {
			return super.release();
		} finally {
			if ( !isValid() )
				getParent().subCLDevices.unregisterObject(this);
		}
	}

	CLObjectRegistry<CLDevice> getSubCLDeviceRegistry() { return subCLDevices; }

	/**
	 * Called from clCreateSubDevicesEXT to register new sub-devices.
	 *
	 * @param devices a buffer containing CLDevice pointers.
	 */
	void registerSubCLDevices(final PointerBuffer devices) {
		for ( int i = devices.position(); i < devices.limit(); i++ ) {
			final long pointer = devices.get(i);
			if ( pointer != 0 )
				new CLDevice(pointer, this);
		}
	}

}