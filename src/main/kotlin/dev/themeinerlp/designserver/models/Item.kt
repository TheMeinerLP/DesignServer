package dev.themeinerlp.designserver.models

import kotlinx.serialization.Serializable

@Serializable
data class Item(val fileName: String,
                val packageName: String,
                val className: String,
                val name: String,
                val lore: List<String>,
                val amount: Int,
                val material: String,
                var meta: ItemMeta? = null)
