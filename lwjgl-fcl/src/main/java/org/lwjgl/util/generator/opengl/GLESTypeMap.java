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

package org.lwjgl.util.generator.opengl;

/**
 *
 * OpenGL sepcific generator behaviour
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 3287 $
 * $Id: GLTypeMap.java 3287 2010-03-14 23:24:40Z spasi $
 */

import org.lwjgl.util.generator.NativeTypeTranslator;
import org.lwjgl.util.generator.PointerWrapper;
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

public class GLESTypeMap implements TypeMap {

	private static final Map<Class<? extends Annotation>, TypeKind> native_types_to_primitive;

	static {
		native_types_to_primitive = new HashMap<Class<? extends Annotation>, TypeKind>();
		native_types_to_primitive.put(GLbitfield.class, TypeKind.INT);
		native_types_to_primitive.put(GLclampf.class, TypeKind.FLOAT);
		native_types_to_primitive.put(GLfloat.class, TypeKind.FLOAT);
		native_types_to_primitive.put(GLint.class, TypeKind.INT);
		native_types_to_primitive.put(GLshort.class, TypeKind.SHORT);
		native_types_to_primitive.put(GLsizeiptr.class, TypeKind.LONG);
		native_types_to_primitive.put(GLuint.class, TypeKind.INT);
		native_types_to_primitive.put(GLboolean.class, TypeKind.BOOLEAN);
		native_types_to_primitive.put(GLchar.class, TypeKind.BYTE);
		native_types_to_primitive.put(GLhalf.class, TypeKind.SHORT);
		native_types_to_primitive.put(GLsizei.class, TypeKind.INT);
		native_types_to_primitive.put(GLushort.class, TypeKind.SHORT);
		native_types_to_primitive.put(GLbyte.class, TypeKind.BYTE);
		native_types_to_primitive.put(GLenum.class, TypeKind.INT);
		native_types_to_primitive.put(GLintptr.class, TypeKind.LONG);
		native_types_to_primitive.put(GLubyte.class, TypeKind.BYTE);
		native_types_to_primitive.put(GLvoid.class, TypeKind.BYTE);
		native_types_to_primitive.put(EGLint64NV.class, TypeKind.LONG);
		native_types_to_primitive.put(EGLuint64NV.class, TypeKind.LONG);
		native_types_to_primitive.put(GLint64.class, TypeKind.LONG);
		native_types_to_primitive.put(GLuint64.class, TypeKind.LONG);
	}

	@Override
	public TypeKind getPrimitiveTypeFromNativeType(Class<? extends Annotation> native_type) {
		TypeKind kind = native_types_to_primitive.get(native_type);
		if ( kind == null )
			throw new RuntimeException("Unsupported type " + native_type);
		return kind;
	}

	@Override
	public void printCapabilitiesInit(final PrintWriter writer) {
		writer.println("\t\tContextCapabilities caps = GLContext.getCapabilities();");
	}

	@Override
	public String getCapabilities() {
		return "caps";
	}

	@Override
	public String getAPIUtilParam(boolean comma) {
		return "";
	}

	@Override
	public void printErrorCheckMethod(final PrintWriter writer, final ExecutableElement method, final String tabs) {
		writer.println(tabs + "Util.checkGLError();");
	}

	@Override
	public String getRegisterNativesFunctionName() {
		return "extgl_InitializeClass";
	}

	@Override
	public Signedness getSignednessFromType(Class<? extends Annotation> type) {
		if ( GLuint.class.equals(type) )
			return Signedness.UNSIGNED;
		else if ( GLint.class.equals(type) )
			return Signedness.SIGNED;
		else if ( GLushort.class.equals(type) )
			return Signedness.UNSIGNED;
		else if ( GLshort.class.equals(type) )
			return Signedness.SIGNED;
		else if ( GLubyte.class.equals(type) )
			return Signedness.UNSIGNED;
		else if ( GLbyte.class.equals(type) )
			return Signedness.SIGNED;
		else if ( EGLuint64NV.class.equals(type) )
			return Signedness.UNSIGNED;
		else if ( EGLint64NV.class.equals(type) )
			return Signedness.SIGNED;
		else
			return Signedness.NONE;
	}

	@Override
	public String translateAnnotation(Class annotation_type) {
		if ( annotation_type.equals(GLuint64.class) || annotation_type.equals(GLint64.class) )
			return "i64";
		else if ( annotation_type.equals(GLuint.class) || annotation_type.equals(GLint.class) )
			return "i";
		else if ( annotation_type.equals(GLushort.class) || annotation_type.equals(GLshort.class) )
			return "s";
		else if ( annotation_type.equals(GLubyte.class) || annotation_type.equals(GLbyte.class) )
			return "b";
		else if ( annotation_type.equals(GLfloat.class) )
			return "f";
		else if ( annotation_type.equals(GLhalf.class) )
			return "h";
		else if ( annotation_type.equals(GLboolean.class) || annotation_type.equals(GLvoid.class) )
			return "";
		else if ( annotation_type.equals(EGLuint64NV.class) || annotation_type.equals(EGLint64NV.class) )
			return "l";
		else
			throw new RuntimeException(annotation_type + " is not allowed");
	}

