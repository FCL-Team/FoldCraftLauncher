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

package org.lwjgl.util.generator.openal;

/**
 *
 * The OpenAL specific generator behaviour
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 2983 $
 * $Id: ALTypeMap.java 2983 2008-04-07 18:36:09Z matzon $
 */

import org.lwjgl.util.generator.Signedness;
import org.lwjgl.util.generator.TypeMap;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.nio.*;
import java.util.HashMap;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

public class ALTypeMap implements TypeMap {
	private static final Map<Class, TypeKind> native_types_to_primitive;

	static {
		native_types_to_primitive = new HashMap<Class, TypeKind>();
		native_types_to_primitive.put(ALboolean.class, TypeKind.BOOLEAN);
		native_types_to_primitive.put(ALbyte.class, TypeKind.BYTE);
		native_types_to_primitive.put(ALenum.class, TypeKind.INT);
		native_types_to_primitive.put(ALfloat.class, TypeKind.FLOAT);
		native_types_to_primitive.put(ALdouble.class, TypeKind.DOUBLE);
		native_types_to_primitive.put(ALint.class, TypeKind.INT);
		native_types_to_primitive.put(ALshort.class, TypeKind.SHORT);
		native_types_to_primitive.put(ALsizei.class, TypeKind.INT);
		native_types_to_primitive.put(ALubyte.class, TypeKind.BYTE);
		native_types_to_primitive.put(ALuint.class, TypeKind.INT);
		native_types_to_primitive.put(ALvoid.class, TypeKind.BYTE);
	}

	@Override
	public TypeKind getPrimitiveTypeFromNativeType(Class native_type) {
		TypeKind kind = native_types_to_primitive.get(native_type);
		if ( kind == null )
			throw new RuntimeException("Unsupported type " + native_type);
		return kind;
	}

	@Override
	public Signedness getSignednessFromType(Class type) {
		if ( ALuint.class.equals(type) )
			return Signedness.UNSIGNED;
		else if ( ALint.class.equals(type) )
			return Signedness.SIGNED;
		else if ( ALshort.class.equals(type) )
			return Signedness.SIGNED;
		else if ( ALbyte.class.equals(type) )
			return Signedness.SIGNED;
		else
			return Signedness.NONE;
	}

	@Override
	public String translateAnnotation(Class annotation_type) {
		if ( annotation_type.equals(ALuint.class) )
			return "i";
		else if ( annotation_type.equals(ALint.class) )
			return "i";
		else if ( annotation_type.equals(ALshort.class) )
			return "s";
		else if ( annotation_type.equals(ALbyte.class) )
			return "b";
		else if ( annotation_type.equals(ALfloat.class) )
			return "f";
		else if ( annotation_type.equals(ALdouble.class) )
			return "d";
		else if ( annotation_type.equals(ALboolean.class) || annotation_type.equals(ALvoid.class) )
			return "";
		else
			throw new RuntimeException(annotation_type + " is not allowed");
	}

	@Override
	public Class getNativeTypeFromPrimitiveType(TypeKind kind) {
		Class type;
		switch ( kind ) {
			case INT:
				type = ALint.class;
				break;
			case FLOAT:
				type = ALfloat.class;
				break;
			case DOUBLE:
				type = ALdouble.class;
				break;
			case SHORT:
				type = ALshort.class;
				break;
			case BYTE:
				type = ALbyte.class;
				break;
			case BOOLEAN:
				type = ALboolean.class;
				break;
			default:
				throw new RuntimeException(kind + " is not allowed");
		}
		return type;
	}

	private static Class[] getValidBufferTypes(Class type) {
		if ( type.equals(IntBuffer.class) )
			return new Class[] { ALenum.class, ALint.class, ALsizei.class, ALuint.class };
		else if ( type.equals(FloatBuffer.class) )
			return new Class[] { ALfloat.class };
		else if ( type.equals(ByteBuffer.class) )
			return new Class[] { ALboolean.class, ALbyte.class, ALvoid.class };
		else if ( type.equals(ShortBuffer.class) )
			return new Class[] { ALshort.class };
		else if ( type.equals(DoubleBuffer.class) )
			return new Class[] { ALdouble.class };
		else
			return new Class[] { };
	}

	private static Class[] getValidPrimitiveTypes(Class type) {
		if ( type.equals(int.class) )
			return new Class[] { ALenum.class, ALint.class, ALsizei.class, ALuint.class };
		else if ( type.equals(double.class) )
			return new Class[] { ALdouble.class };
		else if ( type.equals(float.class) )
			return new Class[] { ALfloat.class };
		else if ( type.equals(short.class) )
			return new Class[] { ALshort.class };
		else if ( type.equals(byte.class) )
			return new Class[] { ALbyte.class };
		else if ( type.equals(boolean.class) )
			return new Class[] { ALboolean.class };
		else if ( type.equals(void.class) )
			return new Class[] { ALvoid.class };
		else
			return new Class[] { };
	}

	@Override
	public void printCapabilitiesInit(final PrintWriter writer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCapabilities() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAPIUtilParam(boolean comma) {
		return "";
	}

	@Override
	public void printErrorCheckMethod(final PrintWriter writer, final ExecutableElement method, final String tabs) {
		writer.println(tabs + "Util.checkALError();");
	}

	@Override
	public String getRegisterNativesFunctionName() {
		return "extal_InitializeClass";
	}

	@Override
	public String getTypedefPostfix() {
		return "";
	}

	@Override
	public String getFunctionPrefix() {
		return "ALAPIENTRY";
	}

	@Override
	public void printNativeIncludes(PrintWriter writer) {
		writer.println("#include \"extal.h\"");
	}

	@Override
	public Class<? extends Annotation> getStringElementType() {
		return ALubyte.class;
	}

	@Override
	public Class<? extends Annotation> getStringArrayType() {
		return ALubyte.class;
	}

	@Override
	public Class<? extends Annotation> getByteBufferArrayType() {
		return ALubyte.class;
	}

	@Override
	public Class[] getValidAnnotationTypes(Class type) {
		Class[] valid_types;
		if ( Buffer.class.isAssignableFrom(type) )
			valid_types = getValidBufferTypes(type);
		else if ( type.isPrimitive() )
			valid_types = getValidPrimitiveTypes(type);
		else if ( type.equals(String.class) )
			valid_types = new Class[] { ALubyte.class };
		else
			valid_types = new Class[] { };
		return valid_types;
	}

	@Override
	public Class<? extends Annotation> getVoidType() {
		return ALvoid.class;
	}

	@Override
	public Class<? extends Annotation> getInverseType(Class type) {
		if ( ALuint.class.equals(type) )
			return ALint.class;
		else if ( ALint.class.equals(type) )
			return ALuint.class;
		else
			return null;
	}

	@Override
	public String getAutoTypeFromAnnotation(AnnotationMirror annotation) {
		return null;
	}
}
