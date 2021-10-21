package dev.themeinerlp.designserver.models

import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemHideFlag

@Serializable
data class ItemMeta(
    val customModelData: Int?,
    val damage: Int?,
    val itemFlags: List<ItemHideFlag>?,
    val enchantments: List<ItemEnchantment>?,
    val attributes: List<JsonItemAttribute>?
)
