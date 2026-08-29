@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.tungsten.fclcore.mod.modinfo

import kotlinx.serialization.Serializable

@Serializable
data class ForgeOldModMetadataLst(
    val modListVersion: Int = 0,
    val modList: List<ForgeOldModMetadata> = emptyList()
)
