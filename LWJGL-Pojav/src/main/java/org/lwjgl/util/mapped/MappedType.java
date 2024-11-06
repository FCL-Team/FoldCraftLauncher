/*
 * Copyright (c) 2002-2011 LWJGL Project
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
package org.lwjgl.util.mapped;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a class as a mapped object, which will go under bytecode
 * transformation at runtime. Mapped objects cannot be instantiated directly; a data
 * buffer must be mapped first and the mapped object instance will then be used as a
 * view on top of the buffer. Instead of a separate instance per "element" in the buffer,
 * only a single instance is used to manage everything. See {@link MappedObject}
 * for API details and {@link org.lwjgl.test.mapped.TestMappedObject} for examples.
 * <p/>
 * The instance fields of the annotated class should only be limited to primitive types or
 * {@link java.nio.ByteBuffer}. Static fields are supported and they can have any type.
 * <p/>
 * The purpose of mapped objects is to reduce the memory requirements required for the type
 * of data that are often used in OpenGL/OpenCL programming, while at the same time enabling
 * clean Java code. There are also performance benefits related to not having to copy data
 * between buffers and Java objects and the removal of bounds checking when accessing
 * buffer data.
 *
 * @author Riven
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedType {

	/**
	 * The number of bytes to add to the total byte size.
	 * SIZEOF will be calculated as <code>SIZEOF = max(field_offset + field_length) + padding</code>.
	 * <p/>
	 * Cannot be used with {@link #cacheLinePadding()}.
	 *
	 * @return the padding amount
	 */
	int padding() default 0;

	/**
	 * When true, SIZEOF will be increased (if necessary) so that it's a multiple of the CPU cache line size.
	 * Additionally, {@link MappedObject#malloc(int)} on the mapped object type will automatically use
	 * {@link CacheUtil#createByteBuffer(int)} instead of the unaligned {@link org.lwjgl.BufferUtils#createByteBuffer(int)}.
	 * <p/>
	 * Cannot be used with {@link #padding()}.
	 *
	 * @return if cache-line padding should be applied
	 *
	 * @see CacheUtil
	 */
	boolean cacheLinePadding() default false;

	/**
	 * The mapped data memory alignment, in bytes.
	 *
	 * @return the memory alignment
	 */
	int align() default 4;

	/**
	 * When autoGenerateOffsets is true, byte offsets of the mapped fields will
	 * be generated automatically. This is convenient for packed data. For manually
	 * aligned data, autoGenerateOffsets must be set to false and the user needs
	 * to manually specify byte offsets using the {@link MappedField} annotation.
	 *
	 * @return true if automatic byte offset generation is required.
	 */
	boolean autoGenerateOffsets() default true;

}