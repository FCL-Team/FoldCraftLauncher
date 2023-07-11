/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.opengl;

/**
 * Native bindings to the <a target="_blank" href="https://www.khronos.org/registry/OpenGL/extensions/AMD/AMD_seamless_cubemap_per_texture.txt">AMD_seamless_cubemap_per_texture</a> extension.
 * 
 * <p>In unextended OpenGL, cube maps are treated as sets of six, independent texture images. Once a face is selected from the set, it is treated exactly as
 * any other two-dimensional texture would be. When sampling linearly from the texture, all of the individual texels that would be used to to create the
 * final, bilinear sample values are taken from the same cube face. The normal, two-dimensional texture coordinate wrapping modes are honored. This
 * sometimes causes seams to appear in cube maps.</p>
 * 
 * <p>ARB_seamless_cube_map addresses this issue by providing a mechanism whereby an implementation could take each of the taps of a bilinear sample from a
 * different face, spanning face boundaries and providing seamless filtering from cube map textures. However, in ARB_seamless_cube_map, this feature was
 * exposed as a global state, affecting all bound cube map textures. It was not possible to mix seamless and per-face cube map sampling modes during
 * multisampling. Furthermore, if an application included cube maps that were meant to be sampled seamlessly and non-seamlessly, it would have to track
 * this state and enable or disable seamless cube map sampling as needed.</p>
 * 
 * <p>This extension addresses this issue and provides an orthogonal method for allowing an implementation to provide a per-texture setting for enabling
 * seamless sampling from cube maps.</p>
 * 
 * <p>Requires {@link ARBTextureCubeMap ARB_texture_cube_map}.</p>
 */
public final class AMDSeamlessCubemapPerTexture {

    /** Accepted by the {@code pname} parameter of TexParameterf, TexParameteri, TexParameterfv, TexParameteriv, GetTexParameterfv, and GetTexParameteriv. */
    public static final int GL_TEXTURE_CUBE_MAP_SEAMLESS = 0x884F;

    private AMDSeamlessCubemapPerTexture() {}

}