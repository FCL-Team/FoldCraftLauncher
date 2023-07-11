/*
 * Copyright (c) 2002-2008 LWJGL Project
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
package org.lwjgl.opengl;

import static org.lwjgl.opengl.GL11.*;

class ReferencesStack {
	private References[] references_stack;
	private int stack_pos;

	public References getReferences() {
		return references_stack[stack_pos];
	}

	public void pushState() {
		int pos = ++stack_pos;
		if (pos == references_stack.length) {
			growStack();
		}
		references_stack[pos].copy(references_stack[pos - 1], GL_ALL_CLIENT_ATTRIB_BITS);
	}

	public References popState(int mask) {
		References result = references_stack[stack_pos--];

		references_stack[stack_pos].copy(result, ~mask);
		result.clear();

		return result;
	}

	private void growStack() {
		References[] new_references_stack = new References[references_stack.length + 1];
		System.arraycopy(references_stack, 0, new_references_stack, 0, references_stack.length);
		references_stack = new_references_stack;
		references_stack[references_stack.length - 1] = new References(GLContext.getCapabilities());
    }

	ReferencesStack() {
        ContextCapabilities caps = GLContext.getCapabilities();
		references_stack = new References[1];
		stack_pos = 0;
		for (int i = 0; i < references_stack.length; i++)
			references_stack[i] = new References(caps);
	}
}
