package org.lwjgl.util.generator.opengl;

import org.lwjgl.util.generator.NativeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@NativeType
@Target({ ElementType.PARAMETER, ElementType.METHOD })
public @interface EGLint64NV {

}