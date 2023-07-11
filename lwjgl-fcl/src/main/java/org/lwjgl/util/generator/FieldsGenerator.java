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
package org.lwjgl.util.generator;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class FieldsGenerator {

	private static void validateField(VariableElement field) {
		// Check if field is "public static final"
		Set<Modifier> modifiers = field.getModifiers();
		if ( modifiers.size() != 3
		     || !modifiers.contains(Modifier.PUBLIC)
		     || !modifiers.contains(Modifier.STATIC)
		     || !modifiers.contains(Modifier.FINAL) ) {
			throw new RuntimeException("Field " + field.getSimpleName() + " is not declared public static final");
		}

		// Check suported types (int, long, float, String)
		TypeMirror field_type = field.asType();
		if ( "java.lang.String".equals(field_type.toString()) ) {
		} else if ( field_type instanceof PrimitiveType ) {
			PrimitiveType field_type_prim = (PrimitiveType)field_type;
			TypeKind field_kind = field_type_prim.getKind();
			if ( field_kind != TypeKind.INT
			     && field_kind != TypeKind.LONG
			     && field_kind != TypeKind.FLOAT
			     && field_kind != TypeKind.BYTE ) {
				throw new RuntimeException("Field " + field.getSimpleName() + " is not of type 'int', 'long', 'float' or 'byte' " + field_kind.toString());
			}
		} else {
			throw new RuntimeException("Field " + field.getSimpleName() + " is not a primitive type or String");
		}

		Object field_value = field.getConstantValue();
		if ( field_value == null ) {
			throw new RuntimeException("Field " + field.getSimpleName() + " has no initial value");
		}
	}

	private static void generateField(PrintWriter writer, VariableElement field, VariableElement prev_field, ProcessingEnvironment env) {
		validateField(field);

		Object value = field.getConstantValue();
		String field_value_string;
		Class field_value_class = value.getClass();
		if ( field_value_class.equals(Integer.class) ) {
			field_value_string = "0x" + Integer.toHexString((Integer)field.getConstantValue()).toUpperCase();
		} else if ( field_value_class.equals(Long.class) ) {
			field_value_string = "0x" + Long.toHexString((Long)field.getConstantValue()).toUpperCase() + 'L';
		} else if ( field_value_class.equals(Float.class) ) {
			field_value_string = field.getConstantValue() + "f";
		} else if ( value.getClass().equals(Byte.class) ) {
			field_value_string = "0x" + Integer.toHexString((Byte)field.getConstantValue()).toUpperCase();
		} else if ( field_value_class.equals(String.class) ) {
			field_value_string = "\"" + field.getConstantValue() + "\"";
		} else {
			throw new RuntimeException("Field is of unexpected type. This means there is a bug in validateField().");
		}

		boolean hadDoc = prev_field != null && env.getElementUtils().getDocComment(prev_field) != null;
		boolean hasDoc = env.getElementUtils().getDocComment(field) != null;
		boolean newBatch = prev_field == null || !prev_field.asType().equals(field.asType()) || (!hadDoc && env.getElementUtils().getDocComment(field) != null) || (hadDoc && hasDoc && !env.getElementUtils().getDocComment(prev_field).equals(env.getElementUtils().getDocComment(field)));

		// Print field declaration
		if ( newBatch ) {
			if ( prev_field != null ) {
				writer.println(";\n");
			}

			Utils.printDocComment(writer, field, env);
			writer.print("\tpublic static final " + field.asType().toString() + " " + field.getSimpleName() + " = " + field_value_string);
		} else {
			writer.print(",\n\t\t" + field.getSimpleName() + " = " + field_value_string);
		}
	}

	public static void generateFields(ProcessingEnvironment env, PrintWriter writer, Collection<? extends VariableElement> fields) {
		if ( 0 < fields.size() ) {
			writer.println();
			VariableElement prev_field = null;
			for ( VariableElement field : fields ) {
				generateField(writer, field, prev_field, env);
				prev_field = field;
			}
			writer.println(";");
		}
	}

}