	@Override
	public Class<? extends Annotation> getNativeTypeFromPrimitiveType(TypeKind kind) {
		Class<? extends Annotation> type;
		switch ( kind ) {
			case INT:
				type = GLint.class;
				break;
			case FLOAT:
				type = GLfloat.class;
				break;
			case SHORT:
				type = GLshort.class;
				break;
			case BYTE:
				type = GLbyte.class;
				break;
			case BOOLEAN:
				type = GLboolean.class;
				break;
			case LONG:
				type = GLint64.class;
				break;
			default:
				throw new RuntimeException(kind + " is not allowed");
		}
		return type;
	}

	@Override
	public Class<? extends Annotation> getVoidType() {
		return GLvoid.class;
	}

	@Override
	public Class<? extends Annotation> getStringElementType() {
		return GLubyte.class;
	}

	@Override
	public Class<? extends Annotation> getStringArrayType() {
		return GLchar.class;
	}

	@Override
	public Class<? extends Annotation> getByteBufferArrayType() {
		return GLubyte.class;
	}

	private static Class[] getValidBufferTypes(Class type) {
		if ( type.equals(IntBuffer.class) )
			return new Class[] { GLbitfield.class, GLenum.class, GLint.class, GLsizei.class, GLuint.class, GLvoid.class };
		else if ( type.equals(FloatBuffer.class) )
			return new Class[] { GLclampf.class, GLfloat.class };
		else if ( type.equals(ByteBuffer.class) )
			return new Class[] { GLboolean.class, GLbyte.class, GLchar.class, GLubyte.class, GLvoid.class };
		else if ( type.equals(ShortBuffer.class) )
			return new Class[] { GLhalf.class, GLshort.class, GLushort.class };
		else if ( type.equals(LongBuffer.class) )
			return new Class[] { GLint64.class, GLuint64.class, EGLint64NV.class, EGLuint64NV.class };
		else
			return new Class[] { };
	}

	private static Class[] getValidPrimitiveTypes(Class type) {
		if ( type.equals(long.class) )
			return new Class[] { GLintptr.class, GLsizeiptr.class, GLint64.class, GLuint64.class, EGLuint64NV.class, EGLint64NV.class };
		else if ( type.equals(int.class) )
			return new Class[] { GLbitfield.class, GLenum.class, GLint.class, GLuint.class, GLsizei.class };
		else if ( type.equals(float.class) )
			return new Class[] { GLclampf.class, GLfloat.class };
		else if ( type.equals(short.class) )
			return new Class[] { GLhalf.class, GLshort.class, GLushort.class };
		else if ( type.equals(byte.class) )
			return new Class[] { GLbyte.class, GLchar.class, GLubyte.class };
		else if ( type.equals(boolean.class) )
			return new Class[] { GLboolean.class };
		else if ( type.equals(void.class) )
			return new Class[] { GLvoid.class, GLreturn.class };
		else
			return new Class[] { };
	}

	@Override
	public String getTypedefPostfix() {
		return "GL_APICALL ";
	}

	@Override
	public String getFunctionPrefix() {
		return "GL_APIENTRY";
	}

	@Override
	public void printNativeIncludes(PrintWriter writer) {
		writer.println("#include \"extgl.h\"");
	}

	@Override
	public Class[] getValidAnnotationTypes(Class type) {
		Class[] valid_types;
		if ( Buffer.class.isAssignableFrom(type) )
			valid_types = getValidBufferTypes(type);
		else if ( type.isPrimitive() )
			valid_types = getValidPrimitiveTypes(type);
		else if ( String.class.equals(type) )
			valid_types = new Class[] { GLubyte.class };
		else if ( org.lwjgl.PointerWrapper.class.isAssignableFrom(type) )
			valid_types = new Class[] { PointerWrapper.class };
		else if ( void.class.equals(type) )
			valid_types = new Class[] { GLreturn.class };
		else
			valid_types = new Class[] { };
		return valid_types;
	}

	@Override
	public Class<? extends Annotation> getInverseType(Class<? extends Annotation> type) {
		if ( GLuint64.class.equals(type) )
			return GLint64.class;
		if ( GLuint.class.equals(type) )
			return GLint.class;
		else if ( GLint.class.equals(type) )
			return GLuint.class;
		else if ( GLushort.class.equals(type) )
			return GLshort.class;
		else if ( GLshort.class.equals(type) )
			return GLushort.class;
		else if ( GLubyte.class.equals(type) )
			return GLbyte.class;
		else if ( GLbyte.class.equals(type) )
			return GLubyte.class;
		else
			return null;
	}

	@Override
	public String getAutoTypeFromAnnotation(AnnotationMirror annotation) {
		Class annotation_class = NativeTypeTranslator.getClassFromType(annotation.getAnnotationType());
		if ( annotation_class.equals(GLint.class) )
			return "GLES20.GL_INT";
		else if ( annotation_class.equals(GLbyte.class) )
			return "GLES20.GL_BYTE";
		else if ( annotation_class.equals(GLshort.class) )
			return "GLES20.GL_SHORT";
		if ( annotation_class.equals(GLuint.class) )
			return "GLES20.GL_UNSIGNED_INT";
		else if ( annotation_class.equals(GLubyte.class) )
			return "GLES20.GL_UNSIGNED_BYTE";
		else if ( annotation_class.equals(GLushort.class) )
			return "GLES20.GL_UNSIGNED_SHORT";
		else if ( annotation_class.equals(GLfloat.class) )
			return "GLES20.GL_FLOAT";
		else
			return null;
	}
}
