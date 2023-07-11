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
 * This class is a wrapper around a cl_command_queue pointer.
 *
 * @author Spasi
 */
public final class CLCommandQueue extends CLObjectChild<CLContext> {

	private static final InfoUtil<CLCommandQueue> util = CLPlatform.getInfoUtilInstance(CLCommandQueue.class, "CL_COMMAND_QUEUE_UTIL");

	private final CLDevice device;

	private final CLObjectRegistry<CLEvent> clEvents;

	CLCommandQueue(final long pointer, final CLContext context, final CLDevice device) {
		super(pointer, context);
		if ( isValid() ) {
			this.device = device;
			this.clEvents = new CLObjectRegistry<CLEvent>();
			context.getCLCommandQueueRegistry().registerObject(this);
		} else {
			this.device = null;
			this.clEvents = null;
		}
	}

	public CLDevice getCLDevice() {
		return device;
	}

	/**
	 * Returns a CLEvent associated with this command-queue.
	 *
	 * @param id the event object id
	 *
	 * @return the CLEvent object
	 */
	public CLEvent getCLEvent(final long id) {
		return clEvents.getObject(id);
	}

	// ---------------[ UTILITY METHODS ]---------------

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

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	CLObjectRegistry<CLEvent> getCLEventRegistry() { return clEvents; }

	/**
	 * Called from OpenCL methods that generate CLEvents.
	 *
	 * @param event a buffer containing a CLEvent pointer.
	 */
	void registerCLEvent(final PointerBuffer event) {
		if ( event != null )
			new CLEvent(event.get(event.position()), this);
	}

	int release() {
		try {
			return super.release();
		} finally {
			if ( !isValid() )
				getParent().getCLCommandQueueRegistry().unregisterObject(this);
		}
	}

}