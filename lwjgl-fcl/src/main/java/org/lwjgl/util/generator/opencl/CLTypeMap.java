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

package org.lwjgl.util.generator.opencl;

/**
 *
 * OpenCL specific generator behaviour
 *
 * @author Spasi
 */

import org.lwjgl.PointerBuffer;
import org.lwjgl.util.generator.*;
import org.lwjgl.util.generator.opengl.GLreturn;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.nio.*;
import java.util.HashMap;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

public class CLTypeMap implements TypeMap {

	private static final Map<Class, TypeKind> native_types_to_primitive;

	static {
		native_types_to_primitive = new HashMap<Class, TypeKind>();
		native_types_to_primitive.put(cl_void.class, TypeKind.BYTE);
		native_types_to_primitive.put(cl_byte.class, TypeKind.BYTE);
		native_types_to_primitive.put(cl_char.class, TypeKind.BYTE);
		native_types_to_primitive.put(cl_uchar.class, TypeKind.BYTE);
		native_types_to_primitive.put(cl_short.class, TypeKind.SHORT);
		native_types_to_primitive.put(cl_bool.class, TypeKind.INT);
		native_types_to_primitive.put(cl_int.class, TypeKind.INT);
		native_types_to_primitive.put(cl_uint.class, TypeKind.INT);
		native_types_to_primitive.put(cl_long.class, TypeKind.LONG);
		native_types_to_primitive.put(size_t.class, TypeKind.LONG);
		native_types_to_primitive.put(cl_bitfield.class, TypeKind.LONG);
		native_types_to_primitive.put(cl_float.class, TypeKind.FLOAT);
		native_types_to_primitive.put(cl_double.class, TypeKind.DOUBLE);
	}

	@Override
	public TypeKind getPrimitiveTypeFromNativeType(Class native_type) {
		TypeKind kind = native_types_to_primitive.get(native_type);
		if ( kind == null )
			throw new RuntimeException("Unsupported type " + native_type);
		return kind;
	}

	@Override
	public void printCapabilitiesInit(final PrintWriter writer) {
	}

	@Override
	public String getCapabilities() {
		return "CLCapabilities";
	}

	@Override
	public String getAPIUtilParam(boolean comma) {
		return "";
	}

	@Override
	public void printErrorCheckMethod(final PrintWriter writer, final ExecutableElement method, final String tabs) {
		final Check check = method.getAnnotation(Check.class);
		if ( check != null ) // Get the error code from an IntBuffer output parameter
			writer.println(tabs + "Util.checkCLError(" + check.value() + ".get(" + check.value() + ".position()));");
		else {
			final Class return_type = Utils.getJavaType(method.getReturnType());
			if ( return_type == int.class )
				writer.println(tabs + "Util.checkCLError(__result);");
			else {
				boolean hasErrCodeParam = false;
				for ( final VariableElement param : method.getParameters() ) {
					if ( "errcode_ret".equals(param.getSimpleName().toString()) && Utils.getJavaType(param.asType()) == IntBuffer.class ) {
						hasErrCodeParam = true;
						break;
					}
				}
				if ( hasErrCodeParam )
					throw new RuntimeException("A method is missing the @Check annotation: " + method.toString());
			}
		}
	}

	@Override
	public String getRegisterNativesFunctionName() {
		return "extcl_InitializeClass";
	}

	@Override
	public Signedness getSignednessFromType(Class type) {
		if ( cl_uint.class.equals(type) )
			return Signedness.UNSIGNED;
		else if ( cl_int.class.equals(type) )
			return Signedness.SIGNED;
		else
			return Signedness.NONE;
	}

	@Override
	public String translateAnnotation(Class annotation_type) {
		if ( annotation_type.equals(cl_uint.class) || annotation_type.equals(cl_int.class) )
			return "i";
		else if ( annotation_type.equals(cl_short.class) )
			return "s";
		else if ( annotation_type.equals(cl_byte.class) )
			return "b";
		else if ( annotation_type.equals(cl_float.class) )
			return "f";
		else if ( annotation_type.equals(cl_double.class) )
			return "d";
		else
			throw new RuntimeException(annotation_type + " is not allowed");
	}

