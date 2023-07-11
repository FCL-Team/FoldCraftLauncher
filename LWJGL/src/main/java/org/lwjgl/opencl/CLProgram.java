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

import java.nio.ByteBuffer;

/**
 * This class is a wrapper around a cl_program pointer.
 *
 * @author Spasi
 */
public final class CLProgram extends CLObjectChild<CLContext> {

	private static final CLProgramUtil util = (CLProgramUtil)CLPlatform.getInfoUtilInstance(CLProgram.class, "CL_PROGRAM_UTIL");

	private final CLObjectRegistry<CLKernel> clKernels;

	CLProgram(final long pointer, final CLContext context) {
		super(pointer, context);

		if ( isValid() ) {
			context.getCLProgramRegistry().registerObject(this);
			clKernels = new CLObjectRegistry<CLKernel>();
		} else
			clKernels = null;
	}

	/**
	 * Returns a CLKernel associated with this program.
	 *
	 * @param id the kernel id
	 *
	 * @return the CLKernel object
	 */
	public CLKernel getCLKernel(final long id) {
		return clKernels.getObject(id);
	}

	// ---------------[ UTILITY METHODS ]---------------

	/**
	 * Creates kernel objects for all kernels functions in this program.
	 *
	 * @return a CLKernel array
	 */
	public CLKernel[] createKernelsInProgram() {
		return util.createKernelsInProgram(this);
	}

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

	/**
	 * Returns an array of size_t values of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter values
	 */
	public long[] getInfoSizeArray(final int param_name) {
		return util.getInfoSizeArray(this, param_name);
	}

	/**
	 * Returns an array of CLDevices associated with this program.
	 *
	 * @return the array of devices
	 */
	public CLDevice[] getInfoDevices() {
		return util.getInfoDevices(this);
	}

	/**
	 * Returns the program binaries for all devices associated with program,
	 * written sequentially in the target ByteBuffer. If the <code>target</code>
	 * parameter is null, a new ByteBuffer will be allocated. If not, the
	 * target ByteBuffer must be big enough to hold the program binaries, as
	 * returned by CL_PROGRAM_BINARY_SIZES.
	 *
	 * @param target the target ByteBuffer array.
	 *
	 * @return the array of devices
	 */
	public ByteBuffer getInfoBinaries(final ByteBuffer target) {
		return util.getInfoBinaries(this, target);
	}

	/**
	 * Returns the program binaries for all devices associated with program,
	 * as a ByteBuffer array. If the <code>target</code> parameter is null,
	 * a new ByteBuffer array will be allocated. If not, the target ByteBuffers
	 * must be big enough to hold the program binaries, as returned by
	 * CL_PROGRAM_BINARY_SIZES.
	 *
	 * @param target the target ByteBuffer array.
	 *
	 * @return the array of devices
	 */
	public ByteBuffer[] getInfoBinaries(final ByteBuffer[] target) {
		return util.getInfoBinaries(this, target);
	}

	// clGetProgramBuildInfo methods

	/**
	 * Returns the String value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public String getBuildInfoString(final CLDevice device, final int param_name) {
		return util.getBuildInfoString(this, device, param_name);
	}

	/**
	 * Returns the integer value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public int getBuildInfoInt(final CLDevice device, final int param_name) {
		return util.getBuildInfoInt(this, device, param_name);
	}

	/** CLProgram utility methods interface. */
	interface CLProgramUtil extends InfoUtil<CLProgram> {

		CLKernel[] createKernelsInProgram(CLProgram program);

		CLDevice[] getInfoDevices(CLProgram program);

		ByteBuffer getInfoBinaries(CLProgram program, ByteBuffer target);

		ByteBuffer[] getInfoBinaries(CLProgram program, ByteBuffer[] target);

		String getBuildInfoString(CLProgram program, final CLDevice device, int param_name);

		int getBuildInfoInt(CLProgram program, final CLDevice device, int param_name);

	}

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	CLObjectRegistry<CLKernel> getCLKernelRegistry() { return clKernels; }

	/**
	 * Called from clCreateKernelsInProgram to register new CLKernels.
	 *
	 * @param kernels a buffer containing CLKernel pointers.
	 */
	void registerCLKernels(final PointerBuffer kernels) {
		for ( int i = kernels.position(); i < kernels.limit(); i++ ) {
			final long pointer = kernels.get(i);
			if ( pointer != 0 )
				new CLKernel(pointer, this);
		}
	}

	int release() {
		try {
			return super.release();
		} finally {
			if ( !isValid() )
				getParent().getCLProgramRegistry().unregisterObject(this);
		}
	}

}