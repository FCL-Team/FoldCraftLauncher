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
 * This class is a wrapper around a cl_mem pointer.
 *
 * @author Spasi
 */
public final class CLEvent extends CLObjectChild<CLContext> {

	private static final CLEventUtil util = (CLEventUtil)CLPlatform.getInfoUtilInstance(CLEvent.class, "CL_EVENT_UTIL");

	private final CLCommandQueue queue;

	CLEvent(final long pointer, final CLContext context) {
		this(pointer, context, null);
	}

	CLEvent(final long pointer, final CLCommandQueue queue) {
		this(pointer, queue.getParent(), queue);
	}

	CLEvent(final long pointer, final CLContext context, final CLCommandQueue queue) {
		super(pointer, context);
		if ( isValid() ) {
			this.queue = queue;
			if ( queue == null )
				context.getCLEventRegistry().registerObject(this);
			else
				queue.getCLEventRegistry().registerObject(this);
		} else
			this.queue = null;
	}

	/**
	 * Returns the command-queue associated with this event. For
	 * user events this method returns null.
	 *
	 * @return the command-queue or null if this is a user event
	 */
	public CLCommandQueue getCLCommandQueue() {
		return queue;
	}

	// ---------------[ UTILITY METHODS ]---------------

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

	// clGetEventProfilingInfo methods

	/**
	 * Returns the long value of the specified parameter. Can be used
	 * for both cl_ulong and cl_bitfield parameters.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public long getProfilingInfoLong(int param_name) {
		return util.getProfilingInfoLong(this, param_name);
	}

	/** CLEvent utility methods interface. */
	interface CLEventUtil extends InfoUtil<CLEvent> {

		long getProfilingInfoLong(CLEvent event, int param_name);

	}

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	CLObjectRegistry<CLEvent> getParentRegistry() {
		if ( queue == null )
			return getParent().getCLEventRegistry();
		else
			return queue.getCLEventRegistry();
	}

	int release() {
		try {
			return super.release();
		} finally {
			if ( !isValid() ) {
				if ( queue == null )
					getParent().getCLEventRegistry().unregisterObject(this);
				else
					queue.getCLEventRegistry().unregisterObject(this);
			}
		}
	}

}