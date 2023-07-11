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

/**
 * This class is a wrapper around a cl_kernel pointer.
 *
 * @author Spasi
 */
public final class CLKernel extends CLObjectChild<CLProgram> {

	private static final CLKernelUtil util = (CLKernelUtil)CLPlatform.getInfoUtilInstance(CLKernel.class, "CL_KERNEL_UTIL");

	CLKernel(final long pointer, final CLProgram program) {
		super(pointer, program);
		if ( isValid() )
			program.getCLKernelRegistry().registerObject(this);
	}

	// ---------------[ UTILITY METHODS ]---------------

	// clSetKernelArg methods

	/**
	 * Sets a kernel argument at the specified index to the specified
	 * byte value.
	 *
	 * @param index the argument index
	 * @param value the argument value
	 *
	 * @return this CLKernel object
	 */
	public CLKernel setArg(final int index, final byte value) {
		util.setArg(this, index, value);
		return this;
	}

	/**
	 * Sets a kernel argument at the specified index to the specified
	 * byte value.
	 *
	 * @param index the argument index
	 * @param value the argument value
	 *
	 * @return this CLKernel object
	 */
	public CLKernel setArg(final int index, final short value) {
		util.setArg(this, index, value);
		return this;
	}

	/**
	 * Sets a kernel argument at the specified index to the specified
	 * int value.
	 *
	 * @param index the argument index
	 * @param value the argument value
	 *
	 * @return this CLKernel object
	 */
	public CLKernel setArg(final int index, final int value) {
		util.setArg(this, index, value);
		return this;
	}

	/**
	 * Sets a kernel argument at the specified index to the specified
	 * long value.
	 *
	 * @param index the argument index
	 * @param value the argument value
	 *
	 * @return this CLKernel object
	 */
	public CLKernel setArg(final int index, final long value) {
		util.setArg(this, index, value);
		return this;
	}

	/**
	 * Sets a kernel argument at the specified index to the specified
	 * float value.
	 *
	 * @param index the argument index
	 * @param value the argument value
	 *
	 * @return this CLKernel object
	 */
	public CLKernel setArg(final int index, final float value) {
		util.setArg(this, index, value);
		return this;
	}

	/**
	 * Sets a kernel argument at the specified index to the specified
	 * double value.
	 *
	 * @param index the argument index
	 * @param value the argument value
	 *
	 * @return this CLKernel object
	 */
	public CLKernel setArg(final int index, final double value) {
		util.setArg(this, index, value);
		return this;
	}

	/**
	 * Sets a kernel argument at the specified index to the specified
	 * pointer value.
	 *
	 * @param index the argument index
	 * @param value the argument value
	 *
	 * @return this CLKernel object
	 */
	public CLKernel setArg(final int index, final CLObject value) {
		util.setArg(this, index, value);
		return this;
	}

	/**
	 * Sets the size of a __local kernel argument at the specified index.
	 *
	 * @param index the argument index
	 * @param size  the argument size
	 *
	 * @return this CLKernel object
	 */
	public CLKernel setArgSize(final int index, final long size) {
		util.setArgSize(this, index, size);
		return this;
	}

	// clGetKernelInfo methods

	/**
	 * Returns the String value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public String getInfoString(final int param_name) {
		return util.getInfoString(this, param_name);
	}

	/**
	 * Returns the integer value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public int getInfoInt(final int param_name) {
		return util.getInfoInt(this, param_name);
	}

	// clGetKernelWorkGroupInfo methods

	/**
	 * Returns the size_t value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public long getWorkGroupInfoSize(final CLDevice device, int param_name) {
		return util.getWorkGroupInfoSize(this, device, param_name);
	}

	/**
	 * Returns an array of size_t values of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter values
	 */
	public long[] getWorkGroupInfoSizeArray(final CLDevice device, int param_name) {
		return util.getWorkGroupInfoSizeArray(this, device, param_name);
	}

	/**
	 * Returns the long value of the specified parameter. Can be used
	 * for both cl_ulong and cl_bitfield parameters.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public long getWorkGroupInfoLong(final CLDevice device, int param_name) {
		return util.getWorkGroupInfoLong(this, device, param_name);
	}

	/** CLKernel utility methods interface. */
	interface CLKernelUtil extends InfoUtil<CLKernel> {

		void setArg(CLKernel kernel, int index, byte value);

		void setArg(CLKernel kernel, int index, short value);

		void setArg(CLKernel kernel, int index, int value);

		void setArg(CLKernel kernel, int index, long value);

		void setArg(CLKernel kernel, int index, float value);

		void setArg(CLKernel kernel, int index, double value);

		void setArg(CLKernel kernel, int index, CLObject pointer);

		void setArgSize(CLKernel kernel, int index, long size);

		long getWorkGroupInfoSize(CLKernel kernel, CLDevice device, int param_name);

		long[] getWorkGroupInfoSizeArray(CLKernel kernel, CLDevice device, int param_name);

		long getWorkGroupInfoLong(CLKernel kernel, CLDevice device, int param_name);

	}

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	int release() {
		try {
			return super.release();
		} finally {
			if ( !isValid() )
				getParent().getCLKernelRegistry().unregisterObject(this);
		}
	}

}