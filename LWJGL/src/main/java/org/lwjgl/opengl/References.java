/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

class References extends BaseReferences {
	References(ContextCapabilities caps) {
		super(caps);
	}
	java.nio.Buffer ARB_matrix_palette_glMatrixIndexPointerARB_pPointer;
	java.nio.Buffer ARB_vertex_blend_glWeightPointerARB_pPointer;
	java.nio.Buffer EXT_fog_coord_glFogCoordPointerEXT_data;
	java.nio.Buffer EXT_secondary_color_glSecondaryColorPointerEXT_pPointer;
	java.nio.Buffer EXT_vertex_shader_glVariantPointerEXT_pAddr;
	java.nio.Buffer EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer;
	java.nio.Buffer GL11_glColorPointer_pointer;
	java.nio.Buffer GL11_glEdgeFlagPointer_pointer;
	java.nio.Buffer GL11_glNormalPointer_pointer;
	java.nio.Buffer GL11_glVertexPointer_pointer;
	java.nio.Buffer GL14_glFogCoordPointer_data;

	void copy(References references, int mask) {
		super.copy(references, mask);
		if ( (mask & GL11.GL_CLIENT_VERTEX_ARRAY_BIT) != 0 ) {
			this.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = references.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer;
			this.ARB_vertex_blend_glWeightPointerARB_pPointer = references.ARB_vertex_blend_glWeightPointerARB_pPointer;
			this.EXT_fog_coord_glFogCoordPointerEXT_data = references.EXT_fog_coord_glFogCoordPointerEXT_data;
			this.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = references.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer;
			this.EXT_vertex_shader_glVariantPointerEXT_pAddr = references.EXT_vertex_shader_glVariantPointerEXT_pAddr;
			this.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = references.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer;
			this.GL11_glColorPointer_pointer = references.GL11_glColorPointer_pointer;
			this.GL11_glEdgeFlagPointer_pointer = references.GL11_glEdgeFlagPointer_pointer;
			this.GL11_glNormalPointer_pointer = references.GL11_glNormalPointer_pointer;
			this.GL11_glVertexPointer_pointer = references.GL11_glVertexPointer_pointer;
			this.GL14_glFogCoordPointer_data = references.GL14_glFogCoordPointer_data;
		}
	}
	void clear() {
		super.clear();
		this.ARB_matrix_palette_glMatrixIndexPointerARB_pPointer = null;
		this.ARB_vertex_blend_glWeightPointerARB_pPointer = null;
		this.EXT_fog_coord_glFogCoordPointerEXT_data = null;
		this.EXT_secondary_color_glSecondaryColorPointerEXT_pPointer = null;
		this.EXT_vertex_shader_glVariantPointerEXT_pAddr = null;
		this.EXT_vertex_weighting_glVertexWeightPointerEXT_pPointer = null;
		this.GL11_glColorPointer_pointer = null;
		this.GL11_glEdgeFlagPointer_pointer = null;
		this.GL11_glNormalPointer_pointer = null;
		this.GL11_glVertexPointer_pointer = null;
		this.GL14_glFogCoordPointer_data = null;
	}
}
