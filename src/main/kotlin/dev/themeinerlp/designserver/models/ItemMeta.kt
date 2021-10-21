package dev.themeinerlp.designserver.models

import kotlinx.serialization.Serializable

@Serializable
data class ItemMeta(
    val customModelData: Int?,
    val damage: Int?,
)