	@Override
	public Class getNativeTypeFromPrimitiveType(TypeKind kind) {
		Class type;
		switch ( kind ) {
			case INT:
				type = cl_int.class;
				break;
			case DOUBLE:
				type = cl_double.class;
				break;
			case FLOAT:
				type = cl_float.class;
				break;
			case SHORT:
				type = cl_short.class;
				break;
			case BYTE:
				type = cl_byte.class;
				break;
			case LONG:
				type = cl_long.class;
				break;
			case BOOLEAN:
				type = cl_bool.class;
				break;
			default:
				throw new RuntimeException(kind + " is not allowed");
		}
		return type;
	}

	@Override
	public Class<? extends Annotation> getVoidType() {
		return cl_void.class;
	}

	@Override
	public Class<? extends Annotation> getStringElementType() {
		return cl_char.class;
	}

	@Override
	public Class<? extends Annotation> getStringArrayType() {
		return cl_char.class;
	}

	@Override
	public Class<? extends Annotation> getByteBufferArrayType() {
		return cl_uchar.class;
	}

	private static Class[] getValidBufferTypes(Class type) {
		if ( type.equals(IntBuffer.class) )
			return new Class[] { cl_int.class, cl_uint.class };
		else if ( type.equals(FloatBuffer.class) )
			return new Class[] { cl_float.class };
		else if ( type.equals(ByteBuffer.class) )
			return new Class[] { cl_byte.class, cl_char.class, cl_uchar.class, cl_void.class };
		else if ( type.equals(ShortBuffer.class) )
			return new Class[] { cl_short.class };
		else if ( type.equals(DoubleBuffer.class) )
			return new Class[] { cl_double.class };
		else if ( type.equals(LongBuffer.class) )
			return new Class[] { cl_long.class };
		else if ( type.equals(PointerBuffer.class) )
			return new Class[] { size_t.class };
		else
			return new Class[] { };
	}

	private static Class[] getValidPrimitiveTypes(Class type) {
		if ( type.equals(long.class) )
			return new Class[] { cl_long.class, size_t.class, cl_bitfield.class };
		else if ( type.equals(int.class) )
			return new Class[] { cl_int.class, cl_uint.class, cl_bool.class };
		else if ( type.equals(double.class) )
			return new Class[] { cl_double.class };
		else if ( type.equals(float.class) )
			return new Class[] { cl_float.class };
		else if ( type.equals(short.class) )
			return new Class[] { cl_short.class };
		else if ( type.equals(byte.class) )
			return new Class[] { cl_byte.class, cl_char.class, cl_uchar.class };
		else if ( type.equals(boolean.class) )
			return new Class[] { cl_bool.class };
		else if ( type.equals(void.class) )
			return new Class[] { cl_void.class };
		else
			return new Class[] { };
	}

	@Override
	public String getTypedefPostfix() {
		return "CL_API_ENTRY ";
	}

	@Override
	public String getFunctionPrefix() {
		return "CL_API_CALL";
	}

	@Override
	public void printNativeIncludes(PrintWriter writer) {
		writer.println("#include \"extcl.h\"");
	}

	@Override
	public Class[] getValidAnnotationTypes(Class type) {
		Class[] valid_types;
		if ( Buffer.class.isAssignableFrom(type) || PointerBuffer.class.isAssignableFrom(type) )
			valid_types = getValidBufferTypes(type);
		else if ( type.isPrimitive() )
			valid_types = getValidPrimitiveTypes(type);
		else if ( String.class.equals(type) )
			valid_types = new Class[] { cl_byte.class };
		else if ( org.lwjgl.PointerWrapper.class.isAssignableFrom(type) )
			valid_types = new Class[] { PointerWrapper.class };
		else if ( ByteBuffer[].class == type )
			valid_types = new Class[] { cl_char.class, cl_uchar.class };
		else if ( void.class.equals(type) )
			valid_types = new Class[] { GLreturn.class };
		else
			valid_types = new Class[] { };
		return valid_types;
	}

	@Override
	public Class<? extends Annotation> getInverseType(Class type) {
		return null;
	}

	@Override
	public String getAutoTypeFromAnnotation(AnnotationMirror annotation) {
		return null;
	}
}