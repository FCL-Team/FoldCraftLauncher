/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opencl;

import org.lwjgl.*;
import java.nio.*;

public final class INTELThreadLocalExec {

	/**
	 *  Allows the user to execute OpenCL tasks and kernels with
	 *  the user application's threads. This token that can
	 *  be passed to clCreateCommandQueue, creating a queue with the "thread
	 *  local exec" capability.
	 *  <p/>
	 *  All enqueue APIs (e.g., clEnqueueRead) submitted to such a queue
	 *  never enqueue commands. An Enqueue API call is executed by the
	 *  caller host-thread itself without involving any of the OpenCL
	 *  runtime threads, much like function calls.
	 */
	public static final int CL_QUEUE_THREAD_LOCAL_EXEC_ENABLE_INTEL = 0x80000000;

	private INTELThreadLocalExec() {}
}
