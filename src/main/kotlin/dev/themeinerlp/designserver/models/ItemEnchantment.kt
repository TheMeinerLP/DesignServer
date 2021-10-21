package dev.themeinerlp.designserver.models

import kotlinx.serialization.Serializable

@Serializable
data class ItemEnchantment(
    val enchantment: String,
    val level: Short
)
