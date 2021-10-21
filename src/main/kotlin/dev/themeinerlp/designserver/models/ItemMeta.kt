package dev.themeinerlp.designserver.models

import kotlinx.serialization.Serializable
import net.minestom.server.item.ItemHideFlag

@Serializable
open class ItemMeta(
    open val customModelData: Int?,
    open val damage: Int?,
    open val itemFlags: List<ItemHideFlag>?,
    open val enchantments: List<ItemEnchantment>?,
    open val attributes: List<JsonItemAttribute>?
)
