package dev.themeinerlp.designserver.models

import net.minestom.server.item.ItemHideFlag

open class PlayerHeadMeta(
    override val customModelData: Int?,
    override val damage: Int?,
    override val itemFlags: List<ItemHideFlag>?,
    override val enchantments: List<ItemEnchantment>?,
    override val attributes: List<JsonItemAttribute>?,
    val skullOwner: String?,
    val playerSkin: String?
) : ItemMeta(customModelData, damage, itemFlags, enchantments, attributes)
